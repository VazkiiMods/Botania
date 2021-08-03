/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.network.PacketBotaniaEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye(Properties props) {
		super(props);
	}

	private static final String TAG_TARGET_PREFIX = "target_";

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Player player = ctx.getPlayer();

		if (player != null && player.isShiftKeyDown()) {
			if (world.isClientSide) {
				for (int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.05F - (float) Math.random() * 0.05F, 0);
				}
			} else {
				ItemStack stack = ctx.getItemInHand();
				Tag nbt = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).get().orThrow();
				ItemNBTHelper.set(stack, TAG_TARGET_PREFIX + world.dimension().location().toString(), nbt);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 5F);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int count) {
		if (world.isClientSide) {
			float x = (float) (living.getX() - Math.random() * living.getBbWidth());
			float y = (float) (living.getY() + Math.random());
			float z = (float) (living.getZ() - Math.random() * living.getBbWidth());
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
			world.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
		}
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Nonnull
	@Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level world, LivingEntity living) {
		String tag = TAG_TARGET_PREFIX + world.dimension().location().toString();
		Tag nbt = ItemNBTHelper.get(stack, tag);
		if (nbt == null) {
			return stack;
		}
		Optional<BlockPos> maybeLoc = BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt).result();
		if (!maybeLoc.isPresent()) {
			ItemNBTHelper.removeEntry(stack, tag);
			return stack;
		}
		BlockPos loc = maybeLoc.get();
		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, living.getX(), living.getY(), living.getZ()) * 10);

		if (!(living instanceof Player) || ManaItemHandler.instance().requestManaExact(stack, (Player) living, cost, true)) {
			moveParticlesAndSound(living);
			living.teleportTo(x + 0.5, y + 1.5, z + 0.5);
			moveParticlesAndSound(living);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketBotaniaEffect.sendNearby(entity, PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.getX(), entity.getY(), entity.getZ(), entity.getId());
		entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Nullable
	@Override
	public BlockPos getBinding(Level world, ItemStack stack) {
		String tag = TAG_TARGET_PREFIX + world.dimension().location().toString();
		Tag nbt = ItemNBTHelper.get(stack, tag);
		if (nbt != null) {
			return BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt).result()
					.orElse(null);
		}
		return null;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);

		if (world == null) {
			return;
		}

		BlockPos binding = getBinding(world, stack);
		Component worldText = new TextComponent(world.dimension().location().toString()).withStyle(ChatFormatting.GREEN);

		if (binding == null) {
			tooltip.add(new TranslatableComponent("botaniamisc.flugelUnbound", worldText).withStyle(ChatFormatting.GRAY));
		} else {
			Component bindingText = new TextComponent("[").withStyle(ChatFormatting.WHITE)
					.append(new TextComponent(Integer.toString(binding.getX())).withStyle(ChatFormatting.GOLD))
					.append(", ")
					.append(new TextComponent(Integer.toString(binding.getY())).withStyle(ChatFormatting.GOLD))
					.append(", ")
					.append(new TextComponent(Integer.toString(binding.getZ())).withStyle(ChatFormatting.GOLD))
					.append("]");

			tooltip.add(new TranslatableComponent("botaniamisc.flugelBound", bindingText, worldText).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return prefix("challenge/flugel_eye");
	}

}
