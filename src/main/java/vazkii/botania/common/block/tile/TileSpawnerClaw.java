/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.mixin.AccessorBaseSpawner;

public class TileSpawnerClaw extends TileMod implements IManaReceiver {
	private static final String TAG_MANA = "mana";
	private static final int MAX_MANA = 160;

	private int mana = 0;

	public TileSpawnerClaw(BlockPos pos, BlockState state) {
		super(ModTiles.SPAWNER_CLAW, pos, state);
	}

	private static final ThreadLocal<Boolean> IS_NEAR_PLAYER_REC_CALL = ThreadLocal.withInitial(() -> false);

	public static void onSpawnerNearPlayer(BaseSpawner spawner, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!IS_NEAR_PLAYER_REC_CALL.get() && level.getBlockState(pos).is(Blocks.SPAWNER)) {
			try {
				IS_NEAR_PLAYER_REC_CALL.set(true);

				// We're injecting into this method, but want to see what the method would have said without us
				boolean vanillaValue = ((AccessorBaseSpawner) spawner)
						.botania_isPlayerInRange(level, pos);

				// If vanilla is out of range, then we do our work
				if (!vanillaValue) {
					BlockPos up = pos.above();
					if (level.getBlockState(up).is(ModBlocks.spawnerClaw)) {
						BlockEntity be = level.getBlockEntity(pos.above());
						if (be instanceof TileSpawnerClaw claw) {
							if (claw.mana > 5) {
								claw.receiveMana(-6);
								if (level.isClientSide && Math.random() > 0.5) {
									WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
									level.addParticle(data, up.getX() + 0.3 + Math.random() * 0.5, up.getY() - 0.3 + Math.random() * 0.25, up.getZ() + Math.random(), 0, -(-0.025F - 0.005F * (float) Math.random()), 0);
								}

								// Yes, perform spawner functions using claw's mana
								cir.setReturnValue(true);
							}
						}
					}
				}
			} finally {
				IS_NEAR_PLAYER_REC_CALL.set(false);
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		mana = cmp.getInt(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

}
