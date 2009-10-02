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

package net.sf.l2j.gameserver.instancemanager.grandbosses;

import java.util.logging.Logger;
import java.util.concurrent.ScheduledFuture;
import java.util.Date;
import java.util.List;
import javolution.util.FastList;
import java.util.Map;
import javolution.util.FastMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.GrandBossState;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.Earthquake;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.util.Rnd;

/**
 * 
 * This class ...
 * control for sequence of fight against Baium.
 * @version $Revision: $ $Date: $
 * @author  L2J_JP SANDMAN
 */
public class BaiumManager
{
    protected static Logger _log = Logger.getLogger(BaiumManager.class.getName());
    private static BaiumManager _instance = new BaiumManager();

    // location of Statue of Baium
    private final int _StatueofBaiumId = 29025;
    private final int _StatueofBaiumLocation[] = {116067,17484,10110,41740};
    protected L2Spawn _StatueofBaiumSpawn = null;
    
    // location of arcangels.
    private final int _Angellocation[][] = 
    	{
			{ 113004, 16209, 10076, 60242 },
			{ 114053, 16642, 10076, 4411 },
			{ 114563, 17184, 10076, 49241 },
			{ 116356, 16402, 10076, 31109 },
			{ 115015, 16393, 10076, 32760 },
			{ 115481, 15335, 10076, 16241 },
			{ 114680, 15407, 10051, 32485 },
			{ 114886, 14437, 10076, 16868 },
			{ 115391, 17593, 10076, 55346 },
			{ 115245, 17558, 10076, 35536 }
		};
    protected List<L2Spawn> _AngelSpawn1 = new FastList<L2Spawn>();
    protected List<L2Spawn> _AngelSpawn2 = new FastList<L2Spawn>();
    @SuppressWarnings("unchecked")
	protected Map<Integer,List> _AngelSpawn = new FastMap<Integer,List>();
    List<L2NpcInstance> _Angels = new FastList<L2NpcInstance>();

    // location of teleport cube.
    private final int _TeleportCubeId = 29055;
    private final int _TeleportCubeLocation[][] =
    	{
    		{115203,16620,10078,0}
    	};
    protected List<L2Spawn> _TeleportCubeSpawn = new FastList<L2Spawn>();
    protected List<L2NpcInstance> _TeleportCube = new FastList<L2NpcInstance>();
    
    // list of intruders.
    protected List<L2PcInstance> _PlayersInLair = new FastList<L2PcInstance>();

    // instance of statue of Baium.
    protected L2NpcInstance _npcbaium;

    // spawn data of monsters.
    protected Map<Integer,L2Spawn> _MonsterSpawn = new FastMap<Integer,L2Spawn>();

    // instance of monsters.
    protected List<L2NpcInstance> _Monsters = new FastList<L2NpcInstance>();

    // tasks.
    @SuppressWarnings("unchecked")
	protected ScheduledFuture _CubeSpawnTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _MonsterSpawnTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _IntervalEndTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _ActivityTimeEndTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _OnPlayersAnnihilatedTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _SocialTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _MobiliseTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _MoveAtRandomTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _SocialTask2 = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _RecallPcTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _KillPcTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _CallAngelTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _SleepCheckTask = null;
    @SuppressWarnings("unchecked")
    protected ScheduledFuture _SpeakTask = null;
    
    // status in lair.
    protected GrandBossState _State = new GrandBossState(29020);
    protected String _ZoneType;
    protected String _QuestName;
    protected long _LastAttackTime = 0;
    protected String _Words = ",Don't obstruct my sleep! Die!";
    
    // location of banishment
    private final int _BanishmentLocation[][] =
    	{
    		{108784, 16000, -4928},
    		{113824, 10448, -5164},
    		{115488, 22096, -5168}
		};

    public BaiumManager()
    {
    }

    public static BaiumManager getInstance()
    {
        if (_instance == null) _instance = new BaiumManager();

        return _instance;
    }

