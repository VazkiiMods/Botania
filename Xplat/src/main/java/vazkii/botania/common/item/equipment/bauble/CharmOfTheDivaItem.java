/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.block.flower.functional.HeiseiDreamBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.mixin.CreeperAccessor;
import vazkii.botania.mixin.EntityAccessor;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CharmOfTheDivaItem extends BaubleItem {
	public static final int MANA_COST = 250;
	public static final int CHARM_RANGE = 20;
	private static final String TAG_MOBS_TO_CHARM = "mobsToCharm";

	public CharmOfTheDivaItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.level().isClientSide()) {
			return;
		}
		var tag = stack.getTag();
		if (tag != null && tag.contains(TAG_MOBS_TO_CHARM, Tag.TAG_INT_ARRAY)) {
			charmMobs(stack, (Player) entity, tag.getIntArray(TAG_MOBS_TO_CHARM));
			stack.removeTagKey(TAG_MOBS_TO_CHARM);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide()) {
			stack.removeTagKey(TAG_MOBS_TO_CHARM);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide()) {
			stack.removeTagKey(TAG_MOBS_TO_CHARM);
		}
	}

	private static Predicate<Mob> getCharmTargetPredicate(Player player, Mob mobToCharm) {
		return mob -> mob != mobToCharm && mob.isAlive() && mob.canBeSeenAsEnemy() && !mob.isPassengerOfSameVehicle(mobToCharm)
				&& (!(mob instanceof TamableAnimal tamable) || !tamable.isOwnedBy(player))
				&& (mob instanceof Enemy || mob instanceof NeutralMob neutralMob && (neutralMob.isAngryAt(player)
						|| mob.getTarget() instanceof TamableAnimal targetTamable && targetTamable.isOwnedBy(player)));
	}

	private static void charmMobs(ItemStack amulet, Player player, int[] mobsToCharmIds) {
		for (int mobId : mobsToCharmIds) {
			if (!ManaItemHandler.instance().requestManaExact(amulet, player, MANA_COST, false)) {
				return;
			}
			var mobEntity = player.level().getEntity(mobId);
			if (mobEntity instanceof Mob target && target.isAlive()
					&& (mobEntity instanceof Enemy || mobEntity instanceof NeutralMob)
					// don't encourage being a bad pet owner
					&& (!(mobEntity instanceof TamableAnimal tamable) || !tamable.isOwnedBy(player))
					// check that target is still nearby, since it was marked some time earlier
					&& player.position().closerThan(target.position(), CHARM_RANGE)) {
				List<Mob> potentialTargets = player.level().getEntitiesOfClass(Mob.class,
						AABB.ofSize(target.position(), 2 * CHARM_RANGE, 2 * CHARM_RANGE, 2 * CHARM_RANGE),
						getCharmTargetPredicate(player, target));
				if (!potentialTargets.isEmpty() && HeiseiDreamBlockEntity.brainwashEntity(target, potentialTargets)) {
					target.heal(target.getMaxHealth());
					((EntityAccessor) target).callUnsetRemoved();
					if (target instanceof Creeper) {
						((CreeperAccessor) target).setCurrentFuseTime(2);
					}

					ManaItemHandler.instance().requestManaExact(amulet, player, MANA_COST, true);
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.divaCharm, SoundSource.PLAYERS, 1F, 1F);
					XplatAbstractions.INSTANCE.sendToTracking(target, new BotaniaEffectPacket(EffectType.DIVA_EFFECT, target.getX(), target.getY(), target.getZ(), target.getId()));
				}
			}
		}
	}

	public static void onEntityDamaged(Player player, Entity entity) {
		if (entity instanceof Mob target
				&& !entity.level().isClientSide
				&& Math.random() < 0.6F) {
			ItemStack amulet = EquipmentHandler.findOrEmpty(BotaniaItems.divaCharm, player);

			if (!amulet.isEmpty()) {
				// only mark potential target, then charm later, outside the damage logic
				var tag = amulet.getOrCreateTag();
				int[] entityIds;
				if (tag.contains(TAG_MOBS_TO_CHARM, Tag.TAG_INT_ARRAY)) {
					int[] oldIds = tag.getIntArray(TAG_MOBS_TO_CHARM);
					entityIds = Arrays.copyOf(oldIds, oldIds.length + 1);
					entityIds[entityIds.length - 1] = entity.getId();
				} else {
					entityIds = new int[] { entity.getId() };
				}
				tag.putIntArray(TAG_MOBS_TO_CHARM, entityIds);
			}
		}
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.head.translateAndRotate(ms);
			ms.translate(0.15, -0.42, -0.35);
			ms.scale(0.4F, -0.4F, -0.4F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
					light, OverlayTexture.NO_OVERLAY, ms, buffers, living.level(), living.getId());
		}
	}
}
