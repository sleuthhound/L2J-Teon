package ai.individual;

import ai.group_template.L2AttackableAIScript;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.gameserver.serverpackets.SpecialCamera;

public class Sailren extends L2AttackableAIScript
{
	private final int STATUE = 32109;
	private final int STONE = 8784;
	private final int SAILREN = 29065;
	private final int VELO = 22196;
	private final int PTERO = 22199;
	private final int TREX = 22215;

	public Sailren(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(STATUE);
		addTalkId(STATUE);
		addKillId(VELO);
		addKillId(PTERO);
		addKillId(TREX);
		addKillId(SAILREN);
	}

	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (event.equals("start"))
		{
			startQuestTimer("camera", 2000, addSpawn(VELO, 27845, -5567, -1982, 45000, false, 0), player);
			cancelQuestTimer("start", npc, null);
		}
		else if (event.equals("round2"))
		{
			startQuestTimer("camera", 2000, addSpawn(PTERO, 27838, -5578, -1982, 45000, false, 0), player);
			cancelQuestTimer("round2", npc, null);
		}
		else if (event.equals("round3"))
		{
			startQuestTimer("camera", 2000, addSpawn(TREX, 27838, -5578, -1982, 45000, false, 0), player);
			cancelQuestTimer("round3", npc, null);
		}
		else if (event.equals("sailren"))
		{
			startQuestTimer("camera", 2000, addSpawn(SAILREN, 27489, -6223, -1982, 45000, false, 0), player);
			startQuestTimer("vkrovatku", 1200000, addSpawn(SAILREN, 27489, -6223, -1982, 45000, false, 0), null);
			cancelQuestTimer("round4", npc, null);
		}
		else if (event.equals("camera"))
		{
			player.broadcastPacket(new SpecialCamera(npc.getObjectId(), 400, -75, 3, -150, 5000));
			npc.broadcastPacket(new SocialAction(npc.getObjectId(), 1));
		}
		else if (event.equals("open"))
		{
			deleteGlobalQuestVar("close");
			cancelQuestTimer("open", npc, null);
		}
		else if (event.equals("vkrovatku"))
		{
			npc.deleteMe();
			deleteGlobalQuestVar("close");
			cancelQuestTimer("open", npc, null);
			cancelQuestTimer("vkrovatku", npc, null);
		}
		return "";
	}

	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState("sailren");
		int npcId = npc.getNpcId();
		String close = loadGlobalQuestVar("close");
		if (npcId == STATUE)
		{
			if (st.getQuestItemsCount(STONE) >= 1)
			{
				if (close == "")
				{
					if (player.isInParty())
					{
						st.takeItems(STONE, 1);
						saveGlobalQuestVar("close", "1");
						player.teleToLocation(27244, -7026, -1974);
						startQuestTimer("start", 30000, npc, player);
						startQuestTimer("open", 1800000, npc, null);
					}
					else
						return "<html><body><font color=LEVEL>Tol'ko Party...</font></body></html>";
				}
				else
					return "<html><body><font color=LEVEL>Kto-to uze vowel, poprobuite pozhe...</font></body></html>";
			}
			else
				return "<html><body>U party dolzen bit' <font color=LEVEL>Gazkh...</font></body></html>";
		}
		return "";
	}

	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == VELO)
			startQuestTimer("round2", 30000, npc, player);
		if (npcId == PTERO)
			startQuestTimer("round3", 60000, npc, player);
		if (npcId == TREX)
			startQuestTimer("sailren", 180000, npc, player);
		if (npcId == SAILREN)
			deleteGlobalQuestVar("close");
		cancelQuestTimer("open", npc, null);
		return "";
	}

	public static void main(String[] args)
	{
		new Sailren(-1, "sailren", "ai");
	}
}
