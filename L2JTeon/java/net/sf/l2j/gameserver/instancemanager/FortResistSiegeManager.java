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

package net.sf.l2j.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.ExclusiveTask;

/*
 *
 * Author: MHard
 * Author: Maxi
 */

public class FortResistSiegeManager
{
	protected static Log		_log	= LogFactory.getLog(SiegeManager.class.getName());
	private static FortResistSiegeManager	_instance;
	protected FastMap _siegeGuards;

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

	private long restoreSiegeDate()
	{
		long res=0;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT siege_data FROM clanhall_siege WHERE id=?");
			statement.setInt(1, 21);
			ResultSet rs = statement.executeQuery();

			if (rs.next())
				res = rs.getLong("siege_data");;

			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.error("Exception: can't get clanhall siege date: " + e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return res;
	}
	private void setNewSiegeDate(long siegeDate)
	{
		Calendar tmpDate=Calendar.getInstance();
		if (siegeDate<=System.currentTimeMillis())
		{
			tmpDate.setTimeInMillis(System.currentTimeMillis());
			tmpDate.add(Calendar.DAY_OF_MONTH, 3);
			tmpDate.set(Calendar.DAY_OF_WEEK, 6);
			tmpDate.set(Calendar.HOUR_OF_DAY, 22);// 22.00
			tmpDate.set(Calendar.MINUTE, 0);
			tmpDate.set(Calendar.SECOND, 0);
			
			_siegeDate=tmpDate;
			Connection con = null;
			try
			{
				con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("UPDATE clanhall_siege SET siege_data=? WHERE id = ?");
				statement.setLong(1, _siegeDate.getTimeInMillis());
				statement.setInt(2, 21);
				statement.execute();
				statement.close();
			}
			catch (Exception e)
			{
				_log.error("Exception: can't save clanhall siege date: " + e.getMessage(), e);
			}
			finally
			{
				try
				{
					if (con != null)
						con.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			tmpDate.setTimeInMillis(System.currentTimeMillis());
			_siegeDate=tmpDate;
		}
	}
	private FortResistSiegeManager()
	{
	_siegeGuards = new FastMap();
	_isInProgress = false;
	_questMobs = new FastList();
	_npcSpawnCount = 0;
	_clansDamageInfo = new HashMap();
	_log.info("SiegeManager of Fortress Of Resistence");
	long siegeDate=restoreSiegeDate();
	setNewSiegeDate(siegeDate);
	// Schedule siege auto start
	_startSiegeTask.schedule(1000);
	}

	public final boolean getIsInProgress()
	{
		return _isInProgress;
	}

	public final Calendar getSiegeDate()
	{
		return _siegeDate;	
	}

	public void startSiege()
	{
		_isInProgress=true;
		_clansDamageInfo.clear();
		for (L2Spawn spawn : _questMobs)
		{
			if (spawn != null)
			{
				spawn.init();
			}
		}
		_siegeEndDate = Calendar.getInstance();
		_siegeEndDate.add(Calendar.MINUTE, 30);
		_endSiegeTask.schedule(1000);
		
	}

	public void endSiege(boolean type)
	{
		_isInProgress = false;
		for (L2Spawn spawn : _questMobs)
		{
			if (spawn == null)
				continue;

			spawn.stopRespawn();
			if (spawn.getLastSpawn() != null)
				spawn.getLastSpawn().doDie(spawn.getLastSpawn());
		}
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
		setNewSiegeDate(_siegeDate.getTimeInMillis());
		_startSiegeTask.schedule(1000);
	}

	public void addSiegeDamage(L2Clan clan,long damage)
	{
		DamageInfo clanDamage=_clansDamageInfo.get(clan.getClanId());
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
	
	private final ExclusiveTask _endSiegeTask = new ExclusiveTask() {
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
	private final ExclusiveTask _startSiegeTask = new ExclusiveTask(){
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

	public void addSiegeMob(int npcTemplate,int locx,int locy,int locz,int resp)
	{	/**
		L2Spawn spawn1;
		L2NpcTemplate template1;
		
		template1 = NpcTable.getInstance().getTemplate(npcTemplate);
		if (template1 != null)
		{
			_npcSpawnCount++;
			spawn1 = new L2Spawn(template1);
			spawn1.setId(_npcSpawnCount);
			spawn1.setAmount(1);
			spawn1.setLocx(locx);
			spawn1.setLocy(locy);
			spawn1.setLocz(locz);
			spawn1.setHeading(0);
			spawn1.setRespawnDelay(resp);
			spawn1.setLocation(0);
			_questMobs.add(spawn1);
		}*/
	}

	private boolean _isInProgress	= false;
	private Calendar _siegeEndDate;
	private Calendar _siegeDate;
	private FastList<L2Spawn> _questMobs = new FastList<L2Spawn>();
	private int _npcSpawnCount =0;
	private Map<Integer, DamageInfo> _clansDamageInfo = new HashMap<Integer, DamageInfo>();
}