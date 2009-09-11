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
/**
 * @author godson
 */
package net.sf.l2j.gameserver.model.olympiad;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastList;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.HeroSkillTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.OlympiadStadiaManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.item.Inventory;
import net.sf.l2j.gameserver.model.olympiad.Olympiad.COMP_TYPE;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.serverpackets.MagicSkillUser;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

public class L2OlympiadGame extends Olympiad
{
	protected COMP_TYPE _type;
	private boolean _aborted;
    private boolean _playerOneDisconnected; 
    private boolean _playerTwoDisconnected; 
	private String _playerOneName;
	private String _playerTwoName;
	private int _playerOneID = 0;
	private int _playerTwoID = 0;
	private L2PcInstance _playerOne;
	private L2PcInstance _playerTwo;
	private List<L2PcInstance> _players;
	private int[] _playerOneLocation;
	private int[] _playerTwoLocation;
	private int[] _stadiumPort;
	private List<L2PcInstance> _spectators;
	private SystemMessage _sm;
	private SystemMessage _sm2;
	private SystemMessage _sm3;

	protected L2OlympiadGame(int id, COMP_TYPE type, List<L2PcInstance> list, int[] stadiumPort)
	{
	    _aborted = false;
        _playerOneDisconnected = false; 
        _playerTwoDisconnected = false; 
	    _type = type;
	    _stadiumPort = stadiumPort;
	    _spectators = new FastList<L2PcInstance>();
	    if (list != null)
	    {
		_players = list;
		_playerOne = list.get(0);
		_playerTwo = list.get(1);
		try
		{
		    _playerOneName = _playerOne.getName();
		    _playerTwoName = _playerTwo.getName();
		    _playerOne.setOlympiadGameId(id);
		    _playerTwo.setOlympiadGameId(id);
		    _playerOneID = _playerOne.getObjectId();
		    _playerTwoID = _playerTwo.getObjectId();
		} catch (Exception e)
		{
		    _aborted = true;
		    clearPlayers();
		}
		_log.info("Olympiad System: Game - " + id + ": " + _playerOne.getName() + " Vs " + _playerTwo.getName());
	    } else
	    {
		_aborted = true;
		clearPlayers();
		return;
	    }
	}

    public boolean isAborted() 
    { 
    	return _aborted; 
    } 
    
    protected void clearPlayers() 
    { 
    	_playerOne = null; 
    	_playerTwo = null; 
    	_players = null; 
    	_playerOneName = ""; 
    	_playerTwoName = ""; 
    	_playerOneID = 0; 
    	_playerTwoID = 0; 
    } 
    
    protected void handleDisconnect(L2PcInstance player) 
    { 
    	if (player == _playerOne) 
    		_playerOneDisconnected = true; 
    	else if (player == _playerTwo) 
    		_playerTwoDisconnected = true; 
    } 
 	
