/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.render.tile.RenderTileTinyPotato;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TinyPotatoModel extends DelegatedModel {
	public TinyPotatoModel(BakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public ItemOverrides getOverrides() {
		return new ItemOverrides() {
			@Override
			public BakedModel resolve(@Nonnull BakedModel model, @Nonnull ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity livingEntity) {
				if (stack.hasCustomHoverName()) {
					return RenderTileTinyPotato.getModelFromDisplayName(stack.getHoverName());
				}
				return model;
			}
		};
	}
}
