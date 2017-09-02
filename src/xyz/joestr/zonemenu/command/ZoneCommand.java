package xyz.joestr.zonemenu.command;

import java.util.concurrent.Executors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.util.DomainInputResolver;
import com.sk89q.worldguard.protection.util.DomainInputResolver.UserLocatorPolicy;
import com.sk89q.worldguard.util.profile.resolver.ProfileService;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import xyz.joestr.zonemenu.ZoneMenu;

@SuppressWarnings("deprecation")
public class ZoneCommand implements CommandExecutor {
	
	private ZoneMenu plugin;
	
	public ZoneCommand(ZoneMenu zonemenu) { this.plugin = zonemenu; }
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//Player
		if(sender instanceof Player) {
			
			Player player = (Player)sender;
			
			if(player.hasPermission("zonemenu.*")) {
				
				if(args.length < 1) {
					
					player.spigot().sendMessage(
					new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head") + (String)this.plugin.yd.Map().get("head_extra")))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("find")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("find_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone find"))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("sign")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("sign_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone create"))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("create")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("create_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone finish"))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("addmember")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("addmember_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zone addmember "))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("removemember")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("removemember_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zone removemember "))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("info")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("info_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone info"))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("delete")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("delete_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zone delete"))
					.append("\n" + this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("cancel")))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("cancel_hover")))
					.create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone cancel"))
					.create());
					return true;
				}
				
				if((args[0].equalsIgnoreCase("find")) && (args.length < 2)) {
					
					if(!player.getInventory().contains(Material.STICK)) {
						
						ItemStack itemstack = new ItemStack(Material.STICK, 1);
						player.getInventory().addItem(new ItemStack[] { itemstack });
					}
					
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_find")));
					this.plugin.Tool.put(player.getName(), "find");
					return true;
				}
				
				if((args[0].equalsIgnoreCase("create")) && (args.length < 2)) {
					
					if(!player.getInventory().contains(Material.STICK)) {
						
						ItemStack itemstack = new ItemStack(Material.STICK, 1);
						player.getInventory().addItem(new ItemStack[] { itemstack });
					}
					
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_sign")));
					this.plugin.Tool.put(player.getName(), "sign");
					return true;
				}
				
				if((args[0].equalsIgnoreCase("finish")) && (args.length < 2)) {
					
					if(this.plugin.getWorldGuard().getRegionManager(player.getWorld()).getRegion(player.getName()) != null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_in_world")));
						return true;
					}
					
					Selection selectedregion = this.plugin.getWorldEdit().getSelection(player);
					
					if(selectedregion == null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_not_sign")));
						return true;
					}
					
					if(selectedregion.getWidth() * selectedregion.getLength() < (int)this.plugin.yd.Map().get("zone_area_min")) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_area_under")).replace("{0}", this.plugin.yd.Map().get("zone_area_min").toString()));
						return true;
					}
					
					if(selectedregion.getWidth() * selectedregion.getLength() > (int)this.plugin.yd.Map().get("zone_area_max")) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_area_over")).replace("{0}", this.plugin.yd.Map().get("zone_area_max").toString()));
						return true;
					}
					
					Location min = selectedregion.getMinimumPoint();
					Location max = selectedregion.getMaximumPoint();
					double first_x = min.getX();
					double first_y = min.getY();
					double first_z = min.getZ();
					double second_x = max.getX();
					double second_y = max.getY();
					double second_z = max.getZ();
					ProtectedCuboidRegion protectedcuboidregion = new ProtectedCuboidRegion((String)this.plugin.yd.Map().get("zone_id") + this.plugin.yd.Map().get("zone_id_counter").toString(), new BlockVector(first_x, first_y, first_z), new BlockVector(second_x, second_y, second_z));
					this.plugin.yd.Map().put("zone_id_counter", Integer.parseInt(this.plugin.yd.Map().get("zone_id_counter").toString()) + 1);
					this.plugin.yd.Save();
					
					if(this.plugin.getWorldGuard().getRegionManager(player.getWorld()).overlapsUnownedRegion(protectedcuboidregion, this.plugin.getWorldGuard().wrapPlayer(player))) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_overlap")));
						return true;
					}
									
					ProfileService ps = this.plugin.wg.getProfileService();
					try { ps.findByName(player.getName()); } catch(Exception e) { e.printStackTrace(); }
					DefaultDomain domain = new DefaultDomain();
					domain.addPlayer(this.plugin.wg.wrapPlayer(player));
					protectedcuboidregion.setOwners(domain);
					protectedcuboidregion.setPriority(Integer.parseInt((String)this.plugin.yd.Map().get("zone_priority")));
					
					/*
					ProtectRegion.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.TNT, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.FIRE_SPREAD, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.OTHER_EXPLOSION, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.ENDER_BUILD, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.GHAST_FIREBALL, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.LAVA_FIRE, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.MOB_DAMAGE, StateFlag.State.DENY);
					ProtectRegion.setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.DENY);
					*/
					
					this.plugin.getWorldGuard().getRegionManager(player.getWorld()).addRegion(protectedcuboidregion);
					
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_create")));
					this.plugin.Tool.remove(player.getName());
					this.plugin.FindLocations.remove(player.getName());
					this.plugin.Worlds.remove(player.getName());
					this.plugin.FirstLocations.remove(player.getName());
					this.plugin.SecondLocations.remove(player.getName());
					//this.plugin.we.getSession(player).getRegionSelector(plugin.we.getSession(player).getSelectionWorld()).clear();
					this.plugin.resetCorner(player, this.plugin.corner1);
					this.plugin.resetCorner(player, this.plugin.corner2);
					this.plugin.resetCorner(player, this.plugin.corner3);
					this.plugin.resetCorner(player, this.plugin.corner4);
					return true;
				}
				
				if((args[0].equalsIgnoreCase("addmember")) && (args.length > 1) && (args.length < 3)) {
					
					ProtectedRegion protectedregion = null;
					
					try { protectedregion = this.plugin.getRegion(player); } catch(Exception e) { e.printStackTrace(); }
					
					if(protectedregion == null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("no_zone")));
						return true;
					}
					
					ProtectedRegion protectedregionforguava = protectedregion;
					
					//From: https://worldguard.enginehub.org/en/latest/developer/regions/protected-region/#domains (modified)
					//start ---
					// Google's Guava library provides useful concurrency classes.
					// The following executor would be re-used in your this.plugin.
					ListeningExecutorService executor =
					        MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

					String[] input = new String[] { args[1] };
					ProfileService profiles = this.plugin.wg.getProfileService();
					DomainInputResolver resolver = new DomainInputResolver(profiles, input);
					resolver.setLocatorPolicy(UserLocatorPolicy.UUID_AND_NAME);
					ListenableFuture<DefaultDomain> future = executor.submit(resolver);

					// Add a callback using Guava
					Futures.addCallback(future, new FutureCallback<DefaultDomain>() {
						
					    @Override
					    public void onSuccess(DefaultDomain result) {
					    	
					    	protectedregionforguava.getMembers().addPlayer(plugin.wg.wrapOfflinePlayer(plugin.getServer().getOfflinePlayer(args[1])));
							player.sendMessage(plugin.ColorCode("&", (String)plugin.yd.Map().get("head")));
							player.sendMessage(plugin.ColorCode("&", (String)plugin.yd.Map().get("zone_addmember")).replace("{0}", args[1]));
					    }

					    @Override
					    public void onFailure(Throwable throwable) {
					    	
					    	player.sendMessage(plugin.ColorCode("&", (String)plugin.yd.Map().get("head")));
							player.sendMessage(plugin.ColorCode("&", (String)plugin.yd.Map().get("zone_addmember_unknownplayer")).replace("{0}", args[1]));
					    }
					});
					//end ---
					
					return true;
				}
				
				if((args[0].equalsIgnoreCase("removemember")) && (args.length > 1) && (args.length < 3)) {
					
					ProtectedRegion protectedregion = null;
					
					try { protectedregion = this.plugin.getRegion(player); } catch(Exception e) { e.printStackTrace(); }
					
					if(protectedregion == null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("no_zone")));
						return true;
					}
					
					DefaultDomain domainmembers = protectedregion.getMembers();
					domainmembers.removePlayer(this.plugin.wg.wrapOfflinePlayer(plugin.getServer().getOfflinePlayer(args[1])));
					protectedregion.setMembers(domainmembers);
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_removemember")).replace("{0}", args[1]));
					return true;
				}
				
				if((args[0].equalsIgnoreCase("info")) && (args.length < 2)) {
					
					ProtectedRegion protectedregion = null;
					
					try { protectedregion = this.plugin.getRegion(player); } catch(Exception e) { e.printStackTrace(); }
					
					if(protectedregion == null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("no_zone")));
						return true;
					}
					
					DefaultDomain domainowners = protectedregion.getOwners();
					DefaultDomain RegionMember = protectedregion.getMembers();
					int min_x = protectedregion.getMinimumPoint().getBlockX();
					int min_z = protectedregion.getMinimumPoint().getBlockZ();
					int max_x = protectedregion.getMaximumPoint().getBlockX();
					int max_z = protectedregion.getMaximumPoint().getBlockZ();
					int area = (this.plugin.difference(min_x, max_x) + 1) * (this.plugin.difference(min_z, max_z) + 1);
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_id")) + protectedregion.getId());
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_owners")) + domainowners.toPlayersString(this.plugin.wg.getProfileCache()).replace("*", ""));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_members")) + RegionMember.toPlayersString(this.plugin.wg.getProfileCache()).replace("*", ""));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_start")).replace("{0}", Integer.toString(min_x)).replace("{1}", Integer.toString(min_z)));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_end")).replace("{0}", Integer.toString(max_x)).replace("{1}", Integer.toString(max_z)));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_info_area")).replace("{0}", Integer.toString(area)));
					return true;
				}
				
				if((args[0].equalsIgnoreCase("delete")) && (args.length < 2)) {
					
					ProtectedRegion protectedregion = null;
					
					try { protectedregion = this.plugin.getRegion(player); } catch(Exception e) { e.printStackTrace(); }
					
					if(protectedregion == null) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("no_zone")));
						return true;
					}
					
					this.plugin.getWorldGuard().getRegionManager(player.getWorld()).removeRegion(protectedregion.getId());
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_delete")));
					return true;
				}
				
				if((args[0].equalsIgnoreCase("cancel")) && (args.length < 2)) {
					
					if(!this.plugin.Tool.containsKey(player.getName())) {
						
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
						player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_already_cancel")));
						return true;
					}
					
					this.plugin.Tool.remove(player.getName());
					this.plugin.FindLocations.remove(player.getName());
					this.plugin.Worlds.remove(player.getName());
					this.plugin.FirstLocations.remove(player.getName());
					this.plugin.SecondLocations.remove(player.getName());
					//this.plugin.we.getSession(player).getRegionSelector(plugin.we.getSession(player).getSelectionWorld()).clear();
					this.plugin.resetCorner(player, this.plugin.corner1);
					this.plugin.resetCorner(player, this.plugin.corner2);
					this.plugin.resetCorner(player, this.plugin.corner3);
					this.plugin.resetCorner(player, this.plugin.corner4);
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
					player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("zone_cancel")));
					return true;
				}
				
				player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
				player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("usage_message")).replace("{0}", "/zone"));
				return true;
			}
			
			player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
			player.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("permission_message")).replace("{0}", "zonemenu.*"));
			return true;
		}
		
		//Console
		plugin.getServer().getConsoleSender().sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("head")));
		sender.sendMessage(this.plugin.ColorCode("&", (String)this.plugin.yd.Map().get("console_message")));
		return true;
	}
}
