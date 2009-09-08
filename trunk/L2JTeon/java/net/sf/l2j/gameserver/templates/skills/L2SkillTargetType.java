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

/**
 *@Last_Author Stefoulis15 
 */

/** Target types of skills : SELF, PARTY, CLAN, PET... */
public enum L2SkillTargetType
{
	
    //========================================================================================================================
	//					Target Types																						
	//========================================================================================================================

    TARGET_NONE,
    TARGET_SELF,
    TARGET_ONE,
    TARGET_PARTY,
    TARGET_ALLY,
    TARGET_CLAN,
    TARGET_PET,
    TARGET_AREA,
    TARGET_AURA,
    TARGET_CORPSE,
    TARGET_UNDEAD,
    TARGET_AREA_UNDEAD,
    TARGET_MULTIFACE,
    TARGET_CORPSE_ALLY,
    TARGET_CORPSE_CLAN,
    TARGET_CORPSE_PLAYER,
    TARGET_CORPSE_PET,
    TARGET_ITEM,
    TARGET_AREA_CORPSE_MOB,
    TARGET_CORPSE_MOB,
    TARGET_UNLOCKABLE,
    TARGET_HOLY,
    TARGET_PARTY_MEMBER,
    TARGET_PARTY_OTHER,
    TARGET_ENEMY_SUMMON,
    TARGET_OWNER_PET,
    TARGET_SIGNET_GROUND,
    TARGET_SIGNET,
    TARGET_GROUND
    
}