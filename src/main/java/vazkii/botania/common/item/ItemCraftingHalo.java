/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Dec 15, 2014, 3:53:30 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibGuiIDs;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCraftingHalo extends ItemMod {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW);
	private static final ItemStack craftingTable = new ItemStack(Blocks.crafting_table);

	private static final int SEGMENTS = 12;
	
	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_ITEM_PREFIX = "item";
	
	public ItemCraftingHalo() {
		setUnlocalizedName(LibItemNames.CRAFTING_HALO);
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		int segment = getSegmentLookedAt(player);
		boolean swing = false;
		
		if(segment == 0) {
			player.openGui(Botania.instance, LibGuiIDs.CRAFTING_HALO, world, 0, 0, 0);
			swing = true;
		} else assignRecipe(stack, segment);
		
		if(swing && world.isRemote)
			player.swingItem();
		
		return stack;
	}
	
	private int getSegmentLookedAt(EntityPlayer player) {
		float yaw = getCheckingAngle(player);
		
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for(int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if(yaw >= calcAngle && yaw < (calcAngle + segAngles))
				return seg;
		}
		return -1;
	}
	
	private float getCheckingAngle(EntityPlayer player) {
		float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90F;
		if(yaw < 0)
			yaw = 180F + (180F - Math.abs(yaw));
		return 360F - yaw;
	}

	public static ItemStack getItemForSlot(ItemStack stack, int slot) {
		if(slot == 0)
			return craftingTable;
		else if(slot >= SEGMENTS)
			return null;
		else {
			NBTTagCompound cmp = getStoredRecipeCompound(stack, slot);
			
			if(cmp != null) {
				ItemStack cmpStack = getLastCraftingItem(cmp, 9);
				return cmpStack;
			} else return null;
		}
	}
	
	public static void assignRecipe(ItemStack stack, int pos) {
		ItemStack itemForPos = getItemForSlot(stack, pos);
		if(itemForPos != null)
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, new NBTTagCompound());
		else {
			System.out.println("Assigning to " + pos + " " + getLastCraftingCompound(stack, false));
			ItemNBTHelper.setCompound(stack, TAG_STORED_RECIPE_PREFIX + pos, getLastCraftingCompound(stack, false));
		}
	}
	
	
	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		for(int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
			ItemStack stack = event.player.inventory.getStackInSlot(i);
			if(stack != null && stack.getItem() == this)
				saveRecipeToStack(event, stack);
		}
	}
	
	private void saveRecipeToStack(ItemCraftedEvent event, ItemStack stack) {
		NBTTagCompound cmp = new NBTTagCompound();
		NBTTagCompound cmp1 = new NBTTagCompound();

		event.crafting.writeToNBT(cmp1);
		cmp.setTag(TAG_ITEM_PREFIX + 9, cmp1);
		
		for(int i = 0; i < 9; i++) {
			cmp1 = new NBTTagCompound();
			ItemStack stackSlot = event.craftMatrix.getStackInSlot(i);
			if(stackSlot != null)
				stackSlot.writeToNBT(cmp1);
			cmp.setTag(TAG_ITEM_PREFIX + i, cmp1);
		}
		
		System.out.println("Set: " + cmp);
		ItemNBTHelper.setCompound(stack, TAG_LAST_CRAFTING, cmp);
	}
	
	public static ItemStack[] getLastCraftingItems(ItemStack stack) {
		return getCraftingItems(stack, SEGMENTS);
	}
	
	public static ItemStack[] getCraftingItems(ItemStack stack, int slot) {
		ItemStack[] stackArray = new ItemStack[10];

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
			return null;
		
		NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_ITEM_PREFIX + pos);
		if(cmp1 == null)
			return null;

		return ItemStack.loadItemStackFromNBT(cmp1);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == this)
			render(stack, player, event.partialTicks);
	}

	@SideOnly(Side.CLIENT)
	public void render(ItemStack stack, EntityPlayer player, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator tess = Tessellator.instance;
		tess.renderingWorldRenderer = false;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);

		int angles = 360;
		int segAngles = angles / SEGMENTS;

		float u = 1F;
		float v = 0.25F;

		float uper = u / angles;
		float ucurr = 0F;

		float s = 3F;
		float m = 0.8F;
		float x = u * s;
		float y = v * s * 2;
		float y0 = 0;
		
		int segmentLookedAt = getSegmentLookedAt(player);
		
		for(int seg = 0; seg < SEGMENTS; seg++) {
			boolean inside = false;
			float rotationAngle = ((float) seg + 0.5F) * segAngles;
			GL11.glPushMatrix();
			GL11.glRotatef(rotationAngle, 0F, 1F, 0F);
			GL11.glTranslatef(s * m, -0.75F, 0F);

			if(segmentLookedAt == seg)
				inside = true;
				
			ItemStack slotStack = getItemForSlot(stack, seg);
			if(slotStack != null) {
				mc.renderEngine.bindTexture(slotStack.getItem() instanceof ItemBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
				
				if(slotStack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(slotStack.getItem()).getRenderType())) {
					float scale = seg == 0 ? 0.75F : 0.5F;
					GL11.glScalef(scale, scale, scale);
					if(seg != 0)
						GL11.glTranslatef(0F, 0.5F, 0F);

					RenderBlocks.getInstance().renderBlockAsItem(Block.getBlockFromItem(slotStack.getItem()), slotStack.getItemDamage(), 1F);
				} else {
					GL11.glScalef(0.75F, 0.75F, 0.75F);
					GL11.glTranslatef(0F, 0F, 0.5F);
					int renderPass = 0;
					do {
						IIcon icon = slotStack.getItem().getIcon(slotStack, renderPass);
						if(icon != null) {
							GL11.glRotatef(90F, 0F, 1F, 0F);
							Color color = new Color(slotStack.getItem().getColorFromItemStack(slotStack, renderPass));
							GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
							float f = icon.getMinU();
							float f1 = icon.getMaxU();
							float f2 = icon.getMinV();
							float f3 = icon.getMaxV();
							ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
							GL11.glColor3f(1F, 1F, 1F);
						}
						renderPass++;
					} while(renderPass < slotStack.getItem().getRenderPasses(slotStack.getItemDamage()));
				}
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glRotatef(180F, 1F, 0F, 0F);
			float a = alpha;
			if(inside) {
				a += 0.3F;
				y0 = -y;
			}
				
			if(seg % 2 == 0)
				GL11.glColor4f(0.6F, 0.6F, 0.6F, a);
			else GL11.glColor4f(1F, 1F, 1F, a);

			mc.renderEngine.bindTexture(glowTexture);
			tess.startDrawingQuads();
			for(int i = 0; i < segAngles; i++) {
				int ang = i + seg * segAngles;
				double xp = Math.cos(ang * Math.PI / 180F) * s;
				double zp = Math.sin(ang * Math.PI / 180F) * s;

				tess.addVertexWithUV(xp * m, y, zp * m, u, v);
				tess.addVertexWithUV(xp, y0, zp, u, 0);

				ucurr += uper;
				xp = Math.cos((ang + 1) * Math.PI / 180F) * s;
				zp = Math.sin((ang + 1) * Math.PI / 180F) * s;

				tess.addVertexWithUV(xp, y0, zp, 0, 0);
				tess.addVertexWithUV(xp * m, y, zp * m, 0, v);
			}
			y0 = 0;
			tess.draw();
			
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

}
