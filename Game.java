import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/*
 * Game{//stores series of rounds
	ArrayList<Round> rounds
ArrayList<Player> players
	Game(ArrayList<Player>, int numberOfRounds){
//determines if is AI game and slow mode, Instantiates rounds with number of rounds, order of players in Round parameters is the order of player’s turns
}
Void runRounds(){//runs through rounds arraylist, calls returnScoreChange after finished
}
Boolean showScoreScreen;
Boolean isAIGame;
Boolean slowMode;
Boolean getShowScoreScreen();//called by MainGamePanel to determine when to change

Int[] playerNumber, winningPlayer, currentScore, scores;
	Int getNumberOfPlayers()
	Int getWinningPlayer()
	Int[] getCurrentScore()
	Int[] getScores()
}

 */
public class Game {
	public static final int ROUND = 0;
	// ^shows the types of specific game states^
	Grid grid;
	ArrayList<Player> players;
	Game(ArrayList<Player> players, boolean slowMode){
		this.players = players;
		this.slowMode = slowMode;
		startingScores = new int[players.size()];
		for(int i = 0;i<players.size();i++) {
			startingScores[i]=players.get(i).getPlayerRecord().getScore();
		}
		isAIGame = true;
		setcitiestoplayers();
		for(Player p:players) {
			if(p.getClass()==HumanPlayer.class) {
				isAIGame = false;
			}
		}
		if(slowMode){
			MapofUSA.currentGrid=grid;
		}else{

		}
	}
	void setcitiestoplayers(){
		grid = new Grid();
		ArrayList<ArrayList<City>> cr = grid.setofgoalCities(players.size());
		for(int i=0;i<players.size();i++){
			players.get(i).record.setCities(new ArrayList<City>(cr.get(i)));
		}
	}
	boolean showScoreScreen;
	boolean isAIGame;
	boolean slowMode;
	int[] startingScores;
	public boolean getShowScoreScreen(){
		//called by MainGamePanel to determine when to change, maybe
		if(showScoreScreen){
			return true;
		}
		return false;
	}

	public int placesleft=2;
	public int getNumberOfPlayers(){
		return players.size();
	}
	Timer gametimer = new Timer();

	void runGame(){
		boolean goAgain = true;
		while(goAgain){
			this.Round();
			for(Player p: players){
				System.out.print(p.record.getScore()+" ");
				if(p.record.getScore()<=0){
					goAgain = false;
				}
			}
			players.get(0).record.score--;
		}
	}

	void Round() {

		// ONLY DOES HUMAN ROUNDS RIGHT NOW

		gametimer.schedule(new TimerTask(){
			public void run() {
				grid = new Grid();
				for (Player p : players) {
					p.clearForNewRound(p.getPlayerRecord().getCities());
				}if(slowMode) {
					startHumanRound();
				}else{
					try{
						startComputerRound();
					}catch(Exception NullPointeronGamesEnd){
					}
				}
				//				System.out.println("hit");
			}

		}, 0);
		//		System.out.println("Round over");
	}

