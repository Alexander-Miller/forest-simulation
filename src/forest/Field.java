package forest;

import java.util.Optional;

public class Field {
	
	private Optional<Tree> tree;
	private Optional<Bear> bear;
	private Optional<LumberJack> lumberjack; 
	
	private final Pair pos;
	
	Field (int x, int y) {
		this.pos = new Pair(x, y);
		this.tree = Optional.ofNullable(null);
		this.bear = Optional.ofNullable(null);
		this.lumberjack = Optional.ofNullable(null);
	}
	
	public boolean isEmpty() {
		return !tree.isPresent() && !bear.isPresent() && !lumberjack.isPresent();
	}
	
	public void occupy(ForestElement e) {
		e.occupy(this);
	}
	
	public void occupyTree(Tree tree) {
		this.tree = Optional.ofNullable(tree);
		tree.setPos(pos);
	}
	
	public void occupyBear(Bear bear) {
		this.bear = Optional.ofNullable(bear);
		bear.setPos(pos);
	}
	
	public void occupyLumberJack(LumberJack lumberjack) {
		this.lumberjack = Optional.ofNullable(lumberjack);
		lumberjack.setPos(pos);
	}
	
	public void leave(ForestElement e) {
		e.leave(this);
	}
	
	public void leaveTree() {
		this.tree = Optional.ofNullable(null);
	}
	
	public void leaveBear() {
		this.bear = Optional.ofNullable(null);
	}
	
	public void leaveLumberJack() {
		this.lumberjack = Optional.ofNullable(null);
	}
	
	public boolean treePresent() {
		return tree.isPresent();
	}
	
	public boolean bearPresent() {
		return bear.isPresent();
	}
	
	public boolean lumberJackPresent() {
		return lumberjack.isPresent();
	}
	
	public Optional<Tree> getTree() {
		return tree;
	}
	
	public Optional<Bear> getBear() {
		return bear;
	}
	public Optional<LumberJack> getLumberJack() {
		return lumberjack;
	}
	
	@Override 
	public String toString() {
		String t = tree.map(tr -> "T").orElse(" ");
		String b = bear.map(br -> "B").orElse(" ");
		String l = lumberjack.map(lj -> "L").orElse(" ");
		return "[" + b + t + l + "]";
	}
	
}
