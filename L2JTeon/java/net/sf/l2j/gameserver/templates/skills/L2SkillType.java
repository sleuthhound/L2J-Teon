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
package net.sf.l2j.gameserver.templates.skills;

import java.lang.reflect.Constructor;

import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillCharge;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillChargeDmg;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillCreateItem;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillDefault;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillDrain;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSeed;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSummon;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @Last_Author Stefoulis15
 */
public enum L2SkillType
{
    //========================================================================================================================
	//					Damage Skills																						
	//========================================================================================================================

	PDAM,
	MDAM,
	CPDAM,
	MANADAM,
	DOT,
	MDOT,
	DRAIN_SOUL,
	DRAIN(L2SkillDrain.class),
	DEATHLINK,
	BLOW,

    //========================================================================================================================
	//					Disablers																						
	//========================================================================================================================

	BLEED,
	POISON,
	STUN,
	ROOT,
	CONFUSION,
	FEAR,
	SLEEP,
	CONFUSE_MOB_ONLY,
	MUTE,
	PARALYZE,
	WEAKNESS,

    //========================================================================================================================
	//					HP , CP , MP																						
	//========================================================================================================================

	HEAL,
	HOT,
	BALANCE_LIFE,
	HEAL_PERCENT,
	HEAL_STATIC,
	COMBATPOINTHEAL,
	CPHOT,
	MANAHEAL,
	MANA_BY_LEVEL,
	MANAHEAL_PERCENT,
	MANARECHARGE,
	MPHOT,
    GIVE_SP, 
    
    //========================================================================================================================
	//					Aggro																						
	//========================================================================================================================

	AGGDAMAGE,
	AGGREDUCE,
	AGGREMOVE,
	AGGREDUCE_CHAR,
	AGGDEBUFF,

    //========================================================================================================================
	//					Fishing																							
	//========================================================================================================================

	FISHING,
	PUMPING,
	REELING,

    //========================================================================================================================
	//					Misc.																							
	//========================================================================================================================

	UNLOCK,
	ENCHANT_ARMOR,
	ENCHANT_WEAPON,
	SOULSHOT,
	SPIRITSHOT,
	SIEGEFLAG,
	TAKECASTLE,
	WEAPON_SA,
	DELUXE_KEY_UNLOCK,
	SOW,
    HARVEST,
    GET_PLAYER,

    //========================================================================================================================
	//					Creation																						
	//========================================================================================================================

	COMMON_CRAFT,
	DWARVEN_CRAFT,
	CREATE_ITEM(L2SkillCreateItem.class),
	SUMMON_TREASURE_KEY,

    //========================================================================================================================
	//					Summons																						
	//========================================================================================================================

	SUMMON(L2SkillSummon.class),
	FEED_PET,
	DEATHLINK_PET,
	STRSIEGEASSAULT,
	ERASE,
	BETRAY,

    //========================================================================================================================
	//					Cancels																						
	//========================================================================================================================

	CANCEL,
	MAGE_BANE,
	WARRIOR_BANE,
	NEGATE,

    //========================================================================================================================
	//					Others																						
	//========================================================================================================================

	BUFF,
	DEBUFF,
	PASSIVE,
	CONT,
	RESURRECT,
	CHARGE(L2SkillCharge.class),
	CHARGEDAM(L2SkillChargeDmg.class),
	MHOT,
	DETECT_WEAKNESS,
	LUCK,
	RECALL,
	SUMMON_FRIEND,
	REFLECT,
	SPOIL,
	SWEEP,
	FAKE_DEATH,
	UNBLEED,
	UNPOISON,
	UNDEAD_DEFENSE,
	SEED (L2SkillSeed.class),
	BEAST_FEED,
	FORCE_BUFF,
	FUSION,
	CLAN_GATE,
	
    //========================================================================================================================
	//					Unimplemented																						
	//========================================================================================================================

    NOTDONE;

    private final Class<? extends L2Skill> _class;

    public L2Skill makeSkill(StatsSet set)
    {
        try
        {
            Constructor<? extends L2Skill> c = _class.getConstructor(StatsSet.class);

            return c.newInstance(set);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private L2SkillType()
    {
        _class = L2SkillDefault.class;
    }

    private L2SkillType(Class<? extends L2Skill> classType)
    {
        _class = classType;
    }
}