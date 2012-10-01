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
package net.sf.l2j.gameserver.cache;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.commons.io.UnicodeReader;
import net.sf.l2j.commons.io.filters.HtmFilter;
import net.sf.l2j.gameserver.util.Util;

/**
 * @author Layane, reworked by Java-man
 */
public class HtmCache
{
	private final static Logger _log = Logger.getLogger(HtmCache.class.getName());
	
	private final static TIntObjectHashMap<String> _cache = new TIntObjectHashMap<>();
	
	private final static FileFilter htmFilter = new HtmFilter();
	
	public static HtmCache getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected HtmCache()
	{
		reload();
	}
	
	public void reload()
	{
		_cache.clear();
		_log.info("Cache[HTML]: Running lazy cache");
	}
	
	public void reloadPath(File f)
	{
		parseDir(f);
		_log.info("Cache[HTML]: Reloaded specified path.");
	}
	
	private void parseDir(File dir)
	{
		final File[] files = dir.listFiles(htmFilter);
		
		for (File file : files)
		{
			if (!file.isDirectory())
				loadFile(file);
			else
				parseDir(file);
		}
	}
	
	public String loadFile(File file)
	{
		final String relpath = Util.getRelativePath(Config.DATAPACK_ROOT, file);
		final int hashcode = relpath.hashCode();
		
		if (file.exists() && htmFilter.accept(file) && !file.isDirectory())
		{
			try (FileInputStream fis = new FileInputStream(file); UnicodeReader ur = new UnicodeReader(fis, "UTF-8"); BufferedReader br = new BufferedReader(ur))
			{
				StringBuilder sb = new StringBuilder();
				String s;
				
				while ((s = br.readLine()) != null)
					sb.append(s).append('\n');
				
				String content = sb.toString().replaceAll("\r\n", "\n");
				sb = null;
				
				_cache.put(hashcode, content);
				
				return content;
			}
			catch (Exception e)
			{
				_log.warning("problem with htm file " + e);
			}
		}
		
		return null;
	}
	
	public String getHtmForce(String path)
	{
		String content = getHtm(path);
		
		if (content == null)
		{
			content = "<html><body>My text is missing:<br>" + path + "</body></html>";
			_log.warning("Cache[HTML]: Missing HTML page: " + path);
		}
		
		return content;
	}
	
	public String getHtm(String path)
	{
		if (path == null || path.isEmpty())
			return ""; // avoid possible NPE
			
		final int hashCode = path.hashCode();
		String content = _cache.get(hashCode);
		
		if (content == null)
			content = loadFile(new File(Config.DATAPACK_ROOT, path));
		
		return content;
	}
	
	public boolean contains(String path)
	{
		return _cache.containsKey(path.hashCode());
	}
	
	/**
	 * Check if an HTM exists and can be loaded
	 * @param path The path to the HTM
	 * @return true if the HTM can be loaded.
	 */
	public boolean isLoadable(String path)
	{
		final File file = new File(path);
		
		return file.exists() && htmFilter.accept(file) && !file.isDirectory();
	}
	
	private static class SingletonHolder
	{
		protected static final HtmCache _instance = new HtmCache();
	}
}