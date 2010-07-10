/* This program is free software; you can redistribute it and/or modify
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
package net.sf.l2j.gameserver.instancemanager.clanhallsiege;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

import javolution.util.FastList;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.entity.ClanHallSiege;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author Maxi
 **/
public class CHSiegeGuardsManager extends ClanHallSiege
{
	protected static Logger _log = Logger.getLogger(CHSiegeGuardsManager.class.getName());
	private static CHSiegeGuardsManager _instance;

	protected List<L2Spawn> _SiegeGuardSpawn = new FastList<L2Spawn>();
	protected L2Spawn _lidiaHellmannSpawn = null;
	protected L2Spawn _gustavSpawn = null;

	protected List<L2NpcInstance> _SiegeGuard = new FastList<L2NpcInstance>();
	protected List<L2MonsterInstance> _SiegeGuard1 = new FastList<L2MonsterInstance>();
	protected L2GrandBossInstance _lidiaHellmann = null;
	protected L2GrandBossInstance _gustav = null;

	private static final int GUSTAV = 35410;
	private static final int GMINION1 = 35408;
	private static final int GMINION2 = 35409;

	private static final int LIDIA = 35629;
	private static final int MINION1 = 35630;
	private static final int MINION2 = 35631;

	public CHSiegeGuardsManager()
	{
	}

	public static CHSiegeGuardsManager getInstance()
	{
		if (_instance == null)
			_instance = new CHSiegeGuardsManager();
		return _instance;
	}

	// initialize
	public void init()
	{
		// load spawn data of monsters.
		loadFOTDSiegeGuards();
		loadLidiaHellmann();
		loadDCSiegeGuards();
		loadGustav();
	}

	protected void spawnFOTDSiegeGuards()
	{
		if (!_SiegeGuard.isEmpty())
			deleteFOTDSiegeGuards();

		for (L2Spawn s : _SiegeGuardSpawn)
		{
			s.startRespawn();
			_SiegeGuard.add(s.doSpawn());
		}
	}

	protected void deleteFOTDSiegeGuards()
	{
		for (L2NpcInstance rg : _SiegeGuard)
		{
			rg.getSpawn().stopRespawn();
			rg.deleteMe();
		}
		_SiegeGuard.clear();
	}

