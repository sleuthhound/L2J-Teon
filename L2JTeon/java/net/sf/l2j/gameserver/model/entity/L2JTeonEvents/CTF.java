package net.sf.l2j.gameserver.model.entity.L2JTeonEvents;

import java.util.Vector;
import javolution.text.TextBuilder;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.PcInventory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.ChainAutomation.L2JTeonEventManager;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class CTF
{
    public static String _eventName = new String("Capture The Flag"),
	    _eventDesc = new String("You must capture the opposing Flag and bring it back to your Flag to score."),
	    _topTeam = new String(),
	    _joiningLocationName = new String("Giran");
    public static Vector<String> _teams = new Vector<String>(),
	    _savePlayers = new Vector<String>(),
	    _savePlayerTeams = new Vector<String>();
    public static Vector<L2PcInstance> _players = new Vector<L2PcInstance>();
    public static Vector<Integer> _teamPlayersCount = new Vector<Integer>(),
	    _teamPointsCount = new Vector<Integer>(),
	    _teamColors = new Vector<Integer>(),
	    _flagIds = new Vector<Integer>(), _flagsX = new Vector<Integer>(),
	    _flagsY = new Vector<Integer>(), _flagsZ = new Vector<Integer>();
    public static Vector<L2Spawn> _flagSpawns = new Vector<L2Spawn>();
    public static Vector<Boolean> _flagsTaken = new Vector<Boolean>();
    public static boolean _joining = false, _teleport = false,
	    _started = false, _finished = false, _sitForced = false;
    public static L2Spawn _npcSpawn;
    public static int _npcId = 70012, _npcX = 0, _npcY = 0, _npcZ = 0,
	    _npcHeading = 180, _rewardId = 57, _rewardAmount = 1000,
	    _topScore = 0;
    public static int _scoreLimit = 5;
    public static boolean _limitReached = false;

    public static void setNpcPos(L2PcInstance activeChar)
    {
	_npcX = activeChar.getX();
	_npcY = activeChar.getY();
	_npcZ = activeChar.getZ();
	_npcHeading = activeChar.getHeading();
    }

    public static void init()
    {
	if (_teams.size() == 0)
	{
	    setup(Config.CTF_TEAM_NAME_1, Config.CTF_TEAM_FLAG_ID_1, Config.CTF_TEAM_CORDS_1[0], Config.CTF_TEAM_CORDS_1[1], Config.CTF_TEAM_CORDS_1[2], Config.CTF_TEAM_COLOR_1);
	    setup(Config.CTF_TEAM_NAME_2, Config.CTF_TEAM_FLAG_ID_2, Config.CTF_TEAM_CORDS_2[0], Config.CTF_TEAM_CORDS_2[1], Config.CTF_TEAM_CORDS_2[2], Config.CTF_TEAM_COLOR_2);
	}
    }

    public static void setup(String teamName, int flagId, int flagX, int flagY, int flagZ, int teamColor)
    {
	int index = _teams.indexOf(teamName);
	_teams.add(teamName);
	_teamPlayersCount.add(0);
	_teamPointsCount.add(0);
	_teamColors.add(0);
	_flagIds.add(0);
	_flagsX.add(0);
	_flagsY.add(0);
	_flagsZ.add(0);
	_flagSpawns.add(null);
	_flagsTaken.add(false);
	_flagIds.set(index, flagId);
	_flagsX.set(index, flagX);
	_flagsY.set(index, flagY);
	_flagsZ.set(index, flagZ);
	_teamColors.set(index, teamColor);
    }

    public static void addTeam(String teamName)
    {
	if (!checkAddTeamOk())
	{
	    System.out.println("CTF Engine[addTeam(" + teamName + ")]: checkAddTeamOk() == false");
	    return;
	}
	if (teamName.equals(" "))
	{
	    return;
	}
	_teams.add(teamName);
	_teamPlayersCount.add(0);
	_teamPointsCount.add(0);
	_teamColors.add(_teams.size());
	_flagIds.add(0);
	_flagsX.add(0);
	_flagsY.add(0);
	_flagsZ.add(0);
	_flagSpawns.add(null);
	_flagsTaken.add(false);
    }

    public static void removeTeam(String teamName)
    {
	if (!checkTeamOk() || _teams.isEmpty())
	{
	    System.out.println("CTF Engine[removeTeam(" + teamName + ")]: checkTeamOk() == false");
	    return;
	}
	if (teamPlayersCount(teamName) > 0)
	{
	    System.out.println("CTF Engine[removeTeam(" + teamName + ")]: teamPlayersCount(teamName) > 0");
	    return;
	}
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return;
	}
	_flagsTaken.remove(index);
	_flagSpawns.remove(index);
	_flagsZ.remove(index);
	_flagsY.remove(index);
	_flagsX.remove(index);
	_flagIds.remove(index);
	_teamColors.remove(index);
	_teamPointsCount.remove(index);
	_teamPlayersCount.remove(index);
	_teams.remove(index);
    }

    public static void setTeamFlag(String teamName, int npcId)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return;
	}
	_flagIds.set(index, npcId);
    }

    public static void setTeamPos(String teamName, L2PcInstance activeChar)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return;
	}
	_flagsX.set(index, activeChar.getX());
	_flagsY.set(index, activeChar.getY());
	_flagsZ.set(index, activeChar.getZ());
    }

    private static boolean checkAddTeamOk()
    {
	if (_teams.size() == 2)
	{
	    return false;
	}
	return checkTeamOk();
    }

    public static boolean checkTeamOk()
    {
	if (_started || _teleport || _joining)
	{
	    return false;
	}
	return true;
    }

    /**
     * This initiates the participation period which allows players to join
     * the event.
     * 
     */
    public static void startJoin()
    {
	if (variableCheck() == false)
	{
	    System.out.println("CTF Engine Failed to start participation period, variable check failed.");
	    return;
	}
	_joining = true;
	Announcements.getInstance().announceToAll("Attention all players. An event is about to start!");
	Announcements.getInstance().announceToAll("At this time you are able to join a CTF event which will start in " + Config.CTF_PARTICIPATION_TIME + " mins.");
	Announcements.getInstance().announceToAll("In this event the you must capture the opposing teams flag and bring it to your base.");
	Announcements.getInstance().announceToAll("You may choose to join team 1 or team 2.");
	Announcements.getInstance().announceToAll("To join the CTF event, type in .joinctf and press enter. ");
    }

    private static boolean variableCheck()
    {
	if (_started || _teleport || _joining || (_teams.size() < 2) || _eventName.equals("") || _joiningLocationName.equals("") || _eventDesc.equals("") || (_npcId == 0) || (_npcX == 0) || (_npcY == 0) || (_npcZ == 0) || (_rewardId == 0) || (_rewardAmount == 0) || _flagIds.contains(0) || _flagsX.contains(0) || _flagsY.contains(0) || _flagsZ.contains(0))
	{
	    return false;
	}
	return true;
    }

    /**
     * This teleports all players participating in the event to their
     * respective starting locations.
     * 
     */
    public static void teleportStart()
    {
	if (!startTeleportOk())
	{
	    System.out.println("CTF Engine failed to teleport players, variable check failed.");
	    return;
	}
	_joining = false;
	Announcements.getInstance().announceToAll(_eventName + " will start in " + Config.CTF_TELEPORT_DELAY_TIME + " mins.");
	setUserData();
	ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
	{
	    public void run()
	    {
		CTF.sit();
		for (L2PcInstance player : CTF._players)
		{
		    if (player != null)
		    {
			if (Config.CTF_ON_START_UNSUMMON_PET)
			{
			    L2Summon s = player.getPet();
			    if (s != null)
			    {
				s.unSummon(player);
			    }
			}
			if (Config.CTF_ON_START_REMOVE_ALL_EFFECTS)
			{
			    for (L2Effect e : player.getAllEffects())
			    {
				if (e != null)
				{
				    e.exit();
				}
			    }
			}
			player.teleToLocation(_flagsX.get(_teams.indexOf(player._teamNameCTF)), _flagsY.get(_teams.indexOf(player._teamNameCTF)), _flagsZ.get(_teams.indexOf(player._teamNameCTF)), false);
		    }
		}
	    }
	}, 5000);
	spawnAllFlags();
	_teleport = true;
    }

    private static boolean startTeleportOk()
    {
	if (!_joining || _teamPlayersCount.contains(0))
	{
	    return false;
	}
	return true;
    }

    public static void setUserData()
    {
	for (L2PcInstance player : _players)
	{
	    player.setTeam(teamColor(player._teamNameCTF));
	    player.setKarma(0);
	    player.broadcastUserInfo();
	}
    }

    private static void spawnAllFlags()
    {
	for (String team : _teams)
	{
	    int index = _teams.indexOf(team);
	    L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_flagIds.get(index));
	    try
	    {
		_flagSpawns.set(index, new L2Spawn(tmpl));
		_flagSpawns.get(index).setLocx(_flagsX.get(index));
		_flagSpawns.get(index).setLocy(_flagsY.get(index));
		_flagSpawns.get(index).setLocz(_flagsZ.get(index));
		_flagSpawns.get(index).setAmount(1);
		_flagSpawns.get(index).setHeading(0);
		_flagSpawns.get(index).setRespawnDelay(1);
		SpawnTable.getInstance().addNewSpawn(_flagSpawns.get(index), false);
		_flagSpawns.get(index).init();
		_flagSpawns.get(index).getLastSpawn().setCurrentHp(999999999);
		_flagSpawns.get(index).getLastSpawn().setTitle(team);
		_flagSpawns.get(index).getLastSpawn().decayMe();
		_flagSpawns.get(index).getLastSpawn().spawnMe(_flagSpawns.get(index).getLastSpawn().getX(), _flagSpawns.get(index).getLastSpawn().getY(), _flagSpawns.get(index).getLastSpawn().getZ());
	    } catch (Exception e)
	    {
		System.out.println("CTF Engine[spawnAllFlags()]: exception: " + e.getStackTrace());
	    }
	}
    }

    public static void startEvent()
    {
	if (!_teleport)
	{
	    System.out.println("CTF Engine[startEvent()]: start conditions wrong");
	    return;
	}
	_teleport = false;
	_limitReached = false;
	_finished = false;
	sit();
	for (L2PcInstance player : _players)
	{
	    if (player != null)
	    {
		player._posCheckerCTF = ThreadPoolManager.getInstance().scheduleGeneral(new posChecker(player), 0);
	    }
	}
	Announcements.getInstance().announceToAll(_eventName + "(CTF): Started. Go to capture the flags!");
	Announcements.getInstance().announceToAll("First team to get " + _scoreLimit + "points, wins!");
	Announcements.getInstance().announceToAll("Reward for this event is  " + _rewardAmount + " Event tokens!");
	_started = true;
    }

    public static void finishEvent()
    {
	if (!finishEventOk())
	{
	    System.out.println("CTF Engine[finishEvent]: finishEventOk() == false");
	    return;
	}
	_started = false;
	_finished = true;
	unspawnEventNpc();
	unspawnAllFlags();
	processTopTeam();
	L2JTeonEventManager.getInstance().init();
	;
	if (_topScore == 0)
	{
	    Announcements.getInstance().announceToAll(_eventName + "(CTF): No team win the match(no one scores).");
	} else
	{
	    Announcements.getInstance().announceToAll(_eventName + "(CTF): " + _topTeam + " win the match! " + _topScore + " score.");
	    rewardTeam(_topTeam);
	}
	if (!_limitReached)
	{
	    teleportFinish();
	}
    }

    private static boolean finishEventOk()
    {
	if (!_started)
	{
	    return false;
	}
	return true;
    }

    public static void processTopTeam()
    {
	for (String team : _teams)
	{
	    if (teamPointsCount(team) > _topScore)
	    {
		_topTeam = team;
		_topScore = teamPointsCount(team);
	    }
	}
    }

    public static void rewardTeam(String teamName)
    {
	for (L2PcInstance player : _players)
	{
	    if (player != null)
	    {
		if (player._teamNameCTF.equals(teamName))
		{
		    PcInventory inv = player.getInventory();
		    if (ItemTable.getInstance().createDummyItem(_rewardId).isStackable())
		    {
			inv.addItem("CTF Event: " + _eventName, _rewardId, _rewardAmount, player, null);
		    } else
		    {
			for (int i = 0; i <= _rewardAmount - 1; i++)
			{
			    inv.addItem("CTF Event: " + _eventName, _rewardId, 1, player, null);
			}
		    }
		    SystemMessage sm;
		    if (_rewardAmount > 1)
		    {
			sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
			sm.addItemName(_rewardId);
			sm.addNumber(_rewardAmount);
			player.sendPacket(sm);
		    } else
		    {
			sm = new SystemMessage(SystemMessageId.EARNED_ITEM);
			sm.addItemName(_rewardId);
			player.sendPacket(sm);
		    }
		    StatusUpdate su = new StatusUpdate(player.getObjectId());
		    su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		    player.sendPacket(su);
		    NpcHtmlMessage nhm = new NpcHtmlMessage(5);
		    TextBuilder replyMSG = new TextBuilder("");
		    replyMSG.append("<html><head><body>Your team win the event. Look in your inventar there should be the reward.</body></html>");
		    nhm.setHtml(replyMSG.toString());
		    player.sendPacket(nhm);
		}
	    }
	}
    }

    public static void sit()
    {
	if (_sitForced)
	{
	    _sitForced = false;
	} else
	{
	    _sitForced = true;
	}
	for (L2PcInstance player : _players)
	{
	    if (player != null)
	    {
		if (_sitForced)
		{
		    player.stopMove(null, false);
		    player.abortAttack();
		    player.abortCast();
		    if (!player.isSitting())
		    {
			player.sitDown();
		    }
		} else
		{
		    if (player.isSitting())
		    {
			player.standUp();
		    }
		}
	    }
	}
    }

    static void clean()
    {
	for (String team : _teams)
	{
	    int index = _teams.indexOf(team);
	    _teamPlayersCount.set(index, 0);
	    _teamPointsCount.set(index, 0);
	    _flagsTaken.set(index, false);
	    _flagSpawns.set(index, null);
	}
	for (L2PcInstance player : _players)
	{
	    player.setTeam(0);
	    player.setKarma(player._originalKarmaCTF);
	    player.broadcastUserInfo();
	    player._teamNameCTF = new String();
	    player._inEventCTF = false;
	    player._haveFlagCTF = false;
	    player._posCheckerCTF.cancel(true);
	    player._posCheckerCTF = null;
	}
	_topScore = 0;
	_topTeam = new String();
	_players = new Vector<L2PcInstance>();
	_savePlayers = new Vector<String>();
	_savePlayerTeams = new Vector<String>();
	_limitReached = false;
	_finished = false;
    }

    public static void unspawnEventNpc()
    {
	if (_npcSpawn == null)
	{
	    return;
	}
	_npcSpawn.getLastSpawn().deleteMe();
	_npcSpawn.stopRespawn();
	SpawnTable.getInstance().deleteSpawn(_npcSpawn, true);
    }

    public static void unspawnAllFlags()
    {
	for (String team : _teams)
	{
	    int index = _teams.indexOf(team);
	    if (_flagSpawns.get(index) != null)
	    {
		_flagSpawns.get(index).getLastSpawn().deleteMe();
		_flagSpawns.get(index).stopRespawn();
		SpawnTable.getInstance().deleteSpawn(_flagSpawns.get(index), true);
	    }
	}
    }

    public static void teleportFinish()
    {
	Announcements.getInstance().announceToAll(_eventName + "(CTF): Teleport back to participation NPC in 5 seconds!");
	ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
	{
	    public void run()
	    {
		for (L2PcInstance player : _players)
		{
		    if (player != null)
		    {
			player.teleToLocation(_npcX, _npcY, _npcZ, false);
		    }
		}
		if (_finished)
		{
		    CTF.clean();
		}
	    }
	}, 5000);
	if (_limitReached)
	{
	    sit();
	}
    }

    static void unspawnFlag(String teamName)
    {
	int index = _teams.indexOf(teamName);
	_flagSpawns.get(index).getLastSpawn().deleteMe();
	_flagSpawns.get(index).stopRespawn();
	SpawnTable.getInstance().deleteSpawn(_flagSpawns.get(index), true);
    }

    public static void spawnFlag(String teamName)
    {
	int index = _teams.indexOf(teamName);
	L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_flagIds.get(index));
	try
	{
	    _flagSpawns.set(index, new L2Spawn(tmpl));
	    _flagSpawns.get(index).setLocx(_flagsX.get(index));
	    _flagSpawns.get(index).setLocy(_flagsY.get(index));
	    _flagSpawns.get(index).setLocz(_flagsZ.get(index));
	    _flagSpawns.get(index).setAmount(1);
	    _flagSpawns.get(index).setHeading(180);
	    _flagSpawns.get(index).setRespawnDelay(1);
	    SpawnTable.getInstance().addNewSpawn(_flagSpawns.get(index), false);
	    _flagSpawns.get(index).init();
	    _flagSpawns.get(index).getLastSpawn().setCurrentHp(999999999);
	    _flagSpawns.get(index).getLastSpawn().setTitle(teamName);
	    _flagSpawns.get(index).getLastSpawn().decayMe();
	    _flagSpawns.get(index).getLastSpawn().spawnMe(_flagSpawns.get(index).getLastSpawn().getX(), _flagSpawns.get(index).getLastSpawn().getY(), _flagSpawns.get(index).getLastSpawn().getZ());
	} catch (Exception e)
	{
	    System.out.println("CTF Engine[spawnFlag(" + teamName + ")]: exception: " + e.getStackTrace());
	}
    }

    public static void showEventHtml(L2PcInstance eventPlayer, String objectId)
    {
	try
	{
	    NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
	    TextBuilder replyMSG = new TextBuilder("<html><head><body>");
	    replyMSG.append("CTF Match<br><br><br>");
	    replyMSG.append("Current event...<br1>");
	    replyMSG.append("    ... name:&nbsp;<font color=\"00FF00\">" + _eventName + "</font><br1>");
	    replyMSG.append("    ... description:&nbsp;<font color=\"00FF00\">" + _eventDesc + "</font><br><br>");
	    if (!_started && !_joining && !_teleport)
	    {
		replyMSG.append("<center>Wait till the admin/gm start the participation.</center>");
	    } else if (!_teleport && !_started && _joining)
	    {
		if (_players.contains(eventPlayer))
		{
		    replyMSG.append("You participated already in team <font color=\"LEVEL\">" + eventPlayer._teamNameCTF + "</font><br><br>");
		    replyMSG.append("<table border=\"0\"><tr>");
		    replyMSG.append("<td width=\"200\">Wait till event start or</td>");
		    replyMSG.append("<td width=\"60\"><center><button value=\"remove\" action=\"bypass -h npc_" + objectId + "_ctf_player_leave\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></td>");
		    replyMSG.append("<td width=\"100\">your participation!</td>");
		    replyMSG.append("</tr></table>");
		} else
		{
		    replyMSG.append("You want to participate in the event?<br><br>");
		    replyMSG.append("<center><table border=\"0\">");
		    for (String team : _teams)
		    {
			replyMSG.append("<tr><td width=\"100\"><font color=\"LEVEL\">" + team + "</font>&nbsp;(" + teamPlayersCount(team) + " joined)</td>");
			replyMSG.append("<td width=\"60\"><button value=\"Join\" action=\"bypass -h npc_" + objectId + "_ctf_player_join " + team + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		    }
		    replyMSG.append("</table></center>");
		}
	    } else if (_started && !_joining && !_teleport)
	    {
		replyMSG.append("<center>CTF match is in progress.</center>");
	    }
	    replyMSG.append("</body></html>");
	    adminReply.setHtml(replyMSG.toString());
	    eventPlayer.sendPacket(adminReply);
	} catch (Exception e)
	{
	    System.out.println("CTF Engine[showEventHtlm(" + eventPlayer.getName() + ", " + objectId + ")]: exception: " + e.getStackTrace());
	}
    }

    public static synchronized void addPlayer(L2PcInstance player, String teamName)
    {
	String returnVal = null;
	if ((returnVal = addPlayerOk(teamName, player)) != null)
	{
	    player.sendMessage(returnVal);
	    return;
	}
	player._teamNameCTF = teamName;
	_players.add(player);
	setTeamPlayersCount(teamName, teamPlayersCount(teamName) + 1);
	_savePlayers.add(player.getName());
	_savePlayerTeams.add(teamName);
	player._originalKarmaCTF = player.getKarma();
	player._inEventCTF = true;
	player._posCheckerCTF = null;
    }

    public static String addPlayerOk(String teamName, L2PcInstance player)
    {
	boolean allTeamsEqual = true;
	int countBefore = -1;
	for (int playersCount : _teamPlayersCount)
	{
	    if (countBefore == -1)
	    {
		countBefore = playersCount;
	    }
	    if (countBefore != playersCount)
	    {
		allTeamsEqual = false;
		break;
	    }
	    countBefore = playersCount;
	}
	if (allTeamsEqual)
	{
	    return null;
	}
	countBefore = Integer.MAX_VALUE;
	for (int teamPlayerCount : _teamPlayersCount)
	{
	    if (teamPlayerCount < countBefore)
	    {
		countBefore = teamPlayerCount;
	    }
	}
	Vector<String> joinableTeams = new Vector<String>();
	for (String team : _teams)
	{
	    if (teamPlayersCount(team) == countBefore)
	    {
		joinableTeams.add(team);
	    }
	}
	if (joinableTeams.contains(teamName))
	{
	    return null;
	}
	return "Too many players in team \"" + teamName + "\".";
    }

    public static synchronized void addDisconnectedPlayer(L2PcInstance player)
    {
	player._teamNameCTF = _savePlayerTeams.get(_savePlayers.indexOf(player.getName()));
	_players.add(player);
	player._originalKarmaCTF = player.getKarma();
	player._inEventCTF = true;
	player._posCheckerCTF = null;
	if (_teleport || _started)
	{
	    player.setTeam(teamColor(player._teamNameCTF));
	    player.setKarma(0);
	    player.broadcastUserInfo();
	    if (_started)
	    {
		player.teleToLocation(_flagsX.get(_teams.indexOf(player._teamNameCTF)), _flagsY.get(_teams.indexOf(player._teamNameCTF)), _flagsZ.get(_teams.indexOf(player._teamNameCTF)), false);
		player._posCheckerCTF = ThreadPoolManager.getInstance().scheduleGeneral(new posChecker(player), 0);
	    }
	}
    }

    public static synchronized void removePlayer(L2PcInstance player)
    {
	_players.remove(player);
	setTeamPlayersCount(player._teamNameCTF, teamPlayersCount(player._teamNameCTF) - 1);
	player._inEventCTF = false;
    }

    private static class posChecker implements Runnable
    {
	private final L2PcInstance _player;

	posChecker(L2PcInstance player)
	{
	    _player = player;
	}

	private void processInFlagRange()
	{
	    for (String team : CTF._teams)
	    {
		if (team.equals(_player._teamNameCTF))
		{
		    int indexOwn = CTF._teams.indexOf(_player._teamNameCTF);
		    if ((_player.getX() > CTF._flagsX.get(indexOwn) - 100) && (_player.getX() < CTF._flagsX.get(indexOwn) + 100) && (_player.getY() > CTF._flagsY.get(indexOwn) - 100) && (_player.getY() < CTF._flagsY.get(indexOwn) + 100) && (_player.getZ() > CTF._flagsZ.get(indexOwn) - 100) && (_player.getZ() < CTF._flagsZ.get(indexOwn) + 100) && !CTF._flagsTaken.get(indexOwn) && _player._haveFlagCTF)
		    {
			int indexEnemy = CTF._teams.indexOf(_player._teamNameHaveFlagCTF);
			CTF._flagsTaken.set(indexEnemy, false);
			CTF.spawnFlag(_player._teamNameHaveFlagCTF);
			_player.broadcastUserInfo();
			_player._haveFlagCTF = false;
			_teamPointsCount.set(indexOwn, teamPointsCount(team) + 1);
			Announcements.getInstance().announceToAll(_eventName + "(CTF): " + _player.getName() + " scores for " + _player._teamNameCTF + ".");
			if (teamPointsCount(team) == _scoreLimit)
			{
			    Announcements.getInstance().announceToAll(_eventName + "(CTF): CTF Game is over");
			    _limitReached = true;
			    sit();
			    teleportFinish();
			    finishEvent();
			}
		    }
		} else
		{
		    int indexEnemy = CTF._teams.indexOf(team);
		    if ((_player.getX() > CTF._flagsX.get(indexEnemy) - 100) && (_player.getX() < CTF._flagsX.get(indexEnemy) + 100) && (_player.getY() > CTF._flagsY.get(indexEnemy) - 100) && (_player.getY() < CTF._flagsY.get(indexEnemy) + 100) && (_player.getZ() > CTF._flagsZ.get(indexEnemy) - 100) && (_player.getZ() < CTF._flagsZ.get(indexEnemy) + 100) && !CTF._flagsTaken.get(indexEnemy) && !_player._haveFlagCTF && !_player.isDead())
		    {
			CTF._flagsTaken.set(indexEnemy, true);
			CTF.unspawnFlag(team);
			_player._teamNameHaveFlagCTF = team;
			_player.broadcastUserInfo();
			_player._haveFlagCTF = true;
			Announcements.getInstance().announceToAll(_eventName + "(CTF): " + team + " flag taken.");
			break;
		    }
		}
	    }
	}

	private void restoreTakenFlag()
	{
	    Vector<Integer> teamsTakenFlag = new Vector<Integer>();
	    for (L2PcInstance player : CTF._players)
	    {
		if ((player != null) && player._haveFlagCTF)
		{
		    teamsTakenFlag.add(CTF._teams.indexOf(player._teamNameHaveFlagCTF));
		}
	    }
	    for (String team : CTF._teams)
	    {
		int index = CTF._teams.indexOf(team);
		if (!teamsTakenFlag.contains(index))
		{
		    if (CTF._flagsTaken.get(index))
		    {
			CTF._flagsTaken.set(index, false);
			CTF.spawnFlag(team);
			Announcements.getInstance().announceToAll(CTF._eventName + "(CTF): " + team + " flag returned.");
		    }
		}
	    }
	}

	public void run()
	{
	    for (;;)
	    {
		try
		{
		    Thread.sleep(50);
		} catch (InterruptedException ie)
		{
		    System.out.println("CTF Engine[posChecker::run()]: exception: " + ie.getStackTrace());
		}
		if (_player == null)
		{
		    System.out.println("CTF Engine[posChecker::run()]: flag owner disconnects!");
		    restoreTakenFlag();
		    break;
		}
		processInFlagRange();
	    }
	}
    }

    public static void dumpData()
    {
	System.out.println("");
	System.out.println("");
	if (!_joining && !_teleport && !_started)
	{
	    System.out.println("<<---------------------------------->>");
	    System.out.println(">> CTF Engine infos dump (INACTIVE) <<");
	    System.out.println("<<--^----^^-----^----^^------^^----->>");
	} else if (_joining && !_teleport && !_started)
	{
	    System.out.println("<<--------------------------------->>");
	    System.out.println(">> CTF Engine infos dump (JOINING) <<");
	    System.out.println("<<--^----^^-----^----^^------^----->>");
	} else if (!_joining && _teleport && !_started)
	{
	    System.out.println("<<---------------------------------->>");
	    System.out.println(">> CTF Engine infos dump (TELEPORT) <<");
	    System.out.println("<<--^----^^-----^----^^------^^----->>");
	} else if (!_joining && !_teleport && _started)
	{
	    System.out.println("<<--------------------------------->>");
	    System.out.println(">> CTF Engine infos dump (STARTED) <<");
	    System.out.println("<<--^----^^-----^----^^------^----->>");
	}
	System.out.println("Name: " + _eventName);
	System.out.println("Desc: " + _eventDesc);
	System.out.println("Join location: " + _joiningLocationName);
	System.out.println("");
	System.out.println("##########################");
	System.out.println("# _teams(Vector<String>) #");
	System.out.println("##########################");
	for (String team : _teams)
	{
	    System.out.println(team);
	}
	System.out.println("");
	System.out.println("##################################");
	System.out.println("# _players(Vector<L2PcInstance>) #");
	System.out.println("##################################");
	for (L2PcInstance player : _players)
	{
	    if (player != null)
	    {
		System.out.println("Name: " + player.getName() + "    Team: " + player._teamNameCTF);
	    }
	}
	System.out.println("");
	System.out.println("#####################################################################");
	System.out.println("# _savePlayers(Vector<String>) and _savePlayerTeams(Vector<String>) #");
	System.out.println("#####################################################################");
	for (String player : _savePlayers)
	{
	    System.out.println("Name: " + player + "    Team: " + _savePlayerTeams.get(_savePlayers.indexOf(player)));
	}
	System.out.println("");
	System.out.println("");
    }

    private static int teamColor(String teamName)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return -1;
	}
	return _teamColors.get(index);
    }

    public static int teamPointsCount(String teamName)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return -1;
	}
	return _teamPointsCount.get(index);
    }

    public static void setTeamPointsCount(String teamName, int teamKillsCount)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return;
	}
	_teamPointsCount.set(index, teamKillsCount);
    }

    public static int teamPlayersCount(String teamName)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return -1;
	}
	return _teamPlayersCount.get(index);
    }

    public static void setTeamPlayersCount(String teamName, int teamPlayersCount)
    {
	int index = _teams.indexOf(teamName);
	if (index == -1)
	{
	    return;
	}
	_teamPlayersCount.set(index, teamPlayersCount);
    }
}