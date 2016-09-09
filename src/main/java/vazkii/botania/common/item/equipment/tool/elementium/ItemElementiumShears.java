package vazkii.botania.common.item.equipment.tool.elementium;

import com.google.common.base.Predicates;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		if(living.worldObj.isRemote)
			return;

		if(count != getMaxItemUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			List sheep = living.worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range), Predicates.instanceOf(IShearable.class));
			if(sheep.size() > 0) {
				for(IShearable target : ((List<IShearable>) sheep)) {
					Entity entity = (Entity) target;
					if(target.isShearable(stack, entity.worldObj, new BlockPos(entity))) {
						List<ItemStack> drops = target.onSheared(stack, entity.worldObj, new BlockPos(entity), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

						Random rand = new Random();
						for(ItemStack drop : drops) {
							EntityItem ent = entity.entityDropItem(drop, 1.0F);
							ent.motionY += rand.nextFloat() * 0.05F;
							ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
							ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
						}

						ToolCommons.damageItem(stack, 1, living, MANA_PER_DAMAGE);
						break;
					}
				}
			}
		}
	}

}
