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
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityIngredient implements IIngredientType<Entity> {

	public static EntityIngredient TYPE = new EntityIngredient();

	@Override
	public Class<Entity> getIngredientClass() {
		return Entity.class;
	}

	public static class Helper implements IIngredientHelper<Entity> {

		public static final Helper INSTANCE = new Helper();

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
			CompoundNBT cmp = new CompoundNBT();
			ingredient.writeWithoutTypeId(cmp);
			Objects.requireNonNull(copied).read(cmp);
			return copied;
		}

		@Override
		public String getErrorInfo(@Nullable Entity ingredient) {
			if(ingredient == null) {
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

		public static Renderer INSTANCE = new Renderer();

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
			matrixStack.translate(0, -0.5, 0);
			matrixStack.rotate(Vector3f.YP.rotation(ClientTickHandler.total / 100));

			IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			Minecraft.getInstance()
					.getRenderManager()
					.getRenderer(ingredient)
					.render(ingredient, 0, 0, matrixStack, buffers, 0xF00000);
			buffers.finish();

			matrixStack.pop();
		}

		@Override
		public List<ITextComponent> getTooltip(Entity ingredient, ITooltipFlag tooltipFlag) {
			List<ITextComponent> tooltip = new ArrayList<>();
			tooltip.add(ingredient.getDisplayName());
			if (tooltipFlag.isAdvanced()) {
				tooltip.add((new StringTextComponent(Helper.INSTANCE.getDisplayName(ingredient))).func_240699_a_(TextFormatting.DARK_GRAY));
			}
			return tooltip;
		}

	}

}
