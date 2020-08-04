/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.Tag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye(Settings props) {
		super(props);
	}

	private static final String TAG_TARGET_PREFIX = "target_";

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		PlayerEntity player = ctx.getPlayer();

		if (player != null && player.isSneaking()) {
			if (world.isClient) {
				for (int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
					world.addParticle(data, x1, y1, z1, 0, 0.05F - (float) Math.random() * 0.05F, 0);
				}
			} else {
				ItemStack stack = ctx.getStack();
				Tag nbt = BlockPos.field_25064.encodeStart(NbtOps.INSTANCE, pos).get().orThrow();
				ItemNBTHelper.set(stack, TAG_TARGET_PREFIX + world.getRegistryKey().getValue().toString(), nbt);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 5F);
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void usageTick(World world, LivingEntity living, ItemStack stack, int count) {
		if (world.isClient) {
			float x = (float) (living.getX() - Math.random() * living.getWidth());
			float y = (float) (living.getY() + Math.random());
			float z = (float) (living.getZ() - Math.random() * living.getWidth());
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
			world.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
		}
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setCurrentHand(hand);
		return TypedActionResult.consume(player.getStackInHand(hand));
	}

	@Nonnull
	@Override
	public ItemStack finishUsing(@Nonnull ItemStack stack, World world, LivingEntity living) {
		String tag = TAG_TARGET_PREFIX + world.getRegistryKey().getValue().toString();
		Tag nbt = ItemNBTHelper.get(stack, tag);
		if (nbt == null) {
			return stack;
		}
		Optional<BlockPos> maybeLoc = BlockPos.field_25064.parse(NbtOps.INSTANCE, nbt).result();
		if (!maybeLoc.isPresent()) {
			ItemNBTHelper.removeEntry(stack, tag);
			return stack;
		}
		BlockPos loc = maybeLoc.get();
		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, living.getX(), living.getY(), living.getZ()) * 10);

		if (!(living instanceof PlayerEntity) || ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) living, cost, true)) {
			moveParticlesAndSound(living);
			living.requestTeleport(x + 0.5, y + 1.5, z + 0.5);
			moveParticlesAndSound(living);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketHandler.sendToNearby(entity.world, entity, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.getX(), entity.getY(), entity.getZ(), entity.getEntityId()));
		entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 40;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Nullable
	@Override
	public BlockPos getBinding(World world, ItemStack stack) {
		String tag = TAG_TARGET_PREFIX + world.getRegistryKey().getValue().toString();
		Tag nbt = ItemNBTHelper.get(stack, tag);
		if (nbt != null) {
			return BlockPos.field_25064.parse(NbtOps.INSTANCE, nbt).result()
					.orElse(null);
		}
		return null;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addInformationAfterShift(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext flags) {
		super.addInformationAfterShift(stack, world, tooltip, flags);

		if (world == null) {
			return;
		}

		BlockPos binding = getBinding(world, stack);
		Text worldText = new LiteralText(world.getRegistryKey().getValue().toString()).formatted(Formatting.GREEN);

		if (binding == null) {
			tooltip.add(new TranslatableText("botaniamisc.flugelUnbound", worldText).formatted(Formatting.GRAY));
		} else {
			Text bindingText = new LiteralText("[").formatted(Formatting.WHITE)
					.append(new LiteralText(Integer.toString(binding.getX())).formatted(Formatting.GOLD))
					.append(", ")
					.append(new LiteralText(Integer.toString(binding.getY())).formatted(Formatting.GOLD))
					.append(", ")
					.append(new LiteralText(Integer.toString(binding.getZ())).formatted(Formatting.GOLD))
					.append("]");

			tooltip.add(new TranslatableText("botaniamisc.flugelBound", bindingText, worldText).formatted(Formatting.GRAY));
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public Identifier getAdvancement() {
		return prefix("challenge/flugel_eye");
	}

}
