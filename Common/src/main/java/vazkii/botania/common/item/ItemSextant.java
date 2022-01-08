/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.VecHelper;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemSextant extends Item {
	public static final ResourceLocation MULTIBLOCK_ID = prefix("sextant");
	private static final int MAX_RADIUS = 256;
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";

	public ItemSextant(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int count) {
		if (getUseDuration(stack) - count < 10
				|| !(living instanceof Player)
				|| world.isClientSide) {
			return;
		}

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if (y != Integer.MIN_VALUE) {
			double radius = calculateRadius(stack, living);

			if (count % 10 == 0) {
				WispParticleData data = WispParticleData.wisp(0.3F, 0F, 1F, 1F, 1);
				for (int i = 0; i < 360; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double zp = z + Math.sin(radian) * radius;
					world.addParticle(data, xp + 0.5, y + 1, zp + 0.5, 0, - -0.01F, 0);
				}
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int time) {
		if (!(living instanceof Player)) {
			return;
		}

		double radius = calculateRadius(stack, living);
		if (1 < radius && radius <= MAX_RADIUS) {
			IStateMatcher matcher = PatchouliAPI.get().predicateMatcher(Blocks.COBBLESTONE, s -> !s.isAir());
			int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
			int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
			int iradius = (int) radius + 1;
			if (y != Integer.MIN_VALUE) {
				Map<BlockPos, IStateMatcher> map = new HashMap<>();
				for (int i = 0; i < iradius * 2 + 1; i++) {
					for (int j = 0; j < iradius * 2 + 1; j++) {
						int xp = x + i - iradius;
						int zp = z + j - iradius;

						if ((int) Math.floor(MathHelper.pointDistancePlane(xp, zp, x, z)) == iradius - 1) {
							map.put(new BlockPos(xp - x, 0, zp - z), matcher);
						}
					}
				}
				IMultiblock sparse = PatchouliAPI.get().makeSparseMultiblock(map).setId(MULTIBLOCK_ID);
				IProxy.INSTANCE.showMultiblock(sparse, new TextComponent("r = " + (int) radius), new BlockPos(x, y, z), Rotation.NONE);
			}
		}
	}

	private void reset(Level world, ItemStack stack) {
		ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		if (world.isClientSide) {
			IProxy.INSTANCE.clearSextantMultiblock();
		}
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.isShiftKeyDown()) {
			BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 128, false);
			if (rtr.getType() == HitResult.Type.BLOCK) {
				if (!world.isClientSide) {
					BlockPos pos = rtr.getBlockPos();
					ItemNBTHelper.setInt(stack, TAG_SOURCE_X, pos.getX());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, pos.getY());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Z, pos.getZ());
				}
				player.startUsingItem(hand);
			}
		} else {
			reset(world, stack);
		}

		return InteractionResultHolder.success(stack);
	}

	private static double calculateRadius(ItemStack stack, LivingEntity living) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		Vec3 source = new Vec3(x, y, z);
		WispParticleData data = WispParticleData.wisp(0.2F, 1F, 0F, 0F, 1);
		living.level.addParticle(data, source.x + 0.5, source.y + 1, source.z + 0.5, 0, - -0.1F, 0);

		Vec3 centerVec = VecHelper.fromEntityCenter(living);
		Vec3 diffVec = source.subtract(centerVec);
		Vec3 lookVec = living.getLookAngle();
		double mul = diffVec.y / lookVec.y;
		lookVec = lookVec.scale(mul).add(centerVec);

		lookVec = new Vec3(net.minecraft.util.Mth.floor(lookVec.x),
				lookVec.y,
				net.minecraft.util.Mth.floor(lookVec.z));

		return MathHelper.pointDistancePlane(source.x, source.z, lookVec.x, lookVec.z);
	}

	public static class Hud {
		public static void render(PoseStack ms, Player player, ItemStack stack) {
			ItemStack onUse = player.getUseItem();
			int time = player.getUseItemRemainingTicks();

			if (onUse == stack && stack.getItem().getUseDuration(stack) - time >= 10) {
				double radius = calculateRadius(stack, player);
				Font font = Minecraft.getInstance().font;
				int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 30;
				int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;

				String s = Integer.toString((int) radius);
				boolean inRange = 0 < radius && radius <= MAX_RADIUS;
				if (!inRange) {
					s = ChatFormatting.RED + s;
				}

				font.drawShadow(ms, s, x - font.width(s) / 2, y - 4, 0xFFFFFF);

				if (inRange) {
					radius += 4;
					RenderSystem.disableTexture();
					RenderSystem.lineWidth(3F);
					Tesselator.getInstance().getBuilder().begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION);
					RenderSystem.setShaderColor(0F, 1F, 1F, 1F);
					for (int i = 0; i < 361; i++) {
						float radian = (float) (i * Math.PI / 180);
						float xp = x + net.minecraft.util.Mth.cos(radian) * (float) radius;
						float yp = y + net.minecraft.util.Mth.sin(radian) * (float) radius;
						Tesselator.getInstance().getBuilder().vertex(ms.last().pose(), xp, yp, 0).endVertex();
					}
					Tesselator.getInstance().end();
					RenderSystem.enableTexture();
				}
			}
		}
	}

}
