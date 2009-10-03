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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ExclusiveTask;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.ClanHallSiege;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author: MHard
 * @author: Maxi(modified)
 * 
 * Rewritten by Setekh for L2J-Archid rev 990.
 */
public class FortResistSiegeManager
{
	private boolean _isInProgress = false;
	private Calendar _siegeEndDate;
	private Calendar _siegeDate;
	private Map<Integer, DamageInfo> _clansDamageInfo;
	private L2Clan _clan;

	private static long NURKA_RESPAWN_TIME = 1209600000; //TODO Should it be in a config?
	private static FortResistSiegeManager _instance;
	private static Log _log = LogFactory.getLog(FortResistSiegeManager.class.getName());

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

	private void restoreSiegeDate()
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT siege_data FROM clanhall_siege WHERE id=?");
			statement.setInt(1, 21);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next())
			{
				_siegeDate = Calendar.getInstance();
				_siegeDate.setTimeInMillis(rs.getLong("siege_data"));
			}

			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.error("Exception: Can't get partisan hideaway siege date: " + e.getMessage(), e);
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

	private void setNewSiegeDate()
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE clanhall_siege SET siege_data=? WHERE id = ?");
			statement.setLong(1, System.currentTimeMillis() + NURKA_RESPAWN_TIME);
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
			_siegeDate.setTimeInMillis(System.currentTimeMillis() + NURKA_RESPAWN_TIME);
			_log.info("Lord Nurka's next spawn is: "+getTimeLeft());
		}
	}

	private FortResistSiegeManager()
	{
		restoreSiegeDate();
		_clansDamageInfo = new HashMap<Integer, DamageInfo>();
		_startSiegeTask.execute();
		_log.info("Fortress of Resistence: initiated.");
		_log.info("Fortress of Resistence: Nurka next spawn: " + getTimeLeft());
	}

	public final boolean getIsInProgress()
	{
		return _isInProgress;
	}

	public final Calendar getSiegeDate()
	{
		return _siegeDate;	
	}
	
	public final String getTimeLeft()
	{
		return _siegeDate.getTime().toString();
	}
	
	public void startSiege()
	{
		_isInProgress = true;
		
		if(!_clansDamageInfo.isEmpty())
			_clansDamageInfo.clear();

		_siegeEndDate = Calendar.getInstance();
		_siegeEndDate.add(Calendar.MINUTE, 30);
		_endSiegeTask.execute();
		
		ClanHall clanhall = ClanHallManager.getInstance().getClanHallById(21);
		
		if(!ClanHallManager.getInstance().isFree(clanhall.getId()))
		{
			ClanTable.getInstance().getClan(clanhall.getOwnerId()).broadcastClanStatus();
			ClanHallManager.getInstance().setFree(clanhall.getId());
		}

		try
        {
            L2NpcTemplate template1;
            L2Spawn tempSpawn;

            template1 = NpcTable.getInstance().getTemplate(35368); //1º bloody lord nurka
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(44525);
            tempSpawn.setLocy(108867);
            tempSpawn.setLocz(-2020);
            tempSpawn.setHeading(16384);
            tempSpawn.setAmount(1);
            tempSpawn.spawnOne();
            SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
            tempSpawn.stopRespawn();

            template1 = NpcTable.getInstance().getTemplate(35375); //2º bloody lord nurka
            tempSpawn = new L2Spawn(template1);
            tempSpawn.setLocx(45109);
            tempSpawn.setLocy(112124);
            tempSpawn.setLocz(-1900);
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
		_isInProgress = false;
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
		    		_clan.setReputationScore(_clan.getReputationScore() + 1, true);
			}
		}
		setNewSiegeDate();
		_startSiegeTask.execute();
	}

	public void addSiegeDamage(L2Clan clan, double damage)
	{
		DamageInfo clanDamage = _clansDamageInfo.get(clan.getClanId());
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
}