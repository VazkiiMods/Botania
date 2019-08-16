/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 15, 2014, 3:53:30 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.AbstractGui;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibGuiIDs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public class ItemCraftingHalo extends ItemMod {

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

		if(!world.isRemote) {
			if(segment == 0) {
				player.openContainer(new SimpleNamedContainerProvider(
						(windowId, playerInv, p) -> new ContainerCraftingHalo(windowId, playerInv),
						stack.getDisplayName()));
			} else {
				if(itemForPos.isEmpty())
					assignRecipe(stack, itemForPos, segment);
				else tryCraft(player, stack, segment, true, getFakeInv(player), true);
			}
		}


		return ActionResult.newResult(ActionResultType.SUCCESS, stack);
	}

	public static IItemHandler getFakeInv(PlayerEntity player) {
		ItemStackHandler ret = new ItemStackHandler(player.inventory.mainInventory.size());
		for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ret.setStackInSlot(i, player.inventory.mainInventory.get(i).copy());
		}

		return ret;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		if (!(entity instanceof LivingEntity))
			return;
		LivingEntity living = (LivingEntity) entity;

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getHeldItemOffhand() == stack)
			equipped = true;

		if(eqLastTick != equipped)
			setEquipped(stack, equipped);

		if(!equipped) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2.0F;
			setRotationBase(stack, getCheckingAngle((LivingEntity) entity) - shift);
		}
	}

	void tryCraft(PlayerEntity player, ItemStack stack, int slot, boolean particles, IItemHandler inv, boolean validate) {
		ItemStack itemForPos = getItemForSlot(stack, slot);
		if(itemForPos.isEmpty())
			return;

		ItemStack[] recipe = getCraftingItems(stack, slot);
		if(validate)
			recipe = validateRecipe(player, stack, recipe, slot);

		if(canCraft(recipe, inv))
			doCraft(player, recipe, particles);
	}

	private static ItemStack[] validateRecipe(PlayerEntity player, ItemStack stack, ItemStack[] recipe, int slot) {
		CraftingInventory fakeInv = new CraftingInventory(new WorkbenchContainer(-1, player.inventory), 3, 3);
		for(int i = 0; i < 9; i++)
			fakeInv.setInventorySlotContents(i, recipe[i]);

		ItemStack result = player.world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, fakeInv, player.world)
				.map(r -> r.getCraftingResult(fakeInv))
				.orElse(ItemStack.EMPTY);

		if(result.isEmpty()) {
			assignRecipe(stack, recipe[9], slot);
			return null;
		}

		if(!result.isItemEqual(recipe[9]) || result.getCount() != recipe[9].getCount() || !ItemStack.areItemStackTagsEqual(recipe[9], result)) {
			assignRecipe(stack, recipe[9], slot);
			return null;
		}

		return recipe;
	}

	private static boolean canCraft(ItemStack[] recipe, IItemHandler inv) {
		if(recipe == null)
			return false;

		if(!ItemHandlerHelper.insertItemStacked(inv, recipe[9], true).isEmpty())
			return false;

		return consumeRecipeIngredients(recipe, inv, null);
	}

	private static void doCraft(PlayerEntity player, ItemStack[] recipe, boolean particles) {
		consumeRecipeIngredients(recipe, player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(EmptyHandler.INSTANCE), player);
		ItemHandlerHelper.giveItemToPlayer(player, recipe[9]);

		if(!particles)
			return;

		Vec3d lookVec3 = player.getLookVec();
		Vector3 centerVector = Vector3.fromEntityCenter(player).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
		float m = 0.1F;
		for(int i = 0; i < 4; i++)
			Botania.proxy.wispFX(centerVector.x, centerVector.y, centerVector.z, 1F, 0F, 1F, 0.2F + 0.2F * (float) Math.random(), ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m);
	}

	private static boolean consumeRecipeIngredients(ItemStack[] recipe, IItemHandler inv, PlayerEntity player) {
		for(int i = 0; i < 9; i++) {
			ItemStack ingredient = recipe[i];
			if(!ingredient.isEmpty() && !consumeFromInventory(ingredient, inv, player))
				return false;
		}

		return true;
	}

	private static boolean consumeFromInventory(ItemStack stack, IItemHandler inv, PlayerEntity player) {
		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if(!stackAt.isEmpty() && stack.isItemEqual(stackAt) && ItemStack.areItemStackTagsEqual(stack, stackAt)) {
				boolean consume = true;

				ItemStack container = stackAt.getItem().getContainerItem(stackAt);
				if(!container.isEmpty()) {
					if(container == stackAt)
						consume = false;
					else {
						if(player == null)
							ItemHandlerHelper.insertItem(inv, container, false);
						else
							ItemHandlerHelper.giveItemToPlayer(player, container);
					}
				}

				if(consume)
					inv.extractItem(i, 1, false);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity player) {
		int segment = getSegmentLookedAt(stack, player);
		if(segment == 0)
			return false;

		ItemStack itemForPos = getItemForSlot(stack, segment);

		if(!itemForPos.isEmpty() && player.isSneaking()) {
			assignRecipe(stack, itemForPos, segment);
			return true;
		}

		return false;
	}

	private static int getSegmentLookedAt(ItemStack stack, LivingEntity player) {
		float yaw = getCheckingAngle(player, getRotationBase(stack));

		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for(int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if(yaw >= calcAngle && yaw < calcAngle + segAngles)
				return seg;
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

		if(yaw < 0)
			yaw = 180F + (180F + yaw);
		yaw -= 360F - base;
		float angle = 360F - yaw + shift;

		if(angle < 0)
			angle = 360F + angle;

		return angle;
	}

	private static ItemStack getItemForSlot(ItemStack stack, int slot) {
		if(slot == 0)
			return craftingTable;
		else if(slot >= SEGMENTS)
			return ItemStack.EMPTY;
		else {
			CompoundNBT cmp = getStoredRecipeCompound(stack, slot);

			if(cmp != null) {
				return getLastCraftingItem(cmp, 9);
			} else return ItemStack.EMPTY;
		}
	}

	private static void assignRecipe(ItemStack stack, ItemStack itemForPos, int pos) {
		if(!itemForPos.isEmpty())
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, new CompoundNBT());
		else
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, getLastCraftingCompound(stack, false));
	}

	private void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if(!(event.getPlayer().openContainer instanceof ContainerCraftingHalo) || !(event.getInventory() instanceof CraftingInventory))
			return;

		for(int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
			ItemStack stack = event.getPlayer().inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemCraftingHalo)
				saveRecipeToStack(event, stack);
		}
	}

	private void saveRecipeToStack(PlayerEvent.ItemCraftedEvent event, ItemStack stack) {
		CompoundNBT cmp = new CompoundNBT();

		if(event.getInventory() instanceof CraftingInventory) {
			Optional<ItemStack> result = event.getPlayer().world.getRecipeManager().getRecipe(IRecipeType.CRAFTING,
					(CraftingInventory) event.getInventory(), event.getPlayer().world)
					.map(r -> r.getCraftingResult((CraftingInventory) event.getInventory()));
			if(result.isPresent() && !result.get().isEmpty()) {
				cmp.put(TAG_ITEM_PREFIX + 9, result.get().write(new CompoundNBT()));

				for(int i = 0; i < 9; i++) {
					CompoundNBT ingr = new CompoundNBT();
					ItemStack stackSlot = event.getInventory().getStackInSlot(i);

					if(!stackSlot.isEmpty()) {
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
		if(cmp != null)
			for(int i = 0; i < stackArray.length; i++)
				stackArray[i] = getLastCraftingItem(cmp, i);

		return stackArray;
	}

	private static CompoundNBT getLastCraftingCompound(ItemStack stack, boolean nullify) {
		return ItemNBTHelper.getCompound(stack, TAG_LAST_CRAFTING, nullify);
	}

	private static CompoundNBT getStoredRecipeCompound(ItemStack stack, int slot) {
		return slot == SEGMENTS ? getLastCraftingCompound(stack, true) : ItemNBTHelper.getCompound(stack, TAG_STORED_RECIPE_PREFIX + slot, true);
	}

	private static ItemStack getLastCraftingItem(CompoundNBT cmp, int pos) {
		if(cmp == null)
			return ItemStack.EMPTY;

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
		if(!stack.isEmpty())
			render(stack, player, event.getPartialTicks());
	}

	@OnlyIn(Dist.CLIENT)
	public void render(ItemStack stack, PlayerEntity player, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		Tessellator tess = Tessellator.getInstance();

		double renderPosX = mc.getRenderManager().renderPosX;
		double renderPosY = mc.getRenderManager().renderPosY;
		double renderPosZ = mc.getRenderManager().renderPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GlStateManager.translated(posX - renderPosX, posY - renderPosY + player.getEyeHeight(), posZ - renderPosZ);


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

		for(int seg = 0; seg < SEGMENTS; seg++) {
			boolean inside = false;
			float rotationAngle = (seg + 0.5F) * segAngles + shift;
			GlStateManager.pushMatrix();
			GlStateManager.rotatef(rotationAngle, 0F, 1F, 0F);
			GlStateManager.translatef(s * m, -0.75F, 0F);

			if(segmentLookedAt == seg)
				inside = true;

			ItemStack slotStack = getItemForSlot(stack, seg);
			if(!slotStack.isEmpty()) {
				mc.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				RenderHelper.enableStandardItemLighting();
				float scale = seg == 0 ? 0.9F : 0.8F;
				GlStateManager.scalef(scale, scale, scale);
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				GlStateManager.translatef(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

				GlStateManager.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
				Minecraft.getInstance().getItemRenderer().renderItem(slotStack, ItemCameraTransforms.TransformType.GUI);
				RenderHelper.disableStandardItemLighting();
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.rotatef(180F, 1F, 0F, 0F);
			float a = alpha;
			if(inside) {
				a += 0.3F;
				y0 = -y;
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if(seg % 2 == 0)
				GlStateManager.color4f(0.6F, 0.6F, 0.6F, a);
			else GlStateManager.color4f(1F, 1F, 1F, a);

			GlStateManager.disableCull();
			ItemCraftingHalo item = (ItemCraftingHalo) stack.getItem();
			mc.textureManager.bindTexture(item.getGlowResource());
			tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			for(int i = 0; i < segAngles; i++) {
				float ang = i + seg * segAngles + shift;
				double xp = Math.cos(ang * Math.PI / 180F) * s;
				double zp = Math.sin(ang * Math.PI / 180F) * s;

				tess.getBuffer().pos(xp * m, y, zp * m).tex(u, v).endVertex();
				tess.getBuffer().pos(xp, y0, zp).tex(u, 0).endVertex();

				xp = Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = Math.sin((ang + 1) * Math.PI / 180F) * s;

				tess.getBuffer().pos(xp, y0, zp).tex(0, 0).endVertex();
				tess.getBuffer().pos(xp * m, y, zp * m).tex(0, v).endVertex();
			}
			y0 = 0;
			tess.draw();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(PlayerEntity player, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		int slot = getSegmentLookedAt(stack, player);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(slot == 0) {
			String name = craftingTable.getDisplayName().getString();
			int l = mc.fontRenderer.getStringWidth(name);
			int x = mc.mainWindow.getScaledWidth() / 2 - l / 2;
			int y = mc.mainWindow.getScaledHeight() / 2 - 65;

			AbstractGui.fill(x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			AbstractGui.fill(x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			mc.getItemRenderer().renderItemAndEffectIntoGUI(craftingTable, mc.mainWindow.getScaledWidth() / 2 - 8, mc.mainWindow.getScaledHeight() / 2 - 52);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

			mc.fontRenderer.drawStringWithShadow(name, x, y, 0xFFFFFF);
		} else {
			ItemStack[] recipe = getCraftingItems(stack, slot);
			ITextComponent label = new TranslationTextComponent("botaniamisc.unsetRecipe");
			boolean setRecipe = false;

			if(recipe[9].isEmpty())
				recipe = getCraftingItems(stack, SEGMENTS);
			else {
				label = recipe[9].getDisplayName();
				setRecipe = true;
			}

			renderRecipe(label, recipe, player, setRecipe);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderRecipe(ITextComponent label, ItemStack[] recipe, PlayerEntity player, boolean setRecipe) {
		Minecraft mc = Minecraft.getInstance();

		if(!recipe[9].isEmpty()) {
			int x = mc.mainWindow.getScaledWidth() / 2 - 45;
			int y = mc.mainWindow.getScaledHeight() / 2 - 90;

			AbstractGui.fill(x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			AbstractGui.fill(x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			AbstractGui.fill(x + 66, y + 14, x + 92, y + 40, 0x22000000);
			AbstractGui.fill(x - 2, y - 2, x + 56, y + 56, 0x22000000);

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			for(int i = 0; i < 9; i++) {
				ItemStack stack = recipe[i];
				if(!stack.isEmpty()) {
					int xpos = x + i % 3 * 18;
					int ypos = y + i / 3 * 18;
					AbstractGui.fill(xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, xpos, ypos);
				}
			}

			mc.getItemRenderer().renderItemAndEffectIntoGUI(recipe[9], x + 72, y + 18);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, recipe[9], x + 72, y + 18);

			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}

		int yoff = 110;
		if(setRecipe && !canCraft(recipe, getFakeInv(player))) {
			String warning = TextFormatting.RED + I18n.format("botaniamisc.cantCraft");
			mc.fontRenderer.drawStringWithShadow(warning, mc.mainWindow.getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(warning) / 2.0F, mc.mainWindow.getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.fontRenderer.drawStringWithShadow(label.getFormattedText(), mc.mainWindow.getScaledWidth() / 2.0F - mc.fontRenderer.getStringWidth(label.getString()) / 2.0F, mc.mainWindow.getScaledHeight() / 2.0F - yoff, 0xFFFFFF);
	}
}
