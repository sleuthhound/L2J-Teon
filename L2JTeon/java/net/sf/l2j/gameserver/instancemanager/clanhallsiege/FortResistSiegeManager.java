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
package net.sf.l2j.gameserver.instancemanager.clanhallsiege;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javolution.util.FastList;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.ClanHallSiege;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.taskmanager.ExclusiveTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Author: MHard
 * Author: Maxi
 */
public class FortResistSiegeManager extends ClanHallSiege
{
	protected static Log _log = LogFactory.getLog(FortResistSiegeManager.class.getName());
	private static FortResistSiegeManager _instance;
	private Map<Integer, DamageInfo> _clansDamageInfo;
	private L2Clan _clan;
/*
	private int[][][] _guardSpawnLoc = {
	        	{ { 35369, 44505, 108867, -2020, 33380 },
	                { 35369, 44545, 108867, -2020, 32768 },
	                { 35370, 44485, 108867, -2020, 49277 },
	                { 35370, 44525, 108827, -2020, 49277 },
	                { 35370, 44535, 108895, -2020, 49277 },
	                { 35370, 44553, 108839, -2020, 49277 },
	                { 35370, 44553, 108895, -2020, 49277 },
	                { 35370, 44565, 108867, -2020, 49277 },
	                { 35371, 44515, 108850, -2020, 49277 },
	                { 35371, 44515, 108884, -2020, 49277 },
	                { 35371, 44535, 108850, -2020, 49277 },
	                { 35373, 44788, 109492, -1705, 49277 },
	                { 35373, 44788, 109492, -1705, 49277 },
	                { 35373, 45168, 109020, -1705, 49277 },
	                { 35374, 44812, 109492, -1705, 49277 },
	                { 35374, 45236, 108980, -1705, 49277 },
	                { 35370, 44497, 108839, -2020, 49317 } }, };*/

	private class DamageInfo
	{
		public L2Clan _clan;
		public long _damage;
	}

	public static final FortResistSiegeManager getInstance()
	{
		if (_instance == null)
			_instance = new FortResistSiegeManager();
		return _instance;
	}

	private FortResistSiegeManager()
	{
		_log.info("Fortress Of Resistence");
		long siegeDate = restoreSiegeDate(21);
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.setTimeInMillis(siegeDate);
		setSiegeDate(tmpDate);
		setNewSiegeDate(siegeDate, 21, 22);
		_clansDamageInfo = new HashMap<Integer, DamageInfo>();
		// Schedule siege auto start
		_startSiegeTask.schedule(1000);
	}

	private final ExclusiveTask _endSiegeTask = new ExclusiveTask()
	{
		@Override
		protected void onElapsed()
		{
			if (!getIsInProgress())
			{
				cancel();
				return;
			}
			final long timeRemaining = _siegeEndDate.getTimeInMillis() - System.currentTimeMillis();
			if (timeRemaining <= 0)
			{
				endSiege(false);
				cancel();
				return;
			}
			schedule(timeRemaining);
		}
	};
	private final ExclusiveTask _startSiegeTask = new ExclusiveTask()
	{
		@Override
		protected void onElapsed()
		{
			if (getIsInProgress())
			{
				cancel();
				return;
			}
			final long timeRemaining = getSiegeDate().getTimeInMillis() - System.currentTimeMillis();
			if (timeRemaining <= 0)
			{
				startSiege();
				cancel();
				return;
			}
			schedule(timeRemaining);
		}
	};

	public void startSiege()
	{
		if (GameServer._instanceOk)
		{
			setIsInProgress(true);
			if (!_clansDamageInfo.isEmpty())
			_clansDamageInfo.clear();
			_siegeEndDate = Calendar.getInstance();
			_siegeEndDate.add(Calendar.MINUTE, 30);
			_endSiegeTask.schedule(1000);
			//locationGuardSpawns();
			ClanHall clanhall = ClanHallManager.getInstance().getClanHallById(21);
			if (!ClanHallManager.getInstance().isFree(clanhall.getId()))
			{
			ClanTable.getInstance().getClan(clanhall.getOwnerId()).broadcastClanStatus();
			ClanHallManager.getInstance().setFree(clanhall.getId());
			clanhall.banishForeigners();
		}
		try
		{
			L2NpcTemplate template1;
			L2Spawn tempSpawn;
			template1 = NpcTable.getInstance().getTemplate(35368); // bloody lord nurka
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(44525);
			tempSpawn.setLocy(108867);
			tempSpawn.setLocz(-2020);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.spawnOne();
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			tempSpawn.stopRespawn();
		}
		catch (Exception e)
		{
			_log.warn("Nurka spawn fails: " + e.getMessage(), e);
		}
		finally
		{
			_log.info("Start the siege of Fortress of Resistance.");
			_log.info("Spawning Bloody Lord Nurka.");
			}
		}
	}

