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
package teleports.GrandBossTeleporters;

import ai.individual.Baium;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2GrandBossInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;
import net.sf.l2j.util.Rnd;

/**
 * This script leads behavior of multiple bosses teleporters.
 * <ul>
 * <li>13001, Heart of Warding : Teleport into Lair of Antharas</li>
 * <li>29055, Teleportation Cubic : Teleport out of Baium zone</li>
 * <li>31859, Teleportation Cubic : Teleport out of Lair of Antharas</li>
 * <li>31384, Gatekeeper of Fire Dragon : Opening some doors</li>
 * <li>31385, Heart of Volcano : Teleport into Lair of Valakas</li>
 * <li>31540, Watcher of Valakas Klein : Teleport into Hall of Flames</li>
 * <li>31686, Gatekeeper of Fire Dragon : Opens doors to Heart of Volcano</li>
 * <li>31687, Gatekeeper of Fire Dragon : Opens doors to Heart of Volcano</li>
 * <li>31759, Teleportation Cubic : Teleport out of Lair of Valakas</li>
 * <li>31862, Angelic Vortex : Baium Teleport (3 different HTMs according of situation)</li>
 * </ul>
 * @author Plim, original python script by Emperorc
 */
public class GrandBossTeleporters extends Quest
{
	private static final String qn = "GrandBossTeleporters";
	
	private static final int VALAKAS = 29028;
	
	public GrandBossTeleporters(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addFirstTalkId(29055, 31862);
		addStartNpc(13001, 29055, 31859, 31384, 31385, 31540, 31686, 31687, 31759, 31862);
		addTalkId(13001, 29055, 31859, 31384, 31385, 31540, 31686, 31687, 31759, 31862);
	}
	
	private Quest valakasAI()
	{
		return QuestManager.getInstance().getQuest("valakas");
	}
	
	private Quest antharasAI()
	{
		return QuestManager.getInstance().getQuest("antharas");
	}
	
