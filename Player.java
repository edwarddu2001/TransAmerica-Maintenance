import java.awt.Color;
import java.util.ArrayList;

/*Abstract Player (players and strategy){
	ArrayList<City> cityGoal
ArrayList<City> cityReached//added in order (also only needed cities)
Boolean PlacedMarkerAlready.
	Rail[] runTurn()//starts round, waits for input if human, and then updates rails and Grid
	Void clearForNewRound
Void updatePlayer
String playerName(){}

Int[] startMarker;
Int[] getStartMarker();
} */

public abstract class Player {//give players playerrecords
	boolean placedMarkerAlready;//this is never set
	Marker startMarker;
	PlayerRecord record;
	int playerNumber = 0;
	int railsRemaining = 2;
	
	String name;
	Color color;
	
	Player(Color c, String n){
		placedMarkerAlready = false;
		this.name = n;
		this.color = c;
		record = new PlayerRecord(c,null, n);
	}
	//This needs to be an object because on first turn returns marker
	abstract Object runTurn(boolean firstturn, boolean firstRailPlaced, Object gridormap);
	void clearForNewRound(ArrayList<City> cities){
		placedMarkerAlready = false;
		record.cities = cities;
		record.citiesReached=new ArrayList<City>();
	}
	String getName(){
		return name;
	}
	Marker getStartMarker(){
		return startMarker;
	}
	Position getStartPosition(){
		return startMarker.p;
	}
	public PlayerRecord getPlayerRecord() {
		return record;
	}
	public Color getColor() {
		return color;
	}
	public void setPlayerNumber(int num) {
		playerNumber = num;
	}
	
}
