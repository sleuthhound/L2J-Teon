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
package net.sf.l2j.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.SecureRandom;

public class RandomIntGenerator
{
    private static final Log _log = LogFactory.getLog(RandomIntGenerator.class); 

    private SecureRandom _random = new SecureRandom();
    
    private static RandomIntGenerator _instance;
    
    public SecureRandom getSecureRandom()
    {
    	return _random;
    }
    
    public static final RandomIntGenerator getInstance()
    {
        if (_instance == null)
            _instance = new RandomIntGenerator();
        return _instance;
    }
    
    private RandomIntGenerator()
    {
        _log.info("RandomIntGenerator: initialized");
    }
}