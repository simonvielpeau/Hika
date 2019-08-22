package fr.noxi.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.noxi.Hika;
import fr.noxi.api.Title;

public class HRespawn extends BukkitRunnable{

	private Hika main;
	private Player p;
	
	public HRespawn(Hika hika, Player p){
		this.main = hika;
		this.p = p;
	}
	private int timer = 3;
	@Override
	public void run() {
		timer --;
		if(timer > 0){
			Title.sendTitle(p, "§eRespawn", "§6"+timer+"§7s", 20);
		}else if(timer == 0){
			main.respawn(p, false);
			p.teleport(main.getTeam(p).getSpawn());
			p.setGameMode(GameMode.SURVIVAL);
			cancel();
		}
		
	}
	
	
}
