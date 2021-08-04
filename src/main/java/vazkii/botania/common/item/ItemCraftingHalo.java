/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.network.PacketBotaniaEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCraftingHalo extends Item {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_GREEN);
	private static final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";

	public ItemCraftingHalo(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide) {
			int segment = getSegmentLookedAt(stack, player);
			Recipe<?> recipe = getSavedRecipe(world, stack, segment);

			if (segment == 0) {
				// Pos is never used by workbench, so use origin.
				// But this cannot be the static dummy one in the interface, we have to pass an actual one for the
				// crafting matrix to update properly.
				ContainerLevelAccess wp = ContainerLevelAccess.create(world, BlockPos.ZERO);
				player.openMenu(new SimpleMenuProvider(
						(windowId, playerInv, p) -> new ContainerCraftingHalo(windowId, playerInv, wp),
						stack.getHoverName()));
			} else {
				if (recipe == null) {
					Recipe<?> lastRecipe = getLastRecipe(world, stack);
					if (lastRecipe != null) {
						saveRecipe(stack, lastRecipe.getId(), segment);
					}
				} else {
					tryCraft(player, stack, segment, true);
				}
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int pos, boolean equipped) {
		if (!(entity instanceof LivingEntity)) {
			return;
		}
		LivingEntity living = (LivingEntity) entity;

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getOffhandItem() == stack) {
			equipped = true;
		}

		if (eqLastTick != equipped) {
			setEquipped(stack, equipped);
		}

		if (!equipped) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2.0F;
			setRotationBase(stack, getCheckingAngle((LivingEntity) entity) - shift);
		}
	}

	private static boolean hasRoomFor(Inventory inv, ItemStack stack) {
		Inventory dummy = new Inventory(inv.player);
		for (int i = 0; i < inv.items.size(); i++) {
			dummy.items.set(i, inv.items.get(i).copy());
		}
		// warning: must be careful to not cause side effects / dupes with dummy here
		return dummy.add(stack.copy());
	}

	private static boolean canCraftHeuristic(Player player, Recipe<CraftingContainer> recipe) {
		StackedContents accounter = new StackedContents();
		player.getInventory().fillStackedContents(accounter);
		return accounter.canCraft(recipe, null);
	}

	void tryCraft(Player player, ItemStack halo, int slot, boolean particles) {
		Recipe<CraftingContainer> recipe = getSavedRecipe(player.level, halo, slot);
		if (recipe == null) {
			return;
		}

		CraftingMenu dummy = new CraftingMenu(-999, player.getInventory());
		CraftingContainer craftInv = (CraftingContainer) dummy.getSlot(1).container;
		RecipePlacer placer = new RecipePlacer(dummy);

		// Try placing the recipe into the dummy workbench, extracting items from player's inventory to do so
		if (!placer.place((ServerPlayer) player, recipe)) {
			return;
		}

		// Double check that the recipe matches
		if (!recipe.matches(craftInv, player.level)) {
			// If the placer worked but the recipe still didn't, this might be a dynamic recipe with special conditions.
			// Return items to the inventory and bail.
			placer.clearGrid();
			return;
		}

		ItemStack result = recipe.assemble(craftInv);

		// Check if we have room for the result
		if (!hasRoomFor(player.getInventory(), result)) {
			placer.clearGrid();
			return;
		}

		// Now we are good to go. Give the result
		player.getInventory().add(result);

		// Give or toss all byproducts
		NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(craftInv);
		remainingItems.forEach(s -> player.getInventory().placeItemBackInInventory(player.level, s));

		// The items we consumed will stay in the dummy workbench and get deleted

		if (particles) {
			PacketBotaniaEffect.sendNearby(player, PacketBotaniaEffect.EffectType.HALO_CRAFT,
					player.getX(), player.getY(), player.getZ(), player.getId());
		}
	}

	public static boolean onEntitySwing(ItemStack stack, LivingEntity player) {
		int segment = getSegmentLookedAt(stack, player);
		if (segment == 0) {
			return false;
		}

		Recipe<?> recipe = getSavedRecipe(player.level, stack, segment);
		if (recipe != null && player.isShiftKeyDown()) {
			saveRecipe(stack, null, segment);
			return true;
		}

		return false;
	}

	private static int getSegmentLookedAt(ItemStack stack, LivingEntity player) {
		float yaw = getCheckingAngle(player, getRotationBase(stack));

		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for (int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if (yaw >= calcAngle && yaw < calcAngle + segAngles) {
				return seg;
			}
		}
		return -1;
	}

	private static float getCheckingAngle(LivingEntity player) {
		return getCheckingAngle(player, 0F);
	}

	// Screw the way minecraft handles rotation
	// Really...
	private static float getCheckingAngle(LivingEntity player, float base) {
		float yaw = Mth.wrapDegrees(player.yRot) + 90F;
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = segAngles / 2;

		if (yaw < 0) {
			yaw = 180F + (180F + yaw);
		}
		yaw -= 360F - base;
		float angle = 360F - yaw + shift;

		if (angle < 0) {
			angle = 360F + angle;
		}

		return angle;
	}

	@Nullable
	private static Recipe<CraftingContainer> getSavedRecipe(Level world, ItemStack halo, int position) {
		String savedId = ItemNBTHelper.getString(halo, TAG_STORED_RECIPE_PREFIX + position, "");
		ResourceLocation id = savedId.isEmpty() ? null : ResourceLocation.tryParse(savedId);

		if (position <= 0 || position >= SEGMENTS || id == null) {
			return null;
		} else {
			return ModRecipeTypes.getRecipes(world, RecipeType.CRAFTING).get(id);
		}
	}

	private static void saveRecipe(ItemStack halo, @Nullable ResourceLocation id, int position) {
		if (id == null) {
			ItemNBTHelper.removeEntry(halo, TAG_STORED_RECIPE_PREFIX + position);
		} else {
			ItemNBTHelper.setString(halo, TAG_STORED_RECIPE_PREFIX + position, id.toString());
		}
	}

	private static ItemStack getDisplayItem(Level world, ItemStack stack, int position) {
		if (position == 0) {
			return craftingTable;
		} else if (position >= SEGMENTS) {
			return ItemStack.EMPTY;
		} else {
			Recipe<?> recipe = getSavedRecipe(world, stack, position);
			if (recipe != null) {
				return recipe.getResultItem();
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	public static void onItemCrafted(Player player, CraftingContainer inv) {
		AbstractContainerMenu container = player.containerMenu;

		if (!(container instanceof ContainerCraftingHalo)) {
			return;
		}

		player.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inv, player.level).ifPresent(recipe -> {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (!stack.isEmpty() && stack.getItem() instanceof ItemCraftingHalo) {
					rememberLastRecipe(recipe.getId(), stack);
				}
			}
		});
	}

	private static void rememberLastRecipe(ResourceLocation recipeId, ItemStack halo) {
		ItemNBTHelper.setString(halo, TAG_LAST_CRAFTING, recipeId.toString());
	}

	@Nullable
	private static Recipe<CraftingContainer> getLastRecipe(Level world, ItemStack halo) {
		String savedId = ItemNBTHelper.getString(halo, TAG_LAST_CRAFTING, "");
		ResourceLocation id = savedId.isEmpty() ? null : ResourceLocation.tryParse(savedId);

		return ModRecipeTypes.getRecipes(world, RecipeType.CRAFTING).get(id);
	}

	private static boolean wasEquipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false);
	}

	private static void setEquipped(ItemStack stack, boolean equipped) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped);
	}

	private static float getRotationBase(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0F);
	}

	private static void setRotationBase(ItemStack stack, float rotation) {
		ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation);
	}

	@Environment(EnvType.CLIENT)
	public static void onRenderWorldLast(float partialTicks, PoseStack ms) {
		Player player = Minecraft.getInstance().player;
		ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemCraftingHalo.class);
		if (!stack.isEmpty()) {
			render(stack, player, ms, partialTicks);
		}
	}

	@Environment(EnvType.CLIENT)
	private static void render(ItemStack stack, Player player, PoseStack ms, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

		double renderPosX = mc.getEntityRenderDispatcher().camera.getPosition().x();
		double renderPosY = mc.getEntityRenderDispatcher().camera.getPosition().y();
		double renderPosZ = mc.getEntityRenderDispatcher().camera.getPosition().z();

		ms.pushPose();
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.xo + (player.getX() - player.xo) * partialTicks;
		double posY = player.yo + (player.getY() - player.yo) * partialTicks + player.getEyeHeight();
		double posZ = player.zo + (player.getZ() - player.zo) * partialTicks;

		ms.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);

		float base = getRotationBase(stack);
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = base - segAngles / 2.0F;

		float u = 1F;
		float v = 0.25F;

		float s = 3F;
		float m = 0.8F;
		float y = v * s * 2;
		float y0 = 0;

		int segmentLookedAt = getSegmentLookedAt(stack, player);
		ItemCraftingHalo item = (ItemCraftingHalo) stack.getItem();
		RenderType layer = RenderHelper.getHaloLayer(item.getGlowResource());

		for (int seg = 0; seg < SEGMENTS; seg++) {
			boolean inside = false;
			float rotationAngle = (seg + 0.5F) * segAngles + shift;
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(rotationAngle));
			ms.translate(s * m, -0.75F, 0F);

			if (segmentLookedAt == seg) {
				inside = true;
			}

			ItemStack slotStack = getDisplayItem(player.level, stack, seg);
			if (!slotStack.isEmpty()) {
				float scale = seg == 0 ? 0.9F : 0.8F;
				ms.scale(scale, scale, scale);
				ms.mulPose(Vector3f.YP.rotationDegrees(180F));
				ms.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

				ms.mulPose(Vector3f.YP.rotationDegrees(90.0F));
				Minecraft.getInstance().getItemRenderer().renderStatic(slotStack, ItemTransforms.TransformType.GUI, 0xF000F0, OverlayTexture.NO_OVERLAY, ms, buffers);
			}
			ms.popPose();

			ms.pushPose();
			ms.mulPose(Vector3f.XP.rotationDegrees(180));
			float r = 1, g = 1, b = 1, a = alpha;
			if (inside) {
				a += 0.3F;
				y0 = -y;
			}

			if (seg % 2 == 0) {
				r = g = b = 0.6F;
			}

			VertexConsumer buffer = buffers.getBuffer(layer);
			for (int i = 0; i < segAngles; i++) {
				Matrix4f mat = ms.last().pose();
				float ang = i + seg * segAngles + shift;
				float xp = (float) Math.cos(ang * Math.PI / 180F) * s;
				float zp = (float) Math.sin(ang * Math.PI / 180F) * s;

				buffer.vertex(mat, xp * m, y, zp * m).color(r, g, b, a).uv(u, v).endVertex();
				buffer.vertex(mat, xp, y0, zp).color(r, g, b, a).uv(u, 0).endVertex();

				xp = (float) Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = (float) Math.sin((ang + 1) * Math.PI / 180F) * s;

				buffer.vertex(mat, xp, y0, zp).color(r, g, b, a).uv(0, 0).endVertex();
				buffer.vertex(mat, xp * m, y, zp * m).color(r, g, b, a).uv(0, v).endVertex();
			}
			y0 = 0;
			ms.popPose();
		}
		ms.popPose();
		buffers.endBatch();
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(PoseStack ms, Player player, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		int slot = getSegmentLookedAt(stack, player);

		if (slot == 0) {
			String name = craftingTable.getHoverName().getString();
			int l = mc.font.width(name);
			int x = mc.getWindow().getGuiScaledWidth() / 2 - l / 2;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 65;

			GuiComponent.fill(ms, x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			GuiComponent.fill(ms, x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			mc.getItemRenderer().renderAndDecorateItem(craftingTable, mc.getWindow().getGuiScaledWidth() / 2 - 8, mc.getWindow().getGuiScaledHeight() / 2 - 52);

			mc.font.drawShadow(ms, name, x, y, 0xFFFFFF);
		} else {
			Recipe<CraftingContainer> recipe = getSavedRecipe(player.level, stack, slot);
			Component label;
			boolean setRecipe = false;

			if (recipe == null) {
				label = new TranslatableComponent("botaniamisc.unsetRecipe");
				recipe = getLastRecipe(player.level, stack);
			} else {
				label = recipe.getResultItem().getHoverName();
				setRecipe = true;
			}

			renderRecipe(ms, label, recipe, player, setRecipe);
		}
	}

	@Environment(EnvType.CLIENT)
	private static void renderRecipe(PoseStack ms, Component label, @Nullable Recipe<CraftingContainer> recipe, Player player, boolean isSavedRecipe) {
		Minecraft mc = Minecraft.getInstance();

		if (recipe != null && !recipe.getResultItem().isEmpty()) {
			int x = mc.getWindow().getGuiScaledWidth() / 2 - 45;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 90;

			GuiComponent.fill(ms, x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			GuiComponent.fill(ms, x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			GuiComponent.fill(ms, x + 66, y + 14, x + 92, y + 40, 0x22000000);
			GuiComponent.fill(ms, x - 2, y - 2, x + 56, y + 56, 0x22000000);

			int wrap = recipe instanceof ShapedRecipe ? ((ShapedRecipe) recipe).getWidth() : 3;
			for (int i = 0; i < recipe.getIngredients().size(); i++) {
				Ingredient ingr = recipe.getIngredients().get(i);
				if (ingr != Ingredient.EMPTY) {
					ItemStack stack = ingr.getItems()[ClientTickHandler.ticksInGame / 20 % ingr.getItems().length];
					int xpos = x + i % wrap * 18;
					int ypos = y + i / wrap * 18;
					GuiComponent.fill(ms, xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getItemRenderer().renderAndDecorateItem(stack, xpos, ypos);
				}
			}

			mc.getItemRenderer().renderAndDecorateItem(recipe.getResultItem(), x + 72, y + 18);
			mc.getItemRenderer().renderGuiItemDecorations(mc.font, recipe.getResultItem(), x + 72, y + 18);

		}

		int yoff = 110;
		if (isSavedRecipe && recipe != null && !canCraftHeuristic(player, recipe)) {
			String warning = ChatFormatting.RED + I18n.get("botaniamisc.cantCraft");
			mc.font.drawShadow(ms, warning, mc.getWindow().getGuiScaledWidth() / 2.0F - mc.font.width(warning) / 2.0F, mc.getWindow().getGuiScaledHeight() / 2.0F - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.font.drawShadow(ms, label, mc.getWindow().getGuiScaledWidth() / 2.0F - mc.font.width(label.getString()) / 2.0F, mc.getWindow().getGuiScaledHeight() / 2.0F - yoff, 0xFFFFFF);
	}

	public static class RecipePlacer extends ServerPlaceRecipe<CraftingContainer> {
		public RecipePlacer(RecipeBookMenu<CraftingContainer> container) {
			super(container);
		}

		// [VanillaCopy] Based on super.place
		public boolean place(ServerPlayer player, @Nullable Recipe<CraftingContainer> recipe) {
			if (recipe != null) {
				this.inventory = player.getInventory();
				this.stackedContents.clear();
				player.getInventory().fillStackedContents(this.stackedContents);
				this.menu.fillCraftSlotsStackedContents(this.stackedContents);

				boolean ret;
				if (this.stackedContents.canCraft(recipe, null)) {
					this.handleRecipeClicked(recipe, false);
					ret = true;
				} else {
					this.clearGrid();
					ret = false;
				}

				player.getInventory().setChanged();
				return ret;
			}
			return false;
		}

		@Override
		public void clearGrid() {
			super.clearGrid();
		}
	}
}
