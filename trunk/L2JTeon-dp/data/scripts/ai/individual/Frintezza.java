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
 
package ai.individual;

import javolution.util.FastList;
import java.util.List;
import ai.group_template.L2AttackableAIScript;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillCanceld;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.ExternalConfig;
 
/**
 * Frintezza AI
 * @author Darki699
 * @author SANDMAN L2J_JP(modified)
 * @author JOJO
 * Update by rocknow
 */
public class Frintezza extends L2AttackableAIScript
{
	private static final int[][] _invadeLoc =
		{
		{ 173235, -76884, -5107 },
		{ 175003, -76933, -5107 },
		{ 174196, -76190, -5107 },
		{ 174013, -76120, -5107 },
		{ 173263, -75161, -5107 }
		};

	private static final int[][] _skill =
		{
		{ 5015, 1, 5000 },
		{ 5015, 4, 5000 },
		{ 5015, 2, 5000 },
		{ 5015, 5, 5000 },
		{ 5018, 1, 10000 },
		{ 5016, 1, 5000 },
		{ 5015, 3, 5000 },
		{ 5015, 6, 5000 },
		{ 5018, 2, 10000 },
		{ 5019, 1, 10000 },
		{ 5016, 1, 5000 }
		};

	private static final int[][] _mobLoc =
		{
		{ 18328,172894,-76019,-5107,243 },
		{ 18328,174095,-77279,-5107,16216 },
		{ 18328,174111,-74833,-5107,49043 },
		{ 18328,175344,-76042,-5107,32847 },
		{ 18330,173489,-76227,-5134,63565 },
		{ 18330,173498,-75724,-5107,58498 },
		{ 18330,174365,-76745,-5107,22424 },
		{ 18330,174570,-75584,-5107,31968 },
		{ 18330,174613,-76179,-5107,31471 },
		{ 18332,173620,-75981,-5107,4588 },
		{ 18332,173630,-76340,-5107,62454 },
		{ 18332,173755,-75613,-5107,57892 },
		{ 18332,173823,-76688,-5107,2411 },
		{ 18332,174000,-75411,-5107,54718 },
		{ 18332,174487,-75555,-5107,33861 },
		{ 18332,174517,-76471,-5107,21893 },
		{ 18332,174576,-76122,-5107,31176 },
		{ 18332,174600,-75841,-5134,35927 },
		{ 18329,173481,-76043,-5107,61312 },
		{ 18329,173539,-75678,-5107,59524 },
		{ 18329,173584,-76386,-5107,3041 },
		{ 18329,173773,-75420,-5107,51115 },
		{ 18329,173777,-76650,-5107,12588 },
		{ 18329,174585,-76510,-5107,21704 },
		{ 18329,174623,-75571,-5107,40141 },
		{ 18329,174744,-76240,-5107,29202 },
		{ 18329,174769,-75895,-5107,29572 },
		{ 18333,173861,-76011,-5107,383 },
		{ 18333,173872,-76461,-5107,8041 },
		{ 18333,173898,-75668,-5107,51856 },
		{ 18333,174422,-75689,-5107,42878 },
		{ 18333,174460,-76355,-5107,27311 },
		{ 18333,174483,-76041,-5107,30947 },
		{ 18331,173515,-76184,-5107,6971 },
		{ 18331,173516,-75790,-5134,3142 },
		{ 18331,173696,-76675,-5107,6757 },
		{ 18331,173766,-75502,-5134,60827 },
		{ 18331,174473,-75321,-5107,37147 },
		{ 18331,174493,-76505,-5107,34503 },
		{ 18331,174568,-75654,-5134,41661 },
		{ 18331,174584,-76263,-5107,31729 },
		{ 18339,173892,-81592,-5123,50849 },
		{ 18339,173958,-81820,-5123,7459 },
		{ 18339,174128,-81805,-5150,21495 },
		{ 18339,174245,-81566,-5123,41760 },
		{ 18334,173264,-81529,-5072,1646 },
		{ 18334,173265,-81656,-5072,441 },
		{ 18334,173267,-81889,-5072,0 },
		{ 18334,173271,-82015,-5072,65382 },
		{ 18334,174867,-81655,-5073,32537 },
		{ 18334,174868,-81890,-5073,32768 },
		{ 18334,174869,-81485,-5073,32315 },
		{ 18334,174871,-82017,-5073,33007 },
		{ 18335,173074,-80817,-5107,8353 },
		{ 18335,173128,-82702,-5107,5345 },
		{ 18335,173181,-82544,-5107,65135 },
		{ 18335,173191,-80981,-5107,6947 },
		{ 18335,174859,-80889,-5134,24103 },
		{ 18335,174924,-82666,-5107,38710 },
		{ 18335,174947,-80733,-5107,22449 },
		{ 18335,175096,-82724,-5107,42205 },
		{ 18336,173435,-80512,-5107,65215 },
		{ 18336,173440,-82948,-5107,417 },
		{ 18336,173443,-83120,-5107,1094 },
		{ 18336,173463,-83064,-5107,286 },
		{ 18336,173465,-80453,-5107,174 },
		{ 18336,173465,-83006,-5107,2604 },
		{ 18336,173468,-82889,-5107,316 },
		{ 18336,173469,-80570,-5107,65353 },
		{ 18336,173469,-80628,-5107,166 },
		{ 18336,173492,-83121,-5107,394 },
		{ 18336,173493,-80683,-5107,0 },
		{ 18336,173497,-80510,-5134,417 },
		{ 18336,173499,-82947,-5107,0 },
		{ 18336,173521,-83063,-5107,316 },
		{ 18336,173523,-82889,-5107,128 },
		{ 18336,173524,-80627,-5134,65027 },
		{ 18336,173524,-83007,-5107,0 },
		{ 18336,173526,-80452,-5107,64735 },
		{ 18336,173527,-80569,-5134,65062 },
		{ 18336,174602,-83122,-5107,33104 },
		{ 18336,174604,-82949,-5107,33184 },
		{ 18336,174609,-80514,-5107,33234 },
		{ 18336,174609,-80684,-5107,32851 },
		{ 18336,174629,-80627,-5107,33346 },
		{ 18336,174632,-80570,-5107,32896 },
		{ 18336,174632,-83066,-5107,32768 },
		{ 18336,174635,-82893,-5107,33594 },
		{ 18336,174636,-80456,-5107,32065 },
		{ 18336,174639,-83008,-5107,33057 },
		{ 18336,174660,-80512,-5107,33057 },
		{ 18336,174661,-83121,-5107,32768 },
		{ 18336,174663,-82948,-5107,32768 },
		{ 18336,174664,-80685,-5107,32676 },
		{ 18336,174687,-83008,-5107,32520 },
		{ 18336,174691,-83066,-5107,32961 },
		{ 18336,174692,-80455,-5107,33202 },
		{ 18336,174692,-80571,-5107,32768 },
		{ 18336,174693,-80630,-5107,32994 },
		{ 18336,174693,-82889,-5107,32622 },
		{ 18337,172837,-82382,-5107,58363 },
		{ 18337,172867,-81123,-5107,64055 },
		{ 18337,172883,-82495,-5107,64764 },
		{ 18337,172916,-81033,-5107,7099 },
		{ 18337,172940,-82325,-5107,58998 },
		{ 18337,172946,-82435,-5107,58038 },
		{ 18337,172971,-81198,-5107,14768 },
		{ 18337,172992,-81091,-5107,9438 },
		{ 18337,173032,-82365,-5107,59041 },
		{ 18337,173064,-81125,-5107,5827 },
		{ 18337,175014,-81173,-5107,26398 },
		{ 18337,175061,-82374,-5107,43290 },
		{ 18337,175096,-81080,-5107,24719 },
		{ 18337,175169,-82453,-5107,37672 },
		{ 18337,175172,-80972,-5107,32315 },
		{ 18337,175174,-82328,-5107,41760 },
		{ 18337,175197,-81157,-5107,27617 },
		{ 18337,175245,-82547,-5107,40275 },
		{ 18337,175249,-81075,-5107,28435 },
		{ 18337,175292,-82432,-5107,42225 },
		{ 18338,173014,-82628,-5107,11874 },
		{ 18338,173033,-80920,-5107,10425 },
		{ 18338,173095,-82520,-5107,49152 },
		{ 18338,173115,-80986,-5107,9611 },
		{ 18338,173144,-80894,-5107,5345 },
		{ 18338,173147,-82602,-5107,51316 },
		{ 18338,174912,-80825,-5107,24270 },
		{ 18338,174935,-80899,-5107,18061 },
		{ 18338,175016,-82697,-5107,39533 },
		{ 18338,175041,-80834,-5107,25420 },
		{ 18338,175071,-82549,-5107,39163 },
		{ 18338,175154,-82619,-5107,36345 }
		};

