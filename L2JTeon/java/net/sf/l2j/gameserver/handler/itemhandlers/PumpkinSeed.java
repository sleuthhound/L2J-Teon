package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

public class PumpkinSeed implements IItemHandler
{
    public class DeSpawnScheduleTimerTask implements Runnable
    {

        public void run()
        {
            try
            {
                spawnedPlant.getLastSpawn().decayMe();
            }
            catch(Throwable throwable) { }
        }

        L2Spawn spawnedPlant;

        public DeSpawnScheduleTimerTask(L2Spawn l2spawn)
        {
            spawnedPlant = null;
            spawnedPlant = l2spawn;
        }
    }

    public void useItem(L2PlayableInstance l2playableinstance, L2ItemInstance l2iteminstance)
    {
        L2PcInstance l2pcinstance = (L2PcInstance)l2playableinstance;
        net.sf.l2j.gameserver.templates.L2NpcTemplate l2npctemplate = null;
        int i = 0;
        int j = l2iteminstance.getItemId();
        if(j == 6391)
        {
            net.sf.l2j.gameserver.model.L2Skill l2skill = SkillTable.getInstance().getInfo(2005, 1);
            l2pcinstance.useMagic(l2skill, false, false);
        } else
        {
            int k = 0;
            do
            {
                if(k >= _itemIds.length)
                    break;
                if(_itemIds[k] == j)
                {
                    l2npctemplate = NpcTable.getInstance().getTemplate(_npcIds[k]);
                    i = _npcLifeTime[k];
                    break;
                }
                k++;
            } while(true);
            if(l2npctemplate == null)
                return;
            try
            {
                L2Spawn l2spawn = new L2Spawn(l2npctemplate);
                l2spawn.setId(IdFactory.getInstance().getNextId());
                l2spawn.setLocx(l2pcinstance.getX());
                l2spawn.setLocy(l2pcinstance.getY());
                l2spawn.setLocz(l2pcinstance.getZ());
                //L2World.getInstance().storeObject(l2spawn.spawnOne(true));
                ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnScheduleTimerTask(l2spawn), i);
                l2pcinstance.destroyItem("Consume", l2iteminstance.getObjectId(), 1, null, false);
            }
            catch(Exception exception)
            {
                SystemMessage systemmessage = new SystemMessage(SystemMessageId.S1_S2);
				systemmessage.addString("Exception in useItem() of PumpkinSeed.java");
                l2pcinstance.sendPacket(systemmessage);
            }
        }
    }

    public int[] getItemIds()
    {
        return _itemIds;
    }

    private static int _itemIds[] = {
        6389, 6390, 6391
    };
    private static int _npcIds[] = {
        12774, 12777
    };
    private static int _npcLifeTime[] = {
        30000, 40000
    };

}
