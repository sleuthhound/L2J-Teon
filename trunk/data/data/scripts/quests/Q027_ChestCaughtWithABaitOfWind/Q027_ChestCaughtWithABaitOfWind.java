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
package quests.Q027_ChestCaughtWithABaitOfWind;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;

public class Q027_ChestCaughtWithABaitOfWind extends Quest
{
	private static final String qn = "Q027_ChestCaughtWithABaitOfWind";
	
	// NPCs
	private final static int Lanosco = 31570;
	private final static int Shaling = 31442;
	
	// Items
	private final static int LargeBlueTreasureChest = 6500;
	private final static int StrangeBlueprint = 7625;
	private final static int BlackPearlRing = 880;
	
	public Q027_ChestCaughtWithABaitOfWind(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		questItemIds = new int[]
		{
			StrangeBlueprint
		};
		
		addStartNpc(Lanosco);
		addTalkId(Lanosco, Shaling);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(qn);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31570-04.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31570-07.htm"))
		{
			if (st.getQuestItemsCount(LargeBlueTreasureChest) == 1)
			{
				st.set("cond", "2");
				st.takeItems(LargeBlueTreasureChest, 1);
				st.giveItems(StrangeBlueprint, 1);
			}
			else
				htmltext = "31570-08.htm";
		}
		else if (event.equalsIgnoreCase("31434-02.htm"))
		{
			if (st.getQuestItemsCount(StrangeBlueprint) == 1)
			{
				htmltext = "31434-02.htm";
				st.takeItems(StrangeBlueprint, 1);
				st.giveItems(BlackPearlRing, 1);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = ("31434-03.htm");
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
				if (player.getLevel() >= 27 && player.getLevel() <= 29)
				{
					QuestState st2 = player.getQuestState("Q050_LanoscosSpecialBait");
					if (st2 != null)
					{
						if (st2.isCompleted())
							htmltext = "31570-01.htm";
						else
						{
							htmltext = "31570-02.htm";
							st.exitQuest(true);
						}
					}
					else
					{
						htmltext = "31570-03.htm";
						st.exitQuest(true);
					}
				}
				else
					htmltext = "31570-02.htm";
				break;
			
			case State.STARTED:
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case Lanosco:
						if (cond == 1)
						{
							htmltext = ("31570-05.htm");
							if (st.getQuestItemsCount(LargeBlueTreasureChest) == 0)
								htmltext = ("31570-06.htm");
						}
						else if (cond == 2)
							htmltext = ("31570-09.htm");
						break;
					
					case Shaling:
						if (cond == 2)
							htmltext = ("31434-01.htm");
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
		new Q027_ChestCaughtWithABaitOfWind(27, qn, "Chest caught with a bait of wind");
	}
}