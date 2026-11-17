/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.block;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.capability.BlockApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Any block with this component can have phantom ink used on it.
 */
public interface PhantomInkableBlock {
	ResourceLocation ID = botaniaRL("phantom_inkable");
	BlockApiNoContext<PhantomInkableBlock> LOOKUP = new BlockApiNoContext<>(ID, PhantomInkableBlock.class);

	/**
	 * Called when the block is clicked with phantom ink.
	 *
	 * @param player Null if the block is being inked by a dispenser
	 */
	boolean onPhantomInked(@Nullable Player player, ItemStack stack, Direction side);
}
