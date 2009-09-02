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
/**
 * @author DaRkRaGe
 */
package net.sf.l2j.gameserver.datatables;

public class PcColorContainer
{
    private int _color;
    private long _regTime;
    private long _time;

    public PcColorContainer(int color, long regTime, long time)
    {
	_color = color;
	_regTime = regTime;
	_time = time;
    }

    /**
     * Returns the color
     * 
     * @return int
     */
    public int getColor()
    {
	return _color;
    }

    /**
     * Returns the time when the color was registered
     * 
     * @return long
     */
    public long getRegTime()
    {
	return _regTime;
    }

    /**
     * Returns the time when the color should be deleted
     * 
     * @return long
     */
    public long getTime()
    {
	return _time;
    }
}