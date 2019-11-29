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
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class ItemDivaCharm extends ItemBauble implements IManaUsingItem {

	public ItemDivaCharm(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityDamaged);
	}

	private void onEntityDamaged(LivingHurtEvent event) {
		if(event.getSource().getImmediateSource() instanceof PlayerEntity
				&& event.getEntityLiving() instanceof MobEntity
				&& !event.getEntityLiving().world.isRemote && Math.random() < 0.6F) {
			Runnable lambda = () -> {
				MobEntity target = (MobEntity) event.getEntityLiving();
				PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
				ItemStack amulet = EquipmentHandler.findOrEmpty(ModItems.divaCharm, player);

				if(!amulet.isEmpty()) {
					final int cost = 250;
					if(ManaItemHandler.requestManaExact(amulet, player, cost, false)) {
						final int range = 20;

						List mobs = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(target.posX - range, target.posY - range, target.posZ - range, target.posX + range, target.posY + range, target.posZ + range), Predicates.instanceOf(IMob.class));
						if(mobs.size() > 1) {
							if(SubTileHeiseiDream.brainwashEntity(target, (List<IMob>) mobs)) {
								target.heal(target.getMaxHealth());
								target.revive();
								if(target instanceof CreeperEntity)
									((CreeperEntity) event.getEntityLiving()).timeSinceIgnited = 2;

								ManaItemHandler.requestManaExact(amulet, player, cost, true);
								player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.divaCharm, SoundCategory.PLAYERS, 1F, 1F);
								PacketHandler.sendToNearby(target.world, target, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.DIVA_EFFECT, target.posX, target.posY, target.posZ, target.getEntityId()));
							}
						}
					}
				}
			};

			// Delay until end of tick because setAttackTarget(player) is called *after* this event fires, but
			// we want to overwrite it
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			server.enqueue(new TickDelayedTask(server.getTickCounter(), lambda));
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		AccessoryRenderHelper.translateToHeadLevel(player, partialTicks);
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.scaled(0.8, 0.8, 0.8);
		GlStateManager.rotatef(-90, 0, 1, 0);
		GlStateManager.rotatef(180, 1, 0, 0);
		GlStateManager.translated(0.1625, -1.625, 0.40);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
	}
}
