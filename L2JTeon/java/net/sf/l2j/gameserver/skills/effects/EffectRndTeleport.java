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
package net.sf.l2j.gameserver.skills.effects;

import java.util.List;

import javolution.util.FastList;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.util.Rnd;

public class EffectRndTeleport extends L2Effect
{
    public EffectRndTeleport(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    @Override
	public EffectType getEffectType()
    {
        return EffectType.RNDTELEPORT;
    }

    @Override
	public void onStart()
	{
		List<L2Character> targetList = new FastList<L2Character>();

        for (L2Object obj : getEffected().getKnownList().getKnownObjects().values())
        {
            if (obj == null)
                continue;
			if (obj.getObjectId() == getEffected().getObjectId())
				continue;
			if (obj instanceof L2PcInstance)
				continue;
            if (obj != getEffected()) targetList.add((L2Character)obj);
        }
		if (targetList.size()==0)
		{
			return;
		}
		int nextTargetIdx = Rnd.nextInt(targetList.size());
		L2Object target = targetList.get(nextTargetIdx);
		int RndSpawn = Rnd.get(-100,100);

		// getEffected().setXYZ(target.getX()-RndSpawn, target.getY()-RndSpawn, target.getZ());
		getEffected().teleToLocation(target.getX()-RndSpawn, target.getY()-RndSpawn, target.getZ());
    }

    @Override
	public void onExit()
	{
    }

    @Override
	public boolean onActionTime()
    {
        return false;
    }
}
