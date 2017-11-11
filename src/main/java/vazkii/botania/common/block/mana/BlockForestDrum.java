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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.DrumVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.item.ItemGrassHorn;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockForestDrum extends BlockMod implements IManaTrigger, ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(3/16.0, 1/16.0, 3/16.0, 13/16.0, 15/16.0, 13/16.0);

	public BlockForestDrum() {
		super(Material.WOOD, LibBlockNames.FOREST_DRUM);
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.DRUM_VARIANT, DrumVariant.WILD));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.DRUM_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.DRUM_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= DrumVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.DRUM_VARIANT, DrumVariant.values()[meta]);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int i = 0; i < 3; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if(burst.isFake())
			return;
		if(world.isRemote) {
			world.spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
			return;
		}
		DrumVariant variant = world.getBlockState(pos).getValue(BotaniaStateProps.DRUM_VARIANT);
		if(variant == DrumVariant.WILD)
			ItemGrassHorn.breakGrass(world, null, 0, pos);
		else if(variant == DrumVariant.CANOPY)
			ItemGrassHorn.breakGrass(world, null, 1, pos);
		else {
			int range = 10;
			List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
			List<EntityLiving> shearables = new ArrayList<>();
			ItemStack stack = new ItemStack(this, 1, 1);

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
							item.setDead();
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
			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundCategory.BLOCKS, 1F, 1F);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		DrumVariant variant = world.getBlockState(pos).getValue(BotaniaStateProps.DRUM_VARIANT);

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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, DrumVariant.values().length);
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
