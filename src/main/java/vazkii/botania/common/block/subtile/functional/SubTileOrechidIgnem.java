/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lib.LibMisc;

import java.util.Map;
import java.util.function.Predicate;

public class SubTileOrechidIgnem extends SubTileOrechid {
	@ObjectHolder(LibMisc.MOD_ID + ":orechid_ignem") public static TileEntityType<SubTileOrechidIgnem> TYPE;

	private static final int COST = 20000;

	public SubTileOrechidIgnem() {
		super(TYPE);
	}

	@Override
	public boolean canOperate() {
		return getWorld().getDimension().isNether();
	}

	@Override
	public Map<ResourceLocation, Integer> getOreMap() {
		return BotaniaAPI.oreWeightsNether;
	}

	@Override
	public Predicate<BlockState> getReplaceMatcher() {
		return state -> state.getBlock() == Blocks.NETHERRACK;
	}

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

}
