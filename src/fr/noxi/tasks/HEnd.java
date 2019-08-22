package fr.noxi.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.noxi.Hika;

public class HEnd extends BukkitRunnable{

	private Hika main;
	private Player p;
	
	public HEnd(Hika hika, Player p){
		this.main = hika;
		this.p = p;
	}
	int timer = 12;
	@Override
	public void run() {
		
			timer--;
			if(timer == 6){
				p.sendMessage(main.getPrefixH()+" §7Fermeture du serveur dans §c3 §7secondes §8 !");
			}
			if(timer == 3){
				main.teleportAll();
			}
			if(timer == 0){
				Bukkit.spigot().restart();
				cancel();
			}
			
		
		
	}
	
	
}
