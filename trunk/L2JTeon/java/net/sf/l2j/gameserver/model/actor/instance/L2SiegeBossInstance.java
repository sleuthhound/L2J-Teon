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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.DevastatedCastleManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortResistSiegeManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortressofTheDeadManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * Author: Maxi
 */
public final class L2SiegeBossInstance extends L2MonsterInstance
{
	private static final int RAIDBOSS_MAINTENANCE_INTERVAL = 30000;

	public L2SiegeBossInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onSpawn()
	{
		super.onSpawn();
	}

	@Override
	protected int getMaintenanceInterval()
	{
		return RAIDBOSS_MAINTENANCE_INTERVAL;
	}

	/**
	 * Spawn all minions at a regular interval Also if boss is too far from home location at the time of this check, teleport it home
	 */
	@Override
	protected void manageMinions()
	{
		_minionList.spawnMinions();
		_minionMaintainTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
		{
			public void run()
			{
				// teleport raid boss home if it's too far from home
				// location
				L2Spawn bossSpawn = getSpawn();
				if (!isInsideRadius(bossSpawn.getLocx(), bossSpawn.getLocy(), bossSpawn.getLocz(), 5000, true, false))
				{
					teleToLocation(bossSpawn.getLocx(), bossSpawn.getLocy(), bossSpawn.getLocz(), true);
					healFull(); // prevents minor exploiting with it
				}
				_minionList.maintainMinions();
			}
		}, 60000, getMaintenanceInterval() + Rnd.get(5000));
	}

    /**
     * Reduce the current HP of the L2Attackable, update its _aggroList and launch the doDie Task if necessary.<BR><BR>
     */
    @Override
    public void reduceCurrentHp(double damage, L2Character attacker, boolean awake)
    {
        super.reduceCurrentHp(damage, attacker, awake);

        if (this.getNpcId() == 35368)
		{
			if (attacker instanceof L2PcInstance && ((L2PcInstance)attacker).getClan()!= null)
				FortResistSiegeManager.getInstance().addSiegeDamage(((L2PcInstance)attacker).getClan(), damage);
			} else
		{
        if (this.getNpcId() == 35410)
		{
			if (attacker instanceof L2PcInstance && ((L2PcInstance)attacker).getClan()!= null)
				DevastatedCastleManager.getInstance().addSiegeDamage(((L2PcInstance)attacker).getClan(), damage);
			}
		}
    }

	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
			return false;
		if (getNpcId() == 35368 && FortResistSiegeManager.getInstance().getIsInProgress()) // Bloody Lord Nurka
			FortResistSiegeManager.getInstance().endSiege(true);
		else if (getNpcId() == 35410 && DevastatedCastleManager.getInstance().getIsInProgress()) // Gustav
			DevastatedCastleManager.getInstance().endSiege(true);
		else if (getNpcId() == 35629 && FortressofTheDeadManager.getInstance().getIsInProgress()) // Lidia von Hellmann
			FortressofTheDeadManager.getInstance().endSiege(true);
		return true;
	}

	public void healFull()
	{
		super.setCurrentHp(super.getMaxHp());
		super.setCurrentMp(super.getMaxMp());
	}
}
