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
/**
 * This is the VIP engine which is called upon by the VIPTask class which is further called upon by the L2JTeonEventManager class. <br>
 * It is fully automated and is a component of the L2JTeon Event Automation System.<br>
 * 
 * @author Ahmed
 */
package net.sf.l2j.gameserver.model.entity.L2JTeonEvents;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;

import javolution.text.TextBuilder;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.ChainAutomation.L2JTeonEventManager;
import net.sf.l2j.gameserver.model.item.PcInventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VIP
{
	private final static Log _log = LogFactory.getLog(VIP.class.getName());
	public static String _teamName = "", _endTPArea = Config.VIP_ENDTP_DESC;
	public static int _time = Config.VIP_LASTING_TIME, _winners = 0, _endNPC = Config.VIP_FINISH_NPC_ID, _delay = Config.VIP_PARTICIPATION_TO_START_DELAY, _endX = 0, _endY = 0, _endZ = 0, _startX = 0, _startY = 0, _startZ = 0, _tpbackX = Config.VIP_TP_LOC_X, _tpbackY = Config.VIP_TP_LOC_Y, _tpbackZ = Config.VIP_TP_LOC_Z, _team = 0; // Human = 1
	// Elf = 2
	// Dark = 3
	// Orc = 4
	// Dwarf = 5
	public static boolean _started = false, _joining = false, _sitForced = Config.VIP_FORCE_SIT;
	public static L2Spawn _endSpawn, _joinSpawn;
	public static Vector<L2PcInstance> _playersVIP = new Vector<L2PcInstance>(), _playersNotVIP = new Vector<L2PcInstance>();

	/** This chooses a race by random as the VIP-Team. */
	public static void setRandomTeam()
	{
		Random generator = new Random();
		int random = generator.nextInt(5) + 1; // (0 - 4) + 1
		if (_log.isDebugEnabled())
		{
			_log.debug("Random number generated in setRandomTeam(): " + random);
		}
		switch (random)
		{
			case 1:
				_team = 1;
				_teamName = "Human";
				setLoc();
				break;
			case 2:
				_team = 2;
				_teamName = "Elf";
				setLoc();
				break;
			case 3:
				_team = 3;
				_teamName = "Dark Elf";
				setLoc();
				break;
			case 4:
				_team = 4;
				_teamName = "Orc";
				setLoc();
				break;
			case 5:
				_team = 5;
				_teamName = "Dwarf";
				setLoc();
				break;
			default:
				break;
		}
	}

	/**
	 * This sets the location start and finish location for the event. <br>
	 * The start location is where the VIP team is teleported too. <br>
	 * The end location is where the Non-VIP team is teleported too. <br>
	 */
	public static void setLoc()
	{
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT endx,endy,endz FROM vipinfo WHERE teamID = " + _team);
			ResultSet rset = statement.executeQuery();
			rset.next();
			_endX = rset.getInt("endx");
			_endY = rset.getInt("endy");
			_endZ = rset.getInt("endz");
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.error("Could not check End LOC for team" + _team + " got: " + e.getMessage());
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				// meh...
			}
		}
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT startx,starty,startz FROM vipinfo WHERE teamID = " + _team);
			ResultSet rset = statement.executeQuery();
			rset.next();
			_startX = rset.getInt("startx");
			_startY = rset.getInt("starty");
			_startZ = rset.getInt("startz");
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.error("Could not check Start LOC for team" + _team + " got: " + e.getMessage());
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				// meh 2?... lol
			}
		}
	}

	/**
	 * This spawns the finish NPC. <br>
	 * It is the VIP's Goal to get to this NPC.
	 */
	public static void spawnEndNPC()
	{
		L2NpcTemplate npctmp = NpcTable.getInstance().getTemplate(_endNPC);
		try
		{
			_endSpawn = new L2Spawn(npctmp);
			_endSpawn.setLocx(_endX);
			_endSpawn.setLocy(_endY);
			_endSpawn.setLocz(_endZ);
			_endSpawn.setAmount(1);
			_endSpawn.setHeading(180);
			_endSpawn.setRespawnDelay(1);
			SpawnTable.getInstance().addNewSpawn(_endSpawn, false);
			_endSpawn.init();
			_endSpawn.getLastSpawn().setCurrentHp(999999999);
			_endSpawn.getLastSpawn().setTitle("VIP FINISH");
			_endSpawn.getLastSpawn()._isEventVIPNPCEnd = true;
			_endSpawn.getLastSpawn().isAggressive();
			_endSpawn.getLastSpawn().decayMe();
			_endSpawn.getLastSpawn().spawnMe(_endSpawn.getLastSpawn().getX(), _endSpawn.getLastSpawn().getY(), _endSpawn.getLastSpawn().getZ());
			_endSpawn.getLastSpawn().broadcastPacket(new MagicSkillUser(_endSpawn.getLastSpawn(), _endSpawn.getLastSpawn(), 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.error("VIP Engine[spawnEndNPC()]: exception: " + e.getMessage());
		}
	}

	/**
	 * This gets the NPC's Name.
	 * 
	 * @param id
	 * @param activeChar
	 * @return
	 */
	public static String getNPCName(int id, L2PcInstance activeChar)
	{
		if (id == 0)
		{
			return "";
		}
		L2NpcTemplate npctmp = NpcTable.getInstance().getTemplate(id);
		return npctmp.name;
	}

	/**
	 * This gets the items name.
	 * 
	 * @param id
	 * @param activeChar
	 * @return
	 */
	public static String getItemName(int id, L2PcInstance activeChar)
	{
		if (id == 0)
		{
			return "";
		}
		L2Item itemtmp = ItemTable.getInstance().getTemplate(id);
		return itemtmp.getName();
	}

	/**
	 * When this is called players can type .joinvip and be entered into the event. Also Random Race is selected as the VIP team.
	 */
	public static void startJoin()
	{
		if ((_teamName == "") || (_endTPArea == "") || (_time == 0) || (_endNPC == 0) || (_delay == 0) || (_endX == 0) || (_endY == 0) || (_endZ == 0) || (_startX == 0) || (_startY == 0) || (_startZ == 0) || (_team == 0))
		{
			System.out.println("VIP Event Participation period has been canceled, check startJoin().");
			return;
		}
		Announcements.getInstance().announceToAll("VIP Event registration is open for " + _delay + " minutes.");
		waiter(10);
		Announcements.getInstance().announceToAll("In the VIP Event the " + _teamName + " characters must safely escort the VIP from one location to their starter town.");
		waiter(10);
		Announcements.getInstance().announceToAll("To join the VIP event, type in .joinvip and press enter. ");
		System.out.println("VIP Event Participation period has started");
	}

	/**
	 * When this is called the players are: <br>
	 * 1. Teleported<br>
	 * 2. The VIP is chosen<br>
	 * 3. The name colors are changed<br>
	 * 4. Announcements to players are sent <br>
	 * 5. The finish npc is spawned.
	 */
	public static void startEvent()
	{
		if ((_playersVIP.size() >= 1) && (_playersNotVIP.size() >= 1))
		{
			Announcements.getInstance().announceToAll("Registration for the VIP event has ended.");
			Announcements.getInstance().announceToAll("Players will be teleported to their locations in" + Config.VIP_TIME_BEFORE_TELEPORT + " seconds.");
			System.out.println("VIP Event Participation period has ended.");
			System.out.println("VIP Event Participants will be teleported in " + Config.VIP_TIME_BEFORE_TELEPORT + " seconds.");
			waiter(Config.VIP_TIME_BEFORE_TELEPORT);
			teleport();
			chooseVIP();
			setUserData();
			Announcements.getInstance().announceToAll("All players teleported successfully, VIP event will start in" + Config.VIP_SIT_TIME + " seconds.");
			spawnEndNPC();
			System.out.println("VIP has been choosen on the " + _teamName + " team, and name colors have been set.");
			waiter(Config.VIP_SIT_TIME - 40);
			System.out.println("VIP Event has started.");
			Announcements.getInstance().announceToAll("VIP event has started.");
			waiter(10);
			Announcements.getInstance().announceToAll("The " + _teamName + "'s VIP must get to the starter city and talk with " + getNPCName(_endNPC, null) + ".");
			waiter(10);
			Announcements.getInstance().announceToAll("The opposing team must kill the VIP. If Killed during the event, all players except the VIP will respawn at their starting locations.");
			waiter(10);
			Announcements.getInstance().announceToAll("VIP event will end if one of the following happens: 1. The " + _teamName + " team makes it to their starter town. 2. When " + _time + " mins have elapsed.");
			waiter(10);
			Announcements.getInstance().announceToAll("3. VIP is killed by the Non-VIP team.");
			stand();
		}
		else if ((_playersVIP.size() < 1) || (_playersNotVIP.size() < 1))
		{
			System.out.println("VIP Event has been cancelled due to lack of participation.");
			EndEventPrematurly();
			return;
		}
	}

	/**
	 * This is called if not enough players on VIP team or Non VIP team., <br>
	 * Announcement to all players <br>
	 * Deletion of all VIP npc's<br>
	 * Event status set to false <br>
	 */
	public static void EndEventPrematurly()
	{
		if (!_started)
		{
			_log.info("Could not finish the event. Event not started or event ended prematurly");
			return;
		}
		_joining = false;
		_started = false;
		unspawnEventNpcs();
		Announcements.getInstance().announceToAll("The VIP has ended due to lack of participation.");
		clean();
		TvTEvent.EventInProgress = false;
		L2JTeonEventManager.getInstance().init();
		return;
	}

	/**
	 * This method waits for a period time delay<br>
	 * <br>
	 * 
	 * @param interval
	 * <br>
	 */
	private static void waiter(int seconds)
	{
		while (seconds > 1)
		{
			seconds--;
			long oneSecWaitStart = System.currentTimeMillis();
			// only the try catch with Thread.sleep(1000) give bad
			// count-down on
			// high wait times
			while (oneSecWaitStart + 1000L > System.currentTimeMillis())
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException ie)
				{
					// meh...
				}
			}
		}
	}

	/**
	 * When this is called the VIP has died, Non-VIP Team gets reward,<br>
	 * players teleported back, and announcement to all players, deletion of the event npc.<br>
	 */
	public static void vipDied()
	{
		if (!_started)
		{
			_log.info("Event not started or event ended prematurly.");
			return;
		}
		_started = false;
		unspawnEventNpcs();
		Announcements.getInstance().announceToAll("The VIP has died. The opposing team has won.");
		rewardNotVIP();
		teleportFinish();
	}

	/**
	 * When this is called the VIP has run out of time,<br>
	 * Non-VIP Team gets reward, players teleported back, <br>
	 * and announcement to all players, deletion of the event npc.
	 */
	public static void endEventTime()
	{
		if (!_started)
		{
			_log.info("Event not started or event ended prematurly");
			return;
		}
		_started = false;
		unspawnEventNpcs();
		Announcements.getInstance().announceToAll("The time has run out and the " + _teamName + "'s have not made it to their goal. Everybody on the opposing team wins.");
		rewardNotVIP();
		teleportFinish();
		TvTEvent.EventInProgress = false;
		return;
	}

	/**
	 * Deletes all NPC's spawned for the VIP event.
	 */
	public static void unspawnEventNpcs()
	{
		if (_endSpawn != null)
		{
			_endSpawn.getLastSpawn().deleteMe();
			_endSpawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(_endSpawn, true);
		}
	}

	/**
	 * his is the message players will get when they talk to the VIP Finish NPC.
	 * 
	 * @param eventPlayer
	 * @param objectId
	 */
	public static void showEndHTML(L2PcInstance eventPlayer, String objectId)
	{
		try
		{
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			TextBuilder replyMSG = new TextBuilder("<html><head><body>");
			replyMSG.append("VIP (End NPC)<br><br>");
			replyMSG.append("Current event...<br1>");
			replyMSG.append("    ... Team:&nbsp;<font color=\"FFFFFF\">" + _teamName + "</font><br><br>");
			if (!_started)
			{
				replyMSG.append("<center>Please wait until the admin/gm starts the joining period.</center>");
			}
			else if (eventPlayer._isTheVIP)
			{
				replyMSG.append("You have made it to the end. All you have to do is hit the finish button to reward yourself and your team. Congrats!<br>");
				replyMSG.append("<center>");
				replyMSG.append("<button value=\"Finish\" action=\"bypass -h npc_" + objectId + "_vip_finishVIP\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
				replyMSG.append("</center>");
			}
			else
			{
				replyMSG.append("I am the character the VIP has to reach in order to win the event.<br>");
			}
			replyMSG.append("</head></body></html>");
			adminReply.setHtml(replyMSG.toString());
			eventPlayer.sendPacket(adminReply);
		}
		catch (Exception e)
		{
			if (_log.isDebugEnabled())
			{
				_log.debug("VIP(showJoinHTML(" + eventPlayer.getName() + ", " + objectId + ")]: exception" + e.getMessage());
			}
		}
	}

	/**
	 * This is called if the VIP wins the Event by talking to the VIP Finish NPC, <br>
	 * he and his team get rewarded. <br>
	 * Announcement to all players, and deletion of all VIP npc's and teleport back to area set by config files. <br>
	 */
	public static void vipWin(L2PcInstance activeChar)
	{
		if (!_started)
		{
			_log.info("Could not finish the event. Event not started or event ended prematurly");
			return;
		}
		_started = false;
		unspawnEventNpcs();
		Announcements.getInstance().announceToAll("The VIP has made it to the goal. " + _teamName + " has won. Everybody on that team wins.");
		rewardVIP();
		teleportFinish();
	}

	/**
	 * This rewards the Non-VIP Team players.
	 */
	public static void rewardNotVIP()
	{
		for (L2PcInstance player : _playersNotVIP)
		{
			for (int[] reward : Config.VIP_NON_REWARD)
			{
				if (player == null)
				{
					continue;
				}
				PcInventory inv = player.getInventory();
				if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
				{
					inv.addItem("VIP Event", reward[0], reward[1], player, null);
				}
				else
				{
					for (int i = 0; i < reward[1]; i++)
					{
						inv.addItem("VIP Event", reward[0], 1, player, null);
					}
				}
				SystemMessage systemMessage = null;
				if (reward[1] > 1)
				{
					systemMessage = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
					systemMessage.addItemName(reward[0]);
					systemMessage.addNumber(reward[1]);
				}
				else
				{
					systemMessage = new SystemMessage(SystemMessageId.EARNED_ITEM);
					systemMessage.addItemName(reward[0]);
				}
				player.sendPacket(systemMessage);
			}
			StatusUpdate statusUpdate = new StatusUpdate(player.getObjectId());
			statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
			player.sendPacket(statusUpdate);
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
			npcHtmlMessage.setHtml("<html><head><title>VIP Event</title></head><body>Your team won the event.<br>Please check your inventory for your reward.</body></html>");
			player.sendPacket(npcHtmlMessage);
		}
	}

	/**
	 * !player._isTheVIP This rewards the VIP and the VIP Team players.
	 */
	public static void rewardVIP()
	{
		for (L2PcInstance player : _playersVIP)
		{
			if ((player != null) && !player._isTheVIP)
			{
				for (int[] reward : Config.VIP_TEAM_REWARD)
				{
					PcInventory inv = player.getInventory();
					if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
					{
						inv.addItem("VIP Event", reward[0], reward[1], player, null);
					}
					else
					{
						for (int i = 0; i < reward[1]; i++)
						{
							inv.addItem("VIP Event", reward[0], 1, player, null);
						}
					}
					SystemMessage systemMessage = null;
					if (reward[1] > 1)
					{
						systemMessage = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
						systemMessage.addItemName(reward[0]);
						systemMessage.addNumber(reward[1]);
					}
					else
					{
						systemMessage = new SystemMessage(SystemMessageId.EARNED_ITEM);
						systemMessage.addItemName(reward[0]);
					}
					player.sendPacket(systemMessage);
				}
				StatusUpdate statusUpdate = new StatusUpdate(player.getObjectId());
				statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
				player.sendPacket(statusUpdate);
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
				npcHtmlMessage.setHtml("<html><head><title>VIP Event</title></head><body>Your team won the event.<br>Please check your inventory for your reward.</body></html>");
				player.sendPacket(npcHtmlMessage);
			}
			else
			{
				if ((player != null) && player._isTheVIP)
				{
					for (int[] reward : Config.VIP_REWARD)
					{
						PcInventory inv = player.getInventory();
						if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
						{
							inv.addItem("VIP Event", reward[0], reward[1], player, null);
						}
						else
						{
							for (int i = 0; i < reward[1]; i++)
							{
								inv.addItem("VIP Event", reward[0], 1, player, null);
							}
						}
						SystemMessage systemMessage = null;
						if (reward[1] > 1)
						{
							systemMessage = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
							systemMessage.addItemName(reward[0]);
							systemMessage.addNumber(reward[1]);
						}
						else
						{
							systemMessage = new SystemMessage(SystemMessageId.EARNED_ITEM);
							systemMessage.addItemName(reward[0]);
						}
						player.sendPacket(systemMessage);
					}
					StatusUpdate statusUpdate = new StatusUpdate(player.getObjectId());
					statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
					player.sendPacket(statusUpdate);
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
					npcHtmlMessage.setHtml("<html><head><title>VIP Event</title></head><body>Your team won the event.<br>Please check your inventory for your reward.</body></html>");
					player.sendPacket(npcHtmlMessage);
				}
			}
		}
	}

	/**
	 * This makes the calls to teleport players back to town if the event ends, the finish location is set in properties files.
	 */
	public static void teleportFinish()
	{
		Announcements.getInstance().announceToAll("Teleporting VIP players back to" + _endTPArea + Config.VIP_TELEPORT_TIME + " seconds.");
		L2JTeonEventManager.getInstance().init();
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			public void run()
			{
				VIPTeamTPFinish();
				NonVIPTeamTPFinish();
				clean();
			}
		}, Config.VIP_TELEPORT_TIME);
	}

	/**
	 * This will teleport all the VIPTeam players to the finish area.
	 */
	public static void VIPTeamTPFinish()
	{
		for (L2PcInstance player : _playersVIP)
		{
			if (player != null)
			{
				player.teleToLocation(_tpbackX, _tpbackY, _tpbackZ);
			}
		}
	}

	/**
	 * This will teleport all the NonVIPTeam players to the finish area.
	 */
	public static void NonVIPTeamTPFinish()
	{
		for (L2PcInstance player : _playersNotVIP)
		{
			if (player != null)
			{
				player.teleToLocation(_tpbackX, _tpbackY, _tpbackZ);
			}
		}
	}

	/**
	 * This calls the cleaning of all the set VIP's set parameters.
	 */
	public static void clean()
	{
		_winners = _endX = _endY = _endZ = _startX = _startY = _startZ = _team = 0;
		_started = _joining = false;
		_teamName = "";
		VIPTeamClean();
		NonVIPTeamClean();
		_playersVIP = new Vector<L2PcInstance>();
		_playersNotVIP = new Vector<L2PcInstance>();
	}

	/**
	 * This cleans the VIP Team Parameters.
	 */
	public static void VIPTeamClean()
	{
		for (L2PcInstance player : _playersVIP)
		{
			player.getAppearance().setNameColor(player._originalNameColourVIP);
			player.setKarma(player._originalKarmaVIP);
			player.broadcastUserInfo();
			player._inEventVIP = false;
			player._isTheVIP = false;
			player._isNotVIP = false;
			player._isVIP = false;
		}
	}

	/**
	 * This cleans the NonVIP Team Parameters.
	 */
	public static void NonVIPTeamClean()
	{
		for (L2PcInstance player : _playersNotVIP)
		{
			player.getAppearance().setNameColor(player._originalNameColourVIP);
			player.setKarma(player._originalKarmaVIP);
			player.broadcastUserInfo();
			player._inEventVIP = false;
			player._isTheVIP = false;
			player._isNotVIP = false;
			player._isVIP = false;
		}
	}

	/**
	 * This randomly selects the VIP player from the team selected as the VIP team.
	 */
	public static void chooseVIP()
	{
		int size = _playersVIP.size();
		if (_log.isDebugEnabled())
		{
			_log.debug("Size of players on VIP: " + size);
		}
		Random generator = new Random();
		int random = generator.nextInt(size);
		if (_log.isDebugEnabled())
		{
			_log.debug("Random number chosen in VIP: " + random);
		}
		L2PcInstance VIP = _playersVIP.get(random);
		VIP._isTheVIP = true;
	}

	/**
	 * This will call on the teleport method.
	 */
	public static void teleport()
	{
		teleportVIPPlayers();
		teleportNonVIPPlayers();
	}

	/**
	 * This teleports VIP players to their starting locations.
	 */
	public static void teleportVIPPlayers()
	{
		sit();
		for (L2PcInstance player : _playersVIP)
		{
			if (player != null)
			{
				player.teleToLocation(_startX, _startY, _startZ);
			}
		}
	}

	/**
	 * This teleports Non-VIP players to their starting locations.
	 */
	public static void teleportNonVIPPlayers()
	{
		sit();
		for (L2PcInstance player : _playersNotVIP)
		{
			if (player != null)
			{
				player.teleToLocation(_endX, _endY, _endZ);
			}
		}
	}

	/**
	 * This calls for allowing the VIP and NonVIP to sit.
	 */
	public static void sit()
	{
		VIPTeamsit();
		NonVIPTeamsit();
	}

	/**
	 * This calls for allowing the VIP and NonVIP to stand.
	 */
	public static void stand()
	{
		VIPTeamStand();
		NonVIPTeamStand();
	}

	/**
	 * This forces the VIPTeam to sit at appropriate times.
	 */
	public static void VIPTeamsit()
	{
		if (_sitForced)
		{
			for (L2PcInstance player : _playersVIP)
			{
				if (player != null)
				{
					player.stopMove(null, false);
					if (!player.isSitting())
					{
						player.breakAttack();
						player.breakCast();
						player.abortAttack();
						player.abortCast();
						player.sitDown();
					}
				}
			}
		}
	}

	/**
	 * This forces the VIPTeam to stand at appropriate times.
	 */
	public static void VIPTeamStand()
	{
		if (_sitForced)
		{
			for (L2PcInstance player : _playersVIP)
			{
				if (player != null)
				{
					if (player.isSitting())
					{
						player.standUp();
					}
				}
			}
		}
	}

	/**
	 * This forces the NonVIPTeam to sit at appropriate times.
	 */
	public static void NonVIPTeamsit()
	{
		if (_sitForced)
		{
			for (L2PcInstance player : _playersNotVIP)
			{
				if (player != null)
				{
					player.stopMove(null, false);
					if (!player.isSitting())
					{
						player.breakAttack();
						player.breakCast();
						player.abortAttack();
						player.abortCast();
						player.sitDown();
					}
				}
			}
		}
	}

	/**
	 * This forces the NonVIPTeam to stand at appropriate times.
	 */
	public static void NonVIPTeamStand()
	{
		if (_sitForced)
		{
			for (L2PcInstance player : _playersNotVIP)
			{
				if (player != null)
				{
					if (player.isSitting())
					{
						player.standUp();
					}
				}
			}
		}
	}

	/**
	 * This calls for setting the Players name colors depending on their team and role.
	 */
	public static void setUserData()
	{
		VIPsetUserData();
		NonVIPsetUserData();
	}

	/**
	 * This sets the color for the VIP Team. (dark slate gray)
	 */
	public static void VIPsetUserData()
	{
		for (L2PcInstance player : _playersVIP)
		{
			/**
			 * Color for the VIP Player. (Dark Goldenrod)
			 */
			if (player._isTheVIP)
			{
				player.getAppearance().setNameColor(184, 134, 11);
			}
			else
			/**
			 * Color for the VIP Team. (Purple)
			 */
			{
				player.getAppearance().setNameColor(160, 32, 240);
			}
			player.setKarma(0);
			player.broadcastUserInfo();
		}
	}

	/**
	 * This sets the color for the Non-VIP Team. (dark slate gray)
	 */
	public static void NonVIPsetUserData()
	{
		for (L2PcInstance player : _playersNotVIP)
		{
			player.getAppearance().setNameColor(47, 79, 79);
			player.setKarma(0);
			player.broadcastUserInfo();
		}
	}

	/**
	 * This allows the addition VIP players.
	 * 
	 * @param activeChar
	 */
	public static void addPlayerVIP(L2PcInstance activeChar)
	{
		activeChar._isVIP = true;
		_playersVIP.add(activeChar);
		activeChar._originalNameColourVIP = activeChar.getAppearance().getNameColor();
		activeChar._originalKarmaVIP = activeChar.getKarma();
		activeChar._inEventVIP = true;
	}

	/**
	 * This allows the addition of Non-VIP players.
	 * 
	 * @param activeChar
	 */
	public static void addPlayerNotVIP(L2PcInstance activeChar)
	{
		activeChar._isNotVIP = true;
		_playersNotVIP.add(activeChar);
		activeChar._originalNameColourVIP = activeChar.getAppearance().getNameColor();
		activeChar._originalKarmaVIP = activeChar.getKarma();
		activeChar._inEventVIP = true;
	}

	/**
	 * This will Remove a VIP Event participant.
	 * 
	 * @param activeChar
	 */
	public static void removePlayerVIP(L2PcInstance activeChar)
	{
		activeChar.getAppearance().setNameColor(activeChar._originalNameColourVIP);
		activeChar.setKarma(activeChar._originalKarmaVIP);
		activeChar.broadcastUserInfo();
		activeChar._inEventVIP = false;
		activeChar._isTheVIP = false;
		activeChar._isNotVIP = false;
		activeChar._isVIP = false;
	}

	public static void logoutPlayerVIP(L2PcInstance playerInstance)
	{
		if ((playerInstance == null) || (!_started && !_joining))
		{
			return;
		}
		removePlayerVIP(playerInstance);
	}
}