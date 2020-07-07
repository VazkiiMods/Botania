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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

public class ItemStarSword extends ItemManasteelSword {

	private static final int MANA_PER_DAMAGE = 120;

	public ItemStarSword(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			EffectInstance haste = player.getActivePotionEffect(Effects.HASTE);
			float check = haste == null ? 0.16666667F : haste.getAmplifier() == 1 ? 0.5F : 0.4F;

			if (player.getHeldItemMainhand() == stack && player.swingProgress == check && !world.isRemote) {
				BlockRayTraceResult pos = ToolCommons.raytraceFromEntity(player, 48, false);
				if (pos.getType() == RayTraceResult.Type.BLOCK) {
					Vector3d posVec = Vector3d.func_237491_b_(pos.getPos());
					Vector3d motVec = new Vector3d((0.5 * Math.random() - 0.25) * 18, 24, (0.5 * Math.random() - 0.25) * 18);
					posVec = posVec.add(motVec);
					motVec = motVec.normalize().inverse().scale(1.5);

					EntityFallingStar star = new EntityFallingStar(player, world);
					star.setPosition(posVec.x, posVec.y, posVec.z);
					star.setMotion(motVec);
					world.addEntity(star);

					if (!world.isRaining()
							&& Math.abs(world.getDayTime() - 18000) < 1800
							&& Math.random() < 0.125) {
						EntityFallingStar bonusStar = new EntityFallingStar(player, world);
						bonusStar.setPosition(posVec.x, posVec.y, posVec.z);
						bonusStar.setMotion(motVec.x + Math.random() - 0.5,
								motVec.y + Math.random() - 0.5, motVec.z + Math.random() - 0.5);
						world.addEntity(bonusStar);
					}

					ToolCommons.damageItem(stack, 1, player, MANA_PER_DAMAGE);
					world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.starcaller, SoundCategory.PLAYERS, 0.4F, 1.4F);
				}
			}
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}
}
