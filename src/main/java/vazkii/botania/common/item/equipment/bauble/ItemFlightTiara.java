/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 26, 2014, 4:05:50 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.StringObfuscator;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemFlightTiara extends ItemBauble implements IManaUsingItem {

	private static final ResourceLocation textureHud = new ResourceLocation(LibResources.GUI_HUD_ICONS);
	private static final ResourceLocation textureHalo = new ResourceLocation(LibResources.MISC_HALO);

	private static final String TAG_VARIANT = "variant";
	private static final String TAG_FLYING = "flying";
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_INFINITE_FLIGHT = "infiniteFlight";
	private static final String TAG_DASH_COOLDOWN = "dashCooldown";
	private static final String TAG_IS_SPRINTING = "isSprinting";

	private static final List<String> playersWithFlight = Collections.synchronizedList(new ArrayList<>());
	private static final int COST = 35;
	private static final int COST_OVERKILL = COST * 3;
	private static final int MAX_FLY_TIME = 1200;

	private static final int SUBTYPES = 8;
	public static final int WING_TYPES = 9;

	private static final String SUPER_AWESOME_HASH = "4D0F274C5E3001C95640B5E88A821422C8B1E132264492C043A3D746B705C025";

	public ItemFlightTiara(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			for(int i = 0; i < SUBTYPES + 1; i++) {
				ItemStack stack = new ItemStack(this);
				ItemNBTHelper.setInt(stack, TAG_VARIANT, i);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		super.addHiddenTooltip(stack, world, stacks, flags);
		stacks.add(new TextComponentTranslation("botania.wings" + ItemNBTHelper.getInt(stack, TAG_VARIANT, 0)));
	}

	@SubscribeEvent
	public void updatePlayerFlyStatus(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
			int left = ItemNBTHelper.getInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME);

			if(playersWithFlight.contains(playerStr(player))) {
				if(shouldPlayerHaveFlight(player)) {
					player.abilities.allowFlying = true;
					if(player.abilities.isFlying) {
						if(!player.world.isRemote)
							ManaItemHandler.requestManaExact(tiara, player, getCost(tiara, left), true);
						else if(Math.abs(player.motionX) > 0.1 || Math.abs(player.motionZ) > 0.1) {
							double x = event.getEntityLiving().posX - 0.5;
							double y = event.getEntityLiving().posY - 0.5;
							double z = event.getEntityLiving().posZ - 0.5;

							player.getGameProfile().getName();
							float r = 1F;
							float g = 1F;
							float b = 1F;

							int variant = ItemNBTHelper.getInt(tiara, TAG_VARIANT, 0);
							switch(variant) {
							case 2 : {
								r = 0.1F;
								g = 0.1F;
								b = 0.1F;
								break;
							}
							case 3 : {
								r = 0F;
								g = 0.6F;
								break;
							}
							case 4 : {
								g = 0.3F;
								b = 0.3F;
								break;
							}
							case 5 : {
								r = 0.6F;
								g = 0F;
								b = 0.6F;
								break;
							}
							case 6 : {
								r = 0.4F;
								g = 0F;
								b = 0F;
								break;
							}
							case 7 : {
								r = 0.2F;
								g = 0.6F;
								b = 0.2F;
								break;
							}
							case 8 : {
								r = 0.85F;
								g = 0.85F;
								b = 0F;
								break;
							}
							case 9 : {
								r = 0F;
								b = 0F;
								break;
							}
							}

							for(int i = 0; i < 2; i++)
								Botania.proxy.sparkleFX(x + Math.random() * event.getEntityLiving().width, y + Math.random() * 0.4, z + Math.random() * event.getEntityLiving().width, r, g, b, 2F * (float) Math.random(), 20);
						}
					}
				} else {
					if(!player.isSpectator() && !player.abilities.isCreativeMode) {
						player.abilities.allowFlying = false;
						player.abilities.isFlying = false;
						player.abilities.disableDamage = false;
					}
					playersWithFlight.remove(playerStr(player));
				}
			} else if(shouldPlayerHaveFlight(player)) {
				playersWithFlight.add(playerStr(player));
				player.abilities.allowFlying = true;
			}
		}
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.getPlayer().getGameProfile().getName();
		playersWithFlight.remove(username + ":false");
		playersWithFlight.remove(username + ":true");
	}

	private static String playerStr(EntityPlayer player) {
		return player.getGameProfile().getName() + ":" + player.world.isRemote;
	}

	private boolean shouldPlayerHaveFlight(EntityPlayer player) {
	    ItemStack armor = EquipmentHandler.findOrEmpty(ModItems.flightTiara, player);
		if(!armor.isEmpty()) {
			int left = ItemNBTHelper.getInt(armor, TAG_TIME_LEFT, MAX_FLY_TIME);
			boolean flying = ItemNBTHelper.getBoolean(armor, TAG_FLYING, false);
			return (left > (flying ? 0 : MAX_FLY_TIME / 10) || player.inventory.hasItemStack(new ItemStack(ModItems.flugelEye))) && ManaItemHandler.requestManaExact(armor, player, getCost(armor, left), false);
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
	public void onEquipped(ItemStack stack, EntityLivingBase living) {
		super.onEquipped(stack, living);
		int variant = ItemNBTHelper.getInt(stack, TAG_VARIANT, 0);
		if(variant != WING_TYPES && StringObfuscator.matchesHash(stack.getDisplayName().getString(), SUPER_AWESOME_HASH)) {
			ItemNBTHelper.setInt(stack, TAG_VARIANT, WING_TYPES);
			stack.clearCustomName();
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) player;
			boolean flying = p.abilities.isFlying;

			boolean wasSprting = ItemNBTHelper.getBoolean(stack, TAG_IS_SPRINTING, false);
			boolean isSprinting = p.isSprinting();
			if(isSprinting != wasSprting)
				ItemNBTHelper.setBoolean(stack, TAG_IS_SPRINTING, isSprinting);

			int time = ItemNBTHelper.getInt(stack, TAG_TIME_LEFT, MAX_FLY_TIME);
			int newTime = time;
			Vector3 look = new Vector3(p.getLookVec()).multiply(1, 0, 1).normalize();

			if(flying) {
				if(time > 0 && !ItemNBTHelper.getBoolean(stack, TAG_INFINITE_FLIGHT, false))
					newTime--;
				final int maxCd = 80;
				int cooldown = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
				if(!wasSprting && isSprinting && cooldown == 0) {
					p.motionX += look.x;
					p.motionZ += look.z;
					p.world.playSound(null, p.posX, p.posY, p.posZ, ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, maxCd);
				} else if(cooldown > 0) {
					if(maxCd - cooldown < 2)
						player.moveRelative(0F, 0F, 1F, 5F);
					else if(maxCd - cooldown < 10)
						player.setSprinting(false);
					ItemNBTHelper.setInt(stack, TAG_DASH_COOLDOWN, cooldown - 2);
				}
			} else if(!flying) {
				boolean doGlide = player.isSneaking() && !player.onGround && player.fallDistance >= 2F;
				if(time < MAX_FLY_TIME && player.ticksExisted % (doGlide ? 6 : 2) == 0)
					newTime++;

				if(doGlide) {
					player.motionY = Math.max(-0.15F, player.motionY);
					float mul = 0.6F;
					player.motionX = look.x * mul;
					player.motionZ = look.z * mul;
					player.fallDistance = 2F;
				}
			}

			ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
			if(newTime != time)
				ItemNBTHelper.setInt(stack, TAG_TIME_LEFT, newTime);
		}
	}

	@Override
	public boolean shouldSyncToTracking(ItemStack stack, EntityLivingBase living) {
		return true;
	}

	@Override
	public boolean hasRender(ItemStack stack, EntityLivingBase living) {
		return super.hasRender(stack, living) && living instanceof EntityPlayer;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		int meta = ItemNBTHelper.getInt(stack, TAG_VARIANT, 0);
		if(meta > 0 && meta <= MiscellaneousIcons.INSTANCE.tiaraWingIcons.length) {
			TextureAtlasSprite icon = MiscellaneousIcons.INSTANCE.tiaraWingIcons[meta - 1];
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			boolean flying = ((EntityPlayer) player).abilities.isFlying;

			float rz = 120F;
			float rx = 20F + (float) ((Math.sin((double) (player.ticksExisted + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
			float ry = 0F;
			float h = 0.2F;
			float i = 0.15F;
			float s = 1F;

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color4f(1F, 1F, 1F, 1F);

			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;

			float lbx = OpenGlHelper.lastBrightnessX;
			float lby = OpenGlHelper.lastBrightnessY;

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
				OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, lightmapX, lightmapY);
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
				OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, lightmapX, lightmapY);
				h = -0.1F;
				rz = 0F;
				ry = -rx;
				rx = 0F;
				GlStateManager.color4f(1F, 1F, 1F, 0.5F + (float) Math.cos((double) (player.ticksExisted + partialTicks) * 0.3F) * 0.2F);
				break;
			}
			case 8: { // Mega Ultra Chicken
				h = 0.1F;
				break;
			}
			case 9: { // The One
				OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, lightmapX, lightmapY);
				rz = 180F;
				rx = 0F;
				h = 1.1F;
				ry = -(float) ((Math.sin((double) (player.ticksExisted + partialTicks) * 0.2F) + 0.6F) * (flying ? 12F : 5F));
				GlStateManager.color4f(1F, 1F, 1F, 0.5F + (flying ? (float) Math.cos((double) (player.ticksExisted + partialTicks) * 0.3F) * 0.25F + 0.25F : 0F));
			}
			}

			// account for padding in the texture
			float mul = 32F / 20F;
			s *= mul;

			float f = icon.getMinU();
			float f1 = icon.getMaxU();
			float f2 = icon.getMinV();
			float f3 = icon.getMaxV();
			float sr = 1F / s;

			AccessoryRenderHelper.rotateIfSneaking(player);

			GlStateManager.translatef(0F, h, i);

			GlStateManager.rotatef(rz, 0F, 0F, 1F);
			GlStateManager.rotatef(rx, 1F, 0F, 0F);
			GlStateManager.rotatef(ry, 0F, 1F, 0F);
			GlStateManager.scalef(s, s, s);
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getWidth(), icon.getHeight(), 1F / 32F);
			GlStateManager.scalef(sr, sr, sr);
			GlStateManager.rotatef(-ry, 0F, 1F, 0F);
			GlStateManager.rotatef(-rx, 1F, 0F, 0F);
			GlStateManager.rotatef(-rz, 0F, 0F, 1F);

			if(meta != 2) { // Sephiroth
				GlStateManager.scalef(-1F, 1F, 1F);
				GlStateManager.rotatef(rz, 0F, 0F, 1F);
				GlStateManager.rotatef(rx, 1F, 0F, 0F);
				GlStateManager.rotatef(ry, 0F, 1F, 0F);
				GlStateManager.scalef(s, s, s);
				IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getWidth(), icon.getHeight(), 1F / 32F);
				GlStateManager.scalef(sr, sr, sr);
				GlStateManager.rotatef(-ry, 1F, 0F, 0F);
				GlStateManager.rotatef(-rx, 1F, 0F, 0F);
				GlStateManager.rotatef(-rz, 0F, 0F, 1F);
			}

			GlStateManager.color3f(1F, 1F, 1F);
			GlStateManager.popMatrix();

			OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, lbx, lby);

			if(meta == 1)
				renderHalo(player, partialTicks);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHalo(EntityLivingBase player, float partialTicks) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, 240, 240);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		Minecraft.getInstance().textureManager.bindTexture(textureHalo);

		if(player != null)
			AccessoryRenderHelper.translateToHeadLevel(player);
		GlStateManager.translatef(0, 1.5F, 0);
		GlStateManager.rotatef(30, 1, 0, -1);
		GlStateManager.translatef(-0.1F, -0.5F, -0.1F);
		if(player != null)
			GlStateManager.rotatef(player.ticksExisted + partialTicks, 0, 1, 0);
		else GlStateManager.rotatef(Botania.proxy.getWorldElapsedTicks(), 0, 1, 0);

		Tessellator tes = Tessellator.getInstance();
		ShaderHelper.useShader(ShaderHelper.halo);
		tes.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tes.getBuffer().pos(-0.75, 0, -0.75).tex(0, 0).endVertex();
		tes.getBuffer().pos(-0.75, 0, 0.75).tex(0, 1).endVertex();
		tes.getBuffer().pos(0.75, 0, 0.75).tex(1, 1).endVertex();
		tes.getBuffer().pos(0.75, 0, -0.75).tex(1, 0).endVertex();
		tes.draw();
		ShaderHelper.releaseShader();

		GlStateManager.enableLighting();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableCull();
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(EntityPlayer player, ItemStack stack) {
		int u = Math.max(1, ItemNBTHelper.getInt(stack, TAG_VARIANT, 0)) * 9 - 9;
		int v = 0;

		Minecraft mc = Minecraft.getInstance();
		mc.textureManager.bindTexture(textureHud);
		int xo = mc.mainWindow.getScaledWidth() / 2 + 10;
		int x = xo;
		int y = mc.mainWindow.getScaledHeight() - ConfigHandler.CLIENT.flightBarHeight.get();
		if(player.areEyesInFluid(FluidTags.WATER))
			y = mc.mainWindow.getScaledHeight() - ConfigHandler.CLIENT.flightBarBreathHeight.get();

		int left = ItemNBTHelper.getInt(stack, TAG_TIME_LEFT, MAX_FLY_TIME);

		int segTime = MAX_FLY_TIME / 10;
		int segs = left / segTime + 1;
		int last = left % segTime;

		for(int i = 0; i < segs; i++) {
			float trans = 1F;
			if(i == segs - 1) {
				trans = (float) last / (float) segTime;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.disableAlphaTest();
			}

			GlStateManager.color4f(1F, 1F, 1F, trans);
			RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 9, 9);
			x += 8;
		}

		if(player.abilities.isFlying) {
			int width = ItemNBTHelper.getInt(stack, TAG_DASH_COOLDOWN, 0);
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			if(width > 0)
				Gui.drawRect(xo, y - 2, xo + 80, y - 1, 0x88000000);
			Gui.drawRect(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
		}

		GlStateManager.enableAlphaTest();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		mc.textureManager.bindTexture(Gui.ICONS);
	}
}
