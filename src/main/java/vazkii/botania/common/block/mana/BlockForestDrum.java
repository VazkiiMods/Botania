/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 16, 2014, 7:34:37 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemHorn;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BlockForestDrum extends BlockMod implements IManaTrigger, ILexiconable {

	public enum Variant {
		WILD,
		GATHERING,
		CANOPY
	}

	private static final VoxelShape SHAPE = Block.makeCuboidShape(3, 1, 3, 13, 15, 13);
	private final Variant variant;

	public BlockForestDrum(Variant v, Builder builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if(burst.isFake())
			return;
		if(world.isRemote) {
			world.spawnParticle(Particles.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
			return;
		}
		if(variant == Variant.WILD)
			ItemHorn.breakGrass(world, new ItemStack(ModItems.grassHorn), pos);
		else if(variant == Variant.CANOPY)
			ItemHorn.breakGrass(world, new ItemStack(ModItems.leavesHorn), pos);
		else {
			int range = 10;
			List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
			List<EntityLiving> shearables = new ArrayList<>();
			ItemStack stack = new ItemStack(ModBlocks.gatheringDrum);

			for(EntityLiving entity : entities) {
				if(entity instanceof IShearable && ((IShearable) entity).isShearable(stack, world, new BlockPos(entity))) {
					shearables.add(entity);
				} else if(entity instanceof EntityCow) {
					List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.posX + entity.width, entity.posY + entity.height, entity.posZ + entity.width));
					for(EntityItem item : items) {
						ItemStack itemstack = item.getItem();
						if(!itemstack.isEmpty() && itemstack.getItem() == Items.BUCKET && !world.isRemote) {
							while(itemstack.getCount() > 0) {
								EntityItem ent = entity.entityDropItem(new ItemStack(Items.MILK_BUCKET), 1.0F);
								ent.motionY += world.rand.nextFloat() * 0.05F;
								ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
								ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
								itemstack.shrink(1);
							}
							item.remove();
						}
					}
				}
			}

			Collections.shuffle(shearables);
			int sheared = 0;

			for(EntityLiving entity : shearables) {
				if(sheared > 4)
					break;

				List<ItemStack> stacks = ((IShearable) entity).onSheared(stack, world, new BlockPos(entity), 0);
				if(stacks != null)
					for(ItemStack wool : stacks) {
						EntityItem ent = entity.entityDropItem(wool, 1.0F);
						ent.motionY += world.rand.nextFloat() * 0.05F;
						ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
						ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
					}
				++sheared;
			}
		}

		for(int i = 0; i < 10; i++)
			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 1F, 1F);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		switch(variant) {
		case GATHERING:
			return LexiconData.gatherDrum;
		case CANOPY:
			return LexiconData.canopyDrum;
		case WILD:
		default:
			return LexiconData.forestDrum;
		}
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
