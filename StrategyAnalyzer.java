//Does the ArrayList of compPlayers mean that we are running multiple AIs against each other
//And then using the arrays to document the results of each individual AI?
import java.util.ArrayList;
public class StrategyAnalyzer{
	private int[] gamesWon;
	private int[] gamesLost;
	private int[] rank;
	private double[] winPercentage;
	private int players;
	private ArrayList<Player> AIs;
	/**
	 * Initializes the data-storing arrays
	 * Pre-supposes that all players passed are AIs
	 * @param computerPlayers Amount of players
	 */
	StrategyAnalyzer (ArrayList<Player> compPlayers){
		players = compPlayers.size();
		gamesWon = new int[players];
		gamesLost = new int[players];
		winPercentage = new double[players];
		AIs = compPlayers;
		rank = new int[players];
		for(int i = 0; i < players; i++)
			rank[i] = i;
	}
	/**
	 * Runs the amount of games passed and then
	 * calculates the results and displays them
	 * via the ComputerStrategyScreen
	 * @param games amount of games to be played
	 */
	public void runGames(int games){
		for(int i = 0; i < games; i++){
			Game game = new Game(AIs, false);
			calculateResults(i+1, game);
		}
		displayResults(games);
	}
	public int[] getLost(){
		return gamesLost;
	}
	public int[] getWon(){
		return gamesWon;
	}
	public int[] getRank(){
		return rank;
	}
	public double[] getPercetage(){
		return winPercentage;
	}
	/**
	 * Called after each game is run
	 * and sets the data arrays
	 */
	private void calculateResults(int games, Game game){
		//The position of the winning player
		ArrayList<Player> winners = game.getWinningPlayerforGame();
		int counter = 0;
		for(Player player : AIs){
			if(winners.contains(player))
				gamesWon[counter]++;
			else
				gamesLost[counter] = gamesLost[counter]++;
			winPercentage[counter] = gamesWon[counter]/games;
			counter++;
		}
		calculateRank();
	}
	/**
	 * Calculates the ranks, best rank is 0,
	 * worst possible rank is 5 (if playing with 6 players)
	 */
	private void calculateRank(){
		for(int j = 0; j < players; j++)
			for(int i = 0; i < players; i++)
				if(gamesWon[j] > gamesWon[i] && rank[j] < rank[i]){
					int tempRank = rank[j];
					rank[j] = rank[i];
					rank[i] = tempRank;
				}
	}
	/**
	 * Creates a ComputerStrategyScreen and passes
	 * it the amount of games played, gamesWon, gamesLost,
	 * rank, and winPercentage
	 * @param gamesPlayed the amount of games played
	 */
	private void displayResults(int gamesPlayed){
		ComputerStrategyScreen results = new ComputerStrategyScreen
				(gamesPlayed, gamesWon, gamesLost, rank, winPercentage, AIs);
	}
}