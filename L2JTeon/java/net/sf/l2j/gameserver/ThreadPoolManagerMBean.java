/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver;


/**
 * Interface for JMX Administration
 * 
 * This Mbean gives information about the thread pools in the gameserver.
 */
public interface ThreadPoolManagerMBean
{
    public int getEffectsNbActiveThreads();
    public int getEffectsCorePoolSize();
    public int getEffectsPoolSize();
    public int getEffectsMaximumPoolSize();
    public long getEffectsCompletedTasks();
    public long getEffectsScheduledTasks();
     
    
    public int getGeneralScheduledActiveThreads();
    public int getGeneralScheduledCorePoolSize();
    public int getGeneralScheduledPoolSize();
    public int getGeneralScheduledMaximumPoolSize();
    public long getGeneralScheduledCompletedTasks();
    public long getGeneralScheduledScheduledTasks();
    
    
    public int getAIActiveThreads();
    public int getAICorePoolSize();
    public int getAIPoolSize();
    public int getAIMaximumPoolSize();
    public long getAICompletedTasks();
    public long getAIScheduledTasks();
    
    
    public int getPacketsActiveThreads();
    public int getPacketsCorePoolSize();
    public int getPacketsMaximumPoolSize();
    public int getPacketsLargestPoolSize();
    public int getPacketsPoolSize();
    public long getPacketsCompletedTasks();
    public long getPacketsQueuedTasks();
    
    
    public int getIoPacketsActiveThreads();
    public int getIoPacketsCorePoolSize();
    public int getIoPacketsMaximumPoolSize();
    public int getIoPacketsLargestPoolSize();
    public int getIoPacketsPoolSize();
    public long getIoPacketsCompletedTasks();
    public long getIoPacketsQueuedTasks();
     
    public int getGeneralActiveThreads();
    public int getGeneralCorePoolSize();
    public int getGeneralMaximumPoolSize();
    public int getGeneralLargestPoolSize();
    public int getGeneralPoolSize();
    public long getGeneralCompletedTasks();
    public long getGeneralQueuedTasks();   
}
