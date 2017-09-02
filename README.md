**Important**  
This plugin requires [WorldEdit](https://github.com/sk89q/WorldEdit) and [WorldGuard](https://github.com/sk89q/WorldGuard)!

**ZoneMenu**  
Simple, interactive menu for region creation with WorldGuard (and WorldEdit) for players. (Spigot)

**Commands**  
*/zone* - Display interactive menu.  
*/zone find* - Find zones.  
*/zone sign* - Sign the start- and ending point of your new zone.  
*/zone create* - Create a zone from your selection.  
*/zone addmember \<player\>* - Add a member to your zone.  
*/zone removemember \<player\>* - Remove a member from your zone.  
*/zone delete* - Delete your zone.  
*/zone cancel* - Cancel your zone creation.

**Permissions**  
*/zone* - zonemenu.\*

**Deprecated**  
*/zone unbind* - Unbind your stick. (Removed in version 0.0.3)  
*unbind: \<String\>* - Message for */zone* unbind in the */zone* command. (Removed in version 0.0.3)  
*unbind_hover: \<String\>* - Hover-Message for */zone unbind* in the */zone* command. (Removed in version 0.0.3)

**Changed**  
*zone_area_limit: \<Int\>* - Maximum area a user can claim. (Replaced by *zone_area_max: \<Int\>* in version 0.0.3)

**Note**  
Version 0.0.3 completes missing config entries automatically while older versions (0.0.1, 0.0.2) don't do that.
