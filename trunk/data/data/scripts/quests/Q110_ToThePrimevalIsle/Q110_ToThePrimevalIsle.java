/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q110_ToThePrimevalIsle;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q110_ToThePrimevalIsle extends Quest
{
	private static final String qn = "Q110_ToThePrimevalIsle";
	
	// NPCs
	private final static int ANTON = 31338;
	private final static int MARQUEZ = 32113;
	
	// Item
	private final static int ANCIENT_BOOK = 8777;
	
	// Reward
	private final static int ADENA = 57;
	
	public Q110_ToThePrimevalIsle(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		questItemIds = new int[]
		{
			ANCIENT_BOOK
		};
		
		addStartNpc(ANTON);
		addTalkId(ANTON, MARQUEZ);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(qn);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31338-02.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.giveItems(ANCIENT_BOOK, 1);
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32113-03.htm") && st.getQuestItemsCount(ANCIENT_BOOK) == 1)
		{
			st.playSound(QuestState.SOUND_FINISH);
			st.rewardItems(ADENA, 169380);
			st.takeItems(ANCIENT_BOOK, 1);
			st.exitQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getLevel() >= 75)
					htmltext = "31338-01.htm";
				else
				{
					htmltext = "31338-00.htm";
					st.exitQuest(true);
				}
				break;
			
			case State.STARTED:
				switch (npc.getNpcId())
				{
					case ANTON:
						htmltext = "31338-01c.htm";
						break;
					
					case MARQUEZ:
						htmltext = "32113-01.htm";
						break;
				}
				break;
			
			case State.COMPLETED:
				htmltext = Quest.getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q110_ToThePrimevalIsle(110, qn, "To the Primeval Isle");
	}
}