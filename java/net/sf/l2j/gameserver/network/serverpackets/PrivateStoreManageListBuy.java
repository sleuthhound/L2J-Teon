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

import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:40 $
 */
public class PrivateStoreManageListBuy extends L2GameServerPacket
{
	private static final String _S__D0_PRIVATESELLLISTBUY = "[S] b7 PrivateSellListBuy";
	private L2PcInstance _activeChar;
	private int _playerAdena;
	private L2ItemInstance[] _itemList;
	private TradeList.TradeItem[] _buyList;

	public PrivateStoreManageListBuy(L2PcInstance player)
	{
		_activeChar = player;
		_playerAdena = _activeChar.getAdena();
		_itemList = _activeChar.getInventory().getUniqueItems(false, true, false);
		_buyList = _activeChar.getBuyList().getItems();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xb7);
		// section 1
		writeD(_activeChar.getObjectId());
		writeD(_playerAdena);
		// section2
		writeD(_itemList.length); // inventory items for potential buy
		for (L2ItemInstance item : _itemList)
		{
			writeD(item.getItemId());
			writeH(0); // show enchant lvl as 0, as you can't buy enchanted
			// weapons
			writeD(item.getCount());
			writeD(item.getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
		}
		// section 3
		writeD(_buyList.length); // count for all items already added for
		// buy
		for (TradeList.TradeItem item : _buyList)
		{
			writeD(item.getItem().getItemId());
			writeH(0);
			writeD(item.getCount());
			writeD(item.getItem().getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
			writeD(item.getPrice());// your price
			writeD(item.getItem().getReferencePrice());// fixed store price
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__D0_PRIVATESELLLISTBUY;
	}
}
