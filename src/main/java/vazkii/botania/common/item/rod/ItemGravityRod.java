/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityThrownItem;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemGravityRod extends Item implements IManaUsingItem {
	private static final Tag.Identified<EntityType<?>> BLACKLIST = ModTags.Entities.SHADED_MESA_BLACKLIST;
	private static final float RANGE = 3F;
	private static final int COST = 2;
	private static final Predicate<Entity> CAN_TARGET = e -> !e.isSpectator() && e.isAlive() && !BLACKLIST.contains(e.getType());

	private static final String TAG_TICKS_TILL_EXPIRE = "ticksTillExpire";
	private static final String TAG_TICKS_COOLDOWN = "ticksCooldown";
	private static final String TAG_TARGET = "target";
	private static final String TAG_DIST = "dist";

	public ItemGravityRod(Settings props) {
		super(props);
	}

	/* todo fabric
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return newStack.getItem() != this;
	}
	*/

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean held) {
		if (!(entity instanceof PlayerEntity)) {
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
		if (!(entity instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;
		leftClick(player);
	}

	/* todo fabric
	// Prevent damaging the entity you just held with the rod
	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		return ItemNBTHelper.getInt(stack, TAG_TICKS_TILL_EXPIRE, 0) != 0;
	}
	*/

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
		int ticksCooldown = ItemNBTHelper.getInt(stack, TAG_TICKS_COOLDOWN, 0);
		double length = ItemNBTHelper.getDouble(stack, TAG_DIST, -1);

		if (ticksCooldown == 0) {
			Entity target = null;
			if (targetID != -1 && player.world.getEntityById(targetID) != null) {
				Entity taritem = player.world.getEntityById(targetID);

				boolean found = false;
				Vector3 targetVec = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					targetVec = targetVec.add(new Vector3(player.getRotationVector()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getOtherEntities(player, targetVec.boxForRange(RANGE), CAN_TARGET);
					distance++;
					if (entities.contains(taritem)) {
						found = true;
					}
				}

				if (found) {
					target = player.world.getEntityById(targetID);
				}
			}

			if (target == null) {
				Vector3 targetVec = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					targetVec = targetVec.add(new Vector3(player.getRotationVector()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getOtherEntities(player, targetVec.boxForRange(RANGE), CAN_TARGET);
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
					return TypedActionResult.fail(stack);
				}

				if (ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true)) {
					if (target instanceof ItemEntity) {
						((ItemEntity) target).setPickupDelay(5);
					}

					if (target instanceof LivingEntity) {
						LivingEntity targetEntity = (LivingEntity) target;
						targetEntity.fallDistance = 0.0F;
						if (targetEntity.getStatusEffect(StatusEffects.SLOWNESS) == null) {
							targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 3, true, true));
						}
					}

					Vector3 target3 = Vector3.fromEntityCenter(player)
							.add(new Vector3(player.getRotationVector()).multiply(length)).add(0, 0.5, 0);
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
						world.addParticle(data, target.getX() + target.getWidth() / 2, target.getY() + target.getHeight() / 2, target.getZ() + target.getWidth() / 2, xm, ym, zm);
					}

					MathHelper.setEntityMotionFromVector(target, target3, 0.3333333F);

					ItemNBTHelper.setInt(stack, TAG_TARGET, target.getEntityId());
					ItemNBTHelper.setDouble(stack, TAG_DIST, length);
				}

				ItemNBTHelper.setInt(stack, TAG_TICKS_TILL_EXPIRE, 5);
				return TypedActionResult.consume(stack);
			}
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	private static void leftClick(PlayerEntity player) {
		ItemStack stack = player.getMainHandStack();
		if (!stack.isEmpty() && stack.getItem() == ModItems.gravityRod) {
			int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
			ItemNBTHelper.getDouble(stack, TAG_DIST, -1);
			Entity item;

			if (targetID != -1 && player.world.getEntityById(targetID) != null) {
				Entity taritem = player.world.getEntityById(targetID);

				boolean found = false;
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					target = target.add(new Vector3(player.getRotationVector()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getOtherEntities(player, target.boxForRange(RANGE), CAN_TARGET);
					distance++;
					if (entities.contains(taritem)) {
						found = true;
					}
				}

				if (found) {
					item = taritem;
					ItemNBTHelper.setInt(stack, TAG_TARGET, -1);
					ItemNBTHelper.setDouble(stack, TAG_DIST, -1);
					Vector3 moveVector = new Vector3(player.getRotationVector().normalize());
					if (item instanceof ItemEntity) {
						((ItemEntity) item).setPickupDelay(20);
						float mot = IManaProficiencyArmor.hasProficiency(player, stack) ? 2.25F : 1.5F;
						item.setVelocity(moveVector.x * mot, moveVector.y, moveVector.z * mot);
						if (!player.world.isClient) {
							EntityThrownItem thrown = new EntityThrownItem(item.world, item.getX(), item.getY(), item.getZ(), (ItemEntity) item);
							item.world.spawnEntity(thrown);
						}
						item.remove();
					} else {
						item.setVelocity(moveVector.multiply(3, 1.5, 3).toVector3d());
					}
					ItemNBTHelper.setInt(stack, TAG_TICKS_COOLDOWN, 10);
				}
			}
		}
	}
}
