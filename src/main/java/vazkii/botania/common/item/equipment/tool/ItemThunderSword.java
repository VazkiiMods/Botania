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

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ItemThunderSword extends ItemManasteelSword {

	private static final String TAG_LIGHTNING_SEED = "lightningSeed";

	public ItemThunderSword(Properties props) {
		super(BotaniaAPI.TERRASTEEL_ITEM_TIER, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity entity, @Nonnull LivingEntity attacker) {
		if(!(entity instanceof PlayerEntity) && entity != null) {
			double range = 8;
			List<LivingEntity> alreadyTargetedEntities = new ArrayList<>();
			int dmg = 5;
			long lightningSeed = ItemNBTHelper.getLong(stack, TAG_LIGHTNING_SEED, 0);

			Predicate<Entity> selector = e -> e instanceof LivingEntity && e instanceof IMob && !(e instanceof PlayerEntity) && !alreadyTargetedEntities.contains(e);

			Random rand = new Random(lightningSeed);
			LivingEntity lightningSource = entity;
			int hops = entity.world.isThundering() ? 10 : 4;
			for(int i = 0; i < hops; i++) {
				List<Entity> entities = entity.world.getEntitiesInAABBexcluding(lightningSource, new AxisAlignedBB(lightningSource.posX - range, lightningSource.posY - range, lightningSource.posZ - range, lightningSource.posX + range, lightningSource.posY + range, lightningSource.posZ + range), selector::test);
				if(entities.isEmpty())
					break;

				LivingEntity target = (LivingEntity) entities.get(rand.nextInt(entities.size()));
				if(attacker instanceof PlayerEntity)
					target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), dmg);
				else target.attackEntityFrom(DamageSource.causeMobDamage(attacker), dmg);

				Botania.proxy.lightningFX(Vector3.fromEntityCenter(lightningSource), Vector3.fromEntityCenter(target), 1, 0x0179C4, 0xAADFFF);

				alreadyTargetedEntities.add(target);
				lightningSource = target;
				dmg--;
			}

			if(!entity.world.isRemote)
				ItemNBTHelper.setLong(stack, TAG_LIGHTNING_SEED, entity.world.rand.nextLong());
		}


		return super.hitEntity(stack, entity, attacker);
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.5, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

}
