package net.sf.l2j.gameserver;

import java.util.logging.Logger;

import net.sf.l2j.Config;

public class L2JTeon 
{
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());
	/**
	 *  L2JTeon Info
	 */
	public static void L2JTeon()
	{
		_log.info("-----------------------------------------------------");
		_log.info("           Developers: Maxi56, Meyknho               ");
		_log.info("-----------------------------------------------------");
		_log.info("#      # # #   # # #   # # #  # # #    ####   #   #  ");
		_log.info("#          #     #       #    #       #    #  ##  #  ");
		_log.info("#        #       #       #    # # #   #    #  # # #  ");
		_log.info("#      #      #  #       #    #       #    #  #  ##  ");
		_log.info("# # #  # # #  ####       #    # # #    ####   #   #  ");
		_log.info("-----------------------------------------------------");
		_log.info("   L2J Teon Core Version: "+Config.SERVER_VERSION     );
		_log.info(" L2J Teon DataPack Version: "+Config.DATAPACK_VERSION );
		_log.info("               Copyright 2009-2010                   ");
		_log.info("-----------------------------------------------------");
	}
}
