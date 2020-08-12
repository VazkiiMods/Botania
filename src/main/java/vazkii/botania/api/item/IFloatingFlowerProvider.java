package vazkii.botania.api.item;

import javax.annotation.Nullable;

/**
 * Block Entity implementing this may conditionally return {@link IFloatingFlower}.
 * This is exposed for public reading, but do not implement it on anything.
 */
public interface IFloatingFlowerProvider {
	@Nullable
	IFloatingFlower getFloatingData();
}