	public void endSiege(boolean type)
	{
		setIsInProgress(false);
		if (type = true)
		{
			L2Clan clanIdMaxDamage = null;
			long tempMaxDamage = 0;
			for (DamageInfo damageInfo : _clansDamageInfo.values())
			{
				if (damageInfo != null)
				{
					if (damageInfo._damage > tempMaxDamage)
					{
						tempMaxDamage = damageInfo._damage;
						clanIdMaxDamage = damageInfo._clan;
					}
				}
			}
			if (clanIdMaxDamage != null)
			{
				ClanHall clanhall = null;
				clanhall = ClanHallManager.getInstance().getClanHallById(21);
				ClanHallManager.getInstance().setOwner(clanhall.getId(), clanIdMaxDamage);
				_clan.setReputationScore(_clan.getReputationScore() + 600, true);
			}
			_log.info("the siege of Fortress of Resistance to finish");
		}
		setNewSiegeDate(getSiegeDate().getTimeInMillis(), 21, 22);
		_startSiegeTask.schedule(1000);
	}

	public void addSiegeDamage(L2Clan clan, long damage)
	{
		setIsInProgress(true);
		DamageInfo clanDamage = _clansDamageInfo.get(clan.getClanId());
		if (clanDamage != null)
			clanDamage._damage += damage;
		else
		{
			clanDamage = new DamageInfo();
			clanDamage._clan = clan;
			clanDamage._damage += damage;
			_clansDamageInfo.put(clan.getClanId(), clanDamage);
			}
		}
	}
	/*public void initLocationGuardSpawns()
	{
		int locNo = Rnd.get(4);
		final int[] Nurka = { 35368 };

		L2Spawn spawnDat;
		L2NpcTemplate template;

		_guardSpawns.clear();

		for (int i = 0; i <= 3; i++)
		{
			template = NpcTable.getInstance().getTemplate(_guardSpawnLoc[locNo][i][0]);
			if (template != null)
			{
				try
				{
					spawnDat = new L2Spawn(template);
					spawnDat.setAmount(1);
					spawnDat.setLocx(_guardSpawnLoc[locNo][i][1]);
					spawnDat.setLocy(_guardSpawnLoc[locNo][i][2]);
					spawnDat.setLocz(_guardSpawnLoc[locNo][i][3]);
					spawnDat.setHeading(_guardSpawnLoc[locNo][i][4]);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					int NpcId = Nurka[i];
					_guardSpawns.put(NpcId, spawnDat);
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	public void locationGuardSpawns()
	{
		int locNo = Rnd.get(4);
		final int[] Nurka = { 35368 };
		
		L2Spawn spawnDat;
		
		for (int i = 0; i <= 3; i++)
		{
			int NpcId = Nurka[i];
			spawnDat = _guardSpawns.get(NpcId);
			spawnDat.setLocx(_guardSpawnLoc[locNo][i][1]);
			spawnDat.setLocy(_guardSpawnLoc[locNo][i][2]);
			spawnDat.setLocz(_guardSpawnLoc[locNo][i][3]);
			spawnDat.setHeading(_guardSpawnLoc[locNo][i][4]);
			_guardSpawns.put(NpcId, spawnDat);
		}
	}

	public void spawnGuard(int npcId)
	{
		if (!isAttackTime())
			return;
		
		L2Spawn spawnDat = _guardSpawns.get(npcId);
		if (spawnDat != null)
		{
			L2SiegeMonsterInstance mob = (L2SiegeMonsterInstance) spawnDat.doSpawn();
			spawnDat.stopRespawn();
			}
		}*/
