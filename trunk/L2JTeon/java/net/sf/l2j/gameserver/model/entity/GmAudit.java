package net.sf.l2j.gameserver.model.entity;

import java.sql.PreparedStatement;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;

/**
 * GM EditManager. Will insert a SQL message upon call.
 * 
 * @author DaRkRaGe
 */
public class GmAudit
{
    /**
     * Inserts the comment into the DataBase.
     * 
     * @param GmName
     * @param GmId
     * @param Target
     * @param Action
     */
    public GmAudit(String GmName, int GmId, String Target, String Action)
    {
	if (Config.GMAUDIT)
	{
	    java.sql.Connection con = null;
	    try
	    {
		con = L2DatabaseFactory.getInstance().getConnection();
		PreparedStatement statement = con.prepareStatement("INSERT INTO `gm_edit` (`id`,`GM_Name`,`GM_ID`,`Edited_Char`,`Action`) " + "VALUES (NULL,'" + GmName + "','" + GmId + "','" + Target + "','" + Action + "')");
		// Information about GM actions.
		statement.execute();
		statement.close();
	    } catch (Exception e)
	    {
		_log.warning("Error in the informative sentence in GmAudit.java");
	    } finally
	    {
		try
		{
		    con.close();
		} catch (Exception e)
		{
		    // meh
		}
	    }
	}
    }

    private static Logger _log = Logger.getLogger(Config.class.getName());
}