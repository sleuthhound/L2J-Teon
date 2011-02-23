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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * by schursin SDH {H DDDHH [DHHH] D [DD]}
 */
public class GMViewWarehouseWithdrawList extends L2GameServerPacket
{
	private static final String _S__95_GMViewWarehouseWithdrawList = "[S] 95 GMViewWarehouseWithdrawList";
	private L2ItemInstance[] _items;
	private String _playerName;
	private L2PcInstance _activeChar;
	private int _money;

	public GMViewWarehouseWithdrawList(L2PcInstance cha)
	{
		_activeChar  = cha;
		_items = _activeChar.getWarehouse().getItems();
		_playerName = _activeChar.getName();
		_money = _activeChar.getWarehouse().getAdena();
        }

    public GMViewWarehouseWithdrawList(L2Clan clan)
    {
        _playerName = clan.getLeaderName();
        _items = clan.getWarehouse().getItems();
        _money = clan.getWarehouse().getAdena();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x95);
		writeS(_playerName);
		writeD(_money);
		writeH(_items.length);
		for (L2ItemInstance item : _items)
		{
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getCount());
			writeH(item.getItem().getType2());
			writeH(item.getCustomType1());
			switch (item.getItem().getType2())
			{
				case L2Item.TYPE2_WEAPON:
				{
					writeD(item.getItem().getBodyPart());
					writeH(item.getEnchantLevel());
					writeH(((L2Weapon) item.getItem()).getSoulShotCount());
					writeH(((L2Weapon) item.getItem()).getSpiritShotCount());
					break;
				}
				case L2Item.TYPE2_SHIELD_ARMOR:
				case L2Item.TYPE2_ACCESSORY:
				case L2Item.TYPE2_PET_WOLF:
				case L2Item.TYPE2_PET_HATCHLING:
				case L2Item.TYPE2_PET_STRIDER:
				case L2Item.TYPE2_PET_BABY:
				{
					writeD(item.getItem().getBodyPart());
					writeH(item.getEnchantLevel());
					writeH(0x00);
					writeH(0x00);
					break;
				}
			}
			writeD(item.getObjectId());
			switch (item.getItem().getType2())
			{
				case L2Item.TYPE2_WEAPON:
				{
					if (item.isAugmented())
					{
						writeD(0x0000FFFF & item.getAugmentation().getAugmentationId());
						writeD(item.getAugmentation().getAugmentationId() >> 16);
					}
					else
					{
						writeD(0);
						writeD(0);
					}
					break;
				}
				case L2Item.TYPE2_SHIELD_ARMOR:
				case L2Item.TYPE2_ACCESSORY:
				case L2Item.TYPE2_PET_WOLF:
				case L2Item.TYPE2_PET_HATCHLING:
				case L2Item.TYPE2_PET_STRIDER:
				case L2Item.TYPE2_PET_BABY:
				{
					writeD(0);
					writeD(0);
				}
			}
		}
	}

	@Override
	public String getType()
	{
		return _S__95_GMViewWarehouseWithdrawList;
	}
}