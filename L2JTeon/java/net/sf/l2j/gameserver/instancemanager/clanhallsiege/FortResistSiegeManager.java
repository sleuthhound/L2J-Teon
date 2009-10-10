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
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Spawn;
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
	private Map<Integer, DamageInfo> _clansDamageInfo = new HashMap<Integer, DamageInfo>();

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
		setNewSiegeDate(siegeDate,21,22);
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
			_clansDamageInfo.clear();
			_siegeEndDate = Calendar.getInstance();
			_siegeEndDate.add(Calendar.MINUTE, 30);
			_endSiegeTask.schedule(1000);
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
        	_log.info("Spawning Nurka.");
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
					if (damageInfo._damage>tempMaxDamage)
					{
						tempMaxDamage=damageInfo._damage;
						clanIdMaxDamage=damageInfo._clan;
					}
				}
			}
			if (clanIdMaxDamage != null)
			{
				ClanHall clanhall = null;
				clanhall = ClanHallManager.getInstance().getClanHallById(21);
				ClanHallManager.getInstance().setOwner(clanhall.getId(), clanIdMaxDamage);
			}
		}
		setNewSiegeDate(getSiegeDate().getTimeInMillis(),21,22);
		_startSiegeTask.schedule(1000);
	}

	public void addSiegeDamage(L2Clan clan,long damage)
	{
		DamageInfo clanDamage =_clansDamageInfo.get(clan.getClanId());
		if (clanDamage != null)
			clanDamage._damage += damage;
		else
		{
			clanDamage = new DamageInfo();
			clanDamage._clan=clan;
			clanDamage._damage += damage;

			_clansDamageInfo.put(clan.getClanId(), clanDamage);
		}
	}
}