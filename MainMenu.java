import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu extends JPanel implements ActionListener{


//	private JPanel[] panes = new JPanel[4];
	private JButton start = new JButton("Play"), exit = new JButton("Exit");
	private JButton[] buttons = new JButton[] {start, exit};
	PlayerPanel[] playerPanels = new PlayerPanel[6];

	MainMenu() {
		this.setLayout(null);		

//		for(int i = 0; i < panes.length; i++) {
//			panes[i] = new JPanel();
//			panes[i].setBackground(Color.WHITE);
//			this.add(panes[i]);
//		}
		JLabel Title =new JLabel("TransAmerica");
		Title.setFont(new Font("Arail", 1, 100));
		Title.setSize(700, 75);
		Title.setLocation(800-350, 50);
		this.add(Title);
		for(int i = 0; i <= 2; i++) {
			playerPanels[i] = new PlayerPanel(i + 1);
			playerPanels[i].setSize(350, 290);
			playerPanels[i].setLocation(800+(450*(i-1))-175,250-100);
			this.add(playerPanels[i]);
		}

		for(int i = 3; i <= 5; i++) {
			playerPanels[i] = new PlayerPanel(i + 1);
			playerPanels[i].setSize(350, 290);
			playerPanels[i].setLocation(800+(450*(i-4))-175,550-100);
			this.add(playerPanels[i]);
		}
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(this);
			buttons[i].setActionCommand(buttons[i].getText());
			buttons[i].setSize(100,50);
			buttons[i].setLocation((int) (800+200*(i-.5))-50,760);
			this.add(buttons[i]);
		}
		try{
			backg= ImageIO.read(new File("Pix/TransAmerica Background.jpg"));
		}catch(Exception E){}
		
	}
	BufferedImage backg;
	public void paint(Graphics g){
		g.drawImage(backg, 0, 0, 1600, 900, null);
		for(int i=0;i<this.getComponentCount();i++){
			g.translate(this.getComponent(i).getX(), this.getComponent(i).getY());
			this.getComponent(i).paint(g);
			g.translate(-this.getComponent(i).getX(), -this.getComponent(i).getY());
		}
		
	}
	
	public ArrayList<Player> generate(ArrayList<Color> playerColors, ArrayList<String> playerNames, ArrayList<String> playerType){
		ArrayList<Player> players = new ArrayList<Player>();
		boolean slowMode = false;
		for(int i = 0; i<playerType.size();i++){
			if(playerType.get(i).equals("Human")){
				players.add(new HumanPlayer(playerColors.get(i),playerNames.get(i)));
				slowMode = true;
			}else if(playerType.get(i).equals("Easy")){
				players.add(new EasyStrategy(playerColors.get(i),playerNames.get(i)));
			}else{
				int[] playerScores = new int[players.size()];
				for(int j = 0;j<playerScores.length;j++){
					playerScores[j] = 12;
				}
				players.add(new HardStrategy(playerColors.get(i),playerScores, playerNames.get(i)));
			}
		}
		return players;
		
	}
//	Timer t= new Timer();
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Play")) {
			int readyPlayers = 0;
			for(PlayerPanel p:playerPanels){
				if(p.isPlayer()){
					readyPlayers++;
				}
			}
			if(readyPlayers>=2){//starts the game, either human or all ai

				ArrayList<PlayerPanel> validPanels = new ArrayList<PlayerPanel>();
				for(PlayerPanel p:playerPanels){
					if(p.isPlayer()){
						validPanels.add(p);
					}
				}
				boolean humanGame = false;
				for(PlayerPanel p: validPanels){
					if(p.isHuman()){
						humanGame = true;
					}
				}
				ArrayList<Player> players = new ArrayList<Player>();//need to actually get the players
				ArrayList<Color> playerColors = new ArrayList<Color>();
				ArrayList<String> playerNames = new ArrayList<String>();
				ArrayList<String> playerType = new ArrayList<String>();
				for(PlayerPanel p : validPanels){
					if(p.isHuman()){
						playerColors.add(p.getBackground());
						playerNames.add(p.getName());
						playerType.add("Human");
					}else{
						playerColors.add(p.getBackground());
						playerNames.add(p.getName());
						playerType.add(p.getStrategy());
					}
				}
				players = generate(playerColors, playerNames, playerType);
				if(humanGame){
					
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
					
					//HEY THIS WORKS BUT IS TERRIBLE

					/*for(int i = 0;i<playerColors.size();i++){
						System.out.println(playerColors.get(i));
					}
					for(int i = 0;i<playerColors.size();i++){
						System.out.println(playerNames.get(i));
					}
					for(int i = 0;i<playerColors.size();i++){
						System.out.println(playerType.get(i));
					}*/
					System.out.println("Reached!");
				}else{
					//progress to ai game
//					something.popup;
//					ComputerStrategyScreen screen = new ComputerStrategyScreen();//pass this all the info from popup
//					add(screen);
					PopUp aiGamePopUp = new PopUp(this,players);
				}
			}
			else{//not enough players
				ErrorMessage error = new ErrorMessage();
			}
			
		} else if(e.getActionCommand().equals("Exit"))
			System.exit(0);
	}
	
