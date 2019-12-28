/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 11, 2015, 5:05:05 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;
import java.util.List;

public class SubTileRafflowsia extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":rafflowsia")
	public static TileEntityType<SubTileRafflowsia> TYPE;

	public static final String TAG_LAST_FLOWER = "lastFlower";
	public static final String TAG_LAST_FLOWER_TIMES = "lastFlowerTimes";

	@Nullable
	private Block lastFlower;
	private int lastFlowerTimes;

	private static final int RANGE = 5;

	public SubTileRafflowsia() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		int mana = 2100;

		if(getMaxMana() - this.getMana() >= mana && !getWorld().isRemote && ticksExisted % 40 == 0) {
			for(int i = 0; i < RANGE * 2 + 1; i++)
				for(int j = 0; j < RANGE * 2 + 1; j++)
					for(int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = getEffectivePos().add(i - RANGE, j - RANGE, k - RANGE);

						BlockState state = getWorld().getBlockState(pos);
						if(state.isIn(ModTags.Blocks.SPECIAL_FLOWERS) && state.getBlock() != ModSubtiles.rafflowsia) {
							if(state.getBlock() == lastFlower)
								lastFlowerTimes++;
							else {
								lastFlower = state.getBlock();
								lastFlowerTimes = 1;
							}

							float mod = 1F / lastFlowerTimes;

							getWorld().destroyBlock(pos, false);
							addMana((int) (mana * mod));
							sync();
							return;
						}
					}
		}
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);

		if(lastFlower != null)
			cmp.putString(TAG_LAST_FLOWER, lastFlower.getRegistryName().toString());
		cmp.putInt(TAG_LAST_FLOWER_TIMES, lastFlowerTimes);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);

		ResourceLocation id = ResourceLocation.tryCreate(cmp.getString(TAG_LAST_FLOWER));
		if(id != null)
			lastFlower = ForgeRegistries.BLOCKS.getValue(id);
		lastFlowerTimes = cmp.getInt(TAG_LAST_FLOWER_TIMES);
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x502C76;
	}

	@Override
	public int getMaxMana() {
		return 9000;
	}

}