	protected void removals()
	{
	    if (_aborted)
		return;
	    if ((_playerOne == null) || (_playerTwo == null))
		return;
        if (_playerOneDisconnected  || _playerTwoDisconnected) return; 
	    for (L2PcInstance player : _players)
	    {
		try
		{
		    // Remove Clan Skills
		    if (player.getClan() != null)
		    {
			for (L2Skill skill : player.getClan().getAllSkills())
			    player.removeSkill(skill, false);
		    }
		    // Abort casting if player casting
		    if (player.isCastingNow())
		    {
			player.abortCast();
		    }
		    // Remove Hero Skills
		    if (player.isHero())
		    {
			for (L2Skill skill : HeroSkillTable.GetHeroSkills())
			    player.removeSkill(skill, false);
		    }
		    // Remove Buffs
		    player.stopAllEffects();
		    // Remove Summon's Buffs
		    if (player.getPet() != null)
		    {
			L2Summon summon = player.getPet();
			summon.stopAllEffects();
			if (summon instanceof L2PetInstance)
			    summon.unSummon(player);
		    }
		    /*
		     * if (player.getCubics() != null) { for(L2CubicInstance
		     * cubic : player.getCubics().values()) {
		     * cubic.stopAction(); player.delCubic(cubic.getId()); }
		     * player.getCubics().clear(); }
		     */
		    // Remove player from his party
		    if (player.getParty() != null)
		    {
			L2Party party = player.getParty();
			party.removePartyMember(player);
		    }
		    // Remove Hero Weapons
		    // check to prevent the using of weapon/shield on
		    // strider/wyvern
		    L2ItemInstance wpn = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		    if (wpn == null)
			wpn = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LRHAND);
		    if (((wpn != null) && (((wpn.getItemId() >= 6611) && (wpn.getItemId() <= 6621)) || (wpn.getItemId() == 6842))) || (Config.ENABLE_OLY_WEAPON_ENCH && (wpn.getEnchantLevel() > Config.MAX_OLY_WEAPON_ENCH)))
		    {
			L2ItemInstance[] unequiped = player.getInventory().unEquipItemInBodySlotAndRecord(wpn.getItem().getBodyPart());
			InventoryUpdate iu = new InventoryUpdate();
			for (int i = 0; i < unequiped.length; i++)
			    iu.addModifiedItem(unequiped[i]);
			player.sendPacket(iu);
			player.abortAttack();
			player.broadcastUserInfo();
			// this can be 0 if the user pressed the right
			// mousebutton twice very fast
			if (unequiped.length > 0)
			{
			    if (unequiped[0].isWear())
				return;
			    SystemMessage sm = null;
			    if (unequiped[0].getEnchantLevel() > 0)
			    {
				sm = new SystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
				sm.addNumber(unequiped[0].getEnchantLevel());
				sm.addItemName(unequiped[0].getItemId());
			    } else
			    {
				sm = new SystemMessage(SystemMessageId.S1_DISARMED);
				sm.addItemName(unequiped[0].getItemId());
			    }
			    player.sendPacket(sm);
                        }
                    }
                    //L2EMU_EDIT_START
                    if(!Config.OLYMPIAD_ALLOW_AUTO_SS)    
                    {    

                    	//Remove shot automation
                    	Map<Integer, Integer> activeSoulShots = player.getAutoSoulShot();
                    	for (int itemId : activeSoulShots.values())
                    	{
                    		player.removeAutoSoulShot(itemId);
                    		ExAutoSoulShot atk = new ExAutoSoulShot(itemId, 0);  
                    		player.sendPacket(atk);  

                    	}
                    }
                }
                //L2EMU_EDIT_END  

               catch (Exception e) {}
                

            }
            
