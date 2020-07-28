/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

public class ItemStarSword extends ItemManasteelSword {

	private static final int MANA_PER_DAMAGE = 120;

	public ItemStarSword(Settings props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			StatusEffectInstance haste = player.getStatusEffect(StatusEffects.HASTE);
			float check = haste == null ? 0.16666667F : haste.getAmplifier() == 1 ? 0.5F : 0.4F;

			if (player.getMainHandStack() == stack && player.handSwingProgress == check && !world.isClient) {
				BlockHitResult pos = ToolCommons.raytraceFromEntity(player, 48, false);
				if (pos.getType() == HitResult.Type.BLOCK) {
					Vec3d posVec = Vec3d.of(pos.getBlockPos());
					Vec3d motVec = new Vec3d((0.5 * Math.random() - 0.25) * 18, 24, (0.5 * Math.random() - 0.25) * 18);
					posVec = posVec.add(motVec);
					motVec = motVec.normalize().negate().multiply(1.5);

					EntityFallingStar star = new EntityFallingStar(player, world);
					star.updatePosition(posVec.x, posVec.y, posVec.z);
					star.setVelocity(motVec);
					world.spawnEntity(star);

					if (!world.isRaining()
							&& Math.abs(world.getTimeOfDay() - 18000) < 1800
							&& Math.random() < 0.125) {
						EntityFallingStar bonusStar = new EntityFallingStar(player, world);
						bonusStar.updatePosition(posVec.x, posVec.y, posVec.z);
						bonusStar.setVelocity(motVec.x + Math.random() - 0.5,
								motVec.y + Math.random() - 0.5, motVec.z + Math.random() - 0.5);
						world.spawnEntity(bonusStar);
					}

					stack.damage(1, player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
					world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.starcaller, SoundCategory.PLAYERS, 0.4F, 1.4F);
				}
			}
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}
}
