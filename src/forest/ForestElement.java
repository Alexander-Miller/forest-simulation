package forest;

import java.util.List;
import java.util.Optional;

public abstract class ForestElement {
	
	private int x;
	private int y;
			
	public Pair getPos() { return new Pair(x ,y); }
	
	public void setPos(Pair pair) { this.x = pair.x; this.y = pair.y; }
	
	public abstract void occupy(Field field);
		
	public abstract void leave(Field field);
	
	public abstract Optional<Field> update(List<Field> neighbours);
}
