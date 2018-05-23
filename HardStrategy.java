import java.awt.Color;
import java.util.ArrayList;
public class HardStrategy extends ComputerPlayer {
	int[] allScores;
	public HardStrategy(Color c, int[] allScores, String name) {
		super(c, name);
		this.allScores = allScores;
	}
	Object runTurn(boolean firstturn,boolean firstRailPlaced, Object grid2) {
		ReadOnlyGrid grid = null;
		try{
			grid = (ReadOnlyGrid) grid2;
		}catch(Exception e){}
		if(firstturn){
			Marker m=new Marker(record.cities.get(2).p,this);
			return m;
		}else{
			ArrayList<Rail> totalRails = grid.allValidMovesforPlayer(this,firstRailPlaced);//getRailsAtPos(startMarker.p),startMarker.p, grid);
			if(this.record.citiesReached.size()==5){
				
			}
			int minDistance = 100000;
			Rail nextRail=null;
			for(Rail r: totalRails){//check the distance to city, set min and nextRail if this rail is closer than previous
				if(!(firstRailPlaced&&r.size==2)){
					if(record.citiesReached.size()==5){
						nextRail=r;
					}else
						for(City c: this.record.getCities()){
							if(!record.citiesReached.contains(c)){
								int distance = distanceToCity(r,c, grid);
								if(distance<minDistance){
									minDistance = distance;
									nextRail = r;
								}
							}
						}
				}
			}
			return nextRail;
		}
		
	}
	public int distanceToCity(Rail rail, City city, ReadOnlyGrid grid) {
		return Math.min(grid.distbetweenpoints(rail.p1,city.getPos()), grid.distbetweenpoints(rail.p2, city.getPos()));
	}
}