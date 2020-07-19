/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

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
			living.setMotion((Math.random() - 0.5) * 3, living.getMotion().getY(),
					(Math.random() - 0.5) * 3);
			break;
		}
		case 1: { // Water
			if (!living.world.isRemote && !living.world.func_230315_m_().func_236040_e_()) {
				living.world.setBlockState(living.func_233580_cy_(), Blocks.WATER.getDefaultState());
			}
			break;
		}
		case 2: { // Set on Fire
			if (!living.world.isRemote) {
				living.setFire(4);
			}
			break;
		}
		case 3: { // Mini Explosion
			if (!living.world.isRemote) {
				living.world.createExplosion(null, living.getPosX(), living.getPosY(),
						living.getPosZ(), 0.25F, Explosion.Mode.NONE);
			}
			break;
		}
		case 4: { // Mega Jump
			if (!living.world.func_230315_m_().func_236040_e_()) {
				if (!living.world.isRemote) {
					living.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 300, 5));
				}
				living.setMotion(living.getMotion().getX(), 6, living.getMotion().getZ());
			}

			break;
		}
		case 5: { // Randomly set HP
			if (!living.world.isRemote) {
				living.setHealth(living.world.rand.nextInt(19) + 1);
			}
			break;
		}
		case 6: { // Lots O' Hearts
			if (!living.world.isRemote) {
				living.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20 * 60 * 2, 9));
			}
			break;
		}
		case 7: { // All your inventory is belong to us
			if (!living.world.isRemote && living instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) living;
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack stackAt = player.inventory.getStackInSlot(i);
					if (stackAt != stack) {
						if (!stackAt.isEmpty()) {
							player.entityDropItem(stackAt, 0);
						}
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}
				}
			}

			break;
		}
		case 8: { // Break your neck
			living.rotationPitch = (float) Math.random() * 360F;
			living.rotationYaw = (float) Math.random() * 180F;

			break;
		}
		case 9: { // Highest Possible
			int x = MathHelper.floor(living.getPosX());
			int z = MathHelper.floor(living.getPosZ());
			for (int i = 256; i > 0; i--) {
				Block block = living.world.getBlockState(new BlockPos(x, i, z)).getBlock();
				if (!block.isAir(living.world.getBlockState(new BlockPos(x, i, z)), living.world, new BlockPos(x, i, z))) {
					if (living instanceof ServerPlayerEntity) {
						ServerPlayerEntity mp = (ServerPlayerEntity) living;
						mp.connection.setPlayerLocation(living.getPosX(), i, living.getPosZ(), living.rotationYaw, living.rotationPitch);
					}
					break;
				}
			}

			break;
		}
		case 10: { // HYPERSPEEEEEED
			if (!living.world.isRemote) {
				living.addPotionEffect(new EffectInstance(Effects.SPEED, 60, 200));
			}
			break;
		}
		case 11: { // Night Vision
			if (!living.world.isRemote) {
				living.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 6000, 0));
			}
			break;
		}
		case 12: { // ???
			if (!living.world.isRemote) {
				// todo 1.16 pick something new
			}

			break;
		}
		case 13: { // Pixie Friend
			if (!living.world.isRemote) {
				EntityPixie pixie = new EntityPixie(living.world);
				pixie.setPosition(living.getPosX(), living.getPosY() + 1.5, living.getPosZ());
				living.world.addEntity(pixie);
			}
			break;
		}
		case 14: { // Nausea + Blindness :3
			if (!living.world.isRemote) {
				living.addPotionEffect(new EffectInstance(Effects.NAUSEA, 160, 3));
				living.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 160, 0));
			}

			break;
		}
		case 15: { // Drop own Head
			if (!living.world.isRemote && living instanceof PlayerEntity) {
				living.attackEntityFrom(DamageSource.MAGIC, living.getHealth() - 1);
				ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
				ItemNBTHelper.setString(skull, "SkullOwner", ((PlayerEntity) living).getGameProfile().getName());
				living.entityDropItem(skull, 0);
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		stacks.add(new TranslationTextComponent("botaniamisc.bottleTooltip"));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
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
