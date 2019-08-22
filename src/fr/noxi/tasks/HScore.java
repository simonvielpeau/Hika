package fr.noxi.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.noxi.Hika;
import fr.noxi.api.ScoreboardSign;
import fr.noxi.api.Title;
import fr.noxi.stats.HState;

public class HScore extends BukkitRunnable{

	private Hika main;
	private ScoreboardSign sb;
	public HScore(Hika hika){
		this.main = hika;
		for(Player p : Bukkit.getOnlinePlayers()){
			ScoreboardSign sb = new ScoreboardSign(p, main.getPrefixH());
			this.sb = sb;
		}
		
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()){
			
			sb.destroy();
			sb.create();
			
			sb.setLine(0, "§9");
			sb.setLine(1, "§ePlayers :");
			
		
				if(main.isState(HState.WAITING) || main.isState(HState.PREGAME)){
					sb.setLine(2, Bukkit.getOnlinePlayers().size()+"");
				}else{
					sb.setLine(2, main.PT.size()+"");
				}
				
			
			sb.setLine(3, "§a");
			sb.setLine(4, "Status du jeu :");
			sb.setLine(6, "§1");
			
			sb.setLine(7, "§7Objectif: 5 points §8!");
			sb.setLine(8, "§4");
			sb.setLine(9, "§6"+main.getConfig().getString("ip"));
			if(main.isState(HState.FINISH)){
				String state1 = "§aTerminé §8!";
				sb.setLine(5, state1);
			}else if(main.isState(HState.GAME)){
				String state1 = "§aEn jeu §8!";
				sb.setLine(5, state1);
			}else if(main.isState(HState.PREGAME)){
				String state1 = "§eLancement...";
				sb.setLine(5, state1);
			}else if(main.isState(HState.WAITING)){
				String state1 = "§bEn attente...";
				sb.setLine(5, state1);
			}
			main.boards.put(p, sb);
			
			if(main.isState(HState.GAME)){
				Player a = main.PT.get(0);
				Player b = main.PT.get(1);
				Title.sendActionBar(p, "§6"+a.getName() + " §7-§e"+main.getTeam(a).getPoint()+ "       §6"+b.getName()+" §7-§e"+main.getTeam(b).getPoint());
			}
		}
		
		
	}

	
	
	
}
