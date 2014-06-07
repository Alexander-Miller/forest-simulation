package forest;

import java.util.List;
import java.util.Optional;

public class Sapling extends Tree {

	public Sapling(int age) {
		super(age);
	}
	
	public Sapling() {
		super(0);
	}
	
	@Override
	public Optional<Field> update(List<Field> neighbours) {
		return Optional.ofNullable(null);
	}
	
	@Override
	public int harvest() {
		throw new RuntimeException("Someone tried to harvest a Sapling!");
	}

}
