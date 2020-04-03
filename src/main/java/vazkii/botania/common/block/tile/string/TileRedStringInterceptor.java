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
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileRedStringInterceptor extends TileRedString {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.RED_STRING_INTERCEPTOR) public static TileEntityType<TileRedStringInterceptor> TYPE;
	private static final Set<TileRedStringInterceptor> interceptors = new HashSet<>();

	public TileRedStringInterceptor() {
		super(TYPE);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isRemote) {
			interceptors.add(this);
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getTileEntity(pos) != null;
	}

	private boolean saneState() {
		return !isRemoved() && world.getTileEntity(pos) == this;
	}

	public static void onInteract(PlayerEntity player, World world, BlockPos pos, Hand hand) {
		if (world.isRemote) {
			return;
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
					Block block = inter.getBlockState().getBlock();
					world.setBlockState(inter.getPos(), world.getBlockState(inter.getPos()).with(BlockStateProperties.POWERED, true));
					world.getPendingBlockTicks().scheduleTick(inter.getPos(), block, block.tickRate(world));
					did = true;
				}
			}
		}

		interceptors.removeAll(remove);
		if (did) {
			player.swingArm(hand);
			world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

}
