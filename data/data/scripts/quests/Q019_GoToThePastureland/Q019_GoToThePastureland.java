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
package quests.Q019_GoToThePastureland;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q019_GoToThePastureland extends Quest
{
	private final static String qn = "Q019_GoToThePastureland";
	
	// Items
	private final static int YoungWildBeastMeat = 7547;
	private final static int Adena = 57;
	
	// NPCs
	private final static int Vladimir = 31302;
	private final static int Tunatun = 31537;
	
	public Q019_GoToThePastureland(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		questItemIds = new int[]
		{
			YoungWildBeastMeat
		};
		
		addStartNpc(Vladimir);
		addTalkId(Vladimir, Tunatun);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(qn);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31302-01.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.giveItems(YoungWildBeastMeat, 1);
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("019_finish"))
		{
			if (st.getQuestItemsCount(YoungWildBeastMeat) == 1)
			{
				htmltext = "31537-01.htm";
				st.takeItems(YoungWildBeastMeat, 1);
				st.rewardItems(Adena, 30000);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "31537-02.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(qn);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getLevel() >= 63)
					htmltext = "31302-00.htm";
				else
					htmltext = "31302-03.htm";
				break;
			
			case State.STARTED:
				switch (npc.getNpcId())
				{
					case Vladimir:
						htmltext = "31302-02.htm";
						break;
					
					case Tunatun:
						htmltext = "31537-00.htm";
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
		new Q019_GoToThePastureland(19, qn, "Go to the Pastureland!");
	}
}