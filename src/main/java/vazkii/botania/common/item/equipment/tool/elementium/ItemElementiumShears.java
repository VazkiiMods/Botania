package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.base.Predicates;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemElementiumShears extends ItemManasteelShears {

	public ItemElementiumShears(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "reddit"),
				(stack, worldIn, entityIn) -> stack.getDisplayName().getString().equalsIgnoreCase("dammit reddit") ? 1F: 0F);
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
		return ActionResult.success(player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		if(living.world.isRemote)
			return;

		if(count != getUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			List sheep = living.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range), Predicates.instanceOf(IShearable.class));
			if(sheep.size() > 0) {
				for(IShearable target : (List<IShearable>) sheep) {
					Entity entity = (Entity) target;
					if(target.isShearable(stack, entity.world, new BlockPos(entity))) {
						List<ItemStack> drops = target.onSheared(stack, entity.world, new BlockPos(entity), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

						for(ItemStack drop : drops) {
							entity.entityDropItem(drop, 1.0F);
						}

						ToolCommons.damageItem(stack, 1, living, MANA_PER_DAMAGE);
						break;
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
