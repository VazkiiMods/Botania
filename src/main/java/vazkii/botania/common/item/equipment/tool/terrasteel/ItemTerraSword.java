/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.network.PacketLeftClick;

import javax.annotation.Nullable;

import java.util.List;

public class ItemTerraSword extends ItemManasteelSword implements ILensEffect {

	private static final int MANA_PER_DAMAGE = 100;

	public ItemTerraSword(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props);
		AttackEntityCallback.EVENT.register(this::attackEntity);
	}

	public static void leftClick(ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof ItemTerraSword) {
			PacketLeftClick.send();
		}
	}

	private InteractionResult attackEntity(Player player, Level world, InteractionHand hand, Entity target, @Nullable EntityHitResult hit) {
		if (!player.level.isClientSide && !player.isSpectator()) {
			trySpawnBurst(player);
		}
		return InteractionResult.PASS;
	}

	public void trySpawnBurst(Player player) {
		if (!player.getMainHandItem().isEmpty()
				&& player.getMainHandItem().getItem() == this
				&& player.getAttackStrengthScale(0) == 1) {
			EntityManaBurst burst = getBurst(player, player.getMainHandItem());
			player.level.addFreshEntity(burst);
			player.getMainHandItem().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.terraBlade, SoundSource.PLAYERS, 0.4F, 1.4F);
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	public EntityManaBurst getBurst(Player player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player);

		float motionModifier = 7F;

		burst.setColor(0x20FF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setBurstMotion(burst.getDeltaMovement().x() * motionModifier,
				burst.getDeltaMovement().y() * motionModifier, burst.getDeltaMovement().z() * motionModifier);

		burst.setSourceLens(stack.copy());
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1);
		List<LivingEntity> entities = entity.level.getEntitiesOfClass(LivingEntity.class, axis);
		Entity thrower = entity.getOwner();

		for (LivingEntity living : entities) {
			if (living == thrower || living instanceof Player && thrower instanceof Player
					&& !((Player) thrower).canHarmPlayer(((Player) living))) {
				continue;
			}

			if (living.hurtTime == 0) {
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if (mana >= cost) {
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamageBonus();
					if (!burst.isFake() && !entity.level.isClientSide) {
						DamageSource source = DamageSource.MAGIC;
						if (thrower instanceof Player) {
							source = DamageSource.playerAttack((Player) thrower);
						} else if (thrower instanceof LivingEntity) {
							source = DamageSource.mobAttack((LivingEntity) thrower);
						}
						living.hurt(source, damage);
						entity.discard();
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}
}
