/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.ModTiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileRedStringInterceptor extends TileRedString {
	private static final Set<TileRedStringInterceptor> interceptors = new HashSet<>();

	public TileRedStringInterceptor() {
		super(ModTiles.RED_STRING_INTERCEPTOR);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			interceptors.add(this);
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockEntity(pos) != null;
	}

	private boolean saneState() {
		return !isRemoved() && world.getBlockEntity(pos) == this;
	}

	public static ActionResult onInteract(PlayerEntity player, World world, BlockPos pos, Hand hand) {
		if (world.isClient) {
			return ActionResult.PASS;
		}

		List<TileRedStringInterceptor> remove = new ArrayList<>();
		boolean did = false;

		for (TileRedStringInterceptor inter : interceptors) {
			if (!inter.saneState()) {
				remove.add(inter);
				continue;
			}

			if (inter.world == world) {
				BlockPos coords = inter.getBinding();
				if (coords != null && coords.equals(pos)) {
					Block block = inter.getCachedState().getBlock();
					world.setBlockState(inter.getPos(), world.getBlockState(inter.getPos()).with(Properties.POWERED, true));
					world.getBlockTickScheduler().schedule(inter.getPos(), block, 2);
					did = true;
				}
			}
		}

		interceptors.removeAll(remove);
		if (did) {
			player.swingHand(hand);
			world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

}
