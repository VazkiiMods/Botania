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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlightTiara extends ItemBauble implements IManaUsingItem, IBaubleRender, ICraftAchievement {

	private static ResourceLocation textureHalo = new ResourceLocation(LibResources.MISC_HALO);

	public static List<String> playersWithFlight = new ArrayList();
	private static final int COST = 35;

	public static IIcon[] wingIcons;
	private static final int SUBTYPES = 7;
	private static final int WING_TYPES = 8;

	public ItemFlightTiara() {
		super(LibItemNames.FLIGHT_TIARA);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		setHasSubtypes(true);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this, 0);
		wingIcons = new IIcon[WING_TYPES];
		for(int i = 0; i < WING_TYPES; i++)
			wingIcons[i] = IconHelper.forItem(par1IconRegister, this, i + 1);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < SUBTYPES + 1; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(StatCollector.translateToLocal("botania.wings" + par1ItemStack.getItemDamage()));
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		if(stack.getItemDamage() != 8 && stack.getDisplayName().hashCode() == 0x7867EB0B) {
			stack.setItemDamage(8);
			stack.getTagCompound().removeTag("display");
		}
	}
	
	@SubscribeEvent
	public void updatePlayerFlyStatus(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;

			ItemStack tiara = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0);
			if(playersWithFlight.contains(playerStr(player))) {
				if(shouldPlayerHaveFlight(player)) {
					player.capabilities.allowFlying = true;
					if(player.capabilities.isFlying) {
						if(!player.worldObj.isRemote)
							ManaItemHandler.requestManaExact(tiara, player, COST, true);
						else if(Math.abs(player.motionX) > 0.1 || Math.abs(player.motionZ) > 0.1) {
							double x = event.entityLiving.posX - 0.5;
							double y = event.entityLiving.posY - 1.7;
							double z = event.entityLiving.posZ - 0.5;

							player.getGameProfile().getName();
							float r = 1F;
							float g = 1F;
							float b = 1F;

							switch(tiara.getItemDamage()) {
							case 2 : {
								r = 0.1F;
								g = 0.1F;
								b = 0.1F;
								break;
							}
							case 3 : {
								r = 0F;
								g = 0.6F;
								b = 1F;
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
								r = 0F;
								b = 0F;
								break;
							}
							}

							for(int i = 0; i < 2; i++)
								Botania.proxy.sparkleFX(event.entityLiving.worldObj, x + Math.random() * event.entityLiving.width, y + Math.random() * 0.4, z + Math.random() * event.entityLiving.width, r, g, b, 2F * (float) Math.random(), 20);
						}
					}
				} else {
					if(!player.capabilities.isCreativeMode) {
						player.capabilities.allowFlying = false;
						player.capabilities.isFlying = false;
						player.capabilities.disableDamage = false;
					}
					playersWithFlight.remove(playerStr(player));
				}
			} else if(shouldPlayerHaveFlight(player)) {
				playersWithFlight.add(playerStr(player));
				player.capabilities.allowFlying = true;
			}
		}
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.player.getGameProfile().getName();
		ItemFlightTiara.playersWithFlight.remove(username + ":false");
		ItemFlightTiara.playersWithFlight.remove(username + ":true");
	}

	public static String playerStr(EntityPlayer player) {
		return player.getGameProfile().getName() + ":" + player.worldObj.isRemote;
	}

	private boolean shouldPlayerHaveFlight(EntityPlayer player) {
		ItemStack armor = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0);
		return armor != null && armor.getItem() == this && ManaItemHandler.requestManaExact(armor, player, COST, false);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		int meta = stack.getItemDamage();
		if(type == RenderType.BODY) {
			if(meta > 0 && meta <= ItemFlightTiara.wingIcons.length) {
				IIcon icon = ItemFlightTiara.wingIcons[meta - 1];
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

				boolean flying = event.entityPlayer.capabilities.isFlying;

				float rz = 120F;
				float rx = 20F + (float) ((Math.sin((double) (event.entityPlayer.ticksExisted + event.partialRenderTick) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
				float ry = 0F;
				float h = 0.2F;
				float i = 0.15F;
				float s = 1F;

				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1F, 1F, 1F, 1F);

				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				switch(meta) {
				case 1 : { // Jibril
					h = 0.4F;
					break;
				}
				case 2 : { // Sephiroth
					s = 1.3F;
					break;
				}
				case 3 : { // Cirno
					h = -0.1F;
					rz = 0F;
					rx = 0F;
					i = 0.3F;
					break;
				}
				case 4 : { // Fire
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
					break;
				}
				case 5 : { // Kuroyukihime
					h = 0.8F;
					rz = 180F;
					ry = -rx;
					rx = 0F;
					s = 2F;
					break;
				}
				case 6 : { // Random Devil
					rz = 150F;
					break;
				}
				case 7 : { // Lyfa
					h = -0.1F;
					rz = 0F;
					ry = -rx;
					rx = 0F;
					GL11.glColor4f(1F, 1F, 1F, 0.5F + (float) Math.cos((double) (event.entityPlayer.ticksExisted + event.partialRenderTick) * 0.3F) * 0.2F);
					break;
				}
				case 8 : { // The One
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
					rz = 180F;
					rx = 0F;
					s = 1.5F;
					h = 1.2F;
					GL11.glColor4f(1F, 1F, 1F, 0.5F + (flying ? ((float) Math.cos((double) (event.entityPlayer.ticksExisted + event.partialRenderTick) * 0.3F) * 0.25F) + 0.25F : 0F));
				}
				}

				float f = icon.getMinU();
				float f1 = icon.getMaxU();
				float f2 = icon.getMinV();
				float f3 = icon.getMaxV();
				float sr = 1F / s;

				Helper.rotateIfSneaking(event.entityPlayer);

				GL11.glTranslatef(0F, h, i);

				GL11.glRotatef(rz, 0F, 0F, 1F);
				GL11.glRotatef(rx, 1F, 0F, 0F);
				GL11.glRotatef(ry, 0F, 1F, 0F);
				GL11.glScalef(s, s, s);
				ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
				GL11.glScalef(sr, sr, sr);
				GL11.glRotatef(-ry, 0F, 1F, 0F);
				GL11.glRotatef(-rx, 1F, 0F, 0F);
				GL11.glRotatef(-rz, 0F, 0F, 1F);

				if(meta != 2) { // Sephiroth
					GL11.glScalef(-1F, 1F, 1F);
					GL11.glRotatef(rz, 0F, 0F, 1F);
					GL11.glRotatef(rx, 1F, 0F, 0F);
					GL11.glRotatef(ry, 0F, 1F, 0F);
					GL11.glScalef(s, s, s);
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
					GL11.glScalef(sr, sr, sr);
					GL11.glRotatef(-ry, 1F, 0F, 0F);
					GL11.glRotatef(-rx, 1F, 0F, 0F);
					GL11.glRotatef(-rz, 0F, 0F, 1F);
				}

				GL11.glColor3f(1F, 1F, 1F);
				GL11.glPopMatrix();
			}
		} else if(meta == 1) // Jibril's Halo
			renderHalo(event.entityPlayer, event.partialRenderTick);
	}

	@SideOnly(Side.CLIENT)
	private void renderHalo(EntityPlayer player, float partialTicks) {
		GL11.glShadeModel(GL11.GL_SMOOTH);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().renderEngine.bindTexture(textureHalo);

		Helper.translateToHeadLevel(player);
		GL11.glRotated(30, 1, 0, -1);
		GL11.glTranslatef(-0.1F, -0.5F, -0.1F);
		GL11.glRotatef(player.ticksExisted + partialTicks, 0, 1, 0);

		Tessellator tes = Tessellator.instance;
		ShaderHelper.useShader(ShaderHelper.halo);
		tes.startDrawingQuads();
		tes.addVertexWithUV(-0.75, 0, -0.75, 0, 0);
		tes.addVertexWithUV(-0.75, 0, 0.75, 0, 1);
		tes.addVertexWithUV(0.75, 0, 0.75, 1, 1);
		tes.addVertexWithUV(0.75, 0, -0.75, 1, 0);
		tes.draw();
		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return stack.getItemDamage() == 1 ? ModAchievements.tiaraWings : null;
	}

}
