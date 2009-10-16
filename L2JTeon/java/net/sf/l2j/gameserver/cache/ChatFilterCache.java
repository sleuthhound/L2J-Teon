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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;

/**
 * @author schursin (L2JOneo Dev Team)
 */
public class ChatFilterCache
{
	private static Logger _log = Logger.getLogger(ChatFilterCache.class.getName());
	private static ChatFilterCache _instance;
	private String[] _wordsCache;
	private String[] _ignoreCache;
	private String[] _pretextCache;
	private String[] _wordsContainer;

	public static ChatFilterCache getInstance()
	{
		if (_instance == null)
			_instance = new ChatFilterCache();
		return _instance;
	}

	public ChatFilterCache()
	{
		reloadCache();
	}

	// reload filter world from database
	public static void reload()
	{
		_instance.reloadCache();
	}

	private void reloadCache()
	{
		_wordsCache = loadCache("chat_filter_words", getRowCount("chat_filter_words"));
		_ignoreCache = loadCache("chat_filter_ignore", getRowCount("chat_filter_ignore"));
		_pretextCache = loadCache("chat_filter_pretext", getRowCount("chat_filter_pretext"));
		_log.info("ChatFilterCache: Loaded");
	}

	// get count of rows in table
	private int getRowCount(String tableName)
	{
		java.sql.Connection con = null;
		int counter = 0;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT COUNT(*) as 'count' FROM " + tableName);
			ResultSet rset = statement.executeQuery();
			rset.next();
			counter = rset.getInt("count");
		}
		catch (SQLException e)
		{
			_log.warning("Could not get row count from DB: " + e);
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
		return counter;
	}

	// fill cache
	private String[] loadCache(String tableName, int cacheLength)
	{
		String[] result = new String[cacheLength];
		int localCounter = 1;
		java.sql.Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT `word` FROM " + tableName);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				result[localCounter - 1] = rset.getString("word");
				localCounter++;
			}
		}
		catch (SQLException e)
		{
			_log.warning("Could not load cache data from DB: " + e);
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
		return result;
	}

	/** Filter Chat Functions * */
	public static String filterText(String textData)
	{
		return _instance.filterIt(textData);
	}

	// delete from text special chars and nums
	private String deleteSpecialChars(String text)
	{
		String chars = "`1234567890-=~!@#$%^&*()_+<>,./?\\|{][}\"'";
		String result = "";
		for (int i = 0; i < text.length(); i++)
		{
			boolean foundIt = false;
			for (int n = 0; n < chars.length(); n++)
				if (chars.charAt(n) == text.charAt(i))
					foundIt = true;
			if (!foundIt)
				result += text.charAt(i);
		}
		return result;
	}

	// delete double chars
	private String deleteDoubleChars(String text)
	{
		String result = "";
		for (int i = 0; i < text.length() - 1; i++)
			if (text.charAt(i) != text.charAt(i + 1))
				result += text.charAt(i);
		if (text.length() > 0)
			result += text.charAt(text.length() - 1);
		return result;
	}

	// replace pretexts in text by spaces
	private String deletePreText(String text)
	{
		text = " " + text + " ";
		for (int i = 0; i < _pretextCache.length; i++)
			text = text.replace(" " + _pretextCache[i] + " ", " ");
		return text;
	}

	// split text by word with min length
	private void splitByWords(String text, int minLen)
	{
		String[] wordArr = text.split(" ");
		String[] result0 = new String[wordArr.length + 1];
		int cellId = 0;
		for (int i = 0; i < wordArr.length; i++)
		{
			if (wordArr[i].length() > minLen)
				cellId += 1;
			result0[cellId] += wordArr[i];
		}
		String[] result = new String[cellId + 1];
		for (int i = 0; i <= cellId; i++)
			result[i] = result0[i];
		// save result
		_wordsContainer = result;
	}

	// delete ignore words from wordsContainer
	private void deleteIgnores()
	{
		for (int i = 0; i < _ignoreCache.length; i++)
			for (int n = 0; n < _wordsContainer.length; n++)
				_wordsContainer[n] = _wordsContainer[n].replace(_ignoreCache[i], " ");
	}

	// search for bad words
	private boolean doWordsFilter()
	{
		for (int n = 0; n < _wordsContainer.length; n++)
			if (_wordsContainer[n] != "")
			{
				String word = _wordsContainer[n];
				for (int i = 0; i < _wordsCache.length; i++)
					word = word.replace(_wordsCache[i], "");
				if (_wordsContainer[n] != word)
					return true;
			}
		return false;
	}

	private String filterIt(String text)
	{
		if (!Config.USE_POWERFULL_CHAT_FILTER)
		// simple chat filter method
		{
			for (int i = 0; i < _wordsCache.length; i++)
				text = text.replaceAll(_wordsCache[i], Config.CHAT_FILTER_CHARS);
		}
		// powerfull chat filter method
		else
		{
			String originalText = text;
			// work with text
			text = text.trim();
			text = text.toLowerCase() + "x"; // "x" is a temp null poiner
			// exeption fix
			text = deleteSpecialChars(text);
			text = deleteDoubleChars(text);
			text = deletePreText(text);
			// splite line by words
			splitByWords(text, 2);
			// work with words
			deleteIgnores();
			boolean badFound = doWordsFilter();
			// result
			text = badFound ? "bad text found" : originalText;
		}
		return text;
	}
	/** Add Word For Filter Into Database * */
	/*
	 * GameServer not use this functions now... will be added soon... papient... DO NOT DELETE THIS FUNCTIONS FROM THE SOURCE - WORK IS IN PROGRESS!!! public static void addWord(String textData) { _instance.addWordInDB(textData); } private void addWordIntoDB(String textData) { java.sql.Connection con = null; try { con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement =
	 * con.prepareStatement("INSERT INTO chat_filter (filterWord) VALUES (?)"); statement.setString(1, textData); statement.executeUpdate(); statement.close(); } catch (SQLException e) { _log.warning("Could not add new filter word into DB: " + e); } finally { try { con.close(); } catch (Exception e) {} } }
	 */
}
