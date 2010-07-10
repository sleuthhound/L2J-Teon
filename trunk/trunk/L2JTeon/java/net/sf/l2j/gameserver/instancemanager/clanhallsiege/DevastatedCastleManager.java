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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2SiegeClan;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2SiegeClan.SiegeClanType;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeBossInstance;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.ClanHallSiege;
import net.sf.l2j.gameserver.model.zone.type.L2ClanHallSiegeZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SiegeInfo;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.taskmanager.ExclusiveTask;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DevastatedCastleManager extends ClanHallSiege
{
	protected static Log					_log				= LogFactory.getLog(DevastatedCastleManager.class.getName());
	public ClanHall 						_clanhall			= ClanHallManager.getInstance().getClanHallById(34);
	private List<L2SiegeClan>				_registeredClans	= new FastList<L2SiegeClan>();	// L2SiegeClan
	private List<L2DoorInstance>			_doors				= new FastList<L2DoorInstance>();
	private List<String>					_doorDefault		= new FastList<String>();
	private L2ClanHallSiegeZone				_zone				= null;
	private L2MonsterInstance				_questMob			= null;
	protected boolean						_isRegistrationOver	= false;
	private static DevastatedCastleManager	_instance;

	private static final int GUSTAV = 35410;
	private static final int MINION1 = 35408;
	private static final int MINION2 = 35409;

	private ScheduledFuture<?> _gustav;
	private ScheduledFuture<?> _dietrich;
	private ScheduledFuture<?> _mikhail;
	private ScheduledFuture<?> _monsterdespawn;

	private L2NpcInstance _minion1 = null;
	private L2NpcInstance _minion2 = null;

	private ArrayList<MonsterLocation> _monsters = new ArrayList<MonsterLocation>();
	private ArrayList<L2Spawn> _spawns = new ArrayList<L2Spawn>();

	public static final DevastatedCastleManager load()
	{
		_log.info("ClanHallSiege: Devastated Castle");
		if (_instance == null)
			_instance = new DevastatedCastleManager();
		return _instance;
	}

	public static final DevastatedCastleManager getInstance()
	{
		if (_instance == null)
			_instance = new DevastatedCastleManager();
		return _instance;
	}

	private DevastatedCastleManager()
	{
		long siegeDate=restoreSiegeDate(34);
		Calendar tmpDate=Calendar.getInstance();
		tmpDate.setTimeInMillis(siegeDate);
		setSiegeDate(tmpDate);
		setNewSiegeDate(siegeDate,34,22);
		loadSiegeClan();
		loadDoor();
		// Schedule siege auto start
		_startSiegeTask.schedule(1000);
		_isRegistrationOver = false;
	}

	public void startSiege()
	{
		if (!getIsInProgress())
		{
			if (getRegisteredClans().size() <= 0)
			{
				SystemMessage sm;
				sm = new SystemMessage(SystemMessageId.S1_SIEGE_WAS_CANCELED_BECAUSE_NO_CLANS_PARTICIPATED);
				sm.addString(_clanhall.getName());
				Announcements.getInstance().announceToAll(sm);
				setNewSiegeDate(getSiegeDate().getTimeInMillis(),34,22);
				_startSiegeTask.schedule(1000);
				_isRegistrationOver = false;
				return;
			}
			setIsInProgress(true);
			_clanhall.setUnderSiege(true);
			_zone.updateSiegeStatus();
			announceToPlayer("The siege of the clan hall: " + _clanhall.getName() + " started.");
			_isRegistrationOver = true;
			updatePlayerSiegeStateFlags(false);
			spawnDoor();
			
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(35410);
			_questMob = new L2SiegeBossInstance(IdFactory.getInstance().getNextId(), template);
			_questMob.getStatus().setCurrentHpMp(_questMob.getMaxHp(), _questMob.getMaxMp());
			_questMob.spawnMe(178282,-17623,-2195);
			_siegeEndDate = Calendar.getInstance();
			_siegeEndDate.add(Calendar.MINUTE, 60);
			_endSiegeTask.schedule(1000);
			CHSiegeGuardsManager.getInstance().spawnGustav();
		}
	}

	public void endSiege(L2Character par)
	{
		if (getIsInProgress())
		{
			setIsInProgress(false);
			if (par!=null)
			{
				if (par instanceof L2PcInstance)
				{
					L2PcInstance killer = ((L2PcInstance)par);
					if ((killer.getClan()!=null)&& (checkIsRegistered(killer.getClan())))
					{
						ClanHallManager.getInstance().setOwner(_clanhall.getId(), killer.getClan());
						announceToPlayer("The Siege Clan Hall: " + _clanhall.getName () + " finished.");
						announceToPlayer("The owner of the clan hall became " + killer.getClan().getName());
					}
					else
					{
						announceToPlayer("The siege of the clan hall: " + _clanhall.getName() + " finished.");
						announceToPlayer("The owner of the clan hall remains the same");
					}
				}
			}
			else
			{
				announceToPlayer("The siege of the clan hall: " + _clanhall.getName() + " finished.");
				announceToPlayer("The owner of the clan hall remains the same");
				_questMob.doDie(_questMob);
			}
			_questMob.deleteMe();
			spawnDoor();
			_clanhall.setUnderSiege(false);
			_zone.updateSiegeStatus();
			updatePlayerSiegeStateFlags(true);
			clearSiegeClan(); // Clear siege clan from db
			if (_clanhall.getOwnerClan() != null)
				saveSiegeClan(_clanhall.getOwnerClan());
			setNewSiegeDate(getSiegeDate().getTimeInMillis(),34,22);
			_startSiegeTask.schedule(1000);
			_isRegistrationOver = false;
			CHSiegeGuardsManager.getInstance().deleteGustav();
		}
	}

	public void updatePlayerSiegeStateFlags(boolean clear)
	{
		L2Clan clan;
		for (L2SiegeClan siegeClan : getRegisteredClans())
		{
			if (siegeClan == null)
				continue;

			clan = ClanTable.getInstance().getClan(siegeClan.getClanId());
			for (L2PcInstance member : clan.getOnlineMembers(""))
			{
				if (clear)
					member.setSiegeState((byte) 0);
				else
					member.setSiegeState((byte) 1);
				member.sendPacket(new UserInfo(member));
				member.revalidateZone(true);
			}
		}
	}

	public void spawnDoor()
	{
		spawnDoor(false);
	}

	public void spawnDoor(boolean isDoorWeak)
	{
		for (int i = 0; i < getDoors().size(); i++)
		{
			L2DoorInstance door = getDoors().get(i);
			if (door.getCurrentHp() <= 0)
			{
				door.decayMe(); // Kill current if not killed already
				door = DoorTable.parseList(_doorDefault.get(i));
				DoorTable.getInstance().putDoor(door); // Readd the new door to the DoorTable By Erb
				if (isDoorWeak)
					door.setCurrentHp(door.getMaxHp() / 2);
				door.spawnMe(door.getX(), door.getY(), door.getZ());
				getDoors().set(i, door);
			}
			else if (door.getOpen() == 0)
				door.closeMe();
		}
	}

	public final List<L2DoorInstance> getDoors()
	{
		return _doors;
	}

	public void registerSiegeZone(L2ClanHallSiegeZone zone)
	{
		_zone = zone;
	}

	private void loadDoor()
	{
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("Select * from castle_door where castleId = ?");
			statement.setInt(1, 34);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				// Create list of the door default for use when respawning dead doors
				_doorDefault.add(rs.getString("name") 
						+ ";" + rs.getInt("id") 
						+ ";" + rs.getInt("x") 
						+ ";" + rs.getInt("y") 
						+ ";" + rs.getInt("z") 
						+ ";" + rs.getInt("range_xmin") 
						+ ";" + rs.getInt("range_ymin") 
						+ ";" + rs.getInt("range_zmin") 
						+ ";" + rs.getInt("range_xmax") 
						+ ";" + rs.getInt("range_ymax") 
						+ ";" + rs.getInt("range_zmax") 
						+ ";" + rs.getInt("hp") 
						+ ";" + rs.getInt("pDef") 
						+ ";" + rs.getInt("mDef"));
				L2DoorInstance door = DoorTable.parseList(_doorDefault.get(_doorDefault.size() - 1));
				door.setCHDoor(true);
				door.spawnMe(door.getX(), door.getY(), door.getZ());
				_doors.add(door);
				DoorTable.getInstance().putDoor(door);
				door.closeMe();
			}
			statement.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception: loadCastleDoor(): " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
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
				endSiege(null);
				cancel();
				return;
			}
			if (3600000 > timeRemaining)
			{
				if (timeRemaining > 120000)
					announceToPlayer(Math.round(timeRemaining / 60000.0) + " min (a) before the end of the siege of the clan hall " + _clanhall.getName() + ".");
				else
					announceToPlayer("The siege of the clan hall " + _clanhall.getName() + " expire " + Math.round(timeRemaining / 1000.0) + " seconds (s)!");
			}
			int divider;
			if (timeRemaining > 3600000)
				divider = 3600000; // 1 hour
			else if (timeRemaining > 600000)
				divider = 600000; // 10 min
			else if (timeRemaining > 60000)
				divider = 60000; // 1 min
			else if (timeRemaining > 10000)
				divider = 10000; // 10 sec
			else
				divider = 1000; // 1 sec
			schedule(timeRemaining-((timeRemaining-500) / divider * divider));
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
			if (!getIsRegistrationOver())
			{
				long regTimeRemaining = (getSiegeDate().getTimeInMillis()-(2*3600000)) - System.currentTimeMillis();
				
				if (regTimeRemaining > 0)
				{
					schedule(regTimeRemaining);
					return;
				}
			}
			final long timeRemaining = getSiegeDate().getTimeInMillis() - System.currentTimeMillis();
			if (timeRemaining <= 0)
			{
				startSiege();
				cancel();
				return;
			}
			if (86400000 > timeRemaining)
			{
				if (!getIsRegistrationOver())
				{
					_isRegistrationOver = true;
					announceToPlayer("The registration period at the siege of the clan hall " + _clanhall.getName() + " over.");
				}
				if (timeRemaining > 7200000)
					announceToPlayer(Math.round(timeRemaining / 3600000.0) + " hours before the siege of the clan hall: " + _clanhall.getName() + ".");
				
				else if (timeRemaining > 120000)
					announceToPlayer(Math.round(timeRemaining / 60000.0) + " minutes before the siege of the clan hall: " + _clanhall.getName() + ".");
				
				else
					announceToPlayer("The siege of the clan hall: " + _clanhall.getName() + " start in " + Math.round(timeRemaining / 1000.0) + " seconds!");
			}
			int divider;
			if (timeRemaining > 86400000)
				divider = 86400000; // 1 day
			else if (timeRemaining > 3600000)
				divider = 3600000; // 1 hour
			else if (timeRemaining > 600000)
				divider = 600000; // 10 min
			else if (timeRemaining > 60000)
				divider = 60000; // 1 min
			else if (timeRemaining > 10000)
				divider = 10000; // 10 sec
			else
				divider = 1000; // 1 sec
			schedule(timeRemaining-((timeRemaining-500) / divider * divider));
		}
	};

	public List<L2SiegeClan> getRegisteredClans()
	{
		return _registeredClans;
	}

	public void registerClan(L2PcInstance player)
	{
		if ((player.getClan() != null) && checkIfCanRegister(player))
			saveSiegeClan(player.getClan()); // Save to database
	}

	public void removeSiegeClan(L2PcInstance player)
	{
		L2Clan clan = player.getClan();
		if (clan == null || clan == _clanhall.getOwnerClan() || !checkIsRegistered(clan))
			return;
		removeSiegeClan(clan.getClanId());
	}

	private boolean checkIfCanRegister(L2PcInstance player)
	{
		L2Clan clan = player.getClan();
		if (clan == null || clan.getLevel() < 4)
		{
			player.sendMessage("Only clans reached the 4-th level and above can take part in the siege...");
			return false;
		}
		else if (getIsRegistrationOver())
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.DEADLINE_FOR_SIEGE_S1_PASSED);
			sm.addString(_clanhall.getName());
			player.sendPacket(sm);
			return false;
		}
		else if (getIsInProgress())
		{
			player.sendPacket(SystemMessageId.NOT_SIEGE_REGISTRATION_TIME2);
			return false;
		}
		else if (clan.getClanId() == _clanhall.getOwnerId())
		{
			player.sendMessage("Clan owns the clan hall will automatically register on the siege.");
			return false;
		}
		else
		{
			if (checkIsRegistered(player.getClan()))
			{
				player.sendPacket(SystemMessageId.ALREADY_REQUESTED_SIEGE_BATTLE);
				return false;
			}
			if (FortressofTheDeadManager.getInstance().checkIsRegistered(player.getClan()))
			{
				player.sendPacket(SystemMessageId.ALREADY_REQUESTED_SIEGE_BATTLE);
				return false;
			}
		}
		if (getRegisteredClans().size() >= 5)
		{
			player.sendPacket(SystemMessageId.ATTACKER_SIDE_FULL);
			return false;
		}		
		return true;
	}

	public final boolean checkIsRegistered(L2Clan clan)
	{
		if (clan == null)
			return false;

		Connection con = null;
		boolean register = false;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM siege_clans WHERE clan_id=? AND castle_id=?");
			statement.setInt(1, clan.getClanId());
			statement.setInt(2, 34);
			ResultSet rs = statement.executeQuery();

			if (rs.next())
				register = true;

			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.error("Exception: checkIsRegistered(): " + e.getMessage(), e);
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
		return register;
	}

	public synchronized void saveSiegeClan(L2Clan clan)
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement;
			statement = con.prepareStatement("INSERT INTO siege_clans (clan_id,castle_id,type,castle_owner) VALUES (?,?,?,0)");
			statement.setInt(1, clan.getClanId());
			statement.setInt(2, _clanhall.getId());
			statement.setInt(3, 1);
			statement.execute();
			statement.close();
			addAttacker(clan.getClanId());
			announceToPlayer(clan.getName() + " registered to attack the clan hall: " + _clanhall.getName());
		}
		catch (Exception e)
		{
			_log.error("Exception: saveSiegeClan(L2Clan clan, int typeId, boolean isUpdateRegistration): " + e.getMessage(), e);
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

	private void loadSiegeClan()
	{
		Connection con = null;
		try
		{
			getRegisteredClans().clear();
			if (_clanhall.getOwnerId() > 0)
				addAttacker(_clanhall.getOwnerId());
			PreparedStatement statement = null;
			ResultSet rs = null;

			con = L2DatabaseFactory.getInstance().getConnection();

			statement = con.prepareStatement("SELECT clan_id,type FROM siege_clans where castle_id=?");
			statement.setInt(1, _clanhall.getId());
			rs = statement.executeQuery();

			int typeId;
			int clanId;
			while (rs.next())
			{
				typeId = rs.getInt("type");
				clanId =rs.getInt("clan_id");
				if (typeId == 1)
					addAttacker(clanId);
			}

			statement.close();
		}
		catch (Exception e)
		{
			_log.error("Exception: loadSiegeClan(): " + e.getMessage(), e);
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

	public void removeSiegeClan(int clanId)
	{
		if (clanId <= 0)
			return;

		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM siege_clans WHERE castle_id=? and clan_id=?");
			statement.setInt(1, _clanhall.getId());
			statement.setInt(2, clanId);
			statement.execute();
			statement.close();

			loadSiegeClan();
		}
		catch (Exception e)
		{
			_log.error(e.getMessage(), e);
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

	public void clearSiegeClan()
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM siege_clans WHERE castle_id=?");
			statement.setInt(1, _clanhall.getId());
			statement.execute();
			statement.close();

			this.getRegisteredClans().clear();
		}
		catch (Exception e)
		{
			_log.error("Exception: clearSiegeClan(): " + e.getMessage(), e);
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

	private void addAttacker(int clanId)
	{
		getRegisteredClans().add(new L2SiegeClan(clanId, SiegeClanType.ATTACKER));
	}

	public void announceToPlayer(String message)
	{
		// Get all players
		for (L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
			player.sendMessage(message);
		}
	}

	public final boolean getIsRegistrationOver()
	{
		return _isRegistrationOver;
	}

	public void listRegisterClan(L2PcInstance player)
	{
		player.sendPacket(new SiegeInfo(null,_clanhall,getSiegeDate()));
	}

	public void spawnGustav()
	{
		L2NpcInstance result = null;
		L2NpcTemplate template = null;
		L2Spawn spawn = null;

		try
		{
			fillMonsters();

			template = NpcTable.getInstance().getTemplate(GUSTAV);
			spawn = new L2Spawn(template);
			spawn.setLocx(178298);
			spawn.setLocy(-17624);
			spawn.setLocz(-2194);
			spawn.stopRespawn();
			result = spawn.spawnOne();
			set_gustav(ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnTimer(result), 3600000)); //60 * 60 * 1000

			template = NpcTable.getInstance().getTemplate(MINION1);
			spawn = new L2Spawn(template);
			spawn.setLocx(178306);
			spawn.setLocy(-17535);
			spawn.setLocz(-2195);
			spawn.stopRespawn();
			_minion1 = spawn.spawnOne();
			set_dietrich(ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnTimer(_minion1), 3600000)); //60 * 60 * 1000

			template = NpcTable.getInstance().getTemplate(MINION2);
			spawn = new L2Spawn(template);
			spawn.setLocx(178304);
			spawn.setLocy(-17712);
			spawn.setLocz(-2194);
			spawn.stopRespawn();
			_minion2 = spawn.spawnOne();
			set_mikhail(ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnTimer(_minion2), 3600000)); //60 * 60 * 1000

			spawnMonsters();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		result = null;
		template = null;
		spawn = null;
    }

	private static class MonsterLocation
	{
		private int _id;
		private int _x;
		private int _y;
		private int _z;
		private int _heading;
	
		private MonsterLocation(int id, int x, int y, int z, int heading)
		{
			_id = id;
			_x = x;
			_y = y;
			_z = z;
			_heading = heading;
		}
	
		private int getId()
		{
			return _id;
		}
	
		private int getX()
		{
			return _x;
		}
	
		private int getY()
		{
			return _y;
		}
	
		private int getZ()
		{
			return _z;
		}
	
		private int getHeading()
		{
			return _heading;
		}
	
	}

	@SuppressWarnings("unused")
	private void addMonster(int id, int x, int y, int z, int heading)
	{
		_monsters.add(new MonsterLocation(id, x, y, z, heading));
	}

	private void fillMonsters()
	{/*
		addMonster(35413, 178288, -14924, -2200, 6320);
		addMonster(35412, 178255, -14884, -2200, 6320);
		addMonster(35413, 178222, -14924, -2200, 6320);
		addMonster(35412, 178222, -14884, -2200, 6320);
		addMonster(35412, 178420, -14904, -2200, 6320);
		addMonster(35412, 178387, -14904, -2200, 6320);
		addMonster(35412, 178288, -14884, -2200, 6320);
		addMonster(35412, 178387, -14884, -2200, 6320);
		addMonster(35413, 178354, -14944, -2200, 6320);
		addMonster(35412, 178321, -14884, -2200, 6320);
		addMonster(35413, 178222, -14944, -2200, 6320);
		addMonster(35412, 178354, -14904, -2200, 6320);
		addMonster(35413, 178255, -14924, -2200, 6320);
		addMonster(35413, 178387, -14924, -2200, 6320);
		addMonster(35413, 178354, -14924, -2200, 6320);
		addMonster(35413, 178420, -14924, -2200, 6320);
		addMonster(35412, 178354, -14884, -2200, 6320);
		addMonster(35412, 178420, -14884, -2200, 6320);
		addMonster(35413, 178454, -14944, -2200, 6320);
		addMonster(35413, 178454, -14924, -2200, 6320);
		addMonster(35413, 178420, -14944, -2200, 6320);
		addMonster(35412, 178222, -14904, -2200, 6320);
		addMonster(35413, 178321, -14944, -2200, 6320);
		addMonster(35413, 178321, -14924, -2200, 6320);
		addMonster(35412, 178288, -14904, -2200, 6320);
		addMonster(35412, 178321, -14904, -2200, 6320);
		addMonster(35413, 178255, -14944, -2200, 6320);
		addMonster(35412, 178255, -14904, -2200, 6320);
		addMonster(35413, 178288, -14944, -2200, 6320);
		addMonster(35412, 178454, -14884, -2200, 6320);
		addMonster(35413, 178387, -14944, -2200, 6320);
		addMonster(35412, 178454, -14904, -2200, 6320);
		addMonster(35413, 179052, -15226, -2221, 6320);
		addMonster(35413, 179260, -15341, -2221, 6320);
		addMonster(35413, 179101, -15253, -2221, 6320);
		addMonster(35413, 179073, -15203, -2221, 6320);
		addMonster(35413, 179144, -15271, -2221, 6320);
		addMonster(35413, 179246, -15285, -2221, 6320);
		addMonster(35413, 179164, -15247, -2221, 6320);
		addMonster(35413, 179226, -15309, -2221, 6320);
		addMonster(35413, 179322, -15349, -2221, 6320);
		addMonster(35413, 179302, -15372, -2221, 6320);
		addMonster(35413, 179189, -15286, -2221, 6320);
		addMonster(35413, 179391, -15439, -2221, 6320);
		addMonster(35413, 179341, -15406, -2221, 6320);
		addMonster(35415, 179503, -15925, -2256, 6320);
		addMonster(35415, 179562, -15984, -2256, 6320);
		addMonster(35415, 179491, -15981, -2256, 6320);
		addMonster(35415, 179370, -16196, -2256, 6320);
		addMonster(35411, 179426, -16009, -2253, 6320);
		addMonster(35415, 179544, -15882, -2256, 6320);
		addMonster(35415, 179599, -15943, -2256, 6320);
		addMonster(35415, 179570, -15901, -2256, 6320);
		addMonster(35415, 179408, -16158, -2256, 6320);
		addMonster(35415, 179279, -16219, -2256, 6320);
		addMonster(35411, 179327, -16101, -2253, 6320);
		addMonster(35411, 179540, -16876, -2246, 6320);
		addMonster(35413, 179009, -15201, -2221, 6320);
		addMonster(35413, 178951, -14699, -2080, 6320);
		addMonster(35413, 178801, -14975, -2080, 6320);
		addMonster(35413, 178865, -14857, -2080, 6320);
		addMonster(35413, 178822, -14936, -2080, 6320);
		addMonster(35413, 178843, -14897, -2080, 6320);
		addMonster(35413, 178929, -14739, -2080, 6320);
		addMonster(35413, 178908, -14778, -2080, 6320);
		addMonster(35413, 178886, -14818, -2080, 6320);
		addMonster(35411, 177719, -15951, -2253, 6320);
		addMonster(35413, 177838, -15664, -2226, 6320);
		addMonster(35411, 177627, -15953, -2250, 6320);
		addMonster(35411, 177387, -15955, -2250, 6320);
		addMonster(35411, 177667, -15921, -2253, 6320);
		addMonster(35413, 177859, -15812, -2226, 6320);
		addMonster(35411, 177661, -16014, -2253, 6320);
		addMonster(35411, 177608, -15985, -2250, 6320);
		addMonster(35415, 177663, -16154, -2250, 6320);
		addMonster(35415, 177530, -16079, -2250, 6320);
		addMonster(35411, 177709, -16043, -2253, 6320);
		addMonster(35411, 177703, -15999, -2250, 6320);
		addMonster(35411, 177810, -16145, -2253, 6320);
		addMonster(35415, 177571, -16105, -2250, 6320);
		addMonster(35415, 177473, -16011, -2250, 6320);
		addMonster(35415, 177612, -16090, -2250, 6320);
		addMonster(35415, 177657, -16113, -2250, 6320);
		addMonster(35415, 177387, -15996, -2250, 6320);
		addMonster(35411, 177564, -15963, -2250, 6320);
		addMonster(35411, 177606, -16035, -2253, 6320);
		addMonster(35411, 177470, -15856, -2250, 6320);
		addMonster(35415, 177428, -15981, -2250, 6320);
		addMonster(35411, 177506, -15887, -2250, 6320);
		addMonster(35411, 177517, -15930, -2250, 6320);
		addMonster(35411, 177308, -15861, -2253, 6320);
		addMonster(35411, 177861, -16164, -2253, 6320);
		addMonster(35413, 177906, -15791, -2226, 6320);
		addMonster(35413, 177765, -15643, -2226, 6320);
		addMonster(35413, 177880, -15744, -2226, 6320);
		addMonster(35413, 177788, -15578, -2226, 6320);
		addMonster(35413, 177811, -15622, -2226, 6320);
		addMonster(35413, 177859, -15704, -2226, 6320);
		addMonster(35413, 177769, -15540, -2226, 6320);
		addMonster(35413, 177813, -15726, -2226, 6320);
		addMonster(35413, 177707, -15427, -2226, 6320);
		addMonster(35413, 177680, -15485, -2226, 6320);
		addMonster(35413, 177722, -15561, -2226, 6320);
		addMonster(35413, 177745, -15498, -2226, 6320);
		addMonster(35413, 177803, -14971, -2210, 6320);
		addMonster(35413, 177727, -15464, -2226, 6320);
		addMonster(35415, 177433, -16026, -2250, 6320);
		addMonster(35413, 177649, -14750, -2210, 6320);
		addMonster(35413, 177619, -14705, -2210, 6320);
		addMonster(35413, 177711, -14838, -2210, 6320);
		addMonster(35413, 177834, -15015, -2210, 6320);
		addMonster(35413, 177741, -14883, -2210, 6320);
		addMonster(35413, 177772, -14927, -2210, 6320);
		addMonster(35413, 177680, -14794, -2210, 6320);
		addMonster(35411, 177400, -15854, -2250, 6320);
		addMonster(35415, 179697, -17781, -2256, 6320);
		addMonster(35411, 179479, -17133, -2256, 6320);
		addMonster(35411, 179485, -17213, -2246, 6320);
		addMonster(35411, 179593, -16876, -2246, 6320);
		addMonster(35411, 179468, -17280, -2256, 6320);
		addMonster(35411, 179433, -16991, -2246, 6320);
		addMonster(35411, 179514, -17281, -2256, 6320);
		addMonster(35411, 179525, -17135, -2256, 6320);
		addMonster(35411, 179444, -16937, -2256, 6320);
		addMonster(35411, 179438, -16875, -2246, 6320);
		addMonster(35415, 179633, -17137, -2256, 6320);
		addMonster(35411, 179537, -17214, -2246, 6320);
		addMonster(35411, 179594, -17453, -2246, 6320);
		addMonster(35415, 179576, -17137, -2256, 6320);
		addMonster(35415, 179508, -17341, -2252, 6320);
		addMonster(35415, 179446, -17391, -2252, 6320);
		addMonster(35415, 179437, -17522, -2252, 6320);
		addMonster(35415, 179536, -17842, -2252, 6320);
		addMonster(35415, 179432, -17719, -2252, 6320);
		addMonster(35415, 179436, -17841, -2252, 6320);
		addMonster(35411, 179542, -17453, -2246, 6320);
		addMonster(35415, 179436, -17776, -2256, 6320);
		addMonster(35415, 179534, -17892, -2252, 6320);
		addMonster(35415, 179482, -17841, -2252, 6320);
		addMonster(35415, 179696, -17844, -2252, 6320);
		addMonster(35415, 179604, -17525, -2252, 6320);
		addMonster(35415, 179707, -17722, -2252, 6320);
		addMonster(35411, 179715, -17454, -2246, 6320);
		addMonster(35411, 179641, -17215, -2246, 6320);
		addMonster(35415, 179665, -17527, -2252, 6320);
		addMonster(35415, 179557, -17524, -2252, 6320);
		addMonster(35415, 179636, -17780, -2256, 6320);
		addMonster(35415, 179694, -17897, -2252, 6320);
		addMonster(35414, 178682, -18200, -2200, 6320);
		addMonster(35413, 178577, -18422, -2250, 6320);
		addMonster(35412, 178745, -18186, -2200, 6320);
		addMonster(35413, 178528, -18499, -2250, 6320);
		addMonster(35414, 178640, -18196, -2200, 6320);
		addMonster(35413, 178766, -18228, -2200, 6320);
		addMonster(35412, 178724, -18184, -2200, 6320);
		addMonster(35412, 178703, -18182, -2200, 6320);
		addMonster(35413, 178575, -18500, -2250, 6320);
		addMonster(35413, 178703, -18222, -2200, 6320);
		addMonster(35413, 178530, -18421, -2250, 6320);
		addMonster(35413, 178523, -18696, -2250, 6320);
		addMonster(35412, 178661, -18178, -2200, 6320);
		addMonster(35414, 178661, -18198, -2200, 6320);
		addMonster(35413, 178788, -18229, -2200, 6320);
		addMonster(35414, 178724, -18204, -2200, 6320);
		addMonster(35413, 178574, -18539, -2250, 6320);
		addMonster(35413, 178578, -18383, -2250, 6320);
		addMonster(35413, 178573, -18577, -2250, 6320);
		addMonster(35413, 178530, -18382, -2250, 6320);
		addMonster(35413, 178528, -18461, -2250, 6320);
		addMonster(35413, 178526, -18538, -2250, 6320);
		addMonster(35413, 178526, -18576, -2250, 6320);
		addMonster(35413, 178570, -18736, -2250, 6320);
		addMonster(35413, 178524, -18618, -2250, 6320);
		addMonster(35413, 178571, -18657, -2250, 6320);
		addMonster(35413, 178523, -18656, -2250, 6320);
		addMonster(35413, 178523, -18735, -2250, 6320);
		addMonster(35413, 178571, -18618, -2250, 6320);
		addMonster(35413, 178571, -18697, -2250, 6320);
		addMonster(35413, 178576, -18461, -2250, 6320);
		addMonster(35413, 178682, -18220, -2200, 6320);
		addMonster(35413, 178661, -18218, -2200, 6320);
		addMonster(35414, 178745, -18206, -2200, 6320);
		addMonster(35412, 178682, -18180, -2200, 6320);
		addMonster(35414, 178703, -18202, -2200, 6320);
		addMonster(35412, 178640, -18176, -2200, 6320);
		addMonster(35414, 178788, -18209, -2200, 6320);
		addMonster(35413, 178640, -18216, -2200, 6320);
		addMonster(35412, 178788, -18189, -2200, 6320);
		addMonster(35413, 178745, -18226, -2200, 6320);
		addMonster(35414, 178766, -18208, -2200, 6320);
		addMonster(35412, 178766, -18188, -2200, 6320);
		addMonster(35413, 178724, -18224, -2200, 6320);
		addMonster(35413, 178430, -16901, -2217, 6320);
		addMonster(35415, 178285, -16832, -2217, 6320);
		addMonster(35413, 178153, -16914, -2217, 6320);
		addMonster(35411, 178398, -16781, -2218, 6320);
		addMonster(35415, 178363, -16768, -2217, 6320);
		addMonster(35413, 178437, -16587, -2217, 6320);
		addMonster(35413, 178431, -16784, -2217, 6320);
		addMonster(35411, 178120, -16714, -2218, 6320);
		addMonster(35415, 178366, -16721, -2217, 6320);
		addMonster(35413, 178433, -16742, -2217, 6320);
		addMonster(35413, 178430, -16862, -2217, 6320);
		addMonster(35411, 178395, -16865, -2218, 6320);
		addMonster(35415, 178288, -16783, -2217, 6320);
		addMonster(35411, 178397, -16824, -2218, 6320);
		addMonster(35415, 178090, -16878, -2217, 6320);
		addMonster(35413, 178154, -16797, -2217, 6320);
		addMonster(35415, 178027, -16773, -2217, 6320);
		addMonster(35413, 178435, -16665, -2217, 6320);
		addMonster(35413, 178433, -16704, -2217, 6320);
		addMonster(35411, 178060, -16868, -2218, 6320);
		addMonster(35413, 178156, -16718, -2217, 6320);
		addMonster(35411, 178324, -16762, -2218, 6320);
		addMonster(35413, 177869, -16832, -2217, 6320);
		addMonster(35411, 178117, -16885, -2218, 6320);
		addMonster(35415, 178366, -16674, -2217, 6320);
		addMonster(35413, 178435, -16627, -2217, 6320);
		addMonster(35415, 178366, -16582, -2217, 6320);
		addMonster(35411, 178403, -16566, -2218, 6320);
		addMonster(35413, 178437, -16548, -2217, 6320);
		addMonster(35415, 178290, -16550, -2217, 6320);
		addMonster(35413, 178160, -16600, -2217, 6320);
		addMonster(35411, 178122, -16759, -2218, 6320);
		addMonster(35411, 178122, -16672, -2218, 6320);
		addMonster(35415, 178095, -16736, -2217, 6320);
		addMonster(35411, 178324, -16804, -2218, 6320);
		addMonster(35415, 178029, -16587, -2217, 6320);
		addMonster(35415, 178290, -16690, -2217, 6320);
		addMonster(35415, 178027, -16820, -2217, 6320);
		addMonster(35413, 178431, -16822, -2217, 6320);
		addMonster(35411, 178326, -16720, -2218, 6320);
		addMonster(35413, 178158, -16679, -2217, 6320);
		addMonster(35413, 178161, -16562, -2217, 6320);
		addMonster(35413, 178158, -16640, -2217, 6320);
		addMonster(35413, 177872, -16714, -2217, 6320);
		addMonster(35411, 178061, -16738, -2218, 6320);
		addMonster(35413, 177871, -16752, -2217, 6320);
		addMonster(35413, 177876, -16597, -2217, 6320);
		addMonster(35413, 177873, -16675, -2217, 6320);
		addMonster(35413, 177869, -16873, -2217, 6320);
		addMonster(35413, 177874, -16637, -2217, 6320);
		addMonster(35411, 178060, -16826, -2218, 6320);
		addMonster(35411, 178063, -16784, -2218, 6320);
		addMonster(35415, 178029, -16727, -2217, 6320);
		addMonster(35413, 177876, -16558, -2217, 6320);
		addMonster(35413, 177870, -16794, -2217, 6320);
		addMonster(35413, 177868, -16911, -2217, 6320);
		addMonster(35413, 178156, -16756, -2217, 6320);
		addMonster(35415, 178092, -16782, -2217, 6320);
		addMonster(35413, 178153, -16876, -2217, 6320);
		addMonster(35413, 178154, -16836, -2217, 6320);
		addMonster(35412, 177217, -17168, -2200, 6320);
		addMonster(35413, 177187, -17128, -2200, 6320);
		addMonster(35413, 177276, -17128, -2200, 6320);
		addMonster(35413, 177335, -17128, -2200, 6320);
		addMonster(35414, 177246, -17148, -2200, 6320);
		addMonster(35412, 177246, -17168, -2200, 6320);
		addMonster(35412, 177129, -17168, -2200, 6320);
		addMonster(35412, 177158, -17168, -2200, 6320);
		addMonster(35413, 177217, -17128, -2200, 6320);
		addMonster(35413, 177158, -17128, -2200, 6320);
		addMonster(35413, 177129, -17128, -2200, 6320);
		addMonster(35414, 177129, -17148, -2200, 6320);
		addMonster(35412, 177276, -17168, -2200, 6320);
		addMonster(35414, 177158, -17148, -2200, 6320);
		addMonster(35413, 177305, -17128, -2200, 6320);
		addMonster(35412, 177187, -17168, -2200, 6320);
		addMonster(35414, 177305, -17148, -2200, 6320);
		addMonster(35413, 177396, -17452, -2207, 6320);
		addMonster(35413, 177397, -17634, -2207, 6320);
		addMonster(35416, 177664, -17599, -2219, 6320);
		addMonster(35416, 177721, -17813, -2219, 6320);
		addMonster(35416, 177501, -17424, -2219, 6320);
		addMonster(35415, 177632, -17788, -2215, 6320);
		addMonster(35413, 177466, -17445, -2207, 6320);
		addMonster(35414, 177217, -17148, -2200, 6320);
		addMonster(35413, 177396, -17545, -2207, 6320);
		addMonster(35414, 177187, -17148, -2200, 6320);
		addMonster(35414, 177335, -17148, -2200, 6320);
		addMonster(35412, 177335, -17168, -2200, 6320);
		addMonster(35412, 177305, -17168, -2200, 6320);
		addMonster(35415, 177439, -17426, -2215, 6320);
		addMonster(35415, 177438, -17472, -2215, 6320);
		addMonster(35416, 177662, -17685, -2219, 6320);
		addMonster(35415, 177527, -17578, -2215, 6320);
		addMonster(35415, 177531, -17399, -2215, 6320);
		addMonster(35415, 177561, -17563, -2215, 6320);
		addMonster(35413, 177604, -17718, -2207, 6320);
		addMonster(35413, 177466, -17537, -2207, 6320);
		addMonster(35416, 177500, -17469, -2219, 6320);
		addMonster(35415, 177527, -17537, -2215, 6320);
		addMonster(35413, 177467, -17809, -2207, 6320);
		addMonster(35413, 177466, -17716, -2207, 6320);
		addMonster(35416, 177496, -17645, -2219, 6320);
		addMonster(35413, 177398, -17817, -2207, 6320);
		addMonster(35416, 177560, -17849, -2219, 6320);
		addMonster(35415, 177524, -17763, -2215, 6320);
		addMonster(35415, 177432, -17791, -2215, 6320);
		addMonster(35413, 177397, -17723, -2207, 6320);
		addMonster(35415, 177523, -17810, -2215, 6320);
		addMonster(35416, 177501, -17384, -2219, 6320);
		addMonster(35413, 177603, -17539, -2207, 6320);
		addMonster(35416, 177725, -17503, -2219, 6320);
		addMonster(35416, 177666, -17508, -2219, 6320);
		addMonster(35415, 177564, -17425, -2215, 6320);
		addMonster(35413, 177749, -17538, -2207, 6320);
		addMonster(35413, 177603, -17446, -2207, 6320);
		addMonster(35413, 177749, -17446, -2207, 6320);
		addMonster(35416, 177728, -17377, -2219, 6320);
		addMonster(35415, 177440, -17380, -2215, 6320);
		addMonster(35416, 177567, -17376, -2219, 6320);
		addMonster(35414, 177276, -17148, -2200, 6320);
		addMonster(35416, 177724, -17594, -2219, 6320);
		addMonster(35415, 177436, -17605, -2215, 6320);
		addMonster(35415, 177636, -17516, -2215, 6320);
		addMonster(35413, 177601, -17811, -2207, 6320);
		addMonster(35413, 177693, -17718, -2207, 6320);
		addMonster(35416, 177722, -17728, -2219, 6320);
		addMonster(35415, 177559, -17708, -2215, 6320);
		addMonster(35416, 177662, -17644, -2219, 6320);
		addMonster(35415, 177635, -17603, -2215, 6320);
		addMonster(35413, 177694, -17811, -2207, 6320);
		addMonster(35415, 177558, -17755, -2215, 6320);
		addMonster(35413, 177693, -17447, -2207, 6320);
		addMonster(35413, 177693, -17629, -2207, 6320);
		addMonster(35416, 177664, -17559, -2219, 6320);
		addMonster(35413, 177466, -17397, -2207, 6320);
		addMonster(35413, 177466, -17627, -2207, 6320);
		addMonster(35416, 177724, -17553, -2219, 6320);
		addMonster(35413, 177750, -17628, -2207, 6320);
		addMonster(35413, 177751, -17810, -2207, 6320);
		addMonster(35416, 177661, -17818, -2219, 6320);
		addMonster(35416, 177496, -17686, -2219, 6320);
		addMonster(35413, 177750, -17717, -2207, 6320);
		addMonster(35415, 177633, -17740, -2215, 6320);
		addMonster(35413, 177693, -17539, -2207, 6320);
		addMonster(35413, 177604, -17628, -2207, 6320);
		addMonster(35415, 177431, -17836, -2215, 6320);
		addMonster(35415, 177631, -17835, -2215, 6320);
		addMonster(35416, 177722, -17768, -2219, 6320);
		addMonster(35413, 177246, -17128, -2200, 6320);*/
	}

	public void spawnMonsters()
	{
		for(MonsterLocation ml : _monsters)

			try
			{
				L2NpcTemplate template = NpcTable.getInstance().getTemplate(ml.getId());
				L2Spawn sp = new L2Spawn(template);
				sp.setAmount(1);
				sp.setLocx(ml.getX());
				sp.setLocy(ml.getY());
				sp.setLocz(ml.getZ());
				sp.setHeading(ml.getHeading());
				sp.setRespawnDelay(300); //3 * 60
				sp.setLocation(0);
				sp.init();
				_spawns.add(sp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			set_monsterdespawn(ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnMonsters(), 3600000));  //60 * 60 * 1000
	}

	protected class DeSpawnMonsters implements Runnable
	{
		public void run()
		{
			DeSpawn();
		}
	}

	public void DeSpawn()
	{
		for(L2Spawn sp : _spawns)
		{
			sp.stopRespawn();
			sp.getLastSpawn().doDie(sp.getLastSpawn());
		}
			_spawns.clear();
			_spawns = null;
	}

	public void set_gustav(ScheduledFuture<?> _gustav)
	{
		this._gustav = _gustav;
	}

	public ScheduledFuture<?> get_gustav()
	{
		return _gustav;
	}

	public void set_dietrich(ScheduledFuture<?> _dietrich)
	{
		this._dietrich = _dietrich;
	}

	public ScheduledFuture<?> get_dietrich()
	{
		return _dietrich;
	}

	public void set_mikhail(ScheduledFuture<?> _mikhail)
	{
		this._mikhail = _mikhail;
	}

	public ScheduledFuture<?> get_mikhail()
	{
		return _mikhail;
	}

	public void set_monsterdespawn(ScheduledFuture<?> _monsterdespawn)
	{
		this._monsterdespawn = _monsterdespawn;
	}

	public ScheduledFuture<?> get_monsterdespawn()
	{
		return _monsterdespawn;
	}

	protected class DeSpawnTimer implements Runnable
	{
		L2NpcInstance _npc = null;
		public DeSpawnTimer(L2NpcInstance npc)
		{
			_npc = npc;
		}
		public void run()
		{
			if (_npc.getNpcId() == 35410)
				_npc.onDecay();
		}
	}
}