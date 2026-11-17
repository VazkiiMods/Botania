/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.internal_caps.ItemLifetime;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.network.clientbound.FlowerTakeItemEffectPacket;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

public class EndoflameBlockEntity extends GeneratingFlowerBlockEntity {
	private static final String TAG_BURN_TIME = "burnTime";
	private static final String TAG_COOLDOWN_TIME = "cooldownTime";
	private static final int FUEL_CAP = 32000;
	private static final int RANGE = 3;
	private static final int START_BURN_EVENT = 0;
	private static final int COOLDOWN_TIME = 40;

	private int burnTime = 0;
	private int cooldownTime = 0;
	private boolean wasBurningOnClient = false;

	public EndoflameBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.ENDOFLAME, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide()) {
			if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
				if (!wasBurningOnClient || level.getRandom().nextInt(10) == 0) {
					wasBurningOnClient = true;
					emitParticle(ParticleTypes.FLAME, 0.4 + Math.random() * 0.2, 0.7, 0.4 + Math.random() * 0.2, 0.0D,
							0.0D, 0.0D);
				}
			} else {
				wasBurningOnClient = false;
				if (getBlockState().getValue(BotaniaStateProperties.ON_COOLDOWN)) {
					FluidGeneratorBlockEntity.doCooldownParticles(this);
				}
			}
			return;
		}

		boolean wasBurning = burnTime > 0;
		boolean onCooldown = cooldownTime > 0;
		if (onCooldown) {
			cooldownTime--;
		} else if (wasBurning) {
			burnTime--;
			if (burnTime <= 0) {
				cooldownTime = COOLDOWN_TIME;
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, getBlockPos());
				level.setBlock(getBlockPos(), getBlockState()
						.setValue(BotaniaStateProperties.GENERATING, false)
						.setValue(BotaniaStateProperties.ON_COOLDOWN, true),
						Block.UPDATE_CLIENTS);
			}
		}

		if (burnTime > 0) {
			if (shouldUpdateThisTick()) {
				addMana(3);
			}
			return;
		}
		if (cooldownTime > 0) {
			return;
		}

		if (getMana() < getMaxMana() && cooldownTime <= 0) {
			for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class,
					MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
					itemEntity -> ItemLifetime.canInteractWith(this, itemEntity)
							&& !itemEntity.getItem().is(BotaniaTags.Items.IGNORED_BY_ENDOFLAME)
							&& !itemEntity.getItem().getItem().hasCraftingRemainingItem())) {
				ItemStack stack = item.getItem();
				int burnTime = getBurnTime(stack);
				if (burnTime > 0) {
					if (BotaniaConfig.common().flowerItemPickupAnimations()) {
						XplatAbstractions.instance().sendToTracking(item,
								FlowerTakeItemEffectPacket.creatOnFire(item.getId(), getEffectivePos(), 1));
					}
					this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

					EntityHelper.shrinkItem(item);
					level.playSound(null, getEffectivePos(), BotaniaSounds.ENDOFLAME, SoundSource.BLOCKS, 1F, 1F);
					level.blockEvent(getBlockPos(), getBlockState().getBlock(), START_BURN_EVENT, item.getId());
					level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, getBlockPos());
					level.setBlock(getBlockPos(),
							getBlockState()
									.setValue(BotaniaStateProperties.GENERATING, true)
									.setValue(BotaniaStateProperties.ON_COOLDOWN, false),
							Block.UPDATE_CLIENTS);
					return;
				}
			}
		}
		if (onCooldown) {
			level.setBlock(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.ON_COOLDOWN, false),
					Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 2;
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == START_BURN_EVENT) {
			Entity e = level.getEntity(param);
			if (e != null) {
				e.level().addParticle(ParticleTypes.LARGE_SMOKE, e.getX(), e.getY() + 0.1, e.getZ(), 0.0D, 0.0D, 0.0D);
				e.level().addParticle(ParticleTypes.FLAME, e.getX(), e.getY(), e.getZ(), 0.0D, 0.0D, 0.0D);
			}
			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	@Override
	public int getMaxMana() {
		return 300;
	}

	@Override
	public int getColor() {
		return 0x785000;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);

		cmp.putInt(TAG_BURN_TIME, burnTime);
		cmp.putInt(TAG_COOLDOWN_TIME, cooldownTime);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);

		burnTime = cmp.getInt(TAG_BURN_TIME);
		cooldownTime = cmp.getInt(TAG_COOLDOWN_TIME);
	}

	private int getBurnTime(ItemStack stack) {
		return stack.isEmpty() ? 0 : XplatAbstractions.INSTANCE.getSmeltingBurnTime(stack);
	}

}
