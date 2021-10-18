/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A list that does not accept duplicates and uses a set to implement fast {@link #contains(Object)}.
 * In addition, equality and hashcode use identity instead of checking the list contents.
 * Certain operations ({@link #set(int, Object)}, {@link #clone()} and {@link #replaceAll(UnaryOperator)})
 * throw {@link UnsupportedOperationException}.
 */
public class SparkArrayList<E> extends ArrayList<E> {
	private final Set<E> set = new HashSet<>();

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(E e) {
		if (set.add(e)) {
			super.add(e);
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, E element) {
		if (set.add(element)) {
			super.add(index, element);
		}
	}

	@Override
	public E remove(int index) {
		E e = super.remove(index);
		set.remove(e);
		return e;
	}

	@Override
	public boolean remove(Object o) {
		set.remove(o);
		return super.remove(o);
	}

	@Override
	public void clear() {
		set.clear();
		super.clear();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean ret = false;
		for (E e : c) {
			ret |= add(e);
		}
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return addAll(c); // Yes.
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = fromIndex; i < toIndex; i++) {
			set.remove(get(i));
		}
		super.removeRange(fromIndex, toIndex);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		set.removeAll(c);
		return super.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		set.retainAll(c);
		return super.retainAll(c);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		set.removeIf(filter);
		return super.removeIf(filter);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	// Spark networks are cached in a weak map keyed with lists of corporea sparks
	// The same contents will always use the same instance, so skip all those checks and just use identity
	@Override
	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}
}
