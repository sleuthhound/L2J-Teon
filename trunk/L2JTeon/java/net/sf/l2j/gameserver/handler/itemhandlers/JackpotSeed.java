package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author DaRkRaGe & schursin ;)
 * 
 */
public class JackpotSeed implements IItemHandler
{
    public class DeSpawnScheduleTimerTask implements Runnable
    {
	L2Spawn spawnedPlant = null;

	public DeSpawnScheduleTimerTask(L2Spawn spawn)
	{
	    spawnedPlant = spawn;
	}

	public void run()
	{
	    try
	    {
		spawnedPlant.getLastSpawn().decayMe();
	    } catch (Throwable t)
	    {
	    }
	}
    }

    private static int[] _itemIds = { 6389, // small seed
    6390
    // large seed
    };
    private static int[] _npcIds = { 12774, // Young Pumpkin
    12777
    // Large Young Pumpkin
    };
    private static int[] _npcLifeTime = { 30000, // Young Pumpkin
    40000
    // Large Young Pumpkin
    };

    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
	L2PcInstance activeChar = (L2PcInstance) playable;
	L2NpcTemplate template1 = null;
	int lifeTime = 0;
	int itemId = item.getItemId();
	for (int i = 0; i < _itemIds.length; i++)
	{
	    if (_itemIds[i] == itemId)
	    {
		template1 = NpcTable.getInstance().getTemplate(_npcIds[i]);
		lifeTime = _npcLifeTime[i];
		break;
	    }
	}
	if (template1 == null)
	    return;
	try
	{
	    L2Spawn spawn = new L2Spawn(template1);
	    spawn.setId(IdFactory.getInstance().getNextId());
	    spawn.setLocx(activeChar.getX());
	    spawn.setLocy(activeChar.getY());
	    spawn.setLocz(activeChar.getZ());
	    L2World.getInstance().storeObject(spawn.spawnOne());
	    ThreadPoolManager.getInstance().scheduleGeneral(new DeSpawnScheduleTimerTask(spawn), lifeTime);
	    activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
	} catch (Exception e)
	{
	    SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
	    sm.addString("Exception in useItem() of JackpotSeed.java");
	    activeChar.sendPacket(sm);
	}
    }

    public int[] getItemIds()
    {
	return _itemIds;
    }
}
