/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 27, 2015, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

public class ItemThunderSword extends ItemManasteelSword implements ICraftAchievement {

	private static final String TAG_LIGHTNING_SEED = "lightningSeed";

	public ItemThunderSword() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.THUNDER_SWORD);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase attacker) {
		if(!(entity instanceof EntityPlayer) && entity != null) {
			double range = 8;
			final List<EntityLivingBase> alreadyTargetedEntities = new ArrayList();
			int dmg = 5;
			long lightningSeed = ItemNBTHelper.getLong(stack, TAG_LIGHTNING_SEED, 0);

			IEntitySelector selector = new IEntitySelector() {

				@Override
				public boolean isEntityApplicable(Entity e) {
					return e instanceof EntityLivingBase && e instanceof IMob && !(e instanceof EntityPlayer) && !alreadyTargetedEntities.contains(e);
				}

			};

			Random rand = new Random(lightningSeed);
			EntityLivingBase lightningSource = entity;
			for(int i = 0; i < 4; i++) {
				List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABBExcludingEntity(lightningSource, AxisAlignedBB.getBoundingBox(lightningSource.posX - range, lightningSource.posY - range, lightningSource.posZ - range, lightningSource.posX + range, lightningSource.posY + range, lightningSource.posZ + range), selector);
				if(entities.isEmpty())
					break;

				EntityLivingBase target = entities.get(rand.nextInt(entities.size()));
				if(attacker instanceof EntityPlayer)
					target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), dmg);
				else target.attackEntityFrom(DamageSource.causeMobDamage(attacker), dmg);

				Botania.proxy.lightningFX(entity.worldObj, Vector3.fromEntityCenter(lightningSource), Vector3.fromEntityCenter(target), 1, 0x0179C4, 0xAADFFF);

				alreadyTargetedEntities.add(target);
				lightningSource = target;
				dmg--;
			}

			if(!entity.worldObj.isRemote)
				ItemNBTHelper.setLong(stack, TAG_LIGHTNING_SEED, entity.worldObj.rand.nextLong());
		}


		return super.hitEntity(stack, entity, attacker);
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.terrasteelWeaponCraft;
	}


}
