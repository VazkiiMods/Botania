/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2014, 10:16:53 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFloatingFlower.IslandType;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.IFloatingFlowerVariant;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFloatingFlower extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(1.6, 1.6, 1.6, 14.4, 14.4, 14.4);
	public final EnumDyeColor color;

	public BlockFloatingFlower(EnumDyeColor color, Properties props) {
		super(props);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return ConfigHandler.CLIENT.staticFloaters.get() ? EnumBlockRenderType.MODEL : EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/* todo 1.13
	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.ISLAND_TYPE });
	}

	@Nonnull
	@Override
	public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockReader world, BlockPos pos) {
		state = getActualState(state, world, pos);
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if (te instanceof TileFloatingFlower) {
			state = ((IExtendedBlockState) state).with(BotaniaStateProps.ISLAND_TYPE, ((TileFloatingFlower) te).getIslandType());
		} else if (te instanceof TileFloatingSpecialFlower) {
			state = ((IExtendedBlockState) state).with(BotaniaStateProps.ISLAND_TYPE, ((TileFloatingSpecialFlower) te).getIslandType());
		}
		return state;
	}
	*/

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		int hex = state.get(BotaniaStateProps.COLOR).colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if(rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.get())
			Botania.proxy.sparkleFX(pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, rand.nextFloat(), 5);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty()) {
			IFloatingFlower flower = (IFloatingFlower) world.getTileEntity(pos);
			IslandType type = null;
			if(stack.getItem() == Items.SNOWBALL)
				type = IslandType.SNOW;
			else if(stack.getItem() instanceof IFloatingFlowerVariant) {
				IslandType newType = ((IFloatingFlowerVariant) stack.getItem()).getIslandType(stack);
				if(newType != null)
					type = newType;
			}

			if(type != null && type != flower.getIslandType()) {
				if(!world.isRemote) {
					flower.setIslandType(type);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}

				if(!player.abilities.isCreativeMode)
					stack.shrink(1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileFloatingFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.shinyFlowers;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}
}