            _sm = new SystemMessage(SystemMessageId.THE_GAME_WILL_START_IN_S1_SECOND_S);
	    _sm.addNumber(120);
	    broadcastMessage(_sm, false);
	}

	protected boolean portPlayersToArena()
	{
	    _playerOneLocation = new int[3];
	    _playerTwoLocation = new int[3];

        boolean _playerOneCrash = (_playerOne == null || _playerOneDisconnected); 
        boolean _playerTwoCrash = (_playerTwo == null || _playerTwoDisconnected); 
        
	    if (_playerOneCrash)
	    {
		StatsSet playerOneStat;
		playerOneStat = _nobles.get(_playerOneID);
		int playerOnePoints = playerOneStat.getInteger(POINTS);
		playerOneStat.set(POINTS, playerOnePoints - playerOnePoints / 5);
		_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... " + _playerOneName + " lost " + (playerOnePoints - playerOnePoints / 5) + " points for crash before teleport to arena");
	    }
	    if (_playerTwoCrash)
	    {
		StatsSet playerTwoStat;
		playerTwoStat = _nobles.get(_playerTwoID);
		int playerTwoPoints = playerTwoStat.getInteger(POINTS);
		playerTwoStat.set(POINTS, playerTwoPoints - playerTwoPoints / 5);
		_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... " + _playerTwoName + " lost " + (playerTwoPoints - playerTwoPoints / 5) + " points for crash before teleport to arena");
	    }
	    if (_playerOneCrash || _playerTwoCrash || _aborted)
	    {
		_playerOne = null;
		_playerTwo = null;
		_aborted = true;
		return false;
	    }
	    try
	    {
		_playerOneLocation[0] = _playerOne.getX();
		_playerOneLocation[1] = _playerOne.getY();
		_playerOneLocation[2] = _playerOne.getZ();
		_playerTwoLocation[0] = _playerTwo.getX();
		_playerTwoLocation[1] = _playerTwo.getY();
		_playerTwoLocation[2] = _playerTwo.getZ();
		// _playerOne.getAppearance().setInvisible();
		// _playerOne.broadcastUserInfo();
		// _playerOne.decayMe();
		// _playerOne.spawnMe();
		// _playerTwo.getAppearance().setInvisible();
		// _playerTwo.broadcastUserInfo();
		// _playerTwo.decayMe();
		// _playerTwo.spawnMe();
		if (_playerOne.isSitting())
		    _playerOne.standUp();
		if (_playerTwo.isSitting())
		    _playerTwo.standUp();
		_playerOne.setTarget(null);
		_playerTwo.setTarget(null);
		_playerOne.teleToLocation(_stadiumPort[0], _stadiumPort[1], _stadiumPort[2], true);
		_playerTwo.teleToLocation(_stadiumPort[0], _stadiumPort[1], _stadiumPort[2], true);
		_playerOne.setIsInOlympiadMode(true);
		_playerOne.setIsOlympiadStart(false);
		_playerTwo.setIsInOlympiadMode(true);
		_playerTwo.setIsOlympiadStart(false);
	    } catch (NullPointerException e)
	    {
		return false;
	    }
	    return true;
	}

	protected void sendMessageToPlayers(boolean toBattleBegin, int nsecond)
	{
	    if (!toBattleBegin)
		_sm = new SystemMessage(SystemMessageId.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S);
	    else
		_sm = new SystemMessage(SystemMessageId.THE_GAME_WILL_START_IN_S1_SECOND_S);
	    _sm.addNumber(nsecond);
	    try
	    {
		for (L2PcInstance player : _players)
		    player.sendPacket(_sm);
	    } catch (Exception e)
	    {
	    }
	    ;
	}

	protected void portPlayersBack()
	{
	    try
	    {
		_playerOne.teleToLocation(_playerOneLocation[0], _playerOneLocation[1], _playerOneLocation[2], true);
	    } catch (Exception e)
	    {
	    }
	    try
	    {
		_playerTwo.teleToLocation(_playerTwoLocation[0], _playerTwoLocation[1], _playerTwoLocation[2], true);
	    } catch (Exception e)
	    {
	    }
	    for (L2PcInstance player : _players)
	    {
		try
		{
		    player.setIsInOlympiadMode(false);
		    player.setIsOlympiadStart(false);
		    player.setOlympiadSide(-1);
		    player.setOlympiadGameId(-1);
		    player.setCurrentCp(player.getMaxCp());
		    player.setCurrentHp(player.getMaxHp());
		    player.setCurrentMp(player.getMaxMp());
		    player.getStatus().startHpMpRegeneration();
		    // Add Clan Skills
		    if (player.getClan() != null)
		    {
			for (L2Skill skill : player.getClan().getAllSkills())
			{
			    if (skill.getMinPledgeClass() <= player.getPledgeClass())
				player.addSkill(skill, false);
			}
		    }
		    // Add Hero Skills
		    if (player.isHero())
		    {
			for (L2Skill skill : HeroSkillTable.GetHeroSkills())
			    player.addSkill(skill, false);
		    }
		    player.sendSkillList();
		} catch (Exception e)
		{
		}
	    }
	}

	protected void validateWinner()
	{
	    if (_aborted || ((_playerOne == null) && (_playerTwo == null)) || ((_playerOneDisconnected) && (_playerTwoDisconnected)))
	    {
		_log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... aborted/tie due to crashes!");
		return;
	    }
	    StatsSet playerOneStat;
	    StatsSet playerTwoStat;
	    playerOneStat = _nobles.get(_playerOneID);
	    playerTwoStat = _nobles.get(_playerTwoID);
	    int playerOnePlayed = playerOneStat.getInteger(COMP_DONE);
	    int playerTwoPlayed = playerTwoStat.getInteger(COMP_DONE);
	    int playerOnePoints = playerOneStat.getInteger(POINTS);
	    int playerTwoPoints = playerTwoStat.getInteger(POINTS);
	    double playerOneHp = 0;
	    double hpDiffOne = 9999999;
	    try
	    {
	    if (_playerOne != null && !_playerOneDisconnected) 
		{
		    playerOneHp = _playerOne.getCurrentHp() + _playerOne.getCurrentCp();
		    hpDiffOne = _playerOne.getMaxHp() + _playerOne.getMaxCp() - playerOneHp;
		}
	    } catch (Exception e)
	    {
		playerOneHp = 0;
		hpDiffOne = 9999999;
	    }
	    double playerTwoHp = 0;
	    double hpDiffTwo = 9999999;
	    try
	    {
	    if (_playerTwo != null && !_playerTwoDisconnected)
		{
		    playerTwoHp = _playerTwo.getCurrentHp() + _playerTwo.getCurrentCp();
		    hpDiffTwo = _playerTwo.getMaxHp() + _playerTwo.getMaxCp() - playerTwoHp;
		}
	    } catch (Exception e)
	    {
		playerTwoHp = 0;
		hpDiffTwo = 9999999;
	    }
	    _sm = new SystemMessage(SystemMessageId.S1_HAS_WON_THE_GAME);
	    _sm2 = new SystemMessage(SystemMessageId.S1_HAS_GAINED_S2_OLYMPIAD_POINTS);
	    _sm3 = new SystemMessage(SystemMessageId.S1_HAS_LOST_S2_OLYMPIAD_POINTS);
	    String result = "";
        // if players crashed, search if they've relogged 
        _playerOne =  L2World.getInstance().getPlayer(_playerOneName);  
        _players.set(0, _playerOne); 
        _playerTwo =  L2World.getInstance().getPlayer(_playerTwoName); 
        _players.set(1, _playerTwo); 
        
        if ((playerTwoHp == 0 && playerOneHp != 0) || (hpDiffOne < hpDiffTwo && playerTwoHp != 0)) 
	    {
		int pointDiff;
		pointDiff = playerTwoPoints / 3;
		playerOneStat.set(POINTS, playerOnePoints + pointDiff);
		playerTwoStat.set(POINTS, playerTwoPoints - pointDiff);
		_sm.addString(_playerOneName);
		broadcastMessage(_sm, true);
		_sm2.addString(_playerOneName);
		_sm2.addNumber(pointDiff);
		broadcastMessage(_sm2, true);
		_sm3.addString(_playerTwoName);
		_sm3.addNumber(pointDiff);
		broadcastMessage(_sm3, true);

		try
		{
		    result = " (" + playerOneHp + "hp vs " + playerTwoHp + "hp - " + hpDiffOne + " vs " + hpDiffTwo + ") " + _playerOneName + " win " + pointDiff + " points";
		    L2ItemInstance item = _playerOne.getInventory().addItem("Olympiad", 6651, 30, _playerOne, null);
		    InventoryUpdate iu = new InventoryUpdate();
		    iu.addModifiedItem(item);
		    _playerOne.sendPacket(iu);
		    SystemMessage sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
		    sm.addItemName(item.getItemId());
		    sm.addNumber(30);
		    _playerOne.sendPacket(sm);
		} catch (Exception e)
		{
		}
	    } else if ((playerOneHp == 0 && playerTwoHp != 0) || (hpDiffOne > hpDiffTwo && playerOneHp != 0))
	    {
		int pointDiff;
		pointDiff = playerOnePoints / 3;
		playerTwoStat.set(POINTS, playerTwoPoints + pointDiff);
		playerOneStat.set(POINTS, playerOnePoints - pointDiff);
		_sm.addString(_playerTwoName);
		broadcastMessage(_sm, true);
        _sm2.addString(_playerTwoName); 
        _sm2.addNumber(pointDiff);
		broadcastMessage(_sm2, true);
        _sm3.addString(_playerOneName); 
        _sm3.addNumber(pointDiff);
		broadcastMessage(_sm3, true);

		try
		{
		    result = " (" + playerOneHp + "hp vs " + playerTwoHp + "hp - " + hpDiffOne + " vs " + hpDiffTwo + ") " + _playerTwoName + " win " + pointDiff + " points";
		    L2ItemInstance item = _playerTwo.getInventory().addItem("Olympiad", 6651, 30, _playerTwo, null);
		    InventoryUpdate iu = new InventoryUpdate();
		    iu.addModifiedItem(item);
		    _playerTwo.sendPacket(iu);
		    SystemMessage sm = new SystemMessage(SystemMessageId.EARNED_S2_S1_S);
		    sm.addItemName(item.getItemId());
		    sm.addNumber(30);
		    _playerTwo.sendPacket(sm);
		} catch (Exception e)
		{
		}
	    } else
	    {
		result = " tie";
		_sm = new SystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE);
		broadcastMessage(_sm, true);
	    }
	    _log.info("Olympia Result: " + _playerOneName + " vs " + _playerTwoName + " ... " + result);
	    playerOneStat.set(COMP_DONE, playerOnePlayed + 1);
	    playerTwoStat.set(COMP_DONE, playerTwoPlayed + 1);
	    _nobles.remove(_playerOneID);
	    _nobles.remove(_playerTwoID);
	    _nobles.put(_playerOneID, playerOneStat);
	    _nobles.put(_playerTwoID, playerTwoStat);
	    _sm = new SystemMessage(SystemMessageId.YOU_WILL_GO_BACK_TO_THE_VILLAGE_IN_S1_SECOND_S);
	    _sm.addNumber(20);
	    broadcastMessage(_sm, true);
	}

	protected void additions()
	{
	    for (L2PcInstance player : _players)
	    {
		try
		{
		    // Set HP/CP/MP to Max
		    player.setCurrentCp(player.getMaxCp());
		    player.setCurrentHp(player.getMaxHp());
		    player.setCurrentMp(player.getMaxMp());
		    // Wind Walk Buff for Both
		    L2Skill skill;
		    SystemMessage sm;
		    skill = SkillTable.getInstance().getInfo(1204, 2);
		    skill.getEffects(player, player);
		    player.broadcastPacket(new MagicSkillUser(player, player, skill.getId(), 2, skill.getHitTime(), 0));
		    sm = new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
		    sm.addSkillName(1204);
		    player.sendPacket(sm);
		    if (!player.isMageClass())
		    {
			// Haste Buff to Fighters
                        if(Config.OLYMPIAD_GIVE_HASTE_FIGHTERS) 
                        {
			skill = SkillTable.getInstance().getInfo(1086, Config.OLYMPIAD_HASTE_LVL);
			skill.getEffects(player, player);
			player.broadcastPacket(new MagicSkillUser(player, player, skill.getId(), Config.OLYMPIAD_HASTE_LVL, skill.getHitTime(), 0));
			sm = new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
			sm.addSkillName(1086);
			player.sendPacket(sm);
                        }
                    }
                    else
                    {
			// Acumen Buff to Mages
                        if(Config.OLYMPIAD_GIVE_ACUMEN_MAGES) 
                        {
			skill = SkillTable.getInstance().getInfo(1085, Config.OLYMPIAD_ACUMEN_LVL);
			skill.getEffects(player, player);
			player.broadcastPacket(new MagicSkillUser(player, player, skill.getId(), Config.OLYMPIAD_ACUMEN_LVL, skill.getHitTime(), 0));
			sm = new SystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
			sm.addSkillName(1085);
			player.sendPacket(sm);
                        }
                        //L2EMU_ADD
                    }
                }
                catch (Exception e) { }
            }
        }
        
	protected boolean makePlayersVisible()
	{
	    _sm = new SystemMessage(SystemMessageId.STARTS_THE_GAME);
	    try
	    {
		for (L2PcInstance player : _players)
		{
		    player.getAppearance().setVisible();
		    player.broadcastUserInfo();
		    player.sendPacket(_sm);
		    if (player.getPet() != null)
			player.getPet().updateAbnormalEffect();
		}
	    } catch (NullPointerException e)
	    {
		_aborted = true;
		return false;
	    }
	    return true;
	}

	protected boolean makeCompetitionStart()
	{
        if (_aborted) return false; 
        
	    _sm = new SystemMessage(SystemMessageId.STARTS_THE_GAME);
	    
	    try
	    {
		for (L2PcInstance player : _players)
		{
                    player.setCurrentCp(player.getMaxCp());
                    player.setCurrentHp(player.getMaxHp());
                    player.setCurrentMp(player.getMaxMp());

		    player.setIsOlympiadStart(true);
		    player.sendPacket(_sm);
		}
	    } catch (Exception e)
	    {
		_aborted = true;
		return false;
	    }
	    return true;
	}

	protected String getTitle()
	{
	    String msg = "";
	    msg += _playerOneName + " : " + _playerTwoName;
	    return msg;
	}

	protected L2PcInstance[] getPlayers()
	{
	    L2PcInstance[] players = new L2PcInstance[2];
	    if ((_playerOne == null) || (_playerTwo == null))
		return null;
	    players[0] = _playerOne;
	    players[1] = _playerTwo;
	    return players;
	}

	protected List<L2PcInstance> getSpectators()
	{
	    return _spectators;
	}

	protected void addSpectator(L2PcInstance spec)
	{
	    _spectators.add(spec);
	}

	protected void removeSpectator(L2PcInstance spec)
	{
	    if ((_spectators != null) && _spectators.contains(spec))
		_spectators.remove(spec);
	}

	protected void clearSpectators()
	{
	    if (_spectators != null)
	    {
		for (L2PcInstance pc : _spectators)
		{
		    try
		    {
			if (!pc.inObserverMode())
			    continue;
			pc.leaveOlympiadObserverMode();
		    } catch (NullPointerException e)
		    {
		    }
		}
		_spectators.clear();
	    }
	}

	private void broadcastMessage(SystemMessage sm, boolean toAll)
	{
        try {  
            _playerOne.sendPacket(sm); 
            _playerTwo.sendPacket(sm);  
        } catch (Exception e) {}
        
	    if (toAll && (_spectators != null))
	    {
		for (L2PcInstance spec : _spectators)
		{
		    try
		    {
			spec.sendPacket(sm);
		    } catch (NullPointerException e)
		    {
		    }
		}
	    }
	}
    }