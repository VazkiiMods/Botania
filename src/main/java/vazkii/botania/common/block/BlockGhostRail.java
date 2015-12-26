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
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGhostRail extends BlockRailBase implements ILexiconable {

	private static final String TAG_FLOAT_TICKS = "Botania_FloatTicks";

	public BlockGhostRail() {
		super(true);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		MinecraftForge.EVENT_BUS.register(this);
		setUnlocalizedName(LibBlockNames.GHOST_RAIL);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.RAIL_DIRECTION, EnumRailDirection.NORTH_SOUTH));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.RAIL_DIRECTION);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.RAIL_DIRECTION).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BotaniaStateProps.RAIL_DIRECTION, EnumRailDirection.byMetadata(meta));
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@SubscribeEvent
	public void onMinecartUpdate(MinecartUpdateEvent event) {
		int x = MathHelper.floor_double(event.entity.posX);
		int y = MathHelper.floor_double(event.entity.posY);
		int z = MathHelper.floor_double(event.entity.posZ);
		BlockPos entPos = new BlockPos(event.entity);
		Block block = event.entity.worldObj.getBlockState(entPos).getBlock();
		boolean air = block.isAir(event.entity.worldObj, entPos);
		int floatTicks = event.entity.getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(block == this)
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, 20);
		else if(block instanceof BlockRailBase || block == ModBlocks.dreamwood) {
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, 0);
			if(floatTicks > 0)
				event.entity.worldObj.playAuxSFX(2003, entPos, 0);
		}
		floatTicks = event.entity.getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(floatTicks > 0) {
			Block blockBelow = event.entity.worldObj.getBlockState(entPos.down()).getBlock();
			boolean airBelow = blockBelow.isAir(event.entity.worldObj, entPos.down());
			if(air && airBelow || !air && !airBelow)
				event.entity.noClip = true;
			event.entity.motionY = 0.2;
			event.entity.motionX *= 1.4;
			event.entity.motionZ *= 1.4;
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, floatTicks - 1);
			event.entity.worldObj.playAuxSFX(2000, entPos, 0);
		} else event.entity.noClip = false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.ghostRail;
	}

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {
		return BlockRailPowered.SHAPE;
	}
}