    // initialize
    public void init()
    {
    	// initialize status in lair.
    	_PlayersInLair.clear();
        _ZoneType = "Lair of Baium";
        _QuestName = "baium";

        // setting spawn data of monsters.
        try
        {
            L2NpcTemplate template1;
            L2Spawn tempSpawn;
            
            // Statue of Baium
            template1 = NpcTable.getInstance().getTemplate(_StatueofBaiumId);
            _StatueofBaiumSpawn = new L2Spawn(template1);
            _StatueofBaiumSpawn.setAmount(1);
            _StatueofBaiumSpawn.setLocx(_StatueofBaiumLocation[0]);
            _StatueofBaiumSpawn.setLocy(_StatueofBaiumLocation[1]);
            _StatueofBaiumSpawn.setLocz(_StatueofBaiumLocation[2]);
            _StatueofBaiumSpawn.setHeading(_StatueofBaiumLocation[3]);
            _StatueofBaiumSpawn.setRespawnDelay(Config.FWB_ACTIVITYTIMEOFBAIUM * 2);
            _StatueofBaiumSpawn.stopRespawn();
            SpawnTable.getInstance().addNewSpawn(_StatueofBaiumSpawn, false);
            
            // Baium.
            template1 = NpcTable.getInstance().getTemplate(29020);
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setAmount(1);
            tempSpawn.setRespawnDelay(Config.FWB_ACTIVITYTIMEOFBAIUM * 2);
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            _MonsterSpawn.put(29020, tempSpawn);
        }
        catch (Exception e)
        {
            _log.warning(e.getMessage());
        }

        // setting spawn data of teleport cube.
        try
        {
            L2NpcTemplate Cube = NpcTable.getInstance().getTemplate(_TeleportCubeId);
            L2Spawn spawnDat;
            for(int i = 0;i < _TeleportCubeLocation.length; i++)
            {
                spawnDat = new L2Spawn(Cube);
                spawnDat.setAmount(1);
                spawnDat.setLocx(_TeleportCubeLocation[i][0]);
                spawnDat.setLocy(_TeleportCubeLocation[i][1]);
                spawnDat.setLocz(_TeleportCubeLocation[i][2]);
                spawnDat.setHeading(_TeleportCubeLocation[i][3]);
                spawnDat.setRespawnDelay(60);
                spawnDat.setLocation(0);
                SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                _TeleportCubeSpawn.add(spawnDat);
            }
        }
        catch (Exception e)
        {
            _log.warning(e.getMessage());
        }

        // setting spawn data of arcangels.
        try
        {
            L2NpcTemplate Angel = NpcTable.getInstance().getTemplate(29021);
            L2Spawn spawnDat;
            _AngelSpawn.clear();
            _AngelSpawn1.clear();
            _AngelSpawn2.clear();

            // 5 in 10 comes.
            for (int i = 0; i < 10; i = i + 2)
            {
                spawnDat = new L2Spawn(Angel);
                spawnDat.setAmount(1);
                spawnDat.setLocx(_Angellocation[i][0]);
                spawnDat.setLocy(_Angellocation[i][1]);
                spawnDat.setLocz(_Angellocation[i][2]);
                spawnDat.setHeading(_Angellocation[i][3]);
                spawnDat.setRespawnDelay(60);
                spawnDat.setLocation(0);
                SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                _AngelSpawn1.add(spawnDat);
            }
            _AngelSpawn.put(0, _AngelSpawn1);

            for (int i = 1; i < 10; i = i + 2)
            {
                spawnDat = new L2Spawn(Angel);
                spawnDat.setAmount(1);
                spawnDat.setLocx(_Angellocation[i][0]);
                spawnDat.setLocy(_Angellocation[i][1]);
                spawnDat.setLocz(_Angellocation[i][2]);
                spawnDat.setHeading(_Angellocation[i][3]);
                spawnDat.setRespawnDelay(60);
                spawnDat.setLocation(0);
                SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                _AngelSpawn2.add(spawnDat);
            }
            _AngelSpawn.put(1, _AngelSpawn1);
        }
        catch (Exception e)
        {
            _log.warning(e.getMessage());
        }
        
        _log.info("BaiumManager : State of Baium is " + _State.getState() + ".");
        if (_State.getState().equals(GrandBossState.StateEnum.NOTSPAWN))
        	_StatueofBaiumSpawn.doSpawn();
        else if (_State.getState().equals(GrandBossState.StateEnum.ALIVE))
        {
        	_State.setState(GrandBossState.StateEnum.NOTSPAWN);
        	_State.update();
        	_StatueofBaiumSpawn.doSpawn();
        }
        else if (_State.getState().equals(GrandBossState.StateEnum.INTERVAL) || _State.getState().equals(GrandBossState.StateEnum.DEAD))
        	setInetrvalEndTask();
        
		Date dt = new Date(_State.getRespawnDate());
        _log.info("BaiumManager : Next spawn date of Baium is " + dt + ".");
        _log.info("BaiumManager : Init BaiumManager.");
    }

