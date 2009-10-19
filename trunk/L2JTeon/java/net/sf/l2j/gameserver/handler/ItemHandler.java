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
package net.sf.l2j.gameserver.handler;

import java.util.Map;
import java.util.TreeMap;

public class ItemHandler
{
	private Map<Integer, IItemHandler> _datatable;

	public static ItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}

	public int size()
	{
		return _datatable.size();
	}

	private ItemHandler()
	{
		_datatable = new TreeMap<Integer, IItemHandler>();
	}

	public void registerItemHandler(IItemHandler handler)
	{
		// Get all ID corresponding to the item type of the handler
		int[] ids = handler.getItemIds();
		// Add handler for each ID found
		for (int i = 0; i < ids.length; i++)
		{
			_datatable.put(new Integer(ids[i]), handler);
		}
	}

	public IItemHandler getItemHandler(int itemId)
	{
		return _datatable.get(new Integer(itemId));
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ItemHandler _instance = new ItemHandler();
	}
}
