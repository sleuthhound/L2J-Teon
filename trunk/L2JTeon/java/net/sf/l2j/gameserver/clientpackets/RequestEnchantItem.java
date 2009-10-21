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
package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.item.Inventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.EnchantResult;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.ItemList;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.util.IllegalPlayerAction;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

public final class RequestEnchantItem extends L2GameClientPacket
{
	protected static final Logger _log = Logger.getLogger(Inventory.class.getName());
	private static final String _C__58_REQUESTENCHANTITEM = "[C] 58 RequestEnchantItem";
	private static final int[] ENCHANT_SCROLLS = { 729, 730, 947, 948, 951, 952, 955, 956, 959, 960 };
	private static final int[] CRYSTAL_SCROLLS = { 731, 732, 949, 950, 953, 954, 957, 958, 961, 962 };
	private static final int[] BLESSED_SCROLLS = { 6569, 6570, 6571, 6572, 6573, 6574, 6575, 6576, 6577, 6578 };
	private int _objectId;

	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_objectId == 0))
		{
			return;
		}
		if (activeChar.isProcessingTransaction())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			return;
		}
		L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		L2ItemInstance scroll = activeChar.getActiveEnchantItem();
		if (item == null || scroll == null)
		{
			activeChar.setActiveEnchantItem(null);
			return;
		}
		if ((item == null) || (scroll == null))
		{
			return;
		}
		// can't enchant rods, hero weapons and shadow items, donator rented
		// items
		if ((item.getItem().getItemType() == L2WeaponType.ROD) || (!Config.ENCHANT_HERO_WEAPONS && (item.getItemId() >= 6611) && (item.getItemId() <= 6621)) || ((item.getItemId() >= 7816) && (item.getItemId() <= 7831)) || item.isShadowItem() || item.isDonatorRented())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
			activeChar.setActiveEnchantItem(null);
			return;
		}
		if (item.isWear())
		{
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant a weared Item", IllegalPlayerAction.PUNISH_KICK);
			return;
		}
		switch (item.getLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
			{
				if (item.getOwnerId() != activeChar.getObjectId())
				{
					activeChar.setActiveEnchantItem(null);
					return;
				}
				break;
			}
			default:
			{
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to use enchant Exploit!", IllegalPlayerAction.PUNISH_KICKBAN);
				return;
			}
		}
		int itemType2 = item.getItem().getType2();
		boolean enchantItem = false;
		boolean blessedScroll = false;
		int crystalId = 0;
		/** pretty code ;D */
		switch (item.getItem().getCrystalType())
		{
			case L2Item.CRYSTAL_A:
				crystalId = 1461;
				switch (scroll.getItemId())
				{
					case 729:
					case 731:
					case 6569:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 730:
					case 732:
					case 6570:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_B:
				crystalId = 1460;
				switch (scroll.getItemId())
				{
					case 947:
					case 949:
					case 6571:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 948:
					case 950:
					case 6572:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_C:
				crystalId = 1459;
				switch (scroll.getItemId())
				{
					case 951:
					case 953:
					case 6573:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 952:
					case 954:
					case 6574:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_D:
				crystalId = 1458;
				switch (scroll.getItemId())
				{
					case 955:
					case 957:
					case 6575:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 956:
					case 958:
					case 6576:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
			case L2Item.CRYSTAL_S:
				crystalId = 1462;
				switch (scroll.getItemId())
				{
					case 959:
					case 961:
					case 6577:
						if (itemType2 == L2Item.TYPE2_WEAPON)
						{
							enchantItem = true;
						}
						break;
					case 960:
					case 962:
					case 6578:
						if ((itemType2 == L2Item.TYPE2_SHIELD_ARMOR) || (itemType2 == L2Item.TYPE2_ACCESSORY))
						{
							enchantItem = true;
						}
						break;
				}
				break;
		}
		if (!enchantItem)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
			return;
		}
		// Get the scroll type - Yesod
		if ((scroll.getItemId() >= 6569) && (scroll.getItemId() <= 6578))
		{
			blessedScroll = true;
		}
		else
		{
			for (int crystalscroll : CRYSTAL_SCROLLS)
			{
				if (scroll.getItemId() == crystalscroll)
				{
					blessedScroll = false;
					break;
				}
			}
		}
		scroll = activeChar.getInventory().destroyItem("Enchant", scroll, activeChar, item);
		if (scroll == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a scroll he doesnt have", Config.DEFAULT_PUNISH);
			return;
		}
		// SystemMessage sm = new
		// SystemMessage(SystemMessageId.ENCHANT_SCROLL_CANCELLED);
		// activeChar.sendPacket(sm);
		SystemMessage sm;
		int chance = 0;
		int maxEnchantLevel = 0;
		if (item.getItem().getType2() == L2Item.TYPE2_WEAPON)
		{
			maxEnchantLevel = Config.ENCHANT_MAX_WEAPON;
			for (int scrollId : ENCHANT_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_WEAPON;
					break;
				}
			}
			for (int scrollId : CRYSTAL_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_WEAPON_CRYSTAL;
					;
					break;
				}
			}
			for (int scrollId : BLESSED_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_WEAPON_BLESSED;
					break;
				}
			}
		}
		else if (item.getItem().getType2() == L2Item.TYPE2_SHIELD_ARMOR)
		{
			maxEnchantLevel = Config.ENCHANT_MAX_ARMOR;
			for (int scrollId : ENCHANT_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_ARMOR;
					break;
				}
			}
			for (int scrollId : CRYSTAL_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_ARMOR_CRYSTAL;
					break;
				}
			}
			for (int scrollId : BLESSED_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_ARMOR_BLESSED;
					break;
				}
			}
		}
		else if (item.getItem().getType2() == L2Item.TYPE2_ACCESSORY)
		{
			maxEnchantLevel = Config.ENCHANT_MAX_JEWELRY;
			for (int scrollId : ENCHANT_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_JEWELRY;
					break;
				}
			}
			for (int scrollId : CRYSTAL_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_JEWELRY_CRYSTAL;
					break;
				}
			}
			for (int scrollId : BLESSED_SCROLLS)
			{
				if (scroll.getItemId() == scrollId)
				{
					chance = Config.ENCHANT_CHANCE_JEWELRY_BLESSED;
					break;
				}
			}
		}
		if ((item.getEnchantLevel() < Config.ENCHANT_SAFE_MAX) || ((item.getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR) && (item.getEnchantLevel() < Config.ENCHANT_SAFE_MAX_FULL)))
		{
			chance = 100;
		}
		int rndValue = Rnd.get(100);
		if (Config.ENABLE_DWARF_ENCHANT_BONUS && (activeChar.getRace() == Race.dwarf))
		{
			if (activeChar.getLevel() >= Config.DWARF_ENCHANT_MIN_LEVEL)
				rndValue -= Config.DWARF_ENCHANT_BONUS;
		}
        if (Config.ENCHANT_MAX_WEAPON > 0)
        {
            if (item.getItem().getType2() == L2Item.TYPE2_WEAPON && item.getEnchantLevel() >= Config.ENCHANT_MAX_WEAPON) 
            {
                    activeChar.sendMessage("This is server limit for enchanting this item with scrolls.");
                    return;
            }
        }
         
        if (Config.ENCHANT_MAX_ARMOR > 0)
        {
            if (item.getItem().getType2() == L2Item.TYPE2_SHIELD_ARMOR && item.getEnchantLevel() >= Config.ENCHANT_MAX_ARMOR) 
            {
                    activeChar.sendMessage("This is server limit for enchanting this item with scrolls.");
                    return;
            }
        }
         
        if (Config.ENCHANT_MAX_JEWELRY > 0)
        {
            if (item.getItem().getType2() == L2Item.TYPE2_ACCESSORY && item.getEnchantLevel() >= Config.ENCHANT_MAX_JEWELRY)
            {
                    activeChar.sendMessage("This is server limit for enchanting this item with scrolls.");
                    return;
            }
        }
	        
		if (rndValue < chance)
		{
			synchronized (item)
			{
				if (item.getOwnerId() != activeChar.getObjectId() // has just lost the item)
						|| ((item.getEnchantLevel() >= maxEnchantLevel) && (maxEnchantLevel != 0)))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
					return;
				}
				if ((item.getLocation() != L2ItemInstance.ItemLocation.INVENTORY) && (item.getLocation() != L2ItemInstance.ItemLocation.PAPERDOLL))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
					return;
				}
				if ((item.getEnchantLevel() >= maxEnchantLevel) && (maxEnchantLevel != 0))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION));
					return;
				}
				if (item.getEnchantLevel() == 0)
				{
					sm = new SystemMessage(SystemMessageId.S1_SUCCESSFULLY_ENCHANTED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.S1_S2_SUCCESSFULLY_ENCHANTED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.updateDatabase();
			}
		}
		else
		{
			if (!blessedScroll)
			{
				if (item.getEnchantLevel() > 0)
				{
					sm = new SystemMessage(SystemMessageId.ENCHANTMENT_FAILED_S1_S2_EVAPORATED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.ENCHANTMENT_FAILED_S1_EVAPORATED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
			}
			else
			{
				sm = new SystemMessage(SystemMessageId.BLESSED_ENCHANT_FAILED);
				activeChar.sendPacket(sm);
			}
			if (!blessedScroll)
			{
				if (item.getEnchantLevel() > 0)
				{
					sm = new SystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				else
				{
					sm = new SystemMessage(SystemMessageId.S1_DISARMED);
					sm.addItemName(item.getItemId());
					activeChar.sendPacket(sm);
				}
				L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(item.getEquipSlot());
				if (item.isEquipped())
				{
					InventoryUpdate iu = new InventoryUpdate();
					for (int i = 0; i < unequiped.length; i++)
					{
						iu.addModifiedItem(unequiped[i]);
					}
					activeChar.sendPacket(iu);
					activeChar.broadcastUserInfo();
				}
				int count = item.getCrystalCount() - (item.getItem().getCrystalCount() + 1) / 2;
				if (count < 1)
				{
					count = 1;
				}
				L2ItemInstance destroyItem = activeChar.getInventory().destroyItem("Enchant", item, activeChar, null);
				if (destroyItem == null)
				{
					return;
				}
				L2ItemInstance crystals = activeChar.getInventory().addItem("Enchant", crystalId, count, activeChar, destroyItem);
				sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
				sm.addItemName(crystals.getItemId());
				sm.addNumber(count);
				activeChar.sendPacket(sm);
				if (!Config.FORCE_INVENTORY_UPDATE)
				{
					InventoryUpdate iu = new InventoryUpdate();
					if (destroyItem.getCount() == 0)
					{
						iu.addRemovedItem(destroyItem);
					}
					else
					{
						iu.addModifiedItem(destroyItem);
					}
					iu.addItem(crystals);
					activeChar.sendPacket(iu);
				}
				else
				{
					activeChar.sendPacket(new ItemList(activeChar, true));
				}
				StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
				su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
				activeChar.sendPacket(su);
				activeChar.broadcastUserInfo();
				L2World world = L2World.getInstance();
				world.removeObject(destroyItem);
			}
			else
			{
				item.setEnchantLevel(0);
				item.updateDatabase();
			}
		}
		sm = null;
		StatusUpdate su = new StatusUpdate(activeChar.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		activeChar.sendPacket(su);
		su = null;
		activeChar.sendPacket(new EnchantResult(item.getEnchantLevel())); // FIXME
		// i'm
		// really
		// not
		// sure
		// about
		// this...
		activeChar.sendPacket(new ItemList(activeChar, false)); // TODO update
		// only the
		// enchanted
		// item
		activeChar.broadcastUserInfo();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__58_REQUESTENCHANTITEM;
	}
}
