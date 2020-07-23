/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.integration.jei.misc.ParticleDrawable;
import vazkii.botania.common.block.ModSubtiles;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Consumer;

import static vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis.*;

public class GourmaryllisCategory extends SimpleGenerationCategory implements Consumer<ParticleDrawable> {

	public GourmaryllisCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.gourmaryllis, ModSubtiles.gourmaryllisFloating);
		particle = new ParticleDrawable()
				.onTick(this);
	}

	@Override
	public void draw(SimpleManaGenRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		currentFood = recipe.getStacks().get(0);
		super.draw(recipe, matrixStack, mouseX, mouseY);
	}

	private ItemStack currentFood = ItemStack.EMPTY;

	@Override
	public void accept(ParticleDrawable drawable) {
		if (ClientTickHandler.ticksInGame % 3 == 0) {
			ItemParticleData data = new ItemParticleData(ParticleTypes.ITEM, currentFood);
			for (int i = 0; i < 10; i++) {
				Random rand = new Random();
				Particle particle = drawable.addParticle(data,
						0.4 + 0.1 * rand.nextGaussian(),
						0.6 + 0.1 * rand.nextGaussian(),
						1.0 + 0.1 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian(),
						0.03 * rand.nextGaussian());
				if (particle != null) {
					particle.setMaxAge(5);
				}
			}
		}
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		List<SimpleManaGenRecipe> recipes = new ArrayList<>();
		for (Item item : Registry.ITEM) {
			if (!item.isFood()) {
				continue;
			}

			int[] mana = new int[STREAK_MULTIPLIERS.length];
			for (int i = 0; i < STREAK_MULTIPLIERS.length; i++) {
				mana[i] = getDigestingMana(getEffectiveFoodValue(item), STREAK_MULTIPLIERS[i]);
			}
			recipes.add(new SimpleManaGenRecipe(Collections.singletonList(new ItemStack(item)), mana));
		}
		return recipes;
	}
}
