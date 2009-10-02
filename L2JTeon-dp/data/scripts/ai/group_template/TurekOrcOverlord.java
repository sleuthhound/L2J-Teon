package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class TurekOrcOverlord extends L2AttackableAIScript
{
	private static final int TUREKO = 20588;

	private static boolean _FirstAttacked;

	public TurekOrcOverlord(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {TUREKO};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == TUREKO)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 50)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Dear ultimate power!!!"));
            }
            else
            {
               _FirstAttacked = true;
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == TUREKO)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new TurekOrcOverlord(-1, "TurekOrcOverlord", "ai");
	}
}