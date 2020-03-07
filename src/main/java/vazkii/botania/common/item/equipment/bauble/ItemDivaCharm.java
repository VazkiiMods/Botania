/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
		if (event.getSource().getImmediateSource() instanceof PlayerEntity
				&& event.getEntityLiving() instanceof MobEntity
				&& !event.getEntityLiving().world.isRemote
				&& event.getEntityLiving().isNonBoss()
				&& Math.random() < 0.6F) {
			Runnable lambda = () -> {
				MobEntity target = (MobEntity) event.getEntityLiving();
				PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
				ItemStack amulet = EquipmentHandler.findOrEmpty(ModItems.divaCharm, player);

				if (!amulet.isEmpty()) {
					final int cost = 250;
					if (ManaItemHandler.requestManaExact(amulet, player, cost, false)) {
						final int range = 20;

						List mobs = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(target.getX() - range, target.getY() - range, target.getZ() - range, target.getX() + range, target.getY() + range, target.getZ() + range), Predicates.instanceOf(IMob.class));
						if (mobs.size() > 1) {
							if (SubTileHeiseiDream.brainwashEntity(target, (List<IMob>) mobs)) {
								target.heal(target.getMaxHealth());
								target.revive();
								if (target instanceof CreeperEntity) {
									((CreeperEntity) event.getEntityLiving()).timeSinceIgnited = 2;
								}

								ManaItemHandler.requestManaExact(amulet, player, cost, true);
								player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.divaCharm, SoundCategory.PLAYERS, 1F, 1F);
								PacketHandler.sendToNearby(target.world, target, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.DIVA_EFFECT, target.getX(), target.getY(), target.getZ(), target.getEntityId()));
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
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.translateToHeadLevel(ms, player, partialTicks);
		ms.scale(0.8F, 0.8F, 0.8F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90));
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		ms.translate(0.1625, -1.625, 0.40);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.DEFAULT_UV, ms, buffers);
	}
}
