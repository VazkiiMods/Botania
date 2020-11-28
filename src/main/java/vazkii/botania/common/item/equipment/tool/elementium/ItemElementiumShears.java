/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.function.Predicate;

public class ItemElementiumShears extends ItemManasteelShears {

	public ItemElementiumShears(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setCurrentHand(hand);
		return TypedActionResult.consume(player.getStackInHand(hand));
	}

	@Override
	public void usageTick(World world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (world.isClient) {
			return;
		}

		if (count != getMaxUseTime(stack) && count % 5 == 0) {
			int range = 12;
			Predicate<Entity> shearablePred = e -> e instanceof Shearable;
			List<Entity> shearable = world.getEntitiesByClass(Entity.class, new Box(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range), shearablePred);
			if (shearable.size() > 0) {
				for (Entity entity : shearable) {
					if (entity instanceof Shearable && ((Shearable) entity).isShearable()) {
						((Shearable) entity).sheared(living.getSoundCategory());
						stack.damage(1, living, l -> l.sendToolBreakStatus(l.getActiveHand()));
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean canRepair(ItemStack toRepair, @Nonnull ItemStack repairBy) {
		return repairBy.getItem() == ModItems.elementium || super.canRepair(toRepair, repairBy);
	}

}
