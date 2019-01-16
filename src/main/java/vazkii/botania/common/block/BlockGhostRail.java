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

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockGhostRail extends BlockRailBase implements ILexiconable, IModelRegister {

	private static final String TAG_FLOAT_TICKS = "Botania_FloatTicks";

	public BlockGhostRail() {
		super(true);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.RAIL_DIRECTION, EnumRailDirection.NORTH_SOUTH));
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.GHOST_RAIL));
		setTranslationKey(LibBlockNames.GHOST_RAIL);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.RAIL_DIRECTION);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.RAIL_DIRECTION).getMetadata();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.RAIL_DIRECTION, EnumRailDirection.byMetadata(meta));
	}

	@SubscribeEvent
	public static void onMinecartUpdate(MinecartUpdateEvent event) {
		BlockPos entPos = new BlockPos(event.getEntity());
		IBlockState state = event.getEntity().world.getBlockState(entPos);
		Block block = state.getBlock();
		boolean air = block.isAir(state, event.getEntity().world, entPos);
		int floatTicks = event.getEntity().getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(block == ModBlocks.ghostRail)
			event.getEntity().getEntityData().setInteger(TAG_FLOAT_TICKS, 20);
		else if(block instanceof BlockRailBase || block == ModBlocks.dreamwood) {
			event.getEntity().getEntityData().setInteger(TAG_FLOAT_TICKS, 0);
			if(floatTicks > 0)
				event.getEntity().world.playEvent(2003, entPos, 0);
		}
		floatTicks = event.getEntity().getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(floatTicks > 0) {
			IBlockState stateBelow = event.getEntity().world.getBlockState(entPos.down());
			Block blockBelow = stateBelow.getBlock();
			boolean airBelow = blockBelow.isAir(stateBelow, event.getEntity().world, entPos.down());
			if(air && airBelow || !air && !airBelow)
				event.getEntity().noClip = true;
			event.getEntity().motionY = 0.2;
			event.getEntity().motionX *= 1.4;
			event.getEntity().motionZ *= 1.4;
			event.getEntity().getEntityData().setInteger(TAG_FLOAT_TICKS, floatTicks - 1);
			event.getEntity().world.playEvent(2000, entPos, 0);
		} else event.getEntity().noClip = false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.ghostRail;
	}

	@Nonnull
	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {
		return BlockRailPowered.SHAPE;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, "ghost_rail");
	}
}
