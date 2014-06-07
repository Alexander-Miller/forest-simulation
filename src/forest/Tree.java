package forest;


public abstract class Tree extends ForestElement {
	
	private int age;
	
	public Tree(int age) {
		this.age = age;
	}
	
	public int getAge() {
		return age;
	}
	
	public void age() {
		age++;
	}
	
	public abstract int harvest();
	
	@Override
	public void leave(Field field) {
		field.leaveTree();
	}
	
	public void occupy(Field field) {
		field.occupyTree(this);
	}
	
}
