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

import javolution.util.FastList;
import javolution.util.FastMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad.COMP_TYPE;
import net.sf.l2j.util.Rnd;

public class OlympiadManager implements Runnable
    {
    private final Olympiad olympiad;
    protected static Map<String, L2PcInstance> _IPRegisters;
	public Map<Integer, L2OlympiadGame> _olympiadInstances;
	public Map<Integer, List<L2PcInstance>> _classBasedParticipants;
	public Map<Integer, List<L2PcInstance>> _nonClassBasedParticipants;

	public OlympiadManager(Olympiad olympiad)
	{
        this.olympiad = olympiad;
	    _olympiadInstances = new FastMap<Integer, L2OlympiadGame>();
	    Olympiad._manager = this;
	}

	public synchronized void run()
	{
	    if (this.olympiad.isOlympiadEnd())
	    {
	    	this.olympiad._scheduledManagerTask.cancel(true);
		return;
	    }
	    if (!this.olympiad.inCompPeriod())
		return;
	    // Announcements.getInstance().announceToAll("Comp Match Init");
	    if (Olympiad._nobles.size() == 0)
		return;

	    try
	    {
		sortClassBasedOpponents();
		_nonClassBasedParticipants = pickOpponents(Olympiad._nonClassBasedRegisters);
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    int classIndex = 0;
	    int nonClassIndex = 0;
	    int index = 0;
	    for (int i = 0; i < Olympiad.COLLISIEUMS; i++)
	    {
		if (_classBasedParticipants.get(classIndex) != null)
		{
		    _olympiadInstances.put(index, new L2OlympiadGame(index, COMP_TYPE.CLASSED, _classBasedParticipants.get(classIndex), Olympiad.STADIUMS[index]));
		    index++;
		    classIndex++;
		}
		if (_nonClassBasedParticipants.get(nonClassIndex) != null)
		{
		    _olympiadInstances.put(index, new L2OlympiadGame(index, COMP_TYPE.NON_CLASSED, _nonClassBasedParticipants.get(nonClassIndex), Olympiad.STADIUMS[index]));
		    nonClassIndex++;
		    index++;
		}
	    }
	    Olympiad._compStarted = false; 
	    if (_olympiadInstances.size() == 0)
		return;
	    
        for (L2OlympiadGame instance : _olympiadInstances.values()) 
        {
            if (!instance.isAborted()) 
            	Olympiad._compStarted = true; 
        }
        
        if (!Olympiad._compStarted) 
            return; 
        	
	    for (L2OlympiadGame instance : _olympiadInstances.values())
		instance.sendMessageToPlayers(false, 30);
	    // Wait 30 seconds
	    try
	    {
		wait(30000);
	    } catch (InterruptedException e)
	    {
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
		instance.portPlayersToArena();
	    // Wait 20 seconds
	    try
	    {
		wait(20000);
	    } catch (InterruptedException e)
	    {
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
		instance.removals();
	    Olympiad._battleStarted = true;
        // The user info status... 
        for (L2OlympiadGame instance : _olympiadInstances.values()) 
            instance.sentPacketInfoToPlayers(); 
        
	    // Wait 1 min
	    for (int i = 60; i > 10; i -= 10)
	    {
		for (L2OlympiadGame instance : _olympiadInstances.values())
		{
		    instance.sendMessageToPlayers(true, i);
		    if (i == 20)
			instance.additions();
		}
		try
		{
		    wait(10000);
		} catch (InterruptedException e)
		{
		}
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
	    {
		instance.sendMessageToPlayers(true, 10);
	    }
	    try
	    {
		wait(5000);
	    } catch (InterruptedException e)
	    {
	    }
	    for (int i = 5; i > 0; i--)
	    {
		for (L2OlympiadGame instance : _olympiadInstances.values())
		{
		    instance.sendMessageToPlayers(true, i);
		}
		try
		{
		    wait(1000);
		} catch (InterruptedException e)
		{
		}
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
	    {
		instance.makeCompetitionStart();
	    }
	    // Wait 6 minutes (Battle)
	    try
	    {
		wait(Olympiad.BATTLE_PERIOD);
	    } catch (InterruptedException e)
	    {
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
	    {
		try
		{
		    instance.validateWinner();
		} catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	    // Wait 20 seconds
	    try
	    {
		wait(20000);
	    } catch (InterruptedException e)
	    {
	    }
	    for (L2OlympiadGame instance : _olympiadInstances.values())
	    {
		instance.portPlayersBack();
		instance.clearSpectators();
	    }
	    // Wait 20 seconds
	    try
	    {
		wait(20000);
	    } catch (InterruptedException e)
	    {
	    }
	    _classBasedParticipants.clear();
	    _nonClassBasedParticipants.clear();
	    _olympiadInstances.clear();
	    Olympiad._classBasedRegisters.clear();
	    Olympiad._nonClassBasedRegisters.clear();
	    if (Config.DISABLE_OLY_DUALBOX)
	    {
		_IPRegisters.clear();
	    }
	    Olympiad._battleStarted = false;
	    Olympiad._compStarted = false;
	}

	protected L2OlympiadGame getOlympiadInstance(int index)
	{
	    if ((_olympiadInstances != null) || Olympiad._compStarted)
	    {
		return _olympiadInstances.get(index);
	    }
	    return null;
	}

	private void sortClassBasedOpponents()
	{
	    Map<Integer, List<L2PcInstance>> result = new FastMap<Integer, List<L2PcInstance>>();
	    _classBasedParticipants = new FastMap<Integer, List<L2PcInstance>>();
	    int count = 0;
	    if (Olympiad._classBasedRegisters.size() == 0)
		return;
	    for (List<L2PcInstance> classed : Olympiad._classBasedRegisters.values())
	    {
		if (classed.size() == 0)
		    continue;
		try
		{
		    result = pickOpponents(classed);
		} catch (Exception e)
		{
		    e.printStackTrace();
		}
		if (result.size() == 0)
		    continue;
		for (List<L2PcInstance> list : result.values())
		{
		    if (count == 10)
			break;
		    _classBasedParticipants.put(count, list);
		    count++;
		}
		if (count == 10)
		    break;
	    }
	}

	protected Map<Integer, L2OlympiadGame> getOlympiadGames()
	{
	    return _olympiadInstances == null ? null : _olympiadInstances;
	}

	private Map<Integer, List<L2PcInstance>> pickOpponents(List<L2PcInstance> list) throws Exception
	{
	    Map<Integer, List<L2PcInstance>> result = new FastMap<Integer, List<L2PcInstance>>();
	    if (list.size() == 0)
		return result;
	    int loopCount = list.size() / 2;
	    int first;
	    int second;
	    if (loopCount < 1)
		return result;
	    int count = 0;
	    for (int i = 0; i < loopCount; i++)
	    {
		count++;
		List<L2PcInstance> opponents = new FastList<L2PcInstance>();
		first = Rnd.nextInt(list.size());
		opponents.add(list.get(first));
		list.remove(first);
		second = Rnd.nextInt(list.size());
		opponents.add(list.get(second));
		list.remove(second);
		result.put(i, opponents);
		if (count == 11)
		    break;
	    }
	    return result;
	}

	protected String[] getAllTitles()
	{
	    if (!Olympiad._compStarted)
		return null;
	    if (_olympiadInstances.size() == 0)
		return null;
	    String[] msg = new String[_olympiadInstances.size()];
	    int count = 0;
	    int match = 1;
	    for (L2OlympiadGame instance : _olympiadInstances.values())
	    {
		msg[count] = match + "_In Progress_" + instance.getTitle();
		count++;
		match++;
	    }
	    return msg;
	}
    }