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
import net.minecraft.block.NoteBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.mixin.AccessorMobEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemCacophonium extends Item {

	private static final String TAG_SOUND = "sound";
	private static final String TAG_SOUND_NAME = "soundName";

	public ItemCacophonium(Settings props) {
		super(props);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		if (entity instanceof MobEntity) {
			MobEntity living = (MobEntity) entity;
			SoundEvent sound = null;

			if (living instanceof CreeperEntity) {
				sound = SoundEvents.ENTITY_CREEPER_PRIMED;
			} else if (living instanceof SlimeEntity) {
				sound = ((SlimeEntity) living).isSmall() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
			} else {
				sound = ((AccessorMobEntity) living).callGetAmbientSound();
			}

			if (sound != null) {
				if (!player.world.isClient) {
					ItemNBTHelper.setString(stack, TAG_SOUND, Registry.SOUND_EVENT.getKey(sound).toString());
					ItemNBTHelper.setString(stack, TAG_SOUND_NAME, entity.getType().getTranslationKey());
					player.setStackInHand(hand, stack);
				}

				return ActionResult.success(player.world.isClient);
			}
		}

		return ActionResult.PASS;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		ItemStack stack = ctx.getStack();
		if (getSound(stack) != null) {
			World world = ctx.getWorld();
			BlockPos pos = ctx.getBlockPos();

			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof NoteBlock) {
				world.setBlockState(pos, ModBlocks.cacophonium.getDefaultState());
				((TileCacophonium) world.getBlockEntity(pos)).stack = stack.copy();
				stack.decrement(1);
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		if (isDOIT(stack)) {
			list.add(new TranslatableText("botaniamisc.justDoIt").formatted(Formatting.GRAY));
		} else if (getSound(stack) != null) {
			list.add(new TranslatableText(ItemNBTHelper.getString(stack, TAG_SOUND_NAME, "")).formatted(Formatting.GRAY));
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (getSound(stack) != null) {
			player.setCurrentHand(hand);
		}
		return TypedActionResult.consume(stack);
	}

	@Override
	public void usageTick(World world, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int count) {
		if (!world.isClient && count % (isDOIT(stack) ? 20 : 6) == 0) {
			playSound(player.world, stack, player.getX(), player.getY(), player.getZ(), SoundCategory.PLAYERS, 0.9F);
		}
	}

	public static void playSound(World world, ItemStack stack, double x, double y, double z, SoundCategory category, float volume) {
		if (stack.isEmpty()) {
			return;
		}

		SoundEvent sound = getSound(stack);

		if (sound != null) {
			world.playSound(null, x, y, z, sound, category, volume, sound == ModSounds.doit ? 1F : (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
		}
	}

	@Nullable
	private static SoundEvent getSound(ItemStack stack) {
		if (isDOIT(stack)) {
			return ModSounds.doit;
		} else {
			try {
				return Registry.SOUND_EVENT.getOrEmpty(new Identifier(ItemNBTHelper.getString(stack, TAG_SOUND, ""))).orElse(null);
			} catch (InvalidIdentifierException ex) {
				return null;
			}
		}
	}

	private static boolean isDOIT(ItemStack stack) {
		return !stack.isEmpty() && stack.getName().getString().equalsIgnoreCase("shia labeouf");
	}
}
