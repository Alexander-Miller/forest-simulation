package forest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElderTree extends Tree {

	public ElderTree(int age) {
		super(age);
	}
	
	public ElderTree() {
		super(120);
	}

	@Override
	public Optional<Field> update(List<Field> neighbours) {
		double rand = Math.random();
		if (rand > 0.2) {
			return Optional.ofNullable(null);
		} else {
			List<Field> filterLst = neighbours.stream()
					.filter(field -> !field.treePresent())
					.collect(Collectors.toList());
				if (!filterLst.isEmpty()) {
					return Optional.of(filterLst.get((int) (Math.random() * filterLst.size())));
				} else {
					return Optional.ofNullable(null);
				}
		}
	}

	@Override
	public int harvest() {
		return 2;
	}
	
}