    // return Baium state.
    public GrandBossState.StateEnum getState()
    {
    	return _State.getState();
    }

    // return list of intruders.
    public List<L2PcInstance> getPlayersInLair()
	{
		return _PlayersInLair;
	}
    
    // Arcangel advent.
    @SuppressWarnings("unchecked")
	protected synchronized void adventArcAngel()
    {
    	int i = Rnd.get(2);
    	for(L2Spawn spawn : (FastList<L2Spawn>)_AngelSpawn.get(i))
    	{
    		_Angels.add(spawn.doSpawn());
    	}
    	
        // set invulnerable.
        for (L2NpcInstance angel : _Angels)
        {
        	angel.setIsInvul(true); // arcangel is invulnerable.
        }
    }

    // Arcangel ascension.
    public void ascensionArcAngel()
    {
        for (L2NpcInstance Angel : _Angels)
        {
            Angel.getSpawn().stopRespawn();
            Angel.deleteMe();
        }
        _Angels.clear();
    }

    // do spawn Baium.
    public void spawnBaium(L2NpcInstance NpcBaium)
    {
        _npcbaium = NpcBaium;

        // get target from statue,to kill a player of make Baium awake.
        L2PcInstance target = (L2PcInstance)_npcbaium.getTarget();
        
        // do spawn.
        L2Spawn baiumSpawn = _MonsterSpawn.get(29020);
        baiumSpawn.setLocx(_npcbaium.getX());
        baiumSpawn.setLocy(_npcbaium.getY());
        baiumSpawn.setLocz(_npcbaium.getZ());
        baiumSpawn.setHeading(_npcbaium.getHeading());

        // delete statue.
		_npcbaium.deleteMe();

        L2GrandBossInstance baium = (L2GrandBossInstance)baiumSpawn.doSpawn();
        _Monsters.add(baium);

        _State.setRespawnDate(
        		Rnd.get(Config.FWB_FIXINTERVALOFBAIUM,Config.FWB_FIXINTERVALOFBAIUM + Config.FWB_RANDOMINTERVALOFBAIUM)
        		+ Config.FWB_ACTIVITYTIMEOFBAIUM);
        _State.setState(GrandBossState.StateEnum.ALIVE);
		_State.update();
        
		// set last attack time.
		setLastAttackTime();
		
    	// do social.
        updateKnownList(baium);
        baium.setIsImmobilized(true);
        baium.setIsInSocialAction(true);

        Earthquake eq = new Earthquake(baium.getX(), baium.getY(), baium.getZ(), 30, 10);
        baium.broadcastPacket(eq);

        SocialAction sa = new SocialAction(baium.getObjectId(), 2);
        baium.broadcastPacket(sa);

        _SocialTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new Social(baium,3), 15000);

