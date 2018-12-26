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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class ItemSextant extends ItemMod {
	private static final int MAX_RADIUS = 256;
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";

	public ItemSextant() {
		super(LibItemNames.SEXTANT);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		if(getMaxItemUseDuration(stack) - count < 10
				|| !(living instanceof EntityPlayer))
			return;

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if(y != -1) {
			Vector3 source = new Vector3(x, y, z);

			double radius = calculateRadius(stack, (EntityPlayer) living);

			if(count % 10 == 0)
				for(int i = 0; i < 360; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double zp = z + Math.sin(radian) * radius;
					Botania.proxy.wispFX(xp + 0.5, source.y + 1, zp + 0.5, 0F, 1F, 1F, 0.3F, -0.01F);
				}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int time) {
		if(!(living instanceof EntityPlayer)) return;

		double radius = calculateRadius(stack, (EntityPlayer) living);
		if(1 < radius && radius <= MAX_RADIUS) {
			int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
			int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
			if(y != -1)
				Botania.proxy.setMultiblock(world, x, y, z, radius, Blocks.COBBLESTONE);
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		Botania.proxy.removeSextantMultiblock();

		ItemStack stack = player.getHeldItem(hand);
		if(!player.isSneaking()) {
			RayTraceResult pos = ToolCommons.raytraceFromEntity(world, player, false, 128);
			if(pos != null && pos.entityHit == null && pos.getBlockPos() != null) {
				if(!world.isRemote) {
					ItemNBTHelper.setInt(stack, TAG_SOURCE_X, pos.getBlockPos().getX());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, pos.getBlockPos().getY());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Z, pos.getBlockPos().getZ());
				}
			} else ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, -1);

			player.setActiveHand(hand);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	public static double calculateRadius(ItemStack stack, EntityPlayer player) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		Vector3 source = new Vector3(x, y, z);
		Botania.proxy.wispFX(source.x + 0.5, source.y + 1, source.z + 0.5, 1F, 0F, 0F, 0.2F, -0.1F);

		Vector3 centerVec = Vector3.fromEntityCenter(player);
		Vector3 diffVec = source.subtract(centerVec);
		Vector3 lookVec = new Vector3(player.getLookVec());
		double mul = diffVec.y / lookVec.y;
		lookVec = lookVec.multiply(mul).add(centerVec);

		lookVec = new Vector3(net.minecraft.util.math.MathHelper.floor(lookVec.x),
				lookVec.y,
				net.minecraft.util.math.MathHelper.floor(lookVec.z));

		return MathHelper.pointDistancePlane(source.x, source.z, lookVec.x, lookVec.z);
	}

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack) {
		ItemStack onUse = player.getActiveItemStack();
		int time = player.getItemInUseCount();

		if(onUse == stack && stack.getItem().getMaxItemUseDuration(stack) - time >= 10) {
			double radius = calculateRadius(stack, player);
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			int x = resolution.getScaledWidth() / 2 + 30;
			int y = resolution.getScaledHeight() / 2;

			String s = Integer.toString((int) radius);
			boolean inRange = 0 < radius && radius <= MAX_RADIUS;
			if (!inRange)
				s = TextFormatting.RED + s;

			font.drawStringWithShadow(s, x - font.getStringWidth(s) / 2, y - 4, 0xFFFFFF);

			if(inRange) {
				radius += 4;
				GlStateManager.disableTexture2D();
				GL11.glLineWidth(3F);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GlStateManager.color(0F, 1F, 1F, 1F);
				for(int i = 0; i < 361; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double yp = y + Math.sin(radian) * radius;
					GL11.glVertex2d(xp, yp);
				}
				GL11.glEnd();
				GlStateManager.enableTexture2D();
			}
		}
	}

	public static class MultiblockSextant extends Multiblock {

		@Override
		public Map<EnumFacing, Multiblock> createRotations() {
			Map<EnumFacing, Multiblock> ret = new EnumMap<>(EnumFacing.class);
			for (EnumFacing e : EnumFacing.VALUES) {
				ret.put(e, this);
			}
			return ret;
		}

	}

}
