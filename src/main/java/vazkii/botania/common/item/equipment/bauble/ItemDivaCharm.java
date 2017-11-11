/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 25, 2014, 10:30:39 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.List;

public class ItemDivaCharm extends ItemBauble implements IManaUsingItem, IBaubleRender {

	public ItemDivaCharm() {
		super(LibItemNames.DIVA_CHARM);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onEntityDamaged(LivingHurtEvent event) {
		if(event.getSource().getImmediateSource() instanceof EntityPlayer && event.getEntityLiving() instanceof EntityLiving && !event.getEntityLiving().world.isRemote && Math.random() < 0.6F) {
			EntityPlayer player = (EntityPlayer) event.getSource().getImmediateSource();
			ItemStack amulet = BaublesApi.getBaublesHandler(player).getStackInSlot(6);
			if(!amulet.isEmpty() && amulet.getItem() == this) {
				final int cost = 250;
				if(ManaItemHandler.requestManaExact(amulet, player, cost, false)) {
					final int range = 20;

					List mobs = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(event.getEntityLiving().posX - range, event.getEntityLiving().posY - range, event.getEntityLiving().posZ - range, event.getEntityLiving().posX + range, event.getEntityLiving().posY + range, event.getEntityLiving().posZ + range), Predicates.instanceOf(IMob.class));
					if(mobs.size() > 1) {
						if(SubTileHeiseiDream.brainwashEntity((EntityLiving) event.getEntityLiving(), (List<IMob>) mobs)) {
							if(event.getEntityLiving() instanceof EntityCreeper)
								((EntityCreeper) event.getEntityLiving()).timeSinceIgnited = 2;
							event.getEntityLiving().heal(event.getEntityLiving().getMaxHealth());
							if(event.getEntityLiving().isDead)
								event.getEntityLiving().isDead = false;

							ManaItemHandler.requestManaExact(amulet, player, cost, true);
							player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.divaCharm, SoundCategory.PLAYERS, 1F, 1F);

							double x = event.getEntityLiving().posX;
							double y = event.getEntityLiving().posY;
							double z = event.getEntityLiving().posZ;

							for(int i = 0; i < 50; i++)
								Botania.proxy.sparkleFX(x + Math.random() * event.getEntityLiving().width, y + Math.random() * event.getEntityLiving().height, z + Math.random() * event.getEntityLiving().width, 1F, 1F, 0.25F, 1F, 3);
						}
					}
				}
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.CHARM;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.HEAD) {
			Helper.translateToHeadLevel(player);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.scale(0.8, 0.8, 0.8);
			GlStateManager.rotate(-90, 0, 1, 0);
			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.translate(0.1625, -1.625, 0.40);
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(this, 1), ItemCameraTransforms.TransformType.GROUND);
		}
	}

}
