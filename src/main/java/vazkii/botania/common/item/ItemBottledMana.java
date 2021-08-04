/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class ItemBottledMana extends Item {
	public static final int SWIGS = 6;
	private static final String TAG_SWIGS_LEFT = "swigsLeft";
	private static final String TAG_SEED = "randomSeed";

	public ItemBottledMana(Properties props) {
		super(props);
	}

	public void effect(ItemStack stack, LivingEntity living, int id) {
		switch (id) {
		case 0: { // Random motion
			living.setDeltaMovement((Math.random() - 0.5) * 3, living.getDeltaMovement().y(),
					(Math.random() - 0.5) * 3);
			break;
		}
		case 1: { // Water
			if (!living.level.isClientSide && !living.level.dimensionType().ultraWarm()) {
				living.level.setBlockAndUpdate(living.blockPosition(), Blocks.WATER.defaultBlockState());
			}
			break;
		}
		case 2: { // Set on Fire
			if (!living.level.isClientSide) {
				living.setSecondsOnFire(4);
			}
			break;
		}
		case 3: { // Mini Explosion
			if (!living.level.isClientSide) {
				living.level.explode(null, living.getX(), living.getY(),
						living.getZ(), 0.25F, Explosion.BlockInteraction.NONE);
			}
			break;
		}
		case 4: { // Mega Jump
			if (!living.level.dimensionType().ultraWarm()) {
				if (!living.level.isClientSide) {
					living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 5));
				}
				living.setDeltaMovement(living.getDeltaMovement().x(), 6, living.getDeltaMovement().z());
			}

			break;
		}
		case 5: { // Randomly set HP
			if (!living.level.isClientSide) {
				living.setHealth(living.level.random.nextInt(19) + 1);
			}
			break;
		}
		case 6: { // Lots O' Hearts
			if (!living.level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 9));
			}
			break;
		}
		case 7: { // All your inventory is belong to us
			if (!living.level.isClientSide && living instanceof Player) {
				Player player = (Player) living;
				for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
					ItemStack stackAt = player.getInventory().getItem(i);
					if (stackAt != stack) {
						if (!stackAt.isEmpty()) {
							player.spawnAtLocation(stackAt, 0);
						}
						player.getInventory().setItem(i, ItemStack.EMPTY);
					}
				}
			}

			break;
		}
		case 8: { // Break your neck
			living.xRot = (float) Math.random() * 360F;
			living.yRot = (float) Math.random() * 180F;

			break;
		}
		case 9: { // Highest Possible
			int x = Mth.floor(living.getX());
			int z = Mth.floor(living.getZ());
			for (int i = 256; i > 0; i--) {
				BlockState state = living.level.getBlockState(new BlockPos(x, i, z));
				if (!state.isAir()) {
					if (living instanceof ServerPlayer) {
						ServerPlayer mp = (ServerPlayer) living;
						mp.connection.teleport(living.getX(), i, living.getZ(), living.yRot, living.xRot);
					}
					break;
				}
			}

			break;
		}
		case 10: { // HYPERSPEEEEEED
			if (!living.level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 200));
			}
			break;
		}
		case 11: { // Night Vision
			if (!living.level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0));
			}
			break;
		}
		case 12: { // ???
			if (!living.level.isClientSide) {
				// todo 1.16 pick something new
			}

			break;
		}
		case 13: { // Pixie Friend
			if (!living.level.isClientSide) {
				EntityPixie pixie = new EntityPixie(living.level);
				pixie.setPos(living.getX(), living.getY() + 1.5, living.getZ());
				living.level.addFreshEntity(pixie);
			}
			break;
		}
		case 14: { // Nausea + Blindness :3
			if (!living.level.isClientSide) {
				living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 3));
				living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 160, 0));
			}

			break;
		}
		case 15: { // Drop own Head
			if (!living.level.isClientSide && living instanceof Player) {
				living.hurt(DamageSource.MAGIC, living.getHealth() - 1);
				ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
				ItemNBTHelper.setString(skull, "SkullOwner", ((Player) living).getGameProfile().getName());
				living.spawnAtLocation(skull, 0);
			}
			break;
		}
		}
	}

	private void randomEffect(LivingEntity player, ItemStack stack) {
		effect(stack, player, new Random(getSeed(stack)).nextInt(16));
	}

	private long getSeed(ItemStack stack) {
		long seed = ItemNBTHelper.getLong(stack, TAG_SEED, -1);
		if (seed == -1) {
			return randomSeed(stack);
		}
		return seed;
	}

	private long randomSeed(ItemStack stack) {
		long seed = Math.abs(random.nextLong());
		ItemNBTHelper.setLong(stack, TAG_SEED, seed);
		return seed;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> stacks, TooltipFlag flags) {
		stacks.add(new TranslatableComponent("botaniamisc.bottleTooltip"));
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.useDrink(world, player, hand);
	}

	@Nonnull
	@Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level world, LivingEntity living) {
		randomEffect(living, stack);
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

	@Nonnull
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
