/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2014, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockPool extends BlockMod implements IWandHUD, IWandable, ILexiconable {
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);

	public enum Variant {
		DEFAULT,
		CREATIVE,
		DILUTED,
		FABULOUS
	}

	public final Variant variant;

	public BlockPool(Variant v) {
		super(Material.ROCK, LibBlockNames.POOL_PREFIX + v.name().toLowerCase(Locale.ROOT));
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
		this.variant = v;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Nonnull
	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if (te instanceof TilePool) {
			return state.withProperty(BotaniaStateProps.COLOR, ((TilePool) te).color);
		} else {
			return state.withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE);
		}
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	// If harvesting, delay setting block to air so getDrops can read the TE
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TilePool && !((TilePool) te).fragile) {
			super.getDrops(drops, world, pos, state, fortune);
		}
	}

	// After getDrops reads the TE, then delete the block
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TilePool();
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TilePool tile = (TilePool) world.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}

	private static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0, 0, 0, 1, 1/16.0, 1);
	private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0, 0, 15/16.0, 1, 0.5, 1);
	private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1/16.0);
	private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0, 0, 0, 1/16.0, 0.5, 1);
	private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(15/16.0, 0, 0, 1, 0.5, 1);


	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> boxes, Entity entity, boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, boxes, BOTTOM_AABB);
		addCollisionBoxToList(pos, entityBox, boxes, NORTH_AABB);
		addCollisionBoxToList(pos, entityBox, boxes, SOUTH_AABB);
		addCollisionBoxToList(pos, entityBox, boxes, WEST_AABB);
		addCollisionBoxToList(pos, entityBox, boxes, EAST_AABB);
	}

	@Override
	public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		if (variant == Variant.FABULOUS)
			return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
		else return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TilePool pool = (TilePool) world.getTileEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TilePool) world.getTileEntity(pos)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TilePool) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return variant == Variant.FABULOUS ? LexiconData.rainbowRod : LexiconData.pool;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).build());
		super.registerModels();
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
