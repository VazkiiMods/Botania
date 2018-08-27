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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ItemCraftingHalo extends ItemMod {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_GREEN);
	private static final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_ITEM_PREFIX = "item";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";

	public ItemCraftingHalo() {
		this(LibItemNames.CRAFTING_HALO);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemCraftingHalo(String name) {
		super(name);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int segment = getSegmentLookedAt(stack, player);
		ItemStack itemForPos = getItemForSlot(stack, segment);

		if(segment == 0)
			player.openGui(Botania.instance, LibGuiIDs.CRAFTING_HALO, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
		else {
			if(itemForPos.isEmpty())
				assignRecipe(stack, itemForPos, segment);
			else tryCraft(player, stack, segment, true, getFakeInv(player), true);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	public static IItemHandler getFakeInv(EntityPlayer player) {
		ItemStackHandler ret = new ItemStackHandler(player.inventory.mainInventory.size());
		for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ret.setStackInSlot(i, player.inventory.mainInventory.get(i).copy());
		}

		return ret;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		if (!(entity instanceof EntityLivingBase))
			return;
		EntityLivingBase living = (EntityLivingBase) entity;

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getHeldItemOffhand() == stack)
			equipped = true;

		if(eqLastTick != equipped)
			setEquipped(stack, equipped);

		if(!equipped) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2;
			setRotationBase(stack, getCheckingAngle((EntityLivingBase) entity) - shift);
		}
	}

	void tryCraft(EntityPlayer player, ItemStack stack, int slot, boolean particles, IItemHandler inv, boolean validate) {
		ItemStack itemForPos = getItemForSlot(stack, slot);
		if(itemForPos.isEmpty())
			return;

		ItemStack[] recipe = getCraftingItems(stack, slot);
		if(validate)
			recipe = validateRecipe(player, stack, recipe, slot);

		if(canCraft(recipe, inv))
			doCraft(player, recipe, particles);
	}

	private static ItemStack[] validateRecipe(EntityPlayer player, ItemStack stack, ItemStack[] recipe, int slot) {
		InventoryCrafting fakeInv = new InventoryCrafting(new ContainerWorkbench(player.inventory, player.world, BlockPos.ORIGIN), 3, 3);
		for(int i = 0; i < 9; i++)
			fakeInv.setInventorySlotContents(i, recipe[i]);

		ItemStack result = CraftingManager.findMatchingResult(fakeInv, player.world);
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

	private static void doCraft(EntityPlayer player, ItemStack[] recipe, boolean particles) {
		consumeRecipeIngredients(recipe, player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), player);
		ItemHandlerHelper.giveItemToPlayer(player, recipe[9]);

		if(!particles)
			return;

		Vec3d lookVec3 = player.getLookVec();
		Vector3 centerVector = Vector3.fromEntityCenter(player).add(lookVec3.x * 3, 1.3, lookVec3.z * 3);
		float m = 0.1F;
		for(int i = 0; i < 4; i++)
			Botania.proxy.wispFX(centerVector.x, centerVector.y, centerVector.z, 1F, 0F, 1F, 0.2F + 0.2F * (float) Math.random(), ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m, ((float) Math.random() - 0.5F) * m);
	}

	private static boolean consumeRecipeIngredients(ItemStack[] recipe, IItemHandler inv, EntityPlayer player) {
		for(int i = 0; i < 9; i++) {
			ItemStack ingredient = recipe[i];
			if(!ingredient.isEmpty() && !consumeFromInventory(ingredient, inv, player))
				return false;
		}

		return true;
	}

	private static boolean consumeFromInventory(ItemStack stack, IItemHandler inv, EntityPlayer player) {
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
	public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
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

	private static int getSegmentLookedAt(ItemStack stack, EntityLivingBase player) {
		getRotationBase(stack);
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

	private static float getCheckingAngle(EntityLivingBase player) {
		return getCheckingAngle(player, 0F);
	}

	// Screw the way minecraft handles rotation
	// Really...
	private static float getCheckingAngle(EntityLivingBase player, float base) {
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

	public static ItemStack getItemForSlot(ItemStack stack, int slot) {
		if(slot == 0)
			return craftingTable;
		else if(slot >= SEGMENTS)
			return ItemStack.EMPTY;
		else {
			NBTTagCompound cmp = getStoredRecipeCompound(stack, slot);

			if(cmp != null) {
				ItemStack cmpStack = getLastCraftingItem(cmp, 9);
				return cmpStack;
			} else return ItemStack.EMPTY;
		}
	}

	public static void assignRecipe(ItemStack stack, ItemStack itemForPos, int pos) {
		if(!itemForPos.isEmpty())
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, new NBTTagCompound());
		else
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, getLastCraftingCompound(stack, false));
	}

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		if(!(event.player.openContainer instanceof ContainerCraftingHalo) || !(event.craftMatrix instanceof InventoryCrafting))
			return;

		for(int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
			ItemStack stack = event.player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemCraftingHalo)
				saveRecipeToStack(event, stack);
		}
	}

	private void saveRecipeToStack(ItemCraftedEvent event, ItemStack stack) {
		NBTTagCompound cmp = new NBTTagCompound();
		NBTTagCompound cmp1 = new NBTTagCompound();

		ItemStack result = CraftingManager.findMatchingResult((InventoryCrafting) event.craftMatrix, event.player.world);
		if(!result.isEmpty()) {
			cmp1 = result.writeToNBT(cmp1);
			cmp.setTag(TAG_ITEM_PREFIX + 9, cmp1);

			for(int i = 0; i < 9; i++) {
				cmp1 = new NBTTagCompound();
				ItemStack stackSlot = event.craftMatrix.getStackInSlot(i);

				if(!stackSlot.isEmpty()) {
					ItemStack writeStack = stackSlot.copy();
					writeStack.setCount(1);
					cmp1 = writeStack.writeToNBT(cmp1);
				}
				cmp.setTag(TAG_ITEM_PREFIX + i, cmp1);
			}
		}

		ItemNBTHelper.setCompound(stack, TAG_LAST_CRAFTING, cmp);
	}

	public static ItemStack[] getLastCraftingItems(ItemStack stack) {
		return getCraftingItems(stack, SEGMENTS);
	}

	public static ItemStack[] getCraftingItems(ItemStack stack, int slot) {
		ItemStack[] stackArray = new ItemStack[10];
		Arrays.fill(stackArray, ItemStack.EMPTY);

		NBTTagCompound cmp = getStoredRecipeCompound(stack, slot);
		if(cmp != null)
			for(int i = 0; i < stackArray.length; i++)
				stackArray[i] = getLastCraftingItem(cmp, i);

		return stackArray;
	}

	public static NBTTagCompound getLastCraftingCompound(ItemStack stack, boolean nullify) {
		return ItemNBTHelper.getCompound(stack, TAG_LAST_CRAFTING, nullify);
	}

	public static NBTTagCompound getStoredRecipeCompound(ItemStack stack, int slot) {
		return slot == SEGMENTS ? getLastCraftingCompound(stack, true) : ItemNBTHelper.getCompound(stack, TAG_STORED_RECIPE_PREFIX + slot, true);
	}

	public static ItemStack getLastCraftingItem(ItemStack stack, int pos) {
		return getLastCraftingItem(getLastCraftingCompound(stack, true), pos);
	}

	public static ItemStack getLastCraftingItem(NBTTagCompound cmp, int pos) {
		if(cmp == null)
			return ItemStack.EMPTY;

		NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_ITEM_PREFIX + pos);
		if(cmp1 == null)
			return ItemStack.EMPTY;

		return new ItemStack(cmp1);
	}

	public static boolean wasEquipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false);
	}

	public static void setEquipped(ItemStack stack, boolean equipped) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped);
	}

	public static float getRotationBase(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0F);
	}

	public static void setRotationBase(ItemStack stack, float rotation) {
		ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemCraftingHalo.class);
		if(!stack.isEmpty())
			render(stack, player, event.getPartialTicks());
	}

	@SideOnly(Side.CLIENT)
	public void render(ItemStack stack, EntityPlayer player, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
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

		GlStateManager.translate(posX - renderPosX, posY - renderPosY + player.getDefaultEyeHeight(), posZ - renderPosZ);


		float base = getRotationBase(stack);
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = base - segAngles / 2;

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
			GlStateManager.rotate(rotationAngle, 0F, 1F, 0F);
			GlStateManager.translate(s * m, -0.75F, 0F);

			if(segmentLookedAt == seg)
				inside = true;

			ItemStack slotStack = getItemForSlot(stack, seg);
			if(!slotStack.isEmpty()) {
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				RenderHelper.enableStandardItemLighting();
				float scale = seg == 0 ? 0.9F : 0.8F;
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				Minecraft.getMinecraft().getRenderItem().renderItem(slotStack, ItemCameraTransforms.TransformType.GUI);
				RenderHelper.disableStandardItemLighting();
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			float a = alpha;
			if(inside) {
				a += 0.3F;
				y0 = -y;
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if(seg % 2 == 0)
				GlStateManager.color(0.6F, 0.6F, 0.6F, a);
			else GlStateManager.color(1F, 1F, 1F, a);

			GlStateManager.disableCull();
			ItemCraftingHalo item = (ItemCraftingHalo) stack.getItem();
			mc.renderEngine.bindTexture(item.getGlowResource());
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

	@SideOnly(Side.CLIENT)
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		int slot = getSegmentLookedAt(stack, player);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(slot == 0) {
			String name = craftingTable.getDisplayName();
			int l = mc.fontRenderer.getStringWidth(name);
			int x = resolution.getScaledWidth() / 2 - l / 2;
			int y = resolution.getScaledHeight() / 2 - 65;

			Gui.drawRect(x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
			Gui.drawRect(x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			mc.getRenderItem().renderItemAndEffectIntoGUI(craftingTable, resolution.getScaledWidth() / 2 - 8, resolution.getScaledHeight() / 2 - 52);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

			mc.fontRenderer.drawStringWithShadow(name, x, y, 0xFFFFFF);
		} else {
			ItemStack[] recipe = getCraftingItems(stack, slot);
			String label = I18n.format("botaniamisc.unsetRecipe");
			boolean setRecipe = false;

			if(recipe[9].isEmpty())
				recipe = getCraftingItems(stack, SEGMENTS);
			else {
				label = recipe[9].getDisplayName();
				setRecipe = true;
			}

			renderRecipe(resolution, label, recipe, player, setRecipe);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void renderRecipe(ScaledResolution resolution, String label, ItemStack[] recipe, EntityPlayer player, boolean setRecipe) {
		Minecraft mc = Minecraft.getMinecraft();

		if(!recipe[9].isEmpty()) {
			int x = resolution.getScaledWidth() / 2 - 45;
			int y = resolution.getScaledHeight() / 2 - 90;

			Gui.drawRect(x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
			Gui.drawRect(x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

			Gui.drawRect(x + 66, y + 14, x + 92, y + 40, 0x22000000);
			Gui.drawRect(x - 2, y - 2, x + 56, y + 56, 0x22000000);

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			for(int i = 0; i < 9; i++) {
				ItemStack stack = recipe[i];
				if(!stack.isEmpty()) {
					int xpos = x + i % 3 * 18;
					int ypos = y + i / 3 * 18;
					Gui.drawRect(xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

					mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xpos, ypos);
				}
			}

			mc.getRenderItem().renderItemAndEffectIntoGUI(recipe[9], x + 72, y + 18);
			mc.getRenderItem().renderItemOverlays(mc.fontRenderer, recipe[9], x + 72, y + 18);

			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}

		int yoff = 110;
		if(setRecipe && !canCraft(recipe, getFakeInv(player))) {
			String warning = TextFormatting.RED + I18n.format("botaniamisc.cantCraft");
			mc.fontRenderer.drawStringWithShadow(warning, resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(warning) / 2, resolution.getScaledHeight() / 2 - yoff, 0xFFFFFF);
			yoff += 12;
		}

		mc.fontRenderer.drawStringWithShadow(label, resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(label) / 2, resolution.getScaledHeight() / 2 - yoff, 0xFFFFFF);
	}
}
