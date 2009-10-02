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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortResistSiegeManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.DevastatedCastleManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * @Author Maxi
 */
public class L2CHSiegeMonsterInstance extends L2Attackable
{
    private static final int MONSTER_MAINTENANCE_INTERVAL = 1000;

    public L2CHSiegeMonsterInstance(int objectId, L2NpcTemplate template)
    {
	super(objectId, template);
    }

    /**
     * Return true if the attacker is not another L2CHSiegeMonsterInstance.<BR>
     * <BR>
     */
    @Override
    public boolean isAutoAttackable(L2Character attacker)
    {
	if (attacker instanceof L2CHSiegeMonsterInstance)
	{
	    return false;
	}
	return !isEventMob;
    }

    /**
     * Return true if the L2CHSiegeMonsterInstance is Agressive (aggroRange > 0).<BR>
     * <BR>
     */
    @Override
    public boolean isAggressive()
    {
	return (getTemplate().aggroRange > 800) && !isEventMob;
    }

    @Override
    public void onSpawn()
    {
	super.onSpawn();
    }

    protected int getMaintenanceInterval()
    {
	return MONSTER_MAINTENANCE_INTERVAL;
    }

    @Override
    public boolean doDie(L2Character killer)
    {
        if (!super.doDie(killer))
    		return false;

        return true;
    }

    @Override
    public void addDamageHate(L2Character attacker, int damage, int aggro)
    {
	if (!(attacker instanceof L2CHSiegeMonsterInstance))
	{
	    super.addDamageHate(attacker, damage, aggro);
	}
    }

    @Override
    public void deleteMe()
    {
    	if (getNpcId() == 35410 || (getNpcId() == 35411 || (getNpcId() == 35412 || (getNpcId() == 35413 || (getNpcId() == 35414 || (getNpcId() == 35415 || (getNpcId() == 35416 || (getNpcId() == 35417 || (getNpcId() == 35418)))))))))
    		DevastatedCastleManager.getInstance().endSiege(true);

    	if (getNpcId() == 35368 || (getNpcId() == 35369 || (getNpcId() == 35370 || (getNpcId() == 35371 || (getNpcId() == 35372 || (getNpcId() == 35373 || (getNpcId() == 35374 || (getNpcId() == 35375 || (getNpcId() == 35376 || (getNpcId() == 35377))))))))))
    		FortResistSiegeManager.getInstance().endSiege(true);

	super.deleteMe();
    }
}