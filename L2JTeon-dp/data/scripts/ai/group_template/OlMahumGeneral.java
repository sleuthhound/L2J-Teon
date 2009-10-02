package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class OlMahumGeneral extends L2AttackableAIScript
{
	private static final int MAHUM = 20438;

	private static boolean _FirstAttacked;

	public OlMahumGeneral(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {MAHUM};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == MAHUM)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 100)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"We shall see about that!"));
            }
            else
            {
               _FirstAttacked = true;
           npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"I will definitely repay this humiliation!"));
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == MAHUM)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new OlMahumGeneral(-1, "OlMahumGeneral", "ai");
	}
}