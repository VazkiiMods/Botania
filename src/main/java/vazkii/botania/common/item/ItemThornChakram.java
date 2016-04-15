/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2015, 6:42:47 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemThornChakram extends ItemMod implements ICraftAchievement {

	public ItemThornChakram() {
		super(LibItemNames.THORN_CHAKRAM);
		setMaxStackSize(6);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + stack.getItemDamage();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)  {
		--stack.stackSize;

		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!world.isRemote) {
			ItemStack copy = stack.copy();
			stack.stackSize = 1;
			EntityThornChakram c = new EntityThornChakram(world, player, copy);
			c.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			c.setFire(stack.getItemDamage() != 0);
			world.spawnEntityInWorld(c);
		}


		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.terrasteelWeaponCraft;
	}

}
