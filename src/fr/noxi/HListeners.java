package fr.noxi;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.noxi.api.Title;
import fr.noxi.stats.HState;
import fr.noxi.tasks.HEndB;
import fr.noxi.tasks.HScore;
import fr.noxi.tasks.HStart;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HListeners implements Listener {

	int task;
	private Hika main;
	public HListeners(Hika hika) {
		this.main = hika;
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		 e.setJoinMessage(null);
		if(Bukkit.getOnlinePlayers().size() >= 3 || main.isState(HState.GAME) || main.isState(HState.FINISH)){
			p.sendMessage("§7Désolé §8! §cLa partie à commencé tu es redirigé vers le hub §8!");
			HEndB endB = new HEndB(main, p);
			endB.runTaskTimer(main, 20, 20);
			
			
			return;
		}if(Bukkit.getOnlinePlayers().size() == 2){
			if(!main.isState(HState.PREGAME)){
			HStart start = new HStart(main);
			start.runTaskTimer(main, 20, 20);
			}
		}
		if(Bukkit.getOnlinePlayers().size() == 2 || Bukkit.getOnlinePlayers().size() == 1){
			final ItemStack it = new ItemStack(Material.BED);					
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName("§7Pour retourner au hub !");
			it.setItemMeta(itM);
			
			p.setGameMode(GameMode.SURVIVAL);
			HScore sc = new HScore(main);
			sc.runTaskTimer(main,20,20);
		
			p.getInventory().clear();
			p.getInventory().setItem(8, it);
			 double x = main.getConfig().getDouble("locations.spawn.lobby.x");
			 double y = main.getConfig().getDouble("locations.spawn.lobby.y");
			 double z = main.getConfig().getDouble("locations.spawn.lobby.z");
			 float p1 = (float) main.getConfig().getDouble("locations.spawn.lobby.p");
			 float ya = (float) main.getConfig().getDouble("locations.spawn.lobby.ya");
			 String prefixz = PermissionsEx.getUser(e.getPlayer()).getPrefix();
			 p.setPlayerListName(prefixz + " " + p.getName());
			 p.teleport(new Location(Bukkit.getWorld("world"),x,y,z,ya,p1));
			 e.setJoinMessage(null);
			 for(Player pl : Bukkit.getOnlinePlayers()){
				 Title.sendActionBar(pl, prefixz + " "+p.getName()+ " §7à rejoint §6Hika§6§lBrain §a("+Bukkit.getOnlinePlayers().size()+"/2)");
			 }
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(main.PT.contains(p)){
			main.PT.remove(p);
		}
		
		main.checkWin();

		 String prefixz = PermissionsEx.getUser(e.getPlayer()).getPrefix();
		 e.setQuitMessage(null);
		 if(main.PT.contains(p)){
			 for(Player pl : Bukkit.getOnlinePlayers()){
				 Title.sendActionBar(pl, prefixz + " "+p.getName()+ " §7à quitter §6Hika§6§lBrain §a("+(Bukkit.getOnlinePlayers().size()-1)+"/2)");
			 }
		 }
		
		if(main.checkTeam(p) == true){
			for(Team t : main.teams){
				t.removePlayer(p);
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		Entity z = e.getEntity();
				
		if(!main.isState(HState.GAME)){
			e.setCancelled(true);
		}else if(main.isState(HState.GAME)){
			if(z instanceof Player){
				Player p = (Player) e.getEntity();
				if(p.getHealth() <= e.getDamage()){
					e.setDamage(0);
					main.respawn(p, false);
				}
				
			}
		}
		if(e.getCause() == DamageCause.FALL){
			if(z instanceof Player){
				Player p = (Player) e.getEntity();
				if(p.getLocation().getY() <= 1){
					e.setDamage(0);
				}
			}
			
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if(main.isState(HState.GAME)){
			Block bottom = p.getLocation().add(0,-1,0).getBlock();
			if(bottom.getType() == Material.DIAMOND_BLOCK){
				
					if(main.getTeam(p).getName() == "§bBleu"){
						int point = main.getTeam(p).getPoint() + 1;
						main.getTeam(p).setPoint(point);
						main.clear();
						main.cheakP(p);
						
						if(main.PT.get(0) == p){
							Bukkit.broadcastMessage(main.getPrefixH()+"  §2"+ main.PT.get(0).getName()+ " §7à §e" +main.getTeam(p).getPoint() +" §6point§7(§6s§7) §8!");
						}else{
							Bukkit.broadcastMessage(main.getPrefixH()+" §2"+ main.PT.get(1).getName()+ " §7à §e" +main.getTeam(p).getPoint() +" §6point§7(§6s§7) §8!");
						}
						
					for(Player pls : Bukkit.getOnlinePlayers()){
						Location loci = main.getTeam(pls).getSpawn();
						pls.teleport(loci);
						}
					}else if(main.getTeam(p).getName() == "§cRouge"){
						p.sendMessage(main.getPrefixH()+" §cErreur §6--§e> §7tu marque de l'autre côté §8!");
					}
					
					
				
				
				
			}
			if(bottom.getType() == Material.REDSTONE_BLOCK){
				
				if(main.getTeam(p).getName() == "§cRouge"){
					int point = main.getTeam(p).getPoint() + 1;
					main.getTeam(p).setPoint(point);
					main.clear();
					main.cheakP(p);
					if(main.PT.get(0) == p){
						Bukkit.broadcastMessage(main.getPrefixH()+" §2"+ main.PT.get(0).getName()+ " §7à §e" +main.getTeam(p).getPoint() +" §6point§7(§6s§7) §8!");
					}else{
						Bukkit.broadcastMessage(main.getPrefixH()+" §2"+ main.PT.get(1).getName()+ " §7à §e" +main.getTeam(p).getPoint() +" §6point§7(§6s§7) §8!");
					}
				for(Player pls : Bukkit.getOnlinePlayers()){
					Location loci = main.getTeam(pls).getSpawn();
					pls.teleport(loci);
					}
				}else if(main.getTeam(p).getName() == "§bBleu"){
					p.sendMessage(main.getPrefixH()+" §cErreur §6--§e> §7tu marque de l'autre côté §8!");
				}
				
				
			
			
			
		}
			if(loc.getY() <= 0){
				main.respawn(p, false);
			}
		}else{
			if(loc.getY() <= 0){
				
			
		
			 double x = main.getConfig().getDouble("locations.spawn.lobby.x");
			 double y = main.getConfig().getDouble("locations.spawn.lobby.y");
			 double z = main.getConfig().getDouble("locations.spawn.lobby.z");
			 float p1 = (float) main.getConfig().getDouble("locations.spawn.lobby.p");
			 float ya = (float) main.getConfig().getDouble("locations.spawn.lobby.ya");
			 p.teleport(new Location(Bukkit.getWorld("world"),x,y,z,ya,p1));
			}
		}
		
	}
	@EventHandler
	public void creatureSpawn(EntitySpawnEvent e){
		if(!(e.getEntity() instanceof Player)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		Player p = e.getPlayer();
		Block a = e.getBlockPlaced();
		Block bottom = a.getLocation().add(0,-1,0).getBlock();
		if(main.isState(HState.GAME)){
			if(bottom.getType() == Material.REDSTONE_BLOCK || bottom.getType() == Material.DIAMOND_BLOCK){
				e.setCancelled(true);
			}
			main.block.add(a);
			
			
		
		}else if(!main.isState(HState.GAME)){
			if(!p.hasPermission("admin")){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block a = e.getBlock();
		if(main.isState(HState.GAME)){
			if(a.getType() == Material.SANDSTONE){
			Location loc = a.getLocation();
			main.blockP.put(loc, a.getType());
			}else{
				p.sendMessage(main.getPrefixH()+" §cErreur §6--§e> §7tu ne peux casser que de la SANDSTONE §8!");
				e.setCancelled(true);
			}
		}else if(!main.isState(HState.GAME)){
			if(!p.hasPermission("admin")){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		if(i == null){
			return;
		}
		if(i != null && i.getType() != null && i.getType() == Material.BED){
			main.teleport(p);
			
		}
	}
}
