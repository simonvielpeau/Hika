package fr.noxi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.noxi.Main;
import fr.noxi.api.ScoreboardSign;
import fr.noxi.api.Title;
import fr.noxi.command.Command;
import fr.noxi.stats.HState;
import fr.noxi.tasks.HEnd;

public class Hika extends JavaPlugin {
	public List<Team> teams = new ArrayList<>();
	public List<Player> PT = new ArrayList<>();
	public Map<Player, Team> teamH = new HashMap<>();
	public Map<Location, Material> blockP = new HashMap<>();
	public List<Block> block = new ArrayList<>();
	private String prefix = "§7[§6Hika§6§lBrain§7]";
	public Map<Player, ScoreboardSign> boards = new HashMap<>();
	private HState current;
	public static Hika instance;
	@Override
	public void onEnable() {

		
		
		getCommand("hika").setExecutor(new Command(this));
		getServer().getPluginManager().registerEvents(new HListeners(this), this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		teams.clear();
		teamH.clear();
		
		blockP.clear();
		for(Team t : teams){
			t.playersT.clear();
		}
		block.clear();
		instance = this;
		double x = getConfig().getDouble("locations.spawn.red.x");
		double y = getConfig().getDouble("locations.spawn.red.y");
		double z = getConfig().getDouble("locations.spawn.red.z");
		float p = (float) getConfig().getDouble("locations.spawn.red.p");
		float ya = (float) getConfig().getDouble("locations.spawn.red.ya");
		double x1 = getConfig().getDouble("locations.spawn.blue.x");
		double y1 = getConfig().getDouble("locations.spawn.blue.y");
		double z1 = getConfig().getDouble("locations.spawn.blue.z");
		float p1 = (float) getConfig().getDouble("locations.spawn.blue.p");
		float ya1 = (float) getConfig().getDouble("locations.spawn.blue.ya");
		teams.add(new Team("§cRouge", new Location(Bukkit.getWorld("world"), x,y,z,ya,p),0));
		teams.add(new Team("§bBleu", new Location(Bukkit.getWorld("world"), x1,y1,z1,ya1,p1),0));
		super.onEnable();
		Bukkit.getScheduler().runTaskLater(this, new Runnable(){

			@Override
			public void run() {
				setState(HState.WAITING);
				
			}
			
		},1*20);
	}
	public void setState(HState state){
		String name =  getConfig().getString("server");
		if(HState.WAITING == state){
			Main.getInstance().sql.actualiseS(name, 1);
		}
		else if(HState.PREGAME == state){
			Main.getInstance().sql.actualiseS(name, 2);
		}
		else if(HState.GAME == state){
			Main.getInstance().sql.actualiseS(name, 3);
		}
		else if(HState.FINISH == state){
			Main.getInstance().sql.actualiseS(name, 4);
		}
		
		current = state;
	}
	public boolean isState(HState state){
		return current == state;
	}
	public String getPrefixH(){
		return prefix;
	}
	@Override
	public void onDisable() {
		
		
		super.onDisable();
	}
	public static Hika getInstance(){
		return instance;
	}
	public List<Team> getTeams(){
		return teams;
	}
	public Team getTeam(Player p){
		return teamH.get(p);
	}
	public void randomTeam(Player p){
		for(Team t : teams){
			if(checkTeam(p) == true){
				return;
			}
			if(t.getSize() < 1){			
				addPlayers(p, t);
				return;
			}
		}
		
	}
	public String test(){
		String e = getTeams().get(0).getName();
		return e;
	}
	public void addPlayers(Player p,Team team){
		
		teamH.put(p, team);
		team.addPlayer(p);
		PT.add(p);
		
	}
	public boolean checkTeam(Player p){
		for(Team t : teams){
		if(t.playersT.contains(p)){
			
			return true;
			
		
		}
		}
		return false;
	}
	public void begin() {
		for(Player pls : Bukkit.getOnlinePlayers()){
			
			Title.sendTitle(pls, "§cC'est parti §8!", "§eLe jeu commence", 30);
			respawn(pls, true);
			
		}
		
	}
	public void clear(){
		Iterator<Block> bs = block.iterator();
		
		for(Entry<Location, Material> blocks : blockP.entrySet()){
			blocks.getKey().getBlock().setType(blocks.getValue());
		}
		
		while(bs.hasNext()){
			

			bs.next().setType(Material.AIR);;
				
		}
	}
	
	public void respawn(Player pls, boolean b) {
		setState(HState.GAME);
		pls.teleport(getTeam(pls).getSpawn());
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta swordM = sword.getItemMeta();
		swordM.setDisplayName("§cH§6§lSword");
		swordM.addEnchant(Enchantment.KNOCKBACK, 1, true);
		sword.setItemMeta(swordM);
		ItemStack pioche = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemMeta piocheM = pioche.getItemMeta();
		
		piocheM.setDisplayName("§cH§6§lPioche");
		pioche.setItemMeta(piocheM);
		ItemStack sandstone = new ItemStack(Material.SANDSTONE, 1650);
		ItemMeta sandstoneM = sandstone.getItemMeta();
		sandstoneM.setDisplayName("§cH§6§lBlock");
		sandstone.setItemMeta(sandstoneM);
		pls.setHealth(pls.getMaxHealth());
		pls.getInventory().clear();
		pls.getInventory().setItemInOffHand(sandstone);
		pls.getInventory().addItem(sword);
		pls.getInventory().addItem(pioche);
		pls.getInventory().addItem(sandstone);
		pls.getInventory().addItem(sandstone);
		
		if(b == false){
			Title.sendTitle(pls, "§6Respawn §8!", "§3", 20);
			pls.setHealth(pls.getMaxHealth());
		}
	}
	public void cheakP(Player p) {
		if(getTeam(p).getPoint() == 5){
			Bukkit.broadcastMessage("§d-----------------------------------------------------");
			Bukkit.broadcastMessage(getPrefixH()+" §aUn grand bravo à §2"+p.getName()+" §7, §ail a gagné cette belle partie §8!");
			Bukkit.broadcastMessage(getPrefixH()+" §aIl remporte §e15 §cXonis§c§lCoins §8!");
			Main.getInstance().sql.addMoney(p, 15);
			if(p != PT.get(0)){
				Player pe = PT.get(0);
				Bukkit.broadcastMessage(getPrefixH()+ " §2"+ pe.getName()+" §aremporte quand même §e3 §cXonis§c§lCoins §8!");
				Main.getInstance().sql.addMoney(pe, 3);
				Bukkit.broadcastMessage("§7-----------------------------------------------------");
			}else{
				Player pe = PT.get(1);
				Bukkit.broadcastMessage(getPrefixH()+ " §2"+ pe.getName()+" §aremporte quand même §e3 §cXonis§c§lCoins §8!");
				Main.getInstance().sql.addMoney(pe, 3);
				Bukkit.broadcastMessage("§d-----------------------------------------------------");
			}
			
			fin();
		}
		
	}
	public void fin() {
		setState(HState.FINISH);
		for(Player p : Bukkit.getOnlinePlayers()){
			p.setGameMode(GameMode.SPECTATOR);
			clear();
			HEnd end = new HEnd(this, p);
			end.runTaskTimer(this, 20, 20);
			
		}
		
	}
	public void checkWin(){
		
		if(PT.size() == 1){
			if(isState(HState.GAME)){
				fin();
			}
		}
		
	}
	public void teleportAll() {
		
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			
			try{
				out.writeUTF("Connect");
				out.writeUTF("hub");
			}catch(IOException e){
				e.printStackTrace();
				
			}
			for(Player pls : Bukkit.getOnlinePlayers()){
				pls.sendPluginMessage(this, "BungeeCord", b.toByteArray());
			}
		}
	public void teleport(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		
		try{
			out.writeUTF("Connect");
			out.writeUTF("hub");
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
			p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
		
		
	}
		
}
