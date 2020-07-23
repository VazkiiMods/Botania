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
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.integration.jei.misc.ArbIngredientRenderer;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.AccessorSlimeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NarslimmusCategory extends SimpleGenerationCategory {

	public NarslimmusCategory(IGuiHelper guiHelper) {
		super(guiHelper, ModSubtiles.narslimmus, ModSubtiles.narslimmusFloating);
	}

	@Override
	protected void setRecipeInputs(IRecipeLayout recipeLayout, SimpleManaGenRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, ((NarslimmusRecipe) recipe).renderer, 76, 4, 18, 18, 1, 1);
		stacks.set(ingredients);
	}

	@Override
	protected Collection<SimpleManaGenRecipe> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers) {
		return IntStream.range(1, 4)
				.mapToObj(NarslimmusRecipe::new)
				.collect(Collectors.toList());
	}

	@Override
	public Class<? extends SimpleManaGenRecipe> getRecipeClass() {
		return NarslimmusRecipe.class;
	}

	private static class NarslimmusRecipe extends SimpleManaGenRecipe {

		public final SlimeRenderer renderer;

		protected NarslimmusRecipe(int slimeSize) {
			super(Collections.singletonList(new ItemStack(Items.SLIME_SPAWN_EGG)),
					(int) Math.pow(2, slimeSize) * 1200);
			renderer = new SlimeRenderer(slimeSize);
		}
	}

	private static class SlimeRenderer extends ArbIngredientRenderer<ItemStack> {

		private final SlimeEntity slime;
		private final EntityRenderer<? super SlimeEntity> renderer;

		public SlimeRenderer(int slimeSize) {
			Minecraft mc = Minecraft.getInstance();
			slime = new SlimeEntity(EntityType.SLIME, Objects.requireNonNull(mc.world));
			((AccessorSlimeEntity) slime).callSetSlimeSize(slimeSize, false);
			renderer = mc.getRenderManager()
					.getRenderer(slime);
		}

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable ItemStack ingredient) {
			matrixStack.push();
			setupDefaultBlockTransforms(matrixStack, xPosition, yPosition);
			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.rotate(Vector3f.YP.rotation(ClientTickHandler.total / 300));

			IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			renderer.render(slime, 0, 0, matrixStack, buffers, 0xF00000);
			buffers.finish();

			matrixStack.pop();
		}

		@Override
		public List<ITextComponent> getTooltip(ItemStack ingredient, ITooltipFlag tooltipFlag) {
			List<ITextComponent> tooltip = new ArrayList<>();
			tooltip.add(slime.getDisplayName());
			if(tooltipFlag.isAdvanced()) {
				tooltip.add((new StringTextComponent(slime.getType().getRegistryName().toString())).func_240699_a_(TextFormatting.DARK_GRAY));
			}
			return tooltip;
		}
	}
}
