/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Objects;

// Generic base class for unlisted properties
public class PropertyObject<T> implements IUnlistedProperty<T> {

	private final String name;
	private final Class<T> clazz;
	private final Predicate<T> validator;
	private final Function<T, String> stringFunction;

	public PropertyObject(String name, Class<T> clazz, Predicate<T> validator, Function<T, String> stringFunction) {
		this.name = name;
		this.clazz = clazz;
		this.validator = validator;
		this.stringFunction = stringFunction;
	}

	public PropertyObject(String name, Class<T> clazz) {
		this(name, clazz, Predicates.<T>alwaysTrue(), new Function<T, String>() {
			@Override
			public String apply(T input) {
				return Objects.toString(input);
			}
		});
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(T value) {
		return validator.apply(value);
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}

	@Override
	public String valueToString(T value) {
		return stringFunction.apply(value);
	}
}
