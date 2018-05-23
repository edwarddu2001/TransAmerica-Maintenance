import java.awt.Color;
import java.util.ArrayList;

/*
 * HumanPlayer extends Player{
Rail[] runTurn() {}
String playerName() {}
}
test
 */
public class HumanPlayer extends Player {
	HumanPlayer(Color c, String name){
		super(c, name);
	}
	Object runTurn(boolean firstturn,boolean firstPlacedRail, Object map2) {
		MapofUSA map = null;
		try {
			map = (MapofUSA)map2;
		}catch(Exception e){
			System.out.println("Not a Map");
		}
		//accesses lastClick to map until it isn't null
//		try{
//			if(firstturn||map.currentGrid.allRails.contains(new Rail(new Position(0,3),new Position(1,2)))){
//				if(firstturn){
//					return new Marker(new Position(0,0),this);
//				}else{
//					try {
//						return new Rail(new Position(0,0),new Position(0,1),this);
//					} catch (Exception e) {
//						return null;
//					}
//				}
//			}else{
	
			Object nextRail = null;
			map.currentPlayer=this;
			boolean ok = true;
			while(nextRail == null||ok){
				ok=true;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try{
					nextRail = map.returnPlacedRail();
				}catch(Exception E){
					System.out.println(map==null);
				}
				try{
					Marker m = (Marker) nextRail;
					ok=false;
				}catch(Exception E){
					try{
						Rail r = (Rail) nextRail;
						if(r!=null&map.currentGrid.checkRail(r, this)){
							ok=false;
						}
					}catch(Exception er){
						
					}
				}
			}
			return nextRail;
//			}
//		}catch(Exception E){}
//		return null;
	}
	
//	Position getStartMarker() {
//		return null;
//	}

}
