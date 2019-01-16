/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 30, 2015, 10:18:50 PM (GMT)]
 */
package vazkii.botania.common.block.decor.panes;

import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class BlockBifrostPane extends BlockModPane {

	public BlockBifrostPane() {
		super(ModBlocks.bifrostPerm);
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, "bifrost_pane");
	}

}
