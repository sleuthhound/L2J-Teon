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

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;

/**
 * @author  Maxi
 * to java Kidzor
 */
public class ancientegg extends L2AttackableAIScript
{
	private int EGG = 18344;

	public ancientegg(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addAttackId(EGG);
	}

	public String onAttack(L2NpcInstance npc, L2PcInstance player, int damage, boolean isPet)
	{
		player.setTarget(player);
		player.doCast(SkillTable.getInstance().getInfo(5088,1));
		return super.onAttack(npc, player, damage, isPet);
	}

	public static void main(String[] args)
	{
		new ancientegg(-1, "ancientegg", "ai");
	}
}