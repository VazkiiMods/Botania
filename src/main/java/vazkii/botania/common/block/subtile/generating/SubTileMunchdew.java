/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2014, 7:25:47 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubTileMunchdew extends SubTileGenerating {

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_ATE_ONCE = "ateOnce";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;

	boolean ateOnce = false;
	int ticksWithoutEating = -1;
	int cooldown = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(getWorld().isRemote)
			return;

		if(cooldown > 0) {
			cooldown--;
			ticksWithoutEating = 0;
			ateOnce = false; // don't start ticking ticksWithoutEating again until we eat again
			return;
		}

		int manaPerLeaf = 160;
		eatLeaves : {
			if(getMaxMana() - mana >= manaPerLeaf && ticksExisted % 4 == 0) {
				List<BlockPos> coords = new ArrayList<>();
				BlockPos pos = supertile.getPos();

				for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-RANGE, 0, -RANGE), pos.add(RANGE, RANGE_Y, RANGE))) {
					if(supertile.getWorld().getBlockState(pos_).getMaterial() == Material.LEAVES) {
						boolean exposed = false;
						for(EnumFacing dir : EnumFacing.VALUES) {
							IBlockState offState = supertile.getWorld().getBlockState(pos_.offset(dir));
							if(offState.getBlock().isAir(offState, supertile.getWorld(), pos_.offset(dir))) {
								exposed = true;
								break;
							}
						}

						if(exposed)
							coords.add(pos_);
					}
				}

				if(coords.isEmpty())
					break eatLeaves;

				Collections.shuffle(coords);
				BlockPos breakCoords = coords.get(0);
				IBlockState state = supertile.getWorld().getBlockState(breakCoords);
				supertile.getWorld().setBlockToAir(breakCoords);
				ticksWithoutEating = 0;
				ateOnce = true;
				if(ConfigHandler.blockBreakParticles)
					supertile.getWorld().playEvent(2001, breakCoords, Block.getStateId(state));
				mana += manaPerLeaf;
			}
		}

		if(ateOnce) {
			ticksWithoutEating++;
			if(ticksWithoutEating >= 5) {
				cooldown = 1600;
				sync();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_COOLDOWN, cooldown);
		cmp.setBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		cooldown = cmp.getInteger(TAG_COOLDOWN);
		ateOnce = cmp.getBoolean(TAG_ATE_ONCE);
	}

	@Override
	public List<ItemStack> getDrops(List<ItemStack> list) {
		List<ItemStack> drops = super.getDrops(list);
		if(cooldown > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, cooldown);
		return drops;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	@Override
	public int getColor() {
		return 0x79C42F;
	}

	@Override
	public int getMaxMana() {
		return 10000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.munchdew;
	}
}
