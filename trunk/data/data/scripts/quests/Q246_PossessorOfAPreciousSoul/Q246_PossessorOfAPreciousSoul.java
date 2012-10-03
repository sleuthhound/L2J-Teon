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
package quests.Q246_PossessorOfAPreciousSoul;

import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.util.Rnd;

public class Q246_PossessorOfAPreciousSoul extends Quest
{
	private static final String qn = "Q246_PossessorOfAPreciousSoul";
	
	// NPCs
	private static final int CARADINE = 31740;
	private static final int OSSIAN = 31741;
	private static final int LADD = 30721;
	
	// Items
	private static final int CARADINE_LETTER_2 = 7678;
	private static final int WATERBINDER = 7591;
	private static final int EVERGREEN = 7592;
	private static final int RAIN_SONG = 7593;
	private static final int RELIC_BOX = 7594;
	private static final int CARADINE_LETTER_3 = 7679;
	
	// Mobs
	private static final int PILGRIM_OF_SPLENDOR = 21541;
	private static final int JUDGE_OF_SPLENDOR = 21544;
	private static final int BARAKIEL = 25325;
	
	public Q246_PossessorOfAPreciousSoul(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		questItemIds = new int[]
		{
			WATERBINDER,
			EVERGREEN,
			RAIN_SONG,
			RELIC_BOX
		};
		
		addStartNpc(CARADINE);
		addTalkId(CARADINE, OSSIAN, LADD);
		
		addKillId(PILGRIM_OF_SPLENDOR, JUDGE_OF_SPLENDOR, BARAKIEL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(qn);
		if (st == null)
			return htmltext;
		
		// Caradine
		if (event.equalsIgnoreCase("31740-04.htm"))
		{
			st.set("cond", "1");
			st.takeItems(CARADINE_LETTER_2, 1);
			st.setState(State.STARTED);
			st.playSound(QuestState.SOUND_ACCEPT);
		}
		// Ossian
		else if (event.equalsIgnoreCase("31741-02.htm"))
		{
			st.set("cond", "2");
			st.playSound(QuestState.SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31741-05.htm"))
		{
			if (st.hasQuestItems(WATERBINDER) && st.hasQuestItems(EVERGREEN))
			{
				st.set("cond", "4");
				st.takeItems(WATERBINDER, 1);
				st.takeItems(EVERGREEN, 1);
				st.playSound(QuestState.SOUND_MIDDLE);
			}
			else
				htmltext = null;
		}
		else if (event.equalsIgnoreCase("31741-08.htm"))
		{
			if (st.hasQuestItems(RAIN_SONG))
			{
				st.set("cond", "6");
				st.takeItems(RAIN_SONG, 1);
				st.giveItems(RELIC_BOX, 1);
				st.playSound(QuestState.SOUND_MIDDLE);
			}
			else
				htmltext = null;
		}
		// Ladd
		else if (event.equalsIgnoreCase("30721-02.htm"))
		{
			if (st.hasQuestItems(RELIC_BOX))
			{
				st.takeItems(RELIC_BOX, 1);
				st.giveItems(CARADINE_LETTER_3, 1);
				st.addExpAndSp(719843, 0);
				st.playSound(QuestState.SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = null;
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
				if (st.hasQuestItems(CARADINE_LETTER_2))
				{
					if (!player.isSubClassActive() || player.getLevel() < 65)
					{
						htmltext = "31740-02.htm";
						st.exitQuest(true);
					}
					else
						htmltext = "31740-01.htm";
				}
				break;
			
			case State.STARTED:
				if (!player.isSubClassActive())
					break;
				
				int cond = st.getInt("cond");
				switch (npc.getNpcId())
				{
					case CARADINE:
						if (cond == 1)
							htmltext = "31740-05.htm";
						break;
					
					case OSSIAN:
						switch (cond)
						{
							case 1:
								htmltext = "31741-01.htm";
								break;
							case 2:
								htmltext = "31741-03.htm";
								break;
							case 3:
								if (st.hasQuestItems(WATERBINDER) && st.hasQuestItems(EVERGREEN))
									htmltext = "31741-04.htm";
								break;
							case 4:
								htmltext = "31741-06.htm";
								break;
							case 5:
								if (st.hasQuestItems(RAIN_SONG))
									htmltext = "31741-07.htm";
								break;
							case 6:
								htmltext = "31741-09.htm";
								break;
						}
						break;
					
					case LADD:
						if (cond == 6 && st.hasQuestItems(RELIC_BOX))
							htmltext = "30721-01.htm";
						break;
				}
				break;
			
			case State.COMPLETED:
				htmltext = Quest.getAlreadyCompletedMsg();
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		if (npcId == BARAKIEL)
		{
			QuestState st;
			if (player.isInParty())
			{
				for (L2PcInstance plr : getPartyMembers(player, npc, "cond", "4"))
				{
					if (!plr.isSubClassActive())
						continue;
					
					st = plr.getQuestState(qn);
					if (!st.hasQuestItems(RAIN_SONG))
					{
						st.set("cond", "5");
						st.giveItems(RAIN_SONG, 1);
						st.playSound(QuestState.SOUND_MIDDLE);
					}
				}
			}
			else if (player.isSubClassActive())
			{
				if (checkPlayerCondition(player, npc, "cond", "4"))
				{
					st = player.getQuestState(qn);
					if (!st.hasQuestItems(RAIN_SONG))
					{
						st.set("cond", "5");
						st.giveItems(RAIN_SONG, 1);
						st.playSound(QuestState.SOUND_MIDDLE);
					}
				}
			}
		}
		else
		{
			if (!player.isSubClassActive() || !checkPlayerCondition(player, npc, "cond", "2"))
				return null;
			
			if (Rnd.get(10) < 2)
			{
				final int neklaceOrRing = (npcId == PILGRIM_OF_SPLENDOR) ? WATERBINDER : EVERGREEN;
				QuestState st = player.getQuestState(qn);
				
				if (!st.hasQuestItems(neklaceOrRing))
				{
					st.giveItems(neklaceOrRing, 1);
					
					if (!st.hasQuestItems((npcId == PILGRIM_OF_SPLENDOR) ? EVERGREEN : WATERBINDER))
						st.playSound(QuestState.SOUND_ITEMGET);
					else
					{
						st.set("cond", "3");
						st.playSound(QuestState.SOUND_MIDDLE);
					}
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new Q246_PossessorOfAPreciousSoul(246, qn, "Possessor of a Precious Soul - 3");
	}
}