/*
 * This program is free software: you can redistribute it and/or modify it under
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
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.clientpackets.Say2;
import net.sf.l2j.gameserver.datatables.ExtractableItemsData;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ExtractableItem;
import net.sf.l2j.gameserver.model.L2ExtractableProductItem;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.item.PcInventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;

/**
 * @author FBIagent 11/12/2006
 */
public class ExtractableItems implements IItemHandler
{
	public static Vector<DelayedOpen> vectDelayedOpen = new Vector<DelayedOpen>();
	@SuppressWarnings("unchecked")
	public Future _actionTask;

	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		if (!(playable instanceof L2PcInstance))
			return;
		L2PcInstance activeChar = (L2PcInstance) playable;
		L2ExtractableItem exitem = ExtractableItemsData.getInstance().getExtractableItem(item.getItemId());
		if (exitem == null)
			return;
		if (isPlayerNotInVector(activeChar))
		{
			activeChar.sitDown();
			DelayedOpen task = new DelayedOpen(activeChar, item);
			vectDelayedOpen.add(task);
			_actionTask = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(task, 0, 1000);
		}
		else
			activeChar.sendMessage("A spot of extraction is already in hand for this character");
	}

	public int[] getItemIds()
	{
		return ExtractableItemsData.getInstance().itemIDs();
	}

	private boolean isPlayerNotInVector(L2PcInstance player)
	{
		Iterator<DelayedOpen> itDelayedOpen = vectDelayedOpen.iterator();
		while (itDelayedOpen.hasNext())
		{
			DelayedOpen task = itDelayedOpen.next();
			if (player == task.getActiveChar())
			{
				return false;
			}
		}
		return true;
	}

	private class DelayedOpen implements Runnable
	{
		private L2PcInstance activeChar;
		private L2ItemInstance item;
		private int max;
		private int current = 0;

		public DelayedOpen(L2PcInstance p, L2ItemInstance i)
		{
			activeChar = p;
			item = i;
			max = activeChar.getInventory().getInventoryItemCount(item.getItemId(), -1);
		}

		public L2PcInstance getActiveChar()
		{
			return activeChar;
		}

		public void run()
		{
			current++;
			if (current > max)
			{
				_actionTask.cancel(true);
				_actionTask = null;
				ExtractableItems.vectDelayedOpen.remove(this);
				return;
			}
			int itemID = item.getItemId();
			L2ExtractableItem exitem = ExtractableItemsData.getInstance().getExtractableItem(itemID);
			int createItemID = 0, createAmount = 0, rndNum = Rnd.get(100), chanceFrom = 0;
			// calculate extraction
			for (L2ExtractableProductItem expi : exitem.getProductItemsArray())
			{
				int chance = expi.getChance();
				if ((rndNum >= chanceFrom) && (rndNum <= chance + chanceFrom))
				{
					createItemID = expi.getId();
					createAmount = expi.getAmmount();
					break;
				}
				chanceFrom += chance;
			}
			if (createItemID == 0)
			{
				activeChar.sendMessage("Nothing happend.");
				return;
			}
			PcInventory inv = activeChar.getInventory();
			if (createItemID > 0)
			{
				if (ItemTable.getInstance().createDummyItem(createItemID).isStackable())
					inv.addItem("Extract", createItemID, createAmount, activeChar, null);
				else
				{
					for (int i = 0; i < createAmount; i++)
						inv.addItem("Extract", createItemID, 1, activeChar, item);
				}
				SystemMessage sm;
				if (createAmount > 1)
				{
					sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
					sm.addItemName(createItemID);
					sm.addNumber(createAmount);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.EARNED_ITEM);
					sm.addItemName(createItemID);
				}
				activeChar.sendPacket(sm);
			}
			else
			{
				activeChar.sendMessage("Item failed to open"); // TODO: Put a
				// more proper
				// message here.
			}
			activeChar.destroyItemByItemId("Extract", itemID, 1, activeChar.getTarget(), true);
			activeChar.sendPacket(new ItemList(activeChar, false));
			StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
			su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
			activeChar.sendPacket(su);
			String _text = current + "/" + max;
			CreatureSay cs = new CreatureSay(activeChar.getObjectId(), Say2.TELL, activeChar.getName(), _text);
			activeChar.sendPacket(cs);
		}
	}
}
