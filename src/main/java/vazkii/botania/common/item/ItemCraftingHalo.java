/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCraftingHalo extends Item {

	private static final Identifier glowTexture = new Identifier(LibResources.MISC_GLOW_GREEN);
	private static final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";

	public ItemCraftingHalo(Settings props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onItemCrafted);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.addListener(this::onRenderWorldLast);
		});
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient) {
			int segment = getSegmentLookedAt(stack, player);
			Recipe<?> recipe = getSavedRecipe(world, stack, segment);

			if (segment == 0) {
				// Pos is never used by workbench, so use origin.
				// But this cannot be the static dummy one in the interface, we have to pass an actual one for the
				// crafting matrix to update properly.
				ScreenHandlerContext wp = ScreenHandlerContext.create(world, BlockPos.ORIGIN);
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
						(windowId, playerInv, p) -> new ContainerCraftingHalo(windowId, playerInv, wp),
						stack.getName()));
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

		return TypedActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		if (!(entity instanceof LivingEntity)) {
			return;
		}
		LivingEntity living = (LivingEntity) entity;

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getOffHandStack() == stack) {
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

	private static boolean hasRoomFor(PlayerInventory inv, ItemStack stack) {
		PlayerInventory dummy = new PlayerInventory(inv.player);
		for (int i = 0; i < inv.main.size(); i++) {
			dummy.main.set(i, inv.main.get(i).copy());
		}
		// warning: must be careful to not cause side effects / dupes with dummy here
		return dummy.insertStack(stack.copy());
	}

	private static boolean canCraftHeuristic(PlayerEntity player, Recipe<CraftingInventory> recipe) {
		RecipeFinder accounter = new RecipeFinder();
		player.inventory.populateRecipeFinder(accounter);
		return accounter.findRecipe(recipe, null);
	}

	void tryCraft(PlayerEntity player, ItemStack halo, int slot, boolean particles) {
		Recipe<CraftingInventory> recipe = getSavedRecipe(player.world, halo, slot);
		if (recipe == null) {
			return;
		}

		CraftingScreenHandler dummy = new CraftingScreenHandler(-999, player.inventory);
		CraftingInventory craftInv = (CraftingInventory) dummy.getSlot(1).inventory;
		RecipePlacer placer = new RecipePlacer(dummy);

		// Try placing the recipe into the dummy workbench, extracting items from player's inventory to do so
		if (!placer.place((ServerPlayerEntity) player, recipe)) {
			return;
		}

		// Double check that the recipe matches
		if (!recipe.matches(craftInv, player.world)) {
			// If the placer worked but the recipe still didn't, this might be a dynamic recipe with special conditions.
			// Return items to the inventory and bail.
			placer.returnInputs();
			return;
		}

		ItemStack result = recipe.craft(craftInv);

		// Check if we have room for the result
		if (!hasRoomFor(player.inventory, result)) {
			placer.returnInputs();
			return;
		}

		// Now we are good to go. Give the result
		player.inventory.insertStack(result);

		// Give or toss all byproducts
		DefaultedList<ItemStack> remainingItems = recipe.getRemainingStacks(craftInv);
		remainingItems.forEach(s -> player.inventory.offerOrDrop(player.world, s));

		// The items we consumed will stay in the dummy workbench and get deleted

		if (particles) {
			PacketBotaniaEffect pkt = new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.HALO_CRAFT,
					player.getX(), player.getY(), player.getZ(), player.getEntityId());
			PacketHandler.sendToNearby(player.world, player, pkt);
		}
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity player) {
		int segment = getSegmentLookedAt(stack, player);
		if (segment == 0) {
			return false;
		}

		Recipe<?> recipe = getSavedRecipe(player.world, stack, segment);
		if (recipe != null && player.isSneaking()) {
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
		float yaw = MathHelper.wrapDegrees(player.yaw) + 90F;
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
	private static Recipe<CraftingInventory> getSavedRecipe(World world, ItemStack halo, int position) {
		String savedId = ItemNBTHelper.getString(halo, TAG_STORED_RECIPE_PREFIX + position, "");
		Identifier id = savedId.isEmpty() ? null : Identifier.tryParse(savedId);

		if (position <= 0 || position >= SEGMENTS || id == null) {
			return null;
		} else {
			return ModRecipeTypes.getRecipes(world, RecipeType.CRAFTING).get(id);
		}
	}

	private static void saveRecipe(ItemStack halo, @Nullable Identifier id, int position) {
		if (id == null) {
			ItemNBTHelper.removeEntry(halo, TAG_STORED_RECIPE_PREFIX + position);
		} else {
			ItemNBTHelper.setString(halo, TAG_STORED_RECIPE_PREFIX + position, id.toString());
		}
	}

	private static ItemStack getDisplayItem(World world, ItemStack stack, int position) {
		if (position == 0) {
			return craftingTable;
		} else if (position >= SEGMENTS) {
			return ItemStack.EMPTY;
		} else {
			Recipe<?> recipe = getSavedRecipe(world, stack, position);
			if (recipe != null) {
				return recipe.getOutput();
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	private void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		PlayerEntity player = event.getPlayer();
		ScreenHandler container = player.currentScreenHandler;
		Inventory inv = event.getInventory();

		if (!(container instanceof ContainerCraftingHalo) || !(inv instanceof CraftingInventory)) {
			return;
		}

		event.getPlayer().world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, (CraftingInventory) inv, player.world).ifPresent(recipe -> {
			for (int i = 0; i < player.inventory.size(); i++) {
				ItemStack stack = player.inventory.getStack(i);
				if (!stack.isEmpty() && stack.getItem() instanceof ItemCraftingHalo) {
					rememberLastRecipe(recipe.getId(), stack);
				}
			}
		});
	}

	private static void rememberLastRecipe(Identifier recipeId, ItemStack halo) {
		ItemNBTHelper.setString(halo, TAG_LAST_CRAFTING, recipeId.toString());
	}

	@Nullable
	private static Recipe<CraftingInventory> getLastRecipe(World world, ItemStack halo) {
		String savedId = ItemNBTHelper.getString(halo, TAG_LAST_CRAFTING, "");
		Identifier id = savedId.isEmpty() ? null : Identifier.tryParse(savedId);

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
	private void onRenderWorldLast(RenderWorldLastEvent event) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemCraftingHalo.class);
		if (!stack.isEmpty()) {
			render(stack, player, event.getMatrixStack(), event.getPartialTicks());
		}
	}

	@Environment(EnvType.CLIENT)
	public void render(ItemStack stack, PlayerEntity player, MatrixStack ms, float partialTicks) {
		MinecraftClient mc = MinecraftClient.getInstance();
		VertexConsumerProvider.Immediate buffers = mc.getBufferBuilders().getEntityVertexConsumers();

		double renderPosX = mc.getEntityRenderManager().camera.getPos().getX();
		double renderPosY = mc.getEntityRenderManager().camera.getPos().getY();
		double renderPosZ = mc.getEntityRenderManager().camera.getPos().getZ();

		ms.push();
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevX + (player.getX() - player.prevX) * partialTicks;
		double posY = player.prevY + (player.getY() - player.prevY) * partialTicks + player.getStandingEyeHeight();
		double posZ = player.prevZ + (player.getZ() - player.prevZ) * partialTicks;

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
		RenderLayer layer = RenderHelper.getHaloLayer(item.getGlowResource());

		for (int seg = 0; seg < SEGMENTS; seg++) {
			boolean inside = false;
			float rotationAngle = (seg + 0.5F) * segAngles + shift;
			ms.push();
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotationAngle));
			ms.translate(s * m, -0.75F, 0F);

			if (segmentLookedAt == seg) {
				inside = true;
			}

			ItemStack slotStack = getDisplayItem(player.world, stack, seg);
			if (!slotStack.isEmpty()) {
				float scale = seg == 0 ? 0.9F : 0.8F;
				ms.scale(scale, scale, scale);
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
				ms.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
				MinecraftClient.getInstance().getItemRenderer().renderItem(slotStack, ModelTransformation.Mode.GUI, 0xF000F0, OverlayTexture.DEFAULT_UV, ms, buffers);
			}
			ms.pop();

			ms.push();
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
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
				Matrix4f mat = ms.peek().getModel();
				float ang = i + seg * segAngles + shift;
				float xp = (float) Math.cos(ang * Math.PI / 180F) * s;
				float zp = (float) Math.sin(ang * Math.PI / 180F) * s;

				buffer.vertex(mat, xp * m, y, zp * m).color(r, g, b, a).texture(u, v).next();
				buffer.vertex(mat, xp, y0, zp).color(r, g, b, a).texture(u, 0).next();

				xp = (float) Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = (float) Math.sin((ang + 1) * Math.PI / 180F) * s;

				buffer.vertex(mat, xp, y0, zp).color(r, g, b, a).texture(0, 0).next();
				buffer.vertex(mat, xp * m, y, zp * m).color(r, g, b, a).texture(0, v).next();
			}
			y0 = 0;
			ms.pop();
		}
		ms.pop();
		buffers.draw();
	}

	@Environment(EnvType.CLIENT)
	public Identifier getGlowResource() {
		return glowTexture;
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player, ItemStack stack) {
		MinecraftClient mc = MinecraftClient.getInstance();
		int slot = getSegmentLookedAt(stack, player);

		if (slot == 0) {
			String name = craftingTable.getName().getString();
			int l = mc.textRenderer.getWidth(name);
			int x = mc.getWindow().getScaledWidth() / 2 - l / 2;
			int y = mc.getWindow().getScaledHeight() / 2 - 65;

			DrawableHelper.fill(ms, x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			DrawableHelper.fill(ms, x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			mc.getItemRenderer().renderInGuiWithOverrides(craftingTable, mc.getWindow().getScaledWidth() / 2 - 8, mc.getWindow().getScaledHeight() / 2 - 52);

			mc.textRenderer.drawWithShadow(ms, name, x, y, 0xFFFFFF);
		} else {
			Recipe<CraftingInventory> recipe = getSavedRecipe(player.world, stack, slot);
			Text label;
			boolean setRecipe = false;

			if (recipe == null) {
				label = new TranslatableText("botaniamisc.unsetRecipe");
				recipe = getLastRecipe(player.world, stack);
			} else {
				label = recipe.getOutput().getName();
				setRecipe = true;
			}

			renderRecipe(ms, label, recipe, player, setRecipe);
		}
	}

	@Environment(EnvType.CLIENT)
	private static void renderRecipe(MatrixStack ms, Text label, @Nullable Recipe<CraftingInventory> recipe, PlayerEntity player, boolean isSavedRecipe) {
		MinecraftClient mc = MinecraftClient.getInstance();

		if (recipe != null && !recipe.getOutput().isEmpty()) {
			int x = mc.getWindow().getScaledWidth() / 2 - 45;
			int y = mc.getWindow().getScaledHeight() / 2 - 90;

			DrawableHelper.fill(ms, x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			DrawableHelper.fill(ms, x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			DrawableHelper.fill(ms, x + 66, y + 14, x + 92, y + 40, 0x22000000);
			DrawableHelper.fill(ms, x - 2, y - 2, x + 56, y + 56, 0x22000000);

			int wrap = recipe instanceof IShapedRecipe<?> ? ((IShapedRecipe<?>) recipe).getRecipeWidth() : 3;
			for (int i = 0; i < recipe.getPreviewInputs().size(); i++) {
				Ingredient ingr = recipe.getPreviewInputs().get(i);
				if (ingr != Ingredient.EMPTY) {
					ItemStack stack = ingr.getMatchingStacksClient()[ClientTickHandler.ticksInGame / 20 % ingr.getMatchingStacksClient().length];
					int xpos = x + i % wrap * 18;
					int ypos = y + i / wrap * 18;
					DrawableHelper.fill(ms, xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getItemRenderer().renderInGuiWithOverrides(stack, xpos, ypos);
				}
			}

			mc.getItemRenderer().renderInGuiWithOverrides(recipe.getOutput(), x + 72, y + 18);
			mc.getItemRenderer().renderGuiItemOverlay(mc.textRenderer, recipe.getOutput(), x + 72, y + 18);

		}

		int yoff = 110;
		if (isSavedRecipe && recipe != null && !canCraftHeuristic(player, recipe)) {
			String warning = Formatting.RED + I18n.translate("botaniamisc.cantCraft");
			mc.textRenderer.drawWithShadow(ms, warning, mc.getWindow().getScaledWidth() / 2.0F - mc.textRenderer.getWidth(warning) / 2.0F, mc.getWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.textRenderer.drawWithShadow(ms, label, mc.getWindow().getScaledWidth() / 2.0F - mc.textRenderer.getWidth(label.getString()) / 2.0F, mc.getWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
	}

	public static class RecipePlacer extends InputSlotFiller<CraftingInventory> {
		public RecipePlacer(AbstractRecipeScreenHandler<CraftingInventory> container) {
			super(container);
		}

		// [VanillaCopy] Based on super.place
		public boolean place(ServerPlayerEntity player, @Nullable Recipe<CraftingInventory> recipe) {
			if (recipe != null) {
				this.inventory = player.inventory;
				this.recipeFinder.clear();
				player.inventory.populateRecipeFinder(this.recipeFinder);
				this.craftingScreenHandler.populateRecipeFinder(this.recipeFinder);

				boolean ret;
				if (this.recipeFinder.findRecipe(recipe, null)) {
					this.fillInputSlots(recipe, false);
					ret = true;
				} else {
					this.returnInputs();
					ret = false;
				}

				player.inventory.markDirty();
				return ret;
			}
			return false;
		}

		@Override
		public void returnInputs() {
			super.returnInputs();
		}
	}
}