	private static final int SCARLET1 = 29046;
	private static final int SCARLET2 = 29047;
	private static final int FRINTEZZA = 29045;
	private static final int GUIDE = 32011;

	//Frintezza Status Tracking :
	private static final byte DORMANT = 0;		//Frintezza is spawned and no one has entered yet. Entry is unlocked
	private static final byte WAITING = 1;		//Frintezza is spawend and someone has entered, triggering a 30 minute window for additional people to enter
												//before he unleashes his attack. Entry is unlocked
	private static final byte FIGHTING = 2; 	//Frintezza is engaged in battle, annihilating his foes. Entry is locked
	private static final byte DEAD = 3;			//Frintezza has been killed. Entry is locked

	private static long _LastAction = 0;
	private static int _Angle = 0;
	private static int _Heading = 0;
	private static int _LocCycle = 0;
	private static int _Remaining = 600000;
	private static int _Time = 0;
	private static int _Bomber = 0;
	private static int _CheckDie = 0;
	private static int _OnCheck = 0;
	private static int _OnSong = 0;
	private static int _Abnormal = 0;
	private static int _OnMorph = 0;
	private static int _Scarlet_x = 0;
	private static int _Scarlet_y = 0;
	private static int _Scarlet_z = 0;
	private static int _Scarlet_h = 0;
	private static double _Scarlet_hp = 0;
	private static double _Scarlet_mp = 0;
	private static int _SecondMorph = 0;
	private static int _ThirdMorph = 0;
	private static int _KillHallAlarmDevice = 0;
	private static int _KillDarkChoirPlayer = 0;
	private static int _KillDarkChoirCaptain = 0;

	private static L2BossZone _Zone;
	private L2GrandBossInstance frintezza, weakScarlet, strongScarlet, activeScarlet;
	private L2MonsterInstance demon1, demon2, demon3, demon4, portrait1, portrait2, portrait3, portrait4;
	private L2NpcInstance _frintezzaDummy, _overheadDummy, _portraitDummy1, _portraitDummy3, _scarletDummy;
	private static List<L2PcInstance> _PlayersInside = new FastList<L2PcInstance>();
	private static List<L2PcInstance> _RegistedPlayers = new FastList<L2PcInstance>();
	private static List<L2NpcInstance> _Room1Mobs = new FastList<L2NpcInstance>();
	private static List<L2NpcInstance> _Room2Mobs = new FastList<L2NpcInstance>();
	private static List<L2Attackable> Minions = new FastList<L2Attackable>();

