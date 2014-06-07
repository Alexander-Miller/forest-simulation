package forest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Forest {
	
	private final Field[][] FIELDS;
	private final List<Field> FIELD_LST;
	private final int SIZE;
	
	private final Logger LOGGER;
	
	private int time = 0;
		
	Forest(int size) {
		this.SIZE = size;
		FIELDS = new Field[size][size];
		FIELD_LST = new ArrayList<>();
		this.LOGGER = new Logger();
		
		makeForest();
		fillForest();
	}
	
	private void makeForest() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Field field = new Field(i, j);
				FIELDS[i][j] = field;
				FIELD_LST.add(field);
			}
		}
	}
	
	private void fillForest() {                 
		fill(0.1, LumberJack.class, x -> LOGGER.updateLumberJacks(x));
		fill(0.5, AdultTree.class, x -> LOGGER.updateAdultTrees(x));
		fill(0.02, Bear.class, x -> LOGGER.updateBears(x));
	}
	
	private void fill(double percent, Class<? extends ForestElement> type, IntConsumer counter) {
		try {
			int times = (int) (SIZE * SIZE * percent);
			List<Field> emptyFields = FIELD_LST.stream()
				.filter(field -> field.isEmpty())
				.collect(Collectors.toList());
			for (;times > 0; times--) {
				int index = (int) (Math.random() * emptyFields.size());
				Field field = emptyFields.get(index);
				field.occupy(type.newInstance());
				emptyFields.remove(field);
				counter.accept(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Field[] row : FIELDS) {
			for (Field field : row) {
				b.append(field);
			}
			b.append(System.lineSeparator());
		}
		return b.toString();
	}
	
	public void tick() {
		time++;
		updateTrees();
		updateLumberJacks();
		updateBears();
		if (time % 12 == 0) {
			lumberQuotaCheck();
			bearQuotaCheck();
		}
		log();
	}
	
	private void log() {
		LOGGER.monthlyLog(time);
		if (time % 12 == 0) LOGGER.yearlyLog(time / 12);
		System.out.println(this);
	}	
	
	private void lumberQuotaCheck() {
		if (LOGGER.lumberjacks > 1 && LOGGER.yearlyWood < LOGGER.lumberjacks) {
			for (int i = LOGGER.lumberjacks - LOGGER.yearlyWood; i > 0 && LOGGER.lumberjacks > 1; i--) {
				getRandom(f -> f.lumberJackPresent()).ifPresent(field -> {
					field.leaveLumberJack();
					LOGGER.updateLumberJacksHired(-1);
					LOGGER.updateLumberJacks(-1);
				});
			}
		} else if (LOGGER.yearlyWood > LOGGER.lumberjacks) {
			int newHires = 1;
			while (LOGGER.yearlyWood > LOGGER.lumberjacks * (newHires + 1)- 1) {
				newHires++;
			}
			for (int i = 0; i < newHires; i++) {
				newSpawn(LumberJack.class, f -> !f.bearPresent() && !f.lumberJackPresent());
				LOGGER.updateLumberJacksHired(1);
				LOGGER.updateLumberJacks(1);
			}
		}
	}
	
	private void bearQuotaCheck() {
		if (LOGGER.yearlyMawings > 0) {
			getRandom(field -> field.bearPresent()).ifPresent(field -> field.leaveBear());
			LOGGER.updateBearsCaught(1);
		} else {
			newSpawn(Bear.class, field -> !field.bearPresent());
			LOGGER.updateBearsCaught(-1);
		}
	}
	
	private void newSpawn(Class<? extends ForestElement> type, Predicate<Field> test) {
		getRandom(test).ifPresent(field -> {
			try {
				field.occupy(type.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	private Optional<Field> getRandom(Predicate<Field> test) {
		List<Field> lst = FIELD_LST.stream()
			.filter(field -> test.test(field))
			.collect(Collectors.toList());
		if (lst.isEmpty()) {
			return Optional.ofNullable(null);
		} else {
			return Optional.of(lst.get((int) (Math.random() * lst.size())));
		}
	}
	
	private void updateTrees() {
		List<Field> treeFields = FIELD_LST.stream()
				.filter(field -> field.treePresent())
				.collect(Collectors.toList());
		
		if (treeFields.isEmpty()) {
			for (int i = 0; i < (int) (SIZE * SIZE * 0.33); i++) {
				newSpawn(Sapling.class, field -> !field.treePresent() && !field.lumberJackPresent());
			}
		} else {
			for (Field field : treeFields) {
				Tree tree = field.getTree().get();
				tree.age();
				List<Field> neighbours = getNeighbours(tree.getPos());
				Optional<Field> saplingField = tree.update(neighbours);
				saplingField.ifPresent(f -> { 
					f.occupyTree(new Sapling());
					LOGGER.updateSaplingTrees(1);
					LOGGER.updateMonthlySaplings(1);
				});

				if (tree.getAge() == 12) {
					field.occupyTree(new AdultTree());
					LOGGER.updateSaplingTrees(-1);
					LOGGER.updateAdultTrees(1);
					LOGGER.updateMonthlyAdults(1);
				} else if (tree.getAge() == 120) {
					field.occupyTree(new ElderTree());
					LOGGER.updateAdultTrees(-1);
					LOGGER.updateElderTrees(1);
					LOGGER.updateMonthlyElders(1);
				}
			}
		}
	}
	
	private void updateLumberJacks() {
		FIELD_LST.stream()
			.filter(field -> field.lumberJackPresent())
			.forEach(field -> {
				LumberJack lj = field.getLumberJack().get();
				wandering: for (int k = 0; k < 3; k++) {
					Optional<Field> newFieldOption = lj.update(getNeighbours(lj.getPos()));
					if (newFieldOption.isPresent()) {
						Field newField = newFieldOption.get();
						Field oldField = FIELDS[lj.getPos().x][lj.getPos().y];
						lj.move(oldField, newField);
						Optional<Tree> tree = newField.getTree();
						if (tree.isPresent() && tree.get().getAge() >= 12) {
							int harvest = tree.get().harvest();
							if (harvest == 1) {
								LOGGER.updateAdultTrees(-1);
							} else {
								LOGGER.updateElderTrees(-1);
							}
							LOGGER.updateMonthlyWood(harvest);
							newField.leaveTree();
							break wandering;
						}
					}
				}
			});
	}
	
	private void updateBears() {
		FIELD_LST.stream()
			.filter(field -> field.bearPresent())
			.forEach(field -> {
				Bear bear = field.getBear().get();
				wandering : for (int k = 0; k < 5; k++) {
					if (field.lumberJackPresent()) {
						field.leaveLumberJack();
						LOGGER.updateMonthlyMawings(1);
						LOGGER.updateLumberJacks(-1);
						if (LOGGER.lumberjacks == 0) {
							newSpawn(LumberJack.class, f -> !f.bearPresent() && ! f.lumberJackPresent());
							LOGGER.updateLumberJacksHired(1);
							LOGGER.updateLumberJacks(1);
						}
						break wandering;
					} else  {
						Optional<Field> newFieldOption = bear.update(getNeighbours(bear.getPos()));
						if (newFieldOption.isPresent()) {
							Field newField = newFieldOption.get();
							Field oldField = FIELDS[bear.getPos().x][bear.getPos().y];
							bear.move(oldField, newField);
						}
							
					}
				}
			});
	}
	
	private List<Field> getNeighbours(Pair pair) {
		List<Pair> lst = new ArrayList<>();
		IntStream.rangeClosed(pair.x - 1, pair.x + 1).forEach(x -> {
			IntStream.rangeClosed(pair.y - 1 , pair.y + 1).forEach(y -> {
				lst.add(new Pair(x, y));
			});
		});
		return lst.stream()
			.filter(p -> p.x >= 0 && p.x < SIZE &&
			             p.y >= 0 && p.y < SIZE &&
			            (p.x != pair.x || p.y != pair.y))
			.map(p -> FIELDS[p.x][p.y])
			.collect(Collectors.toList());
	}
	
	public void go(int x) {
		for (int i = 0; i < x; i++) {
			tick();
		}
	}
	
	public static void main(String[] args) {
		Forest forest = new Forest(20);
		System.out.println(forest);
		forest.go(500);
	}
	
}
