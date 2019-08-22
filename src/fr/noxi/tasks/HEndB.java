package fr.noxi.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.noxi.Hika;

public class HEndB extends BukkitRunnable{

	private Hika main;
	private Player p;
	
	public HEndB(Hika hika, Player p){
		this.main = hika;
		this.p = p;
	}
	int timer = 4;
	@Override
	public void run() {
		
			timer--;
			
			if(timer == 3){
				main.teleport(p);
			}
			if(timer == 2){
				main.teleport(p);
			}
			if(timer == 1){
				main.teleport(p);
			}
			if(timer == 0){
				cancel();
			}
			
		
		
	}
	
	
}
