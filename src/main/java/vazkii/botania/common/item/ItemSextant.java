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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemSextant extends Item {
	public static final Identifier MULTIBLOCK_ID = prefix("sextant");
	private static final int MAX_RADIUS = 256;
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";

	public ItemSextant(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public void usageTick(World world, LivingEntity living, ItemStack stack, int count) {
		if (getMaxUseTime(stack) - count < 10
				|| !(living instanceof PlayerEntity)
				|| world.isClient) {
			return;
		}

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if (y != -1) {
			Vector3 source = new Vector3(x, y, z);

			double radius = calculateRadius(stack, living);

			if (count % 10 == 0) {
				WispParticleData data = WispParticleData.wisp(0.3F, 0F, 1F, 1F, 1);
				for (int i = 0; i < 360; i++) {
					float radian = (float) (i * Math.PI / 180);
					double xp = x + Math.cos(radian) * radius;
					double zp = z + Math.sin(radian) * radius;
					world.addParticle(data, xp + 0.5, source.y + 1, zp + 0.5, 0, - -0.01F, 0);
				}
			}
		}
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity living, int time) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}

		double radius = calculateRadius(stack, living);
		if (1 < radius && radius <= MAX_RADIUS) {
			IStateMatcher matcher = PatchouliAPI.instance.predicateMatcher(Blocks.COBBLESTONE, s -> !s.isAir());
			int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
			int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
			int iradius = (int) radius + 1;
			if (y != -1) {
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
				IMultiblock sparse = PatchouliAPI.instance.makeSparseMultiblock(map).setId(MULTIBLOCK_ID);
				Botania.proxy.showMultiblock(sparse, new LiteralText("r = " + (int) radius), new BlockPos(x, y, z), BlockRotation.NONE);
			}
		}
	}

	private void reset(World world, ItemStack stack) {
		ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, -1);
		if (world.isClient) {
			Botania.proxy.clearSextantMultiblock();
		}
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!player.isSneaking()) {
			BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 128, false);
			if (rtr.getType() == HitResult.Type.BLOCK) {
				if (!world.isClient) {
					BlockPos pos = rtr.getBlockPos();
					ItemNBTHelper.setInt(stack, TAG_SOURCE_X, pos.getX());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, pos.getY());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Z, pos.getZ());
				}
				player.setCurrentHand(hand);
			}
		} else {
			reset(world, stack);
		}

		return TypedActionResult.success(stack);
	}

	private static double calculateRadius(ItemStack stack, LivingEntity living) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		Vector3 source = new Vector3(x, y, z);
		WispParticleData data = WispParticleData.wisp(0.2F, 1F, 0F, 0F, 1);
		living.world.addParticle(data, source.x + 0.5, source.y + 1, source.z + 0.5, 0, - -0.1F, 0);

		Vector3 centerVec = Vector3.fromEntityCenter(living);
		Vector3 diffVec = source.subtract(centerVec);
		Vector3 lookVec = new Vector3(living.getRotationVector());
		double mul = diffVec.y / lookVec.y;
		lookVec = lookVec.multiply(mul).add(centerVec);

		lookVec = new Vector3(net.minecraft.util.math.MathHelper.floor(lookVec.x),
				lookVec.y,
				net.minecraft.util.math.MathHelper.floor(lookVec.z));

		return MathHelper.pointDistancePlane(source.x, source.z, lookVec.x, lookVec.z);
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player, ItemStack stack) {
		ItemStack onUse = player.getActiveItem();
		int time = player.getItemUseTimeLeft();

		if (onUse == stack && stack.getItem().getMaxUseTime(stack) - time >= 10) {
			double radius = calculateRadius(stack, player);
			TextRenderer font = MinecraftClient.getInstance().textRenderer;
			int x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 + 30;
			int y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;

			String s = Integer.toString((int) radius);
			boolean inRange = 0 < radius && radius <= MAX_RADIUS;
			if (!inRange) {
				s = Formatting.RED + s;
			}

			font.drawWithShadow(ms, s, x - font.getWidth(s) / 2, y - 4, 0xFFFFFF);

			if (inRange) {
				radius += 4;
				RenderSystem.disableTexture();
				RenderSystem.lineWidth(3F);
				Tessellator.getInstance().getBuffer().begin(GL11.GL_LINE_STRIP, VertexFormats.POSITION);
				RenderSystem.color4f(0F, 1F, 1F, 1F);
				for (int i = 0; i < 361; i++) {
					float radian = (float) (i * Math.PI / 180);
					float xp = x + net.minecraft.util.math.MathHelper.cos(radian) * (float) radius;
					float yp = y + net.minecraft.util.math.MathHelper.sin(radian) * (float) radius;
					Tessellator.getInstance().getBuffer().vertex(ms.peek().getModel(), xp, yp, 0).next();
				}
				Tessellator.getInstance().draw();
				RenderSystem.enableTexture();
			}
		}
	}

}
