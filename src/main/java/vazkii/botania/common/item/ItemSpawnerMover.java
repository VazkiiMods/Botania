/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemSpawnerMover extends Item {

	private static final String TAG_SPAWNER = "spawner";
	private static final String TAG_SPAWN_DATA = "SpawnData";
	private static final String TAG_ID = "id";

	public ItemSpawnerMover(Properties props) {
		super(props);
	}

	@Nullable
	private static ResourceLocation getEntityId(ItemStack stack) {
		CompoundTag tag = stack.getTagElement(TAG_SPAWNER);
		if (tag != null && tag.contains(TAG_SPAWN_DATA)) {
			tag = tag.getCompound(TAG_SPAWN_DATA);
			if (tag.contains(TAG_ID)) {
				return ResourceLocation.tryParse(tag.getString(TAG_ID));
			}
		}

		return null;
	}

	public static boolean hasData(ItemStack stack) {
		return getEntityId(stack) != null;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> infoList, TooltipFlag flags) {
		ResourceLocation id = getEntityId(stack);
		if (id != null) {
			Registry.ENTITY_TYPE.getOptional(id).ifPresent(type -> infoList.add(type.getDescription()));
		}
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		if (getEntityId(ctx.getItemInHand()) == null) {
			return captureSpawner(ctx) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		} else {
			return placeSpawner(ctx);
		}
	}

	private InteractionResult placeSpawner(UseOnContext ctx) {
		ItemStack useStack = new ItemStack(Blocks.SPAWNER);
		Pair<InteractionResult, BlockPos> res = PlayerHelper.substituteUseTrackPos(ctx, useStack);

		if (res.getFirst().consumesAction()) {
			Level world = ctx.getLevel();
			BlockPos pos = res.getSecond();
			ItemStack mover = ctx.getItemInHand();

			if (!world.isClientSide) {
				if (ctx.getPlayer() != null) {
					ctx.getPlayer().broadcastBreakEvent(ctx.getHand());
				}
				mover.shrink(1);

				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof SpawnerBlockEntity) {
					CompoundTag spawnerTag = ctx.getItemInHand().getTagElement(TAG_SPAWNER).copy();
					spawnerTag.putInt("x", pos.getX());
					spawnerTag.putInt("y", pos.getY());
					spawnerTag.putInt("z", pos.getZ());
					te.load(spawnerTag);
				}
			} else {
				for (int i = 0; i < 100; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle(0.45F + 0.2F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 6);
					world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
				}
			}
		}

		return res.getFirst();
	}

	private boolean captureSpawner(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();
		Player player = ctx.getPlayer();

		if (world.getBlockState(pos).is(Blocks.SPAWNER)) {
			if (!world.isClientSide) {
				BlockEntity te = world.getBlockEntity(pos);
				stack.getOrCreateTag().put(TAG_SPAWNER, te.saveWithFullMetadata());
				world.destroyBlock(pos, false);
				if (player != null) {
					player.getCooldowns().addCooldown(this, 20);
					UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayer) player, stack, (ServerLevel) world, pos.getX(), pos.getY(), pos.getZ());
					player.broadcastBreakEvent(ctx.getHand());
				}
			} else {
				for (int i = 0; i < 50; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.1F + 0.05F, red, green, blue);
					world.addParticle(data, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F);
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
