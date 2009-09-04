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

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import net.sf.l2j.Base64;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.TaskPriority;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.communitybbs.Manager.RegionBBSManager;
import net.sf.l2j.gameserver.datatables.CharSchemesTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.PcColorTable;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.CrownManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.FortSiegeManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2ClassMasterInstance;
import net.sf.l2j.gameserver.model.base.ClassLevel;
import net.sf.l2j.gameserver.model.base.PlayerClass;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Couple;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.entity.L2Event;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.CTF;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.TvTEvent;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.VIP;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.Die;
import net.sf.l2j.gameserver.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.serverpackets.ExStorageMaxCount;
import net.sf.l2j.gameserver.serverpackets.FriendList;
import net.sf.l2j.gameserver.serverpackets.HennaInfo;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.serverpackets.PledgeSkillList;
import net.sf.l2j.gameserver.serverpackets.PledgeStatusChanged;
import net.sf.l2j.gameserver.serverpackets.QuestList;
import net.sf.l2j.gameserver.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.serverpackets.SignsSky;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;
import net.sf.l2j.gameserver.services.WindowService;
import net.sf.l2j.gameserver.util.FloodProtector;

/**
 * Enter World Packet Handler
 * <p>
 * <p>
 * 0000: 03
 * <p>
 * packet format rev656 cbdddd
 * <p>
 *
 * @version $Revision: 1.16.2.1.2.7 $ $Date: 2005/03/29 23:15:33 $
 */
public class EnterWorld extends L2GameClientPacket
{
    private static final String _C__03_ENTERWORLD = "[C] 03 EnterWorld";
    private static Logger _log = Logger.getLogger(EnterWorld.class.getName());

    public TaskPriority getPriority()
    {
	return TaskPriority.PR_URGENT;
    }

    @Override
    protected void readImpl()
    {
	// this is just a trigger packet. it has no content
    }

