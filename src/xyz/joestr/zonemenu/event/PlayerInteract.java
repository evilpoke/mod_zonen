package xyz.joestr.zonemenu.event;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.joestr.zonemenu.ZoneMenu;
import xyz.joestr.zonemenu.util.ActionBar;

public class PlayerInteract implements Listener {
	
	private ZoneMenu plugin;
	
	public PlayerInteract(ZoneMenu zonemenu) {
		
		this.plugin = zonemenu;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if((player.getInventory().getItemInMainHand().getType() == Material.STICK) && (this.plugin.Tool.get(player.getName()) == "find")) {
			
			String find1 = "";
			String find2 = "";
			
			if((event.getAction() == Action.LEFT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				
				if(plugin.FindLocations.containsKey(player.getName())) {
					
					if(((Location)plugin.FindLocations.get(player.getName())).equals(event.getClickedBlock().getLocation())) { event.setCancelled(true); return; }
				}
				
				this.plugin.FindLocations.put(player.getName(), event.getClickedBlock().getLocation());
				event.setCancelled(true);
				ApplicableRegionSet regiononloc = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(event.getClickedBlock().getLocation());
				
				for(ProtectedRegion region : regiononloc) {
					
					if(find2 != "") {
						
						find2 = find2 + ", ";
					}
					
					find2 = find2 + region.getId();
				}
				
				if(find2 == "") {
					
					find1 = (String)plugin.yd.Map().get("event_find_no");
				
				} else if(find2.contains(",")) {
					
					find1 = (String)plugin.yd.Map().get("event_find_multi");
				} else {
					
					find1 = (String)plugin.yd.Map().get("event_find");
				}
				
				ActionBar actiobarmessage = new ActionBar(this.plugin.ColorCode("&", find1 + find2));
				actiobarmessage.sendToPlayer(player);
			}
		}
		
		if((player.getInventory().getItemInMainHand().getType() == Material.STICK) && (this.plugin.Tool.get(player.getName()) == "sign")) {
			
			String sign1 = "";
			
			if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				if(plugin.FirstLocations.containsKey(player.getName())) {
					
					if(((Location)plugin.FirstLocations.get(player.getName())).equals(event.getClickedBlock().getLocation())) { event.setCancelled(true); return; }
				}
				
				this.plugin.Worlds.put(player.getName(), player.getWorld());
				this.plugin.FirstLocations.put(player.getName(), event.getClickedBlock().getLocation());
				event.setCancelled(true);
				World playerworld = (World)this.plugin.Worlds.get(player.getName());
				Location playerpos1 = (Location)this.plugin.FirstLocations.get(player.getName());
				Location playerpos2 = (Location)this.plugin.SecondLocations.get(player.getName());
				playerpos1.setY(0);
				
				this.plugin.resetCorner(player, this.plugin.corner1);
				this.plugin.createBeaconCorner(playerpos1, player, this.plugin.corner1, (byte)1);
				this.plugin.laterSet(event.getClickedBlock().getLocation(), player, Material.STAINED_GLASS, (byte)1);
				
				if((playerworld != null) && (playerpos1 != null) && (playerpos2 != null)) {
					
					this.plugin.resetCorner(player, this.plugin.corner2);
					this.plugin.createBeaconCorner(playerpos2, player, this.plugin.corner2, (byte)2);
					Location loc = playerpos1.clone();
					loc.setX(playerpos2.getX());
					this.plugin.resetCorner(player, this.plugin.corner3);
					this.plugin.createBeaconCorner(loc, player, this.plugin.corner3, (byte)0);
					loc = playerpos1.clone();
					loc.setZ(playerpos2.getZ());
					this.plugin.resetCorner(player, this.plugin.corner4);
					this.plugin.createBeaconCorner(loc, player, this.plugin.corner4, (byte)0);
					
					CuboidSelection cs = new CuboidSelection(playerworld, playerpos1, playerpos2);
					this.plugin.getWorldEdit().setSelection(player, cs);
					sign1 = (String)plugin.yd.Map().get("event_sign_first") + (String)((String)plugin.yd.Map().get("event_sign_area")).replace("{0}", Integer.toString(this.plugin.getWorldEdit().getSelection(player).getLength() * this.plugin.getWorldEdit().getSelection(player).getWidth()));
				} else {
					
					sign1 = (String)plugin.yd.Map().get("event_sign_first");
				}
				
				ActionBar actionbarmessage = new ActionBar(this.plugin.ColorCode("&", sign1));
				actionbarmessage.sendToPlayer(player);
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				if(plugin.SecondLocations.containsKey(player.getName())) {
					
					if(((Location)plugin.SecondLocations.get(player.getName())).equals(event.getClickedBlock().getLocation())) { event.setCancelled(true); return; }
				}
				
				this.plugin.Worlds.put(player.getName(), player.getWorld());
				this.plugin.SecondLocations.put(player.getName(), event.getClickedBlock().getLocation());
				event.setCancelled(true);
				World playerworld = (World)this.plugin.Worlds.get(player.getName());
				Location playerpos1 = (Location)this.plugin.FirstLocations.get(player.getName());
				Location playerpos2 = (Location)this.plugin.SecondLocations.get(player.getName());
				playerpos2.setY(255);
				//playerpos2.setY(player.getWorld().getMaxHeight());
				
				this.plugin.resetCorner(player, this.plugin.corner2);
				this.plugin.createBeaconCorner(playerpos2, player, this.plugin.corner2, (byte)2);
				this.plugin.laterSet(event.getClickedBlock().getLocation(), player, Material.STAINED_GLASS, (byte)2);
				
				if((playerworld != null) && (playerpos1 != null) && (playerpos2 != null)) {
					
					this.plugin.resetCorner(player, this.plugin.corner1);
					this.plugin.createBeaconCorner(playerpos1, player, this.plugin.corner1, (byte)1);
					Location loc = playerpos1.clone();
					loc.setX(playerpos2.getX());
					this.plugin.resetCorner(player, this.plugin.corner3);
					this.plugin.createBeaconCorner(loc, player, this.plugin.corner3, (byte)0);
					loc = playerpos1.clone();
					loc.setZ(playerpos2.getZ());
					this.plugin.resetCorner(player, this.plugin.corner4);
					this.plugin.createBeaconCorner(loc, player, this.plugin.corner4, (byte)0);
					
					CuboidSelection cs = new CuboidSelection(playerworld, playerpos1, playerpos2);
					this.plugin.getWorldEdit().setSelection(player, cs);
					sign1 = (String)plugin.yd.Map().get("event_sign_second") + (String)((String)plugin.yd.Map().get("event_sign_area")).replace("{0}", Integer.toString(this.plugin.getWorldEdit().getSelection(player).getLength() * this.plugin.getWorldEdit().getSelection(player).getWidth()));
				} else {
					
					sign1 = (String)plugin.yd.Map().get("event_sign_second");
				}
				
				ActionBar actionbarmessage = new ActionBar(this.plugin.ColorCode("&", sign1));
				actionbarmessage.sendToPlayer(player);
			}
		}
	}
}