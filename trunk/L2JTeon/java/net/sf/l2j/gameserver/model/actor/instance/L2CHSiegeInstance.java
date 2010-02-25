/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import java.text.SimpleDateFormat;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortressofTheDeadManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2CHSiegeInstance extends L2NpcInstance
{
	public L2CHSiegeInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onAction(L2PcInstance player)
	{
		if (!canTarget(player))
			return;
		// Check if the L2PcInstance already target the L2NpcInstance
		if (this != player.getTarget())
		{
			// Set the target of the L2PcInstance player
			player.setTarget(this);
			// Send a Server->Client packet MyTargetSelected to the L2PcInstance
			// player
			MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
			player.sendPacket(my);
			// Send a Server->Client packet ValidateLocation to correct the
			// L2NpcInstance position and heading on the client
			player.sendPacket(new ValidateLocation(this));
		}
		else
		{
			// Calculate the distance between the L2PcInstance and the
			// L2NpcInstance
			if (!canInteract(player))
				// Notify the L2PcInstance AI with AI_INTENTION_INTERACT
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
			else
				showChatWindow(player, 0);
		}
		// Send a Server->Client ActionFailed to the L2PcInstance in order to
		// avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (command.startsWith("Chat"))
		{
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			showChatWindow(player, val);
		}
		else if (command.startsWith("Quest"))
		{
			String quest = "";
			try
			{
				quest = command.substring(5).trim();
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			if (quest.length() == 0)
				showQuestWindow(player);
			else
				showQuestWindow(player, quest);
		}
		else if (command.startsWith("Registration"))
		{
			L2Clan Clan = player.getClan();
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			String str;
			str = "<html><body>Newspaper!<br>";
			switch (getTemplate().getNpcId())
			{
			case 35639:
				if (!FortressofTheDeadManager.getInstance().isRegistrationPeriod())
				{
					showChatWindow(player, 3);
					return;
				}
				if (Clan == null || Clan.getLeaderName() != player.getName() || Clan.getLevel() < 4)
				{
					showChatWindow(player, 1);
					return;
				}
				if (FortressofTheDeadManager.getInstance().clanhall.getOwnerClan() == Clan)
				{
					str += "Your clan is already registered for the siege, what more do you want from me?<br>";
				}
				else
				{
					if (FortressofTheDeadManager.getInstance().isClanOnSiege(Clan))
					{
						str += "Your clan is already registered for the siege, what more do you want from me?<br>";
						str += "<a action=\"bypass -h npc_%objectId%_UnRegister\">Unsubscribe</a><br>";
					}
					else
					{
						int res = FortressofTheDeadManager.getInstance().registerClanOnSiege(player, Clan);
						if (res == 0)
						{
							str += "Your clan : <font color=\"LEVEL\">" + player.getClan().getName() + "</font>, successfully registered for the siege clan hall.<br>";
							str += "Now you need to select no more than 18 igokov who will take part in the siege, a member of your clan.<br>";
						}
						else if (res == 1)
						{
							str += "You have participation in the siege";
						}
						else if (res == 2)
						{
							str += "Unfortunately, you are late. Five tribal leaders have already filed an application for registration.<br>";
						}
					}
				}
				break;
			}
			str += "</body></html>";
			html.setHtml(str);
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}
		else if (command.startsWith("UnRegister"))
		{
			L2Clan Clan = player.getClan();
			if (Clan == null || Clan.getLeaderName() != player.getName() || Clan.getLevel() < 4)
			{
				_log.warning("Attention!!! player " + player.getName() + " use packet hack, try unregister clan.");
				return;
			}
			if (!FortressofTheDeadManager.getInstance().isRegistrationPeriod())
			{
				showChatWindow(player, 3);
				return;
			}
			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			String str;
			if (FortressofTheDeadManager.getInstance().isClanOnSiege(Clan))
			{
				if (FortressofTheDeadManager.getInstance().unRegisterClan(Clan))
				{
					str = "<html><body>Newspaper!<br>";
					str += "Your clan : <font color=\"LEVEL\">" + player.getClan().getName() + "</font>, successfully removed from the register at the siege clan hall.<br>";
					str += "</body></html>";
					html.setHtml(str);
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
				}
			}
			else
				_log.warning("Attention!!! player " + player.getName() + " use packet hack, try unregister clan.");
		}
	}

	@Override
	public void showChatWindow(L2PcInstance player, int val)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		long startSiege = 0;
		int npcId = getTemplate().getNpcId();
		String filename;
		if (val == 0)
			filename = "data/html/siege/clanhall/" + npcId + ".htm";
		else
			filename = "data/html/siege/clanhall/" + npcId + "-" + val + ".htm";
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		if (npcId == 35639)
		{
			ClanHall clanhall = null;
			String clans = "";
			clans += "<table width=280 border=0>";
			int clanCount = 0;
			switch (npcId)
			{
			case 35639:
				clanhall = ClanHallManager.getInstance().getClanHallById(64);
				startSiege = FortressofTheDeadManager.getInstance().getSiegeDate().getTimeInMillis();
				for (String a : FortressofTheDeadManager.getInstance().getRegisteredClans())
				{
					clanCount++;
					clans += "<tr><td><font color=\"LEVEL\">" + a + "</font>  " + FortressofTheDeadManager.getInstance().getClansCount(a) + "</td></tr>";
				}
				break;
			}
			while (clanCount < 5)
			{
				clans += "<tr><td></td></tr>";
				clanCount++;
			}
			clans += "</table>";
			html.replace("%clan%", String.valueOf(clans));
			L2Clan clan = clanhall.getOwnerClan();
			String clanName;
			if (clan == null)
				clanName = "NPC";
			else
				clanName = clan.getName();
			html.replace("%clanname%", String.valueOf(clanName));
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		html.replace("%SiegeDate%", String.valueOf(format.format(startSiege)));
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
	}

	private boolean validateCondition(L2PcInstance player)
	{
		return true;
	}
}