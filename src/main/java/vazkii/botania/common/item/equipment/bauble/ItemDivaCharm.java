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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.mixin.AccessorCreeperEntity;

import java.util.List;

public class ItemDivaCharm extends ItemBauble implements IManaUsingItem {

	public ItemDivaCharm(Settings props) {
		super(props);
	}

	public static void onEntityDamaged(PlayerEntity player, Entity entity) {
		if (entity instanceof MobEntity
				&& !entity.world.isClient
				&& entity.canUsePortals()
				&& Math.random() < 0.6F) {
			MobEntity target = (MobEntity) entity;
			ItemStack amulet = EquipmentHandler.findOrEmpty(ModItems.divaCharm, player);

			if (!amulet.isEmpty()) {
				final int cost = 250;
				if (ManaItemHandler.instance().requestManaExact(amulet, player, cost, false)) {
					final int range = 20;

					@SuppressWarnings("unchecked")
					List<Monster> mobs = (List<Monster>) (List<?>) player.world.getEntitiesByClass(Entity.class, new Box(target.getX() - range, target.getY() - range, target.getZ() - range, target.getX() + range, target.getY() + range, target.getZ() + range), Predicates.instanceOf(Monster.class));
					if (mobs.size() > 1) {
						if (SubTileHeiseiDream.brainwashEntity(target, mobs)) {
							target.heal(target.getMaxHealth());
							target.removed = false;
							if (target instanceof CreeperEntity) {
								((AccessorCreeperEntity) target).setCurrentFuseTime(2);
							}

							ManaItemHandler.instance().requestManaExact(amulet, player, cost, true);
							player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.divaCharm, SoundCategory.PLAYERS, 1F, 1F);
							PacketBotaniaEffect.sendNearby(target, PacketBotaniaEffect.EffectType.DIVA_EFFECT, target.getX(), target.getY(), target.getZ(), target.getEntityId());
						}
					}
				}
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		bipedModel.head.rotate(ms);
		ms.translate(0.15, -0.42, -0.35);
		ms.scale(0.4F, -0.4F, -0.4F);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, ms, buffers);
	}
}
