/*
 * This program is free software; you can redistribute it and/or modify
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
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;  
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.instancemanager.DuelManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.2.2.2.9 $ $Date: 2005/04/03 15:55:04 $
 */
public class Continuous implements ISkillHandler
{
    // private static Logger _log =
    // Logger.getLogger(Continuous.class.getName());
    private static final SkillType[] SKILL_IDS = { L2Skill.SkillType.BUFF, L2Skill.SkillType.DEBUFF, L2Skill.SkillType.DOT, L2Skill.SkillType.MDOT, L2Skill.SkillType.POISON, L2Skill.SkillType.BLEED, L2Skill.SkillType.HOT, L2Skill.SkillType.CPHOT, L2Skill.SkillType.MPHOT,
    // L2Skill.SkillType.MANAHEAL,
    // L2Skill.SkillType.MANA_BY_LEVEL,
    L2Skill.SkillType.FEAR, L2Skill.SkillType.CONT, L2Skill.SkillType.WEAKNESS, L2Skill.SkillType.REFLECT, L2Skill.SkillType.UNDEAD_DEFENSE, L2Skill.SkillType.AGGDEBUFF, L2Skill.SkillType.FORCE_BUFF };

    private L2Skill _skill;  
    
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.l2j.gameserver.handler.IItemHandler#useItem(net.sf.l2j.gameserver.model.L2PcInstance,
     *      net.sf.l2j.gameserver.model.L2ItemInstance)
     */
    public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
    {
	L2Character target = null;
	L2PcInstance player = null;
	if (activeChar instanceof L2PcInstance)
	{
	    player = (L2PcInstance) activeChar;
	}
    if (skill.getEffectId() != 0)  
    {  
    	int skillLevel = skill.getEffectLvl();  
    	int skillEffectId = skill.getEffectId();  
    	if (skillLevel == 0)  
    	{  
    		_skill = SkillTable.getInstance().getInfo(skillEffectId, 1);  
    	}  
    	else  
    	{  
    		_skill = SkillTable.getInstance().getInfo(skillEffectId, skillLevel);  
    	}  
    	
    	if (_skill != null)  
    		skill = _skill;  
    } 
	for (int index = 0; index < targets.length; index++)
	{
	    target = (L2Character) targets[index];
	    if ((skill.getSkillType() != L2Skill.SkillType.BUFF) && (skill.getSkillType() != L2Skill.SkillType.HOT) && (skill.getSkillType() != L2Skill.SkillType.CPHOT) && (skill.getSkillType() != L2Skill.SkillType.MPHOT) && (skill.getSkillType() != L2Skill.SkillType.UNDEAD_DEFENSE) && (skill.getSkillType() != L2Skill.SkillType.AGGDEBUFF) && (skill.getSkillType() != L2Skill.SkillType.CONT))
	    {
		if (target.reflectSkill(skill))
		{
		    target = activeChar;
		}
	    }
	    // Walls and Door should not be buffed
	    if ((target instanceof L2DoorInstance) && ((skill.getSkillType() == L2Skill.SkillType.BUFF) || (skill.getSkillType() == L2Skill.SkillType.HOT)))
	    {
		continue;
	    }
	    // Player holding a cursed weapon can't be buffed and can't buff
	    if (skill.getSkillType() == L2Skill.SkillType.BUFF)
	    {
		if (target != activeChar)
		{
		    if ((target instanceof L2PcInstance) && ((L2PcInstance) target).isCursedWeaponEquiped())
		    {
			continue;
		    } else if ((player != null) && player.isCursedWeaponEquiped())
		    {
			continue;
		    }
		}
	    }
	    if (skill.isOffensive())
	    {
		boolean ss = false;
		boolean sps = false;
		boolean bss = false;
		if (player != null)
		{
		    L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		    if (weaponInst != null)
		    {
			if (skill.isMagic())
			{
			    if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			    {
				bss = true;
				if (skill.getId() != 1020)
				{
				    weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
				}
			    } else if (weaponInst.getChargedSpiritshot() == L2ItemInstance.CHARGED_SPIRITSHOT)
			    {
				sps = true;
				if (skill.getId() != 1020)
				{
				    weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_NONE);
				}
			    }
			} else if (weaponInst.getChargedSoulshot() == L2ItemInstance.CHARGED_SOULSHOT)
			{
			    ss = true;
			    if (skill.getId() != 1020)
			    {
				weaponInst.setChargedSoulshot(L2ItemInstance.CHARGED_NONE);
			    }
			}
		    }
		} else if (activeChar instanceof L2Summon)
		{
		    L2Summon activeSummon = (L2Summon) activeChar;
		    if (skill.isMagic())
		    {
			if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT)
			{
			    bss = true;
			    activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
			} else if (activeSummon.getChargedSpiritShot() == L2ItemInstance.CHARGED_SPIRITSHOT)
			{
			    sps = true;
			    activeSummon.setChargedSpiritShot(L2ItemInstance.CHARGED_NONE);
			}
		    } else if (activeSummon.getChargedSoulShot() == L2ItemInstance.CHARGED_SOULSHOT)
		    {
			ss = true;
			activeSummon.setChargedSoulShot(L2ItemInstance.CHARGED_NONE);
		    }
		}
		boolean acted = Formulas.getInstance().calcSkillSuccess(activeChar, target, skill, ss, sps, bss);
		if (!acted)
		{
		    activeChar.sendPacket(new SystemMessage(SystemMessageId.ATTACK_FAILED));
		    continue;
		}
	    }
	    boolean stopped = false;
	    L2Effect[] effects = target.getAllEffects();
	    if (effects != null)
	    {
		for (L2Effect e : effects)
		{
		    if ((e != null) && (skill != null))
		    {
			if (e.getSkill().getId() == skill.getId())
			{
			    e.exit();
			    stopped = true;
			}
		    }
		}
	    }
	    if (skill.isToggle() && stopped)
	    {
		return;
	    }
	    // if this is a debuff let the duel manager know about it
	    // so the debuff can be removed after the duel
	    // (player & target must be in the same duel)
	    if ((target instanceof L2PcInstance) && ((L2PcInstance) target).isInDuel() && ((skill.getSkillType() == L2Skill.SkillType.DEBUFF) || (skill.getSkillType() == L2Skill.SkillType.BUFF)) && (player.getDuelId() == ((L2PcInstance) target).getDuelId()))
	    {
		DuelManager dm = DuelManager.getInstance();
		for (L2Effect buff : skill.getEffects(activeChar, target))
		{
		    if (buff != null)
		    {
			dm.onBuff(((L2PcInstance) target), buff);
		    }
		}
	    } else
	    {
		skill.getEffects(activeChar, target);
	    }
	    if (skill.getSkillType() == L2Skill.SkillType.AGGDEBUFF)
	    {
		if (target instanceof L2Attackable)
		{
		    target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, (int) skill.getPower());
		} else if (target instanceof L2PlayableInstance)
		{
		    if (target.getTarget() == activeChar)
		    {
			target.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, activeChar);
		    } else
		    {
			target.setTarget(activeChar);
		    }
		}
	    }
	}
	// self Effect :]
	L2Effect effect = activeChar.getFirstEffect(skill.getId());
	if ((effect != null) && effect.isSelfEffect())
	{
	    // Replace old effect with new one.
	    effect.exit();
	}
	skill.getEffectsSelf(activeChar);
    }

    public SkillType[] getSkillIds()
    {
	return SKILL_IDS;
    }
}