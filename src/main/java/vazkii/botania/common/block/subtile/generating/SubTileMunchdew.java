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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.api.subtile.SubTileType;
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

	private boolean ateOnce = false;
	private int ticksWithoutEating = -1;
	private int cooldown = 0;

	public SubTileMunchdew(SubTileType type) {
		super(type);
	}

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

				nextCoord:
				for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-RANGE, 0, -RANGE), pos.add(RANGE, RANGE_Y, RANGE))) {
					if(supertile.getWorld().getBlockState(pos_).getBlock().isIn(BlockTags.LEAVES)) {
						for(EnumFacing dir : EnumFacing.values()) {
							if(supertile.getWorld().isAirBlock(pos_.offset(dir))) {
								coords.add(pos_);
								break nextCoord;
							}
						}
					}
				}

				if(coords.isEmpty())
					break eatLeaves;

				Collections.shuffle(coords);
				BlockPos breakCoords = coords.get(0);
				IBlockState state = supertile.getWorld().getBlockState(breakCoords);
				supertile.getWorld().removeBlock(breakCoords);
				ticksWithoutEating = 0;
				ateOnce = true;
				if(ConfigHandler.COMMON.blockBreakParticles.get())
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

		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		cooldown = cmp.getInt(TAG_COOLDOWN);
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
