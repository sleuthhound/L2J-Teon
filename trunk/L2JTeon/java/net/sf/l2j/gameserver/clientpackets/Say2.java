/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.clientpackets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.ChatFilterCache;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.FloodProtector;

/**
 * This class is describes Say2 packet
 *
 * @version $Revision: 1.16.2.12.2.7 $ $Date: 2005/04/11 10:06:11 $
 */
public final class Say2 extends L2GameClientPacket
{
    private static final String _C__38_SAY2 = "[C] 38 Say2";
    private static Logger _log = Logger.getLogger(Say2.class.getName());
    private static Logger _logChat = Logger.getLogger("chat");
    public final static int ALL = 0;
    public final static int SHOUT = 1; // !
    public final static int TELL = 2;
    public final static int PARTY = 3; // #
    public final static int CLAN = 4; // @
    public final static int GM = 5; // gmchat
    public final static int PETITION_PLAYER = 6; // used for petition
    public final static int PETITION_GM = 7; // used for petition
    public final static int TRADE = 8; // +
    public final static int ALLIANCE = 9; // $
    public final static int ANNOUNCEMENT = 10; // announce
    public final static int PARTYROOM_ALL = 16; // (Red)
    public final static int PARTYROOM_COMMANDER = 15; // (Yellow)
    public final static int HERO_VOICE = 17;
    private final static String[] CHAT_NAMES = { "ALL  ", "SHOUT", "TELL ", "PARTY", "CLAN ", "GM   ", "PETITION_PLAYER", "PETITION_GM", "TRADE", "ALLIANCE", "ANNOUNCEMENT", "WILLCRASHCLIENT:)", "FAKEALL?", "FAKEALL?", "FAKEALL?", "PARTYROOM_ALL", "PARTYROOM_COMMANDER", "HERO_VOICE" };
    private String _text;
    private int _type;
    private String _target;

    @Override
    protected void readImpl()
    {
	_text = readS();
	try
	{
	    _type = readD();
	} catch (BufferUnderflowException e)
	{
	    _type = CHAT_NAMES.length;
	}
	_target = _type == TELL ? readS() : null;
    }

