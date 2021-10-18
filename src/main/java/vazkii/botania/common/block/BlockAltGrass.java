/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import vazkii.botania.client.fx.SparkleParticleData;

import java.util.Random;

public class BlockAltGrass extends BlockMod {

	public enum Variant {
		DRY,
		GOLDEN,
		VIVID,
		SCORCHED,
		INFUSED,
		MUTATED
	}

	private final Variant variant;

	public BlockAltGrass(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack held = player.getItemInHand(hand);
		if (held.getItem() instanceof HoeItem && world.isEmptyBlock(pos.above())) {
			held.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
			world.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (!world.isClientSide && state.is(this) && world.getMaxLocalRawBrightness(pos.above()) >= 9) {
			for (int l = 0; l < 4; ++l) {
				BlockPos pos1 = pos.offset(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
				BlockPos pos1up = pos1.above();

				if (world.getBlockState(pos1).is(Blocks.DIRT) && world.getMaxLocalRawBrightness(pos1up) >= 4 && world.getBlockState(pos1up).getLightBlock(world, pos1up) <= 2) {
					world.setBlockAndUpdate(pos1, defaultBlockState());
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random r) {
		switch (variant) {
		case DRY:
			break;
		case GOLDEN:
			break;
		case VIVID:
			break;
		case SCORCHED:
			if (r.nextInt(80) == 0) {
				world.addParticle(ParticleTypes.FLAME, pos.getX() + r.nextFloat(), pos.getY() + 1.1, pos.getZ() + r.nextFloat(), 0, 0, 0);
			}
			break;
		case INFUSED:
			if (r.nextInt(100) == 0) {
				SparkleParticleData data = SparkleParticleData.sparkle(r.nextFloat() * 0.2F + 1F, 0F, 1F, 1F, 5);
				world.addParticle(data, pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 0, 0, 0);
			}
			break;
		case MUTATED:
			if (r.nextInt(100) == 0) {
				if (r.nextInt(100) > 25) {
					SparkleParticleData data = SparkleParticleData.sparkle(r.nextFloat() * 0.2F + 1F, 1F, 0F, 1F, 5);
					world.addParticle(data, pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 0, 0, 0);
				} else {
					SparkleParticleData data = SparkleParticleData.sparkle(r.nextFloat() * 0.2F + 1F, 1F, 1F, 0F, 5);
					world.addParticle(data, pos.getX() + r.nextFloat(), pos.getY() + 1.05, pos.getZ() + r.nextFloat(), 0, 0, 0);
				}
			}
			break;
		}
	}
}
