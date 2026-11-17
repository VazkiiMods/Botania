/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.entity.ManaSparkEntity;

public class ManaSparkRenderer extends BaseSparkRenderer<ManaSparkEntity> {

	public ManaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Nullable
	@Override
	public TextureAtlasSprite getSpinningIcon(ManaSparkEntity entity) {
		ItemStack upgrade = entity.getUpgrade();
		if (upgrade.isEmpty()) {
			return null;
		}

		ResourceLocation icon = upgrade.get(BotaniaDataComponents.AUGMENT_ICON);
		if (icon == null) {
			return null;
		}

		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(icon);
	}

}
