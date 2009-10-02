package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class TimakOrcTroopLeader extends L2AttackableAIScript
{
	private static final int TIMAK = 20767;

	private static boolean _FirstAttacked;

	public TimakOrcTroopLeader(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {TIMAK};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == TIMAK)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 50)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Destroy the enemy, my brothers!"));
            }
            else
            {
               _FirstAttacked = true;
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill (L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == TIMAK)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new TimakOrcTroopLeader(-1, "TimakOrcTroopLeader", "ai");
	}
}