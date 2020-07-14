/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Predicate;

public class ItemElementiumShears extends ItemManasteelShears {

	public ItemElementiumShears(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public void onUse(World world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (world.isRemote) {
			return;
		}

		if (count != getUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			Predicate<Entity> shearablePred = e -> e instanceof IShearable || e instanceof IForgeShearable;
			List<Entity> shearable = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(living.getPosX() - range, living.getPosY() - range, living.getPosZ() - range, living.getPosX() + range, living.getPosY() + range, living.getPosZ() + range), shearablePred);
			if (shearable.size() > 0) {
				for (Entity entity : shearable) {
					if (entity instanceof IShearable && ((IShearable) entity).func_230262_K__()) {
						((IShearable) entity).func_230263_a_(living.getSoundCategory());
						ToolCommons.damageItem(stack, 1, living, MANA_PER_DAMAGE);
						break;
					} else {
						IForgeShearable target = (IForgeShearable) entity;
						if (target.isShearable(stack, entity.world, entity.func_233580_cy_())) {
							PlayerEntity player = living instanceof PlayerEntity ? (PlayerEntity) living : null;
							List<ItemStack> drops = target.onSheared(player, stack, entity.world, entity.func_233580_cy_(), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

							for (ItemStack drop : drops) {
								entity.entityDropItem(drop, 1.0F);
							}

							ToolCommons.damageItem(stack, 1, living, MANA_PER_DAMAGE);
							break;
						}
					}

				}
			}
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repairBy) {
		return repairBy.getItem() == ModItems.elementium || super.getIsRepairable(toRepair, repairBy);
	}

}
