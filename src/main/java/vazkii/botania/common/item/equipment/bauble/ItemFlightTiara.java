/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.StringObfuscator;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemFlightTiara extends ItemBauble implements IManaUsingItem {

	private static final ResourceLocation textureHud = new ResourceLocation(LibResources.GUI_HUD_ICONS);
	public static final ResourceLocation textureHalo = new ResourceLocation(LibResources.MISC_HALO);

	private static final String TAG_VARIANT = "variant";
	private static final String TAG_FLYING = "flying";
	private static final String TAG_GLIDING = "gliding";
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_INFINITE_FLIGHT = "infiniteFlight";
	private static final String TAG_DASH_COOLDOWN = "dashCooldown";
	private static final String TAG_IS_SPRINTING = "isSprinting";
	private static final String TAG_BOOST_PENDING = "boostPending";

	private static final List<String> playersWithFlight = Collections.synchronizedList(new ArrayList<>());
	private static final int COST = 35;
	private static final int COST_OVERKILL = COST * 3;
	private static final int MAX_FLY_TIME = 1200;

	private static final int SUBTYPES = 8;
	public static final int WING_TYPES = 9;

	private static final String SUPER_AWESOME_HASH = "4D0F274C5E3001C95640B5E88A821422C8B1E132264492C043A3D746B705C025";

	public ItemFlightTiara(Properties props) {
		super(props);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			for (int i = 0; i < SUBTYPES + 1; i++) {
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setInt(stack, TAG_VARIANT, i);
				list.add(stack);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent("botania.wings" + getVariant(stack)));
	}

	public static void updatePlayerFlyStatus(Player player) {
		ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
		int left = ItemNBTHelper.getInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME);

		if (playersWithFlight.contains(playerStr(player))) {
			if (shouldPlayerHaveFlight(player)) {
				player.abilities.mayfly = true;
				if (player.abilities.flying) {
					if (!player.level.isClientSide) {
						if (!player.isCreative() && !player.isSpectator()) {
							ManaItemHandler.instance().requestManaExact(tiara, player, getCost(tiara, left), true);
						}
					} else if (Math.abs(player.getDeltaMovement().x()) > 0.1 || Math.abs(player.getDeltaMovement().z()) > 0.1) {
						double x = player.getX() - 0.5;
						double y = player.getY() - 0.5;
						double z = player.getZ() - 0.5;

						float r = 1F;
						float g = 1F;
						float b = 1F;

						int variant = getVariant(tiara);
						switch (variant) {
						case 2: {
							r = 0.1F;
							g = 0.1F;
							b = 0.1F;
							break;
						}
						case 3: {
							r = 0F;
							g = 0.6F;
							break;
						}
						case 4: {
							g = 0.3F;
							b = 0.3F;
							break;
						}
						case 5: {
							r = 0.6F;
							g = 0F;
							b = 0.6F;
							break;
						}
						case 6: {
							r = 0.4F;
							g = 0F;
							b = 0F;
							break;
						}
						case 7: {
							r = 0.2F;
							g = 0.6F;
							b = 0.2F;
							break;
						}
						case 8: {
							r = 0.85F;
							g = 0.85F;
							b = 0F;
							break;
						}
						case 9: {
							r = 0F;
							b = 0F;
							break;
						}
						}

						for (int i = 0; i < 2; i++) {
							SparkleParticleData data = SparkleParticleData.sparkle(2F * (float) Math.random(), r, g, b, 20);
							player.level.addParticle(data, x + Math.random() * player.getBbWidth(), y + Math.random() * 0.4, z + Math.random() * player.getBbWidth(), 0, 0, 0);
						}
					}
				}
			} else {
				if (!player.isSpectator() && !player.abilities.instabuild) {
					player.abilities.mayfly = false;
					player.abilities.flying = false;
					player.abilities.invulnerable = false;
				}
				playersWithFlight.remove(playerStr(player));
			}
		} else if (shouldPlayerHaveFlight(player)) {
			playersWithFlight.add(playerStr(player));
			player.abilities.mayfly = true;
		}
	}

	public static void playerLoggedOut(ServerGamePacketListenerImpl handler, MinecraftServer server) {
		String username = handler.player.getGameProfile().getName();
		playersWithFlight.remove(username + ":false");
		playersWithFlight.remove(username + ":true");
	}

	private static String playerStr(Player player) {
		return player.getGameProfile().getName() + ":" + player.level.isClientSide;
	}

	private static boolean shouldPlayerHaveFlight(Player player) {
		ItemStack armor = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
		if (!armor.isEmpty()) {
			int left = ItemNBTHelper.getInt(armor, TAG_TIME_LEFT, MAX_FLY_TIME);
			boolean flying = ItemNBTHelper.getBoolean(armor, TAG_FLYING, false);
			return (left > (flying ? 0 : MAX_FLY_TIME / 10) || player.inventory.contains(new ItemStack(ModItems.flugelEye))) && ManaItemHandler.instance().requestManaExact(armor, player, getCost(armor, left), false);
		}

		return false;
	}

	public static int getCost(ItemStack stack, int timeLeft) {
		return timeLeft <= 0 ? COST_OVERKILL : COST;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		super.onEquipped(stack, living);
		int variant = getVariant(stack);
		if (variant != WING_TYPES && StringObfuscator.matchesHash(stack.getHoverName().getString(), SUPER_AWESOME_HASH)) {
			ItemNBTHelper.setInt(stack, TAG_VARIANT, WING_TYPES);
			stack.resetHoverName();
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof Player) {
			Player p = (Player) player;
			boolean flying = p.abilities.flying;

			boolean wasSprting = ItemNBTHelper.getBoolean(stack, TAG_IS_SPRINTING, false);
			boolean isSprinting = p.isSprinting();
			if (isSprinting != wasSprting) {
				ItemNBTHelper.setBoolean(stack, TAG_IS_SPRINTING, isSprinting);
			}

			int time = ItemNBTHelper.getInt(stack, TAG_TIME_LEFT, MAX_FLY_TIME);
			int newTime = time;
			Vector3 look = new Vector3(p.getLookAngle()).multiply(1, 0, 1).normalize();

			if (flying) {
				if (time > 0 && !p.isSpectator() && !p.isCreative()
						&& !ItemNBTHelper.getBoolean(stack, TAG_INFINITE_FLIGHT, false)) {
					newTime--;
				}
				final int maxCd = 80;
				int cooldown = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
				if (!wasSprting && isSprinting && cooldown == 0) {
					p.setDeltaMovement(p.getDeltaMovement().add(look.x, 0, look.z));
					p.level.playSound(null, p.getX(), p.getY(), p.getZ(), ModSounds.dash, SoundSource.PLAYERS, 1F, 1F);
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, maxCd);
					ItemNBTHelper.setBoolean(stack, TAG_BOOST_PENDING, true);
				} else if (cooldown > 0) {
					if (ItemNBTHelper.getBoolean(stack, TAG_BOOST_PENDING, false)) {
						player.moveRelative(5F, new Vec3(0F, 0F, 1F));
						ItemNBTHelper.removeEntry(stack, TAG_BOOST_PENDING);
					}
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, cooldown - 2);
				}
			} else {
				boolean wasGliding = ItemNBTHelper.getBoolean(stack, TAG_GLIDING, false);
				boolean doGlide = player.isShiftKeyDown() && !player.isOnGround() && (player.getDeltaMovement().y() < -.7F || wasGliding);
				if (time < MAX_FLY_TIME && player.tickCount % (doGlide ? 6 : 2) == 0) {
					newTime++;
				}

				if (doGlide) {
					float mul = 0.6F;
					player.setDeltaMovement(look.x * mul, Math.max(-0.15F, player.getDeltaMovement().y()), look.z * mul);
					player.fallDistance = 2F;
				}
				ItemNBTHelper.setBoolean(stack, TAG_GLIDING, doGlide);
			}

			ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
			if (newTime != time) {
				ItemNBTHelper.setInt(stack, TAG_TIME_LEFT, newTime);
			}
		}
	}

	@Override
	public boolean hasRender(ItemStack stack, LivingEntity living) {
		return super.hasRender(stack, living) && living instanceof Player;
	}

	/*
		NB: All of the following methods are somewhat similar, but they are split apart to isolate the logic.
		Trying too hard to factor things out of each case led to very spaghetti-looking code.
		As such, only Jibril's is commented, the rest are variations on the same theme
	*/

	@Environment(EnvType.CLIENT)
	private static void renderBasic(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
		ms.pushPose();

		// attach to body
		bipedModel.body.translateAndRotate(ms);

		// position on body
		ms.translate(0, 0.5, 0.2);

		for (int i = 0; i < 2; i++) {
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(i == 0 ? flap : 180 - flap));

			// move so flapping about the edge instead of center of texture
			ms.translate(-1, 0, 0);

			// rotate since the textures are stored rotated
			ms.mulPose(Vector3f.ZP.rotationDegrees(-60));
			ms.scale(1.5F, -1.5F, -1.5F);
			Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		ms.popPose();
	}

	@Environment(EnvType.CLIENT)
	private static void renderSephiroth(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
		ms.pushPose();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(0, 0.5, 0.2);

		ms.mulPose(Vector3f.YP.rotationDegrees(flap));
		ms.translate(-1.1, 0, 0);

		ms.mulPose(Vector3f.ZP.rotationDegrees(-60));
		ms.scale(1.6F, -1.6F, -1.6F);
		Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
		ms.popPose();
	}

	@Environment(EnvType.CLIENT)
	private static void renderCirno(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light) {
		ms.pushPose();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(-0.8, 0.15, 0.25);

		for (int i = 0; i < 2; i++) {
			ms.pushPose();

			if (i == 1) {
				ms.mulPose(Vector3f.YP.rotationDegrees(180));
				ms.translate(-1.6, 0, 0);
			}

			ms.scale(1.6F, -1.6F, -1.6F);
			Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		ms.popPose();
	}

	@Environment(EnvType.CLIENT)
	private static void renderPhoenix(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
		ms.pushPose();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(0, -0.2, 0.2);

		for (int i = 0; i < 2; i++) {
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(i == 0 ? flap : 180 - flap));

			ms.translate(-0.9, 0, 0);

			ms.scale(1.7F, -1.7F, -1.7F);
			Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		ms.popPose();
	}

	@Environment(EnvType.CLIENT)
	private static void renderKuroyukihime(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
		ms.pushPose();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(0, -0.4, 0.2);

		for (int i = 0; i < 2; i++) {
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(i == 0 ? flap : 180 - flap));

			ms.translate(-1.3, 0, 0);

			ms.scale(2.5F, -2.5F, -2.5F);
			Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		ms.popPose();
	}

	@Environment(EnvType.CLIENT)
	private static void renderCustomColor(HumanoidModel<?> bipedModel, BakedModel model, LivingEntity living, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap, int color) {
		ms.pushPose();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(0, 0, 0.2);

		for (int i = 0; i < 2; i++) {
			ms.pushPose();
			ms.mulPose(Vector3f.YP.rotationDegrees(i == 0 ? flap : 180 - flap));
			ms.translate(-0.7, 0, 0);

			ms.scale(1.5F, -1.5F, -1.5F);

			RenderHelper.renderItemCustomColor(living, stack, color, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		ms.popPose();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		int meta = getVariant(stack);
		if (meta <= 0 || meta >= MiscellaneousIcons.INSTANCE.tiaraWingIcons.length + 1) {
			return;
		}

		BakedModel model = MiscellaneousIcons.INSTANCE.tiaraWingIcons[meta - 1];
		boolean flying = living instanceof Player && ((Player) living).abilities.flying;
		float flap = 20F + (float) ((Math.sin((double) (living.tickCount + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));

		switch (meta) {
		case 1:
			renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
			ms.pushPose();
			renderHalo(bipedModel, living, ms, buffers, partialTicks);
			ms.popPose();
			break;
		case 2:
			renderSephiroth(bipedModel, model, stack, ms, buffers, light, flap);
			break;
		case 3:
			renderCirno(bipedModel, model, stack, ms, buffers, light);
			break;
		case 4:
			renderPhoenix(bipedModel, model, stack, ms, buffers, flap);
			break;
		case 5:
			renderKuroyukihime(bipedModel, model, stack, ms, buffers, flap);
			break;
		case 6:
		case 8:
			renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
			break;
		case 7:
			float alpha = 0.5F + (float) Math.cos((double) (living.tickCount + partialTicks) * 0.3F) * 0.2F;
			int color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
			renderCustomColor(bipedModel, model, living, stack, ms, buffers, flap, color);
			break;
		case 9:
			flap = -(float) ((Math.sin((double) (living.tickCount + partialTicks) * 0.2F) + 0.6F) * (flying ? 12F : 5F));
			alpha = 0.5F + (flying ? (float) Math.cos((double) (living.tickCount + partialTicks) * 0.3F) * 0.25F + 0.25F : 0F);
			color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
			renderCustomColor(bipedModel, model, living, stack, ms, buffers, flap, color);
			break;
		}
	}

	@Environment(EnvType.CLIENT)
	public static void renderHalo(@Nullable HumanoidModel<?> model, @Nullable LivingEntity player, PoseStack ms, MultiBufferSource buffers, float partialTicks) {
		if (model != null) {
			model.body.translateAndRotate(ms);
		}

		ms.translate(0.2, -0.65, 0);
		ms.mulPose(Vector3f.ZP.rotationDegrees(30));

		if (player != null) {
			ms.mulPose(Vector3f.YP.rotationDegrees(player.tickCount + partialTicks));
		} else {
			ms.mulPose(Vector3f.YP.rotationDegrees(ClientTickHandler.ticksInGame));
		}

		ms.scale(0.75F, -0.75F, -0.75F);
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.HALO);
		Matrix4f mat = ms.last().pose();
		buffer.vertex(mat, -1F, 0, -1F).uv(0, 0).endVertex();
		buffer.vertex(mat, 1F, 0, -1F).uv(1, 0).endVertex();
		buffer.vertex(mat, 1F, 0, 1F).uv(1, 1).endVertex();
		buffer.vertex(mat, -1F, 0, 1F).uv(0, 1).endVertex();
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(PoseStack ms, Player player, ItemStack stack) {
		int u = Math.max(1, getVariant(stack)) * 9 - 9;
		int v = 0;

		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bind(textureHud);
		int xo = mc.getWindow().getGuiScaledWidth() / 2 + 10;
		int x = xo;
		int y = mc.getWindow().getGuiScaledHeight() - 49;
		if (player.isEyeInFluid(FluidTags.WATER)) {
			y -= 10;
		}

		int left = ItemNBTHelper.getInt(stack, TAG_TIME_LEFT, MAX_FLY_TIME);

		int segTime = MAX_FLY_TIME / 10;
		int segs = left / segTime + 1;
		int last = left % segTime;

		for (int i = 0; i < segs; i++) {
			float trans = 1F;
			if (i == segs - 1) {
				trans = (float) last / (float) segTime;
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				RenderSystem.disableAlphaTest();
			}

			RenderSystem.color4f(1F, 1F, 1F, trans);
			RenderHelper.drawTexturedModalRect(ms, x, y, u, v, 9, 9);
			x += 8;
		}

		if (player.abilities.flying) {
			int width = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			if (width > 0) {
				GuiComponent.fill(ms, xo, y - 2, xo + 80, y - 1, 0x88000000);
			}
			GuiComponent.fill(ms, xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
		}

		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		mc.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
	}

	public static int getVariant(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_VARIANT, 0);
	}
}
