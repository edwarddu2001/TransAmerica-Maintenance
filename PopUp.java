import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class PopUp extends JPanel implements ActionListener{
	//private JPanel popUps;
	//private JPanel aiPopup;
	private JFrame frame;
	private JLabel question;
	private JTextField gameNumber;
	private JButton fast, slow;
	private int numberOfGivenGames;
	ArrayList<Player> players;

	MainMenu main;
	public PopUp(MainMenu m, ArrayList<Player> players){
		main=m;
		this.players = players;
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		question = new JLabel("How many games are to be played?");
		this.add(question);

		gameNumber = new JTextField();
		gameNumber.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String numOfGames = gameNumber.getText();
				int number = Integer.parseInt(numOfGames);
				numberOfGivenGames = number;
			}

		});
		this.add(gameNumber);

		fast = new JButton("Fast Mode");
		fast.addActionListener(this);
		this.add(fast);

		slow = new JButton("Slow Mode");
		slow.addActionListener(this);
		this.add(slow);

		frame = new JFrame("Strategy Analysis Mode");
		frame.setSize(300, 150);
		frame.setResizable(false);
		frame.add(this);
		frame.setVisible(true);
	}

	public int numberOfGamesToBeRun(){
		return numberOfGivenGames;
	}

	public int[] sort(int[] gamesWon){
		int[] sorted = new int[gamesWon.length];
		int maximum = 0;
		for(int counter = 1; counter<=sorted.length;counter++){
			int i = 0;
			int maxIndex = 0;
			for(;i<gamesWon.length;i++){
				if(gamesWon[i]>maximum){
					maximum = (int) gamesWon[i];
					maxIndex = i;
				}
			}
			sorted[maxIndex]=counter;
			gamesWon[maxIndex]=0;
			maximum = 0;
		}
		return sorted;
	}

	public int[][] runGames(int games, ArrayList<Player> players,boolean slowmode){
		int[] gamesWon = new int[players.size()];
		int[] gamesLost = new int[players.size()];
		for(;games>0;games--){
			System.out.println("Start Game");
			Game game = new Game(players, slowmode);
			game.runGame();
			ArrayList<Player> winningPlayers = game.getWinningPlayerforGame();
			
			for(Player p: winningPlayers){
				for(int i = 0;i<players.size();i++){
					if(p.equals(players.get(i))){
						gamesWon[i]++;
					}else{
						gamesLost[i]++;
					}
				}
			}
			System.out.println("End Game");
		}
		int[] rank = sort(gamesWon);
		int[][] bigArray = {gamesWon,gamesLost,rank};
		return bigArray;

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int games;
		try {
			games=Integer.parseInt(gameNumber.getText());
		}
		catch (NumberFormatException num) {
			return;
		}
		if(games>0){
			if(e.getSource().equals(fast)){
				int[][] info = runGames(games,players,false);
				double[] winPercentage = new double[players.size()];
				for(int i = 0;i<players.size();i++){
					double winPer = info[0][i]/(info[0][i]+info[1][i]);
					winPercentage[i]=winPer;
				}
				ComputerStrategyScreen screen = new ComputerStrategyScreen(games,info[0],info[1],info[2],winPercentage, players);
				TransAmerica.transamerica.add(screen);
				TransAmerica.transamerica.remove(this);
				TransAmerica.transamerica.dispose();
				JFrame f = new JFrame();
				TransAmerica.transamerica = f;
				TransAmerica.transamerica.add(screen);
				TransAmerica.transamerica.setTitle("TransAmerica");
				TransAmerica.transamerica.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				TransAmerica.transamerica.dispose();
				TransAmerica.transamerica.setUndecorated(true);
				TransAmerica.transamerica.setExtendedState(JFrame.MAXIMIZED_BOTH);
				TransAmerica.transamerica.setVisible(true);
				TransAmerica.transamerica.repaint();
				frame.dispose();

			}else if(e.getSource().equals(slow)){
				for(int i=0;i<games;i++){
					System.out.println("Start Game");
					MainGameScreen screen = new MainGameScreen(new Game(players,true));
					TransAmerica.transamerica.add(screen);
					TransAmerica.transamerica.remove(0);
					TransAmerica.transamerica.dispose();
					JFrame f = new JFrame();
					f.add(screen);
					TransAmerica.transamerica = f;
					TransAmerica.transamerica.setTitle("TransAmerica");
					TransAmerica.transamerica.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					TransAmerica.transamerica.dispose();
					TransAmerica.transamerica.setUndecorated(true);
					TransAmerica.transamerica.setExtendedState(JFrame.MAXIMIZED_BOTH);
					TransAmerica.transamerica.setVisible(true);
					TransAmerica.transamerica.repaint();
					frame.dispose();
					System.out.println("End Game");
				}
			}
		}	
	}



}
