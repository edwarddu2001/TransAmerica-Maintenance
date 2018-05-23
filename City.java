import java.awt.Color;

public class City {
	
	private String name;
	Position p;
	Color color;
	
	public City(String name, Position pos, Color color) {
		this.name = name;
		this.p = pos;
		this.color = color;
	}
	public boolean equals(Object compare){
		City compare2;
		try{
			compare2=(City)compare;
		}catch(Exception e){
			return false;
		}
		if(name.equals(compare2.name)){
			return true;
		}
		return false;
	}
	String getName(){
		return name;
	}
	
	Position getPos(){
		return p;
	}
}
