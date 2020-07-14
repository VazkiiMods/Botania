/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
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
		MinecraftForge.EVENT_BUS.addListener(this::updatePlayerFlyStatus);
		MinecraftForge.EVENT_BUS.addListener(this::playerLoggedOut);
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			for (int i = 0; i < SUBTYPES + 1; i++) {
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setInt(stack, TAG_VARIANT, i);
				list.add(stack);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addHiddenTooltip(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent("botania.wings" + getVariant(stack)));
	}

	private void updatePlayerFlyStatus(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
			int left = ItemNBTHelper.getInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME);

			if (playersWithFlight.contains(playerStr(player))) {
				if (shouldPlayerHaveFlight(player)) {
					player.abilities.allowFlying = true;
					if (player.abilities.isFlying) {
						if (!player.world.isRemote) {
							ManaItemHandler.instance().requestManaExact(tiara, player, getCost(tiara, left), true);
						} else if (Math.abs(player.getMotion().getX()) > 0.1 || Math.abs(player.getMotion().getZ()) > 0.1) {
							double x = event.getEntityLiving().getPosX() - 0.5;
							double y = event.getEntityLiving().getPosY() - 0.5;
							double z = event.getEntityLiving().getPosZ() - 0.5;

							player.getGameProfile().getName();
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
								player.world.addParticle(data, x + Math.random() * event.getEntityLiving().getWidth(), y + Math.random() * 0.4, z + Math.random() * event.getEntityLiving().getWidth(), 0, 0, 0);
							}
						}
					}
				} else {
					if (!player.isSpectator() && !player.abilities.isCreativeMode) {
						player.abilities.allowFlying = false;
						player.abilities.isFlying = false;
						player.abilities.disableDamage = false;
					}
					playersWithFlight.remove(playerStr(player));
				}
			} else if (shouldPlayerHaveFlight(player)) {
				playersWithFlight.add(playerStr(player));
				player.abilities.allowFlying = true;
			}
		}
	}

	private void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.getPlayer().getGameProfile().getName();
		playersWithFlight.remove(username + ":false");
		playersWithFlight.remove(username + ":true");
	}

	private static String playerStr(PlayerEntity player) {
		return player.getGameProfile().getName() + ":" + player.world.isRemote;
	}

	private boolean shouldPlayerHaveFlight(PlayerEntity player) {
		ItemStack armor = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
		if (!armor.isEmpty()) {
			int left = ItemNBTHelper.getInt(armor, TAG_TIME_LEFT, MAX_FLY_TIME);
			boolean flying = ItemNBTHelper.getBoolean(armor, TAG_FLYING, false);
			return (left > (flying ? 0 : MAX_FLY_TIME / 10) || player.inventory.hasItemStack(new ItemStack(ModItems.flugelEye))) && ManaItemHandler.instance().requestManaExact(armor, player, getCost(armor, left), false);
		}

		return false;
	}

	public int getCost(ItemStack stack, int timeLeft) {
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
		if (variant != WING_TYPES && StringObfuscator.matchesHash(stack.getDisplayName().getString(), SUPER_AWESOME_HASH)) {
			ItemNBTHelper.setInt(stack, TAG_VARIANT, WING_TYPES);
			stack.clearCustomName();
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof PlayerEntity) {
			PlayerEntity p = (PlayerEntity) player;
			boolean flying = p.abilities.isFlying;

			boolean wasSprting = ItemNBTHelper.getBoolean(stack, TAG_IS_SPRINTING, false);
			boolean isSprinting = p.isSprinting();
			if (isSprinting != wasSprting) {
				ItemNBTHelper.setBoolean(stack, TAG_IS_SPRINTING, isSprinting);
			}

			int time = ItemNBTHelper.getInt(stack, TAG_TIME_LEFT, MAX_FLY_TIME);
			int newTime = time;
			Vector3 look = new Vector3(p.getLookVec()).multiply(1, 0, 1).normalize();

			if (flying) {
				if (time > 0 && !p.isSpectator() && !p.isCreative()
						&& !ItemNBTHelper.getBoolean(stack, TAG_INFINITE_FLIGHT, false)) {
					newTime--;
				}
				final int maxCd = 80;
				int cooldown = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
				if (!wasSprting && isSprinting && cooldown == 0) {
					p.setMotion(p.getMotion().add(look.x, 0, look.z));
					p.world.playSound(null, p.getPosX(), p.getPosY(), p.getPosZ(), ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, maxCd);
					ItemNBTHelper.setBoolean(stack, TAG_BOOST_PENDING, true);
				} else if (cooldown > 0) {
					if (ItemNBTHelper.getBoolean(stack, TAG_BOOST_PENDING, false)) {
						player.moveRelative(5F, new Vector3d(0F, 0F, 1F));
						ItemNBTHelper.removeEntry(stack, TAG_BOOST_PENDING);
					}
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, cooldown - 2);
				}
			} else {
				boolean wasGliding = ItemNBTHelper.getBoolean(stack, TAG_GLIDING, false);
				boolean doGlide = player.isSneaking() && !player.func_233570_aj_() && (player.getMotion().getY() < -.7F || wasGliding);
				if (time < MAX_FLY_TIME && player.ticksExisted % (doGlide ? 6 : 2) == 0) {
					newTime++;
				}

				if (doGlide) {
					float mul = 0.6F;
					player.setMotion(look.x * mul, Math.max(-0.15F, player.getMotion().getY()), look.z * mul);
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
		return super.hasRender(stack, living) && living instanceof PlayerEntity;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		int meta = getVariant(stack);
		if (meta > 0 && meta <= MiscellaneousIcons.INSTANCE.tiaraWingIcons.length) {
			IBakedModel model = MiscellaneousIcons.INSTANCE.tiaraWingIcons[meta - 1];

			boolean flying = ((PlayerEntity) player).abilities.isFlying;

			float rz = 120F;
			float rx = 20F + (float) ((Math.sin((double) (player.ticksExisted + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
			float ry = 0F;
			float h = 0.2F;
			float i = 0.15F;
			float s = 1F;
			int color = -1;
			boolean fullbright = false;

			ms.push();

			switch (meta) {
			case 1: { // Jibril
				h = 0.4F;
				break;
			}
			case 2: { // Sephiroth
				s = 1.3F;
				break;
			}
			case 3: { // Cirno
				h = -0.1F;
				rz = 0F;
				rx = 0F;
				i = 0.3F;
				break;
			}
			case 4: { // Phoenix
				rz = 180F;
				h = 0.5F;
				rx = 20F;
				ry = -(float) ((Math.sin((double) (player.ticksExisted + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.6F) * (flying ? 30F : 5F));
				fullbright = true;
				break;
			}
			case 5: { // Kuroyukihime
				h = 0.8F;
				rz = 180F;
				ry = -rx;
				rx = 0F;
				s = 2F;
				break;
			}
			case 6: { // Random Devil
				rz = 150F;
				break;
			}
			case 7: { // Lyfa
				fullbright = true;
				h = -0.1F;
				rz = 0F;
				ry = -rx;
				rx = 0F;
				float alpha = 0.5F + (float) Math.cos((double) (player.ticksExisted + partialTicks) * 0.3F) * 0.2F;
				color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
				break;
			}
			case 8: { // Mega Ultra Chicken
				h = 0.1F;
				break;
			}
			case 9: { // The One
				fullbright = true;
				rz = 180F;
				rx = 0F;
				h = 1.1F;
				ry = -(float) ((Math.sin((double) (player.ticksExisted + partialTicks) * 0.2F) + 0.6F) * (flying ? 12F : 5F));
				float alpha = 0.5F + (flying ? (float) Math.cos((double) (player.ticksExisted + partialTicks) * 0.3F) * 0.25F + 0.25F : 0F);
				color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
			}
			}

			// account for padding in the texture
			float mul = 32F / 20F;
			s *= mul;

			AccessoryRenderHelper.rotateIfSneaking(ms, player);

			ms.translate(0F, h, i);

			ms.push();
			ms.rotate(Vector3f.ZP.rotationDegrees(rz));
			ms.rotate(Vector3f.XP.rotationDegrees(rx));
			ms.rotate(Vector3f.YP.rotationDegrees(ry));
			ms.scale(s, s, s);

			RenderHelper.renderItemCustomColor(player, stack, color, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
			ms.pop();

			if (meta != 2) { // Sephiroth
				ms.scale(-1F, 1F, 1F);
				ms.push();
				ms.rotate(Vector3f.ZP.rotationDegrees(rz));
				ms.rotate(Vector3f.XP.rotationDegrees(rx));
				ms.rotate(Vector3f.YP.rotationDegrees(ry));
				ms.scale(s, s, s);
				RenderHelper.renderItemCustomColor(player, stack, color, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
				ms.pop();
			}

			ms.pop();

			if (meta == 1) {
				renderHalo(player, ms, buffers, partialTicks);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHalo(@Nullable LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, float partialTicks) {
		if (player != null) {
			AccessoryRenderHelper.translateToHeadLevel(ms, player, partialTicks);
		}
		ms.translate(0, 1.5F, 0);
		ms.rotate(new Vector3f(1, 0, -1).rotationDegrees(30));
		ms.translate(-0.1F, -0.5F, -0.1F);
		if (player != null) {
			ms.rotate(Vector3f.YP.rotationDegrees(player.ticksExisted + partialTicks));
		} else {
			ms.rotate(Vector3f.YP.rotationDegrees(Botania.proxy.getWorldElapsedTicks()));
		}

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.HALO);
		Matrix4f mat = ms.getLast().getMatrix();
		buffer.pos(mat, -0.75F, 0, -0.75F).tex(0, 0).endVertex();
		buffer.pos(mat, -0.75F, 0, 0.75F).tex(0, 1).endVertex();
		buffer.pos(mat, 0.75F, 0, 0.75F).tex(1, 1).endVertex();
		buffer.pos(mat, 0.75F, 0, -0.75F).tex(1, 0).endVertex();
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player, ItemStack stack) {
		int u = Math.max(1, getVariant(stack)) * 9 - 9;
		int v = 0;

		Minecraft mc = Minecraft.getInstance();
		mc.textureManager.bindTexture(textureHud);
		int xo = mc.getMainWindow().getScaledWidth() / 2 + 10;
		int x = xo;
		int y = mc.getMainWindow().getScaledHeight() - ForgeIngameGui.right_height;
		ForgeIngameGui.right_height += 10;

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

		if (player.abilities.isFlying) {
			int width = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			if (width > 0) {
				AbstractGui.fill(ms, xo, y - 2, xo + 80, y - 1, 0x88000000);
			}
			AbstractGui.fill(ms, xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
		}

		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		mc.textureManager.bindTexture(AbstractGui.GUI_ICONS_LOCATION);
	}

	public static int getVariant(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_VARIANT, 0);
	}
}
