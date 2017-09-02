package xyz.joestr.zonemenu.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YMLDelegate {
	
	Plugin plugin = null;
	Map<String, Object> hashMap = new HashMap<String, Object>();
	String configurationSection = "";
	String fileName = "";
	
	public YMLDelegate(Plugin plugin, String configurationSection, String fileName) {
		
		this.plugin = plugin;
		this.configurationSection = configurationSection;
		this.fileName = fileName;
	}
	
	public String getFileName() { return this.fileName; }
	
	@Deprecated
	public Map<String, Object> getMap() { return this.Map(); }
	
	@Deprecated
	public void setMap(Map<String, Object> map) { this.Map(map); }
	
	public Map<String, Object> Map() { return this.hashMap; }
	
	public void Map(Map<String, Object> map) { this.hashMap = map; }
	
	public void Load() {
		
	    try {
	    	
	    	FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), fileName));
	    	
	    	try {
	    		
	    		hashMap.putAll(fileConfiguration.getConfigurationSection(configurationSection).getValues(true));
	    	} catch (Exception exception) {
	    		
	    		exception.printStackTrace();
	        }
	    } catch (Exception exception) {
	    	
	        exception.printStackTrace();
	    }
	}
	
	public void Save() {

		try {
			
			FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), fileName));
			fileConfiguration.createSection(configurationSection, hashMap);
			fileConfiguration.save(new File(this.plugin.getDataFolder(), fileName));
		} catch (Exception exception) {
			
	        exception.printStackTrace();
		}
	}
	
	public void Reset() {
		
		try {
			
			this.plugin.saveResource(fileName, true);
		} catch(Exception exception) {
			
			exception.printStackTrace();
		}
		
		//To aplice the reset, we have to load the file again.
		Load();
	}
	
	public void Create() {
		
		try {
			
			this.plugin.saveResource(fileName, false);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public boolean Exist() {
		
		boolean returnVariable = false;
		
		try {
			
			File file = new File(this.plugin.getDataFolder(), fileName);
			
			if(file.exists()) {
				
				returnVariable = true;
	        }
		} catch (Exception exception) {
			
			exception.printStackTrace();
		}
		
		return returnVariable;
	}
	
	public boolean Check() {
		
		boolean returnVariable = true;
		
		try {
			
			Reader reader = new InputStreamReader(this.plugin.getResource(fileName), "UTF-8");
			for(String s : YamlConfiguration.loadConfiguration(reader).getConfigurationSection(configurationSection).getKeys(true)) {
								
				if(!this.hashMap.containsKey(s)) {
					
					returnVariable = false;
					break;
				}
			}
		} catch(Exception exception) {
			
			exception.printStackTrace();
		}
		
		return returnVariable;
	}
	
	public boolean EntryCheck() {
		
		boolean addedEntry = false;
		
		try {
			
			Reader reader = new InputStreamReader(this.plugin.getResource(fileName), "UTF-8");
			ConfigurationSection configurationsection = YamlConfiguration.loadConfiguration(reader).getConfigurationSection(configurationSection);
			for(String s : configurationsection.getKeys(true)) {
								
				if(!this.hashMap.containsKey(s)) {
					
					Bukkit.getLogger().log(Level.WARNING, "File: " + this.getFileName() + "; Missing entry: " + s + "; (using from default config)");
					this.hashMap.put(s, configurationsection.get(s));
					
					if(!addedEntry) { addedEntry = true; }
				}
			}
		} catch(Exception exception) {
			
			exception.printStackTrace();
		}
		
		return addedEntry;
	}
}
