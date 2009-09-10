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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.SocialAction;

/**
 * @author DaRkRaGe [L2JOneo]
 * 
 */
public class HeroCustomItem implements IItemHandler
{
    private static final int[] ITEM_IDS = { 3481 };

    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
	if (Config.HERO_CUSTOM_ITEMS)
	{
	    if (!(playable instanceof L2PcInstance))
	    {
		return;
	    }
	    L2PcInstance activeChar = (L2PcInstance) playable;
	    if (activeChar.isHero())
	    {
		activeChar.sendMessage("U Allready have Hero Status.");
		activeChar.sendPacket(new ActionFailed());
	    } else
	    {
		activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
		activeChar.setHero(true);
		activeChar.sendMessage("You are now a hero, you will remain a hero until you log off your character.");
		activeChar.broadcastUserInfo();
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
	    }
	}
    }

    public int[] getItemIds()
    {
	return ITEM_IDS;
    }
}