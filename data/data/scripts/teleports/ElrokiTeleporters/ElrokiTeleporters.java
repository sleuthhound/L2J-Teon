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
package teleports.ElrokiTeleporters;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;

public class ElrokiTeleporters extends Quest
{
	public ElrokiTeleporters(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(32111, 32112);
		addTalkId(32111, 32112);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		int npcId = npc.getNpcId();
		
		if (npcId == 32111)
		{
			if (player.isInCombat())
				htmltext = "32111-no.htm";
			else
				player.teleToLocation(4990, -1879, -3178);
		}
		else if (npcId == 32112)
			player.teleToLocation(7557, -5513, -3221);
		
		st.exitQuest(true);
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new ElrokiTeleporters(-1, "ElrokiTeleporters", "teleports");
	}
}