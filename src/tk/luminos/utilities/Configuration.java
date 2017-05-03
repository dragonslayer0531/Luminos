package tk.luminos.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Load and set engine configuration information
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Configuration {
	
	protected Map<String, Integer> settings;
	protected Version version;
	
	protected enum Version {
		LUMINOS_0_0_1
	}
	
	/**
	 * Gets value of setting
	 * 
	 * @param setting	setting name
	 * @return			value
	 */
	public Integer getValue(String setting)
	{
		return settings.get(setting);
	}
	
	/**
	 * Sets value of setting
	 * 
	 * @param setting	setting name
	 * @param value		value
	 */
	public void setValue(String setting, Integer value)
	{
		settings.put(setting, value);
	}
	
	/**
	 * Loads settings to configuration
	 * 
	 * @param configurationFile		config file
	 * @return						configuration object
	 * @throws IOException			thrown if cannot find/open file
	 */
	public static Configuration loadSettings(String configurationFile) throws IOException {
		FileReader fr = new FileReader(configurationFile);
		BufferedReader br = new BufferedReader(fr);		
		
		Configuration config = new Configuration();
		config.settings = new LinkedHashMap<String, Integer>();
		
		String line = br.readLine();
		
		if (!line.equals("luminos.configuration"))
		{
			br.close();
			throw new RuntimeException("Luminos configuration file invalid.  Bad header.");
		}
		
		line = br.readLine();
		config.version = Version.valueOf(line);
		
		if (!(line = br.readLine()).equals("settings"))
		{
			br.close();
			throw new RuntimeException("Luminos configuration file invalid. Corrupt.");
		}
		
		while ((line = br.readLine()) != null) {
			String[] settings = line.split("/");
			config.settings.put(settings[0], Integer.parseInt(settings[1]));
		}
		
		br.close();
		
		return config;
	}

	/**
	 * Saves settings to file
	 * 
	 * @param configurationTarget		target file
	 * @throws IOException				thrown if cannot make/write to file
	 */
	public void saveSettings(String configurationTarget) throws IOException {
		PrintWriter pw = new PrintWriter(configurationTarget);
		pw.write("luminos.configuration" + System.lineSeparator());
		pw.write(version.toString() + System.lineSeparator());
		pw.write("settings" + System.lineSeparator());
		int i = 0; 
		for (String str : this.settings.keySet())
		{
			pw.write(str + "/" + this.settings.get(str));
			if (i != this.settings.keySet().size() - 1)
			{
				pw.write(System.lineSeparator());
			}
			i++;
		}
		pw.close();
	}
	
}