        _RecallPcTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new RecallPc(target), 20000);
        
        _SpeakTask =
        	ThreadPoolManager.getInstance().scheduleGeneral(new Speak(target,baium), 24000);

        _SocialTask2 = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new Social(baium,1), 25000);

        _KillPcTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new KillPc(target,baium), 26000);

        _CallAngelTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new CallArcAngel(),35000);

        _MobiliseTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new SetMobilised(baium),35500);

        // move at random.
        if(Config.FWB_MOVEATRANDOM)
        {
        	L2CharPosition pos = new L2CharPosition(Rnd.get(112826, 116241),Rnd.get(15575, 16375),10078,0);
        	_MoveAtRandomTask = ThreadPoolManager.getInstance().scheduleGeneral(
            		new MoveAtRandom(baium,pos),36000);
        }
        
        // set delete task.
        _ActivityTimeEndTask = 
        	ThreadPoolManager.getInstance().scheduleGeneral(new ActivityTimeEnd(),Config.FWB_ACTIVITYTIMEOFBAIUM);

		_SleepCheckTask = ThreadPoolManager.getInstance().scheduleGeneral(new CheckLastAttack(),60000);

        baium = null;
    }

    // Whether it lairs is confirmed. 
    public boolean isEnableEnterToLair()
    {
    	if(_State.getState().equals(GrandBossState.StateEnum.NOTSPAWN))
    		return true;
    	else
    		return false;
    }

    // update list of intruders.
    public void addPlayerToLair(L2PcInstance pc)
    {
        if (!_PlayersInLair.contains(pc)) _PlayersInLair.add(pc);
    }
    
    // Whether the players was annihilated is confirmed. 
    public synchronized boolean isPlayersAnnihilated()
    {
    	for (L2PcInstance pc : _PlayersInLair)
		{
			// player is must be alive and stay inside of lair.
			if (!pc.isDead()
					&& CustomZoneManager.getInstance().checkIfInZone(_ZoneType, pc))
			{
				return false;
			}
		}
		return true;
    }
    
    // banishes players from lair.
    public void banishesPlayers()
    {
    	for(L2PcInstance pc : _PlayersInLair)
    	{
    		if(pc.getQuestState(_QuestName) != null) pc.getQuestState(_QuestName).exitQuest(true);
    		if(CustomZoneManager.getInstance().checkIfInZone(_ZoneType, pc))
    		{
        		int driftX = Rnd.get(-80,80);
        		int driftY = Rnd.get(-80,80);
        		int loc = Rnd.get(3);
        		pc.teleToLocation(_BanishmentLocation[loc][0] + driftX,_BanishmentLocation[loc][1] + driftY,_BanishmentLocation[loc][2]);
    		}
    	}
    	_PlayersInLair.clear();
    }

    // at end of activity time.
    private class ActivityTimeEnd implements Runnable
    {
    	public ActivityTimeEnd()
    	{
    	}
    	
    	public void run()
    	{
    		if(_State.getState().equals(GrandBossState.StateEnum.DEAD))
    			setInetrvalEndTask();
    		else
    			sleepBaium();
    	}
    }

    // clean Baium's lair.
    public void setUnspawn()
	{
    	// eliminate players.
    	banishesPlayers();

    	// delete monsters.
    	ascensionArcAngel();
    	for(L2NpcInstance mob : _Monsters)
    	{
    		mob.getSpawn().stopRespawn();
    		mob.deleteMe();
    	}
    	_Monsters.clear();
    	
    	// delete teleport cube.
		for (L2NpcInstance cube : _TeleportCube)
		{
			cube.getSpawn().stopRespawn();
			cube.deleteMe();
		}
		_TeleportCube.clear();
		
		// not executed tasks is canceled.
		if(_CubeSpawnTask != null)
		{
			_CubeSpawnTask.cancel(true);
			_CubeSpawnTask = null;
		}
		if(_MonsterSpawnTask != null)
		{
			_MonsterSpawnTask.cancel(true);
			_MonsterSpawnTask = null;
		}
		if(_IntervalEndTask != null)
		{
			_IntervalEndTask.cancel(true);
			_IntervalEndTask = null;
		}
		if(_ActivityTimeEndTask != null)
		{
			_ActivityTimeEndTask.cancel(true);
			_ActivityTimeEndTask = null;
		}
		if(_OnPlayersAnnihilatedTask != null)
		{
			_OnPlayersAnnihilatedTask.cancel(true);
			_OnPlayersAnnihilatedTask = null;
		}
		if(_SocialTask != null)
		{
			_SocialTask.cancel(true);
			_SocialTask = null;
		}
		if(_MobiliseTask != null)
		{
			_MobiliseTask.cancel(true);
			_MobiliseTask = null;
		}
		if(_MoveAtRandomTask != null)
		{
			_MoveAtRandomTask.cancel(true);
			_MoveAtRandomTask = null;
		}
		if(_SocialTask2 != null)
		{
			_SocialTask2.cancel(true);
			_SocialTask2 = null;
		}
		if(_RecallPcTask != null)
		{
			_RecallPcTask.cancel(true);
			_RecallPcTask = null;
		}
		if(_KillPcTask != null)
		{
			_KillPcTask.cancel(true);
			_KillPcTask = null;
		}
		if(_CallAngelTask != null)
		{
			_CallAngelTask.cancel(true);
			_CallAngelTask = null;
		}
		if(_SleepCheckTask != null)
		{
			_SleepCheckTask.cancel(true);
			_SleepCheckTask = null;
		}
		if(_SpeakTask != null)
		{
			_SpeakTask.cancel(true);
			_SpeakTask = null;
		}
	}

    // do spawn teleport cube.
    public void spawnCube()
    {
		for (L2Spawn spawnDat : _TeleportCubeSpawn)
		{
			_TeleportCube.add(spawnDat.doSpawn());
		}
    }

    // When the party is annihilated, they are banished.
    public void checkAnnihilated()
    {
    	if(isPlayersAnnihilated())
    	{
    		_OnPlayersAnnihilatedTask =
				ThreadPoolManager.getInstance().scheduleGeneral(new OnPlayersAnnihilatedTask(),5000);
    	}
    }

	// When the party is annihilated, they are banished.
	private class OnPlayersAnnihilatedTask implements Runnable
	{
		public OnPlayersAnnihilatedTask()
		{
		}
		
		public void run()
		{
		    // banishes players from lair.
			banishesPlayers();
		}
	}
    
    // start interval.
    public void setInetrvalEndTask()
    {
		setUnspawn();

		// init state of Baium's lair.
    	if (!_State.getState().equals(GrandBossState.StateEnum.INTERVAL))
    	{
            _State.setRespawnDate(Rnd.get(Config.FWB_FIXINTERVALOFBAIUM,Config.FWB_FIXINTERVALOFBAIUM + Config.FWB_RANDOMINTERVALOFBAIUM));
    		_State.setState(GrandBossState.StateEnum.INTERVAL);
    		_State.update();
    	}
		
    	_IntervalEndTask = ThreadPoolManager.getInstance().scheduleGeneral(
            	new IntervalEnd(),_State.getInterval());
    }

    // at end of interval.
    private class IntervalEnd implements Runnable
    {
    	public IntervalEnd()
    	{
    	}
    	
    	public void run()
    	{
    		_PlayersInLair.clear();
    		_State.setState(GrandBossState.StateEnum.NOTSPAWN);
    		_State.update();
    		
    		// statue of Baium respawn.
    		_StatueofBaiumSpawn.doSpawn();
    	}
    }
    
    // setting teleport cube spawn task.
    public void setCubeSpawn()
    {
        _State.setState(GrandBossState.StateEnum.DEAD);
		_State.update();

		ascensionArcAngel();

    	_CubeSpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new CubeSpawn(),10000);
    }
    
    // update knownlist.
    protected void updateKnownList(L2NpcInstance boss)
    {
    	boss.getKnownList().getKnownPlayers().clear();
		for (L2PcInstance pc : _PlayersInLair)
		{
			boss.getKnownList().getKnownPlayers().put(pc.getObjectId(), pc);
		}
    }

    // do spawn teleport cube.
    private class CubeSpawn implements Runnable
    {
    	public CubeSpawn()
    	{
    	}
    	
        public void run()
        {
        	spawnCube();
        }
    }
    
    // do social.
    private class Social implements Runnable
    {
        private int _action;
        private L2NpcInstance _npc;

        public Social(L2NpcInstance npc,int actionId)
        {
        	_npc = npc;
            _action = actionId;
        }

        public void run()
        {
        	
        	updateKnownList(_npc);
        	
    		SocialAction sa = new SocialAction(_npc.getObjectId(), _action);
            _npc.broadcastPacket(sa);
        }
    }

    // action is enabled the boss.
    private class SetMobilised implements Runnable
    {
        private L2GrandBossInstance _boss;
        public SetMobilised(L2GrandBossInstance boss)
        {
        	_boss = boss;
        }

        public void run()
        {
        	_boss.setIsImmobilized(false);
        	_boss.setIsInSocialAction(false);
            
            // When it is possible to act, a social action is canceled.
            if (_SocialTask != null)
            {
            	_SocialTask.cancel(true);
                _SocialTask = null;
            }
        }
    }
    
    // Move at random on after Baium appears.
    private class MoveAtRandom implements Runnable
    {
    	private L2NpcInstance _npc;
    	L2CharPosition _pos;
    	
    	public MoveAtRandom(L2NpcInstance npc,L2CharPosition pos)
    	{
    		_npc = npc;
    		_pos = pos;
    	}
    	
    	public void run()
    	{
    		_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO,_pos);
    	}
    }

    // call Arcangels
    private class CallArcAngel implements Runnable
    {
    	public CallArcAngel()
    	{
    	}

    	public void run()
    	{
    		adventArcAngel();
    	}
    }

    // recall pc
    private class RecallPc implements Runnable
    {
    	L2PcInstance _target;
    	public RecallPc(L2PcInstance target)
    	{
    		_target = target;
    	}
    	public void run()
    	{
    		_target.teleToLocation(115831, 17248, 10078);
    	}
    }
    
    // kill pc
    private class KillPc implements Runnable
    {
    	L2PcInstance _target;
    	L2GrandBossInstance _boss;
    	public KillPc(L2PcInstance target,L2GrandBossInstance boss)
    	{
    		_target = target;
    		_boss = boss;
    	}

    	public void run()
    	{
    		if (_target != null)
    			_target.reduceCurrentHp(100000 + Rnd.get(_target.getMaxHp()/2,_target.getMaxHp()),_boss);
    	}
    }
    
    // Baium sleeps if never attacked for 30 minutes. 
    public void sleepBaium()
    {
    	setUnspawn();

    	_PlayersInLair.clear();
		_State.setState(GrandBossState.StateEnum.NOTSPAWN);
		_State.update();
		
		// statue of Baium respawn.
		_StatueofBaiumSpawn.doSpawn();
    }
    
    public void setLastAttackTime()
    {
    	_LastAttackTime = System.currentTimeMillis();
    }
    
    private class CheckLastAttack implements Runnable
    {
    	public CheckLastAttack()
    	{
    	}

    	public void run()
    	{
    		if(_State.getState().equals(GrandBossState.StateEnum.ALIVE))
    		{
        		if(_LastAttackTime + Config.FWB_LIMITUNTILSLEEP < System.currentTimeMillis())
        			sleepBaium();
        		else
        		{
        			if(_SleepCheckTask != null)
        			{
        				_SleepCheckTask.cancel(true);
        				_SleepCheckTask = null;
        			}
        			_SleepCheckTask = ThreadPoolManager.getInstance().scheduleGeneral(new CheckLastAttack(),60000);
        		}
    		}
    	}
    }
    
    private class Speak implements Runnable
    {
    	L2PcInstance _target;
    	L2GrandBossInstance _boss;

    	public Speak(L2PcInstance target,L2GrandBossInstance boss)
    	{
    		_target = target;
    		_boss = boss;
    	}

    	public void run()
    	{
        	CreatureSay cs = new CreatureSay(_boss.getObjectId(),0,_boss.getName(),_target.getName() + _Words);
        	_boss.broadcastPacket(cs);
    	}
    }
}
