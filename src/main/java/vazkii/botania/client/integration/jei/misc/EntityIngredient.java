/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.misc;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityIngredient implements IIngredientType<Entity> {

	public static final IIngredientHelper<Entity> HELPER = new Helper();
	public static final IIngredientType<Entity> TYPE = new EntityIngredient();
	public static final IIngredientRenderer<Entity> RENDERER = new Renderer();

	@Override
	public Class<Entity> getIngredientClass() {
		return Entity.class;
	}

	public static class Helper implements IIngredientHelper<Entity> {

		@Nullable
		@Override
		public Entity getMatch(Iterable<Entity> ingredients, Entity ingredientToMatch) {
			for (Entity ingredient : ingredients) {
				if (ingredient.equals(ingredientToMatch)) {
					return ingredient;
				}
			}
			return null;
		}

		@Override
		public String getDisplayName(Entity ingredient) {
			return ingredient.getDisplayName().getString();
		}

		@Override
		public String getUniqueId(Entity ingredient) {
			return getWildcardId(ingredient);
		}

		@Override
		public String getWildcardId(Entity ingredient) {
			return Registry.ENTITY_TYPE.getKey(ingredient.getType()).toString();
		}

		@Override
		public String getModId(Entity ingredient) {
			return Registry.ENTITY_TYPE.getKey(ingredient.getType()).getNamespace();
		}

		@Override
		public String getResourceId(Entity ingredient) {
			return LibMisc.MOD_ID + ":entity";
		}

		@Override
		public Entity copyIngredient(Entity ingredient) {
			Entity copied = ingredient.getType()
					.create(ingredient.world);
			if (copied == null) {
				return ingredient;
			}
			CompoundNBT cmp = new CompoundNBT();
			ingredient.writeWithoutTypeId(cmp);
			copied.read(cmp);
			return copied;
		}

		@Override
		public String getErrorInfo(@Nullable Entity ingredient) {
			if (ingredient == null) {
				return "null";
			}
			return getDisplayName(ingredient);
		}

		@Override
		public ItemStack getCheatItemStack(Entity ingredient) {
			return ingredient.getPickedResult(new EntityRayTraceResult(ingredient));
		}

		@Override
		public Collection<ResourceLocation> getTags(Entity ingredient) {
			return ingredient.getType().getTags();
		}
	}

	public static class Renderer implements IIngredientRenderer<Entity> {

		@Override
		public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable Entity ingredient) {
			ClientWorld world = Minecraft.getInstance().world;
			if (ingredient == null || world == null) {
				return;
			}

			ingredient.setWorld(world);

			matrixStack.push();
			matrixStack.translate(xPosition + 3, yPosition + 13, 100);
			matrixStack.scale(10F, -10F, 10F); // 16 * 0.625, according to block.json

			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.rotate(Vector3f.XP.rotationDegrees(30));
			matrixStack.rotate(Vector3f.YP.rotationDegrees(45));
			matrixStack.rotate(Vector3f.YP.rotation(ClientTickHandler.total / 100));
			matrixStack.translate(0, -0.5, 0);

			doScaling(matrixStack, ingredient);

			IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			Minecraft.getInstance()
					.getRenderManager()
					.getRenderer(ingredient)
					.render(ingredient, 0, 0, matrixStack, buffers, 0xF00000);
			buffers.finish();

			matrixStack.pop();
		}

		protected void doScaling(MatrixStack matrixStack, Entity ingredient) {
			AxisAlignedBB size = ingredient.getRenderBoundingBox();
			double max = Math.max(size.getXSize(),
					Math.max(size.getYSize(),
							size.getZSize()));
			if (max > 1) {
				float scale = (float) (1 / max);
				matrixStack.scale(scale, scale, scale);
			}
		}

		@Override
		public List<ITextComponent> getTooltip(Entity ingredient, ITooltipFlag tooltipFlag) {
			List<ITextComponent> tooltip = new ArrayList<>();
			tooltip.add(ingredient.getDisplayName());
			if (tooltipFlag.isAdvanced()) {
				tooltip.add((new StringTextComponent(HELPER.getWildcardId(ingredient))).func_240699_a_(TextFormatting.DARK_GRAY));
			}
			return tooltip;
		}

	}

}
