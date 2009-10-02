package ai.group_template;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.NpcSay;
import net.sf.l2j.util.Rnd;

/**
 * @author Maxi
 */
public class DeluLizardmanSpecialAgent extends L2AttackableAIScript
{
	private static final int LIZARDMAN = 21105;

	private static boolean _FirstAttacked;

	public DeluLizardmanSpecialAgent(int questId, String name, String descr)
	{
		super(questId, name, descr);
		int[] mobs = {LIZARDMAN};
		registerMobs(mobs);
		_FirstAttacked = false;
	}

	public String onAttack (L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
        if (npc.getNpcId() == LIZARDMAN)
        {
            if (_FirstAttacked)
            {
               if (Rnd.get(100) == 40)
            	   npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Hey! Were having a duel here!"));
            }
            else
            {
               _FirstAttacked = true;
           npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"How dare you interrupt our fight! Hey guys, help!"));
		}
        }
        return super.onAttack(npc, attacker, damage, isPet);
    }

	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        int npcId = npc.getNpcId();
        if (npcId == LIZARDMAN)
        {
            _FirstAttacked = false;
        }
        return super.onKill(npc,killer,isPet);
    }

	public static void main(String[] args)
	{
		new DeluLizardmanSpecialAgent(-1, "DeluLizardmanSpecialAgent", "ai");
	}
}