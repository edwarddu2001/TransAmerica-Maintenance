import java.awt.Color;
import java.util.ArrayList;
public class ActuallyCompetitiveStrategy extends ComputerPlayer{

		int[] allScores;
		public ActuallyCompetitiveStrategy(Color c, int[] allScores, String name) {
			super(c,  name);
			this.allScores = allScores;
		}
		Object runTurn(boolean firstturn,boolean firstRailPlaced, Object grid2) {
			ReadOnlyGrid grid = null;
			try{
				grid = (ReadOnlyGrid) grid2;
			}catch(Exception e){}
			if(firstturn){
				//Returns the marker closest to the average positions of all goal cities
				return null;
			}else{
				//please implement Dijkstra's algorithm
				//aka, using another player costs 0, placing reg rail costs 1, placing mountain rail costs 2
				return null;
			}
			
		}
	}