	private static int _valakasPlayersCount = 0;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		
		if (event.equalsIgnoreCase("baium"))
		{
			// Player is mounted on a wyvern, cancel it.
			if (player.isFlying())
				htmltext = "31862-05.htm";
			// Player hasn't blooded fabric, cancel it.
			else if (!st.hasQuestItems(4295))
				htmltext = "31862-03.htm";
			// All is ok, take the item and teleport the player inside.
			else
			{
				st.takeItems(4295, 1);
				
				// allow entry for the player for the next 30 secs.
				GrandBossManager.getInstance().getZone(113100, 14500, 10077).allowPlayerEntry(player, 30);
				player.teleToLocation(113100, 14500, 10077);
			}
		}
		else if (event.equalsIgnoreCase("baium_story"))
			htmltext = "31862-02.htm";
		else if (event.equalsIgnoreCase("baium_exit"))
		{
			final int chance = Rnd.get(3);
			int x, y, z;
			
			switch (chance)
			{
				case 0:
					x = 108784 + Rnd.get(100);
					y = 16000 + Rnd.get(100);
					z = -4928;
					break;
				
				case 1:
					x = 113824 + Rnd.get(100);
					y = 10448 + Rnd.get(100);
					z = -5164;
					break;
				
				default:
					x = 115488 + Rnd.get(100);
					y = 22096 + Rnd.get(100);
					z = -5168;
					break;
			}
			player.teleToLocation(x, y, z);
		}
		else if (event.equalsIgnoreCase("31540"))
		{
			if (st.hasQuestItems(7267))
			{
				st.takeItems(7267, 1);
				player.teleToLocation(183813, -115157, -3303);
				st.set("allowEnter", "1");
			}
			else
				htmltext = "31540-06.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		
		switch (npc.getNpcId())
		{
			case 29055:
				htmltext = "29055-01.htm";
				break;
			
			case 31862:
				final int baiumState = GrandBossManager.getInstance().getBossStatus(29020);
				
				if (baiumState == Baium.AWAKE)
					htmltext = "31862-01.htm";
				else if (baiumState == Baium.DEAD)
					htmltext = "31862-04.htm";
				else
					htmltext = "31862-00.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		if (st == null)
			return null;
		
		switch (npc.getNpcId())
		{
			case 13001:
				if (antharasAI() != null)
				{
					int status = GrandBossManager.getInstance().getBossStatus(29019);
					int statusW = GrandBossManager.getInstance().getBossStatus(29066);
					int statusN = GrandBossManager.getInstance().getBossStatus(29067);
					int statusS = GrandBossManager.getInstance().getBossStatus(29068);
					
					if (status == 2 || statusW == 2 || statusN == 2 || statusS == 2)
						htmltext = "13001-02.htm";
					else if (status == 3 || statusW == 3 || statusN == 3 || statusS == 3)
						htmltext = "13001-01.htm";
					else if (status == 0 || status == 1) // If entrance to see Antharas is unlocked (he is Dormant or Waiting)
					{
						if (st.hasQuestItems(3865))
						{
							st.takeItems(3865, 1);
							L2BossZone zone = GrandBossManager.getInstance().getZone(179700, 113800, -7709);
							if (zone != null)
								zone.allowPlayerEntry(player, 30);
							
							player.teleToLocation(179700 + Rnd.get(700), 113800 + Rnd.get(2100), -7709);
							
							if (status == 0)
							{
								L2GrandBossInstance antharas = GrandBossManager.getInstance().getBoss(29019);
								antharasAI().notifyEvent("waiting", antharas, player);
							}
						}
						else
							htmltext = "13001-03.htm";
					}
				}
				break;
			
			case 31859:
				player.teleToLocation(79800 + Rnd.get(600), 151200 + Rnd.get(1100), -3534);
				break;
			
			case 31385:
				if (valakasAI() != null)
				{
					final int status = GrandBossManager.getInstance().getBossStatus(VALAKAS);
					if (status == 0 || status == 1)
					{
						if (_valakasPlayersCount >= 200)
							htmltext = "31385-03.htm";
						else if (st.getInt("allowEnter") == 1)
						{
							st.unset("allowEnter");
							final L2BossZone zone = GrandBossManager.getInstance().getZone(212852, -114842, -1632);
							if (zone != null)
								zone.allowPlayerEntry(player, 30);
							
							player.teleToLocation(204328 + Rnd.get(600), -111874 + Rnd.get(600), 70);
							
							_valakasPlayersCount++;
							
							if (status == 0)
							{
								L2GrandBossInstance valakas = GrandBossManager.getInstance().getBoss(VALAKAS);
								valakasAI().startQuestTimer("beginning", Config.WAIT_TIME_VALAKAS, valakas, null);
								GrandBossManager.getInstance().setBossStatus(VALAKAS, 1);
							}
						}
						else
							htmltext = "31385-04.htm";
					}
					else if (status == 2)
						htmltext = "31385-02.htm";
					else
						htmltext = "31385-01.htm";
				}
				else
					htmltext = "31385-01.htm";
				break;
			
			case 31384:
				DoorTable.getInstance().getDoor(24210004).openMe();
				break;
			
			case 31686:
				DoorTable.getInstance().getDoor(24210006).openMe();
				break;
			
			case 31687:
				DoorTable.getInstance().getDoor(24210005).openMe();
				break;
			
			case 31540:
				if (_valakasPlayersCount < 50)
					htmltext = "31540-01.htm";
				else if (_valakasPlayersCount < 100)
					htmltext = "31540-02.htm";
				else if (_valakasPlayersCount < 150)
					htmltext = "31540-03.htm";
				else if (_valakasPlayersCount < 200)
					htmltext = "31540-04.htm";
				else
					htmltext = "31540-05.htm";
				break;
			
			case 31759:
				player.teleToLocation(150037 + Rnd.get(500), -57720 + Rnd.get(500), -2976);
				break;
		}
		
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new GrandBossTeleporters(-1, qn, "teleports");
	}
}