	protected void loadFOTDSiegeGuards()
	{
		_SiegeGuardSpawn.clear();
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM cch_guards Where npc_templateid between ? and ? ORDER BY id");
			statement.setInt(1, 35633);
			statement.setInt(2, 35634);
			statement.setInt(3, 35635);
			statement.setInt(4, 35636);
			statement.setInt(5, 35637);
			ResultSet rset = statement.executeQuery();
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			while (rset.next())
			{
				template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
				if (template1 != null)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(rset.getInt("count"));
					spawnDat.setLocx(rset.getInt("locx"));
					spawnDat.setLocy(rset.getInt("locy"));
					spawnDat.setLocz(rset.getInt("locz"));
					spawnDat.setHeading(rset.getInt("heading"));
					spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_SiegeGuardSpawn.add(spawnDat);
				}
				else
					_log.warning("Fortress of The Dead Siege Guards: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
			}
			rset.close();
			statement.close();
			_log.info("Fortress of The Dead Siege Guards: Loaded " + _SiegeGuardSpawn.size() + " Siege Guards spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("Fortress of The Dead Siege Guards: Spawn could not be initialized: " + e);
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

	protected void spawnLidia()
	{
		if (!FortressofTheDeadManager.getInstance().getIsInProgress())
		{
			_lidiaHellmann.setIsImmobilized(true);
			_lidiaHellmann.setIsInvul(true);
			_lidiaHellmann.getSpawn().stopRespawn();
			_lidiaHellmann.deleteMe();
		}
		else {
			_lidiaHellmann = (L2GrandBossInstance) _lidiaHellmannSpawn.doSpawn();
			_lidiaHellmann.setIsImmobilized(false);
			_lidiaHellmann.setIsInvul(false);
		}
		spawnFOTDSiegeGuards();
		resetSpawns(1);
	}

	protected void deleteLidia()
	{
		if (_lidiaHellmann != null)
		{
			_lidiaHellmann.setIsImmobilized(false);
			_lidiaHellmann.setIsInvul(false);
			_lidiaHellmann.getSpawn().stopRespawn();
			_lidiaHellmann.deleteMe();
		}
		_lidiaHellmann.setIsImmobilized(false);
		_lidiaHellmann.setIsInvul(false);
	}

	protected void loadLidiaHellmann()
	{
		_lidiaHellmannSpawn = null;
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM cch_guards Where npc_templateid = ? ORDER BY id");
			statement.setInt(1, LIDIA);
			statement.setInt(2, MINION1);
			statement.setInt(3, MINION2);
			ResultSet rset = statement.executeQuery();
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			while (rset.next())
			{
				template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
				if (template1 != null)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(rset.getInt("count"));
					spawnDat.setLocx(rset.getInt("locx"));
					spawnDat.setLocy(rset.getInt("locy"));
					spawnDat.setLocz(rset.getInt("locz"));
					spawnDat.setHeading(rset.getInt("heading"));
					spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_lidiaHellmannSpawn = spawnDat;
				}
				else
					_log.warning("Fortress of The Dead Siege Guards: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
			}
			rset.close();
			statement.close();
			_log.info("Fortress of The Dead Siege Guards: Loaded Fortress of The Dead spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("Fortress of The Dead Siege Guards: Spawn could not be initialized: " + e);
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
/*		_lidiaHellmann.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
		if (_lidiaHellmann.isAfraid()) _lidiaHellmann.stopFear(null);
		_lidiaHellmann.startFear(); if (_lidiaHellmann.getZ() >= -10476)
		if (_lidiaHellmann != null && _lidiaHellmann.isDead())
			_lidiaHellmann.getSpawn().stopRespawn();
		else
			deleteVanHalter();

			_lidiaHellmann.setHeading(16384);
			_lidiaHellmann.setTarget(_RitualOffering);

					L2Skill skill = SkillTable.getInstance().getInfo(1168, 7);
					_lidiaHellmann.setTarget(_RitualOffering);
					_lidiaHellmann.setIsImmobilized(false);
					_lidiaHellmann.doCast(skill);
					_lidiaHellmann.setIsImmobilized(true);*/

	public void resetSpawns(int val)
	{
		if (val == 1)
		{
			deleteFOTDSiegeGuards();
			deleteLidia();
			spawnLidia();
		}
		if (val == 2)
		{
			deleteDCSiegeGuards();
			deleteGustav();
			spawnGustav();
		}
	}

	protected void spawnDCSiegeGuards()
	{
		if (!_SiegeGuard.isEmpty())
			deleteDCSiegeGuards();

		if (DevastatedCastleManager.getInstance().getIsInProgress())
		{
			for (L2Spawn s : _SiegeGuardSpawn)
			{
				s.startRespawn();
				_SiegeGuard.add(s.doSpawn());
			}
		}
	}

	protected void deleteDCSiegeGuards()
	{
		if (!DevastatedCastleManager.getInstance().getIsInProgress())
		{
			for (L2NpcInstance rg : _SiegeGuard)
			{
				rg.getSpawn().stopRespawn();
				rg.deleteMe();
			}
			_SiegeGuard.clear();
		}
	}

	protected void loadDCSiegeGuards()
	{
		_SiegeGuardSpawn.clear();
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM cch_guards Where npc_templateid between ? and ? ORDER BY id");
			statement.setInt(1, 35411);
			statement.setInt(2, 35412);
			statement.setInt(3, 35413);
			statement.setInt(4, 35414);
			statement.setInt(5, 35415);
			statement.setInt(6, 35416);
			ResultSet rset = statement.executeQuery();
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			while (rset.next())
			{
				template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
				if (template1 != null)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(rset.getInt("count"));
					spawnDat.setLocx(rset.getInt("locx"));
					spawnDat.setLocy(rset.getInt("locy"));
					spawnDat.setLocz(rset.getInt("locz"));
					spawnDat.setHeading(rset.getInt("heading"));
					spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_SiegeGuardSpawn.add(spawnDat);
				}
				else
					_log.warning("Devastated Castle Siege Guards: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
			}
			rset.close();
			statement.close();
			_log.info("Devastated Castle Siege Guards: Loaded " + _SiegeGuardSpawn.size() + " Siege Guards spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("Devastated Castle Siege Guards: Spawn could not be initialized: " + e);
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

	protected void spawnGustav()
	{
		_gustav = (L2GrandBossInstance) _gustavSpawn.doSpawn();
		_gustav.setIsImmobilized(false);
		_gustav.setIsInvul(false);
		spawnDCSiegeGuards();
		resetSpawns(2);
	}

	protected void deleteGustav()
	{
		if (_gustav != null)
		{
			_gustav.setIsImmobilized(false);
			_gustav.setIsInvul(false);
			_gustav.getSpawn().stopRespawn();
			_gustav.deleteMe();
		}
		_gustav.setIsImmobilized(false);
		_gustav.setIsInvul(false);
		_gustav.getSpawn().stopRespawn();
		_gustav.deleteMe();
	}

	protected void loadGustav()
	{
		_gustavSpawn = null;
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay FROM cch_guards Where npc_templateid = ? ORDER BY id");
			statement.setInt(1, GUSTAV);
			statement.setInt(2, GMINION1);
			statement.setInt(3, GMINION2);
			ResultSet rset = statement.executeQuery();
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			while (rset.next())
			{
				template1 = NpcTable.getInstance().getTemplate(rset.getInt("npc_templateid"));
				if (template1 != null)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(rset.getInt("count"));
					spawnDat.setLocx(rset.getInt("locx"));
					spawnDat.setLocy(rset.getInt("locy"));
					spawnDat.setLocz(rset.getInt("locz"));
					spawnDat.setHeading(rset.getInt("heading"));
					spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_gustavSpawn = spawnDat;
				}
				else
					_log.warning("Devastated Castle Siege Guards: Data missing in NPC table for ID: " + rset.getInt("npc_templateid") + ".");
			}
			rset.close();
			statement.close();
			_log.info("Devastated Castle Siege Guards: Loaded Devastated Castle spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("Devastated Castle Siege Guards: Spawn could not be initialized: " + e);
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
}