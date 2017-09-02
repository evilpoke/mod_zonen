package xyz.joestr.zonemenu.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import xyz.joestr.zonemenu.ZoneMenu;

public class PlayerChangedWorld implements Listener {
	
	private ZoneMenu plugin;
	
	public PlayerChangedWorld(ZoneMenu zonemenu) {
		
		this.plugin = zonemenu;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onChangedWorld(PlayerChangedWorldEvent event) {
		
		//plugin.Tool.remove(event.getPlayer().getName());
		this.plugin.FindLocations.remove(event.getPlayer().getName());
		this.plugin.Worlds.remove(event.getPlayer().getName());
		this.plugin.FirstLocations.remove(event.getPlayer().getName());
		this.plugin.SecondLocations.remove(event.getPlayer().getName());
		//this.plugin.we.getSession(event.getPlayer()).getRegionSelector(plugin.we.getSession(event.getPlayer()).getSelectionWorld()).clear();
		this.plugin.resetCorner(event.getPlayer(), this.plugin.corner1);
		this.plugin.resetCorner(event.getPlayer(), this.plugin.corner2);
		this.plugin.resetCorner(event.getPlayer(), this.plugin.corner3);
		this.plugin.resetCorner(event.getPlayer(), this.plugin.corner4);
	}
}
