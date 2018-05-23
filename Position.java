
public class Position {
	
	public int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(double d, double e) {
		this((int)d,(int)e);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public String toString(){
		return x+" "+y;
	}
	public boolean equals(Object c) {
		try{
			Position compare = (Position)c;
			if(x == compare.x && y == compare.y)
				return true;
		}catch(Exception E){
			return false;
		}
		return false;
	}

}
