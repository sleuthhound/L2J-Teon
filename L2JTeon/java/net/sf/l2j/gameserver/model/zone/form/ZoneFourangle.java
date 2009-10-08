/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.zone.form;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

import javolution.lang.MathLib;
import net.sf.l2j.gameserver.model.zone.L2ZoneForm;

/**
 * A triangle zones
 * 
 * @author desti
 */
public class ZoneFourangle extends L2ZoneForm
{
    private final int _z1;
    private final int _z2;
    private Area _area = null;
    private final GeneralPath p;

    public ZoneFourangle(final int x1, final int y1, final int z1, final int z2)
    {
        p = new GeneralPath();
        _area = new Area();
        p.moveTo(x1, y1);
        _z1 = z1;
        _z2 = z2;
    }

    public void add(final int _x, final int _y)
    {
        p.lineTo(_x, _y);
    }

    public void done()
    {
        p.closePath();
        _area.add(new Area(p));
    }

    /**
     * @return Returns the area.
     */
    public Area getArea()
    {
        return _area;
    }

    @Override
    public double getDistanceToZone(final int x, final int y)
    {
        final double _x = _area.getBounds().getCenterX();
        final double _y = _area.getBounds().getCenterY();
        return MathLib.sqrt(MathLib.pow(_x - x, 2) + MathLib.pow(_y - y, 2));
    }

    @Override
    public int getHighZ()
    {
        return _z2;
    }

    /*
     * getLowZ() / getHighZ() - These two functions were added to cope with the
     * demand of the new fishing algorithms, wich are now able to correctly
     * place the hook in the water, thanks to getHighZ(). getLowZ() was added,
     * considering potential future modifications.
     */

    @Override
    public int getLowZ()
    {
        return _z1;
    }

    @Override
    public boolean intersectsRectangle(final int ax1, final int ax2,
            final int ay1, final int ay2)
    {
        final GeneralPath _tmp = new GeneralPath();
        _tmp.moveTo(ax1, ay1);
        _tmp.quadTo(ax1, ay1, ax2, ay2);
        _tmp.closePath();

        if (_area.intersects(_tmp.getBounds2D()))
            return true;
        else
            return false;
    }

    @Override
    public boolean isInsideZone(final int x, final int y, final int z)
    {
        if (z < _z1 || z > _z2)
            return false;

        if (_area.contains(x, y))
            return true;
        else
            return false;
    }
}
