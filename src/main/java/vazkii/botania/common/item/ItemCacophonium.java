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
import net.minecraft.block.NoteBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

	public ItemCacophonium(Properties props) {
		super(props);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		if (entity instanceof MobEntity) {
			MobEntity living = (MobEntity) entity;
			SoundEvent sound = null;

			if (living instanceof CreeperEntity) {
				sound = SoundEvents.ENTITY_CREEPER_PRIMED;
			} else if (living instanceof SlimeEntity) {
				sound = ((SlimeEntity) living).isSmallSlime() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
			} else {
				sound = ((AccessorMobEntity) living).callGetAmbientSound();
			}

			if (sound != null) {
				ItemNBTHelper.setString(stack, TAG_SOUND, Registry.SOUND_EVENT.getKey(sound).toString());
				ItemNBTHelper.setString(stack, TAG_SOUND_NAME, entity.getType().getTranslationKey());
				player.setHeldItem(hand, stack);

				if (player.world.isRemote) {
					player.swingArm(hand);
				}

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		if (getSound(stack) != null) {
			World world = ctx.getWorld();
			BlockPos pos = ctx.getPos();

			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof NoteBlock) {
				world.setBlockState(pos, ModBlocks.cacophonium.getDefaultState());
				((TileCacophonium) world.getTileEntity(pos)).stack = stack.copy();
				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		if (isDOIT(stack)) {
			list.add(new TranslationTextComponent("botaniamisc.justDoIt").func_240699_a_(TextFormatting.GRAY));
		} else if (getSound(stack) != null) {
			list.add(new TranslationTextComponent(ItemNBTHelper.getString(stack, TAG_SOUND_NAME, "")).func_240699_a_(TextFormatting.GRAY));
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (getSound(stack) != null) {
			player.setActiveHand(hand);
		}
		return ActionResult.resultSuccess(stack);
	}

	@Override
	public void onUse(World world, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int count) {
		if (!world.isRemote && count % (isDOIT(stack) ? 20 : 6) == 0) {
			playSound(player.world, stack, player.getPosX(), player.getPosY(), player.getPosZ(), SoundCategory.PLAYERS, 0.9F);
		}
	}

	public static void playSound(World world, ItemStack stack, double x, double y, double z, SoundCategory category, float volume) {
		if (stack.isEmpty()) {
			return;
		}

		SoundEvent sound = getSound(stack);

		if (sound != null) {
			world.playSound(null, x, y, z, sound, category, volume, sound == ModSounds.doit ? 1F : (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
		}
	}

	@Nullable
	private static SoundEvent getSound(ItemStack stack) {
		if (isDOIT(stack)) {
			return ModSounds.doit;
		} else {
			try {
				return Registry.SOUND_EVENT.getValue(new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SOUND, ""))).orElse(null);
			} catch (ResourceLocationException ex) {
				return null;
			}
		}
	}

	private static boolean isDOIT(ItemStack stack) {
		return !stack.isEmpty() && stack.getDisplayName().getString().equalsIgnoreCase("shia labeouf");
	}
}
