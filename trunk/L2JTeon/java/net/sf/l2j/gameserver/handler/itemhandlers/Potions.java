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

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.CTF;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.TvTEvent;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.VIP;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * Potions.java
 * 
 * Reworked by schursin From now: Pets can drink potions!
 * 
 * PLEASE DONT DO "CLEANUP" OR ETC. OF THIS FILE! LET IT BE "AS IS". IT'S
 * FORMATED FOR FUTURE CHANGES! (schursin)
 */
public class Potions implements IItemHandler
{
    protected static final Logger _log = Logger.getLogger(Potions.class.getName());
    private int _herbstask = 0;

    /**
     * Task for Herbs
     */
    private class HerbTask implements Runnable
    {
	private L2PlayableInstance _playable;
	private int _magicId;
	private int _level;

	HerbTask(L2PlayableInstance playable, int magicId, int level)
	{
	    _playable = playable;
	    _magicId = magicId;
	    _level = level;
	}

	public void run()
	{
	    try
	    {
		usePotion(_playable, _magicId, _level);
	    } catch (Throwable t)
	    {
		_log.log(Level.WARNING, "", t);
	    }
	}
    }

    /**
     * Items
     */
    private static final int[] ITEM_IDS = {
    // potions
    65, 725, 726, 727, 728, 734, 735, 1060, 1061, 1062, 1073, 1374, 1375, 1539, 1540, 5591, 5592, 6035, 6036, 6652, 6553, 6554, 6555, 8193, 8194, 8195, 8196, 8197, 8198, 8199, 8200, 8201, 8202, 8600, 8601, 8602, 8603, 8604, 8605, 8606, 8607, 8608, 8609, 8610, 8611, 8612, 8613, 8614, 8786, 8787, 
    // elixir of life
    8622, 8623, 8624, 8625, 8626, 8627,
    // elixir of strength
    8628, 8629, 8630, 8631, 8632, 8633,
    // elixir of cp
    8634, 8635, 8636, 8637, 8638, 8639 };

    private void sendPacket(L2PlayableInstance playable, L2GameServerPacket packet)
    {
	if (playable instanceof L2PcInstance)
	{
	    playable.sendPacket(packet);
	}
    }

