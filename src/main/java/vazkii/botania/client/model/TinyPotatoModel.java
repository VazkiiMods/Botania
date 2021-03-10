/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.client.render.tile.RenderTileTinyPotato;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TinyPotatoModel extends DelegatedModel {
	public TinyPotatoModel(IBakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList() {
			@Override
			public IBakedModel getOverrideModel(@Nonnull IBakedModel model, @Nonnull ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
				if (stack.hasDisplayName()) {
					return RenderTileTinyPotato.getModelFromDisplayName(stack.getDisplayName());
				}
				return model;
			}
		};
	}
}
