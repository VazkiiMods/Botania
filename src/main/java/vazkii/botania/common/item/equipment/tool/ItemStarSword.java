/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 17, 2015, 3:55:52 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

public class ItemStarSword extends ItemManasteelSword {

	private static final int MANA_PER_DAMAGE = 120;

	public ItemStarSword(Properties props) {
		super(BotaniaAPI.TERRASTEEL_ITEM_TIER, props);
	}

	@Override
	public void inventoryTick(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
		super.inventoryTick(par1ItemStack, world, par3Entity, par4, par5);
		if(par3Entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) par3Entity;
			EffectInstance haste = player.getActivePotionEffect(Effects.HASTE);
			float check = haste == null ? 0.16666667F : haste.getAmplifier() == 1 ? 0.5F : 0.4F;

			if(player.getHeldItemMainhand() == par1ItemStack && player.swingProgress == check && !world.isRemote) {
				RayTraceResult pos = ToolCommons.raytraceFromEntity(world, player, RayTraceContext.FluidMode.NONE, 48);
				if(pos.getType() == RayTraceResult.Type.BLOCK) {
					Vector3 posVec = Vector3.fromBlockPos(((BlockRayTraceResult) pos).getPos());
					Vector3 motVec = new Vector3((0.5 * Math.random() - 0.25) * 18, 24, (0.5 * Math.random() - 0.25) * 18);
					posVec = posVec.add(motVec);
					motVec = motVec.normalize().negate().multiply(1.5);

					EntityFallingStar star = new EntityFallingStar(player, world);
					star.setPosition(posVec.x, posVec.y, posVec.z);
					star.setMotion(motVec.toVec3D());
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

					ToolCommons.damageItem(par1ItemStack, 1, player, MANA_PER_DAMAGE);
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
