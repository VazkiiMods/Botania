/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 17, 2015, 4:08:58 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockLightLauncher extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);

	public BlockLightLauncher() {
		super(Material.WOOD, LibBlockNames.LIGHT_LAUNCHER);
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.POWERED, false));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.POWERED) ? 8 : 0;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.POWERED, meta == 8);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0|| world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.getValue(BotaniaStateProps.POWERED);

		if(power && !powered) {
			pickUpEntities(world, pos);
			world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, true), 4);
		} else if(!power && powered)
			world.setBlockState(pos, state.withProperty(BotaniaStateProps.POWERED, false), 4);
	}

	private void pickUpEntities(World world, BlockPos pos) {
		List<TileLightRelay> relays = new ArrayList<>();
		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			TileEntity tile = world.getTileEntity(pos.offset(dir));
			if(tile instanceof TileLightRelay) {
				TileLightRelay relay = (TileLightRelay) tile;
				if(relay.getNextDestination() != null)
					relays.add(relay);
			}
		}

		if(!relays.isEmpty()) {
			AxisAlignedBB aabb = new AxisAlignedBB(pos, pos.add(1, 1, 1));
			List<Entity> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			entities.addAll(world.getEntitiesWithinAABB(EntityItem.class, aabb));

			if(!entities.isEmpty()) {
				for(Entity entity : entities) {
					TileLightRelay relay = relays.get(world.rand.nextInt(relays.size()));
					relay.mountEntity(entity);
				}
			}
		}
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.luminizerTransport;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

}
