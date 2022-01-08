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
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.EffectType;
import vazkii.botania.mixin.AccessorCreeper;
import vazkii.botania.mixin.AccessorEntity;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public class ItemDivaCharm extends ItemBauble {

	public ItemDivaCharm(Properties props) {
		super(props);
		Botania.runOnClient.accept(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	public static void onEntityDamaged(Player player, Entity entity) {
		if (entity instanceof Mob target
				&& !entity.level.isClientSide
				&& entity.canChangeDimensions()
				&& Math.random() < 0.6F) {
			ItemStack amulet = EquipmentHandler.findOrEmpty(ModItems.divaCharm, player);

			if (!amulet.isEmpty()) {
				final int cost = 250;
				if (ManaItemHandler.instance().requestManaExact(amulet, player, cost, false)) {
					final int range = 20;

					@SuppressWarnings("unchecked")
					List<Enemy> mobs = (List<Enemy>) (List<?>) player.level.getEntitiesOfClass(Entity.class, new AABB(target.getX() - range, target.getY() - range, target.getZ() - range, target.getX() + range, target.getY() + range, target.getZ() + range), Predicates.instanceOf(Enemy.class));
					if (mobs.size() > 1) {
						if (SubTileHeiseiDream.brainwashEntity(target, mobs)) {
							target.heal(target.getMaxHealth());
							((AccessorEntity) target).callUnsetRemoved();
							if (target instanceof Creeper) {
								((AccessorCreeper) target).setCurrentFuseTime(2);
							}

							ManaItemHandler.instance().requestManaExact(amulet, player, cost, true);
							player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.divaCharm, SoundSource.PLAYERS, 1F, 1F);
							IXplatAbstractions.INSTANCE.sendEffectPacketNear(target, EffectType.DIVA_EFFECT, target.getX(), target.getY(), target.getZ(), target.getId());
						}
					}
				}
			}
		}
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.head.translateAndRotate(ms);
			ms.translate(0.15, -0.42, -0.35);
			ms.scale(0.4F, -0.4F, -0.4F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE,
					light, OverlayTexture.NO_OVERLAY, ms, buffers, player.getId());
		}
	}
}
