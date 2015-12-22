package vazkii.botania.common.item.equipment.tool.elementium;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicates;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemElementiumShears extends ItemManasteelShears {

	public ItemElementiumShears() {
		super(LibItemNames.ELEMENTIUM_SHEARS);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

//	@Override todo 1.8
//	public IIcon getIconIndex(ItemStack par1ItemStack) {
//		return par1ItemStack.getDisplayName().equalsIgnoreCase("dammit reddit") ? dammitReddit : super.getIconIndex(par1ItemStack);
//	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(player.worldObj.isRemote)
			return;

		if(count != getMaxItemUseDuration(stack) && count % 5 == 0) {
			int range = 12;
			List sheep = player.worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range), Predicates.instanceOf(IShearable.class));
			if(sheep.size() > 0) {
				for(IShearable target : ((List<IShearable>) sheep)) {
					Entity entity = (Entity) target;
					if(target.isShearable(stack, entity.worldObj, new BlockPos(entity))) {
						List<ItemStack> drops = target.onSheared(stack, entity.worldObj, new BlockPos(entity), EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack));

						Random rand = new Random();
						for(ItemStack drop : drops) {
							EntityItem ent = entity.entityDropItem(drop, 1.0F);
							ent.motionY += rand.nextFloat() * 0.05F;
							ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
							ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
						}

						ToolCommons.damageItem(stack, 1, player, MANA_PER_DAMAGE);
						break;
					}
				}
			}
		}
	}

}
