/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.entity.PixieEntity;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BottledManaItem extends Item {
	public static final int SWIGS = 6;
	private static final String TAG_SWIGS_LEFT = "swigsLeft";
	private static final String TAG_SEED = "randomSeed";

	public BottledManaItem(Properties props) {
		super(props);
	}

	private void effect(ItemStack stack, LivingEntity living) {
		switch (new Random(getSeed(stack)).nextInt(16)) {
			case 0 -> { // Random motion
				living.setDeltaMovement((Math.random() - 0.5) * 3, living.getDeltaMovement().y(),
						(Math.random() - 0.5) * 3);
			}
			case 1 -> { // Water
				if (!living.level.isClientSide && !living.level.dimensionType().ultraWarm()) {
					living.level.setBlockAndUpdate(living.blockPosition(), Blocks.WATER.defaultBlockState());
				}
			}
			case 2 -> { // Set on Fire
				if (!living.level.isClientSide) {
					living.setSecondsOnFire(4);
				}
			}
			case 3 -> { // Mini Explosion
				if (!living.level.isClientSide) {
					living.level.explode(null, living.getX(), living.getY(),
							living.getZ(), 0.25F, Explosion.BlockInteraction.NONE);
				}
			}
			case 4 -> { // Mega Jump
				if (!living.level.dimensionType().ultraWarm()) {
					if (!living.level.isClientSide) {
						living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 5));
					}
					living.setDeltaMovement(living.getDeltaMovement().x(), 6, living.getDeltaMovement().z());
				}

			}
			case 5 -> { // Randomly set HP
				if (!living.level.isClientSide) {
					float nextHealth = (float) (Math.random() * living.getMaxHealth());
					if (Mth.equal(nextHealth, 0.0F)) {
						nextHealth = 0.5F;
					}
					living.setHealth(nextHealth);
				}
			}
			case 6 -> { // Lots O' Hearts
				if (!living.level.isClientSide) {
					living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 9));
				}
			}
			case 7 -> { // All your inventory is belong to us
				if (!living.level.isClientSide && living instanceof Player player) {
					player.getInventory().dropAll();
				}

			}
			case 8 -> { // Break your neck
				living.setXRot((float) Math.random() * 360F);
				living.setYRot((float) Math.random() * 180F);

			}
			case 9 -> { // Highest Possible
				int x = Mth.floor(living.getX());
				int z = Mth.floor(living.getZ());
				for (int i = living.level.getMaxBuildHeight(); i > living.level.getMinBuildHeight(); i--) {
					BlockState state = living.level.getBlockState(new BlockPos(x, i, z));
					if (!state.isAir()) {
						living.teleportTo(living.getX(), i, living.getZ());
						break;
					}
				}

			}
			case 10 -> { // HYPERSPEEEEEED
				if (!living.level.isClientSide) {
					living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 200));
				}
			}
			case 11 -> { // Night Vision
				if (!living.level.isClientSide) {
					living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0));
				}
			}
			case 12 -> { // ???
				if (!living.level.isClientSide) {
					// todo 1.16 pick something new
				}
			}
			case 13 -> { // Pixie Friend
				if (!living.level.isClientSide) {
					PixieEntity pixie = new PixieEntity(living.level);
					pixie.setPos(living.getX(), living.getY() + 1.5, living.getZ());
					living.level.addFreshEntity(pixie);
				}
			}
			case 14 -> { // Nausea + Blindness :3
				if (!living.level.isClientSide) {
					living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 3));
					living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 160, 0));
				}

			}
			case 15 -> { // Drop own Head
				if (!living.level.isClientSide && living instanceof Player player) {
					living.hurt(DamageSource.MAGIC, living.getHealth() - 1);
					ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
					ItemNBTHelper.setString(skull, "SkullOwner", player.getGameProfile().getName());
					living.spawnAtLocation(skull, 0);
				}
			}
		}
	}

	private long getSeed(ItemStack stack) {
		long seed = ItemNBTHelper.getLong(stack, TAG_SEED, -1);
		if (seed == -1) {
			return randomSeed(stack);
		}
		return seed;
	}

	private long randomSeed(ItemStack stack) {
		long seed = Math.abs(ThreadLocalRandom.current().nextLong());
		ItemNBTHelper.setLong(stack, TAG_SEED, seed);
		return seed;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> stacks, TooltipFlag flags) {
		stacks.add(Component.translatable("botaniamisc.bottleTooltip"));
	}

	@NotNull
	@Override
	public ItemStack finishUsingItem(@NotNull ItemStack stack, Level world, LivingEntity living) {
		effect(stack, living);
		int left = getSwigsLeft(stack);
		if (left <= 1) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			setSwigsLeft(stack, left - 1);
			randomSeed(stack);
			return stack;
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 20;
	}

	@NotNull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	public static int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, SWIGS);
	}

	private void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}

}