    private boolean doChecks(L2PlayableInstance playable, int itemId)
    {
	if (playable instanceof L2PcInstance)
	{
	    L2PcInstance activeChar = (L2PcInstance) playable;
	    // TVT EVENT CHECK
	    if (!TvTEvent.onPotionUse(activeChar.getName(), itemId))
	    {
		activeChar.sendPacket(new ActionFailed());
		return false;
	    }
	    // VIP EVENT CHECK
	    if (activeChar._inEventVIP && VIP._started && !Config.VIP_EVENT_POTIONS_ALLOWED)
	    {
		activeChar.sendPacket(new ActionFailed());
		return false;
	    }
	    // CTF EVENT CHECK
	    if (activeChar._inEventCTF && CTF._started && !Config.CTF_ALLOW_POTIONS)
	    {
		activeChar.sendPacket(new ActionFailed());
		return false;
	    }
        // SITTING CHECK 
	    if(activeChar.isSitting())  
	    {  
	    	activeChar.sendPacket(new ActionFailed());  
	    	return false;  
	    } 
	    // OLYMPIAD CHECK
	    if (activeChar.isInOlympiadMode())
	    {
		activeChar.sendPacket(new SystemMessage(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
		return false;
	    }
	}
	// are skills disabled?
	if (playable.isAllSkillsDisabled())
	{
	    sendPacket(playable, new ActionFailed());
	    return false;
	}
	return true;
    }

    private boolean isEffectReplaceable(L2PlayableInstance playable, Enum effectType, int itemId)
    {
	L2Effect[] effects = playable.getAllEffects();
	if (effects == null)
	{
	    return true;
	}
	for (L2Effect e : effects)
	{
	    if (e.getEffectType() == effectType)
	    {
		if (e.getTaskTime() > e.getSkill().getBuffDuration() * 67 / 100000)
		{
		    return true;
		}
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_PREPARED_FOR_REUSE);
		sm.addItemName(itemId);
		sendPacket(playable, sm);
		return false;
	    }
	}
	return true;
    }

    public synchronized void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
	// do common checks
	if ((playable instanceof L2PcInstance) || (playable instanceof L2PetInstance))
	{
	    if (!doChecks(playable, item.getItemId()))
		return;
	} else
	    return;
	// setup some vars
	boolean res = false;
	int itemId = item.getItemId();
	switch (itemId)
	{
	// MANA POTIONS
	case 726:
	{
	    res = usePotion(playable, 2003, 1); // mana drug, xml: 2003
	    break;
	}
	case 728:
	{
	    res = usePotion(playable, 2005, 1); // mana_potion, xml: 2005
	    break;
	}
	    // HEALING AND SPEED POTIONS
	case 65:
	{
	    res = usePotion(playable, 2001, 1); // red_potion, xml: 2001
	    break;
	}
	case 725:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2002, 1); // healing_drug, xml: 2002
	    break;
	}
	case 727:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2032, 1); // healing_potion, xml: 2032
	    break;
	}
	case 734:
	{
	    res = usePotion(playable, 2011, 1); // quick_step_potion, xml: 2011
	    break;
	}
	case 735:
	{
	    res = usePotion(playable, 2012, 1); // swift_attack_potion, xml:
	    // 2012
	    break;
	}
	case 1060:
	case 1073:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2031, 1); // beginner's potion, xml: 2031
	    break;
	}
	case 1061:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2032, 1); // healing_potion, xml: 2032
	    break;
	}
	case 1062:
	{
	    res = usePotion(playable, 2033, 1); // haste_potion, xml: 2033
	    break;
	}
	case 1374:
	{
	    res = usePotion(playable, 2034, 1); // adv_quick_step_potion, xml:
	    // 2034
	    break;
	}
	case 1375:
	{
	    res = usePotion(playable, 2035, 1); // adv_swift_attack_potion, xml:
	    // 2035
	    break;
	}
	case 1539:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2037, 1); // greater_healing_potion, xml:
	    // 2037
	    break;
	}
	case 1540:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2038, 1); // quick_healing_potion, xml:
	    // 2038
	    break;
	}
	case 5591:
	case 5592:
	{
	    if (!isEffectReplaceable(playable, L2Effect.EffectType.COMBAT_POINT_HEAL_OVER_TIME, itemId))
		return;
	    res = usePotion(playable, 2166, itemId == 5591 ? 1 : 2); // cp
	    // and
	    // greater
	    // cp
	    break;
	}
	case 6035:
	{
	    res = usePotion(playable, 2169, 1); // magic haste potion, xml: 2169
	    break;
	}
	case 6036:
	{
	    res = usePotion(playable, 2169, 2); // greater magic haste potion,
	    // xml: 2169
	    break;
	}
	    // ELIXIRS
	case 8622:
	case 8623:
	case 8624:
	case 8625:
	case 8626:
	case 8627:
	{
	    // only players can use elixires of life
	    if (playable instanceof L2PcInstance)
	    {
		L2PcInstance activeChar = (L2PcInstance) playable;
		// elixir of life
		if (((itemId == 8622) && (activeChar.getExpertiseIndex() == 0)) || ((itemId == 8623) && (activeChar.getExpertiseIndex() == 1)) || ((itemId == 8624) && (activeChar.getExpertiseIndex() == 2)) || ((itemId == 8625) && (activeChar.getExpertiseIndex() == 3)) || ((itemId == 8626) && (activeChar.getExpertiseIndex() == 4)) || ((itemId == 8627) && (activeChar.getExpertiseIndex() == 5)))
		{
		    res = usePotion(playable, 2287, (activeChar.getExpertiseIndex() + 1));
		} else
		{
		    // INCOMPATIBLE_ITEM_GRADE
		    SystemMessage sm = new SystemMessage(SystemMessageId.INCOMPATIBLE_ITEM_GRADE);
		    sm.addItemName(itemId);
		    sendPacket(playable, sm);
		    return;
		}
	    }
	    break;
	}
	case 8628:
	case 8629:
	case 8630:
	case 8631:
	case 8632:
	case 8633:
	{
	    // only players can use elixires of life
	    if (playable instanceof L2PcInstance)
	    {
		L2PcInstance activeChar = (L2PcInstance) playable;
		// elixir of Strength
		if (((itemId == 8628) && (activeChar.getExpertiseIndex() == 0)) || ((itemId == 8629) && (activeChar.getExpertiseIndex() == 1)) || ((itemId == 8630) && (activeChar.getExpertiseIndex() == 2)) || ((itemId == 8631) && (activeChar.getExpertiseIndex() == 3)) || ((itemId == 8632) && (activeChar.getExpertiseIndex() == 4)) || ((itemId == 8633) && (activeChar.getExpertiseIndex() == 5)))
		{
		    res = usePotion(playable, 2288, (activeChar.getExpertiseIndex() + 1));
		} else
		{
		    // INCOMPATIBLE_ITEM_GRADE
		    SystemMessage sm = new SystemMessage(SystemMessageId.INCOMPATIBLE_ITEM_GRADE);
		    sm.addItemName(itemId);
		    sendPacket(playable, sm);
		    return;
		}
	    }
	    break;
	}
	case 8634:
	case 8635:
	case 8636:
	case 8637:
	case 8638:
	case 8639:
	{
	    // only players can use elixires of life
	    if (playable instanceof L2PcInstance)
	    {
		L2PcInstance activeChar = (L2PcInstance) playable;
		// elixir of cp
		if (((itemId == 8634) && (activeChar.getExpertiseIndex() == 0)) || ((itemId == 8635) && (activeChar.getExpertiseIndex() == 1)) || ((itemId == 8636) && (activeChar.getExpertiseIndex() == 2)) || ((itemId == 8637) && (activeChar.getExpertiseIndex() == 3)) || ((itemId == 8638) && (activeChar.getExpertiseIndex() == 4)) || ((itemId == 8639) && (activeChar.getExpertiseIndex() == 5)))
		{
		    res = usePotion(playable, 2289, (activeChar.getExpertiseIndex() + 1));
		} else
		{
		    // INCOMPATIBLE_ITEM_GRADE
		    SystemMessage sm = new SystemMessage(SystemMessageId.INCOMPATIBLE_ITEM_GRADE);
		    sm.addItemName(itemId);
		    sendPacket(playable, sm);
		    return;
		}
	    }
	    break;
	}
	    // VALAKAS AMULETS
	case 6652:
	{
	    res = usePotion(playable, 2231, 1); // Amulet Protection of Valakas
	    break;
	}
	case 6653:
	{
	    res = usePotion(playable, 2223, 1); // Amulet Flames of Valakas
	    break;
	}
	case 6654:
	{
	    res = usePotion(playable, 2233, 1); // Amulet Flames of Valakas
	    break;
	}
	case 6655:
	{
	    res = usePotion(playable, 2232, 1); // Amulet Slay Valakas
	    break;
	}
	    // HERBS
	case 8600:
	{
	    res = usePotion(playable, 2278, 1); // Herb of Life
	    break;
	}
	case 8601:
	{
	    res = usePotion(playable, 2278, 2); // Greater Herb of Life
	    break;
	}
	case 8602:
	{
	    res = usePotion(playable, 2278, 3); // Superior Herb of Life
	    break;
	}
	case 8603:
	{
	    res = usePotion(playable, 2279, 1); // Herb of Mana
	    break;
	}
	case 8604:
	{
	    res = usePotion(playable, 2279, 2); // Greater Herb of Mane
	    break;
	}
	case 8605:
	{
	    res = usePotion(playable, 2279, 3); // Superior Herb of Mane
	    break;
	}
	case 8606:
	{
	    res = usePotion(playable, 2280, 1); // Herb of Strength
	    break;
	}
	case 8607:
	{
	    res = usePotion(playable, 2281, 1); // Herb of Magic
	    break;
	}
	case 8608:
	{
	    res = usePotion(playable, 2282, 1); // Herb of Atk. Spd.
	    break;
	}
	case 8609:
	{
	    res = usePotion(playable, 2283, 1); // Herb of Casting Spd.
	    break;
	}
	case 8610:
	{
	    res = usePotion(playable, 2284, 1); // Herb of Critical Attack
	    break;
	}
	case 8611:
	{
	    res = usePotion(playable, 2285, 1); // Herb of Speed
	    break;
	}
	case 8612:
	{
	    // Herb of Warrior
	    res = usePotion(playable, 2280, 1); // Herb of Strength
	    usePotion(playable, 2282, 1); // Herb of Atk. Spd
	    usePotion(playable, 2284, 1); // Herb of Critical Attack
	    break;
	}
	case 8613:
	{
	    // Herb of Mystic
	    res = usePotion(playable, 2281, 1); // Herb of Magic
	    usePotion(playable, 2283, 1); // Herb of Casting Spd.
	    break;
	}
	case 8614:
	{
	    // Herb of Warrior
	    res = usePotion(playable, 2278, 3); // Superior Herb of Life
	    usePotion(playable, 2279, 3); // Superior Herb of Mana
	    break;
	}
	case 8786:
	{
	    res = usePotion(playable, 2305, 1); // primeval_potion, xml: 2305
	    break;
	}
	case 8787:
	{
	    res = usePotion(playable, 2305, 1); // primeval_pot, xml: 2305
	    break;
	}
	    // FISHERMAN POTIONS
	case 8193:
	{
	    // Fisherman's Potion - Green
	    if (playable.getSkillLevel(1315) <= 3)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 1);
	    break;
	}
	case 8194:
	{
	    // Fisherman's Potion - Jade
	    if (playable.getSkillLevel(1315) <= 6)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 2);
	    break;
	}
	case 8195:
	{
	    // Fisherman's Potion - Blue
	    if (playable.getSkillLevel(1315) <= 9)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 3);
	    break;
	}
	case 8196:
	{
	    // Fisherman's Potion - Yellow
	    if (playable.getSkillLevel(1315) <= 12)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 4);
	    break;
	}
	case 8197:
	{
	    // Fisherman's Potion - Orange
	    if (playable.getSkillLevel(1315) <= 15)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 5);
	    break;
	}
	case 8198:
	{
	    // Fisherman's Potion - Purple
	    if (playable.getSkillLevel(1315) <= 18)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 6);
	    break;
	}
	case 8199:
	{
	    // Fisherman's Potion - Red
	    if (playable.getSkillLevel(1315) <= 21)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 7);
	    break;
	}
	case 8200:
	{
	    // Fisherman's Potion - White
	    if (playable.getSkillLevel(1315) <= 24)
	    {
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		sendPacket(playable, new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
		return;
	    }
	    res = usePotion(playable, 2274, 8);
	    break;
	}
	case 8201:
	{
	    res = usePotion(playable, 2274, 9); // Fisherman's Potion - Black
	    break;
	}
	case 8202:
	{
	    res = usePotion(playable, 2275, 1); // Fishing Potion
	    break;
	}
	    // DEFAULT
	default:
	}
	if (res)
	{
	    playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
	}
    }

    public boolean usePotion(L2PlayableInstance playable, int magicId, int level)
    {
	if (playable.isCastingNow() && (magicId > 2277) && (magicId < 2285))
	{
	    _herbstask += 100;
	    ThreadPoolManager.getInstance().scheduleAi(new HerbTask(playable, magicId, level), _herbstask);
	} else
	{
	    if ((magicId > 2277) && (magicId < 2285) && (_herbstask >= 100))
	    {
		_herbstask -= 100;
	    }
	    L2Skill skill = SkillTable.getInstance().getInfo(magicId, level);
	    if (skill != null)
	    {
		if (playable instanceof L2PcInstance)
		{
		    L2PcInstance activeChar = (L2PcInstance) playable;
		    activeChar.doCast(skill);
		    if (!(activeChar.isSitting() && !skill.isPotion()))
		    {
			return true;
		    }
		} else
		{
		    ((L2PetInstance) playable).doCast(skill);
		    return true;
		}
	    }
	}
	return false;
    }

    public int[] getItemIds()
    {
	return ITEM_IDS;
    }
}
