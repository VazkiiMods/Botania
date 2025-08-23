package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Random;

public class NaturaPylonBlock extends PylonBlock {

	public NaturaPylonBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected void clientTick(Level level, BlockPos worldPosition, BlockState state, PylonBlockEntity self) {

		super.clientTick(level, worldPosition, state, self);

		if (self.activated && self.centerPos != null) {
			BlockState centerState = level.getBlockState(self.centerPos);
			if (!centerState.is(BotaniaBlocks.alfPortal)
					|| centerState.getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.OFF
					|| !(level.getBlockState(worldPosition.below()).getBlock() instanceof ManaPoolBlock)) {
				self.activated = false;
				return;
			}

			Vec3 centerBlock = new Vec3(self.centerPos.getX() + 0.5, self.centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), self.centerPos.getZ() + 0.5);

			if (BotaniaConfig.client().elfPortalParticlesEnabled()) {
				double worldTime = level.getGameTime();
				worldTime += new Random(worldPosition.hashCode()).nextInt(1000);
				worldTime /= 5;

				float r = 0.75F + (float) Math.random() * 0.05F;
				double x = worldPosition.getX() + 0.5 + Math.cos(worldTime) * r;
				double z = worldPosition.getZ() + 0.5 + Math.sin(worldTime) * r;

				Vec3 ourCoords = new Vec3(x, worldPosition.getY() + 0.25, z);
				centerBlock = centerBlock.subtract(0, 0.5, 0);
				Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

				WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 1);
				level.addParticle(data, x, worldPosition.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
				if (level.random.nextInt(3) == 0) {
					WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
					level.addParticle(data1, x, worldPosition.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			}
		}
	}

	@Override
	public void addRandomParticle(Level level, BlockPos pos) {
		SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 0.5f, 1.0f, 0.5f, 2);
		level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random() * 1.3, pos.getZ() + Math.random(), 0, 0, 0);
	}

	public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
		return 15f;
	}
}
