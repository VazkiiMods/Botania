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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileManaVoid;

import javax.annotation.Nonnull;

public class BlockManaVoid extends BlockMod implements IPoolOverlayProvider, EntityBlock {

	public BlockManaVoid(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileManaVoid();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public TextureAtlasSprite getIcon(Level world, BlockPos pos) {
		return MiscellaneousIcons.INSTANCE.manaVoidOverlay.sprite();
	}

}
