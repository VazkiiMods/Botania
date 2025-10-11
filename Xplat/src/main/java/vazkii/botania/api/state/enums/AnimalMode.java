package vazkii.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.animal.Animal;

import java.util.function.Predicate;

public enum AnimalMode implements StringRepresentable, Predicate<Animal> {
	ALL("all_animals", Animal::isAlive),
	ADULTS("adult_animals", animal -> animal.isAlive() && !animal.isBaby()),
	BABIES("baby_animals", animal -> animal.isAlive() && animal.isBaby());

	private final String name;
	private final Predicate<Animal> predicate;

	AnimalMode(String name, Predicate<Animal> predicate) {
		this.name = name;
		this.predicate = predicate;
	}

	@Override
	public boolean test(Animal animal) {
		return predicate.test(animal);
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
