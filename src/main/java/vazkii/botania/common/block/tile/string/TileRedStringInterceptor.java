/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 21, 2015, 4:58:20 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileRedStringInterceptor extends TileRedString {

	private static final ThreadLocal<Set<TileRedStringInterceptor>> interceptors = ThreadLocal.withInitial(HashSet::new);;

	@Override
	public void update() {
		super.update();
		interceptors.get().add(this);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return worldObj.getTileEntity(pos) != null;
	}

	private boolean saneState() {
		return !isInvalid() && worldObj.getTileEntity(pos) == this;
	}

	public static void onInteract(EntityPlayer player, World world, BlockPos pos, EnumHand hand) {
		List<TileRedStringInterceptor> remove = new ArrayList<>();
		boolean did = false;


		for(TileRedStringInterceptor inter : interceptors.get()) {
			if(!inter.saneState()) {
				remove.add(inter);
				continue;
			}

			if(inter.worldObj == world) {
				BlockPos coords = inter.getBinding();
				if(coords != null && coords.equals(pos)) {
					if(!world.isRemote) {
						Block block = inter.getBlockType();
						world.setBlockState(inter.getPos(), world.getBlockState(inter.getPos()).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
						world.scheduleUpdate(inter.getPos(), block, block.tickRate(world));
					}

					did = true;
				}
			}
		}

		interceptors.get().removeAll(remove);
		if(did) {
			if(world.isRemote)
				player.swingArm(hand);
			else world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

}
