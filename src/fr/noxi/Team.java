package fr.noxi;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {

	private Location spawn;
	private String name;
	private int point;
	
	//Savoir si il a team
	public ArrayList<Player> playersT = new ArrayList<>();
	public Team(String name,Location spawn, int point){
		this.name = name;
		this.spawn = spawn;
		this.setPoint(point);

	}
	public Location getSpawn() {
		return spawn;
	}
	public String getName() {
		return name;
	}
	
	public int getSize(){
		return playersT.size();
	}
	
	public void addPlayer(Player player){
		
		playersT.add(player);
	}
	public void removePlayer(Player player){
		playersT.remove(player);
		Hika.getInstance().PT.remove(player);
		Hika.getInstance().teamH.remove(player);
		Hika.getInstance().teams.remove(player);
		
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}
