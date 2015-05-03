/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 10:13:26 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlugelEye extends ItemRelic {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_CYAN);

	private static final int SEGMENTS = 12;
	private static final MultiversePosition FALLBACK_POSITION = new MultiversePosition(0, -1, 0, 0);

	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";
	private static final String TAG_WARP_PREFIX = "warp";
	private static final String TAG_POS_X = "posX";
	private static final String TAG_POS_Y = "posY";
	private static final String TAG_POS_Z = "posZ";
	private static final String TAG_DIMENSION = "dim";
	private static final String TAG_FIRST_TICK = "firstTick";

	IIcon[] signs;

	public ItemFlugelEye() {
		super(LibItemNames.FLUGEL_EYE);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		signs = new IIcon[12];
		for(int i = 0; i < 12; i++)
			signs[i] = IconHelper.forName(par1IconRegister, "sign" + i);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(isRightPlayer(player, stack)) {
			int segment = getSegmentLookedAt(stack, player);
			MultiversePosition pos = getWarpPoint(stack, segment);
			if(pos.isValid()) {
				if(pos.dim == world.provider.dimensionId && player instanceof EntityPlayerMP) {
					((EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
					world.playSoundAtEntity(player, "mob.endermen.portal", 1F, 1F);
				}
			} else setWarpPoint(stack, segment, player.posX, player.posY, player.posZ, world.provider.dimensionId);
		}

		return stack;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
		if(player.isSneaking() && player instanceof EntityPlayer && isRightPlayer((EntityPlayer) player, stack)) {
			int segment = getSegmentLookedAt(stack, player);
			MultiversePosition pos = getWarpPoint(stack, segment);
			if(pos.isValid()) {
				setWarpPoint(stack, segment, 0, -1, 0, 0);
				return true;
			}
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		super.onUpdate(stack, world, entity, pos, equipped);
		boolean eqLastTick = wasEquipped(stack);
		boolean firstTick = isFirstTick(stack);
		if(eqLastTick != equipped)
			setEquipped(stack, equipped);

		if((!equipped || firstTick) && entity instanceof EntityLivingBase) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2;
			setRotationBase(stack, getCheckingAngle((EntityLivingBase) entity) - shift);
			if(firstTick)
				tickFirst(stack);
		}
	}

	private static int getSegmentLookedAt(ItemStack stack, EntityLivingBase player) {
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
		float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90F;
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

	public static boolean isFirstTick(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FIRST_TICK, true);
	}

	public static void tickFirst(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_FIRST_TICK, false);
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

	public static void setWarpPoint(ItemStack stack, int warp, double x, double y, double z, int dim) {
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setDouble(TAG_POS_X, x);
		cmp.setDouble(TAG_POS_Y, y);
		cmp.setDouble(TAG_POS_Z, z);
		cmp.setInteger(TAG_DIMENSION, dim);
		ItemNBTHelper.setCompound(stack, TAG_WARP_PREFIX + warp, cmp);
	}

	public static MultiversePosition getWarpPoint(ItemStack stack, int warp) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_WARP_PREFIX + warp, true);
		if(cmp == null)
			return FALLBACK_POSITION;

		double x = cmp.getDouble(TAG_POS_X);
		double y = cmp.getDouble(TAG_POS_Y);
		double z = cmp.getDouble(TAG_POS_Z);
		int dim = cmp.getInteger(TAG_DIMENSION);
		return new MultiversePosition(x, y, z, dim);
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
		Tessellator.renderingWorldRenderer = false;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

		double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);

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
			if(segmentLookedAt == seg)
				inside = true;

			GL11.glPushMatrix();
			GL11.glRotatef(rotationAngle, 0F, 1F, 0F);
			GL11.glTranslatef(s * m, -0.75F, 0F);

			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
			GL11.glScalef(0.75F, 0.75F, 0.75F);
			GL11.glTranslatef(0F, 0F, 0.5F);
			IIcon icon = signs[seg];
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glColor4f(1F, 1F, 1F, getWarpPoint(stack, seg).isValid() ? 1F : 0.2F);
			float f = icon.getMinU();
			float f1 = icon.getMaxU();
			float f2 = icon.getMinV();
			float f3 = icon.getMaxV();
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);

			GL11.glColor3f(1F, 1F, 1F);
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
				float ang = i + seg * segAngles + shift;
				double xp = Math.cos(ang * Math.PI / 180F) * s;
				double zp = Math.sin(ang * Math.PI / 180F) * s;

				tess.addVertexWithUV(xp * m, y, zp * m, u, v);
				tess.addVertexWithUV(xp, y0, zp, u, 0);

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

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		Minecraft.getMinecraft();
		int slot = getSegmentLookedAt(stack, player);
		MultiversePosition pos = getWarpPoint(stack, slot);

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		String s = StatCollector.translateToLocal("botania.sign" + slot);
		font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 55, 0xFFD409);

		if(pos.isValid()) {
			int dist = (int) vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(pos.x, pos.y, pos.z, player.posX, player.posY - 1.6, player.posZ);
			int xpos = pos.x;
			int ypos = pos.y;
			int zpos = pos.z;
			
			x = String.format(StatCollector.translateToLocal("botaniamisc.cords"), xpos);
			y = String.format(StatCollector.translateToLocal("botaniamisc.cords"), ypos);
			z = String.format(StatCollector.translateToLocal("botaniamisc.cords"), zpos);
			font.drawStringWithShadow(x, y, z, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 40, 0x4C4C7F);
			s = dist == 1 ? StatCollector.translateToLocal("botaniamisc.blockAway") : String.format(StatCollector.translateToLocal("botaniamisc.blocksAway"), dist);
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 30, 0x9999FF);
			s = StatCollector.translateToLocal("botaniamisc.clickToTeleport");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 20, 0xFFFFFF);
			s = StatCollector.translateToLocal("botaniamisc.clickToRemoveWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 10, 0xFFFFFF);
		} else {
			s = StatCollector.translateToLocal("botaniamisc.unboundWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 40, 0xFFFFFF);
			s = StatCollector.translateToLocal("botaniamisc.clickToAddWarp");
			font.drawStringWithShadow(s, resolution.getScaledWidth() / 2 - font.getStringWidth(s) / 2, resolution.getScaledHeight() / 2 - 30, 0xFFFFFF);
		}
	}

	private static class MultiversePosition {

		public final double x, y, z;
		public final int dim;

		public MultiversePosition(double x, double y, double z, int dim) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.dim = dim;
		}

		boolean isValid() {
			return y > 0;
		}

	}

}
