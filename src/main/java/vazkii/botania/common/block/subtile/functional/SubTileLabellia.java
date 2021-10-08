/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.DelayHelper;

import java.util.List;

public class SubTileLabellia extends TileEntityFunctionalFlower {
	private static final int PICKUP_RANGE = 0;
	private static final int RENAME_RANGE = 2;
	private static final int COST = 500;

	public SubTileLabellia(BlockPos pos, BlockState state) {
		super(ModSubtiles.LABELLIA, pos, state);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0xFFEE83;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!level.isClientSide && redstoneSignal == 0 && getMana() >= COST) {
			BlockPos effPos = getEffectivePos();
			int x = effPos.getX();
			int y = effPos.getY();
			int z = effPos.getZ();

			for (ItemEntity nameTagEnt : level.getEntitiesOfClass(ItemEntity.class,
					new AABB(x - PICKUP_RANGE, y, z - PICKUP_RANGE,
							x + PICKUP_RANGE + 1, y + 1, z + PICKUP_RANGE + 1),
					EntitySelector.ENTITY_STILL_ALIVE)) {
				if (!DelayHelper.canInteractWith(this, nameTagEnt)) {
					continue;
				}

				ItemStack nameTag = nameTagEnt.getItem();
				if (nameTag.is(Items.NAME_TAG) && nameTag.hasCustomHoverName()) {
					AABB renameArea = new AABB(x - RENAME_RANGE, y, z - RENAME_RANGE, x + RENAME_RANGE + 1, y + 1, z + RENAME_RANGE + 1);
					Component name = nameTag.getHoverName();
					List<LivingEntity> nameableEntities = level.getEntitiesOfClass(LivingEntity.class, renameArea,
							EntitySelector.ENTITY_STILL_ALIVE.and(e -> !name.equals(e.getCustomName()) && !(e instanceof Player)));

					List<ItemEntity> nameableItems = level.getEntitiesOfClass(ItemEntity.class, renameArea,
							i -> DelayHelper.canInteractWith(this, i)
									&& i != nameTagEnt
									&& !name.equals(i.getItem().getHoverName()));

					if (!nameableItems.isEmpty() || !nameableEntities.isEmpty()) {
						for (LivingEntity e : nameableEntities) {
							// [VanillaCopy] from NameTagItem
							e.setCustomName(name);
							if (e instanceof Mob mob) {
								mob.setPersistenceRequired();
							}
						}
						for (ItemEntity i : nameableItems) {
							i.getItem().setHoverName(name);
							i.setItem(i.getItem()); // ensure it syncs
							((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT,
									i.getX(), i.getY(), i.getZ(),
									3, 0, 0, 0, 0);

						}
						addMana(-COST);
						nameTag.shrink(1);
						level.playSound(null, x + 0.5, y + 0.5, z + 0.5, ModSounds.labellia, SoundSource.BLOCKS, 1F, 1F);
						break;
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RENAME_RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), PICKUP_RANGE);
	}

	@Override
	public int getMaxMana() {
		return 6000;
	}
}
