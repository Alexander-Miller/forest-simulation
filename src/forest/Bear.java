package forest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Bear extends ForestElement implements Wandering {

	@Override
	public void occupy(Field field) {
		field.occupyBear(this);		
	}
	
	@Override
	public void leave(Field field) {
		field.leaveBear();
	}

	@Override
	public Optional<Field> update(List<Field> neighbours) {
		List<Field> filterLst = neighbours.stream()
			.filter(field -> !field.bearPresent())
			.collect(Collectors.toList());
		if (!filterLst.isEmpty()) {
			return Optional.of(filterLst.get((int) (Math.random() * filterLst.size())));
		} else {
			return Optional.ofNullable(null);
		}
	}
	
	@Override
	public void move(Field oldField, Field newField) {
		oldField.leaveBear();
		newField.occupyBear(this);
	}

}
