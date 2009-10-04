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
package net.sf.l2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;



public final class ExternalConfig
{
	protected static final Logger _log = Logger.getLogger(Config.class.getName());
	
	/** Extneral Config Path **/
	
    /** Properties file for Character Configurations */
    public static final String GRANDBOSS_CONFIG_FILE							= "./config/bosses/Grandboss.properties";
    
    
    /** Extneral Config Settings **/
    
    public static int load = 0;
    
    /** GrandBoss Settings **/

    public static int Antharas_Wait_Time;
    public static int Valakas_Wait_Time;
    public static int Interval_Of_Antharas_Spawn;
    public static int Random_Of_Antharas_Spawn;
    public static int Interval_Of_Valakas_Spawn;
    public static int Random_Of_Valakas_Spawn;
    public static int Interval_Of_Baium_Spawn;
    public static int Random_Of_Baium_Spawn;
    public static int Interval_Of_Core_Spawn;
    public static int Random_Of_Core_Spawn;
    public static int Interval_Of_Orfen_Spawn;
    public static int Random_Of_Orfen_Spawn;
    public static int Interval_Of_QueenAnt_Spawn;
    public static int Random_Of_QueenAnt_Spawn;
    public static int Interval_Of_Zaken_Spawn;
    public static int Random_Of_Zaken_Spawn;
    public static int Interval_Of_Sailren_Spawn;
    public static int Random_Of_Sailren_Spawn;
    public static int Interval_Of_Baylor_Spawn;
    public static int Random_Of_Baylor_Spawn;
    public static int Interval_Of_Frintezza_Spawn;
    public static int Random_Of_Frintezza_Spawn;
    
