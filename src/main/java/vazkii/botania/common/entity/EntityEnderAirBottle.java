/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@OnlyIn(
	value = Dist.CLIENT,
	_interface = IRendersAsItem.class
)
public class EntityEnderAirBottle extends ThrowableEntity implements IRendersAsItem {
	private static final ResourceLocation GHAST_LOOT_TABLE = prefix("ghast_ender_air_crying");

	public EntityEnderAirBottle(EntityType<EntityEnderAirBottle> type, World world) {
		super(type, world);
	}

	public EntityEnderAirBottle(LivingEntity entity, World world) {
		super(ModEntities.ENDER_AIR_BOTTLE, entity, world);
	}

	public EntityEnderAirBottle(double x, double y, double z, World world) {
		super(ModEntities.ENDER_AIR_BOTTLE, x, y, z, world);
	}

	private void convertStone(@Nonnull BlockPos pos) {
		List<BlockPos> coordsList = getCoordsToPut(pos);
		world.playEvent(2002, getPosition(), 8);

		for (BlockPos coords : coordsList) {
			world.setBlockState(coords, Blocks.END_STONE.getDefaultState());
			if (Math.random() < 0.1) {
				world.playEvent(2001, coords, Block.getStateId(Blocks.END_STONE.getDefaultState()));
			}
		}
	}

	@Override
	protected void func_230299_a_(@Nonnull BlockRayTraceResult result) {
		if (world.isRemote) {
			return;
		}
		convertStone(result.getPos());
		remove();
	}

	@Override
	protected void onEntityHit(@Nonnull EntityRayTraceResult result) {
		if (world.isRemote) {
			return;
		}
		Entity entity = result.getEntity();
		if (entity.getType() == EntityType.GHAST && world.getDimensionKey() == World.OVERWORLD) {
			world.playEvent(2002, getPosition(), 8);
			DamageSource source = DamageSource.causeThrownDamage(this, func_234616_v_());
			entity.attackEntityFrom(source, 0);

			LootTable table = world.getServer().getLootTableManager().getLootTableFromLocation(GHAST_LOOT_TABLE);
			LootContext.Builder builder = new LootContext.Builder(((ServerWorld) world));
			builder.withParameter(LootParameters.THIS_ENTITY, entity);
			builder.withParameter(LootParameters.field_237457_g_, entity.getPositionVec());
			builder.withParameter(LootParameters.DAMAGE_SOURCE, source);

			LootContext context = builder.build(LootParameterSets.ENTITY);
			for (ItemStack stack : table.generate(context)) {
				ItemEntity item = entity.entityDropItem(stack, 2);
				item.setMotion(item.getMotion().add(entity.getLookVec().scale(0.4)));
			}
		} else {
			convertStone(new BlockPos(result.getHitVec()));
		}
		remove();
	}

	private List<BlockPos> getCoordsToPut(BlockPos pos) {
		List<BlockPos> possibleCoords = new ArrayList<>();
		int range = 4;
		int rangeY = 4;

		for (BlockPos bPos : BlockPos.getAllInBoxMutable(pos.add(-range, -rangeY, -range),
				pos.add(range, rangeY, range))) {
			BlockState state = world.getBlockState(bPos);
			if (state.getBlock() == Blocks.STONE) {
				possibleCoords.add(bPos.toImmutable());
			}
		}

		Collections.shuffle(possibleCoords, rand);

		return possibleCoords.stream().limit(64).collect(Collectors.toList());
	}

	@Override
	protected void registerData() {}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return new ItemStack(ModItems.enderAirBottle);
	}
}
