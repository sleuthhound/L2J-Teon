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
package ai.group_template;
 
import java.util.Collection;

import javolution.util.FastList;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;
 
public class Monastery extends L2AttackableAIScript
{
	static final int[] mobs1 = {22124, 22125, 22126, 22127, 22129};
	static final int[] mobs2 = {22134, 22135};
	static final String[] text = {
		"You cannot carry a weapon without authorization!",
		"name, why would you choose the path of darkness?!",
		"name! How dare you defy the will of Einhasad!"
	};
	private static boolean _isAttacked = false;

    public Monastery(int questId, String name, String descr)
    {
        super(questId, name, descr);
        registerMobs(mobs1);
        registerMobs(mobs2);
        _isAttacked = false;
    }
 
    public String onAggroRangeEnter(L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
    	if (contains(mobs1,npc.getNpcId()) && !npc.isInCombat() && npc.getTarget() == null)
    	{
    		if (player.getActiveWeaponInstance() != null)
    		{
    			npc.setTarget(player);
    			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text[0]));
    			switch (npc.getNpcId())
    			{
    				case 22124:
    				case 22126:
    				{
    					L2Skill skill = SkillTable.getInstance().getInfo(4589,8);
    	    			npc.doCast(skill);
    	    			break;
    				}
    				default:
    				{
    					npc.setIsRunning(true);
    	    			((L2Attackable) npc).addDamageHate(player, 0, 999);
    	    			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				_isAttacked = true;
    	    			break;
    				}
    			}
    		}
    		else if (((L2Attackable)npc).getMostHated() == null) 
    			return null;
    	}
        return super.onAggroRangeEnter(npc, player, isPet);
    }

    public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
    	if (contains(mobs2,npc.getNpcId()))
    	{
    		if (skill.getSkillType() == SkillType.AGGDAMAGE && targets.length != 0)
    		{
    			for (L2Object obj : targets)
    			{
    				if (obj.equals(npc))
    				{
    					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text[Rnd.get(2)+1].replace("name", caster.getName())));
	    				((L2Attackable) npc).addDamageHate(caster, 0, 999);
	    				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, caster);
            				_isAttacked = true;
	    				break;
    				}
    			}
    		}
    	}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
    
    public String onSpawn(L2NpcInstance npc)
	{
    	if (contains(mobs1,npc.getNpcId()))
    	{
    		FastList<L2PlayableInstance> result = new FastList<L2PlayableInstance>();
    		Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
    		for (L2Object obj : objs)
			{
				if (obj instanceof L2PcInstance || obj instanceof L2PetInstance)
				{
					if (Util.checkIfInRange(npc.getAggroRange(), npc, obj, true) && !((L2Character) obj).isDead())
						result.add((L2PlayableInstance) obj);
				}
			}
    		if (!result.isEmpty() && result.size() != 0)
    		{
    			Object[] characters = result.toArray();
    			for (Object obj : characters)
    			{
    	    		L2PlayableInstance target = (L2PlayableInstance) (obj instanceof L2PcInstance ? obj : ((L2Summon) obj).getOwner());
    	    		if (target.getActiveWeaponInstance() != null && !npc.isInCombat() && npc.getTarget() == null)
    	    		{
    	    			npc.setTarget(target);
    	    			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text[0]));
    	    			switch (npc.getNpcId())
    	    			{
    	    				case 22124:
    	    				case 22126:
    	    				case 22127:
    	    				{
    	    					L2Skill skill = SkillTable.getInstance().getInfo(4589,8);
    	    	    			npc.doCast(skill);
    	    	    			break;
    	    				}
    	    				default:
    	    				{
    	    					npc.setIsRunning(true);
    	    	    			((L2Attackable) npc).addDamageHate(target, 0, 999);
    	    	    			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
            				_isAttacked = true;
    	    	    			break;
    	    				}
    	    			}
    	    		}
    			}
    		}
    	}
		return super.onSpawn(npc);
	}
    
    public String onSpellFinished(L2NpcInstance npc, L2PcInstance player, L2Skill skill)
    {
    	if (contains(mobs1,npc.getNpcId()) && skill.getId() == 4589)
    	{
    		npc.setIsRunning(true);
    		((L2Attackable) npc).addDamageHate(player, 0, 999);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
            				_isAttacked = true;
    	}
    	return super.onSpellFinished(npc, player, skill);
    }
    
    public static void main(String[] args)
    {
        new Monastery(-1, "Monastery", "ai");
    }
}