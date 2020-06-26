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
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Optional;

public class ItemCraftingHalo extends Item {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_GREEN);
	private static final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_ITEM_PREFIX = "item";
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
		int segment = getSegmentLookedAt(stack, player);
		ItemStack itemForPos = getItemForSlot(stack, segment);

		if (!world.isRemote) {
			if (segment == 0) {
				IWorldPosCallable wp = IWorldPosCallable.of(world, BlockPos.ZERO); // pos is never used by workbench
				player.openContainer(new SimpleNamedContainerProvider(
						(windowId, playerInv, p) -> new ContainerCraftingHalo(windowId, playerInv, wp),
						stack.getDisplayName()));
			} else {
				if (itemForPos.isEmpty()) {
					assignRecipe(stack, itemForPos, segment);
				} else {
					tryCraft(player, stack, segment, true, getFakeInv(player), true);
				}
			}
		}

		return ActionResult.resultSuccess(stack);
	}

	public static IItemHandler getFakeInv(PlayerEntity player) {
		ItemStackHandler ret = new ItemStackHandler(player.inventory.mainInventory.size());
		for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ret.setStackInSlot(i, player.inventory.mainInventory.get(i).copy());
		}

		return ret;
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

	void tryCraft(PlayerEntity player, ItemStack stack, int slot, boolean particles, IItemHandler inv, boolean validate) {
		ItemStack itemForPos = getItemForSlot(stack, slot);
		if (itemForPos.isEmpty()) {
			return;
		}

		ItemStack[] recipe = getCraftingItems(stack, slot);
		if (validate) {
			recipe = validateRecipe(player, stack, recipe, slot);
		}

		if (canCraft(recipe, inv)) {
			doCraft(player, recipe, particles);
		}
	}

	private static ItemStack[] validateRecipe(PlayerEntity player, ItemStack stack, ItemStack[] recipe, int slot) {
		CraftingInventory fakeInv = new CraftingInventory(new WorkbenchContainer(-1, player.inventory), 3, 3);
		for (int i = 0; i < 9; i++) {
			fakeInv.setInventorySlotContents(i, recipe[i]);
		}

		ItemStack result = player.world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, fakeInv, player.world)
				.map(r -> r.getCraftingResult(fakeInv))
				.orElse(ItemStack.EMPTY);

		if (result.isEmpty()) {
			assignRecipe(stack, recipe[9], slot);
			return null;
		}

		if (!result.isItemEqual(recipe[9]) || result.getCount() != recipe[9].getCount() || !ItemStack.areItemStackTagsEqual(recipe[9], result)) {
			assignRecipe(stack, recipe[9], slot);
			return null;
		}

		return recipe;
	}

	private static boolean canCraft(ItemStack[] recipe, IItemHandler inv) {
		if (recipe == null) {
			return false;
		}

		if (!ItemHandlerHelper.insertItemStacked(inv, recipe[9], true).isEmpty()) {
			return false;
		}

		return consumeRecipeIngredients(recipe, inv, null);
	}

	private static void doCraft(PlayerEntity player, ItemStack[] recipe, boolean particles) {
		consumeRecipeIngredients(recipe, player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(EmptyHandler.INSTANCE), player);
		ItemHandlerHelper.giveItemToPlayer(player, recipe[9]);

		if (!particles) {
			return;
		}

		Vector3d lookVec3 = player.getLookVec();
		Vector3 centerVector = Vector3.fromEntityCenter(player).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
		float m = 0.1F;
		for (int i = 0; i < 4; i++) {
			WispParticleData data = WispParticleData.wisp(0.2F + 0.2F * (float) Math.random(), 1F, 0F, 1F);
			player.world.addParticle(data, centerVector.x, centerVector.y, centerVector.z, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m);
		}
	}

	private static boolean consumeRecipeIngredients(ItemStack[] recipe, IItemHandler inv, PlayerEntity player) {
		for (int i = 0; i < 9; i++) {
			ItemStack ingredient = recipe[i];
			if (!ingredient.isEmpty() && !consumeFromInventory(ingredient, inv, player)) {
				return false;
			}
		}

		return true;
	}

	private static boolean consumeFromInventory(ItemStack stack, IItemHandler inv, PlayerEntity player) {
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if (!stackAt.isEmpty() && stack.isItemEqual(stackAt) && ItemStack.areItemStackTagsEqual(stack, stackAt)) {
				boolean consume = true;

				ItemStack container = stackAt.getItem().getContainerItem(stackAt);
				if (!container.isEmpty()) {
					if (container == stackAt) {
						consume = false;
					} else {
						if (player == null) {
							ItemHandlerHelper.insertItem(inv, container, false);
						} else {
							ItemHandlerHelper.giveItemToPlayer(player, container);
						}
					}
				}

				if (consume) {
					inv.extractItem(i, 1, false);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity player) {
		int segment = getSegmentLookedAt(stack, player);
		if (segment == 0) {
			return false;
		}

		ItemStack itemForPos = getItemForSlot(stack, segment);

		if (!itemForPos.isEmpty() && player.isSneaking()) {
			assignRecipe(stack, itemForPos, segment);
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

	private static ItemStack getItemForSlot(ItemStack stack, int slot) {
		if (slot == 0) {
			return craftingTable;
		} else if (slot >= SEGMENTS) {
			return ItemStack.EMPTY;
		} else {
			CompoundNBT cmp = getStoredRecipeCompound(stack, slot);

			if (cmp != null) {
				return getLastCraftingItem(cmp, 9);
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	private static void assignRecipe(ItemStack stack, ItemStack itemForPos, int pos) {
		if (!itemForPos.isEmpty()) {
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, new CompoundNBT());
		} else {
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, getLastCraftingCompound(stack, false));
		}
	}

	private void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if (!(event.getPlayer().openContainer instanceof ContainerCraftingHalo) || !(event.getInventory() instanceof CraftingInventory)) {
			return;
		}

		for (int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
			ItemStack stack = event.getPlayer().inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemCraftingHalo) {
				saveRecipeToStack(event, stack);
			}
		}
	}

	private void saveRecipeToStack(PlayerEvent.ItemCraftedEvent event, ItemStack stack) {
		CompoundNBT cmp = new CompoundNBT();

		if (event.getInventory() instanceof CraftingInventory) {
			Optional<ItemStack> result = event.getPlayer().world.getRecipeManager().getRecipe(IRecipeType.CRAFTING,
					(CraftingInventory) event.getInventory(), event.getPlayer().world)
					.map(r -> r.getCraftingResult((CraftingInventory) event.getInventory()));
			if (result.isPresent() && !result.get().isEmpty()) {
				cmp.put(TAG_ITEM_PREFIX + 9, result.get().write(new CompoundNBT()));

				for (int i = 0; i < 9; i++) {
					CompoundNBT ingr = new CompoundNBT();
					ItemStack stackSlot = event.getInventory().getStackInSlot(i);

					if (!stackSlot.isEmpty()) {
						ItemStack writeStack = stackSlot.copy();
						writeStack.setCount(1);
						ingr = writeStack.write(new CompoundNBT());
					}
					cmp.put(TAG_ITEM_PREFIX + i, ingr);
				}
			}

			ItemNBTHelper.setCompound(stack, TAG_LAST_CRAFTING, cmp);
		}
	}

	private static ItemStack[] getCraftingItems(ItemStack stack, int slot) {
		ItemStack[] stackArray = new ItemStack[10];
		Arrays.fill(stackArray, ItemStack.EMPTY);

		CompoundNBT cmp = getStoredRecipeCompound(stack, slot);
		if (cmp != null) {
			for (int i = 0; i < stackArray.length; i++) {
				stackArray[i] = getLastCraftingItem(cmp, i);
			}
		}

		return stackArray;
	}

	private static CompoundNBT getLastCraftingCompound(ItemStack stack, boolean nullify) {
		return ItemNBTHelper.getCompound(stack, TAG_LAST_CRAFTING, nullify);
	}

	private static CompoundNBT getStoredRecipeCompound(ItemStack stack, int slot) {
		return slot == SEGMENTS ? getLastCraftingCompound(stack, true) : ItemNBTHelper.getCompound(stack, TAG_STORED_RECIPE_PREFIX + slot, true);
	}

	private static ItemStack getLastCraftingItem(CompoundNBT cmp, int pos) {
		if (cmp == null) {
			return ItemStack.EMPTY;
		}

		return ItemStack.read(cmp.getCompound(TAG_ITEM_PREFIX + pos));
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

			ItemStack slotStack = getItemForSlot(stack, seg);
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

			AbstractGui.func_238467_a_(ms, x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			AbstractGui.func_238467_a_(ms, x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(craftingTable, mc.getMainWindow().getScaledWidth() / 2 - 8, mc.getMainWindow().getScaledHeight() / 2 - 52);

			mc.fontRenderer.func_238405_a_(ms, name, x, y, 0xFFFFFF);
		} else {
			ItemStack[] recipe = getCraftingItems(stack, slot);
			ITextComponent label = new TranslationTextComponent("botaniamisc.unsetRecipe");
			boolean setRecipe = false;

			if (recipe[9].isEmpty()) {
				recipe = getCraftingItems(stack, SEGMENTS);
			} else {
				label = recipe[9].getDisplayName();
				setRecipe = true;
			}

			renderRecipe(ms, label, recipe, player, setRecipe);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderRecipe(MatrixStack ms, ITextComponent label, ItemStack[] recipe, PlayerEntity player, boolean setRecipe) {
		Minecraft mc = Minecraft.getInstance();

		if (!recipe[9].isEmpty()) {
			int x = mc.getMainWindow().getScaledWidth() / 2 - 45;
			int y = mc.getMainWindow().getScaledHeight() / 2 - 90;

			AbstractGui.func_238467_a_(ms, x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			AbstractGui.func_238467_a_(ms, x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			AbstractGui.func_238467_a_(ms, x + 66, y + 14, x + 92, y + 40, 0x22000000);
			AbstractGui.func_238467_a_(ms, x - 2, y - 2, x + 56, y + 56, 0x22000000);

			for (int i = 0; i < 9; i++) {
				ItemStack stack = recipe[i];
				if (!stack.isEmpty()) {
					int xpos = x + i % 3 * 18;
					int ypos = y + i / 3 * 18;
					AbstractGui.func_238467_a_(ms, xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, xpos, ypos);
				}
			}

			mc.getItemRenderer().renderItemAndEffectIntoGUI(recipe[9], x + 72, y + 18);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, recipe[9], x + 72, y + 18);

		}

		int yoff = 110;
		if (setRecipe && !canCraft(recipe, getFakeInv(player))) {
			String warning = TextFormatting.RED + I18n.format("botaniamisc.cantCraft");
			mc.fontRenderer.func_238405_a_(ms, warning, mc.getMainWindow().getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(warning) / 2.0F, mc.getMainWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.fontRenderer.func_238407_a_(ms, label, mc.getMainWindow().getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(label.getString()) / 2.0F, mc.getMainWindow().getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
	}
}
