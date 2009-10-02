package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class CatsEyeBandit extends L2AttackableAIScript
{
	private static final int BANDIT = 27038;

	private static boolean _FirstAttacked;

	public CatsEyeBandit(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {BANDIT};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == BANDIT)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 40)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"You childish fool, do you think you can catch me?"));
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
        if (npcId == BANDIT)
        {
		int objId = npc.getObjectId();
               if (Rnd.get(100) == 80)
            npc.broadcastPacket(new NpcSay(objId,0,npcId,"I must do something about this shameful incident..."));
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new CatsEyeBandit(-1, "CatsEyeBandit", "ai");
	}
}