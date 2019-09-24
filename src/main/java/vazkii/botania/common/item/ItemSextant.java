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

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class ItemSextant extends ItemMod {
	private static final int MAX_RADIUS = 256;
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";

	public ItemSextant(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack par1ItemStack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		if(getUseDuration(stack) - count < 10
				|| !(living instanceof PlayerEntity))
			return;

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if(y != -1) {
			Vector3 source = new Vector3(x, y, z);

			double radius = calculateRadius(stack, (PlayerEntity) living);

			if(count % 10 == 0) {
				WispParticleData data = WispParticleData.wisp(0.3F, 0F, 1F, 1F, 1);
				for (int i = 0; i < 360; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double zp = z + Math.sin(radian) * radius;
					living.world.addParticle(data, xp + 0.5, source.y + 1, zp + 0.5, 0, - -0.01F, 0);
				}
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int time) {
		if(!(living instanceof PlayerEntity)) return;

		double radius = calculateRadius(stack, (PlayerEntity) living);
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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		Botania.proxy.removeSextantMultiblock();

		ItemStack stack = player.getHeldItem(hand);
		if(!player.isSneaking()) {
			RayTraceResult rtr = ToolCommons.raytraceFromEntity(world, player, RayTraceContext.FluidMode.NONE, 128);
			if(rtr.getType() == RayTraceResult.Type.BLOCK) {
				if(!world.isRemote) {
					BlockPos pos = ((BlockRayTraceResult) rtr).getPos();
					ItemNBTHelper.setInt(stack, TAG_SOURCE_X, pos.getX());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, pos.getY());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Z, pos.getZ());
				}
			} else ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, -1);

			player.setActiveHand(hand);
		}

		return ActionResult.newResult(ActionResultType.SUCCESS, stack);
	}

	private static double calculateRadius(ItemStack stack, PlayerEntity player) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		Vector3 source = new Vector3(x, y, z);
		WispParticleData data = WispParticleData.wisp(0.2F, 1F, 0F, 0F, 1);
		player.world.addParticle(data, source.x + 0.5, source.y + 1, source.z + 0.5, 0, - -0.1F, 0);

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

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(PlayerEntity player, ItemStack stack) {
		ItemStack onUse = player.getActiveItemStack();
		int time = player.getItemInUseCount();

		if(onUse == stack && stack.getItem().getUseDuration(stack) - time >= 10) {
			double radius = calculateRadius(stack, player);
			FontRenderer font = Minecraft.getInstance().fontRenderer;
			int x = Minecraft.getInstance().mainWindow.getScaledWidth() / 2 + 30;
			int y = Minecraft.getInstance().mainWindow.getScaledHeight() / 2;

			String s = Integer.toString((int) radius);
			boolean inRange = 0 < radius && radius <= MAX_RADIUS;
			if (!inRange)
				s = TextFormatting.RED + s;

			font.drawStringWithShadow(s, x - font.getStringWidth(s) / 2, y - 4, 0xFFFFFF);

			if(inRange) {
				radius += 4;
				GlStateManager.disableTexture();
				GL11.glLineWidth(3F);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GlStateManager.color4f(0F, 1F, 1F, 1F);
				for(int i = 0; i < 361; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double yp = y + Math.sin(radian) * radius;
					GL11.glVertex2d(xp, yp);
				}
				GL11.glEnd();
				GlStateManager.enableTexture();
			}
		}
	}

	public static class MultiblockSextant extends Multiblock {

		@Override
		public Map<Direction, Multiblock> createRotations() {
			Map<Direction, Multiblock> ret = new EnumMap<>(Direction.class);
			for (Direction e : Direction.values()) {
				ret.put(e, this);
			}
			return ret;
		}

	}

}
