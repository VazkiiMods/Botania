/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.VecHelper;
import vazkii.botania.common.entity.EntityThrownItem;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemGravityRod extends Item implements FabricItem {
	private static final Tag.Named<EntityType<?>> BLACKLIST = ModTags.Entities.SHADED_MESA_BLACKLIST;
	private static final float RANGE = 3F;
	private static final int COST = 2;
	private static final Predicate<Entity> CAN_TARGET = e -> !e.isSpectator() && e.isAlive() && !BLACKLIST.contains(e.getType());

	private static final String TAG_TICKS_TILL_EXPIRE = "ticksTillExpire";
	private static final String TAG_TICKS_COOLDOWN = "ticksCooldown";
	private static final String TAG_TARGET = "target";
	private static final String TAG_DIST = "dist";

	public ItemGravityRod(Properties props) {
		super(props);
	}

	@Override
	public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return !newStack.is(this);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean held) {
		if (!(entity instanceof Player)) {
			return;
		}

		int ticksTillExpire = ItemNBTHelper.getInt(stack, TAG_TICKS_TILL_EXPIRE, 0);
		int ticksCooldown = ItemNBTHelper.getInt(stack, TAG_TICKS_COOLDOWN, 0);

		if (ticksTillExpire == 0) {
			ItemNBTHelper.setInt(stack, TAG_TARGET, -1);
			ItemNBTHelper.setDouble(stack, TAG_DIST, -1);
		}

		if (ticksCooldown > 0) {
			ticksCooldown--;
		}

		if (ticksTillExpire >= 0) {
			ticksTillExpire--;
		}
		ItemNBTHelper.setInt(stack, TAG_TICKS_TILL_EXPIRE, ticksTillExpire);
		ItemNBTHelper.setInt(stack, TAG_TICKS_COOLDOWN, ticksCooldown);
	}

	public static void onEntitySwing(LivingEntity entity) {
		if (!(entity instanceof Player player)) {
			return;
		}
		leftClick(player);
	}

	// Prevent damaging the entity you just held with the rod
	public static InteractionResult onAttack(Player player, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(ModItems.gravityRod)) {
			if (ItemNBTHelper.getInt(stack, TAG_TICKS_TILL_EXPIRE, 0) != 0) {
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
		int ticksCooldown = ItemNBTHelper.getInt(stack, TAG_TICKS_COOLDOWN, 0);
		double length = ItemNBTHelper.getDouble(stack, TAG_DIST, -1);

		if (ticksCooldown == 0) {
			Entity target = null;
			if (targetID != -1 && player.level.getEntity(targetID) != null) {
				Entity taritem = player.level.getEntity(targetID);

				boolean found = false;
				Vec3 targetVec = VecHelper.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					targetVec = targetVec.add(player.getLookAngle().scale(distance)).add(0, 0.5, 0);
					entities = player.level.getEntities(player, VecHelper.boxForRange(targetVec, RANGE), CAN_TARGET);
					distance++;
					if (entities.contains(taritem)) {
						found = true;
					}
				}

				if (found) {
					target = player.level.getEntity(targetID);
				}
			}

			if (target == null) {
				Vec3 targetVec = VecHelper.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					targetVec = targetVec.add(player.getLookAngle().scale(distance)).add(0, 0.5, 0);
					entities = player.level.getEntities(player, VecHelper.boxForRange(targetVec, RANGE), CAN_TARGET);
					distance++;
				}

				if (entities.size() > 0) {
					target = entities.get(0);
					length = 5.5D;
					if (target instanceof ItemEntity) {
						length = 2.0D;
					}
				}
			}

			if (target != null) {
				if (BLACKLIST.contains(target.getType())) {
					return InteractionResultHolder.fail(stack);
				}

				if (ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true)) {
					if (target instanceof ItemEntity item) {
						item.setPickUpDelay(5);
					}

					if (target instanceof LivingEntity living) {
						living.fallDistance = 0.0F;
						if (living.getEffect(MobEffects.MOVEMENT_SLOWDOWN) == null) {
							living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 3, true, true));
						}
					}

					Vec3 target3 = VecHelper.fromEntityCenter(player)
							.add(player.getLookAngle().scale(length)).add(0, 0.5, 0);
					if (target instanceof ItemEntity) {
						target3 = target3.add(0, 0.25, 0);
					}

					for (int i = 0; i < 4; i++) {
						float r = 0.5F + (float) Math.random() * 0.5F;
						float b = 0.5F + (float) Math.random() * 0.5F;
						float s = 0.2F + (float) Math.random() * 0.1F;
						float m = 0.1F;
						float xm = ((float) Math.random() - 0.5F) * m;
						float ym = ((float) Math.random() - 0.5F) * m;
						float zm = ((float) Math.random() - 0.5F) * m;
						WispParticleData data = WispParticleData.wisp(s, r, 0F, b);
						world.addParticle(data, target.getX() + target.getBbWidth() / 2, target.getY() + target.getBbHeight() / 2, target.getZ() + target.getBbWidth() / 2, xm, ym, zm);
					}

					MathHelper.setEntityMotionFromVector(target, target3, 0.3333333F);

					ItemNBTHelper.setInt(stack, TAG_TARGET, target.getId());
					ItemNBTHelper.setDouble(stack, TAG_DIST, length);
				}

				ItemNBTHelper.setInt(stack, TAG_TICKS_TILL_EXPIRE, 5);
				return InteractionResultHolder.consume(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	private static void leftClick(Player player) {
		ItemStack stack = player.getMainHandItem();
		if (!stack.isEmpty() && stack.is(ModItems.gravityRod)) {
			int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
			ItemNBTHelper.getDouble(stack, TAG_DIST, -1);

			if (targetID != -1 && player.level.getEntity(targetID) != null) {
				Entity target = player.level.getEntity(targetID);

				boolean found = false;
				Vec3 vec = VecHelper.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					vec = vec.add(player.getLookAngle().scale(distance)).add(0, 0.5, 0);
					entities = player.level.getEntities(player, new AABB(vec.subtract(RANGE, RANGE, RANGE), vec.add(RANGE, RANGE, RANGE)), CAN_TARGET);
					distance++;
					if (entities.contains(target)) {
						found = true;
					}
				}

				if (found) {
					ItemNBTHelper.setInt(stack, TAG_TARGET, -1);
					ItemNBTHelper.setDouble(stack, TAG_DIST, -1);
					Vec3 moveVector = player.getLookAngle().normalize();
					if (target instanceof ItemEntity item) {
						item.setPickUpDelay(20);
						float mot = IManaProficiencyArmor.hasProficiency(player, stack) ? 2.25F : 1.5F;
						item.setDeltaMovement(moveVector.x * mot, moveVector.y, moveVector.z * mot);
						if (!player.level.isClientSide) {
							EntityThrownItem thrown = new EntityThrownItem(item.level, item.getX(), item.getY(), item.getZ(), item);
							item.level.addFreshEntity(thrown);
						}
						item.discard();
					} else {
						if (target instanceof LivingEntity living) {
							living.setLastHurtByMob(player);
							living.setLastHurtByPlayer(player);
						}
						target.setDeltaMovement(moveVector.multiply(3, 1.5, 3));
					}
					ItemNBTHelper.setInt(stack, TAG_TICKS_COOLDOWN, 10);
				}
			}
		}
	}
}
