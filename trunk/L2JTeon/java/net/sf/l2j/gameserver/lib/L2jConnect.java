package net.sf.l2j.gameserver.lib;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import net.sf.l2j.Config;

public class L2jConnect
{
    IRCConnection _server = new IRCConnection(Config.IRC_HOSTNAME, Config.IRC_PORT, 6669, null, Config.IRC_USERNAME, Config.IRC_USERREALNAME, Config.IRC_USERMAIL);

    public void doSendMsg() throws IOException
    {
	String target = Config.IRC_CHANNEL;
	int endlos = 1;
	while (endlos == 1)
	{
	    File file = new File(Config.IRC_IGTOIRC_FILE);
	    if (file.exists())
	    {
		LineNumberReader f;
		String line = "";
		try
		{
		    f = new LineNumberReader(new FileReader(Config.IRC_IGTOIRC_FILE));
		    while ((line = f.readLine()) != null)
		    {
			line = f.readLine();
			if (line.contains(Config.IRC_KEYWORDTOIRC))
			{
			    String text = line.replace(Config.IRC_KEYWORDTOIRC, "");
			    _server.doPrivmsg(target, text);
			    file.delete();
			}
		    }
		    f.close();
		    file.delete();
		} catch (IOException e)
		{
		    System.out.println("error reading file");
		}
	    }
	}
    }

    public void L2jConnect()
    {
	_server.addIRCEventListener(new Listener());
	_server.setDaemon(true);
	_server.setColors(false);
	_server.setPong(true);
	try
	{
	    _server.connect();
	    _server.doJoin(Config.IRC_CHANNEL);
	} catch (IOException ioexc)
	{
	    ioexc.printStackTrace();
	}
    }
}
