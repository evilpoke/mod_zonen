package xyz.joestr.zonemenu;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.joestr.zonemenu.util.YMLDelegate;
import xyz.joestr.zonemenu.command.ZoneCommand;
import xyz.joestr.zonemenu.event.PlayerChangedWorld;
import xyz.joestr.zonemenu.event.PlayerQuit;
import xyz.joestr.zonemenu.event.PlayerInteract;
import xyz.joestr.zonemenu.tabcomplete.ZoneTabComplete;

public class ZoneMenu extends JavaPlugin implements Listener {
	
	public YMLDelegate yd = new YMLDelegate(this, "config", "config.yml");
	
	public WorldEditPlugin we;
	public WorldGuardPlugin wg;
	
	public HashMap<String, Location> FindLocations = new HashMap<String, Location>();
	public HashMap<String, World> Worlds = new HashMap<String, World>();
	public HashMap<String, Location> FirstLocations = new HashMap<String, Location>();
	public HashMap<String, Location> SecondLocations = new HashMap<String, Location>();
	public HashMap<String, String> Tool = new HashMap<String, String>();
	//public HashMap<String, Object> Config = new HashMap<String, Object>();
	public HashMap<String, List<Location>> corner1 = new HashMap<String, List<Location>>();
	public HashMap<String, List<Location>> corner2 = new HashMap<String, List<Location>>();
	public HashMap<String, List<Location>> corner3 = new HashMap<String, List<Location>>();
	public HashMap<String, List<Location>> corner4 = new HashMap<String, List<Location>>();
	
	public void onEnable() {
		
		Bukkit.getServer().getConsoleSender().sendMessage("[ZoneMenu] Version: " + Bukkit.getServer().getVersion());
		Bukkit.getServer().getConsoleSender().sendMessage("[ZoneMenu] Bukkit Version: " + Bukkit.getServer().getBukkitVersion());
		if(!yd.Exist()) { yd.Create(); }
		
		yd.Load();
		
		new PlayerInteract(this);
		new PlayerQuit(this);
		new PlayerChangedWorld(this);
		
		getCommand("zone").setExecutor(new ZoneCommand(this));
		getCommand("zone").setTabCompleter(new ZoneTabComplete(this));
		
		we = getWorldEdit();
		wg = getWorldGuard();
		
		if(yd.EntryCheck()) { yd.Save(); }
		
		//Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[ZoneMenu] Activated.");
	}
	
	public void onDisable() {
		
		//Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[ZoneMenu] Deactivated.");
	}
	
	public WorldEditPlugin getWorldEdit() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		
		if((plugin == null) || (!(plugin instanceof WorldEditPlugin))) {
			
			Bukkit.getServer().getConsoleSender().sendMessage("[ZoneMenu] WorldEdit not initialized.");
			Bukkit.getPluginManager().disablePlugin(this);
			return null;
		}
		
		return (WorldEditPlugin)plugin;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		
		if((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
			
			Bukkit.getServer().getConsoleSender().sendMessage("[ZoneMenu] WorldGuard not initialized.");
			Bukkit.getPluginManager().disablePlugin(this);
			return null;
		}
		
		return (WorldGuardPlugin)plugin;
	}
	
	public String ColorCode(String s, String t) { return t.replace(s, "§"); }
	
	public String AlternativeColorCode(String s, String t) { return t.replace("§", s); }
	
	@SuppressWarnings("deprecation")
	public void resetCorner(Player player, Map<String, List<Location>> map) {
		
		if(!map.containsKey(player.getName())) { return; }
		
		for(Location l : map.get(player.getName())) {
			
			player.sendBlockChange(l, player.getWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).getType(), player.getWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).getData());
			map.remove(l);
		}
	}
	
	//From: https://bukkit.org/threads/lib-beacon-creator.179399/ (modified)
	//--- start
	@SuppressWarnings("deprecation")
	public void createBeaconCorner(Location location, Player player, Map<String, List<Location>> map, byte glasscolor) {
		
		List<Location> locations = new ArrayList<Location>();
		int x = location.getBlockX();
		int y = 0;
		int z = location.getBlockZ();
	    World world = location.getWorld();
	    
	    /*
	     * 3x3 area on layer 0
	     * 1 Beacon at layer 1
	     * 1 Staindes glass pane to passthrough blocks
	     */
	    locations.add(world.getBlockAt(x, 1, z).getLocation());
	    player.sendBlockChange(world.getBlockAt(x, 1, z).getLocation(), Material.BEACON, (byte)0);
	    
	    for(int i = 2; i <= 255; i++) {
	    	
	    	if(world.getBlockAt(x, i, z).getType() != Material.AIR) {
	    		
	    		locations.add(world.getBlockAt(x, i, z).getLocation());
	    		player.sendBlockChange(world.getBlockAt(x, i, z).getLocation(), Material.STAINED_GLASS, glasscolor);
	    	}
	    }
	    
	    for(int xPoint = x-1; xPoint <= x+1 ; xPoint++) {
	    	
	        for(int zPoint = z-1 ; zPoint <= z+1; zPoint++) {
	        	
	        	locations.add(world.getBlockAt(xPoint, y, zPoint).getLocation());
	        	player.sendBlockChange(world.getBlockAt(xPoint, y, zPoint).getLocation(), Material.DIAMOND_BLOCK, (byte)0);
	        }
	    }
	    
	    map.put(player.getName(), locations);
	}
	//--- end
	
	@SuppressWarnings("deprecation")
	public void laterSet(Location location, Player player, Material material, byte glasscolor) {
		
		//10 ticks delay time (just for safety)
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
			public void run() { player.sendBlockChange(location, material, glasscolor); }
		}, 10L);
	}
	
	public int difference(int num1, int num2) { return num1 > num2 ? num1 - num2 : num2 - num1; }
	
	public ProtectedRegion getRegion(Player player) throws InterruptedException, ExecutionException {
		
		player.sendMessage(ColorCode("&", (String)yd.Map().get("head")));
		player.sendMessage(ColorCode("&", (String)yd.Map().get("zone_id_search")));
		
		ProtectedRegion p = null;
        ExecutorService executor = Executors.newFixedThreadPool(1);
 
        FutureTask<ProtectedRegion> futureTask = new FutureTask<ProtectedRegion>(new Callable<ProtectedRegion>() {
        	
            @Override
            public ProtectedRegion call() {
                
            	ProtectedRegion p = null;
            	
            	for(String s : wg.getRegionManager(player.getWorld()).getRegions().keySet()) {
					
					if(wg.getRegionManager(player.getWorld()).getRegions().get(s).isOwner(wg.wrapPlayer(player))) {
						
						p = wg.getRegionManager(player.getWorld()).getRegions().get(s);
					}
				}
            	
            	return p;
            }
        });
        
        executor.execute(futureTask);
 
        while(!futureTask.isDone()) { try { Thread.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); } }
        
        p = futureTask.get();
        executor.shutdown();
 
        return p;
    }
}
