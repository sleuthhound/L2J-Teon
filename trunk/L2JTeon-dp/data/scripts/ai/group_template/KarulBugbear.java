package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class KarulBugbear extends L2AttackableAIScript
{
	private static final int KARUL = 20600;

	private static boolean _FirstAttacked;

	public KarulBugbear(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {KARUL};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == KARUL)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 4)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Your rear is practically unguarded!"));
            }
            else
            {
               _FirstAttacked = true;
               if (Rnd.get(100) == 4)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Watch your back!"));
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == KARUL)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new KarulBugbear(-1, "KarulBugbear", "ai");
	}
}