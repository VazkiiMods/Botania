/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 9, 2015, 12:48:18 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockGhostRail extends AbstractRailBlock {

	private static final String TAG_FLOAT_TICKS = "botania:float_ticks";

	public BlockGhostRail(Properties builder) {
		super(true, builder);
		setDefaultState(stateContainer.getBaseState().with(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
		MinecraftForge.EVENT_BUS.addListener(this::cartSpawn);
		MinecraftForge.EVENT_BUS.addListener(this::worldTick);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
	}

	private static void updateFloating(AbstractMinecartEntity cart, int floatTicks) {
		BlockPos entPos = new BlockPos(cart);
		BlockState state = cart.world.getBlockState(entPos);
		boolean air = state.isAir(cart.world, entPos);
		if(floatTicks > 0) {
			BlockState stateBelow = cart.world.getBlockState(entPos.down());
			Block blockBelow = stateBelow.getBlock();
			boolean airBelow = blockBelow.isAir(stateBelow, cart.world, entPos.down());
			if(air && airBelow || !air && !airBelow)
				cart.noClip = true;
			cart.setMotion(cart.getMotion().getX() * 1.4, 0.2, cart.getMotion().getZ() * 1.4);
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, floatTicks - 1);
			cart.world.playEvent(2000, entPos, 0);
		} else cart.noClip = false; // todo 1.14 doesn't seem to work for some reason?
	}

	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		super.onMinecartPass(state, world, pos, cart);
		if(!world.isRemote) {
			int floatTicks = 20;
			cart.getPersistentData().putInt(TAG_FLOAT_TICKS, floatTicks);
			updateFloating(cart, floatTicks);
		}
	}

	private final Map<DimensionType, Set<AbstractMinecartEntity>> carts = new HashMap<>();

	private void cartSpawn(EntityJoinWorldEvent evt) {
		if(!evt.getWorld().isRemote && evt.getEntity() instanceof AbstractMinecartEntity) {
			carts.computeIfAbsent(evt.getWorld().getDimension().getType(), t -> Collections.newSetFromMap(new WeakHashMap<>()))
				.add((AbstractMinecartEntity) evt.getEntity());
		}
	}

	private void worldTick(TickEvent.WorldTickEvent evt) {
		if(!evt.world.isRemote() && evt.phase == TickEvent.Phase.END) {
			for (AbstractMinecartEntity c : carts.getOrDefault(evt.world.getDimension().getType(), Collections.emptySet())) {
				BlockPos entPos = new BlockPos(c);
				BlockState state = c.world.getBlockState(entPos);
				Block block = state.getBlock();
				int floatTicks = c.getPersistentData().getInt(TAG_FLOAT_TICKS);

				if(block != ModBlocks.ghostRail
						&& (block instanceof AbstractRailBlock || block == ModBlocks.dreamwood)) {
					c.getPersistentData().putInt(TAG_FLOAT_TICKS, 0);
					if(floatTicks > 0)
						c.world.playEvent(2003, entPos, 0);
				}

				updateFloating(c, c.getPersistentData().getInt(TAG_FLOAT_TICKS));
			}
		}
	}

	@Nonnull
	@Override
	public IProperty<RailShape> getShapeProperty() {
		return BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	}
}