class PlayerPanel extends JPanel implements ActionListener{
		
		private static final long serialVersionUID = 1L;
		private ButtonGroup group = new ButtonGroup();
		private Color[] colors = new Color[] {new Color(255, 40, 40), Color.YELLOW, new Color(0, 204, 0), Color.BLUE , new Color(139,69,19), Color.white};
		private String[] optionNames = {"None", "Human Player", "Computer"};
		private JRadioButton[] options = new JRadioButton[3];
		private String[] stratNames = {"Easy" , "Hard"};
		private JComboBox<String> strategy = new JComboBox<String>(stratNames);
		
		private int noPlayers = 6;
		private boolean player = false;
		private boolean humanPlayer = false;
		private JTextField name;

		int playernum;
		public void paint(Graphics g){
			g.setColor(this.colors[playernum]);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(int i=0;i<this.getComponentCount();i++){
				g.translate(this.getComponent(i).getX(), this.getComponent(i).getY());
				if(this.getComponent(i).isVisible()){
//					if(i!=0)
						g.drawImage(buttonImage, -10, -8, this.getComponent(i).getWidth()+20, this.getComponent(i).getHeight()+16, null);
					this.getComponent(i).paint(g);//needs to be specialized
				}
				g.translate(-this.getComponent(i).getX(), -this.getComponent(i).getY());
			}
		}
		BufferedImage buttonImage;
		public PlayerPanel(int playerNum) {
			try {
				buttonImage=ImageIO.read(new File("Pix\\button-design3.png"));
			} catch (IOException e1) {}
			playernum=playerNum-1;
			setBackground(colors[playerNum - 1]);
//			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setLayout(null);
			JLabel playernumber = new JLabel("Player " + playerNum);
			playernumber.setFont(new Font("Arial",Font.BOLD,24));
			playernumber.setLocation(25,15);
			playernumber.setSize(100, 30);
			add(playernumber);
			name = new JTextField();
			name.setText("Player " + playerNum);
			
			for(int i = 0; i < options.length; i++) {
				options[i] = new JRadioButton(optionNames[i]);
				options[i].addActionListener(this);
				options[i].setActionCommand(optionNames[i]);
				options[i].setBackground(new Color(0,0,0,0));
				options[i].setLocation(70,65+(int) (75*(i)));
				options[i].setSize(150, 20);
				group.add(options[i]);
				
				add(options[i]);
				
				if(i == 1) {
					add(name);
					name.setVisible(false);
					name.setBounds(90, 175, 60, 25);
				} else if(i == 2) {
					add(strategy);
					strategy.setVisible(false);
					strategy.setBounds(90, 252, 60, 25);
				}
			}
//			for(int i = 0; i < strategies.length; i ++) {
//				strategies[i] = new JComboBox(stratNames);
//			}
			this.setPreferredSize(new Dimension(400,200));
//			if(playerNum == 1 || playerNum == 2) {THIS ALSO BREAKS EVERYTHING
//				options[1].setSelected(true);
//			}
			if(playerNum == 1) {
				options[1].setSelected(true);
				noPlayers --;
				player = true;
				humanPlayer = true;
				name.setVisible(true);
				strategy.setVisible(false);
			} else if(playerNum >= 2 && playerNum <= 4) {
				options[2].setSelected(true);
				player = true;
				humanPlayer = false;
				name.setVisible(false);
				strategy.setVisible(true);
				strategy.setSelectedIndex(1);
			} else {
				options[0].setSelected(true);
				player = false;
				humanPlayer = false;
				name.setVisible(false);
				strategy.setVisible(false);
			}
				
		}
		
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(optionNames[0])) {
				player = false;
				humanPlayer = false;
				name.setVisible(false);
				strategy.setVisible(false);
			} else if(e.getActionCommand().equals(optionNames[1])) {
//				noPlayers --;
				player = true;
				humanPlayer = true;
				name.setVisible(true);
				strategy.setVisible(false);
				TransAmerica.transamerica.repaint();
			} else if(e.getActionCommand().equals(optionNames[2])) {
//				noPlayers --;
				player = true;
				humanPlayer = false;
				name.setVisible(false);
				strategy.setVisible(true);
			}
//			System.out.println(noPlayers);
		}
		
		public boolean isPlayer() {
			return player;
		}
		
		public boolean isHuman(){
			return humanPlayer;
		}
		
		public String getName() {
			return name.getText();
		}
		
		public String getStrategy() {
			return (String)strategy.getSelectedItem();
		}

	}
}



