/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.client.render.tile.RenderTileTinyPotato;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TinyPotatoModel extends DelegatedModel {
	public TinyPotatoModel(BakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public ModelOverrideList getOverrides() {
		return new ModelOverrideList() {
			@Override
			public BakedModel apply(@Nonnull BakedModel model, @Nonnull ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
				return RenderTileTinyPotato.getModelFromDisplayName(stack.getName());
			}
		};
	}
}