	// Boss: Frintezza
	public Frintezza(int id, String name, String descr)
	{
		super(id,name,descr);
		int[] mob = {SCARLET1, SCARLET2, FRINTEZZA, 18328, 18329, 18330, 18331, 18332, 18333, 18334, 18335, 18336, 18337, 18338, 18339, 29048, 29049, 29050, 29051};
		_Zone = GrandBossManager.getInstance().getZone(174232, -88020, -5116);
		this.registerMobs(mob);
		addStartNpc(GUIDE);
		addTalkId(GUIDE);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(FRINTEZZA);
		int status = GrandBossManager.getInstance().getBossStatus(FRINTEZZA);
		if (status == DEAD)
		{
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				this.startQuestTimer("frintezza_unlock", temp, null, null);
			}
			else
			{
				GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DORMANT);
			}
		}
		else
		{
			GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DORMANT);
		}
	}

	public String onAdvEvent (String event, L2NpcInstance npc, L2PcInstance player)
	{
		long temp = 0;
		if (event.equalsIgnoreCase("waiting"))
		{
			this.startQuestTimer("close", 27000, npc, null);
			this.startQuestTimer("camera_1", 30000, npc, null);
			_Zone.broadcastPacket(new Earthquake(174232, -88020, -5116, 45, 29));
		}
		else if (event.equalsIgnoreCase("room1_spawn"))
		{
			for (int i = 0; i <= 17; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room1Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room1_spawn2"))
		{
			for (int i = 18; i <= 26; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room1Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room1_spawn3"))
		{
			for (int i = 27; i <= 32; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room1Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room1_spawn4"))
		{
			for (int i = 33; i <= 40; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room1Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room2_spawn"))
		{
			for (int i = 41; i <= 44; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room2Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room2_spawn2"))
		{
			for (int i = 45; i <= 131; i++)
			{
				L2NpcInstance mob = addSpawn(_mobLoc[i][0],_mobLoc[i][1],_mobLoc[i][2],_mobLoc[i][3],_mobLoc[i][4],false,0);
				_Room2Mobs.add(mob);
			}
		}
		else if (event.equalsIgnoreCase("room1_del"))
		{
			for (L2NpcInstance mob : _Room1Mobs)
			{
				if (mob != null)
					mob.deleteMe();
			}
			_Room1Mobs.clear();
		}
		else if (event.equalsIgnoreCase("room2_del"))
		{
			for (L2NpcInstance mob : _Room2Mobs)
			{
				if (mob != null)
					mob.deleteMe();
			}
			_Room2Mobs.clear();
		}
		else if (event.equalsIgnoreCase("room3_del"))
		{
			if (demon1 != null)
				demon1.deleteMe();
			if (demon2 != null)
				demon2.deleteMe();
			if (demon3 != null)
				demon3.deleteMe();
			if (demon4 != null)
				demon4.deleteMe();
			if (portrait1 != null)
				portrait1.deleteMe();
			if (portrait2 != null)
				portrait2.deleteMe();
			if (portrait3 != null)
				portrait3.deleteMe();
			if (portrait4 != null)
				portrait4.deleteMe();
			if (frintezza != null)
				frintezza.deleteMe();
			if (weakScarlet != null)
				weakScarlet.deleteMe();
			if (strongScarlet != null)
				strongScarlet.deleteMe();
			demon1 = null;
			demon2 = null;
			demon3 = null;
			demon4 = null;
			portrait1 = null;
			portrait2 = null;
			portrait3 = null;
			portrait4 = null;
			frintezza = null;
			weakScarlet = null;
			strongScarlet = null;
			activeScarlet = null;
		}
		else if (event.equalsIgnoreCase("clean"))
		{
			_LastAction = 0;
			_LocCycle = 0;
			_Remaining = 600000;
			_CheckDie = 0;
			_OnCheck = 0;
			_Abnormal = 0;
			_OnMorph = 0;
			_SecondMorph = 0;
			_ThirdMorph = 0;
			_KillHallAlarmDevice = 0;
			_KillDarkChoirPlayer = 0;
			_KillDarkChoirCaptain = 0;
			_PlayersInside.clear();
			_RegistedPlayers.clear();
		}
		else if (event.equalsIgnoreCase("close"))
		{
			for (int i = 25150051; i <= 25150058; i++)
			{
				DoorTable.getInstance().getDoor(i).closeMe();
			}
			for (int i = 25150061; i <= 25150070; i++)
			{
				DoorTable.getInstance().getDoor(i).closeMe();
			}
			DoorTable.getInstance().getDoor(25150042).closeMe();
			DoorTable.getInstance().getDoor(25150043).closeMe();
			DoorTable.getInstance().getDoor(25150045).closeMe();
			DoorTable.getInstance().getDoor(25150046).closeMe();
		}
		else if (event.equalsIgnoreCase("ann_reg"))
		{
			if (_Remaining >= 60000)
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...1" + _Remaining / 60000 + "...2"));
				npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...3" + _RegistedPlayers.size() + "...4"));
				if (_Remaining > 60000)
				{
					_Remaining = _Remaining - 60000;
					_Time = 60000;
				}
				else
				{
					_Remaining = _Remaining - 30000;
					_Time = 30000;
				}
			}
			else if (_Remaining >= 10000)
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...5" + _Remaining / 1000 + "...6"));
				npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...7" + _RegistedPlayers.size() + "...8"));
				if (_Remaining > 20000)
				{
					_Remaining = _Remaining - 20000;
					_Time = 20000;
				}
				else
				{
					_Time = _Remaining;
					_Remaining = _Remaining - 10000;
				}
			}
			if (_Remaining >= 10000)
				this.startQuestTimer("ann_reg", _Time, npc, null);
			else
				this.startQuestTimer("ann_start", _Time, npc, null);
		}
		else if (event.equalsIgnoreCase("ann_start"))
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...9"));
			npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...10" + _RegistedPlayers.size() + "...11"));
			for (L2PcInstance pc : _RegistedPlayers)
			{
				if (pc.isDead())
				{
					pc.sendMessage("...12");
				}
				else if (!Util.checkIfInRange(700, npc, pc, true))
				{
					pc.sendMessage("...13");
				}
				else if (pc.getInventory().getItemByItemId(8073) == null)
				{
					pc.sendMessage("...14");
				}
				else
				{
					pc.destroyItemByItemId("Quest", 8073, 1, pc, true);
					_PlayersInside.add(pc);
					_Zone.allowPlayerEntry(pc, 300);
					if (_LocCycle >= 5)
						_LocCycle = 0;
					if (_PlayersInside.size() == 1)
					{
						GrandBossManager.getInstance().setBossStatus(FRINTEZZA,WAITING);
						pc.teleToLocation(_invadeLoc[_LocCycle][0] + Rnd.get(50), _invadeLoc[_LocCycle][1] + Rnd.get(50), _invadeLoc[_LocCycle][2]);
						_LocCycle ++;
						this.startQuestTimer("close", 0, npc, null);
						this.startQuestTimer("room1_spawn", 5000, npc, null);
						this.startQuestTimer("room_final", 2100000, npc, null);
						this.startQuestTimer("frintezza_despawn", 60000, npc, null, true);
						_LastAction = System.currentTimeMillis();
					}
					else if (_PlayersInside.size() > 45)
					{
						pc.sendMessage("...15");
					}
					else
					{
						pc.teleToLocation(_invadeLoc[_LocCycle][0] + Rnd.get(50), _invadeLoc[_LocCycle][1] + Rnd.get(50), _invadeLoc[_LocCycle][2]);
						_LocCycle ++;
					}
				}
			}
			_RegistedPlayers.clear();
		}
		else if (event.equalsIgnoreCase("loc_check"))
		{
			if (GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
			{
				if (!_Zone.isInsideZone(npc))
					npc.teleToLocation(174232,-88020,-5116);
				if (npc.getX() < 171932 || npc.getX() > 176532 || npc.getY() < -90320 || npc.getY() > -85720 || npc.getZ() < -5130)
					npc.teleToLocation(174232,-88020,-5116);
			}
		}
		else if (event.equalsIgnoreCase("camera_1"))
		{
			GrandBossManager.getInstance().setBossStatus(FRINTEZZA,FIGHTING);
			
			_frintezzaDummy = (L2NpcInstance) addSpawn(29052,174240,-89805,-5022,16048,false,0);
			_frintezzaDummy.setIsInvul(true);
			_frintezzaDummy.setIsImmobilized(true);
			
			_overheadDummy = (L2NpcInstance) addSpawn(29052,174232,-88020,-5116,16384,false,0);
			_overheadDummy.setIsInvul(true);
			_overheadDummy.setIsImmobilized(true);
			_overheadDummy.setCollisionHeight(600);
			//_Zone.broadcastPacket(new NpcInfo(_overheadDummy, null));
			
			_portraitDummy1 = (L2NpcInstance) addSpawn(29052, 172450, -87890, -5089, 16048,false,0);
			_portraitDummy1.setIsImmobilized(true);
			_portraitDummy1.setIsInvul(true);
			
			_portraitDummy3 = (L2NpcInstance) addSpawn(29052, 176012, -87890, -5089, 16048,false,0);
			_portraitDummy3.setIsImmobilized(true);
			_portraitDummy3.setIsInvul(true);
			
			_scarletDummy = (L2NpcInstance) addSpawn(29053,174232,-88020,-5116,16384,false,0);
			_scarletDummy.setIsInvul(true);
			_scarletDummy.setIsImmobilized(true);
			
			this.startQuestTimer("stop_pc", 0, npc, null);
			this.startQuestTimer("camera_2", 1000, _overheadDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_2"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_overheadDummy.getObjectId(),0, 75, -89, 0, 100));
			this.startQuestTimer("camera_2b", 0, _overheadDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_2b"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_overheadDummy.getObjectId(),0, 75, -89, 0, 100));
			this.startQuestTimer("camera_3", 0, _overheadDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_3"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_overheadDummy.getObjectId(),300, 90, -10, 6500, 7000));
			
			frintezza = (L2GrandBossInstance) addSpawn(FRINTEZZA,174240,-89805,-5022,16048,false,0);
			GrandBossManager.getInstance().addBoss(frintezza);
			frintezza.setIsImmobilized(true);
			frintezza.disableAllSkills();
			_Zone.updateKnownList(frintezza);
			
			demon2 = (L2MonsterInstance) addSpawn(29051, 175876, -88713, -4972, 28205,false,0);
			demon2.setIsImmobilized(true);
			demon2.disableAllSkills();
			_Zone.updateKnownList(demon2);
			
			demon3 = (L2MonsterInstance) addSpawn(29051, 172608, -88702, -4972, 64817,false,0);
			demon3.setIsImmobilized(true);
			demon3.disableAllSkills();
			_Zone.updateKnownList(demon3);
			
			demon1 = (L2MonsterInstance) addSpawn(29050, 175833, -87165, -4972, 35048,false,0);
			demon1.setIsImmobilized(true);
			demon1.disableAllSkills();
			_Zone.updateKnownList(demon1);
			
			demon4 = (L2MonsterInstance) addSpawn(29050, 172634, -87165, -4972, 57730,false,0);
			demon4.setIsImmobilized(true);
			demon4.disableAllSkills();
			_Zone.updateKnownList(demon4);
			
			this.startQuestTimer("camera_4", 6500, _overheadDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_4"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_frintezzaDummy.getObjectId(),1800, 90, 8, 6500, 7000));
			this.startQuestTimer("camera_5", 900, _frintezzaDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_5"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_frintezzaDummy.getObjectId(),140, 90, 10, 2500, 4500));
			this.startQuestTimer("camera_5b", 4000, _frintezzaDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_5b"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),40, 75, -10, 0, 1000));
			this.startQuestTimer("camera_6", 0, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_6"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),40, 75, -10, 0, 12000));
			this.startQuestTimer("camera_7", 1350, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_7"))
		{
			_Zone.broadcastPacket(new SocialAction(frintezza.getObjectId(),2));
			this.startQuestTimer("camera_8", 7000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_8"))
		{
			this.startQuestTimer("camera_9", 1000, frintezza, null);
			_frintezzaDummy.deleteMe();
			_frintezzaDummy = null;
		}
		else if (event.equalsIgnoreCase("camera_9"))
		{
			_Zone.broadcastPacket(new SocialAction(demon2.getObjectId(),1));
			_Zone.broadcastPacket(new SocialAction(demon3.getObjectId(),1));
			this.startQuestTimer("camera_9b", 400, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_9b"))
		{
			_Zone.broadcastPacket(new SocialAction(demon1.getObjectId(),1));
			_Zone.broadcastPacket(new SocialAction(demon4.getObjectId(),1));
			
			for (L2Character pc : _Zone.getCharactersInside().values())
			{
				if (pc instanceof L2PcInstance)
				{
					if (pc.getX() < 174232)
						pc.broadcastPacket(new SpecialCamera(_portraitDummy1.getObjectId(),1000, 118, 0, 0, 1000));
					else
						pc.broadcastPacket(new SpecialCamera(_portraitDummy3.getObjectId(),1000, 62, 0, 0, 1000));
				}
			}
			
			this.startQuestTimer("camera_9c", 0, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_9c"))
		{
			for (L2Character pc : _Zone.getCharactersInside().values())
			{
				if (pc instanceof L2PcInstance)
				{
					if (pc.getX() < 174232)
						pc.broadcastPacket(new SpecialCamera(_portraitDummy1.getObjectId(),1000, 118, 0, 0, 10000));
					else
						pc.broadcastPacket(new SpecialCamera(_portraitDummy3.getObjectId(),1000, 62, 0, 0, 10000));
				}
			}
			
			this.startQuestTimer("camera_10", 2000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_10"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),240, 90, 0, 0, 1000));
			this.startQuestTimer("camera_11", 0, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_11"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),240, 90, 25, 5500, 10000));
			_Zone.broadcastPacket(new SocialAction(frintezza.getObjectId(),3));
			_portraitDummy1.deleteMe();
			_portraitDummy3.deleteMe();
			_portraitDummy1 = null;
			_portraitDummy3 = null;
			this.startQuestTimer("camera_12", 4500, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_12"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),100, 195, 35, 0, 10000));
			this.startQuestTimer("camera_13", 700, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_13"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),100, 195, 35, 0, 10000));
			this.startQuestTimer("camera_14", 1300, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_14"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),120, 180, 45, 1500, 10000));
			_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5006, 1, 34000, 0));
			this.startQuestTimer("camera_16", 1500, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_16"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),520, 135, 45, 8000, 10000));
			this.startQuestTimer("camera_17", 7500, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_17"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),1500, 110, 25, 10000, 13000));
			this.startQuestTimer("camera_18", 9500, frintezza, null);
		}
		else if (event.equalsIgnoreCase("camera_18"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_overheadDummy.getObjectId(),930, 160, -20, 0, 1000));
			this.startQuestTimer("camera_18b", 0, _overheadDummy, null);
		}
		else if (event.equalsIgnoreCase("camera_18b"))
		{
			_Zone.broadcastPacket(new SpecialCamera(_overheadDummy.getObjectId(),930, 160, -20, 0, 10000));
			
			weakScarlet = (L2GrandBossInstance) addSpawn(29046,174232,-88020,-5116,16384,false,0);
			weakScarlet.setIsInvul(true);
			weakScarlet.setIsImmobilized(true);
			weakScarlet.disableAllSkills();
			_Zone.updateKnownList(weakScarlet);
			activeScarlet = weakScarlet;
			
			_Zone.broadcastPacket(new MagicSkillUser(_scarletDummy,_overheadDummy, 5004, 1, 5800, 0));
			
			this.startQuestTimer("camera_19", 5500, _scarletDummy, null);
			this.startQuestTimer("camera_19b", 5400, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("camera_19"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),800, 160, 5, 1000, 10000));
			this.startQuestTimer("camera_20", 2100, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("camera_19b"))
		{
			_Zone.broadcastPacket(new SocialAction(weakScarlet.getObjectId(),3));
		}
		else if (event.equalsIgnoreCase("camera_20"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),300, 60, 8, 0, 10000));
			this.startQuestTimer("camera_21", 2000, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("camera_21"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),500, 90, 10, 3000, 5000));
			this.startQuestTimer("camera_22", 3000, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("camera_22"))
		{
			portrait2 = (L2MonsterInstance) addSpawn(29049, 175876, -88713, -4972, 28205,false,0);
			portrait2.setIsImmobilized(true);
			portrait2.disableAllSkills();
			_Zone.updateKnownList(portrait2);
			
			portrait3 = (L2MonsterInstance) addSpawn(29049, 172608, -88702, -4972, 64817,false,0);
			portrait3.setIsImmobilized(true);
			portrait3.disableAllSkills();
			_Zone.updateKnownList(portrait3);
			
			portrait1 = (L2MonsterInstance) addSpawn(29048, 175833, -87165, -4972, 35048,false,0);
			portrait1.setIsImmobilized(true);
			portrait1.disableAllSkills();
			_Zone.updateKnownList(portrait1);
			
			portrait4 = (L2MonsterInstance) addSpawn(29048, 172634, -87165, -4972, 57730,false,0);
			portrait4.setIsImmobilized(true);
			portrait4.disableAllSkills();
			_Zone.updateKnownList(portrait4);
			
			_overheadDummy.deleteMe();
			_scarletDummy.deleteMe();
			_overheadDummy = null;
			_scarletDummy = null;
			this.startQuestTimer("camera_23", 2000, weakScarlet, null);
			this.startQuestTimer("start_pc", 2000, weakScarlet, null);
			this.startQuestTimer("loc_check", 60000, weakScarlet, null, true);
			this.startQuestTimer("songs_play", 10000 + Rnd.get(10000), frintezza, null);
			this.startQuestTimer("skill01", 10000 + Rnd.get(10000), weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("camera_23"))
		{
			demon1.setIsImmobilized(false);
			demon2.setIsImmobilized(false);
			demon3.setIsImmobilized(false);
			demon4.setIsImmobilized(false);
			demon1.enableAllSkills();
			demon2.enableAllSkills();
			demon3.enableAllSkills();
			demon4.enableAllSkills();
			portrait1.setIsImmobilized(false);
			portrait2.setIsImmobilized(false);
			portrait3.setIsImmobilized(false);
			portrait4.setIsImmobilized(false);
			portrait1.enableAllSkills();
			portrait2.enableAllSkills();
			portrait3.enableAllSkills();
			portrait4.enableAllSkills();
			weakScarlet.setIsInvul(false);
			weakScarlet.setIsImmobilized(false);
			weakScarlet.enableAllSkills();
			weakScarlet.setRunning();
			this.startQuestTimer("spawn_minion", 20000, portrait1, null);
			this.startQuestTimer("spawn_minion", 20000, portrait2, null);
			this.startQuestTimer("spawn_minion", 20000, portrait3, null);
			this.startQuestTimer("spawn_minion", 20000, portrait4, null);
		}
		else if (event.equalsIgnoreCase("stop_pc"))
		{
			for (L2Character cha : _Zone.getCharactersInside().values())
			{
				cha.abortAttack();
				cha.abortCast();
				cha.disableAllSkills();
				cha.setTarget(null);
				cha.stopMove(null);
				cha.setIsImmobilized(true);
				cha.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			}
		}
		else if (event.equalsIgnoreCase("stop_npc"))
		{
			_Heading = npc.getHeading();
			if (_Heading < 32768)
				_Angle = Math.abs(180 - (int)(_Heading / 182.044444444));
			else
				_Angle = Math.abs(540 - (int)(_Heading / 182.044444444));
		}
		else if (event.equalsIgnoreCase("start_pc"))
		{
			for (L2Character cha : _Zone.getCharactersInside().values())
			{
				if (cha != frintezza)
				{
					cha.enableAllSkills();
					cha.setIsImmobilized(false);
				}
			}
		}
		else if (event.equalsIgnoreCase("start_npc"))
		{
			npc.setRunning();
			npc.setIsInvul(false);
		}
		else if (event.equalsIgnoreCase("morph_end"))
		{
			_OnMorph = 0;
		}
		else if (event.equalsIgnoreCase("morph_01"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),250, _Angle, 12, 2000, 15000));
			this.startQuestTimer("morph_02", 3000, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_02"))
		{
			_Zone.broadcastPacket(new SocialAction(weakScarlet.getObjectId(),1));
			weakScarlet.setRHandId(7903);
			this.startQuestTimer("morph_03", 4000, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_03"))
		{
			this.startQuestTimer("morph_04", 1500, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_04"))
		{
			_Zone.broadcastPacket(new SocialAction(weakScarlet.getObjectId(),4));
			L2Skill skill = SkillTable.getInstance().getInfo(5017, 1);
			skill.getEffects(weakScarlet, weakScarlet);
			this.startQuestTimer("morph_end", 6000, weakScarlet, null);
			this.startQuestTimer("start_pc", 3000, weakScarlet, null);
			this.startQuestTimer("start_npc", 3000, weakScarlet, null);
			this.startQuestTimer("songs_play", 10000 + Rnd.get(10000), frintezza, null);
			this.startQuestTimer("skill02", 10000 + Rnd.get(10000), weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_05a"))
		{
			_Zone.broadcastPacket(new SocialAction(frintezza.getObjectId(),4));
		}
		else if (event.equalsIgnoreCase("morph_05"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),250, 120, 15, 0, 1000));
			this.startQuestTimer("morph_06", 0, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_06"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),250, 120, 15, 0, 10000));
			
			this.cancelQuestTimers("loc_check");
			_Scarlet_x = weakScarlet.getX();
			_Scarlet_y = weakScarlet.getY();
			_Scarlet_z = weakScarlet.getZ();
			_Scarlet_h = weakScarlet.getHeading();
			_Scarlet_hp = weakScarlet.getCurrentHp();
			_Scarlet_mp = weakScarlet.getCurrentMp();
			weakScarlet.deleteMe();
			weakScarlet = null;
			activeScarlet = null;
			weakScarlet = (L2GrandBossInstance) addSpawn(29046, _Scarlet_x, _Scarlet_y, _Scarlet_z, _Scarlet_h, false,0);
			weakScarlet.setIsInvul(true);
			weakScarlet.setIsImmobilized(true);
			weakScarlet.disableAllSkills();
			weakScarlet.setRHandId(7903);
			_Zone.updateKnownList(weakScarlet);
			this.startQuestTimer("morph_07", 7000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_07"))
		{
			_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5006, 1, 34000, 0));
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),500, 70, 15, 3000, 10000));
			this.startQuestTimer("morph_08", 3000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_08"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),2500, 90, 12, 6000, 10000));
			this.startQuestTimer("morph_09", 3000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_09"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),250, _Angle, 12, 0, 1000));
			this.startQuestTimer("morph_10", 0, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_10"))
		{
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),250, _Angle, 12, 0, 10000));
			this.startQuestTimer("morph_11", 500, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_11"))
		{
			weakScarlet.doDie(weakScarlet);
			_Zone.broadcastPacket(new SpecialCamera(weakScarlet.getObjectId(),450, _Angle, 14, 8000, 8000));
			this.startQuestTimer("morph_12", 6250, weakScarlet, null);
			this.startQuestTimer("morph_13", 7200, weakScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_12"))
		{
			weakScarlet.deleteMe();
			weakScarlet = null;
		}
		else if (event.equalsIgnoreCase("morph_13"))
		{
			strongScarlet = (L2GrandBossInstance) addSpawn(SCARLET2, _Scarlet_x, _Scarlet_y, _Scarlet_z, _Scarlet_h, false,0);
			strongScarlet.setIsInvul(true);
			strongScarlet.setIsImmobilized(true);
			strongScarlet.disableAllSkills();
			strongScarlet.setCurrentHpMp(_Scarlet_hp, _Scarlet_mp);
			_Zone.updateKnownList(strongScarlet);
			activeScarlet = strongScarlet;
			
			_Zone.broadcastPacket(new SpecialCamera(strongScarlet.getObjectId(),450, _Angle, 12, 500, 14000));
			this.startQuestTimer("morph_14", 3000, strongScarlet, null);
			this.startQuestTimer("loc_check", 60000, strongScarlet, null, true);
		}
		else if (event.equalsIgnoreCase("morph_14"))
		{
			this.startQuestTimer("morph_15", 5100, strongScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_15"))
		{
			_Zone.broadcastPacket(new SocialAction(strongScarlet.getObjectId(),2));
			L2Skill skill = SkillTable.getInstance().getInfo(5017, 1);
			skill.getEffects(strongScarlet, strongScarlet);
			this.startQuestTimer("morph_end", 9000, strongScarlet, null);
			this.startQuestTimer("start_pc", 6000, strongScarlet, null);
			this.startQuestTimer("start_npc", 6000, strongScarlet, null);
			this.startQuestTimer("songs_play", 10000 + Rnd.get(10000), frintezza, null);
			this.startQuestTimer("skill03", 10000 + Rnd.get(10000), strongScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_16"))
		{
			_Zone.broadcastPacket(new SpecialCamera(strongScarlet.getObjectId(),300, _Angle - 180 , 5, 0, 7000));
			this.startQuestTimer("morph_17", 0, strongScarlet, null);
		}
		else if (event.equalsIgnoreCase("morph_17"))
		{
			_Zone.broadcastPacket(new SpecialCamera(strongScarlet.getObjectId(),200, _Angle, 85, 4000, 10000));
			this.startQuestTimer("morph_17b", 7400, frintezza, null);
			this.startQuestTimer("morph_18", 7500, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_17b"))
		{
			frintezza.doDie(frintezza);
		}
		else if (event.equalsIgnoreCase("morph_18"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),100, 120, 5, 0, 7000));
			this.startQuestTimer("morph_19", 0, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_19"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),100, 90, 5, 5000, 15000));
			this.startQuestTimer("morph_20", 7000, frintezza, null);
			this.startQuestTimer("spawn_cubes", 7000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("morph_20"))
		{
			_Zone.broadcastPacket(new SpecialCamera(frintezza.getObjectId(),900, 90, 25, 7000, 10000));
			this.startQuestTimer("start_pc", 7000, frintezza, null);
		}
		else if (event.equalsIgnoreCase("songs_play"))
		{
			if (frintezza != null && !frintezza.isDead() && _OnMorph == 0)
			{
				_OnSong = Rnd.get(1, 5);
				if (_OnSong == 1 && _ThirdMorph == 1 && strongScarlet.getCurrentHp() < strongScarlet.getMaxHp() * 0.6 && Rnd.get(100) < 80)
				{
					_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5007, 1, 32000, 0));
					this.startQuestTimer("songs_effect", 5000, frintezza, null);
					this.startQuestTimer("songs_play", 32000 + Rnd.get(10000), frintezza, null);
				}
				else if (_OnSong == 2 || _OnSong == 3)
				{
					_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5007, _OnSong, 32000, 0));
					this.startQuestTimer("songs_effect", 5000, frintezza, null);
					this.startQuestTimer("songs_play", 32000 + Rnd.get(10000), frintezza, null);
				}
				else if (_OnSong == 4 && _SecondMorph == 1)
				{
					_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5007, 4, 31000, 0));
					this.startQuestTimer("songs_effect", 5000, frintezza, null);
					this.startQuestTimer("songs_play", 31000 + Rnd.get(10000), frintezza, null);
				}
				else if (_OnSong == 5 && _ThirdMorph == 1 && _Abnormal == 0)
				{
					_Abnormal = 1;
					_Zone.broadcastPacket(new MagicSkillUser(frintezza, frintezza, 5007, 5, 35000, 0));
					this.startQuestTimer("songs_effect", 5000, frintezza, null);
					this.startQuestTimer("songs_play", 35000 + Rnd.get(10000), frintezza, null);
				}
				else
				{
					this.startQuestTimer("songs_play", 5000 + Rnd.get(5000), frintezza, null);
				}
			}
		}
		else if (event.equalsIgnoreCase("songs_effect"))
		{
			L2Skill skill = SkillTable.getInstance().getInfo(5008, _OnSong);
			if (_OnSong == 1 || _OnSong == 2 || _OnSong == 3)
			{
				if (frintezza != null && !frintezza.isDead() && activeScarlet != null && !activeScarlet.isDead())
				{
					skill.getEffects(frintezza, activeScarlet);
				}
			}
			else if (_OnSong == 4)
			{
				for (L2Character cha : _Zone.getCharactersInside().values())
				{
					if (cha instanceof L2PcInstance && Rnd.get(100) < 80)
						skill.getEffects(frintezza, cha);
						cha.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(5008, 4));
				}
			}
			else if (_OnSong == 5)
			{
				for (L2Character cha : _Zone.getCharactersInside().values())
				{
					if (cha instanceof L2PcInstance && Rnd.get(100) < 70)
					{
						cha.abortAttack();
						cha.abortCast();
						cha.disableAllSkills();
						cha.stopMove(null);
						cha.setIsParalyzed(true);
						cha.setIsImmobilized(true);
						cha.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
						skill.getEffects(frintezza, cha);
						cha.startAbnormalEffect(L2Character.ABNORMAL_EFFECT_DANCE_STUNNED);
						cha.sendPacket(new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(5008, 5));
					}
				}
				this.startQuestTimer("stop_effect", 25000, frintezza, null);
			}
		}
		else if (event.equalsIgnoreCase("stop_effect"))
		{
			for (L2Character cha : _Zone.getCharactersInside().values())
			{
				if (cha instanceof L2PcInstance)
				{
					cha.stopAbnormalEffect(L2Character.ABNORMAL_EFFECT_DANCE_STUNNED);
					cha.stopAbnormalEffect(L2Character.ABNORMAL_EFFECT_FLOATING_ROOT);
					cha.enableAllSkills();
					cha.setIsImmobilized(false);
					cha.setIsParalyzed(false);
				}
			}
			_Abnormal = 0;
		}
		else if (event.equalsIgnoreCase("attack_stop"))
		{
			this.cancelQuestTimers("skill01");
			this.cancelQuestTimers("skill02");
			this.cancelQuestTimers("skill03");
			this.cancelQuestTimers("songs_play");
			this.cancelQuestTimers("songs_effect");
			_Zone.broadcastPacket(new MagicSkillCanceld(frintezza.getObjectId()));
		}
		else if (event.equalsIgnoreCase("check_hp"))
		{
			if (npc.isDead())
			{
				_OnMorph = 1;
				_Zone.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
				this.startQuestTimer("attack_stop", 0, frintezza, null);
				this.startQuestTimer("stop_pc", 0, npc, null);
				this.startQuestTimer("stop_npc", 0, npc, null);
				this.startQuestTimer("morph_16", 0, npc, null);
			}
			else
			{
				_CheckDie = _CheckDie + 10;
				if (_CheckDie < 3000)
					this.startQuestTimer("check_hp", 10, npc, null);
				else
				{
					_OnCheck = 0;
					_CheckDie = 0;
				}
			}
			
		}
		else if (event.equalsIgnoreCase("skill01"))
		{
			if (weakScarlet != null && !weakScarlet.isDead() && _SecondMorph == 0 && _ThirdMorph == 0 && _OnMorph == 0)
			{
				int i = Rnd.get(0,1);
				weakScarlet.doCast(SkillTable.getInstance().getInfo(_skill[i][0],_skill[i][1]));
				this.startQuestTimer("skill01", _skill[i][2] + 5000 + Rnd.get(10000), npc, null);
			}
		}
		else if (event.equalsIgnoreCase("skill02"))
		{
			if (weakScarlet != null && !weakScarlet.isDead() && _SecondMorph == 1 && _ThirdMorph == 0 && _OnMorph == 0)
			{
				int i = 0;
				if (_Abnormal == 0)
					i = Rnd.get(2,5);
				else
					i = Rnd.get(2,4);
				weakScarlet.doCast(SkillTable.getInstance().getInfo(_skill[i][0],_skill[i][1]));
				this.startQuestTimer("skill02", _skill[i][2] + 5000 + Rnd.get(10000), npc, null);
				if (i == 5)
				{
					_Abnormal = 1;
					this.startQuestTimer("float_effect", 4000, weakScarlet, null);
				}
			}
		}
		else if (event.equalsIgnoreCase("skill03"))
		{
			if (strongScarlet != null && !strongScarlet.isDead() && _SecondMorph == 1 && _ThirdMorph == 1 && _OnMorph == 0)
			{
				int i = 0;
				if (_Abnormal == 0)
					i = Rnd.get(6,10);
				else
					i = Rnd.get(6,9);
				strongScarlet.doCast(SkillTable.getInstance().getInfo(_skill[i][0],_skill[i][1]));
				this.startQuestTimer("skill03", _skill[i][2] + 5000 + Rnd.get(10000), npc, null);
				if (i == 10)
				{
					_Abnormal = 1;
					this.startQuestTimer("float_effect", 3000, npc, null);
				}
			}
		}
		else if (event.equalsIgnoreCase("float_effect"))
		{
			if (npc.isCastingNow())
			{
				this.startQuestTimer("float_effect", 500, npc, null);
			}
			else
			{
				for (L2Character cha : _Zone.getCharactersInside().values())
				{
					if (cha instanceof L2PcInstance)
					{
						if (cha.getFirstEffect(5016) != null)
						{
							cha.abortAttack();
							cha.abortCast();
							cha.disableAllSkills();
							cha.stopMove(null);
							cha.setIsParalyzed(true);
							cha.setIsImmobilized(true);
							cha.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
							cha.startAbnormalEffect(L2Character.ABNORMAL_EFFECT_FLOATING_ROOT);
						}
					}
				}
				this.startQuestTimer("stop_effect", 25000, npc, null);
			}
		}
		else if (event.equalsIgnoreCase("action"))
		{
			_Zone.broadcastPacket(new SocialAction(npc.getObjectId(),1));
		}
		else if (event.equalsIgnoreCase("bomber"))
		{
			_Bomber = 0;
		}
		else if (event.equalsIgnoreCase("room_final"))
		{
			_Zone.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"超過時間限制，挑戰失敗！"));
			_Zone.oustAllPlayers();
			this.cancelQuestTimers("waiting");
			this.cancelQuestTimers("frintezza_despawn");
			this.startQuestTimer("clean", 1000, npc, null);
			this.startQuestTimer("close", 1000, npc, null);
			this.startQuestTimer("room1_del", 1000, npc, null);
			this.startQuestTimer("room2_del", 1000, npc, null);
			GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DORMANT);
		}
		else if (event.equalsIgnoreCase("frintezza_despawn"))
		{
			temp = (System.currentTimeMillis() - _LastAction);
			if (temp > 900000)
			{
				_Zone.oustAllPlayers();
				this.cancelQuestTimers("waiting");
				this.cancelQuestTimers("loc_check");
				this.cancelQuestTimers("room_final");
				this.cancelQuestTimers("spawn_minion");
				this.startQuestTimer("clean", 1000, npc, null);
				this.startQuestTimer("close", 1000, npc, null);
				this.startQuestTimer("attack_stop", 1000, npc, null);
				this.startQuestTimer("room1_del", 1000, npc, null);
				this.startQuestTimer("room2_del", 1000, npc, null);
				this.startQuestTimer("room3_del", 1000, npc, null);
				this.startQuestTimer("minions_despawn", 1000, npc, null);
				GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DORMANT);
				this.cancelQuestTimers("frintezza_despawn");
			}
		}
		else if (event.equalsIgnoreCase("minions_despawn"))
		{
			for (int i = 0; i < Minions.size(); i++)
			{
				L2Attackable mob = Minions.get(i);
				if (mob != null)
					mob.decayMe();
			}
			Minions.clear();
		}
		else if (event.equalsIgnoreCase("spawn_minion"))
		{
			if (npc != null && !npc.isDead() && frintezza != null && !frintezza.isDead())
			{
				L2NpcInstance mob = addSpawn(npc.getNpcId()+2,npc.getX(),npc.getY(),npc.getZ(),npc.getHeading(),false,0);
				mob.setIsRaidMinion(true);
				Minions.add((L2Attackable)mob);
				this.startQuestTimer("action", 200, mob, null);
				this.startQuestTimer("spawn_minion", 18000, npc, null);
			}
		}
		else if (event.equalsIgnoreCase("spawn_cubes"))
		{
			addSpawn(29061,174232,-88020,-5114,16384,false,900000);
		}
		else if (event.equalsIgnoreCase("frintezza_unlock"))
		{
			GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DORMANT);
		}
		else if (event.equalsIgnoreCase("remove_players"))
		{
			_Zone.oustAllPlayers();
		}
	return super.onAdvEvent(event, npc, player);
	}

	public String onTalk (L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = "";
		if (GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == DORMANT)
		{
			if (!player.isInParty())
			{
				if (_RegistedPlayers.contains(player))
				{
					htmltext = "<html><body>...16<br>...17</body></html>";
				}
				else if (player.getLevel() < 74)
				{
					htmltext = "<html><body>...18<br>";
				}
				else if (player.getQuestState("frintezza").getQuestItemsCount(8073) == 0)
				{
					htmltext = "<html><body>...19<br><font color=LEVEL>...20</font>...21</body></html>";
				}
				else
				{
					_RegistedPlayers.add(player);
					if (_RegistedPlayers.size() == 1)
					{
						npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...22"));
						this.startQuestTimer("ann_reg", 1000, npc, null);
					}
					htmltext = "<html><body>...23<br>...24"+player.getName()+"...25";
				}
			}
			else if (!player.getParty().isLeader(player)) 
			{
				htmltext = "<html><body>...26<br><font color=LEVEL>...27</font></body></html>";
			}
			else
			{
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					if (mem.isDead())
					{
						htmltext = "<html><body>...28<br>...29"+mem.getName()+"...30</body></html>";
						return htmltext;
					}
				}
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					if (!Util.checkIfInRange(700, player, mem, true))
					{
						htmltext = "<html><body>...31<br>...32"+mem.getName()+"...33</body></html>";
						return htmltext;
					}
				}
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					if (mem.getLevel() < 74)
					{
						htmltext = "<html><body>...34<br>...35"+mem.getName()+"...36</body></html>";
						mem.sendMessage("...37");
						return htmltext;
					}
				}
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
					if (mem.getInventory().getItemByItemId(8073) == null)
					{
						htmltext = "<html><body>...38<br>...38a"+mem.getName()+"...39</body></html>";
						mem.sendMessage("...40");
						return htmltext;
					}
				}
				for (L2PcInstance mem : player.getParty().getPartyMembers())
				{
						if (_RegistedPlayers.contains(mem))
						{
							player.sendMessage("...41"+mem.getName()+"...41a");
						}
						else
						{
							_RegistedPlayers.add(mem);
							if (_RegistedPlayers.size() == 1)
							{
								npc.broadcastPacket(new NpcSay(npc.getObjectId(),1,npc.getNpcId(),"...42"));
								this.startQuestTimer("ann_reg", 1000, npc, null);
							}
							player.sendMessage("「"+mem.getName()+"...43");
							if (mem != player)
							{
								mem.sendMessage("...44"+mem.getName()+"...45");
							}
						}
						htmltext = "";
				}
			}
		}
		else if (GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == WAITING || GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
			htmltext = "<html><body>...46<br><font color=\"LEVEL\">...47<br1>...48</font></body></html>";
		else
			htmltext = "<html><body>...49<br><font color=\"LEVEL\">...50.....</font></body></html>";
	return htmltext;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		_LastAction = System.currentTimeMillis();
		if (npc.getNpcId() == FRINTEZZA)
		{
			npc.setCurrentHpMp(npc.getMaxHp(), 0);
			return null;
		}
		if (npc.getNpcId() == SCARLET1 && _SecondMorph == 0 && _ThirdMorph == 0 && _OnMorph == 0 && npc.getCurrentHp() < npc.getMaxHp() * 0.75 && GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
		{
			this.startQuestTimer("attack_stop", 0, frintezza, null);
			
			_SecondMorph = 1;
			_OnMorph = 1;
			this.startQuestTimer("stop_pc", 1000, npc, null);
			this.startQuestTimer("stop_npc", 1000, npc, null);
			this.startQuestTimer("morph_01", 1100, npc, null);
		}
		else if (npc.getNpcId() == SCARLET1 && _SecondMorph == 1 && _ThirdMorph == 0 && _OnMorph == 0 && npc.getCurrentHp() < npc.getMaxHp() * 0.5 && GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
		{
			this.startQuestTimer("attack_stop", 0, frintezza, null);
			
			_ThirdMorph = 1;
			_OnMorph = 1;
			this.startQuestTimer("stop_pc", 2000, npc, null);
			this.startQuestTimer("stop_npc", 2000, npc, null);
			this.startQuestTimer("morph_05a", 2000, npc, null);
			this.startQuestTimer("morph_05", 2100, npc, null);
		}
		else if (npc.getNpcId() == SCARLET2 && _SecondMorph == 1 && _ThirdMorph == 1 && _OnCheck == 0 && damage >= npc.getCurrentHp() && GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
		{
			_OnCheck = 1;
			this.startQuestTimer("check_hp", 0, npc, null);
		}
		else if ((npc.getNpcId() == 29050 || npc.getNpcId() == 29051) && _Bomber == 0)
		{
			if (npc.getCurrentHp() < npc.getMaxHp() * 0.1)
			{
				if (Rnd.get(100) < 30)
				{
					_Bomber = 1;
					this.startQuestTimer("bomber", 3000, npc, null);
					npc.doCast(SkillTable.getInstance().getInfo(5011,1));
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}

	public String onKill (L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
		if (npc.getNpcId() == FRINTEZZA)
		{
			return null;
		}
		else if (npc.getNpcId() == SCARLET2 && _OnCheck == 0 && GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
		{
			_OnCheck = 1;
			this.startQuestTimer("stop_pc", 0, npc, null);
			this.startQuestTimer("stop_npc", 0, npc, null);
			this.startQuestTimer("morph_16", 0, npc, null);
		}
		else if (npc.getNpcId() == SCARLET2 && _OnCheck == 1 && GrandBossManager.getInstance().getBossStatus(FRINTEZZA) == FIGHTING)
		{
			this.cancelQuestTimers("loc_check");
			this.cancelQuestTimers("spawn_minion");
			this.cancelQuestTimers("frintezza_despawn");
			this.startQuestTimer("clean", 30000, npc, null);
			this.startQuestTimer("close", 30000, npc, null);
			this.startQuestTimer("room3_del", 60000, npc, null);
			this.startQuestTimer("minions_despawn", 60000, npc, null);
			this.startQuestTimer("remove_players", 900000, npc, null);
			GrandBossManager.getInstance().setBossStatus(FRINTEZZA,DEAD);
			long respawnTime = (ExternalConfig.Interval_Of_Frintezza_Spawn + Rnd.get(ExternalConfig.Random_Of_Frintezza_Spawn));
			this.startQuestTimer("frintezza_unlock", respawnTime, npc, null);
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(FRINTEZZA);
			info.set("respawn_time", System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatsSet(FRINTEZZA,info);
		}
		else if (npc.getNpcId() == 18328)
		{
			_KillHallAlarmDevice ++;
			switch (_KillHallAlarmDevice)
			{
			case 1:
				if (Rnd.get(100) < 10)
				{
					this.startQuestTimer("room1_del", 100, npc, null);
					this.startQuestTimer("room2_spawn", 100, npc, null);
					DoorTable.getInstance().getDoor(25150042).openMe();
					DoorTable.getInstance().getDoor(25150043).openMe();
					DoorTable.getInstance().getDoor(25150045).openMe();
					DoorTable.getInstance().getDoor(25150046).openMe();
					for (int i = 25150051; i <= 25150058; i++)
					{
						DoorTable.getInstance().getDoor(i).openMe();
					}
				}
				else
				{
					this.startQuestTimer("room1_spawn2", 100, npc, null);
				}
				break;
			case 2:
				if (Rnd.get(100) < 20)
				{
					this.startQuestTimer("room1_del", 100, npc, null);
					this.startQuestTimer("room2_spawn", 100, npc, null);
					DoorTable.getInstance().getDoor(25150042).openMe();
					DoorTable.getInstance().getDoor(25150043).openMe();
					DoorTable.getInstance().getDoor(25150045).openMe();
					DoorTable.getInstance().getDoor(25150046).openMe();
					for (int i = 25150051; i <= 25150058; i++)
					{
						DoorTable.getInstance().getDoor(i).openMe();
					}
				}
				else
				{
					this.startQuestTimer("room1_spawn3", 100, npc, null);
				}
				break;
			case 3:
				if (Rnd.get(100) < 30)
				{
					this.startQuestTimer("room1_del", 100, npc, null);
					this.startQuestTimer("room2_spawn", 100, npc, null);
					DoorTable.getInstance().getDoor(25150042).openMe();
					DoorTable.getInstance().getDoor(25150043).openMe();
					DoorTable.getInstance().getDoor(25150045).openMe();
					DoorTable.getInstance().getDoor(25150046).openMe();
					for (int i = 25150051; i <= 25150058; i++)
					{
						DoorTable.getInstance().getDoor(i).openMe();
					}
				}
				else
				{
					this.startQuestTimer("room1_spawn4", 100, npc, null);
				}
				break;
			case 4:
					this.startQuestTimer("room1_del", 100, npc, null);
					this.startQuestTimer("room2_spawn", 100, npc, null);
					DoorTable.getInstance().getDoor(25150042).openMe();
					DoorTable.getInstance().getDoor(25150043).openMe();
					DoorTable.getInstance().getDoor(25150045).openMe();
					DoorTable.getInstance().getDoor(25150046).openMe();
					for (int i = 25150051; i <= 25150058; i++)
					{
						DoorTable.getInstance().getDoor(i).openMe();
					}
				break;
			}
		}
		else if (npc.getNpcId() == 18339)
		{
			_KillDarkChoirPlayer ++;
			if (_KillDarkChoirPlayer == 2)
			{
				DoorTable.getInstance().getDoor(25150042).closeMe();
				DoorTable.getInstance().getDoor(25150043).closeMe();
				DoorTable.getInstance().getDoor(25150045).closeMe();
				DoorTable.getInstance().getDoor(25150046).closeMe();
				int outside = 0;
				for (L2PcInstance room2_pc : _PlayersInside)
				{
					if (_Zone.isInsideZone(room2_pc) && room2_pc.getY() > -86130)
						outside++;
				}
				if (outside == 0)
				{
					this.startQuestTimer("room2_del", 100, npc, null);
					this.startQuestTimer("waiting", 180000, npc, null);
					this.cancelQuestTimers("room_final");
				}
				else
				{
					for (int i = 25150061; i <= 25150070; i++)
					{
						DoorTable.getInstance().getDoor(i).openMe();
					}
					this.startQuestTimer("room2_spawn2", 1000, npc, null);
				}
			}
		}
		else if (npc.getNpcId() == 18334)
		{
			_KillDarkChoirCaptain ++;
			if (_KillDarkChoirCaptain == 8)
			{
				this.startQuestTimer("room2_del", 100, npc, null);
				DoorTable.getInstance().getDoor(25150045).openMe();
				DoorTable.getInstance().getDoor(25150046).openMe();
				this.startQuestTimer("waiting", 180000, npc, null);
				this.cancelQuestTimers("room_final");
			}
		}
	return super.onKill(npc,killer,isPet);
	}

	public static void main(String[] args)
	{
		// now call the constructor (starts up the ai)
		new Frintezza(-1,"frintezza","ai");
	}
}