package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class TimakOrcSupplier extends L2AttackableAIScript
{
	private static final int TIMAKS = 20498;

	private static boolean _FirstAttacked;

	public TimakOrcSupplier(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {TIMAKS};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == TIMAKS)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 40)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"You wont take me down easily."));
            }
            else
            {
               _FirstAttacked = true;
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"We shall see about that!"));
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == TIMAKS)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new TimakOrcSupplier(-1, "TimakOrcSupplier", "ai");
	}
}