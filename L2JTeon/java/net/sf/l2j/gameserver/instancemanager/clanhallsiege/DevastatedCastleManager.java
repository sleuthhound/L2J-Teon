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
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.zone.type.L2FortResistZone;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.ExclusiveTask;

/*
 * Author: Maxi
 * Beta
 */

public class DevastatedCastleManager
{
	private static DevastatedCastleManager	_instance;
	protected FastMap _siegeGuards;
    	private List<L2DoorInstance> _doors = new FastList<L2DoorInstance>();
    	private List<String> _doorDefault = new FastList<String>();
    	private int _clanHallId = 34;
	private boolean _isInProgress	= false;
	private Calendar _siegeEndDate;
	private Calendar _siegeDate;
	private Map<Integer, DamageInfo> _clansDamageInfo = new HashMap<Integer, DamageInfo>();
	private static long GUSTAV_RESPAWN_TIME = 1209600000;
	protected static Log _log = LogFactory.getLog(DevastatedCastleManager.class.getName());


	private class DamageInfo
	{
		public L2Clan _clan;
		public long _damage;
	}

	protected class RunSiege implements Runnable
    {
        public void run()
        {
            Siege();
        }

        final DevastatedCastleManager this1;

        protected RunSiege()
        {
            this1 = DevastatedCastleManager.this;
        }
    }

	public static final DevastatedCastleManager getInstance()
	{
		if (_instance == null)
			_instance = new DevastatedCastleManager();
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
			statement.setInt(1, 0);
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
				statement.setInt(2, 0);
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

	private DevastatedCastleManager()
	{
	_isInProgress = false;
	_clansDamageInfo = new HashMap();
	_log.info("Devastated Castle Siege");
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
		_isInProgress = true;
		_clansDamageInfo.clear();
		_siegeEndDate = Calendar.getInstance();
		_siegeEndDate.add(Calendar.MINUTE, 60);
		_endSiegeTask.schedule(1000);
        RunSiege rs = new RunSiege();
        ClanHall CH = ClanHallManager.getInstance().getClanHallById(34);
        CH.banishForeigners();
        CH.spawnDoor();
        try
        {
            /*fillMonsters();
            spawnMinions();*/
            L2NpcTemplate template;
            L2Spawn spawn;

            template = NpcTable.getInstance().getTemplate(35410);
            spawn = new L2Spawn(template);
            spawn.setLocx(178298);
            spawn.setLocy(-17624);
            spawn.setLocz(-2194);
            spawn.stopRespawn();
            spawn.spawnOne();
        }
        catch (Exception e)
        {
        	_log.warn("Gustav spawn fails: " + e.getMessage(), e);
        }
        finally
        {
        	_log.info("Siege of Devastated castle has begun!");
        	_log.info("Spawning Gustav.");
        	}
	}

	public void addSiegeDamage(L2Clan clan, double damage)
	{
		_isInProgress = true;
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
				clanhall = ClanHallManager.getInstance().getClanHallById(34);
        			clanhall.banishForeigners();
        			clanhall.spawnDoor();
        			clanhall = null;
				ClanHallManager.getInstance().setOwner(clanhall.getId(), clanIdMaxDamage);
			}
		}
		setNewSiegeDate(_siegeDate.getTimeInMillis());
		_startSiegeTask.schedule(1000);
	}
    /**
     * Respawn all doors on clanhall<BR>
     * <BR>
     */
    public void spawnDoor()
    {
	spawnDoor(false);
    }

    /**
     * Respawn all doors on clanhall<BR>
     * <BR>
     */
    public void spawnDoor(boolean isDoorWeak)
    {
	for (int i = 0; i < getDoors().size(); i++)
	{
	    L2DoorInstance door = getDoors().get(i);
	    if (door.getCurrentHp() <= 0)
	    {
		door.decayMe(); // Kill current if not killed already
		door = DoorTable.parseList(_doorDefault.get(i));
		if (isDoorWeak)
		    door.setCurrentHp(door.getMaxHp() / 2);
		door.spawnMe(door.getX(), door.getY(), door.getZ());
		getDoors().set(i, door);
	    } else if (door.getOpen() == 0)
		door.closeMe();
	}
    }


    public final L2DoorInstance getDoor(int doorId)
    {
	if (doorId <= 0)
	    return null;
	for (int i = 0; i < getDoors().size(); i++)
	{
	    L2DoorInstance door = getDoors().get(i);
	    if (door.getDoorId() == doorId)
		return door;
	}
	return null;
    }

    public final List<L2DoorInstance> getDoors()
    {
	return _doors;
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

    public void Siege()
    {
		_isInProgress = true;
        ClanHall CH = ClanHallManager.getInstance().getClanHallById(34);
        CH.banishForeigners();
        CH.spawnDoor();
    }
}