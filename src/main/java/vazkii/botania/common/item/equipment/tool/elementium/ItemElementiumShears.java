/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.item.ModItems;
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
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public void onUseTick(Level world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (world.isClientSide) {
			return;
		}

		if (count != getUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			Predicate<Entity> shearablePred = e -> e instanceof Shearable;
			List<Entity> shearable = world.getEntitiesOfClass(Entity.class, new AABB(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range), shearablePred);
			if (shearable.size() > 0) {
				for (Entity entity : shearable) {
					if (entity instanceof Shearable && ((Shearable) entity).readyForShearing()) {
						((Shearable) entity).shear(living.getSoundSource());
						stack.hurtAndBreak(1, living, l -> l.broadcastBreakEvent(l.getUsedItemHand()));
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, @Nonnull ItemStack repairBy) {
		return repairBy.is(ModItems.elementium) || super.isValidRepairItem(toRepair, repairBy);
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return super.getSortingPriority(stack, state) + 100;
	}
}
