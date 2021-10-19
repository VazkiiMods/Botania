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
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.mixin.AccessorMob;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemCacophonium extends Item {

	private static final String TAG_SOUND = "sound";
	private static final String TAG_SOUND_NAME = "soundName";

	public ItemCacophonium(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Mob living) {
			SoundEvent sound = null;

			if (living instanceof Creeper) {
				sound = SoundEvents.CREEPER_PRIMED;
			} else if (living instanceof Slime) {
				sound = ((Slime) living).isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
			} else {
				sound = ((AccessorMob) living).botania_getAmbientSound();
			}

			if (sound != null) {
				if (!player.level.isClientSide) {
					ItemNBTHelper.setString(stack, TAG_SOUND, Registry.SOUND_EVENT.getKey(sound).toString());
					ItemNBTHelper.setString(stack, TAG_SOUND_NAME, entity.getType().getDescriptionId());
					player.setItemInHand(hand, stack);
				}

				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
		}

		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		if (getSound(stack) != null) {
			Level world = ctx.getLevel();
			BlockPos pos = ctx.getClickedPos();

			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof NoteBlock) {
				world.setBlockAndUpdate(pos, ModBlocks.cacophonium.defaultBlockState());
				((TileCacophonium) world.getBlockEntity(pos)).stack = stack.copy();
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		if (isDOIT(stack)) {
			list.add(new TranslatableComponent("botaniamisc.justDoIt").withStyle(ChatFormatting.GRAY));
		} else if (getSound(stack) != null) {
			list.add(new TranslatableComponent(ItemNBTHelper.getString(stack, TAG_SOUND_NAME, "")).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (getSound(stack) != null) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public void onUseTick(Level world, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int count) {
		if (!world.isClientSide && count % (isDOIT(stack) ? 20 : 6) == 0) {
			playSound(player.level, stack, player.getX(), player.getY(), player.getZ(), SoundSource.PLAYERS, 0.9F);
		}
	}

	public static void playSound(Level world, ItemStack stack, double x, double y, double z, SoundSource category, float volume) {
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
				return Registry.SOUND_EVENT.getOptional(new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SOUND, ""))).orElse(null);
			} catch (ResourceLocationException ex) {
				return null;
			}
		}
	}

	private static boolean isDOIT(ItemStack stack) {
		return !stack.isEmpty() && stack.getHoverName().getString().equalsIgnoreCase("shia labeouf");
	}
}
