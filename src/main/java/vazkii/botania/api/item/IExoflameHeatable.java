/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import vazkii.botania.api.BotaniaAPI;

/**
 * A Block Entity that has this component can be heated by an Exoflame flower.
 * NOTE: Do not attach this component to subclasses of AbstractFurnaceBlockEntity, as Botania already does so.
 */
public interface IExoflameHeatable extends ComponentV3 {
	/**
	 * The component ID used by Botania
	 */
	Identifier ID = new Identifier(BotaniaAPI.MODID, "exoflame_heatable");

	/**
	 * Can this TileEntity smelt its contents. If true, the Exoflame is allowed
	 * to fuel it.
	 */
	boolean canSmelt();

	/**
	 * Gets the amount of ticks left for the fuel. If below 2, the exoflame
	 * will call boostBurnTime.
	 */
	int getBurnTime();

	/**
	 * Called to increase the amount of time this furnace should be burning
	 * the fuel for. Even if it doesn't have any fuel.
	 */
	void boostBurnTime();

	/**
	 * Called once every two ticks to increase the speed of the furnace. Feel
	 * free to not do anything if all you want is to allow the exoflame to feed
	 * it, not make it faster.
	 */
	void boostCookTime();

	@Override
	default void readFromNbt(CompoundTag tag) {}

	@Override
	default void writeToNbt(CompoundTag tag) {}
}
