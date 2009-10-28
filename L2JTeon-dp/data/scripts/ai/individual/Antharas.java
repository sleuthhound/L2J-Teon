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
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.ExternalConfig;
import ai.group_template.L2AttackableAIScript;

/**
 * Antharas AI
 * @author Emperorc
 */
public class Antharas extends L2AttackableAIScript
{
	private static final int ANTHARAS = 29019;
	private static final int Behemoth = 29069;
	private static final int Bomber = 29070;

	//Antharas Status Tracking :
	private static final byte DORMANT = 0;	 	//Antharas is spawned and no one has entered yet. Entry is unlocked
	private static final byte WAITING = 1;	 	//Antharas is spawend and someone has entered, triggering a 30 minute window for additional people to enter
												//before he unleashes his attack. Entry is unlocked
	private static final byte FIGHTING = 2;		//Antharas is engaged in battle, annihilating his foes. Entry is locked
	private static final byte DEAD = 3;			//Antharas has been killed. Entry is locked

	private static L2BossZone _Zone;

	private static long _LastAction = 0;
	private static int _SkillCycle = 0;

	List<L2Attackable> Minions = new FastList<L2Attackable>();

	// Boss: Antharas
	public Antharas(int id,String name,String descr)
	{
		super(id,name,descr);
		int[] mob = {ANTHARAS, Behemoth, Bomber};
		this.registerMobs(mob);
		_Zone = GrandBossManager.getInstance().getZone(181323,114850,-7618);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ANTHARAS);
		int status = GrandBossManager.getInstance().getBossStatus(ANTHARAS);
		if (status == DEAD)
		{
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				this.startQuestTimer("antharas_unlock", temp, null, null);
			}
			else
			{
				GrandBossManager.getInstance().setBossStatus(ANTHARAS,DORMANT);
			}
		}
		else
		{
			GrandBossManager.getInstance().setBossStatus(ANTHARAS,DORMANT);
		}
	}

	public String onAdvEvent (String event, L2NpcInstance npc, L2PcInstance player)
	{
		long temp = 0;
		if (event.equalsIgnoreCase("waiting"))
		{
			GrandBossManager.getInstance().setBossStatus(ANTHARAS,WAITING);
			this.startQuestTimer("waiting_boss", ExternalConfig.Antharas_Wait_Time, npc, null);
		}
		else if (event.equalsIgnoreCase("waiting_boss"))
		{
			GrandBossManager.getInstance().setBossStatus(ANTHARAS,FIGHTING);
			L2GrandBossInstance antharas = (L2GrandBossInstance) addSpawn(ANTHARAS,185452,114850,-8221,32768,false,0);
			GrandBossManager.getInstance().addBoss(antharas);
			antharas.setRunning();
			antharas.setIsInvul(true);
			antharas.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(181323,114850,-7618,0));
			this.startQuestTimer("antharas_has_arrived", 200, antharas, null, true);
			antharas.broadcastPacket(new PlaySound(1, "BS02_A", 1, antharas.getObjectId(), 181323, 114850, -7618));
		}
		else if (event.equalsIgnoreCase("skillcycle"))
		{
			_SkillCycle = 0;
		}
		else if (event.equalsIgnoreCase("loc_check"))
		{
			if (GrandBossManager.getInstance().getBossStatus(ANTHARAS) == FIGHTING)
			{
				if (!_Zone.isInsideZone(npc))
					npc.teleToLocation(181323,114850,-7618);
				if (npc.getX() < 173680 || npc.getX() > 181880 || npc.getY() < 110750 || npc.getY() > 118950 || npc.getZ() < -7720)
					npc.teleToLocation(181323,114850,-7618);
			}
		}
		else if (event.equalsIgnoreCase("camera_1"))
		{
			npc.broadcastPacket(new SocialAction(npc.getObjectId(),1));
			this.startQuestTimer("camera_2", 3000, npc, null);
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),700,13,-19,0,10000));
		}
		else if (event.equalsIgnoreCase("camera_2"))
		{
			this.startQuestTimer("camera_3", 10000, npc, null);
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),700,13,0,6000,10000));
		}
		else if (event.equalsIgnoreCase("camera_3"))
		{
			this.startQuestTimer("camera_4", 200, npc, null);
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),3800,0,0,0,10000));
		}
		else if (event.equalsIgnoreCase("camera_4"))
		{
			npc.broadcastPacket(new SocialAction(npc.getObjectId(),2));
			this.startQuestTimer("camera_5", 10800, npc, null);
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),1200,0,0,22000,11000));
		}
		else if (event.equalsIgnoreCase("camera_5"))
		{
			this.startQuestTimer("camera_6", 1900, npc, null);
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),1200,0,0,300,2000));
		}
		else if (event.equalsIgnoreCase("camera_6"))
		{
			npc.setIsInvul(false);
			npc.setIsParalyzed(false);
			npc.setIsImmobilized(false);
			_SkillCycle = 0;
			_LastAction = System.currentTimeMillis();
			this.startQuestTimer("loc_check", 15000, npc, null, true);
			this.startQuestTimer("antharas_despawn", 60000, npc, null, true);
			this.startQuestTimer("spawn_Behemoth", 20000, npc, null);
			this.startQuestTimer("spawn_Bomber", 20000, npc, null);
		}
		else if (event.equalsIgnoreCase("antharas_despawn"))
		{
			temp = (System.currentTimeMillis() - _LastAction);
			if (temp > 900000)
			{
				npc.deleteMe();
				GrandBossManager.getInstance().setBossStatus(ANTHARAS,DORMANT);
				_Zone.oustAllPlayers();
				this.cancelQuestTimer("antharas_despawn", npc, null);
				this.startQuestTimer("minions_despawn", 1000, npc, null);
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
		else if (event.equalsIgnoreCase("antharas_has_arrived"))
		{
			int dx = Math.abs(npc.getX() - 181323);
			int dy = Math.abs(npc.getY() - 114850);
			if (dx <= 20 && dy <= 20)
			{
				npc.setIsParalyzed(true);
				npc.setIsImmobilized(true);
				npc.getSpawn().setLocx(181323);
				npc.getSpawn().setLocy(114850);
				npc.getSpawn().setLocz(-7618);
				this.startQuestTimer("camera_1", 500, npc, null);
				this.cancelQuestTimer("antharas_has_arrived", npc, null);
			}
		}
		else if (event.equalsIgnoreCase("spawn_cubes"))
		{
			addSpawn(31859,177615,114941,-7709,0,false,900000);
			int radius = 1500;
			for (int i = 0; i < 19; i++)
			{
				int x = (int) (radius*Math.cos(i*.331)); //.331~2pi/19
				int y = (int) (radius*Math.sin(i*.331));
				addSpawn(31859,177615+x,114941+y,-7709,0,false,900000);
			}
		}
		else if (event.equalsIgnoreCase("spawn_minion"))
		{
			L2NpcInstance mob = addSpawn(npc.getNpcId(),Rnd.get(175000, 179900),Rnd.get(112400, 116000),-7709,0,false,0);
			mob.setIsRaidMinion(true);
			Minions.add((L2Attackable)mob);
		}
		else if (event.equalsIgnoreCase("spawn_Behemoth"))
		{
			L2NpcInstance mob = addSpawn(Behemoth,Rnd.get(175000, 179900),Rnd.get(112400, 116000),-7709,0,false,0);
			mob.setIsRaidMinion(true);
			Minions.add((L2Attackable)mob);
		}
		else if (event.equalsIgnoreCase("spawn_Bomber"))
		{
			L2NpcInstance mob = addSpawn(Bomber,Rnd.get(175000, 179900),Rnd.get(112400, 116000),-7709,0,false,0);
			mob.setIsRaidMinion(true);
			Minions.add((L2Attackable)mob);
		}
		else if (event.equalsIgnoreCase("antharas_unlock"))
		{
			GrandBossManager.getInstance().setBossStatus(ANTHARAS,DORMANT);
		}
		else if (event.equalsIgnoreCase("remove_players"))
		{
			_Zone.oustAllPlayers();
		}
		return super.onAdvEvent(event, npc, player);
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		_LastAction = System.currentTimeMillis();
		if (npc.isInvul() && npc.getNpcId() == ANTHARAS)
		{
			return null;
		}
		else if (npc.getNpcId() == ANTHARAS && _SkillCycle == 0 && GrandBossManager.getInstance().getBossStatus(ANTHARAS) == FIGHTING)
		{
			int x = 40;
			if (npc.getCurrentHp() > npc.getMaxHp() / 4 * 3)
				x = 100;
			if (npc.getCurrentHp() > npc.getMaxHp() / 2)
				x = 80;
			if (npc.getCurrentHp() > npc.getMaxHp() / 4)
				x = 60;
			int y = Rnd.get(x);
			if (y < 2 && npc.isAttackingNow())
			{
				_SkillCycle = 1;
				npc.doCast(SkillTable.getInstance().getInfo(4108,1));
				this.startQuestTimer("skillcycle", Rnd.get(6000,9000), npc, null);
			}
			else if (y < 5 && npc.isAttackingNow())
			{
				_SkillCycle = 1;
				npc.doCast(SkillTable.getInstance().getInfo(Rnd.get(5092,5093),1));
				this.startQuestTimer("skillcycle", Rnd.get(6000,9000), npc, null);
			}
			else if (y < 10 && npc.isAttackingNow())
			{
				_SkillCycle = 1;
				npc.doCast(SkillTable.getInstance().getInfo(Rnd.get(4106,4107),1));
				this.startQuestTimer("skillcycle", Rnd.get(6000,8000), npc, null);
			}
			else if (y < 15 && npc.isAttackingNow())
			{
				_SkillCycle = 1;
				npc.doCast(SkillTable.getInstance().getInfo(Rnd.get(4109,4111),1));
				this.startQuestTimer("skillcycle", Rnd.get(6000,8000), npc, null);
			}
			else if (y < 35 && npc.isAttackingNow())
			{
				_SkillCycle = 1;
				npc.doCast(SkillTable.getInstance().getInfo(Rnd.get(4112,4113),1));
				this.startQuestTimer("skillcycle", Rnd.get(4000,6000), npc, null);
			}
			else 
			{
				_SkillCycle = 1;
				this.startQuestTimer("skillcycle", 1000, npc, null);
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}

	public String onKill (L2NpcInstance npc, L2PcInstance killer, boolean isPet) 
	{ 
		if (npc.getNpcId() == ANTHARAS && GrandBossManager.getInstance().getBossStatus(ANTHARAS) == FIGHTING)
		{
			npc.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
			this.cancelQuestTimers("spawn_minion");
			this.cancelQuestTimer("loc_check", npc, null);
			this.cancelQuestTimer("antharas_despawn", npc, null);
			this.startQuestTimer("spawn_cubes", 10000, npc, null);
			this.startQuestTimer("minions_despawn", 20000, npc, null);
			this.startQuestTimer("remove_players", 900000, npc, null);
			GrandBossManager.getInstance().setBossStatus(ANTHARAS,DEAD);
			long respawnTime = (ExternalConfig.Interval_Of_Antharas_Spawn + Rnd.get(ExternalConfig.Random_Of_Antharas_Spawn));
			this.startQuestTimer("antharas_unlock", respawnTime, npc, null);
			// also save the respawn time so that the info is maintained past reboots
			StatsSet info = GrandBossManager.getInstance().getStatsSet(ANTHARAS);
			info.set("respawn_time",System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatsSet(ANTHARAS,info);
		}
		else if (Minions != null && Minions.contains(npc) && GrandBossManager.getInstance().getBossStatus(ANTHARAS) == FIGHTING)
		{
			Minions.remove(npc);
			this.startQuestTimer("spawn_minion", 60000, npc, null);
		}
		return super.onKill(npc,killer,isPet);
	}

	public static void main(String[] args)
	{
		// now call the constructor (starts up the ai)
		new Antharas(-1,"antharas","ai");
	}
}