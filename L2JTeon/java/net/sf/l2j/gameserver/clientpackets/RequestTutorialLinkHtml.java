package net.sf.l2j.gameserver.clientpackets;

public class RequestTutorialLinkHtml extends L2GameClientPacket
{
    // private static Logger _log =
    // Logger.getLogger(RequestTutorialLinkHtml.class.getName());
    String _bypass = null;

    @Override
    protected void readImpl()
    {
	_bypass = readS();
    }

    @Override
    protected void runImpl()
    {
    }

    @Override
    public String getType()
    {
	return "[C] 7B RequestTutorialLinkHtml";
    }
}
