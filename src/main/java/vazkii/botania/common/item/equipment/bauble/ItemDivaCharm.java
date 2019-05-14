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

import com.google.common.base.Predicates;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosAPI;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class ItemDivaCharm extends ItemBauble implements IManaUsingItem {

	public ItemDivaCharm(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityDamaged);
	}

	private void onEntityDamaged(LivingHurtEvent event) {
		if (event.getSource().getImmediateSource() instanceof EntityPlayer && event.getEntityLiving() instanceof EntityLiving && !event.getEntityLiving().world.isRemote && Math.random() < 0.6F) {
			Runnable lambda = () -> {
				EntityLiving target = (EntityLiving) event.getEntityLiving();
				EntityPlayer player = (EntityPlayer) event.getSource().getImmediateSource();
				CuriosAPI.FinderData result = CuriosAPI.getCurioEquipped(ModItems.divaCharm, player);

				if(result != null) {
					ItemStack amulet = result.getStack();
					final int cost = 250;
					if(ManaItemHandler.requestManaExact(amulet, player, cost, false)) {
						final int range = 20;

						List mobs = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(target.posX - range, target.posY - range, target.posZ - range, target.posX + range, target.posY + range, target.posZ + range), Predicates.instanceOf(IMob.class));
						if(mobs.size() > 1) {
							if(SubTileHeiseiDream.brainwashEntity(target, (List<IMob>) mobs)) {
								target.heal(target.getMaxHealth());
								target.revive();
								if(target instanceof EntityCreeper)
									((EntityCreeper) event.getEntityLiving()).timeSinceIgnited = 2;

								ManaItemHandler.requestManaExact(amulet, player, cost, true);
								player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.divaCharm, SoundCategory.PLAYERS, 1F, 1F);
								PacketHandler.sendToNearby(target.world, target, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.DIVA_EFFECT, target.posX, target.posY, target.posZ, target.getEntityId()));
							}
						}
					}
				}
			};

			// Have to delay a tick because setAttackTarget(player) is called *after* the event fires, and we want to get rid of that
			// addScheduledTask runs the lambda immediately if on the main thread, hence this trickery
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			synchronized (server.futureTaskQueue) {
				server.futureTaskQueue.add(ListenableFutureTask.create(lambda, null));
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public boolean hasRender(String identifier, EntityLivingBase living) {
			return true;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void doRender(String identifier, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			IBaubleRender.Helper.translateToHeadLevel(player);
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.scaled(0.8, 0.8, 0.8);
			GlStateManager.rotatef(-90, 0, 1, 0);
			GlStateManager.rotatef(180, 1, 0, 0);
			GlStateManager.translated(0.1625, -1.625, 0.40);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
		}
	}
}
