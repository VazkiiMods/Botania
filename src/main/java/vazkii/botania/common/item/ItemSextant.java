/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [01/11/2015, 18:25:54 (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSextant extends ItemMod {

	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";

	public ItemSextant() {
		setUnlocalizedName(LibItemNames.SEXTANT);
		setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(getMaxItemUseDuration(stack) - count < 10)
			return;

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if(y != -1) {
			World world = player.worldObj;
			Vector3 source = new Vector3(x, y, z);

			double radius = calculateRadius(stack, player);

			if(count % 10 == 0)
				for(int i = 0; i < 360; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double zp = z + Math.sin(radian) * radius;
					Botania.proxy.wispFX(world, xp + 0.5, source.y + 1, zp + 0.5, 0F, 1F, 1F, 0.3F, -0.01F);
				}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time) {
		double radius = calculateRadius(stack, player);
		if(radius > 1) {
			int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
			int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
			if(y != -1)
				Botania.proxy.setMultiblock(world, x, y, z, radius, Blocks.cobblestone);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		Botania.proxy.removeSextantMultiblock();

		if(!par3EntityPlayer.isSneaking()) {
			MovingObjectPosition pos = ToolCommons.raytraceFromEntity(par2World, par3EntityPlayer, false, 128);
			if(pos != null && pos.entityHit == null) {
				if(!par2World.isRemote) {
					ItemNBTHelper.setInt(par1ItemStack, TAG_SOURCE_X, pos.blockX);
					ItemNBTHelper.setInt(par1ItemStack, TAG_SOURCE_Y, pos.blockY);
					ItemNBTHelper.setInt(par1ItemStack, TAG_SOURCE_Z, pos.blockZ);
				}
			} else ItemNBTHelper.setInt(par1ItemStack, TAG_SOURCE_Y, -1);

			par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		}

		return par1ItemStack;
	}

	public static double calculateRadius(ItemStack stack, EntityPlayer player) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		World world = player.worldObj;
		Vector3 source = new Vector3(x, y, z);
		Botania.proxy.wispFX(world, source.x + 0.5, source.y + 1, source.z + 0.5, 1F, 0F, 0F, 0.2F, -0.1F);

		Vector3 centerVec = Vector3.fromEntityCenter(player);
		Vector3 diffVec = source.copy().subtract(centerVec);
		Vector3 lookVec = new Vector3(player.getLookVec());
		double mul = diffVec.y / lookVec.y;
		lookVec.multiply(mul).add(centerVec);

		lookVec.x = net.minecraft.util.MathHelper.floor_double(lookVec.x);
		lookVec.z = net.minecraft.util.MathHelper.floor_double(lookVec.z);

		return MathHelper.pointDistancePlane(source.x, source.z, lookVec.x, lookVec.z);
	}

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		ItemStack onUse = player.getItemInUse();
		int time = player.getItemInUseCount();

		if(onUse == stack && stack.getItem().getMaxItemUseDuration(stack) - time >= 10) {
			double radius = calculateRadius(stack, player);
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			int x = resolution.getScaledWidth() / 2 + 30;
			int y = resolution.getScaledHeight() / 2;

			String s = "" + (int) radius;
			font.drawStringWithShadow(s, x - font.getStringWidth(s) / 2, y - 4, 0xFFFFFF);

			if(radius > 0) {
				radius += 4;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glLineWidth(3F);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glColor4f(0F, 1F, 1F, 1F);
				for(int i = 0; i < 361; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double yp = y + Math.sin(radian) * radius;
					GL11.glVertex2d(xp, yp);
				}
				GL11.glEnd();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		}
	}

	public static class MultiblockSextant extends Multiblock {

		@Override
		public Multiblock[] createRotations() {
			return new Multiblock[] { this };
		}

	}

}