    @Override
    protected void runImpl()
    {
	if (Config.DEBUG)
	{
	    _log.info("Say2: Msg Type = '" + _type + "' Text = '" + _text + "'.");
	}
	// is message type valid?
	if ((_type < 0) || (_type >= CHAT_NAMES.length))
	{
	    _log.warning("Say2: Invalid type: " + _type);
	    return;
	}
	// getting char instance
	L2PcInstance activeChar = getClient().getActiveChar();
	// words from nowere?
	if (activeChar == null)
	{
	    _log.warning("[Say2.java] Active Character is null.");
	    return;
	}
    if (_text.length() >= 100) 
    {  
        _log.warning("Say2: Max input exceeded.");  
        return;
    }
	// player chat banned?
	if (activeChar.isChatBanned())
	{
	    activeChar.checkBanChat(true);
	    return;
	}
	// player jailed?
	if (activeChar.isInJail() && Config.JAIL_DISABLE_CHAT)
	{
	    if ((_type == TELL) || (_type == SHOUT) || (_type == TRADE) || (_type == HERO_VOICE))
	    {
		activeChar.sendMessage("You Have been Chat Banned");
		return;
	    }
	}
	// is it GM petition?
	if ((_type == PETITION_PLAYER) && activeChar.isGM())
	{
	    _type = PETITION_GM;
	}
	// must we log chat text?
	if (Config.LOG_CHAT)
	{
	    LogRecord record = new LogRecord(Level.INFO, _text);
	    record.setLoggerName("chat");
	    if (_type == TELL)
	    {
		record.setParameters(new Object[] { CHAT_NAMES[_type], "[" + activeChar.getName() + " to " + _target + "]" });
	    } else
	    {
		record.setParameters(new Object[] { CHAT_NAMES[_type], "[" + activeChar.getName() + "]" });
	    }
	    _logChat.log(record);
	}
	// must we use chat filter?
	if (Config.USE_CHAT_FILTER && (_type != ALLIANCE) && (_type != CLAN) && (_type != TELL))
	{
	    String filterText = ChatFilterCache.filterText(_text);
	    if ((filterText != _text) && (Config.CHAT_FILTER_PUNISHMENT > 0) && (Config.CHAT_FILTER_PUNISHMENT_TIME > 0))
	    {
		switch (Config.CHAT_FILTER_PUNISHMENT)
		{
		case 1:
		    activeChar.setChatBanned(true, Config.CHAT_FILTER_PUNISHMENT_TIME * 60, "Chat Filter");
		    break;
		case 2:
		    activeChar.setInJail(true, Config.CHAT_FILTER_PUNISHMENT_TIME);
		    break;
		}
		return;
	    }
	}
	// prepare packet
	CreatureSay cs = new CreatureSay(activeChar.getObjectId(), _type, activeChar.getName(), _text);
	switch (_type)
	{
	case TELL:
	    L2PcInstance receiver = L2World.getInstance().getPlayer(_target);
	    if ((receiver != null) && !BlockList.isBlocked(receiver, activeChar))
	    {
		if (activeChar != receiver)
		{
		    // can we send message?
		    if (!receiver.getMessageRefusal())
		    {
			receiver.sendPacket(cs);
			activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), _type, "->" + receiver.getName(), _text));
			// receiver is in jail?
			if (Config.JAIL_DISABLE_CHAT && receiver.isInJail())
			{
			    activeChar.sendMessage(receiver.getName() + " is in jail.");
			    return;
			}
			if (receiver.isAway())
			{
				activeChar.sendMessage("Player is Away try again later.");
			    	return;
			}
			// receiver's chat is banned?
			if (receiver.isChatBanned())
			{
			    activeChar.sendMessage(receiver.getName() + "'s chat is banned.");
			    return;
			}
		    } else
		    {
			activeChar.sendPacket(new SystemMessage(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE));
		    }
		} else
		{
		    activeChar.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
		}
	    } else
	    {
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_IS_NOT_ONLINE);
		sm.addString(_target);
		activeChar.sendPacket(sm);
		sm = null;
	    }
	    break;
	case SHOUT:
	    if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("gm") && activeChar.isGM()))
	    {
		int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
		    if (region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY()))
		    {
			player.sendPacket(cs);
		    }
		}
		File file = new File(Config.IRC_IGTOIRC_FILE);
		FileWriter save = null;
		try
		{
		    save = new FileWriter(file);
		    save.write("" + "\r\n");
		    save.write(activeChar.getName() + "_IG" + ": " + _text + "\r\n");
		    save.flush();
		    save.close();
		    save = null;
		} catch (IOException e)
		{
		    _log.warning("saving IngameChat has failed: " + e);
		}
	    } else if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("global"))
	    {
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
		    player.sendPacket(cs);
		}
	    }
	    break;
	case TRADE:
	    if (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("gm") && activeChar.isGM()))
	    {
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
		    player.sendPacket(cs);
		}
	    } else if (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("limited"))
	    {
		int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
		    if (region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY()))
		    {
			player.sendPacket(cs);
		    }
		}
	    }
	    break;
	case ALL:
	    if (_text.startsWith("."))
	    {
		StringTokenizer st = new StringTokenizer(_text);
		String command;
		String params;
		if (st.countTokens() >= 2)
		{
		    command = st.nextToken().substring(1);
		    params = st.nextToken();
		} else
		{
		    command = _text.substring(1).trim();
		    params = "";
		}
		if (command.length() > 0)
		{
		    IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
		    if (vch != null)
		    {
			vch.useVoicedCommand(command, activeChar, params);
		    } else
		    {
			_log.warning("This command: '" + command + "' was attempted but does not exist.");
		    }
		}
		command = null;
		params = null;
	    } else
	    {
		for (L2PcInstance player : activeChar.getKnownList().getKnownPlayers().values())
		{
		    if (activeChar.isInsideRadius(player, 1250, false, true))
		    {
			player.sendPacket(cs);
		    }
		}
		activeChar.sendPacket(cs);
	    }
	    break;
	case CLAN:
	    if (activeChar.getClan() != null)
	    {
		activeChar.getClan().broadcastToOnlineMembers(cs);
	    }
	    break;
	case ALLIANCE:
	    if (activeChar.getClan() != null)
	    {
		activeChar.getClan().broadcastToOnlineAllyMembers(cs);
	    }
	    break;
	case PARTY:
	    if (activeChar.isInParty())
	    {
		activeChar.getParty().broadcastToPartyMembers(cs);
	    }
	    break;
	case PETITION_PLAYER:
	case PETITION_GM:
	    if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar))
	    {
		activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_IN_PETITION_CHAT));
		break;
	    }
	    PetitionManager.getInstance().sendActivePetitionMessage(activeChar, _text);
	    break;
	case PARTYROOM_ALL:
	    if (activeChar.isInParty())
	    {
		if (activeChar.getParty().isInCommandChannel() && activeChar.getParty().isLeader(activeChar))
		{
		    activeChar.getParty().getCommandChannel().broadcastToChannelMembers(cs);
		}
	    }
	    break;
	case PARTYROOM_COMMANDER:
	    if (activeChar.isInParty())
	    {
		if (activeChar.getParty().isInCommandChannel() && activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar))
		{
		    activeChar.getParty().getCommandChannel().broadcastToChannelMembers(cs);
		}
	    }
	    break;
	case HERO_VOICE:
	    if (activeChar.isHero() || activeChar.isGM())
	    {
		if (!FloodProtector.getInstance().tryPerformAction(activeChar.getObjectId(), FloodProtector.PROTECTED_HEROVOICE))
		{
		    activeChar.sendMessage("Action failed. Heroes are only able to speak in the global channel once every 10 seconds.");
		    return;
		}
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
		    if (!BlockList.isBlocked(player, activeChar))
		    {
			player.sendPacket(cs);
		    }
		}
	    }
	    break;
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
     */
    @Override
    public String getType()
    {
	return _C__38_SAY2;
    }
}
