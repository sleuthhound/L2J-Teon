/* This program is free software: you can redistribute it and/or modify it under
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
package ai.individual;

import ai.group_template.L2AttackableAIScript;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.gameserver.serverpackets.SocialAction;
import net.sf.l2j.gameserver.serverpackets.SpecialCamera;
import net.sf.l2j.util.L2FastList;
import net.sf.l2j.util.Rnd;

public class Benom extends L2AttackableAIScript
{
    private L2NpcInstance benom;
    private static final int Benom = 29054;
    private static final int BenomTeleport = 35506;
    private static final String[] BenomSpeak = {"You should have finished me when you had the chance!!!", "I will crush all of you!!!", "I am not finished here, come face me!!!", "You cowards!!! I will torture each and everyone of you!!!"};
    private static final int[] WalkInterval = {18000, 17000, 4500, 16000, 22000, 14000, 10500, 14000, 9500, 12500, 20500, 14500, 17000, 20000, 22000, 11000, 11000, 20000, 8000, 5500, 20000, 18000, 25000, 28000, 25000, 25000, 25000, 25000, 10000, 24000, 7000, 12000, 20000};
    private static final byte ALIVE = 0;
    private static final byte DEAD = 1;
    private static byte BenomIsSpawned = 0;
    private static int BenomWalkRouteStep = 0;
    private static final int[][] benomWalkRoutes = {{12565, -49739, -547}, {11242, -49689, -33}, {10751, -49702, 83}, {10824, -50808, 316}, {9084, -50786, 972}, {9095, -49787, 1252}, {8371, -49711, 1252}, {8423, -48545, 1252}, {9105, -48474, 1252}, {9085, -47488, 972}, {10858, -47527, 316}, {10842, -48626, 75}, {12171, -48464, -547}, {13565, -49145, -535}, {15653, -49159, -1059}, {15423, -48402, -839}, {15066, -47438, -419}, {13990, -46843, -292}, {13685, -47371, -163}, {13384, -47470, -163}, {14609, -48608, 346}, {13878, -47449, 747}, {12894, -49109, 980}, {10135, -49150, 996}, {12894, -49109, 980}, {13738, -50894, 747}, {14579, -49698, 347}, {12896, -51135, -166}, {12971, -52046, -292,}, {15140, -50781, -442,}, {15328, -50406, -603}, {15594, -49192, -1059}, {13175, -49153, -537}};

    public Benom(int questId, String name, String descr)
    {
        super(questId, name, descr);
        addStartNpc(BenomTeleport);
        addTalkId(BenomTeleport);
        addAggroRangeEnterId(Benom);
        addKillId(Benom);
        int castleOwner = CastleManager.getInstance().getCastleById(8).getOwnerId();
        long siegeDate = CastleManager.getInstance().getCastleById(8).getSiegeDate().getTimeInMillis();
        long benomTeleporterSpawn = (siegeDate - System.currentTimeMillis()) - 86400000;
        long benomRaidRoomSpawn = (siegeDate - System.currentTimeMillis()) - 86400000;
        long benomRaidSiegeSpawn = (siegeDate - System.currentTimeMillis());
        if (benomTeleporterSpawn < 0)
            benomTeleporterSpawn = 1;
        if (benomRaidSiegeSpawn < 0)
            benomRaidSiegeSpawn = 1;
        if (castleOwner > 0)
            if (benomTeleporterSpawn >= 1)
                startQuestTimer("BenomTeleSpawn", benomTeleporterSpawn, null, null);
        if ((siegeDate - System.currentTimeMillis()) > 0)
            startQuestTimer("BenomRaidRoomSpawn", benomRaidRoomSpawn, null, null);
        startQuestTimer("BenomRaidSiegeSpawn", benomRaidSiegeSpawn, null, null);
    }

    public String onTalk (L2NpcInstance npc, L2PcInstance player)
    {
        String htmltext = "";
        int castleOwner = CastleManager.getInstance().getCastleById(8).getOwnerId();
        int clanId = player.getClanId();
        if (castleOwner != 0 && clanId != 0)
        {
            if (castleOwner == clanId)
            {
                int X = 12558 + (Rnd.get(200) - 100);
                int Y = -49279 + (Rnd.get(200) - 100);
                player.teleToLocation(X, Y, -3007);
                return htmltext;
            }
            else
                htmltext = "<html><body>Benom's Avatar:<br>Your clan does not own this castle. Only members of this Castle's owning clan can challenge Benom.</body></html>";
        }
        else
            htmltext = "<html><body>Benom's Avatar:<br>Your clan does not own this castle. Only members of this Castle's owning clan can challenge Benom.</body></html>";
        return htmltext;
    }

    public String onAdvEvent (String event, L2NpcInstance npc, L2PcInstance player)
    {
        if (event.equals("BenomTeleSpawn"))
            addSpawn(BenomTeleport, 11013, -49629, -547, 13400, false, 0);
        else if (event.equals("BenomRaidRoomSpawn"))
        {
            if (BenomIsSpawned == 0 && GrandBossManager.getInstance().getBossStatus(Benom) == 0)
                benom = addSpawn(Benom, 12047, -49211, -3009, 0, false, 0);
            BenomIsSpawned = 1;
        }
        else if (event.equals("BenomRaidSiegeSpawn"))
        {
            if (GrandBossManager.getInstance().getBossStatus(Benom) == 0)
            {
                if (BenomIsSpawned == 0)
                {
                    benom = addSpawn(Benom, 11025, -49152, -537, 0, false, 0);
                    BenomIsSpawned = 1;
                }
                else if (BenomIsSpawned == 1)
                    benom.teleToLocation(11025, -49152, -537);
                startQuestTimer("BenomSpawnEffect", 100, npc, null);
                startQuestTimer("BenomBossDespawn", 5400000, npc, null);
                cancelQuestTimer("BenomSpawn", npc, null);
                unspawnNpc(BenomTeleport);
            }
        }
        else if (event.equals("BenomSpawnEffect"))
        {
            npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 200, 0, 150, 0, 5000));
            npc.broadcastPacket(new SocialAction(npc.getObjectId(), 3));
            startQuestTimer("BenomWalk", 5000, npc, null);
            BenomWalkRouteStep = 0;
        }
        else if (event.equals("Attacking"))
        {
            L2FastList<L2PcInstance> NumPlayers = new L2FastList<L2PcInstance>();
            for (L2PcInstance plr : npc.getKnownList().getKnownPlayers().values())
                NumPlayers.add(plr);
            if (NumPlayers.size() > 0)
            {
                L2PcInstance target = NumPlayers.get(Rnd.get(NumPlayers.size()));
                ((L2Attackable) npc).addDamageHate(target, 0, 999);
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
                startQuestTimer("Attacking", 2000, npc, player);
            }
            else if (NumPlayers.size() == 0)
                startQuestTimer("BenomWalkFinish", 2000, npc, null);
        }
        else if (event.equals("BenomWalkFinish"))
        {
            if (npc.getCastle().getSiege().getIsInProgress())
                cancelQuestTimer("Attacking", npc, player);
            int X = benomWalkRoutes[BenomWalkRouteStep][0];
            int Y = benomWalkRoutes[BenomWalkRouteStep][1];
            int Z = benomWalkRoutes[BenomWalkRouteStep][2];
            npc.teleToLocation(X, Y, Z);
            npc.setWalking();
            BenomWalkRouteStep = 0;
            startQuestTimer("BenomWalk", 2200, npc, null);
        }
        else if (event.equals("BenomWalk"))
        {
            if (BenomWalkRouteStep == 33)
            {
                BenomWalkRouteStep = 0;
                startQuestTimer("BenomWalk", 100, npc, null);
            }
            else
            {
                startQuestTimer("Talk", 100, npc, null);
                if (BenomWalkRouteStep == 14)
                {
                    startQuestTimer("DoorOpen", 15000, null, null);
                    startQuestTimer("DoorClose", 23000, null, null);
                }
                if (BenomWalkRouteStep == 32)
                {
                    startQuestTimer("DoorOpen", 500, null, null);
                    startQuestTimer("DoorClose", 4000, null, null);
                }
                int Time = WalkInterval[BenomWalkRouteStep];
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                int X = benomWalkRoutes[BenomWalkRouteStep][0];
                int Y = benomWalkRoutes[BenomWalkRouteStep][1];
                int Z = benomWalkRoutes[BenomWalkRouteStep][2];
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(X, Y, Z, 0));
                BenomWalkRouteStep = BenomWalkRouteStep + 1;
                startQuestTimer("BenomWalk", Time, npc, null);
            }
        }
        else if (event.equals("DoorOpen"))
            DoorTable.getInstance().getDoor(20160005).openMe();
        else if (event.equals("DoorClose"))
            DoorTable.getInstance().getDoor(20160005).closeMe();
        else if (event.equals("Talk"))
        {
            if (Rnd.get(100) < 40)
                npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), BenomSpeak[Rnd.get(4)]));
        }
        else if (event.equals("BenomBossDespawn"))
        {
            GrandBossManager.getInstance().setBossStatus(Benom, ALIVE);
            BenomIsSpawned = 0;
            unspawnNpc(Benom);
        }
        return super.onAdvEvent(event, npc, player);
    }

    public String onAggroRangeEnter (L2NpcInstance npc, L2PcInstance player, boolean isPet)
    {
        cancelQuestTimer("BenomWalk", npc, null);
        cancelQuestTimer("BenomWalkFinish", npc, null);
        startQuestTimer("Attacking", 100, npc, player);
        return super.onAggroRangeEnter(npc, player, isPet);
    }

    public String onKill (L2NpcInstance npc, L2PcInstance player, Boolean isPet)
    {
        GrandBossManager.getInstance().setBossStatus(Benom, DEAD);
        cancelQuestTimer("BenomWalk", npc, null);
        cancelQuestTimer("BenomWalkFinish", npc, null);
        cancelQuestTimer("BenomBossDespawn", npc, null);
        cancelQuestTimer("Talk", npc, null);
        cancelQuestTimer("Attacking", npc, null);
        return super.onKill(npc, player, isPet);
    }

    private void unspawnNpc(int npcId)
    {
        for (L2Spawn spawn : SpawnTable.getInstance().getSpawnTable().values())
        {
            if (spawn.getId() == npcId)
            {
                SpawnTable.getInstance().deleteSpawn(spawn, false);
                L2NpcInstance npc = spawn.getLastSpawn();
                npc.deleteMe();
            }
        }
    }

    public static void main(String[] args)
    {
        new Benom(-1, "Benom", "ai");
    }
}