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

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author Forsaiken
 */
public class EffectSignetAntiSummon extends EffectSignet
{
	public EffectSignetAntiSummon(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public EffectType getEffectType()
	{
		return EffectType.SIGNET_GROUND;
	}

	@Override
	public boolean onActionTime()
	{
		int mpConsume = getSkill().getMpConsume();
		L2PcInstance caster = (L2PcInstance) getEffected();
		for (L2Character cha : zone.getCharactersInZone())
		{
			if (cha == null)
				continue;
			if (cha instanceof L2PlayableInstance)
			{
				L2PcInstance owner = null;
				if (cha instanceof L2Summon)
					owner = ((L2Summon) cha).getOwner();
				else
					owner = (L2PcInstance) cha;
				if (owner != null && owner.getPet() != null)
				{
					if (mpConsume > caster.getCurrentMp())
					{
						caster.sendPacket(new SystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
						return false;
					}
					else
						caster.reduceCurrentMp(mpConsume);
					owner.getPet().unSummon(owner);
					owner.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, caster);
				}
			}
		}
		return true;
	}
}