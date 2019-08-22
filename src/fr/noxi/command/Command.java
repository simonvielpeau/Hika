package fr.noxi.command;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.noxi.Hika;

public class Command implements CommandExecutor{

	private Hika pl;
	private FileConfiguration config;
	public Command(Hika hika){
		this.pl = hika;
		this.config = pl.getConfig();
	}
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) { 
		Player p = (Player)sender;
		if(sender instanceof Player){
			if(args[0].equalsIgnoreCase("lobby")){
				Location loc = p.getLocation();
				config.set("locations.spawn.lobby.x", loc.getX());
				config.set("locations.spawn.lobby.y", loc.getY());
				config.set("locations.spawn.lobby.z", loc.getZ());
				config.set("locations.spawn.lobby.ya", loc.getYaw());
				config.set("locations.spawn.lobby.p", loc.getPitch());
				pl.saveConfig();
				p.sendMessage("§cLobby bien set");
				
			}
			if(args[0].equalsIgnoreCase("1")){
				Location loc = p.getLocation();
				config.set("locations.spawn.red.x", loc.getX());
				config.set("locations.spawn.red.y", loc.getY());
				config.set("locations.spawn.red.z", loc.getZ());
				config.set("locations.spawn.red.ya", loc.getYaw());
				config.set("locations.spawn.red.p", loc.getPitch());
				pl.saveConfig();
				p.sendMessage("§cRed bien set");
				
			}
			if(args[0].equalsIgnoreCase("2")){
				Location loc = p.getLocation();
				config.set("locations.spawn.blue.x", loc.getX());
				config.set("locations.spawn.blue.y", loc.getY());
				config.set("locations.spawn.blue.z", loc.getZ());
				config.set("locations.spawn.blue.ya", loc.getYaw());
				config.set("locations.spawn.blue.p", loc.getPitch());
				pl.saveConfig();
				p.sendMessage("§bBlue bien set");
				
			}
			if(args[0].equalsIgnoreCase("clear")){
				pl.clear();
			}
		}
		return false;
	}
	
	
	
}