    public static void loadconfig()
    {
    	InputStream is = null;
    	if (load == 1)
    		return;
    	
    	 try 
    	 {
             Properties grandbossSettings    = new Properties();
             is                           = new FileInputStream(new File(GRANDBOSS_CONFIG_FILE));
             grandbossSettings.load(is);
             
             Antharas_Wait_Time     = Integer.parseInt(grandbossSettings.getProperty("AntharasWaitTime","10"));
             if (Antharas_Wait_Time < 3 || Antharas_Wait_Time > 60) Antharas_Wait_Time = 10;
             Antharas_Wait_Time = Antharas_Wait_Time * 60000;
			 
             Valakas_Wait_Time     = Integer.parseInt(grandbossSettings.getProperty("ValakasWaitTime","10"));
             if (Valakas_Wait_Time < 3 || Valakas_Wait_Time > 60) Valakas_Wait_Time = 10;
             Valakas_Wait_Time = Valakas_Wait_Time * 60000;
			 
             Interval_Of_Antharas_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfAntharasSpawn","192"));
             if (Interval_Of_Antharas_Spawn < 1 || Interval_Of_Antharas_Spawn > 192) Interval_Of_Antharas_Spawn = 192;
             Interval_Of_Antharas_Spawn = Interval_Of_Antharas_Spawn * 3600000;
			 
             Random_Of_Antharas_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfAntharasSpawn","145"));
             if (Random_Of_Antharas_Spawn < 1 || Random_Of_Antharas_Spawn > 192) Random_Of_Antharas_Spawn = 145;
             Random_Of_Antharas_Spawn = Random_Of_Antharas_Spawn * 3600000;
			 
             Interval_Of_Valakas_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfValakasSpawn","192"));
             if (Interval_Of_Valakas_Spawn < 1 || Interval_Of_Valakas_Spawn > 192) Interval_Of_Valakas_Spawn = 192;
             Interval_Of_Valakas_Spawn = Interval_Of_Valakas_Spawn * 3600000;
			 
             Random_Of_Valakas_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfValakasSpawn","145"));
             if (Random_Of_Valakas_Spawn < 1 || Random_Of_Valakas_Spawn > 192) Random_Of_Valakas_Spawn = 145;
             Random_Of_Valakas_Spawn = Random_Of_Valakas_Spawn * 3600000;
			 
             Interval_Of_Baium_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfBaiumSpawn","121"));
             if (Interval_Of_Baium_Spawn < 1 || Interval_Of_Baium_Spawn > 192) Interval_Of_Baium_Spawn = 121;
             Interval_Of_Baium_Spawn = Interval_Of_Baium_Spawn * 3600000;
			 
             Random_Of_Baium_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfBaiumSpawn","8"));
             if (Random_Of_Baium_Spawn < 1 || Random_Of_Baium_Spawn > 192) Random_Of_Baium_Spawn = 8;
             Random_Of_Baium_Spawn = Random_Of_Baium_Spawn * 3600000;
			 
             Interval_Of_Core_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfCoreSpawn","27"));
             if (Interval_Of_Core_Spawn < 1 || Interval_Of_Core_Spawn > 192) Interval_Of_Core_Spawn = 27;
             Interval_Of_Core_Spawn = Interval_Of_Core_Spawn * 3600000;
			 
             Random_Of_Core_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfCoreSpawn","47"));
             if (Random_Of_Core_Spawn < 1 || Random_Of_Core_Spawn > 192) Random_Of_Core_Spawn = 47;
             Random_Of_Core_Spawn = Random_Of_Core_Spawn * 3600000;
			 
             Interval_Of_Orfen_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfOrfenSpawn","28"));
             if (Interval_Of_Orfen_Spawn < 1 || Interval_Of_Orfen_Spawn > 192) Interval_Of_Orfen_Spawn = 28;
             Interval_Of_Orfen_Spawn = Interval_Of_Orfen_Spawn * 3600000;
			 
             Random_Of_Orfen_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfOrfenSpawn","41"));
             if (Random_Of_Orfen_Spawn < 1 || Random_Of_Orfen_Spawn > 192) Random_Of_Orfen_Spawn = 41;
             Random_Of_Orfen_Spawn = Random_Of_Orfen_Spawn * 3600000;
			 
             Interval_Of_QueenAnt_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfQueenAntSpawn","19"));
             if (Interval_Of_QueenAnt_Spawn < 1 || Interval_Of_QueenAnt_Spawn > 192) Interval_Of_QueenAnt_Spawn = 19;
             Interval_Of_QueenAnt_Spawn = Interval_Of_QueenAnt_Spawn * 3600000;
			 
             Random_Of_QueenAnt_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfQueenAntSpawn","35"));
             if (Random_Of_QueenAnt_Spawn < 1 || Random_Of_QueenAnt_Spawn > 192) Random_Of_QueenAnt_Spawn = 35;
             Random_Of_QueenAnt_Spawn = Random_Of_QueenAnt_Spawn * 3600000;
			 
             Interval_Of_Zaken_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfZakenSpawn","19"));
             if (Interval_Of_Zaken_Spawn < 1 || Interval_Of_Zaken_Spawn > 192) Interval_Of_Zaken_Spawn = 19;
             Interval_Of_Zaken_Spawn = Interval_Of_Zaken_Spawn * 3600000;
			 
             Random_Of_Zaken_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfZakenSpawn","35"));
             if (Random_Of_Zaken_Spawn < 1 || Random_Of_Zaken_Spawn > 192) Random_Of_Zaken_Spawn = 35;
             Random_Of_Zaken_Spawn = Random_Of_Zaken_Spawn * 3600000;
			 
             Interval_Of_Sailren_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfSailrenSpawn","12"));
             if (Interval_Of_Sailren_Spawn < 1 || Interval_Of_Sailren_Spawn > 192) Interval_Of_Sailren_Spawn = 12;
             Interval_Of_Sailren_Spawn = Interval_Of_Sailren_Spawn * 3600000;
			 
             Random_Of_Sailren_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfSailrenSpawn","24"));
             if (Random_Of_Sailren_Spawn < 1 || Random_Of_Sailren_Spawn > 192) Random_Of_Sailren_Spawn = 24;
             Random_Of_Sailren_Spawn = Random_Of_Sailren_Spawn * 3600000;
			 
             Interval_Of_Baylor_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfBaylorSpawn","12"));
             if (Interval_Of_Baylor_Spawn < 1 || Interval_Of_Baylor_Spawn > 192) Interval_Of_Baylor_Spawn = 12;
             Interval_Of_Baylor_Spawn = Interval_Of_Baylor_Spawn * 3600000;
			 
             Random_Of_Baylor_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfBaylorSpawn","24"));
             if (Random_Of_Baylor_Spawn < 1 || Random_Of_Baylor_Spawn > 192) Random_Of_Baylor_Spawn = 24;
             Random_Of_Baylor_Spawn = Random_Of_Baylor_Spawn * 3600000;
			
             Interval_Of_Frintezza_Spawn     = Integer.parseInt(grandbossSettings.getProperty("IntervalOfFrintezzaSpawn","121"));
             if (Interval_Of_Frintezza_Spawn < 1 || Interval_Of_Frintezza_Spawn > 192) Interval_Of_Frintezza_Spawn = 121;
             Interval_Of_Frintezza_Spawn = Interval_Of_Frintezza_Spawn * 3600000;
			 
             Random_Of_Frintezza_Spawn     = Integer.parseInt(grandbossSettings.getProperty("RandomOfFrintezzaSpawn","8"));
             if (Random_Of_Frintezza_Spawn < 1 || Random_Of_Frintezza_Spawn > 192) Random_Of_Frintezza_Spawn = 8;
             Random_Of_Frintezza_Spawn = Random_Of_Frintezza_Spawn * 3600000;
         }
         catch (Exception e)
         {
             e.printStackTrace();
             throw new Error("Failed to Load "+GRANDBOSS_CONFIG_FILE+" File.");
         }
         
         
         load = 1;
    
    }
}