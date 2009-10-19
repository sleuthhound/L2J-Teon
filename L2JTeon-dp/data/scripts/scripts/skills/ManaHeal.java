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
package scripts.skills;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * Mana Heal Skills Handler (Recharge and etc.) Date: 2007/08/31
 */
public class ManaHeal implements ISkillHandler
{
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	private static final SkillType[] SKILL_IDS = { SkillType.MANAHEAL, SkillType.MANARECHARGE, SkillType.MANAHEAL_PERCENT };

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance, net.sf.l2j.gameserver.model.L2ItemInstance)
	 */
	public void useSkill(L2Character actChar, L2Skill skill, L2Object[] targets)
	{
		SystemMessage sm;
		L2Character target = null;
		L2PcInstance targetInstance = null;
		int classId = 0;
		int skillId = 0;
		for (int index = 0; index < targets.length; index++)
		{
			target = (L2Character) targets[index];
			if (target instanceof L2PcInstance)
			{
				targetInstance = (L2PcInstance) target;
				classId = targetInstance.getClassId().getId();
				skillId = skill.getId();
				// skill "recharge" cant be used on player-classes that own it,
				// self too (in Interlude)
				if ((skillId == 1013) && ((classId == 29) || (classId == 30) || (classId == 42) || (classId == 43)))
				{
					if (actChar instanceof L2PcInstance)
					{
						sm = new SystemMessage(SystemMessageId.TARGET_IS_INCORRECT);
						actChar.sendPacket(sm);
					}
					return;
				}
			}
			// calculate MP heal power
			double mp = skill.getPower();
			if (skill.getSkillType() == SkillType.MANAHEAL_PERCENT)
			{
				mp = target.getMaxMp() * mp / 100.0;
			}
			else
			{
				mp = skill.getSkillType() == SkillType.MANARECHARGE ? target.calcStat(Stats.RECHARGE_MP_RATE, mp, null, null) : mp;
			}
			// heal MP
			target.setLastHealAmount((int) mp);
			target.setCurrentMp(mp + target.getCurrentMp());
			StatusUpdate sump = new StatusUpdate(target.getObjectId());
			sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
			target.sendPacket(sump);
			// creating system messege about restore MP
			if (actChar instanceof L2PcInstance)
			{
				sm = new SystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1);
				sm.addString(actChar.getName());
			}
			else
			{
				sm = new SystemMessage(SystemMessageId.S1_MP_RESTORED);
			}
			// send message
			sm.addNumber((int) mp);
			target.sendPacket(sm);
		}
	}

	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}