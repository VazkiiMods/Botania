/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 30, 2015, 10:01:17 PM (GMT)]
 */
package vazkii.botania.common.block.decor.panes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class BlockModPane extends BlockPane implements IModelRegister {

	public BlockModPane(Block source) {
		super(Material.GLASS, false);
		// Backward compat don't kill me
		String name = source.getTranslationKey().replaceAll("tile.", "") + "Pane";
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
		setTranslationKey(name);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F);
		useNeighborBrightness = true;
	}

	/*@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess iblockaccess, BlockPos pos, EnumFacing side) {
		return false;
	}*/

	@Override
	public boolean canPaneConnectTo(IBlockAccess world, BlockPos pos, @Nonnull EnumFacing dir) {
		Block block = world.getBlockState(pos).getBlock();
		return block == ModBlocks.elfGlass || block == ModBlocks.manaGlass || block == ModBlocks.bifrostPerm || super.canPaneConnectTo(world, pos, dir);
	}

	@Override
	public void registerModels() {}
}
