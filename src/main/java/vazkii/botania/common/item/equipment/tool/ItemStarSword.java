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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemStarSword extends ItemManasteelSword {

	private static final int MANA_PER_DAMAGE = 120;

	public ItemStarSword() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.STAR_SWORD);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, world, par3Entity, par4, par5);
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) par3Entity;
			PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
			float check = haste == null ? 0.16666667F : haste.getAmplifier() == 1 ? 0.5F : 0.4F;

			if(player.getHeldItemMainhand() == par1ItemStack && player.swingProgress == check && !world.isRemote) {
				RayTraceResult pos = ToolCommons.raytraceFromEntity(world, par3Entity, true, 48);
				if(pos != null && pos.getBlockPos() != null) {
					Vector3 posVec = Vector3.fromBlockPos(pos.getBlockPos());
					Vector3 motVec = new Vector3((0.5 * Math.random() - 0.25) * 18, 24, (0.5 * Math.random() - 0.25) * 18);
					posVec = posVec.add(motVec);
					motVec = motVec.normalize().negate().multiply(1.5);

					EntityFallingStar star = new EntityFallingStar(world, player);
					star.setPosition(posVec.x, posVec.y, posVec.z);
					star.motionX = motVec.x;
					star.motionY = motVec.y;
					star.motionZ = motVec.z;
					world.spawnEntity(star);

					if (!world.isRaining()
							&& Math.abs(world.getWorldTime() - 18000) < 1800
							&& Math.random() < 0.125) {
						EntityFallingStar bonusStar = new EntityFallingStar(world, player);
						bonusStar.setPosition(posVec.x, posVec.y, posVec.z);
						bonusStar.motionX = motVec.x + Math.random() - 0.5;
						bonusStar.motionY = motVec.y + Math.random() - 0.5;
						bonusStar.motionZ = motVec.z + Math.random() - 0.5;
						world.spawnEntity(bonusStar);
					}

					ToolCommons.damageItem(par1ItemStack, 1, player, MANA_PER_DAMAGE);
					world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.starcaller, SoundCategory.PLAYERS, 0.4F, 1.4F);
				}
			}
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, @Nonnull ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 4 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
}
