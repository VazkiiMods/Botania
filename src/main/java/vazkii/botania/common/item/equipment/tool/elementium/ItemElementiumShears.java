package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.base.Predicates;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemElementiumShears extends ItemManasteelShears {

	public ItemElementiumShears() {
		super(LibItemNames.ELEMENTIUM_SHEARS);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "reddit"),
				(stack, worldIn, entityIn) -> stack.getDisplayName().equalsIgnoreCase("dammit reddit") ? 1F: 0F);
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		if(living.world.isRemote)
			return;

		if(count != getMaxItemUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			List sheep = living.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range), Predicates.instanceOf(IShearable.class));
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
		return repairBy.getItem() == ModItems.manaResource && repairBy.getItemDamage() == 7 ? true : super.getIsRepairable(toRepair, repairBy);
	}

}