    @Override
    protected void runImpl()
    {
	L2PcInstance activeChar = getClient().getActiveChar();
	if (activeChar == null)
	{
	    _log.warning("EnterWorld failed! activeChar is null...");
	    getClient().closeNow();
	    return;
	}
	// Register in flood protector
	FloodProtector.getInstance().registerNewPlayer(activeChar.getObjectId());
	if (L2World.getInstance().findObject(activeChar.getObjectId()) != null)
	{
	    if (Config.DEBUG)
	    {
		_log.warning("User already exist in OID map! User " + activeChar.getName() + " is character clone");
		// activeChar.closeNetConnection();
	    }
	}
	if (activeChar.isGM())
	{
	    if (Config.GM_STARTUP_INVULNERABLE && ((!Config.ALT_PRIVILEGES_ADMIN && (activeChar.getAccessLevel() >= Config.GM_GODMODE)) || (Config.ALT_PRIVILEGES_ADMIN && AdminCommandHandler.getInstance().checkPrivileges(activeChar, "admin_invul"))))
	    {
		activeChar.setIsInvul(true);
	    }
	    if (Config.GM_STARTUP_INVISIBLE && ((!Config.ALT_PRIVILEGES_ADMIN && (activeChar.getAccessLevel() >= Config.GM_GODMODE)) || (Config.ALT_PRIVILEGES_ADMIN && AdminCommandHandler.getInstance().checkPrivileges(activeChar, "admin_invisible"))))
	    {
		activeChar.getAppearance().setInvisible();
	    }
	    if (Config.GM_STARTUP_SILENCE && ((!Config.ALT_PRIVILEGES_ADMIN && (activeChar.getAccessLevel() >= Config.GM_MENU)) || (Config.ALT_PRIVILEGES_ADMIN && AdminCommandHandler.getInstance().checkPrivileges(activeChar, "admin_silence"))))
	    {
		activeChar.setMessageRefusal(true);
	    }
	    if (Config.GM_STARTUP_AUTO_LIST && ((!Config.ALT_PRIVILEGES_ADMIN && (activeChar.getAccessLevel() >= Config.GM_MENU)) || (Config.ALT_PRIVILEGES_ADMIN && AdminCommandHandler.getInstance().checkPrivileges(activeChar, "admin_gmliston"))))
	    {
		GmListTable.getInstance().addGm(activeChar, false);
	    } else
	    {
		GmListTable.getInstance().addGm(activeChar, true);
	    }
	    if (Config.GM_NAME_COLOR_ENABLED)
	    {
		if (activeChar.getAccessLevel() >= 100)
		{
		    activeChar.getAppearance().setNameColor(Config.ADMIN_NAME_COLOR);
		} else if (activeChar.getAccessLevel() >= 75)
		{
		    activeChar.getAppearance().setNameColor(Config.GM_NAME_COLOR);
		}
		if (Config.SHOW_GM_LOGIN)
		{
		    String name = activeChar.getName();
		    String text = "GameMaster " + name + " Is Currently Online.";
		    Announcements.getInstance().announceToAll(text);
		}
	    }
	}
	if (Config.PLAYER_SPAWN_PROTECTION > 0)
	{
	    activeChar.setProtection(true);
	}
	activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
	if (L2Event.active && L2Event.connectionLossData.containsKey(activeChar.getName()) && L2Event.isOnEvent(activeChar))
	{
	    L2Event.restoreChar(activeChar);
	} else if (L2Event.connectionLossData.containsKey(activeChar.getName()))
	{
	    L2Event.restoreAndTeleChar(activeChar);
	}
	if (SevenSigns.getInstance().isSealValidationPeriod())
	{
	    sendPacket(new SignsSky());
	}
	// buff and status icons
	if (Config.STORE_SKILL_COOLTIME)
	{
	    activeChar.restoreEffects();
	}
	activeChar.sendPacket(new EtcStatusUpdate(activeChar));
	// engage and notify Partner
	if (Config.ALLOW_WEDDING)
	{
	    engage(activeChar);
	    notifyPartner(activeChar, activeChar.getPartnerId());
	    // Check if player is maried and remove if necessary Cupid's Bow
	    if (!activeChar.isMaried())
	    {
		L2ItemInstance item = activeChar.getInventory().getItemByItemId(9140);
		// Remove Cupid's Bow
		if (item != null)
		{
		    activeChar.destroyItem("Removing Cupid's Bow", item, activeChar, true);
		    activeChar.getInventory().updateDatabase();
		    // Log it
		    _log.info("Character " + activeChar.getName() + " of account " + activeChar.getAccountName() + " got Cupid's Bow removed.");
		}
	    }
	}
	if (activeChar.getAllEffects() != null)
	{
	    for (L2Effect e : activeChar.getAllEffects())
	    {
		if (e.getEffectType() == L2Effect.EffectType.HEAL_OVER_TIME)
		{
		    activeChar.stopEffects(L2Effect.EffectType.HEAL_OVER_TIME);
		    activeChar.removeEffect(e);
		}
		if (e.getEffectType() == L2Effect.EffectType.COMBAT_POINT_HEAL_OVER_TIME)
		{
		    activeChar.stopEffects(L2Effect.EffectType.COMBAT_POINT_HEAL_OVER_TIME);
		    activeChar.removeEffect(e);
		}
	    }
	}
	// apply augmentation boni for equipped items
	for (L2ItemInstance temp : activeChar.getInventory().getAugmentedItems())
	{
	    if ((temp != null) && temp.isEquipped())
	    {
		temp.getAugmentation().applyBoni(activeChar);
	    }
	}
	// Expand Skill
	ExStorageMaxCount esmc = new ExStorageMaxCount(activeChar);
	activeChar.sendPacket(esmc);
	activeChar.getMacroses().sendUpdate();
	// checkup and delete delayed donator rented items
	if (Config.DONATOR_DELETE_RENTED_ITEMS)
	{
	    activeChar.donatorDeleteDelayedRentedItems();
	}
	// sends general packets
	sendPacket(new UserInfo(activeChar));
	sendPacket(new HennaInfo(activeChar));
	sendPacket(new FriendList(activeChar));
	sendPacket(new ItemList(activeChar, false));
	sendPacket(new ShortCutInit(activeChar));
	SystemMessage sm = new SystemMessage(SystemMessageId.WELCOME_TO_LINEAGE);
	sendPacket(sm);
	if (Config.SHOW_L2J_LICENSE)
	{
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString(getText("VGhpcyBzZXJ2ZXIgaXMgcnVubmluZyB0aGUgTDJKT25lbyBkaXN0cmlidXRpb24gb2YgTDJqLg=="));
	    sendPacket(sm);
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString(getText("TDJKT25lbyBEZXYgVGVhbTpEYVJrUmFHZSwgc2NodXJzaW4sIEFobWVkLA=="));
	    sm.addString(getText("bmVvRGV2aWwsIFNseWNlciwgQmFvcmMsIFZpY2UsIEV6RXJhbCwgTGlxdWlkSWNlLg=="));
	    sendPacket(sm);
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString(getText("TDJKIHdhcyBmb3VuZGVkIGJ5IEwyQ2hlZiBhbmQgdGhlIEwySiBUZWFtLg=="));
	    sm.addString(getText("IEwySiB0ZWFtLg=="));
	    sendPacket(sm);
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString(getText("VmlzaXQgaHR0cDovL3d3dy5sMmpzZXJ2ZXIuY29t"));
	    sm.addString(getText("ICBmb3Igc3VwcG9ydC4="));
	    sendPacket(sm);
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString(getText("V2VsY29tZSB0byA="));
	    sm.addString(LoginServerThread.getInstance().getServerName());
	    sendPacket(sm);
	    if (Config.SERVER_VERSION != null)
	    {
		sm = new SystemMessage(SystemMessageId.S1_S2);
		sm.addString(getText("TDJKIFNlcnZlciBWZXJzaW9uOg==") + "   " + Config.SERVER_VERSION);
		sendPacket(sm);
		sm = new SystemMessage(SystemMessageId.S1_S2);
		sm.addString(getText("TDJKIFNlcnZlciBCdWlsZCBEYXRlOg==") + " " + Config.SERVER_BUILD_DATE);
		sendPacket(sm);
	    }
	}
	sm = null;
	SevenSigns.getInstance().sendCurrentPeriodMsg(activeChar);
	Announcements.getInstance().showAnnouncements(activeChar);
	CrownManager.getInstance().checkCrowns(activeChar);
	// restore info about chat ban
	activeChar.checkBanChat(false);
	// restore info about auto herbs loot
	if (Config.ALLOW_AUTOHERBS_CMD)
	{
	    activeChar.getAutoLootHerbs();
	}
	// restore info about withdraw state
	if (Config.ALLOW_WITHDRAW_CWH_CMD)
	{
	    activeChar.getCanWithdrawCWH();
	}
	// donator's "Hello!"
	if (activeChar.isDonator())
	{
	    activeChar.getAppearance().setNameColor(Config.DONATOR_NAME_COLOR);
	    activeChar.sendMessage("Welcome " + activeChar.getName() + " to our L][ Server!");
	    activeChar.sendMessage("Enjoy your Stay Donator!");
	}
	// Faction Engine by DaRkRaGe
	if (activeChar.isKoof() && Config.ENABLE_FACTION_KOOFS_NOOBS)
	{
	    activeChar.getAppearance().setNameColor(Config.KOOFS_NAME_COLOR);
	    activeChar.sendMessage("Welcome " + activeChar.getName() + " u Are fighiting for " + Config.KOOFS_NAME_TEAM + "  Faction");
	}
	if (activeChar.isNoob() && Config.ENABLE_FACTION_KOOFS_NOOBS)
	{
	    activeChar.getAppearance().setNameColor(Config.NOOBS_NAME_COLOR);
	    activeChar.sendMessage("Welcome " + activeChar.getName() + " u Are fighiting for " + Config.NOOBS_NAME_TEAM + " Faction");
	}
	if (Config.ONLINE_PLAYERS_AT_STARTUP)
	{
	    int PLAYERS_ONLINE = L2World.getInstance().getAllPlayers().size() + Config.PLAYERS_ONLINE_TRICK;
	    sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString("Players online: ");
	    sm.addNumber(PLAYERS_ONLINE);
	    sendPacket(sm);
	}
	Quest.playerEnter(activeChar);
	activeChar.sendPacket(new QuestList());
	// sends server news
	String serverNews = HtmCache.getInstance().getHtm("data/html/servnews.htm");
	if (serverNews != null)
	{
	    WindowService.sendWindow(activeChar, "data/html/", "servnews.htm");
	}
	// check any poending petitions
	PetitionManager.getInstance().checkPetitionMessages(activeChar);
	// sends welcome htm if enabled.
	if (Config.SHOW_HTML_WELCOME)
	{
	    WindowService.sendWindow(activeChar, "data/html/", "TeonInfo.htm");
	}
	// sends newbie htm if enabled.
	if (Config.SHOW_HTML_NEWBIE && (activeChar.getLevel() < Config.LEVEL_HTML_NEWBIE))
	{
	    WindowService.sendWindow(activeChar, "data/html/", "newbie.htm");
	}
	// check player for unlegit skills
	if (Config.CHECK_SKILLS_ON_ENTER && !Config.ALT_GAME_SKILL_LEARN)
	{
	    activeChar.checkAllowedSkills();
	}
	// send user info again .. just like the real client
	// sendPacket(ui);
	if ((activeChar.getClanId() != 0) && (activeChar.getClan() != null))
	{
	    sendPacket(new PledgeShowMemberListAll(activeChar.getClan(), activeChar));
	    sendPacket(new PledgeStatusChanged(activeChar.getClan()));
	}
	// no broadcast needed since the player will already spawn dead to
	// others
	if (activeChar.isAlikeDead())
	{
	    sendPacket(new Die(activeChar));
	}
	if (Config.ALLOW_WATER)
	{
	    activeChar.checkWaterState();
	}
	if ((Hero.getInstance().getHeroes() != null) && Hero.getInstance().getHeroes().containsKey(activeChar.getObjectId()))
	{
	    activeChar.setHero(true);
	}
	// set clan class
	setPledgeClass(activeChar);
	// add char to online characters
	activeChar.setOnlineStatus(true);
	// notify friend of charater login
	notifyFriends(activeChar);
	// notify clan of charater login
	notifyClanMembers(activeChar);
	// notify sponsor charater login
	notifySponsorOrApprentice(activeChar);
	// notify castle owner charater login
	notifyCastleOwner(activeChar);
	// player l2pcinstance enter
	activeChar.onPlayerEnter();
	// player l2pcinstance enter
	checkCrown(activeChar);
	/*
	 * Commented out by Rayan: uneeded, all players independing on faction
	 * are moved to stadia.
	 *
	 */
	/*
	 * if (activeChar.isNoob() &&
	 * Olympiad.getInstance().playerInStadia(activeChar)) {
	 * activeChar.teleToLocation(45356, 48939, -3060);
	 * activeChar.sendMessage("You have been teleported Back to your Faction
	 * Base."); }
	 *
	 * if (activeChar.isKoof() &&
	 * Olympiad.getInstance().playerInStadia(activeChar)) {
	 * activeChar.teleToLocation(12408, 16605, -4585);
	 * activeChar.sendMessage("You have been teleported Back to your Faction
	 * Base."); }
	 */
	if (Olympiad.getInstance().playerInStadia(activeChar))
	{
	    activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);
	    activeChar.sendMessage("You have been teleported to the nearest town due to you being in an Olympiad Stadium");
	}
	if (DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), false))
	{
	    DimensionalRiftManager.getInstance().teleportToWaitingRoom(activeChar);
	}
	if (!activeChar.isGM() && (activeChar.getSiegeState() < 2) && activeChar.isInsideZone(L2Character.ZONE_SIEGE))
	{
	    // Attacker or spectator logging in to a siege zone. Actually
	    // should
	    // be checked for inside castle only?
	    activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);
	    activeChar.sendMessage("You have been teleported to the nearest town due to you being in siege zone");
	}
	/** More configuration and checks on this later, Ahmed. */
	if (Config.SET_LVL_ON_START && (activeChar.getLevel() >= 1) && (activeChar.getLevel() < 80))
	{
	    if (!Config.HIGH_LEVEL_ON_START_FOR_SUBCLASS && activeChar.isSubClassActive())
		return;
	    activeChar.getStat().addExp(840000000);
	    activeChar.getStat().addExp(840000000);
	    activeChar.getStat().addExp(840000000);
	    activeChar.getStat().addExp(840000000);
	    activeChar.getStat().addExp(840000000);
	    activeChar.getStat().addExp(100000000);
	    activeChar.getStat().addSp(99999999);
	}
	if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
	{
	    activeChar.sendPacket(new SystemMessage(SystemMessageId.CLAN_MEMBERSHIP_TERMINATED));
	}
	if (activeChar.getClan() != null)
	{
	    activeChar.sendPacket(new PledgeSkillList(activeChar.getClan()));

	    SiegeManager.getInstance().onEnterWorld(activeChar);
	    FortSiegeManager.getInstance().onEnterWorld(activeChar);
	    
	    // Add message at connexion if clanHall not paid.
	    // Possibly this is custom...
	    ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan());
	    if (clanHall != null)
	    {
		if (!clanHall.getPaid())
		{
		    activeChar.sendPacket(new SystemMessage(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW));
		}
	    }
	}
	RegionBBSManager.getInstance().changeCommunityBoard();
	/*
	 * if(Config.GAMEGUARD_ENFORCE) - disabled by KenM will be reenabled
	 * later activeChar.sendPacket(new GameGuardQuery());
	 */
	TvTEvent.onLogin(activeChar);
	PcColorTable.getInstance().process(activeChar);
        // NPCBuffer
        if (Config.NPCBUFFER_FEATURE_ENABLED) CharSchemesTable.getInstance().onPlayerLogin(activeChar.getObjectId());

	if (VIP._playersVIP.contains(activeChar.getName()))
	{
	    VIP.addPlayerVIP(activeChar);
	}
	if (VIP._playersNotVIP.contains(activeChar.getName()))
	{
	    VIP.addPlayerNotVIP(activeChar);
	}
	if (CTF._savePlayers.contains(activeChar.getName()))
	{
	    CTF.addDisconnectedPlayer(activeChar);
	}
    }

    /**
     * @param activeChar
     */
    private void engage(L2PcInstance cha)
    {
	int _chaid = cha.getObjectId();
	for (Couple cl : CoupleManager.getInstance().getCouples())
	{
	    if ((cl.getPlayer1Id() == _chaid) || (cl.getPlayer2Id() == _chaid))
	    {
		if (cl.getMaried())
		{
		    cha.setMaried(true);
		}
		cha.setCoupleId(cl.getId());
		if (cl.getPlayer1Id() == _chaid)
		{
		    cha.setPartnerId(cl.getPlayer2Id());
		} else
		{
		    cha.setPartnerId(cl.getPlayer1Id());
		}
	    }
	}
    }

    /**
     * @param activeChar
     *                partnerid
     */
    private void notifyPartner(L2PcInstance cha, int partnerId)
    {
	if (cha.getPartnerId() != 0)
	{
	    L2PcInstance partner;
	    partner = (L2PcInstance) L2World.getInstance().findObject(cha.getPartnerId());
	    if (partner != null)
	    {
		partner.sendMessage("Your Partner has logged in");
	    }
	    partner = null;
	}
    }

    /**
     * @param activeChar
     */
    private void notifyFriends(L2PcInstance cha)
    {
	java.sql.Connection con = null;
	try
	{
	    con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement;
	    statement = con.prepareStatement("SELECT friend_name FROM character_friends WHERE char_id=?");
	    statement.setInt(1, cha.getObjectId());
	    ResultSet rset = statement.executeQuery();
	    L2PcInstance friend;
	    String friendName;
	    SystemMessage sm = new SystemMessage(SystemMessageId.FRIEND_S1_HAS_LOGGED_IN);
	    sm.addString(cha.getName());
	    while (rset.next())
	    {
		friendName = rset.getString("friend_name");
		friend = L2World.getInstance().getPlayer(friendName);
		if (friend != null) // friend logged in.
		{
		    friend.sendPacket(new FriendList(friend));
		    friend.sendPacket(sm);
		}
	    }
	    sm = null;
	    rset.close();
	    statement.close();
	} catch (Exception e)
	{
	    _log.warning("could not restore friend data:" + e);
	} finally
	{
	    try
	    {
		con.close();
	    } catch (Exception e)
	    {
	    }
	}
    }

    /**
     * @param activeChar
     */
    private void notifyClanMembers(L2PcInstance activeChar)
    {
	L2Clan clan = activeChar.getClan();
	if (clan != null)
	{
	    clan.getClanMember(activeChar.getName()).setPlayerInstance(activeChar);
	    SystemMessage msg = new SystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN);
	    msg.addString(activeChar.getName());
	    clan.broadcastToOtherOnlineMembers(msg, activeChar);
	    msg = null;
	    clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(activeChar), activeChar);
	}
    }

    /**
     * @param activeChar
     */
    private void notifyCastleOwner(L2PcInstance activeChar)
    {
	if (Config.ANNOUNCE_CASTLE_LORDS)
	{
	    L2Clan clan = activeChar.getClan();
	    if (clan != null)
	    {
		if (clan.getHasCastle() > 0)
		{
		    Castle castle = CastleManager.getInstance().getCastleById(clan.getHasCastle());
		    if ((castle != null) && (activeChar.getObjectId() == clan.getLeaderId()))
		    {
			Announcements.getInstance().announceToAll("Castle Lord " + activeChar.getName() + " Of " + castle.getName() + " Castle Is Currently Online.");
		    }
		}
	    }
	}
    }

    /**
     * @param activeChar
     */
    private void checkCrown(L2PcInstance activeChar)
    {
	if (activeChar.isClanLeader() && (activeChar.getClan().getHasCastle() != 0))
	{
	    if ((activeChar.getInventory().getItemByItemId(6841) == null) && activeChar.getInventory().validateCapacity(1))
	    {
		activeChar.getInventory().addItem("Crown", 6841, 1, activeChar, null);
		activeChar.getInventory().updateDatabase();
	    }
	} else
	{
	    if (activeChar.getInventory().getItemByItemId(6841) != null)
	    {
		activeChar.getInventory().destroyItemByItemId("Crown", 6841, 1, activeChar, null);
	    }
	}
    }

    /**
     * @param activeChar
     */
    private void notifySponsorOrApprentice(L2PcInstance activeChar)
    {
	if (activeChar.getSponsor() != 0)
	{
	    L2PcInstance sponsor = (L2PcInstance) L2World.getInstance().findObject(activeChar.getSponsor());
	    if (sponsor != null)
	    {
		SystemMessage msg = new SystemMessage(SystemMessageId.YOUR_APPRENTICE_S1_HAS_LOGGED_IN);
		msg.addString(activeChar.getName());
		sponsor.sendPacket(msg);
	    }
	} else if (activeChar.getApprentice() != 0)
	{
	    L2PcInstance apprentice = (L2PcInstance) L2World.getInstance().findObject(activeChar.getApprentice());
	    if (apprentice != null)
	    {
		SystemMessage msg = new SystemMessage(SystemMessageId.YOUR_SPONSOR_S1_HAS_LOGGED_IN);
		msg.addString(activeChar.getName());
		apprentice.sendPacket(msg);
	    }
	}
    }

    /**
     * @param string
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getText(String string)
    {
	try
	{
	    String result = new String(Base64.decode(string), "UTF-8");
	    return result;
	} catch (UnsupportedEncodingException e)
	{
	    // huh, UTF-8 is not supported? :)
	    return null;
	}
    }

    private void setPledgeClass(L2PcInstance activeChar)
    {
	int pledgeClass = 0;
	if (activeChar.getClan() != null)
	{
	    pledgeClass = activeChar.getClan().getClanMember(activeChar.getObjectId()).calculatePledgeClass(activeChar);
	}
	if (activeChar.isNoble() && (pledgeClass < 5))
	{
	    pledgeClass = 5;
	}
	if (activeChar.isHero())
	{
	    pledgeClass = 8;
	}
	activeChar.setPledgeClass(pledgeClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
     */
    @Override
    public String getType()
    {
	return _C__03_ENTERWORLD;
    }
}