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
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

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

	public ItemSpawnerMover(Settings props) {
		super(props);
	}

	@Nullable
	private static Identifier getEntityId(ItemStack stack) {
		CompoundTag tag = stack.getSubTag(TAG_SPAWNER);
		if (tag != null && tag.contains(TAG_SPAWN_DATA)) {
			tag = tag.getCompound(TAG_SPAWN_DATA);
			if (tag.contains(TAG_ID)) {
				return Identifier.tryParse(tag.getString(TAG_ID));
			}
		}

		return null;
	}

	public static boolean hasData(ItemStack stack) {
		return getEntityId(stack) != null;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> infoList, TooltipContext flags) {
		Identifier id = getEntityId(stack);
		if (id != null) {
			Registry.ENTITY_TYPE.getOrEmpty(id).ifPresent(type -> infoList.add(type.getName()));
		}
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		if (getEntityId(ctx.getStack()) == null) {
			return captureSpawner(ctx) ? ActionResult.SUCCESS : ActionResult.PASS;
		} else {
			return placeSpawner(ctx);
		}
	}

	private ActionResult placeSpawner(ItemUsageContext ctx) {
		ItemStack useStack = new ItemStack(Blocks.SPAWNER);
		Pair<ActionResult, BlockPos> res = PlayerHelper.substituteUseTrackPos(ctx, useStack);

		if (res.getFirst().isAccepted()) {
			World world = ctx.getWorld();
			BlockPos pos = res.getSecond();
			ItemStack mover = ctx.getStack();

			if (!world.isClient) {
				if (ctx.getPlayer() != null) {
					ctx.getPlayer().sendToolBreakStatus(ctx.getHand());
				}
				mover.decrement(1);

				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof MobSpawnerBlockEntity) {
					CompoundTag spawnerTag = ctx.getStack().getSubTag(TAG_SPAWNER).copy();
					spawnerTag.putInt("x", pos.getX());
					spawnerTag.putInt("y", pos.getY());
					spawnerTag.putInt("z", pos.getZ());
					te.fromTag(world.getBlockState(pos), spawnerTag);
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

	private boolean captureSpawner(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		ItemStack stack = ctx.getStack();
		PlayerEntity player = ctx.getPlayer();

		if (world.getBlockState(pos).getBlock() == Blocks.SPAWNER) {
			if (!world.isClient) {
				BlockEntity te = world.getBlockEntity(pos);
				stack.getOrCreateTag().put(TAG_SPAWNER, te.toTag(new CompoundTag()));
				world.breakBlock(pos, false);
				if (player != null) {
					player.getItemCooldownManager().set(this, 20);
					UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack, (ServerWorld) world, pos.getX(), pos.getY(), pos.getZ());
					player.sendToolBreakStatus(ctx.getHand());
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
