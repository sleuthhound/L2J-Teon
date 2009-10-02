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

package net.sf.l2j.gameserver.instancemanager.clanhallsiege;

import java.util.Calendar;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.ArrayList;

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.DevastatedCastleManager;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DevastatedGuard
{
	private static DevastatedGuard _instance;
	protected static Log _log = LogFactory.getLog(DevastatedGuard.class.getName());

    private L2NpcInstance _minion1;
    private L2NpcInstance _minion2;
    private L2NpcInstance _result;
    private ArrayList _monsters;
    private ArrayList _spawns;

	public static final DevastatedGuard getInstance()
	{
		if (_instance == null)
			_instance = new DevastatedGuard();
		return _instance;
	}

	private DevastatedGuard()
	{
		fillMonsters();
	}

    private static class MonsterLocation
    {

        private int getId()
        {
            return _id;
        }

        private int getX()
        {
            return _x;
        }

        private int getY()
        {
            return _y;
        }

        private int getZ()
        {
            return _z;
        }

        private int getHeading()
        {
            return _heading;
        }

        private int _id;
        private int _x;
        private int _y;
        private int _z;
        private int _heading;

        private MonsterLocation(int id, int x, int y, int z, int heading)
        {
            _id = id;
            _x = x;
            _y = y;
            _z = z;
            _heading = heading;
        }

    }

    public void spawnMonsters()
    {
        for(Iterator i = _monsters.iterator(); i.hasNext();)
        {
            MonsterLocation ml = (MonsterLocation)i.next();
            try
            {
            L2NpcTemplate template;
            L2Spawn sp;

                template = NpcTable.getInstance().getTemplate(ml.getId());
                sp = new L2Spawn(template);
                sp.setAmount(1);
                sp.setLocx(ml.getX());
                sp.setLocy(ml.getY());
                sp.setLocz(ml.getZ());
                sp.setHeading(ml.getHeading());
                sp.setRespawnDelay(300);
                sp.setLocation(0);
                sp.init();
                _spawns.add(sp);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void DeSpawn()
    {
        L2Spawn sp;
        for(Iterator i = _spawns.iterator(); i.hasNext(); sp.getLastSpawn().doDie(sp.getLastSpawn()))
        {
            sp = (L2Spawn)i.next();
            sp.stopRespawn();
        }

        _spawns.clear();
        _spawns = null;
    }


    private void addMonster(int id, int x, int y, int z, int heading)
    {
        _monsters.add(new MonsterLocation(id, x, y, z, heading));
    }

	private void fillMonsters()
	{
		addMonster(35413, 178288, -14924, -2200, 6320);
		addMonster(35412, 178255, -14884, -2200, 6320);
		addMonster(35413, 178222, -14924, -2200, 6320);
		addMonster(35412, 178222, -14884, -2200, 6320);
		addMonster(35412, 178420, -14904, -2200, 6320);
		addMonster(35412, 178387, -14904, -2200, 6320);
		addMonster(35412, 178288, -14884, -2200, 6320);
		addMonster(35412, 178387, -14884, -2200, 6320);
		addMonster(35413, 178354, -14944, -2200, 6320);
		addMonster(35412, 178321, -14884, -2200, 6320);
		addMonster(35413, 178222, -14944, -2200, 6320);
		addMonster(35412, 178354, -14904, -2200, 6320);
		addMonster(35413, 178255, -14924, -2200, 6320);
		addMonster(35413, 178387, -14924, -2200, 6320);
		addMonster(35413, 178354, -14924, -2200, 6320);
		addMonster(35413, 178420, -14924, -2200, 6320);
		addMonster(35412, 178354, -14884, -2200, 6320);
		addMonster(35412, 178420, -14884, -2200, 6320);
		addMonster(35413, 178454, -14944, -2200, 6320);
		addMonster(35413, 178454, -14924, -2200, 6320);
		addMonster(35413, 178420, -14944, -2200, 6320);
		addMonster(35412, 178222, -14904, -2200, 6320);
		addMonster(35413, 178321, -14944, -2200, 6320);
		addMonster(35413, 178321, -14924, -2200, 6320);
		addMonster(35412, 178288, -14904, -2200, 6320);
		addMonster(35412, 178321, -14904, -2200, 6320);
		addMonster(35413, 178255, -14944, -2200, 6320);
		addMonster(35412, 178255, -14904, -2200, 6320);
		addMonster(35413, 178288, -14944, -2200, 6320);
		addMonster(35412, 178454, -14884, -2200, 6320);
		addMonster(35413, 178387, -14944, -2200, 6320);
		addMonster(35412, 178454, -14904, -2200, 6320);
		addMonster(35413, 179052, -15226, -2221, 6320);
		addMonster(35413, 179260, -15341, -2221, 6320);
		addMonster(35413, 179101, -15253, -2221, 6320);
		addMonster(35413, 179073, -15203, -2221, 6320);
		addMonster(35413, 179144, -15271, -2221, 6320);
		addMonster(35413, 179246, -15285, -2221, 6320);
		addMonster(35413, 179164, -15247, -2221, 6320);
		addMonster(35413, 179226, -15309, -2221, 6320);
		addMonster(35413, 179322, -15349, -2221, 6320);
		addMonster(35413, 179302, -15372, -2221, 6320);
		addMonster(35413, 179189, -15286, -2221, 6320);
		addMonster(35413, 179391, -15439, -2221, 6320);
		addMonster(35413, 179341, -15406, -2221, 6320);
		addMonster(35415, 179503, -15925, -2256, 6320);
		addMonster(35415, 179562, -15984, -2256, 6320);
		addMonster(35415, 179491, -15981, -2256, 6320);
		addMonster(35415, 179370, -16196, -2256, 6320);
		addMonster(35411, 179426, -16009, -2253, 6320);
		addMonster(35415, 179544, -15882, -2256, 6320);
		addMonster(35415, 179599, -15943, -2256, 6320);
		addMonster(35415, 179570, -15901, -2256, 6320);
		addMonster(35415, 179408, -16158, -2256, 6320);
		addMonster(35415, 179279, -16219, -2256, 6320);
		addMonster(35411, 179327, -16101, -2253, 6320);
		addMonster(35411, 179540, -16876, -2246, 6320);
		addMonster(35413, 179009, -15201, -2221, 6320);
		addMonster(35413, 178951, -14699, -2080, 6320);
		addMonster(35413, 178801, -14975, -2080, 6320);
		addMonster(35413, 178865, -14857, -2080, 6320);
		addMonster(35413, 178822, -14936, -2080, 6320);
		addMonster(35413, 178843, -14897, -2080, 6320);
		addMonster(35413, 178929, -14739, -2080, 6320);
		addMonster(35413, 178908, -14778, -2080, 6320);
		addMonster(35413, 178886, -14818, -2080, 6320);
		addMonster(35411, 177719, -15951, -2253, 6320);
		addMonster(35413, 177838, -15664, -2226, 6320);
		addMonster(35411, 177627, -15953, -2250, 6320);
		addMonster(35411, 177387, -15955, -2250, 6320);
		addMonster(35411, 177667, -15921, -2253, 6320);
		addMonster(35413, 177859, -15812, -2226, 6320);
		addMonster(35411, 177661, -16014, -2253, 6320);
		addMonster(35411, 177608, -15985, -2250, 6320);
		addMonster(35415, 177663, -16154, -2250, 6320);
		addMonster(35415, 177530, -16079, -2250, 6320);
		addMonster(35411, 177709, -16043, -2253, 6320);
		addMonster(35411, 177703, -15999, -2250, 6320);
		addMonster(35411, 177810, -16145, -2253, 6320);
		addMonster(35415, 177571, -16105, -2250, 6320);
		addMonster(35415, 177473, -16011, -2250, 6320);
		addMonster(35415, 177612, -16090, -2250, 6320);
		addMonster(35415, 177657, -16113, -2250, 6320);
		addMonster(35415, 177387, -15996, -2250, 6320);
		addMonster(35411, 177564, -15963, -2250, 6320);
		addMonster(35411, 177606, -16035, -2253, 6320);
		addMonster(35411, 177470, -15856, -2250, 6320);
		addMonster(35415, 177428, -15981, -2250, 6320);
		addMonster(35411, 177506, -15887, -2250, 6320);
		addMonster(35411, 177517, -15930, -2250, 6320);
		addMonster(35411, 177308, -15861, -2253, 6320);
		addMonster(35411, 177861, -16164, -2253, 6320);
		addMonster(35413, 177906, -15791, -2226, 6320);
		addMonster(35413, 177765, -15643, -2226, 6320);
		addMonster(35413, 177880, -15744, -2226, 6320);
		addMonster(35413, 177788, -15578, -2226, 6320);
		addMonster(35413, 177811, -15622, -2226, 6320);
		addMonster(35413, 177859, -15704, -2226, 6320);
		addMonster(35413, 177769, -15540, -2226, 6320);
		addMonster(35413, 177813, -15726, -2226, 6320);
		addMonster(35413, 177707, -15427, -2226, 6320);
		addMonster(35413, 177680, -15485, -2226, 6320);
		addMonster(35413, 177722, -15561, -2226, 6320);
		addMonster(35413, 177745, -15498, -2226, 6320);
		addMonster(35413, 177803, -14971, -2210, 6320);
		addMonster(35413, 177727, -15464, -2226, 6320);
		addMonster(35415, 177433, -16026, -2250, 6320);
		addMonster(35413, 177649, -14750, -2210, 6320);
		addMonster(35413, 177619, -14705, -2210, 6320);
		addMonster(35413, 177711, -14838, -2210, 6320);
		addMonster(35413, 177834, -15015, -2210, 6320);
		addMonster(35413, 177741, -14883, -2210, 6320);
		addMonster(35413, 177772, -14927, -2210, 6320);
		addMonster(35413, 177680, -14794, -2210, 6320);
		addMonster(35411, 177400, -15854, -2250, 6320);
		addMonster(35415, 179697, -17781, -2256, 6320);
		addMonster(35411, 179479, -17133, -2256, 6320);
		addMonster(35411, 179485, -17213, -2246, 6320);
		addMonster(35411, 179593, -16876, -2246, 6320);
		addMonster(35411, 179468, -17280, -2256, 6320);
		addMonster(35411, 179433, -16991, -2246, 6320);
		addMonster(35411, 179514, -17281, -2256, 6320);
		addMonster(35411, 179525, -17135, -2256, 6320);
		addMonster(35411, 179444, -16937, -2256, 6320);
		addMonster(35411, 179438, -16875, -2246, 6320);
		addMonster(35415, 179633, -17137, -2256, 6320);
		addMonster(35411, 179537, -17214, -2246, 6320);
		addMonster(35411, 179594, -17453, -2246, 6320);
		addMonster(35415, 179576, -17137, -2256, 6320);
		addMonster(35415, 179508, -17341, -2252, 6320);
		addMonster(35415, 179446, -17391, -2252, 6320);
		addMonster(35415, 179437, -17522, -2252, 6320);
		addMonster(35415, 179536, -17842, -2252, 6320);
		addMonster(35415, 179432, -17719, -2252, 6320);
		addMonster(35415, 179436, -17841, -2252, 6320);
		addMonster(35411, 179542, -17453, -2246, 6320);
		addMonster(35415, 179436, -17776, -2256, 6320);
		addMonster(35415, 179534, -17892, -2252, 6320);
		addMonster(35415, 179482, -17841, -2252, 6320);
		addMonster(35415, 179696, -17844, -2252, 6320);
		addMonster(35415, 179604, -17525, -2252, 6320);
		addMonster(35415, 179707, -17722, -2252, 6320);
		addMonster(35411, 179715, -17454, -2246, 6320);
		addMonster(35411, 179641, -17215, -2246, 6320);
		addMonster(35415, 179665, -17527, -2252, 6320);
		addMonster(35415, 179557, -17524, -2252, 6320);
		addMonster(35415, 179636, -17780, -2256, 6320);
		addMonster(35415, 179694, -17897, -2252, 6320);
		addMonster(35414, 178682, -18200, -2200, 6320);
		addMonster(35413, 178577, -18422, -2250, 6320);
		addMonster(35412, 178745, -18186, -2200, 6320);
		addMonster(35413, 178528, -18499, -2250, 6320);
		addMonster(35414, 178640, -18196, -2200, 6320);
		addMonster(35413, 178766, -18228, -2200, 6320);
		addMonster(35412, 178724, -18184, -2200, 6320);
		addMonster(35412, 178703, -18182, -2200, 6320);
		addMonster(35413, 178575, -18500, -2250, 6320);
		addMonster(35413, 178703, -18222, -2200, 6320);
		addMonster(35413, 178530, -18421, -2250, 6320);
		addMonster(35413, 178523, -18696, -2250, 6320);
		addMonster(35412, 178661, -18178, -2200, 6320);
		addMonster(35414, 178661, -18198, -2200, 6320);
		addMonster(35413, 178788, -18229, -2200, 6320);
		addMonster(35414, 178724, -18204, -2200, 6320);
		addMonster(35413, 178574, -18539, -2250, 6320);
		addMonster(35413, 178578, -18383, -2250, 6320);
		addMonster(35413, 178573, -18577, -2250, 6320);
		addMonster(35413, 178530, -18382, -2250, 6320);
		addMonster(35413, 178528, -18461, -2250, 6320);
		addMonster(35413, 178526, -18538, -2250, 6320);
		addMonster(35413, 178526, -18576, -2250, 6320);
		addMonster(35413, 178570, -18736, -2250, 6320);
		addMonster(35413, 178524, -18618, -2250, 6320);
		addMonster(35413, 178571, -18657, -2250, 6320);
		addMonster(35413, 178523, -18656, -2250, 6320);
		addMonster(35413, 178523, -18735, -2250, 6320);
		addMonster(35413, 178571, -18618, -2250, 6320);
		addMonster(35413, 178571, -18697, -2250, 6320);
		addMonster(35413, 178576, -18461, -2250, 6320);
		addMonster(35413, 178682, -18220, -2200, 6320);
		addMonster(35413, 178661, -18218, -2200, 6320);
		addMonster(35414, 178745, -18206, -2200, 6320);
		addMonster(35412, 178682, -18180, -2200, 6320);
		addMonster(35414, 178703, -18202, -2200, 6320);
		addMonster(35412, 178640, -18176, -2200, 6320);
		addMonster(35414, 178788, -18209, -2200, 6320);
		addMonster(35413, 178640, -18216, -2200, 6320);
		addMonster(35412, 178788, -18189, -2200, 6320);
		addMonster(35413, 178745, -18226, -2200, 6320);
		addMonster(35414, 178766, -18208, -2200, 6320);
		addMonster(35412, 178766, -18188, -2200, 6320);
		addMonster(35413, 178724, -18224, -2200, 6320);
		addMonster(35413, 178430, -16901, -2217, 6320);
		addMonster(35415, 178285, -16832, -2217, 6320);
		addMonster(35413, 178153, -16914, -2217, 6320);
		addMonster(35411, 178398, -16781, -2218, 6320);
		addMonster(35415, 178363, -16768, -2217, 6320);
		addMonster(35413, 178437, -16587, -2217, 6320);
		addMonster(35413, 178431, -16784, -2217, 6320);
		addMonster(35411, 178120, -16714, -2218, 6320);
		addMonster(35415, 178366, -16721, -2217, 6320);
		addMonster(35413, 178433, -16742, -2217, 6320);
		addMonster(35413, 178430, -16862, -2217, 6320);
		addMonster(35411, 178395, -16865, -2218, 6320);
		addMonster(35415, 178288, -16783, -2217, 6320);
		addMonster(35411, 178397, -16824, -2218, 6320);
		addMonster(35415, 178090, -16878, -2217, 6320);
		addMonster(35413, 178154, -16797, -2217, 6320);
		addMonster(35415, 178027, -16773, -2217, 6320);
		addMonster(35413, 178435, -16665, -2217, 6320);
		addMonster(35413, 178433, -16704, -2217, 6320);
		addMonster(35411, 178060, -16868, -2218, 6320);
		addMonster(35413, 178156, -16718, -2217, 6320);
		addMonster(35411, 178324, -16762, -2218, 6320);
		addMonster(35413, 177869, -16832, -2217, 6320);
		addMonster(35411, 178117, -16885, -2218, 6320);
		addMonster(35415, 178366, -16674, -2217, 6320);
		addMonster(35413, 178435, -16627, -2217, 6320);
		addMonster(35415, 178366, -16582, -2217, 6320);
		addMonster(35411, 178403, -16566, -2218, 6320);
		addMonster(35413, 178437, -16548, -2217, 6320);
		addMonster(35415, 178290, -16550, -2217, 6320);
		addMonster(35413, 178160, -16600, -2217, 6320);
		addMonster(35411, 178122, -16759, -2218, 6320);
		addMonster(35411, 178122, -16672, -2218, 6320);
		addMonster(35415, 178095, -16736, -2217, 6320);
		addMonster(35411, 178324, -16804, -2218, 6320);
		addMonster(35415, 178029, -16587, -2217, 6320);
		addMonster(35415, 178290, -16690, -2217, 6320);
		addMonster(35415, 178027, -16820, -2217, 6320);
		addMonster(35413, 178431, -16822, -2217, 6320);
		addMonster(35411, 178326, -16720, -2218, 6320);
		addMonster(35413, 178158, -16679, -2217, 6320);
		addMonster(35413, 178161, -16562, -2217, 6320);
		addMonster(35413, 178158, -16640, -2217, 6320);
		addMonster(35413, 177872, -16714, -2217, 6320);
		addMonster(35411, 178061, -16738, -2218, 6320);
		addMonster(35413, 177871, -16752, -2217, 6320);
		addMonster(35413, 177876, -16597, -2217, 6320);
		addMonster(35413, 177873, -16675, -2217, 6320);
		addMonster(35413, 177869, -16873, -2217, 6320);
		addMonster(35413, 177874, -16637, -2217, 6320);
		addMonster(35411, 178060, -16826, -2218, 6320);
		addMonster(35411, 178063, -16784, -2218, 6320);
		addMonster(35415, 178029, -16727, -2217, 6320);
		addMonster(35413, 177876, -16558, -2217, 6320);
		addMonster(35413, 177870, -16794, -2217, 6320);
		addMonster(35413, 177868, -16911, -2217, 6320);
		addMonster(35413, 178156, -16756, -2217, 6320);
		addMonster(35415, 178092, -16782, -2217, 6320);
		addMonster(35413, 178153, -16876, -2217, 6320);
		addMonster(35413, 178154, -16836, -2217, 6320);
		addMonster(35412, 177217, -17168, -2200, 6320);
		addMonster(35413, 177187, -17128, -2200, 6320);
		addMonster(35413, 177276, -17128, -2200, 6320);
		addMonster(35413, 177335, -17128, -2200, 6320);
		addMonster(35414, 177246, -17148, -2200, 6320);
		addMonster(35412, 177246, -17168, -2200, 6320);
		addMonster(35412, 177129, -17168, -2200, 6320);
		addMonster(35412, 177158, -17168, -2200, 6320);
		addMonster(35413, 177217, -17128, -2200, 6320);
		addMonster(35413, 177158, -17128, -2200, 6320);
		addMonster(35413, 177129, -17128, -2200, 6320);
		addMonster(35414, 177129, -17148, -2200, 6320);
		addMonster(35412, 177276, -17168, -2200, 6320);
		addMonster(35414, 177158, -17148, -2200, 6320);
		addMonster(35413, 177305, -17128, -2200, 6320);
		addMonster(35412, 177187, -17168, -2200, 6320);
		addMonster(35414, 177305, -17148, -2200, 6320);
		addMonster(35413, 177396, -17452, -2207, 6320);
		addMonster(35413, 177397, -17634, -2207, 6320);
		addMonster(35416, 177664, -17599, -2219, 6320);
		addMonster(35416, 177721, -17813, -2219, 6320);
		addMonster(35416, 177501, -17424, -2219, 6320);
		addMonster(35415, 177632, -17788, -2215, 6320);
		addMonster(35413, 177466, -17445, -2207, 6320);
		addMonster(35414, 177217, -17148, -2200, 6320);
		addMonster(35413, 177396, -17545, -2207, 6320);
		addMonster(35414, 177187, -17148, -2200, 6320);
		addMonster(35414, 177335, -17148, -2200, 6320);
		addMonster(35412, 177335, -17168, -2200, 6320);
		addMonster(35412, 177305, -17168, -2200, 6320);
		addMonster(35415, 177439, -17426, -2215, 6320);
		addMonster(35415, 177438, -17472, -2215, 6320);
		addMonster(35416, 177662, -17685, -2219, 6320);
		addMonster(35415, 177527, -17578, -2215, 6320);
		addMonster(35415, 177531, -17399, -2215, 6320);
		addMonster(35415, 177561, -17563, -2215, 6320);
		addMonster(35413, 177604, -17718, -2207, 6320);
		addMonster(35413, 177466, -17537, -2207, 6320);
		addMonster(35416, 177500, -17469, -2219, 6320);
		addMonster(35415, 177527, -17537, -2215, 6320);
		addMonster(35413, 177467, -17809, -2207, 6320);
		addMonster(35413, 177466, -17716, -2207, 6320);
		addMonster(35416, 177496, -17645, -2219, 6320);
		addMonster(35413, 177398, -17817, -2207, 6320);
		addMonster(35416, 177560, -17849, -2219, 6320);
		addMonster(35415, 177524, -17763, -2215, 6320);
		addMonster(35415, 177432, -17791, -2215, 6320);
		addMonster(35413, 177397, -17723, -2207, 6320);
		addMonster(35415, 177523, -17810, -2215, 6320);
		addMonster(35416, 177501, -17384, -2219, 6320);
		addMonster(35413, 177603, -17539, -2207, 6320);
		addMonster(35416, 177725, -17503, -2219, 6320);
		addMonster(35416, 177666, -17508, -2219, 6320);
		addMonster(35415, 177564, -17425, -2215, 6320);
		addMonster(35413, 177749, -17538, -2207, 6320);
		addMonster(35413, 177603, -17446, -2207, 6320);
		addMonster(35413, 177749, -17446, -2207, 6320);
		addMonster(35416, 177728, -17377, -2219, 6320);
		addMonster(35415, 177440, -17380, -2215, 6320);
		addMonster(35416, 177567, -17376, -2219, 6320);
		addMonster(35414, 177276, -17148, -2200, 6320);
		addMonster(35416, 177724, -17594, -2219, 6320);
		addMonster(35415, 177436, -17605, -2215, 6320);
		addMonster(35415, 177636, -17516, -2215, 6320);
		addMonster(35413, 177601, -17811, -2207, 6320);
		addMonster(35413, 177693, -17718, -2207, 6320);
		addMonster(35416, 177722, -17728, -2219, 6320);
		addMonster(35415, 177559, -17708, -2215, 6320);
		addMonster(35416, 177662, -17644, -2219, 6320);
		addMonster(35415, 177635, -17603, -2215, 6320);
		addMonster(35413, 177694, -17811, -2207, 6320);
		addMonster(35415, 177558, -17755, -2215, 6320);
		addMonster(35413, 177693, -17447, -2207, 6320);
		addMonster(35413, 177693, -17629, -2207, 6320);
		addMonster(35416, 177664, -17559, -2219, 6320);
		addMonster(35413, 177466, -17397, -2207, 6320);
		addMonster(35413, 177466, -17627, -2207, 6320);
		addMonster(35416, 177724, -17553, -2219, 6320);
		addMonster(35413, 177750, -17628, -2207, 6320);
		addMonster(35413, 177751, -17810, -2207, 6320);
		addMonster(35416, 177661, -17818, -2219, 6320);
		addMonster(35416, 177496, -17686, -2219, 6320);
		addMonster(35413, 177750, -17717, -2207, 6320);
		addMonster(35415, 177633, -17740, -2215, 6320);
		addMonster(35413, 177693, -17539, -2207, 6320);
		addMonster(35413, 177604, -17628, -2207, 6320);
		addMonster(35415, 177431, -17836, -2215, 6320);
		addMonster(35415, 177631, -17835, -2215, 6320);
		addMonster(35416, 177722, -17768, -2219, 6320);
		addMonster(35413, 177246, -17128, -2200, 6320);
	}

	public void spawnMinions(L2NpcInstance npc, L2Object object)
	{
		CreatureSay cs = null;
        try
        {
            fillMonsters();
            L2NpcTemplate template;
            L2Spawn spawn;

            template = NpcTable.getInstance().getTemplate(35408);
            spawn = new L2Spawn(template);
            spawn.setLocx(178306);
            spawn.setLocy(-17535);
            spawn.setLocz(-2195);
            spawn.stopRespawn();
            spawn.spawnOne();
            cs = new CreatureSay(npc.getObjectId(),1,npc.getName(),"Gustave's soldiers, fight! Delivers the invader to die!");
            npc.broadcastPacket(cs);

            template = NpcTable.getInstance().getTemplate(35409);
            spawn = new L2Spawn(template);
            spawn.setLocx(178304);
            spawn.setLocy(-17712);
            spawn.setLocz(-2194);
            spawn.stopRespawn();
            _minion1 = spawn.spawnOne();
            cs = new CreatureSay(npc.getObjectId(),1,npc.getName(),"Qrants kingdom of Aden lion, honorable! Grants does not die $$ln Gustave to be honorable!");
            npc.broadcastPacket(cs);

            template = NpcTable.getInstance().getTemplate(35410);
            spawn = new L2Spawn(template);
            spawn.setLocx(178298);
            spawn.setLocy(-17624);
            spawn.setLocz(-2194);
            spawn.stopRespawn();
            _minion2 = spawn.spawnOne();
            cs = new CreatureSay(npc.getObjectId(),1,npc.getName(),"Comes to understand! Your these foreign lands invaders! This fort forever ruler, my Gustave lifts the sword!");
            npc.broadcastPacket(cs);

            spawnMonsters();
        }
        catch (Exception e)
        {
        	_log.warn("Guards spawn fails: " + e.getMessage(), e);
        }
        finally
        {
        	_log.info("Spawning Guards.");
        	}
	}
}