	boolean gameOver(){
		boolean over=false;
		for (Player p : players) {
			boolean all=true;
			for(City c : p.record.getCities()){
				if(!p.record.getCitiesReached().contains(c)){
					all=false;
				}
			}
			if(all){
				over=true;
			}
		}
		return over;
	}
	boolean noMoreRounds(){
		boolean over=false;
		for (Player p : players){
			if(p.getPlayerRecord().getScore()<=0){
				over = true;
			}
		}
		return over;
	}
	static boolean ignoremap=true;
	void startComputerRound(){
		boolean FirstTurn =true;

		//rotate through who goes first
		while(!gameOver()){
//			System.out.println("not gameover");
			for (Player p : players) {
				if(p.startMarker!=null){
					grid.connectCities(p);
				}
				int railsleft=2;
				placesleft=railsleft;
				try{
					ComputerPlayer c = (ComputerPlayer)p;
					if(!gameOver()){
						do{
							do{
								Object o = c.runTurn(FirstTurn,!(railsleft==2),new ReadOnlyGrid(grid,getCurrentScore()));
								if(o!=null){
									try{
										Marker m = (Marker) o;
										if(grid.alllandpositions[grid.boardheight-1-m.p.y][m.p.x]==1){
											c.startMarker=m;
											grid.placeMarker(m.p, c);
										}
									}catch(Exception Eer){
										try{
											Rail r = (Rail) o;
											if(r.size<=railsleft){
												grid.placeRail(r);
												railsleft-=r.size;
												placesleft=railsleft;
											}
										}catch(Exception er){
											er.printStackTrace();
										}
									}
								}
							}while(railsleft>0&!FirstTurn);
							//								try{
							//									System.out.println("red"+((Marker)c.runTurn(FirstTurn,!(railsleft==2),new ReadOnlyGrid(grid))).p);
							//								}catch(Exception e){}
						}while(c.startMarker==null);
					}
				}catch(Exception Ee){
					Ee.printStackTrace();
				}
			}
		}
		FirstTurn=false;
		System.out.println("GAMEOVER");


		int[] p = grid.railsMissing(players);
		for(int i=0;i<players.size();i++){
			players.get(i).getPlayerRecord().score-=p[i];
		}

	}
	void startHumanRound() {
		boolean FirstTurn =true;
		MapofUSA.firstturn=true;
		MapofUSA.currentGrid=grid;

		//		ArrayList<City> citiesfortesting = new ArrayList<City>();
		//		citiesfortesting.add(grid.allcities[5][0]);
		//		citiesfortesting.add(grid.allcities[5][1]);
		//		citiesfortesting.add(grid.allcities[5][2]);
		//		citiesfortesting.add(grid.allcities[5][3]);
		//		citiesfortesting.add(grid.allcities[5][4]);
		//		players.get(0).record.cities=citiesfortesting;

		while(!gameOver()){
			for (Player p : players) {
				if(p.startMarker!=null){
					grid.connectCities(p);
				}
				int railsleft=2;
				try{
					HumanPlayer h = (HumanPlayer)p;
					placesleft=railsleft;
					if(!gameOver()){
						do{
							do{
								Object o = h.runTurn(FirstTurn,!(railsleft==2),MainGameScreen.map);
								if(o!=null){
									try{
										Marker m = (Marker) o;
										try{
											if(grid.alllandpositions[grid.boardheight-1-m.p.y][m.p.x]==1||ignoremap){
												h.startMarker=m;
												grid.placeMarker(m.p, h);

											}
										}catch(Exception E){}
									}catch(Exception E){
										try{
											Rail r = (Rail) o;
											if(r.size<=railsleft){//Ehhe
												grid.placeRail(r);
												railsleft-=r.size;
												placesleft=railsleft;
											}
										}catch(Exception er){
											er.printStackTrace();
										}
									}
								}
							}while(railsleft>0&!FirstTurn);
						}while(h.startMarker==null);
					}
				}catch(Exception E){
					try{
						ComputerPlayer c = (ComputerPlayer)p;
						if(!gameOver()){
							do{
								do{
									Object o = null;
									try {
										o = c.runTurn(FirstTurn,!(railsleft==2),new ReadOnlyGrid(grid,getCurrentScore()));
									}catch(Exception concmod) {
									}
									if(o!=null){
										try{
											Marker m = (Marker) o;
											if(grid.alllandpositions[grid.boardheight-1-m.p.y][m.p.x]==1){
												c.startMarker=m;
												grid.placeMarker(m.p, c);
											}
										}catch(Exception Eer){
											try{
												Rail r = (Rail) o;
												if(r.size<=railsleft){
													grid.placeRail(r);
													railsleft-=r.size;
													placesleft=railsleft;
												}
											}catch(Exception er){
												er.printStackTrace();
											}
										}
									}
									try {
										//Thread.sleep(350);
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}while(railsleft>0&!FirstTurn);
							}while(c.startMarker==null);
						}
					}catch(Exception Ee){
						Ee.printStackTrace();
					}
					
				}

			}
			FirstTurn=false;
			MapofUSA.firstturn=false;
		}
		System.out.println("GAMEOVER");
		showScoreScreen=true;
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int[] p =grid.railsMissing(players);
		for(int i=0;i<players.size();i++){
			players.get(i).getPlayerRecord().score-=p[i];
		}

	}

	public int[] returnScoreChange() {
		int[] scoreChange = new int[players.size()];
		int[] newScore = getCurrentScore();
		for(int i = 0;i<scoreChange.length;i++) {
			scoreChange[i]=startingScores[i]-newScore[i];
		}
		return scoreChange;
	}
	public int[] getCurrentScore(){
		int[] currentScore =new int[players.size()];
		for(int i=0;i<players.size();i++){
			currentScore[i]=players.get(i).record.score;
		}
		return currentScore;
	}
	public ArrayList<Player> getWinningPlayerforRound(){
		int winningplayerscore =0;
		ArrayList<Player> winningplayers=new ArrayList<Player>();
		for (int i=0;i<players.size();i++){
			if(players.get(i).record.score>winningplayerscore){
				winningplayers.clear();
				winningplayerscore=players.get(i).record.score;
				winningplayers.add(players.get(i));
			}
			else{
				if(players.get(i).record.score==winningplayerscore&winningplayerscore>0){
					winningplayers.add(players.get(i));
				}
			}
		}
		return winningplayers;
	}
	public ArrayList<Player> getWinningPlayerforGame(){
		int winningplayerscore =0;
		ArrayList<Player> winningplayers=new ArrayList<Player>();
		for (int i=0;i<players.size();i++){
			if(players.get(i).record.score>winningplayerscore){
				winningplayers.clear();
				winningplayerscore=players.get(i).record.score;
				winningplayers.add(players.get(i));
			}
			else{
				if(players.get(i).record.score==winningplayerscore&winningplayerscore>0){
					winningplayers.add(players.get(i));
				}
			}
		}
		return winningplayers;
	}
}
