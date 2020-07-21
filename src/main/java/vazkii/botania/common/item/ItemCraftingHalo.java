/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_GREEN);
	private static final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";

	public ItemCraftingHalo(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onItemCrafted);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.addListener(this::onRenderWorldLast);
		});
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			int segment = getSegmentLookedAt(stack, player);
			IRecipe<?> recipe = getSavedRecipe(world, stack, segment);

			if (segment == 0) {
				// Pos is never used by workbench, so use origin.
				// But this cannot be the static dummy one in the interface, we have to pass an actual one for the
				// crafting matrix to update properly.
				IWorldPosCallable wp = IWorldPosCallable.of(world, BlockPos.ZERO);
				player.openContainer(new SimpleNamedContainerProvider(
						(windowId, playerInv, p) -> new ContainerCraftingHalo(windowId, playerInv, wp),
						stack.getDisplayName()));
			} else {
				if (recipe == null) {
					IRecipe<?> lastRecipe = getLastRecipe(world, stack);
					if (lastRecipe != null) {
						saveRecipe(stack, lastRecipe.getId(), segment);
					}
				} else {
					tryCraft(player, stack, segment, true);
				}
			}
		}

		return ActionResult.resultSuccess(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		if (!(entity instanceof LivingEntity)) {
			return;
		}
		LivingEntity living = (LivingEntity) entity;

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getHeldItemOffhand() == stack) {
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
		for (int i = 0; i < inv.mainInventory.size(); i++) {
			dummy.mainInventory.set(i, inv.mainInventory.get(i).copy());
		}
		// warning: must be careful to not cause side effects / dupes with dummy here
		return dummy.addItemStackToInventory(stack.copy());
	}

	private static boolean canCraftHeuristic(PlayerEntity player, IRecipe<CraftingInventory> recipe) {
		RecipeItemHelper accounter = new RecipeItemHelper();
		player.inventory.accountStacks(accounter);
		return accounter.canCraft(recipe, null);
	}

	void tryCraft(PlayerEntity player, ItemStack halo, int slot, boolean particles) {
		IRecipe<CraftingInventory> recipe = getSavedRecipe(player.world, halo, slot);
		if (recipe == null) {
			return;
		}

		WorkbenchContainer dummy = new WorkbenchContainer(-999, player.inventory);
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
			placer.clear();
			return;
		}

		ItemStack result = recipe.getCraftingResult(craftInv);

		// Check if we have room for the result
		if (!hasRoomFor(player.inventory, result)) {
			placer.clear();
			return;
		}

		// Now we are good to go. Give the result
		player.inventory.addItemStackToInventory(result);

		// Give or toss all byproducts
		NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(craftInv);
		remainingItems.forEach(s -> player.inventory.placeItemBackInInventory(player.world, s));

		// The items we consumed will stay in the dummy workbench and get deleted

		if (particles) {
			PacketBotaniaEffect pkt = new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.HALO_CRAFT,
					player.getPosX(), player.getPosY(), player.getPosZ(), player.getEntityId());
			PacketHandler.sendToNearby(player.world, player, pkt);
		}
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity player) {
		int segment = getSegmentLookedAt(stack, player);
		if (segment == 0) {
			return false;
		}

		IRecipe<?> recipe = getSavedRecipe(player.world, stack, segment);
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
		float yaw = MathHelper.wrapDegrees(player.rotationYaw) + 90F;
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
	private static IRecipe<CraftingInventory> getSavedRecipe(World world, ItemStack halo, int position) {
		String savedId = ItemNBTHelper.getString(halo, TAG_STORED_RECIPE_PREFIX + position, "");
		ResourceLocation id = savedId.isEmpty() ? null : ResourceLocation.tryCreate(savedId);

		if (position <= 0 || position >= SEGMENTS || id == null) {
			return null;
		} else {
			return ModRecipeTypes.getRecipes(world, IRecipeType.CRAFTING).get(id);
		}
	}

	private static void saveRecipe(ItemStack halo, @Nullable ResourceLocation id, int position) {
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
			IRecipe<?> recipe = getSavedRecipe(world, stack, position);
			if (recipe != null) {
				return recipe.getRecipeOutput();
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	private void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		PlayerEntity player = event.getPlayer();
		Container container = player.openContainer;
		IInventory inv = event.getInventory();

		if (!(container instanceof ContainerCraftingHalo) || !(inv instanceof CraftingInventory)) {
			return;
		}

		event.getPlayer().world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, (CraftingInventory) inv, player.world).ifPresent(recipe -> {
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
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
	private static IRecipe<CraftingInventory> getLastRecipe(World world, ItemStack halo) {
		String savedId = ItemNBTHelper.getString(halo, TAG_LAST_CRAFTING, "");
		ResourceLocation id = savedId.isEmpty() ? null : ResourceLocation.tryCreate(savedId);

		return ModRecipeTypes.getRecipes(world, IRecipeType.CRAFTING).get(id);
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

	@OnlyIn(Dist.CLIENT)
	private void onRenderWorldLast(RenderWorldLastEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemCraftingHalo.class);
		if (!stack.isEmpty()) {
			render(stack, player, event.getMatrixStack(), event.getPartialTicks());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void render(ItemStack stack, PlayerEntity player, MatrixStack ms, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffers = mc.getRenderTypeBuffers().getBufferSource();

		double renderPosX = mc.getRenderManager().info.getProjectedView().getX();
		double renderPosY = mc.getRenderManager().info.getProjectedView().getY();
		double renderPosZ = mc.getRenderManager().info.getProjectedView().getZ();

		ms.push();
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.getPosX() - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.getPosY() - player.prevPosY) * partialTicks + player.getEyeHeight();
		double posZ = player.prevPosZ + (player.getPosZ() - player.prevPosZ) * partialTicks;

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
			ms.push();
			ms.rotate(Vector3f.YP.rotationDegrees(rotationAngle));
			ms.translate(s * m, -0.75F, 0F);

			if (segmentLookedAt == seg) {
				inside = true;
			}

			ItemStack slotStack = getDisplayItem(player.world, stack, seg);
			if (!slotStack.isEmpty()) {
				float scale = seg == 0 ? 0.9F : 0.8F;
				ms.scale(scale, scale, scale);
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

				ms.rotate(Vector3f.YP.rotationDegrees(90.0F));
				Minecraft.getInstance().getItemRenderer().renderItem(slotStack, ItemCameraTransforms.TransformType.GUI, 0xF000F0, OverlayTexture.NO_OVERLAY, ms, buffers);
			}
			ms.pop();

			ms.push();
			ms.rotate(Vector3f.XP.rotationDegrees(180));
			float r = 1, g = 1, b = 1, a = alpha;
			if (inside) {
				a += 0.3F;
				y0 = -y;
			}

			if (seg % 2 == 0) {
				r = g = b = 0.6F;
			}

			IVertexBuilder buffer = buffers.getBuffer(layer);
			for (int i = 0; i < segAngles; i++) {
				Matrix4f mat = ms.getLast().getMatrix();
				float ang = i + seg * segAngles + shift;
				float xp = (float) Math.cos(ang * Math.PI / 180F) * s;
				float zp = (float) Math.sin(ang * Math.PI / 180F) * s;

				buffer.pos(mat, xp * m, y, zp * m).color(r, g, b, a).tex(u, v).endVertex();
				buffer.pos(mat, xp, y0, zp).color(r, g, b, a).tex(u, 0).endVertex();

				xp = (float) Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = (float) Math.sin((ang + 1) * Math.PI / 180F) * s;

				buffer.pos(mat, xp, y0, zp).color(r, g, b, a).tex(0, 0).endVertex();
				buffer.pos(mat, xp * m, y, zp * m).color(r, g, b, a).tex(0, v).endVertex();
			}
			y0 = 0;
			ms.pop();
		}
		ms.pop();
		buffers.finish();
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		int slot = getSegmentLookedAt(stack, player);

		if (slot == 0) {
			String name = craftingTable.getDisplayName().getString();
			int l = mc.fontRenderer.getStringWidth(name);
			int x = mc.getMainWindow().getScaledWidth() / 2 - l / 2;
			int y = mc.getMainWindow().getScaledHeight() / 2 - 65;

			AbstractGui.fill(ms, x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			AbstractGui.fill(ms, x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(craftingTable, mc.getMainWindow().getScaledWidth() / 2 - 8, mc.getMainWindow().getScaledHeight() / 2 - 52);

			mc.fontRenderer.drawStringWithShadow(ms, name, x, y, 0xFFFFFF);
		} else {
			IRecipe<CraftingInventory> recipe = getSavedRecipe(player.world, stack, slot);
			ITextComponent label;
			boolean setRecipe = false;

			if (recipe == null) {
				label = new TranslationTextComponent("botaniamisc.unsetRecipe");
				recipe = getLastRecipe(player.world, stack);
			} else {
				label = recipe.getRecipeOutput().getDisplayName();
				setRecipe = true;
			}

			renderRecipe(ms, label, recipe, player, setRecipe);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderRecipe(MatrixStack ms, ITextComponent label, @Nullable IRecipe<CraftingInventory> recipe, PlayerEntity player, boolean isSavedRecipe) {
		Minecraft mc = Minecraft.getInstance();

		if (recipe != null && !recipe.getRecipeOutput().isEmpty()) {
			int x = mc.getMainWindow().getScaledWidth() / 2 - 45;
			int y = mc.getMainWindow().getScaledHeight() / 2 - 90;

			AbstractGui.fill(ms, x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			AbstractGui.fill(ms, x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			AbstractGui.fill(ms, x + 66, y + 14, x + 92, y + 40, 0x22000000);
			AbstractGui.fill(ms, x - 2, y - 2, x + 56, y + 56, 0x22000000);

			int wrap = recipe instanceof IShapedRecipe<?> ? ((IShapedRecipe<?>) recipe).getRecipeWidth() : 3;
			for (int i = 0; i < recipe.getIngredients().size(); i++) {
				Ingredient ingr = recipe.getIngredients().get(i);
				if (ingr != Ingredient.EMPTY) {
					ItemStack stack = ingr.getMatchingStacks()[ClientTickHandler.ticksInGame / 20 % ingr.getMatchingStacks().length];
					int xpos = x + i % wrap * 18;
					int ypos = y + i / wrap * 18;
					AbstractGui.fill(ms, xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, xpos, ypos);
				}
			}

			mc.getItemRenderer().renderItemAndEffectIntoGUI(recipe.getRecipeOutput(), x + 72, y + 18);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, recipe.getRecipeOutput(), x + 72, y + 18);

		}

		int yoff = 110;
		if (isSavedRecipe && recipe != null && !canCraftHeuristic(player, recipe)) {
			String warning = TextFormatting.RED + I18n.format("botaniamisc.cantCraft");
			mc.fontRenderer.drawStringWithShadow(ms, warning, mc.getMainWindow().getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(warning) / 2.0F, mc.getMainWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.fontRenderer.func_238407_a_(ms, label, mc.getMainWindow().getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(label.getString()) / 2.0F, mc.getMainWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
	}

	public static class RecipePlacer extends ServerRecipePlacer<CraftingInventory> {
		public RecipePlacer(RecipeBookContainer<CraftingInventory> container) {
			super(container);
		}

		// [VanillaCopy] Based on super.place
		public boolean place(ServerPlayerEntity player, @Nullable IRecipe<CraftingInventory> recipe) {
			if (recipe != null) {
				this.playerInventory = player.inventory;
				this.recipeItemHelper.clear();
				player.inventory.accountStacks(this.recipeItemHelper);
				this.recipeBookContainer.fillStackedContents(this.recipeItemHelper);

				boolean ret;
				if (this.recipeItemHelper.canCraft(recipe, null)) {
					this.tryPlaceRecipe(recipe, false);
					ret = true;
				} else {
					this.clear();
					ret = false;
				}

				player.inventory.markDirty();
				return ret;
			}
			return false;
		}

		@Override
		public void clear() {
			super.clear();
		}
	}
}
