import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapofUSA extends JPanel implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
		/*
		rounding error on left side of screen
		make rails not be placed at all if invalid, 
		make markers be placed in valid position	
	
		*/
		//Need to Make turns not skipped and make sure turns work properly
		//ALSO NEEDS TO DRAW MOUNTAINS SOMEHOW: BIGGER SIZE RAIL?
		BufferedImage Map;
		Player currentPlayer;
		static Grid currentGrid;
		
		private int[] siz;
		static ArrayList<City> allCities;		

		MapofUSA(int x, int y, int width, int height,Grid grid){
			currentGrid=grid;
			try {
				Map=ImageIO.read(new File("Pix\\mapofusa.png"));
			} catch (IOException e1) {}
			siz=new int[]{width,height};
			this.setBounds(x, y, width, height);
			scalefactor = new int[]{siz[0]/Grid.boardwidth,siz[1]/Grid.boardheight};
			scalefactord = new double[]{((double)siz[0])/Grid.boardwidth,((double)siz[1])/Grid.boardheight};
			try {
				highlighted=new Rail(new Position(0,0),new Position(0,1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			final MapofUSA test = this;
			this.addMouseMotionListener(new MouseMotionListener(){
				public void mouseDragged(MouseEvent e) {}
				public void mouseMoved(MouseEvent e) {
					test.mouseMoved(e);
				}
			});
			this.addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e) {test.mouseClicked(e);}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});
		}
		void setCurrentGrid(Grid grid){//called at beginning of round
			currentGrid = grid;
		}
		void setCurrentPlayer(Player player){//called at beginning of each turn
			currentPlayer = player;
		}
		Object returnPlacedRail(){//checks placedMarker in Player, if true, returns marker info instead
			if(firstturn){
				if(placedmarker!=null){
					Marker red = placedmarker;
					placedmarker=null;
					return red;
				}
				return null;
			}
			else{
				Rail r =placedRail;
				placedRail=null;
				return r;
			}
		}
		
		Rail highlighted;
		Position highlightedmarker=new Position(0,0);
		int[] scalefactor;
		double[] scalefactord;
		int[] mos=new int[]{0,0};
		int[] mos2=new int[]{0,0};
		int markersize=35;
		int citysize=14;
		
		void drawposline(double x1, double y1, double x2, double y2, Graphics g){
			y1++;
			y2++;
			g.drawLine((int)(scalefactor[0]*(y1%2==0?x1+0.5d:x1)), (int)(siz[1]-scalefactor[1]*y1), (int)(scalefactor[0]*(y2%2==0?x2+0.5d:x2)), (int) (siz[1]-scalefactor[1]*y2));
		}
		
		public void paint(Graphics g){
			Graphics2D g2d = (Graphics2D)g;
			g.setColor(Color.gray);
			g2d.setStroke(new BasicStroke(6,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			g.drawRect(0, 0, (int) ((Grid.boardwidth-0.5d)*scalefactor[0]), (Grid.boardheight-1)*scalefactor[1]);
			g.drawImage(Map,-50, -30, siz[0]+70, siz[1]+30, null);
			if(currentGrid==null||currentPlayer==null)
				return;
//			for(int i=siz[0]/3;i<siz[0];i++){
//				for(int j=3*siz[1]/4;j<siz[1];j++){
////					try {
////						Rail r= nearestRail(i,j);
////						
////					} catch (Exception e) {}
//					int c= colorcode(i,j);
//					if(c==0){
//						g.setColor(Color.BLACK);//doesn't work for x=0
//					}
//					if(c==1){
//						g.setColor(Color.yellow);
//					}
//					if(c==2){
//						g.setColor(Color.BLUE);
//					}
//					if(c==3){
//						g.setColor(Color.green);
//					}
////					g.fillRect(i, j, 2, 2);
//					g.drawLine(i, j, i, j);
//				}
//			}
//			drawposline(zero.x,zero.y,one.x,one.y,g);
//			g.setColor(Color.black);
//			g.fillOval((int)(scalefactor[0]*(zero.y%2==1?zero.x+0.5d:zero.x))-markersize/2, siz[1]-scalefactor[1]*(zero.y+1)-markersize/2, markersize, markersize);
//			g.setColor(Color.white);
//			g.fillOval((int)(scalefactor[0]*(one.y%2==1?one.x+0.5d:one.x))-markersize/2, siz[1]-scalefactor[1]*(one.y+1)-markersize/2, markersize, markersize);
//			
//			g.setFont(new Font("Arial",1,34));
//			g.setColor(Color.black);
//			g.drawString(currentGrid.distbetweenpoints(zero, one)+"", 800, -80);
//			g.drawString(currentGrid.distbetweenpoints2(zero, one)[0]+" "+currentGrid.distbetweenpoints2(zero, one)[1], 900, -80);
			
			g.setColor(currentPlayer.getColor());
			if(firstturn){
				g.fillOval((int)(scalefactor[0]*(highlightedmarker.y%2==1?highlightedmarker.x+0.5d:highlightedmarker.x))-markersize/2, siz[1]-scalefactor[1]*(highlightedmarker.y+1)-markersize/2, markersize, markersize);
				
			}else{
				g2d.setStroke(new BasicStroke(8,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				if(currentGrid.alllegalrails.contains(highlighted))
					g.drawLine((int)(scalefactor[0]*(highlighted.p1.y%2==1?highlighted.p1.x+0.5d:highlighted.p1.x)), siz[1]-scalefactor[1]*(highlighted.p1.y+1), (int)(scalefactor[0]*(highlighted.p2.y%2==1?highlighted.p2.x+0.5d:highlighted.p2.x)), siz[1]-scalefactor[1]*(highlighted.p2.y+1));
			}
//			g.drawString(highlightedmarker.x+" "+highlightedmarker.y, 0, 50);
			g.setColor(Color.black);
			for(Rail r: currentGrid.alllegalrails){
				g2d.setStroke(new BasicStroke(r.size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				g.drawLine((int)(scalefactor[0]*((r.p1.y)%2==1?r.p1.x+0.5d:r.p1.x)), siz[1]-scalefactor[1]*(r.p1.y+1), (int)(scalefactor[0]*((r.p2.y)%2==1?r.p2.x+0.5d:r.p2.x)), siz[1]-scalefactor[1]*(r.p2.y+1));
			}
			for(int i=0;i<currentGrid.allRails.size();i++){
				Rail r = currentGrid.allRails.get(i);
				if(r.player!=null){
					g.setColor(r.player.getColor());
					g2d.setStroke(new BasicStroke(4+r.size*3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				}else{
					g.setColor(Color.black);
					g2d.setStroke(new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				}
				g.drawLine((int)(scalefactor[0]*((r.p1.y)%2==1?r.p1.x+0.5d:r.p1.x)), siz[1]-scalefactor[1]*(r.p1.y+1), (int)(scalefactor[0]*((r.p2.y)%2==1?r.p2.x+0.5d:r.p2.x)), siz[1]-scalefactor[1]*(r.p2.y+1));
			}
			for(int i = 0;i<currentGrid.markers.size();i++) {
				Marker r = currentGrid.markers.get(i);
				if(r.player!=null){
					g.setColor(r.player.getColor());
					g2d.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				}else{
					g.setColor(Color.black);
					g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				}
				g.fillOval((int)(scalefactor[0]*((r.p.y)%2==1?r.p.x+0.5d:r.p.x))-markersize/2, siz[1]-scalefactor[1]*(r.p.y+1)-markersize/2, markersize, markersize);
			}
			g.setFont(new Font("Arial",1,14));
			for(City[] cer : currentGrid.allcities){
				for(City c : cer){
					HumanPlayer h=null;
					try{
						h= (HumanPlayer)currentPlayer;
						if(h.record.cities.contains(c)){
							g.setColor(Color.magenta);
							g.fillOval((int)(scalefactor[0]*((c.p.y)%2==1?c.p.x+0.5d:c.p.x))-(citysize+10)/2, siz[1]-scalefactor[1]*(c.p.y+1)-(citysize+10)/2, citysize+10, citysize+10);
						}
					}catch(Exception E){}
					
					g.setColor(c.color);
					g.drawString(c.getName(), -25+(int)(scalefactor[0]*((c.p.y)%2==1?c.p.x+0.5d:c.p.x))-citysize/2, siz[1]-scalefactor[1]*(c.p.y+1)-citysize/2);
					g.fillOval((int)(scalefactor[0]*((c.p.y)%2==1?c.p.x+0.5d:c.p.x))-citysize/2, siz[1]-scalefactor[1]*(c.p.y+1)-citysize/2, citysize, citysize);
				}
			}
//			for(int i=0;i<currentPlayer.record.citiesReached.size();i++){
//				g.setColor(Color.black);
//				g.drawString(currentPlayer.record.citiesReached.get(i).getName(), 0, -10-10*i);
//			}
		}
		ArrayList<Rail> ok =new ArrayList<Rail>();
		
		Rail colortoRail(int one,int x, int y){
			Rail returnrail=null;
			Position p1=null,p2=null;
			x+=scalefactor[0];//This fixes a strange rounding error on left side
			switch (one){
				case 2: 
					if(((siz[1]-y-scalefactor[1]/2-1)/scalefactor[1])%2==1){
						x-=scalefactor[0]/2;
					}
					p1=new Position(x/scalefactor[0],((siz[1]-y-scalefactor[1]/2-1)/scalefactor[1]));
					p2=new Position(x/scalefactor[0]+1,((siz[1]-y-scalefactor[1]/2-1)/scalefactor[1]));
					
				break;
				case 1: 
					if(((siz[1]-y-1)/scalefactor[1])%2==1){
						x-=scalefactor[0]/2;
						p1=new Position(x/scalefactor[0]+1,((siz[1]-y-1)/scalefactor[1])-1);
						p2=new Position(x/scalefactor[0]+1,((siz[1]-y-1)/scalefactor[1]));
					}else{
						p1=new Position(x/scalefactor[0],((siz[1]-y-1)/scalefactor[1])-1);
						p2=new Position(x/scalefactor[0]+1,((siz[1]-y-1)/scalefactor[1]));
					}
				break;
				case 3: one=3;
					if(((siz[1]-y-1)/scalefactor[1])%2==1){
						x-=scalefactor[0]/2;
						p1=new Position(x/scalefactor[0]+1,((siz[1]-y-1)/scalefactor[1])-1);
						p2=new Position(x/scalefactor[0],((siz[1]-y-1)/scalefactor[1]));
					}else{
						p1=new Position(x/scalefactor[0],((siz[1]-y-1)/scalefactor[1])-1);
						p2=new Position(x/scalefactor[0],((siz[1]-y-1)/scalefactor[1]));
					}
				break;
			}
			try {
				returnrail=new Rail(new Position(p1.x-1,p1.y),new Position(p2.x-1,p2.y),currentPlayer);
			} catch (Exception e) {}
			return returnrail;
		}
		int colorcode(int x,int y){
			if(y+scalefactor[1]>siz[1])
				return 0;
			int[] init= new int[]{x,y};
			if(((siz[1]-y-scalefactor[1]/2-1)/scalefactor[1])%2==1){
				x-=scalefactor[0]/2;
			}
			if(y%scalefactor[1]<oddmod(x,scalefactord[0]/2)/2){
				return 2;
			}
			y=siz[1]-y;
			if(y%scalefactor[1]<oddmod(x,scalefactord[0]/2)/2){
				return 2;
			}
			x=init[0];
			if(x%scalefactord[0]<scalefactord[0]/2){
				if(((siz[1]-y-1)/scalefactor[1])%2==1){
					return 3;
				}
				return 1;
				
			}
			if(((siz[1]-y-1)/scalefactor[1])%2==1){
				return 1;
			}
			return 3;
		}
		Rail nearestRail(int x, int y) throws Exception{
			int c = colorcode(x,y);
			return colortoRail(c,x,y);
		}
		
		
		static double oddmod(double ee, double er){
			ee%=er*2;
			if(ee<er){
				return ee;
			}
			return er-ee%er;
		}
		static boolean firstturn=true;
		static Marker placedmarker;
		Rail placedRail;
//		Position zero=new Position(0,0),one=new Position(0,1);
		public void mouseClicked(MouseEvent e) {//when mouse is clicked, converts click (x, y) coordinates to grid coordinates, and then uses the grid validrail method to determine if rail is valid, if it is then add to lastClick, else ignore that it was clicked;
			int x = e.getX();
			int y = e.getY();
			if(firstturn){
				placedmarker=new Marker(nearestPosition2(x,y),currentPlayer);
			}else{
				try {
					if(nearestRail(x,y)!=null)
						placedRail = nearestRail(x,y);
				} catch (Exception e1) {}
			}
//			if(e.getButton()==e.BUTTON1){
//				zero=nearestPosition2(x,y);
//			}else{
//				one=nearestPosition2(x,y);
//			}
		}
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			mos=new int[]{x,(int) (y-oddmod(x,scalefactor[0]/2)/2)};
			if(firstturn){
				highlightedmarker = nearestPosition2(x,y);
			}else{
				try {
					if(nearestRail(x,y)!=null)
						highlighted = nearestRail(x,y);
				} catch (Exception e1) {}
			}
		}
		private Position nearestPosition2(int x, int y) {
			if(((siz[1]-y-scalefactor[1]/2-1)/scalefactor[1])%2==1)
				return new Position((int)Math.round(((double)(x-scalefactor[0]/2))/scalefactor[0]),(siz[1]-(y+scalefactor[1]/2)-1)/scalefactor[1]);
			else
				return new Position((int)Math.round(((double)x)/scalefactor[0]),(siz[1]-(y+scalefactor[1]/2)-1)/scalefactor[1]);
		}	
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
}
