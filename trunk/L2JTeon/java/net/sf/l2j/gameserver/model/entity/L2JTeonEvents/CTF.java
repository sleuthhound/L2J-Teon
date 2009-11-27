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

/**
 * 
 * @author FBIagent
 * 
 */

package net.sf.l2j.gameserver.model.entity.L2JTeonEvents;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.lang.Boolean;
import java.util.Random;
import java.util.Vector;

import javolution.text.TextBuilder;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.item.PcInventory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CTF
{  
    private final static Log _log = LogFactory.getLog(CTF.class.getName());
    
    public static String _eventName = new String(),
                         _eventDesc = new String(),
                         _topTeam = new String(),
                         _joiningLocationName = new String();
    public static Vector<String> _teams = new Vector<String>(),
                                 _savePlayers = new Vector<String>(),
                                 _savePlayerTeams = new Vector<String>();
    public static Vector<L2PcInstance> _players = new Vector<L2PcInstance>(),
                                        _playersShuffle = new Vector<L2PcInstance>();
    public static Vector<Integer> _teamPlayersCount = new Vector<Integer>(),
                                  _teamPointsCount = new Vector<Integer>(),
                                  _teamColors = new Vector<Integer>(),
                                  _flagIds = new Vector<Integer>(),
                                  _flagsX = new Vector<Integer>(),
                                  _flagsY = new Vector<Integer>(),
                                  _flagsZ = new Vector<Integer>();
    public static Vector<L2Spawn> _flagSpawns = new Vector<L2Spawn>();
    public static Vector<Boolean> _flagsTaken = new Vector<Boolean>(),
                                    _flagSpawned = new Vector<Boolean>();
    public static boolean _joining = false,
                          _teleport = false,
                          _started = false,
                          _sitForced = false;
    public static L2Spawn _npcSpawn;
    public static int _npcId = 0,
                      _npcX = 0,
                      _npcY = 0,
                      _npcZ = 0,
                      _npcHeading = 0,
                      _rewardId = 0,
                      _rewardAmount = 0,
                      _topScore = 0,
                      _minlvl = 0,
                      _maxlvl = 0;                      

    public static void setNpcPos(L2PcInstance activeChar)
    {
        _npcX = activeChar.getX();
        _npcY = activeChar.getY();
        _npcZ = activeChar.getZ();
        _npcHeading = activeChar.getHeading();
    }
    
    public static boolean checkMaxLevel(int maxlvl)
    {
        if (_minlvl >= maxlvl)
            return false;
        
        return true;
    }
    
    public static boolean checkMinLevel(int minlvl)
    {
        if (_maxlvl <= minlvl)
            return false;
        
        return true;
    }
    
    public static void addTeam(String teamName)
    {
        if (!checkTeamOk())
        {
            System.out.println("CTF Engine[addTeam(" + teamName + ")]: checkTeamOk() == false");
            return;
        }
        
        if (teamName.equals(" "))
            return;

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
        _flagSpawned.add(false);
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
            return;

        _flagsTaken.remove(index);
        _flagSpawns.remove(index);
        _flagSpawned.remove(index);
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
            return;
        
        _flagIds.set(index, npcId);
    }
    
    public static void setTeamPos(String teamName, L2PcInstance activeChar)
    {
        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return;
        
        _flagsX.set(index, activeChar.getX());
        _flagsY.set(index, activeChar.getY());
        _flagsZ.set(index, activeChar.getZ());
    }
    
    public static void setTeamColor(String teamName, int color)
    {
        if (!checkTeamOk())
            return;

        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return;

        _teamColors.set(index, color);
    }
    
    public static boolean checkTeamOk()
    {
        if (_started || _teleport || _joining)
            return false;
        
        return true;
    }
    
    public static void startJoin(L2PcInstance activeChar)
    {
        if (!startJoinOk())
        {
            System.out.println("CTF Engine[startJoin(" + activeChar.getName() + ")]: startJoinOk() == false");
            return;
        }
        
        _joining = true;
        spawnEventNpc();
        Announcements.getInstance().announceToAll(_eventName + "(CTF): Joinable in " + _joiningLocationName + "!");
    }
    
    private static boolean startJoinOk()
    {
        if (_started || _teleport || _joining || _teams.size() < 2 || _eventName.equals("") ||
            _joiningLocationName.equals("") || _eventDesc.equals("") || _npcId == 0 ||
            _npcX == 0 || _npcY == 0 || _npcZ == 0 || _rewardId == 0 || _rewardAmount == 0 ||
            _flagIds.contains(0) || _flagsX.contains(0) || _flagsY.contains(0) || _flagsZ.contains(0))
            return false;
        
        return true;
    }
    
    private static void spawnEventNpc()
    {
        L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_npcId);

        try
        {
            _npcSpawn = new L2Spawn(tmpl);

            _npcSpawn.setLocx(_npcX);
            _npcSpawn.setLocy(_npcY);
            _npcSpawn.setLocz(_npcZ);
            _npcSpawn.setAmount(1);
            _npcSpawn.setHeading(_npcHeading);
            _npcSpawn.setRespawnDelay(1);

            SpawnTable.getInstance().addNewSpawn(_npcSpawn, false);

            _npcSpawn.init();
            _npcSpawn.getLastSpawn().setCurrentHp(999999999);
            _npcSpawn.getLastSpawn().setTitle(_eventName);
            _npcSpawn.getLastSpawn()._isEventMobCTF = true;
            _npcSpawn.getLastSpawn().isAggressive();
            _npcSpawn.getLastSpawn().decayMe();
            _npcSpawn.getLastSpawn().spawnMe(_npcSpawn.getLastSpawn().getX(), _npcSpawn.getLastSpawn().getY(), _npcSpawn.getLastSpawn().getZ());

            _npcSpawn.getLastSpawn().broadcastPacket(new MagicSkillUser(_npcSpawn.getLastSpawn(), _npcSpawn.getLastSpawn(), 1034, 1, 1, 1));
        }
        catch (Exception e)
        {
            _log.warn("CTF Engine[spawnEventNpc()]: exception: " + e);
        }
    }
    
    public static void teleportStart()
    {
        if (!startTeleportOk()) 
        { 
            System.out.println("CTF Engine[teleportStart()]: startTeleportOk() == false"); 
            return; 
        } 
        
        _joining = false;
        Announcements.getInstance().announceToAll(_eventName + "(CTF): Teleport to team flag in 20 seconds!");

        if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
            shuffleTeams();
        
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
                                                                               s.unSummon(player);
                                                                       }

                                                                       if (Config.CTF_ON_START_REMOVE_ALL_EFFECTS)
                                                                       {
                                                                           for (L2Effect e : player.getAllEffects())
                                                                           {
                                                                               if (e != null)
                                                                                   e.exit();
                                                                           }
                                                                       }
                                                                       
                                                                       player.teleToLocation(_flagsX.get(_teams.indexOf(player._teamNameCTF)), _flagsY.get(_teams.indexOf(player._teamNameCTF)), _flagsZ.get(_teams.indexOf(player._teamNameCTF)),false);
                                                                   }
                                                               }
                                                           }
                                                       }, 20000);
        
        _teleport = true;
    }
    
    private static boolean startTeleportOk()
    {
        if (!_joining || _started || _teleport)
            return false;

        return true;
    }
    
    public static void shuffleTeams()
    {
        int teamCount = 0,
            playersCount = 0;

        for (;;)
        {
            if (_playersShuffle.isEmpty())
                break;

            int playerToAddIndex = new Random().nextInt(_playersShuffle.size());
            
            _players.add(_playersShuffle.get(playerToAddIndex));
            _players.get(playersCount)._inEventCTF = true;
            _players.get(playersCount)._teamNameCTF = _teams.get(teamCount);
            _savePlayers.add(_players.get(playersCount).getName());
            _savePlayerTeams.add(_teams.get(teamCount));
            playersCount++;

            if (teamCount == _teams.size()-1)
                teamCount = 0;
            else
                teamCount++;
            
            _playersShuffle.remove(playerToAddIndex);
        }
    }
    
    public static void setUserData()
    {
        for (L2PcInstance player : _players)
        {
            player.setNameColor(_teamColors.get(_teams.indexOf(player._teamNameCTF)));
            player.setKarma(0);
            player.broadcastUserInfo();
        }
    }
    
    private static void spawnAllFlags()
    {
        for (String team : _teams)
        {
            spawnFlag(team);            
        }
    }
    
    public static void startEvent()
    {
        if (!startEventOk())
        {
            System.out.println("CTF Engine[startEvent()]: start conditions wrong");
            return;
        }
        
        _teleport = false;
        spawnAllFlags();
        sit();
        
        
        for (L2PcInstance player : _players)
        {
            if (player != null)
                player._posCheckerCTF = ThreadPoolManager.getInstance().scheduleGeneral(new posChecker(player), 0);
        }

        Announcements.getInstance().announceToAll(_eventName + "(CTF): Started. Go to capture the flags!");
        _started = true;
    }
    private static boolean startEventOk()
    {
        if (_joining || !_teleport || _started)
            return false;
        
        if (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE"))
        {
            if (_teamPlayersCount.contains(0))
                return false;
        }
        else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
        {
            Vector<L2PcInstance> playersShuffleTemp = new Vector<L2PcInstance>();
            int loopCount = 0;
            
            loopCount = _playersShuffle.size();

            for (int i=0;i<loopCount;i++)
            {
                if (_playersShuffle != null)
                    playersShuffleTemp.add(_playersShuffle.get(i));
            }
            
            _playersShuffle = playersShuffleTemp; 
            playersShuffleTemp.clear();            
          
          }
        
        return true;
    }
    
    public static void finishEvent(L2PcInstance activeChar)
    {
        if (!finishEventOk())
        {
            System.out.println("CTF Engine[finishEvent(" + activeChar.getName() + ")]: finishEventOk() == false");
            return;
        }
        
        unspawnEventNpc();        
        processTopTeam();

        if (_topScore == 0)
            Announcements.getInstance().announceToAll(_eventName + "(CTF): No team win the match(no one scores).");
        else
        {
            Announcements.getInstance().announceToAll(_eventName + "(CTF): " + _topTeam + " win the match! " + _topScore + " score.");
            rewardTeam(activeChar, _topTeam);
        }
        
        teleportFinish();
    }
    
    private static boolean finishEventOk()
    {
        if (!_started)
            return false;
        
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
    
    public static void rewardTeam(L2PcInstance activeChar, String teamName)
    {
        for (L2PcInstance player : _players)
        {
            if (player != null)
            {
                if (player._teamNameCTF.equals(teamName))
                {
                    PcInventory inv = player.getInventory();
                
                    if (ItemTable.getInstance().createDummyItem(_rewardId).isStackable())
                        inv.addItem("CTF Event: " + _eventName, _rewardId, _rewardAmount, player, activeChar.getTarget());
                    else
                    {
                        for (int i=0;i<=_rewardAmount-1;i++)
                            inv.addItem("CTF Event: " + _eventName, _rewardId, 1, player, activeChar.getTarget());
                    }
                
                    SystemMessage sm;

                    if (_rewardAmount > 1)
                    {
                        sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
                        sm.addItemName(_rewardId);
                        sm.addNumber(_rewardAmount);
                        player.sendPacket(sm);
                    }
                    else
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
    
    public static void abortEvent()
    {
        if (!_joining && !_teleport && !_started)
            return;
        
        _joining = false;
        _teleport = false;
        
        unspawnEventNpc();
        Announcements.getInstance().announceToAll(_eventName + "(CTF): Match aborted!");
        teleportFinish();        
    }

    public static void sit()
    {
        if (_sitForced)
            _sitForced = false;
        else
            _sitForced = true;
        
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
                        player.sitDown();
                }
                else
                {
                    if (player.isSitting())
                        player.standUp();
                }
            }
        }
        for (L2PcInstance player : _players)
        {
            //Remove Buffs
            for (L2Effect e : player.getAllEffects())
                e.exit();
            
            //Remove Summon's buffs
            if (player.getPet() != null)
            {
                L2Summon summon = player.getPet();
                for (L2Effect e : summon.getAllEffects())
                    e.exit();
                
                if (summon instanceof L2PetInstance)
                    summon.unSummon(player);
            }
            
            //Remove player from his party
            if (player.getParty() != null)
            {
                L2Party party = player.getParty();
                party.removePartyMember(player);
            }
        }
    }
    
    public static void clean()
    {
        for (String team : _teams)
        {
            int index = _teams.indexOf(team);

            _teamPlayersCount.set(index, 0);
            _teamPointsCount.set(index, 0);            
        }
        
        for (L2PcInstance player : _players)
        {
            removePlayer(player);
        }

        _topScore = 0;
        _topTeam = new String();
        _players = new Vector<L2PcInstance>();
        _playersShuffle = new Vector<L2PcInstance>();
        _savePlayers = new Vector<String>();
        _savePlayerTeams = new Vector<String>();
        _flagSpawns = new Vector<L2Spawn>();
        _flagsTaken = new Vector<Boolean>();
        _flagSpawned = new Vector<Boolean>();
    }
    
    public static void unspawnEventNpc()
    {
        if (_npcSpawn == null)
            return;

        _npcSpawn.getLastSpawn().deleteMe();
        _npcSpawn.stopRespawn();
        SpawnTable.getInstance().deleteSpawn(_npcSpawn, true);
    }
    
    public static void unspawnAllFlags()
    {
        for (String team : _teams)
        {
            unspawnFlag(team);
        }
    }
    
    public static void teleportFinish()
    {
        Announcements.getInstance().announceToAll(_eventName + "(CTF): Teleport back to participation NPC in 20 seconds!");

        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                                                       {
                                                            public void run()
                                                            {
                                                                for (L2PcInstance player : _players)
                                                                {
                                                                    if (player !=  null)
                                                                        player.teleToLocation(_npcX, _npcY, _npcZ, false);
                                                                }
                                                                if (_started){
                                                                    unspawnAllFlags();
                                                                    _started = false;
                                                                }
                                                                CTF.clean();
                                                            }
                                                       }, 20000);
    }
    
    public static void unspawnFlag(String teamName)
    {
        int index = _teams.indexOf(teamName);
        
        if (_flagSpawned.get(index))
        {
            _flagSpawns.get(index).stopRespawn();
            _flagSpawns.get(index).getLastSpawn().deleteMe();
            SpawnTable.getInstance().deleteSpawn(_flagSpawns.get(index), true);
            _flagSpawned.set(index,false);
        }
        else{
            _log.info("CTF Engine[can't unspawnFlag(" + teamName + ")]: flag not spawned");
        }
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
            _flagSpawns.get(index).setHeading(0);
            _flagSpawns.get(index).setRespawnDelay(1);

            SpawnTable.getInstance().addNewSpawn(_flagSpawns.get(index), false);

            _flagSpawns.get(index).init();
            _flagSpawns.get(index).getLastSpawn().setCurrentHp(999999999);
            _flagSpawns.get(index).getLastSpawn().setTitle(teamName);
            _flagSpawns.get(index).getLastSpawn()._isEventMobCTF = false;
            _flagSpawns.get(index).getLastSpawn().decayMe();
            _flagSpawns.get(index).getLastSpawn().spawnMe(_flagSpawns.get(index).getLastSpawn().getX(), _flagSpawns.get(index).getLastSpawn().getY(), _flagSpawns.get(index).getLastSpawn().getZ());
            _flagSpawned.set(index,true);
        }
        catch(Exception e)
        {
            _log.warn("CTF Engine[spawnFlag(" + teamName + ")]: exception: " + e);
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
                replyMSG.append("<center>Wait till the admin/gm start the participation.</center>");
            else if (!_teleport && !_started && _joining && eventPlayer.getLevel()>=_minlvl && eventPlayer.getLevel()<_maxlvl)
            {
                if (_players.contains(eventPlayer) || _playersShuffle.contains(eventPlayer))
                {
                    if (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE"))
                        replyMSG.append("You participated already in team <font color=\"LEVEL\">" + eventPlayer._teamNameCTF + "</font><br><br>");
                    else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
                        replyMSG.append("You participated already!<br><br>");

                    replyMSG.append("<table border=\"0\"><tr>");
                    replyMSG.append("<td width=\"200\">Wait till event start or</td>");
                    replyMSG.append("<td width=\"60\"><center><button value=\"remove\" action=\"bypass -h npc_" + objectId + "_ctf_player_leave\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></td>");
                    replyMSG.append("<td width=\"100\">your participation!</td>");
                    replyMSG.append("</tr></table>");
                }
                else
                {
                    replyMSG.append("You want to participate in the event?<br><br>");
                    replyMSG.append("<td width=\"200\">Admin set min lvl : <font color=\"00FF00\">" + _minlvl + "</font></td><br>");
                    replyMSG.append("<td width=\"200\">Admin set max lvl : <font color=\"00FF00\">" + _maxlvl + "</font></td><br><br>");

                    if (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE"))
                    {
                        replyMSG.append("<center><table border=\"0\">");
                    
                        for (String team : _teams)
                        {
                            replyMSG.append("<tr><td width=\"100\"><font color=\"LEVEL\">" + team + "</font>&nbsp;(" + teamPlayersCount(team) + " joined)</td>");
                            replyMSG.append("<td width=\"60\"><button value=\"Join\" action=\"bypass -h npc_" + objectId + "_ctf_player_join " + team + "\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
                        }
                    
                        replyMSG.append("</table></center>");
                    }
                    else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
                    {
                        replyMSG.append("<center><table border=\"0\">");
                        
                        for (String team : _teams)
                            replyMSG.append("<tr><td width=\"100\"><font color=\"LEVEL\">" + team + "</font></td>");
                    
                        replyMSG.append("</table></center><br>");
                        
                        replyMSG.append("<button value=\"Join\" action=\"bypass -h npc_" + objectId + "_ctf_player_join eventShuffle\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
                        replyMSG.append("Teams will be reandomly generated!");
                    }
                }
            }
            else if (_started && !_joining && !_teleport)
                replyMSG.append("<center>CTF match is in progress.</center>");
            else if (eventPlayer.getLevel() < _minlvl || eventPlayer.getLevel() > _maxlvl )
            {
                replyMSG.append("Your lvl : <font color=\"00FF00\">" + eventPlayer.getLevel() +"</font><br>");
                replyMSG.append("Admin set min lvl : <font color=\"00FF00\">" + _minlvl + "</font><br>");
                replyMSG.append("Admin set max lvl : <font color=\"00FF00\">" + _maxlvl + "</font><br><br>");
                replyMSG.append("<font color=\"FFFF00\">You can't participate to this event.</font><br>");
            }
            
            replyMSG.append("</body></html>");
            adminReply.setHtml(replyMSG.toString());
            eventPlayer.sendPacket(adminReply);
        }
        catch (Exception e)
        {
            _log.warn("CTF Engine[showEventHtlm(" + eventPlayer.getName() + ", " + objectId + ")]: exception" + e.getMessage());
        }
    }
    
    public static synchronized void addPlayer(L2PcInstance player, String teamName)
    {
        if (!addPlayerOk(teamName,player))
        {
            player.sendMessage("Too many players in team \"" + teamName + "\".");
            return;
        }
        
        if (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE"))
        {
            player._teamNameCTF = teamName;
            _players.add(player);
            setTeamPlayersCount(teamName, teamPlayersCount(teamName)+1);
            _savePlayers.add(player.getName());
            _savePlayerTeams.add(teamName);
        }
        else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
            _playersShuffle.add(player);
                
        player._originalTitleCTF = player.getTitle();
        player._originalNameColorCTF = player.getNameColor();
        player._originalKarmaCTF = player.getKarma();
        player._inEventCTF = true;
        player._posCheckerCTF = null;
    }
    
    public static boolean addPlayerOk(String teamName, L2PcInstance eventPlayer)
    {
       /* if (TvTEvent._savePlayers.contains(eventPlayer.getName()))
        {
            eventPlayer.sendMessage("You have already participated in another event!");
            return false;
        }*/

        if (Config.CTF_EVEN_TEAMS.equals("NO"))
            return true;
        else if (Config.CTF_EVEN_TEAMS.equals("BALANCE"))
        {
            boolean allTeamsEqual = true;
            int countBefore = -1;
        
            for (int playersCount : _teamPlayersCount)
            {
                if (countBefore == -1)
                    countBefore = playersCount;
            
                if (countBefore != playersCount)
                {
                    allTeamsEqual = false;
                    break;
                }
            
                countBefore = playersCount;
            }
        
            if (allTeamsEqual)
                return true;

            countBefore = Integer.MAX_VALUE;
        
            for (int teamPlayerCount : _teamPlayersCount)
            {
                if (teamPlayerCount < countBefore)
                    countBefore = teamPlayerCount;
            }

            Vector<String> joinableTeams = new Vector<String>();
        
            for (String team : _teams)
            {
                if (teamPlayersCount(team) == countBefore)
                    joinableTeams.add(team);
            }
        
            if (joinableTeams.contains(teamName))
                return true;
        }
        else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
            return true;

        return false;
    }
    
    public static synchronized void addDisconnectedPlayer(L2PcInstance player)
    {
        if ((Config.CTF_EVEN_TEAMS.equals("SHUFFLE") && (_teleport || _started)) || (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE")))
        {
            player._teamNameCTF = _savePlayerTeams.get(_savePlayers.indexOf(player.getName()));
            _players.add(player);
            player._originalTitleCTF = player.getTitle();
            player._originalNameColorCTF = player.getNameColor();
            player._originalKarmaCTF = player.getKarma();
            player._inEventCTF = true;
            player._posCheckerCTF = null;

            if (_teleport || _started)
            {
                player.setNameColor(_teamColors.get(_teams.indexOf(player._teamNameCTF)));
                player.setKarma(0);
                player.broadcastUserInfo();

                if (_started)
                {
                    player.teleToLocation(_flagsX.get(_teams.indexOf(player._teamNameCTF)), _flagsY.get(_teams.indexOf(player._teamNameCTF)), _flagsZ.get(_teams.indexOf(player._teamNameCTF)), false);
                    player._posCheckerCTF = ThreadPoolManager.getInstance().scheduleGeneral(new posChecker(player), 0);
                }
            }
        }
    }
    
    public static synchronized void removePlayer(L2PcInstance player)
    {
        if (player != null)
        {
            if (Config.CTF_EVEN_TEAMS.equals("NO") || Config.CTF_EVEN_TEAMS.equals("BALANCE"))
            {
                _players.remove(player);
                setTeamPlayersCount(player._teamNameCTF, teamPlayersCount(player._teamNameCTF)-1);
                player._inEventCTF = false;
            }
            else if (Config.CTF_EVEN_TEAMS.equals("SHUFFLE"))
                _playersShuffle.remove(player);
            
            player.setNameColor(player._originalNameColorCTF);
            player.setKarma(player._originalKarmaCTF);
            player.setTitle(player._originalTitleCTF);
            player.broadcastUserInfo();
            player._teamNameCTF = new String();
            player._inEventCTF = false;
            player._haveFlagCTF = false;
            player._posCheckerCTF.cancel(true);
            player._posCheckerCTF = null;
        }
    }
    
    private static class posChecker implements Runnable
    {
        private L2PcInstance _player;

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
                    
                    if (_player.getX() > CTF._flagsX.get(indexOwn)-100 && _player.getX() < CTF._flagsX.get(indexOwn)+100 &&
                        _player.getY() > CTF._flagsY.get(indexOwn)-100 && _player.getY() < CTF._flagsY.get(indexOwn)+100 &&
                        _player.getZ() > CTF._flagsZ.get(indexOwn)-100 && _player.getZ() < CTF._flagsZ.get(indexOwn)+100 &&
                        !CTF._flagsTaken.get(indexOwn) && _player._haveFlagCTF)
                    {
                        int indexEnemy = CTF._teams.indexOf(_player._teamNameHaveFlagCTF);

                        _flagsTaken.set(indexEnemy, false);
                        spawnFlag(_player._teamNameHaveFlagCTF);
                        _player.setTitle(_player._originalTitleCTF);
                        _player.broadcastUserInfo();
                        _player._haveFlagCTF = false;
                        _teamPointsCount.set(indexOwn, teamPointsCount(team)+1);
                        Announcements.getInstance().announceToAll(_eventName + "(CTF): " + _player.getName() + " scores for " + _player._teamNameCTF + ".");
                    }
                }
                else
                {
                    int indexEnemy = CTF._teams.indexOf(team);
                    
                    if ((_player.getX() > CTF._flagsX.get(indexEnemy)-100 && _player.getX() < CTF._flagsX.get(indexEnemy)+100) &&
                        (_player.getY() > CTF._flagsY.get(indexEnemy)-100 && _player.getY() < CTF._flagsY.get(indexEnemy)+100) &&
                        (_player.getZ() > CTF._flagsZ.get(indexEnemy)-100 && _player.getZ() < CTF._flagsZ.get(indexEnemy)+100) &&
                        !CTF._flagsTaken.get(indexEnemy) && !_player._haveFlagCTF && !_player.isDead())
                    {
                        _flagsTaken.set(indexEnemy, true);
                        unspawnFlag(team);
                        _player._teamNameHaveFlagCTF = team;
                        _player.setTitle("Flag Owner");
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
                if (player != null && player._haveFlagCTF)              
                    teamsTakenFlag.add(CTF._teams.indexOf(player._teamNameHaveFlagCTF));
            }
            
            for (String team : CTF._teams)
            {
                int index = CTF._teams.indexOf(team);
                
                if (!teamsTakenFlag.contains(index))
                {
                    if (CTF._flagsTaken.get(index))
                    {
                        _flagsTaken.set(index, false);
                        spawnFlag(team);
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
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    _log.warn("CTF Engine[posChecker::run()]: exception: " + e.getMessage());
                }
                
                if (_player == null)
                {
                    _log.warn("CTF Engine[posChecker::run()]: flag owner disconnects!");
                    restoreTakenFlag();
                    break;
                }
                else
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
        }
        else if (_joining && !_teleport && !_started)
        {
            System.out.println("<<--------------------------------->>");
            System.out.println(">> CTF Engine infos dump (JOINING) <<");
            System.out.println("<<--^----^^-----^----^^------^----->>");
        }
        else if (!_joining && _teleport && !_started)
        {
            System.out.println("<<---------------------------------->>");
            System.out.println(">> CTF Engine infos dump (TELEPORT) <<");
            System.out.println("<<--^----^^-----^----^^------^^----->>");
        }
        else if (!_joining && !_teleport && _started)
        {
            System.out.println("<<--------------------------------->>");
            System.out.println(">> CTF Engine infos dump (STARTED) <<");
            System.out.println("<<--^----^^-----^----^^------^----->>");
        }

        System.out.println("Name: " + _eventName);
        System.out.println("Desc: " + _eventDesc);
        System.out.println("Join location: " + _joiningLocationName);
        System.out.println("NPC Id: " + _npcId);
        System.out.println("NPC X: " + _npcX);
        System.out.println("NPC Y: " + _npcY);
        System.out.println("NPC Z: " + _npcZ);
        System.out.println("Reward Id: " + _rewardId);
        System.out.println("Reward Amount: " + _rewardAmount);        
        System.out.println("Min lvl: " + _minlvl);
        System.out.println("Max lvl: " + _maxlvl);
        System.out.println("Total Teams: " + _teams.size());
        System.out.println("");
        System.out.println("##########################");
        System.out.println("# _teams(Vector<String>) #");
        System.out.println("##########################");
        
        for (String team : _teams)
        {
            int index = _teams.indexOf(team);            
            if (index == -1)
                return;
            System.out.println("");
            System.out.println("Team Id: " + index);
            System.out.println("Team Name: " + team);
            System.out.println("Flag Id: " + _flagIds.get(index));
            System.out.println("Flag X: " + _flagsX.get(index));
            System.out.println("Flag Y: " + _flagsY.get(index));
            System.out.println("Flag Z: " + _flagsZ.get(index));
            System.out.println("Team Collor: " + _teamColors.get(index));
            System.out.println("");
            System.out.println("##########################");
        }
        
        System.out.println("");
        System.out.println("#########################################");
        System.out.println("# _playersShuffle(Vector<L2PcInstance>) #");
        System.out.println("#########################################");
        
        for (L2PcInstance player : _playersShuffle)
        {
            if (player != null)
                System.out.println("Name: " + player.getName());
        }
        
        System.out.println("");
        System.out.println("##################################");
        System.out.println("# _players(Vector<L2PcInstance>) #");
        System.out.println("##################################");
        
        for (L2PcInstance player : _players)
        {
            if (player != null)
                System.out.println("Name: " + player.getName() + "    Team: " + player._teamNameCTF);
        }
        
        System.out.println("");
        System.out.println("#####################################################################");
        System.out.println("# _savePlayers(Vector<String>) and _savePlayerTeams(Vector<String>) #");
        System.out.println("#####################################################################");
        
        for (String player : _savePlayers)
            System.out.println("Name: " + player + "    Team: " + _savePlayerTeams.get(_savePlayers.indexOf(player)));
        
        System.out.println("");
        System.out.println("");
    }
    
    public static void loadData()
    {
        _eventName = new String();
        _eventDesc = new String();
        _topTeam = new String();
        _joiningLocationName = new String();
        _teams = new Vector<String>();
        _savePlayers = new Vector<String>();
        _savePlayerTeams = new Vector<String>();
        _players = new Vector<L2PcInstance>();
        _playersShuffle = new Vector<L2PcInstance>();
        _flagSpawns = new Vector<L2Spawn>();
        _flagsTaken = new Vector<Boolean>();
        _flagSpawned = new Vector<Boolean>();
        _teamPlayersCount = new Vector<Integer>();
        _teamPointsCount = new Vector<Integer>();
        _teamColors = new Vector<Integer>();
        _flagIds = new Vector<Integer>();
        _flagsX = new Vector<Integer>();
        _flagsY = new Vector<Integer>();
        _flagsZ = new Vector<Integer>();
        _joining = false;
        _teleport = false;
        _started = false;
        _sitForced = false;
        
        
        java.sql.Connection con = null;
        try
        {
            PreparedStatement statement;
            ResultSet rs;

            con = L2DatabaseFactory.getInstance().getConnection();

            statement = con.prepareStatement("Select * from ctf");
            rs = statement.executeQuery();
            
            int teams =0;
            
            while (rs.next())
            {        
                _eventName = rs.getString("eventNane");
                _eventDesc = rs.getString("eventDesc");
                _joiningLocationName = rs.getString("joiningLocation");
                _minlvl = rs.getInt("minlvl");
                _maxlvl = rs.getInt("maxlvl");
                _npcId = rs.getInt("npcId");
                _npcX = rs.getInt("npcX");
                _npcY = rs.getInt("npcY");
                _npcZ = rs.getInt("npcZ");
                _npcHeading = rs.getInt("npcHeading");
                _rewardId = rs.getInt("rewardId");
                _rewardAmount = rs.getInt("rewardAmount"); 
                teams = rs.getInt("teamsCount");
            
            }                    
            statement.close();            
            
            int index = -1;
            if (teams > 0)
                index = 0;    
            while (index < teams && index > -1)
            { 
                statement = con.prepareStatement("Select * from ctf_teams where teamId = ?");
                statement.setInt(1, index);
                rs = statement.executeQuery(); 
                while (rs.next())
                {
                    _teams.add(rs.getString("teamName"));
                    _teamPlayersCount.add(0);
                    _teamPointsCount.add(0); 
                    _teamColors.add(0);
                    _flagIds.add(0);
                    _flagsX.add(0);
                    _flagsY.add(0);
                    _flagsZ.add(0);
                    _flagSpawns.add(null);
                    _flagsTaken.add(false);
                    _flagSpawned.add(false);
                    _flagIds.set(index, rs.getInt("flagId"));
                    _flagsX.set(index, rs.getInt("flagX"));
                    _flagsY.set(index, rs.getInt("flagY"));
                    _flagsZ.set(index, rs.getInt("flagZ"));
                    _teamColors.set(index, rs.getInt("teamColor"));                    
                }                
                index ++;
                statement.close();            
            }       
        }        
        catch (Exception e)
        {
            System.out.println("Exception: CTF.loadData(): " + e.getMessage());
        }
        finally {try { con.close(); } catch (Exception e) {}}
    }
    
    public static void saveData()
    {
        java.sql.Connection con = null;
        try
        {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement;
            
            statement = con.prepareStatement("Delete from ctf");
            statement.execute();
            statement.close();

            statement = con.prepareStatement("INSERT INTO ctf (eventNane, eventDesc, joiningLocation, minlvl, maxlvl, npcId, npcX, npcY, npcZ, npcHeading, rewardId, rewardAmount, teamsCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");  
            statement.setString(1, _eventName);
            statement.setString(2, _eventDesc);
            statement.setString(3, _joiningLocationName);
            statement.setInt(4, _minlvl);
            statement.setInt(5, _maxlvl);
            statement.setInt(6, _npcId);
            statement.setInt(7, _npcX);
            statement.setInt(8, _npcY);
            statement.setInt(9, _npcZ);
            statement.setInt(10, _npcHeading);
            statement.setInt(11, _rewardId);
            statement.setInt(12, _rewardAmount);
            statement.setInt(13, _teams.size());
            statement.execute();
            statement.close();
            
            statement = con.prepareStatement("Delete from ctf_teams");
            statement.execute();
            statement.close();
            
            for (String teamName : _teams)
            { 
                int index = _teams.indexOf(teamName);
                
                if (index == -1)
                    return;
                statement = con.prepareStatement("INSERT INTO ctf_teams (teamId ,teamName, flagId, flagX, flagY, flagZ, teamColor) VALUES (?, ?, ?, ?, ?, ?, ?)");  
                statement.setInt(1 , index);
                statement.setString(2, teamName);
                statement.setInt(3, _flagIds.get(index));
                statement.setInt(4, _flagsX.get(index));
                statement.setInt(5, _flagsY.get(index));
                statement.setInt(6, _flagsZ.get(index));
                statement.setInt(7, _teamColors.get(index));                
                statement.execute();
                statement.close();
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception: CTF.saveData(): " + e.getMessage());
        }        
        finally {try { con.close(); } catch (Exception e) {}}
    }

    public static int teamPointsCount(String teamName)
    {
        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return -1;

        return _teamPointsCount.get(index);
    }
    
    public static void setTeamPointsCount(String teamName, int teamKillsCount)
    {
        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return;

        _teamPointsCount.set(index, teamKillsCount);
    }
    
    public static int teamPlayersCount(String teamName)
    {
        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return -1;

        return _teamPlayersCount.get(index);
    }
    
    public static void setTeamPlayersCount(String teamName, int teamPlayersCount)
    {
        int index = _teams.indexOf(teamName);
        
        if (index == -1)
            return;
        
        _teamPlayersCount.set(index, teamPlayersCount);
    }
}