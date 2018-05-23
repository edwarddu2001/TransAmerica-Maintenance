import java.util.ArrayList;

public class ReadOnlyGrid {
	private Grid grid;
	int[] scores;
	ReadOnlyGrid(Grid r,int[] scores_){
		grid=r;
		scores=scores_;
	}
	boolean checkRail(Rail r, Player p){
		return grid.checkRail(r, p);
	}
	int[] railsMissing(ArrayList<Player> players){
		return grid.railsMissing(players);
	}
	ArrayList<Rail> immediateneighbors(Position p){
		return grid.immediateneighbors(p);
	}
	int distbetweenpoints(Position p1, Position p2){
		return grid.distbetweenpoints(p1, p2);
	}
	ArrayList<Rail> allValidMovesforPlayer(Player p,boolean mountainsallowed){
		try{
			ArrayList<Rail> r= grid.allValidMovesForPlayer(p);
			for(int i=0;i<r.size();i++){
				if(mountainsallowed&&r.get(i).size==2){
					r.remove(i);
					i--;
				}
			}
			return r;
		}catch(Exception E){return null;}
	}
	boolean RailExists(Position p1, Position p2) {
		return grid.RailExists(p1, p2);
	}
}
