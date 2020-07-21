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
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Random;

public class ItemBottledMana extends Item {
	public static final int SWIGS = 6;
	private static final String TAG_SWIGS_LEFT = "swigsLeft";
	private static final String TAG_SEED = "randomSeed";

	public ItemBottledMana(Settings props) {
		super(props);
	}

	public void effect(ItemStack stack, LivingEntity living, int id) {
		switch (id) {
		case 0: { // Random motion
			living.setVelocity((Math.random() - 0.5) * 3, living.getVelocity().getY(),
					(Math.random() - 0.5) * 3);
			break;
		}
		case 1: { // Water
			if (!living.world.isClient && !living.world.getDimension().isUltrawarm()) {
				living.world.setBlockState(living.getBlockPos(), Blocks.WATER.getDefaultState());
			}
			break;
		}
		case 2: { // Set on Fire
			if (!living.world.isClient) {
				living.setOnFireFor(4);
			}
			break;
		}
		case 3: { // Mini Explosion
			if (!living.world.isClient) {
				living.world.createExplosion(null, living.getX(), living.getY(),
						living.getZ(), 0.25F, Explosion.DestructionType.NONE);
			}
			break;
		}
		case 4: { // Mega Jump
			if (!living.world.getDimension().isUltrawarm()) {
				if (!living.world.isClient) {
					living.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 5));
				}
				living.setVelocity(living.getVelocity().getX(), 6, living.getVelocity().getZ());
			}

			break;
		}
		case 5: { // Randomly set HP
			if (!living.world.isClient) {
				living.setHealth(living.world.random.nextInt(19) + 1);
			}
			break;
		}
		case 6: { // Lots O' Hearts
			if (!living.world.isClient) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 60 * 2, 9));
			}
			break;
		}
		case 7: { // All your inventory is belong to us
			if (!living.world.isClient && living instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) living;
				for (int i = 0; i < player.inventory.size(); i++) {
					ItemStack stackAt = player.inventory.getStack(i);
					if (stackAt != stack) {
						if (!stackAt.isEmpty()) {
							player.dropStack(stackAt, 0);
						}
						player.inventory.setStack(i, ItemStack.EMPTY);
					}
				}
			}

			break;
		}
		case 8: { // Break your neck
			living.pitch = (float) Math.random() * 360F;
			living.yaw = (float) Math.random() * 180F;

			break;
		}
		case 9: { // Highest Possible
			int x = MathHelper.floor(living.getX());
			int z = MathHelper.floor(living.getZ());
			for (int i = 256; i > 0; i--) {
				Block block = living.world.getBlockState(new BlockPos(x, i, z)).getBlock();
				if (!block.isAir(living.world.getBlockState(new BlockPos(x, i, z)), living.world, new BlockPos(x, i, z))) {
					if (living instanceof ServerPlayerEntity) {
						ServerPlayerEntity mp = (ServerPlayerEntity) living;
						mp.networkHandler.requestTeleport(living.getX(), i, living.getZ(), living.yaw, living.pitch);
					}
					break;
				}
			}

			break;
		}
		case 10: { // HYPERSPEEEEEED
			if (!living.world.isClient) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 200));
			}
			break;
		}
		case 11: { // Night Vision
			if (!living.world.isClient) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 6000, 0));
			}
			break;
		}
		case 12: { // ???
			if (!living.world.isClient) {
				// todo 1.16 pick something new
			}

			break;
		}
		case 13: { // Pixie Friend
			if (!living.world.isClient) {
				EntityPixie pixie = new EntityPixie(living.world);
				pixie.updatePosition(living.getX(), living.getY() + 1.5, living.getZ());
				living.world.spawnEntity(pixie);
			}
			break;
		}
		case 14: { // Nausea + Blindness :3
			if (!living.world.isClient) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 160, 3));
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 160, 0));
			}

			break;
		}
		case 15: { // Drop own Head
			if (!living.world.isClient && living instanceof PlayerEntity) {
				living.damage(DamageSource.MAGIC, living.getHealth() - 1);
				ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
				ItemNBTHelper.setString(skull, "SkullOwner", ((PlayerEntity) living).getGameProfile().getName());
				living.dropStack(skull, 0);
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
		long seed = Math.abs(RANDOM.nextLong());
		ItemNBTHelper.setLong(stack, TAG_SEED, seed);
		return seed;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> stacks, TooltipContext flags) {
		stacks.add(new TranslatableText("botaniamisc.bottleTooltip"));
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setCurrentHand(hand);
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	@Nonnull
	@Override
	public ItemStack finishUsing(@Nonnull ItemStack stack, World world, LivingEntity living) {
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
	public int getMaxUseTime(ItemStack stack) {
		return 20;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	public static int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, SWIGS);
	}

	private void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}

}
