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

import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
		CompoundNBT tag = stack.getChildTag(TAG_SPAWNER);
		if (tag != null && tag.contains(TAG_SPAWN_DATA)) {
			tag = tag.getCompound(TAG_SPAWN_DATA);
			if (tag.contains(TAG_ID)) {
				return ResourceLocation.tryCreate(tag.getString(TAG_ID));
			}
		}

		return null;
	}

	public static boolean hasData(ItemStack stack) {
		return getEntityId(stack) != null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> infoList, ITooltipFlag flags) {
		ResourceLocation id = getEntityId(stack);
		if (id != null) {
			Registry.ENTITY_TYPE.getValue(id).ifPresent(type -> infoList.add(type.getName()));
		}
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		if (getEntityId(ctx.getItem()) == null) {
			return captureSpawner(ctx) ? ActionResultType.SUCCESS : ActionResultType.PASS;
		} else {
			return placeSpawner(ctx);
		}
	}

	private ActionResultType placeSpawner(ItemUseContext ctx) {
		ItemStack useStack = new ItemStack(Blocks.SPAWNER);
		Pair<ActionResultType, BlockPos> res = PlayerHelper.substituteUseTrackPos(ctx, useStack);

		if (res.getFirst() == ActionResultType.SUCCESS) {
			World world = ctx.getWorld();
			BlockPos pos = res.getSecond();
			ItemStack mover = ctx.getItem();

			if (!world.isRemote) {
				if (ctx.getPlayer() != null) {
					ctx.getPlayer().sendBreakAnimation(ctx.getHand());
				}
				mover.shrink(1);

				TileEntity te = world.getTileEntity(pos);
				if (te instanceof MobSpawnerTileEntity) {
					CompoundNBT spawnerTag = ctx.getItem().getChildTag(TAG_SPAWNER).copy();
					spawnerTag.putInt("x", pos.getX());
					spawnerTag.putInt("y", pos.getY());
					spawnerTag.putInt("z", pos.getZ());
					te.func_230337_a_(world.getBlockState(pos), spawnerTag);
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

	private boolean captureSpawner(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		ItemStack stack = ctx.getItem();
		PlayerEntity player = ctx.getPlayer();

		if (world.getBlockState(pos).getBlock() == Blocks.SPAWNER) {
			if (!world.isRemote) {
				TileEntity te = world.getTileEntity(pos);
				stack.getOrCreateTag().put(TAG_SPAWNER, te.write(new CompoundNBT()));
				world.destroyBlock(pos, false);
				if (player != null) {
					player.getCooldownTracker().setCooldown(this, 20);
					UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack, (ServerWorld) world, pos.getX(), pos.getY(), pos.getZ());
					player.sendBreakAnimation(ctx.getHand());
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
