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

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager.StatusEnum;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2RaidBossInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class BenomManager
{
	protected static final Logger _log = Logger.getLogger(BenomManager.class.getName());
	private static BenomManager _instance;

	public static final BenomManager getInstance()
	{
		if (_instance == null)
			_instance = new BenomManager();
		return _instance;
	}

	Castle _rune = CastleManager.getInstance().getCastle("Rune");
	L2RaidBossInstance _benom = null;
	protected ScheduledFuture<?> _sf;
	protected L2Spawn _spawn = null;
	protected int _x1 = 12082, _y1 = -49152, _z1 = -539, _x2 = 12121, _y2 = -49164, _z2 = -1115;

	public BenomManager()
	{
		_log.info("BenomManager: Loading.");
		scheduleBenom(false);
	}

	private class ScheduleSpawnBenom implements Runnable
	{
		private boolean _insiege;

		public ScheduleSpawnBenom(boolean inSiege)
		{
			_insiege = inSiege;
		}

		public void run()
		{
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(29054);
			if (template == null)
			{
				_log.warning("BenomManager Error, template not found!");
				return;
			}
			try
			{
				_spawn = new L2Spawn(template);
				_spawn.setHeading(0);
				_spawn.setLocx(_insiege ? _x1 : _x2);
				_spawn.setLocy(_insiege ? _y1 : _y2);
				_spawn.setLocz(_insiege ? _z1 : _z2);
				_spawn.setAmount(1);
				_spawn.setRespawnMinDelay(24 * 3600 * 1000);
				_spawn.setRespawnMaxDelay(24 * 3600 * 1000);
				_spawn.stopRespawn();
				SpawnTable.getInstance().addNewSpawn(_spawn, false);
				_benom = (L2RaidBossInstance) _spawn.doSpawn();

				if (_benom != null)
				{
					_benom.setCurrentHp(_benom.getMaxHp());
					_benom.setCurrentMp(_benom.getMaxMp());
					_benom.setRaidStatus(StatusEnum.ALIVE);
				} else
				{
					_log.warning("BenomManager Error: Spawn impossible!");
					return;
				}
			} catch (Exception ex)
			{
				_log.warning("BenomManager Error:");
				ex.printStackTrace();
				return;
			}
		}
	}

	public void onKillBenom()
	{
		_rune.setBossKilled(true);
		ThreadPoolManager.getInstance().scheduleGeneral( new ScheduleUnspawnBenom(), 60 * 1000);
		_log.info("BenomManager : Benom vient de mourir.");
	}

	protected class ScheduleUnspawnBenom implements Runnable
	{
		public void run()
		{
			removeSpawn();
		}
	}

	public void scheduleBenom(boolean force)
	{
		try
		{
			long ms = _rune.getSiegeDate().getTimeInMillis() - (new Date()).getTime();
			removeSpawn();
			if (ms > 24 * 3600 * 1000 || force)
			{
				_sf = ThreadPoolManager.getInstance().scheduleGeneral( new ScheduleSpawnBenom(false), ms - (24 * 3600 * 1000));
				_log.info("BenomManager: spawn in planning " + (ms - (24 * 3600 * 1000)) + "ms.");
			} 
			else if (ms <= 24 * 3600 * 1000 && ms > 3600 * 1000)
			{
				if (!_rune.isBossKilled())
				{
					_sf = ThreadPoolManager.getInstance().scheduleGeneral( new ScheduleSpawnBenom(false), 1000);
					_log.info("BenomManager: Benom not yet dead, spawn immediatly.");
				}
				else
					_log.info("BenomManager: Benom already dead, no respawn.");
			}
		} catch (Exception ex)
		{
			_log.warning("BenomManager error: Unable to schedule:");
			ex.printStackTrace();
			return;
		}
	}

	public void spawnBenomOnCtKilled()
	{
		if (_rune.isBossKilled())
		{
			_log.info("BenomManager: Benom died before the beginning of the siege, spawn cancel.");
			return;
		}
		removeSpawn();
		_sf = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleSpawnBenom(true), 10 * 1000);
		_log.info("BenomManager: Benom living at the beginning of the siege, spawn in 10 seconds.");
	}

	protected void removeSpawn()
	{
		try
		{
			if (_sf != null)
				_sf.cancel(true);
			if (_spawn != null)
			{
				if (_spawn.getLastSpawn() != null)
					_spawn.getLastSpawn().deleteMe();
				SpawnTable.getInstance().deleteSpawn(_spawn, false);
			}
		} catch (Exception ex)
		{
			_log.warning("BenomManager Error: Unable to delete the spawn");
			ex.printStackTrace();
			return;
		}
	}
}