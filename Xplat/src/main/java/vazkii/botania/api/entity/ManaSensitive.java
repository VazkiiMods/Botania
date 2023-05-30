package vazkii.botania.api.entity;

import vazkii.botania.api.internal.ManaBurst;

/**
 * An entity with this component reacts in some way when hit with a mana burst.
 */
public interface ManaSensitive {

	/**
	 * Called when the entity is hit by a burst.
	 * 
	 * @return Whether the burst should be destroyed
	 */
	boolean onBurstCollision(ManaBurst burst);

}
