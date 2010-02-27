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

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeFlagInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeGuardInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.util.Rnd;

/**
 * @author Harpun
 */
final class EffectFear extends L2Effect
{
	public static final int FEAR_RANGE = 500;

	public EffectFear(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public EffectType getEffectType()
	{
		return EffectType.FEAR;
	}

	/** Notify started */
	@Override
	public void onStart()
	{
		if (!getEffected().isAfraid())
		{
			getEffected().startFear();
			// Fear skills cannot be used l2pcinstance to l2pcinstance. Heroic
			// Dread, Curse: Fear, Fear and Horror , Sword Symphony, Word of Fear and Mass Curse Fear are the exceptions.
			if ((getEffected() instanceof L2PcInstance) && (getEffector() instanceof L2PcInstance) && (getSkill().getId() != 1376) && (getSkill().getId() != 1169) && (getSkill().getId() != 65) && (getSkill().getId() != 1092) && (getSkill().getId() != 98) && (getSkill().getId() != 1272) && (getSkill().getId() != 1272) && (getSkill().getId() != 1381))
				return;
			if (getEffected() instanceof L2FolkInstance)
				return;
			if (getEffected() instanceof L2SiegeGuardInstance)
				return;
			// Fear skills cannot be used on Headquarters Flag.
			if (getEffected() instanceof L2SiegeFlagInstance)
				return;
			if (getEffected() instanceof L2SiegeSummonInstance)
				return;
			int posX = getEffected().getX();
			int posY = getEffected().getY();
			int posZ = getEffected().getZ();
			int signx = -1;
			int signy = -1;
			if (getEffected().getX() > getEffector().getX())
				signx = 1;
			if (getEffected().getY() > getEffector().getY())
				signy = 1;
			posX += signx * FEAR_RANGE;
			posY += signy * FEAR_RANGE;
			if (!getEffected().isRaid() && !(getEffected() instanceof L2DoorInstance) && !(getEffected() instanceof L2NpcInstance && ((L2NpcInstance) getEffected()).getNpcId() == 35062))
			{
				int chance = Rnd.get(100);
				if (getSkill().getLethalChance2() > 0 && chance < Formulas.getInstance().calcLethal(getEffector(), getEffected(), getSkill().getLethalChance2()))
				{
					if (getEffected() instanceof L2NpcInstance)
					{
						getEffected().reduceCurrentHp(getEffected().getCurrentHp() - 1, getEffector());
						getEffector().sendPacket(new SystemMessage(SystemMessageId.LETHAL_STRIKE));
					}
				}
			}
			getEffected().setRunning();
			getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(posX, posY, posZ, 0));
		}
	}

	/** Notify exited */
	@Override
	public void onExit()
	{
		getEffected().stopFear(this);
	}

	@Override
	public boolean onActionTime()
	{
		return true;
	}
}