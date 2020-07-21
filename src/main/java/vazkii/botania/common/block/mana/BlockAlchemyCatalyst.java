/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.BlockMod;

public class BlockAlchemyCatalyst extends BlockMod implements IPoolOverlayProvider {

	public BlockAlchemyCatalyst(Settings builder) {
		super(builder);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Sprite getIcon(World world, BlockPos pos) {
		return MiscellaneousIcons.INSTANCE.alchemyCatalystOverlay;
	}

}
