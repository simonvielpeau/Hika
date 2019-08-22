package fr.noxi.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.noxi.Hika;
import fr.noxi.api.Title;
import fr.noxi.stats.HState;

public class HStart extends BukkitRunnable{

	private int timer = 11;
	private Hika main;
	
	public HStart(Hika hika){
		this.main = hika ;
	}
	@Override
	public void run() {
		timer--;
		main.setState(HState.PREGAME);
		if(!(Bukkit.getOnlinePlayers().size() >= 2)){
			main.setState(HState.WAITING);
			Bukkit.broadcastMessage(main.getPrefixH()+" §cErreur §6--§e> §7il n'y a pas assez de joueurs pour start la partie §8!");
			cancel();
		}
		if(timer == 5 || timer == 4 ||timer == 3 || timer == 2 || timer == 1){
			for(Player pls : Bukkit.getOnlinePlayers()){
				Title.sendTitle(pls, "§6", "§c"+timer+"§7s", 20);
					main.randomTeam(pls);
				
			}
		}else if(timer == 10){
			for(Player pls : Bukkit.getOnlinePlayers()){
				Title.sendTitle(pls, "§6Lancement dans", "§c"+timer+"§7s", 20);
			}
		}
			
			
		
		if(timer == 0){
			main.begin();
			cancel();
		}
		
	}

	
	
}
