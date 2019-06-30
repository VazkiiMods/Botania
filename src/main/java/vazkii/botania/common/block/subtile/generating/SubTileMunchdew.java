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
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubTileMunchdew extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":munchdew")
	public static TileEntityType<SubTileMunchdew> TYPE;

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_ATE_ONCE = "ateOnce";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;

	private boolean ateOnce = false;
	private int ticksWithoutEating = -1;
	private int cooldown = 0;

	public SubTileMunchdew() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

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
				BlockPos pos = getPos();

				nextCoord:
				for(BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-RANGE, 0, -RANGE),
						pos.add(RANGE, RANGE_Y, RANGE))) {
					if(getWorld().getBlockState(pos_).getBlock().isIn(BlockTags.LEAVES)) {
						for(Direction dir : Direction.values()) {
							if(getWorld().isAirBlock(pos_.offset(dir))) {
								coords.add(pos_.toImmutable());
								break nextCoord;
							}
						}
					}
				}

				if(coords.isEmpty())
					break eatLeaves;

				Collections.shuffle(coords);
				BlockPos breakCoords = coords.get(0);
				BlockState state = getWorld().getBlockState(breakCoords);
				getWorld().removeBlock(breakCoords, false);
				ticksWithoutEating = 0;
				ateOnce = true;
				if(ConfigHandler.COMMON.blockBreakParticles.get())
					getWorld().playEvent(2001, breakCoords, Block.getStateId(state));
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
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);

		cooldown = cmp.getInt(TAG_COOLDOWN);
		ateOnce = cmp.getBoolean(TAG_ATE_ONCE);
	}

	@Override
	public List<ItemStack> getDrops(List<ItemStack> list, LootContext.Builder ctx) {
		List<ItemStack> drops = super.getDrops(list, ctx);
		if(cooldown > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, cooldown);
		return drops;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
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
