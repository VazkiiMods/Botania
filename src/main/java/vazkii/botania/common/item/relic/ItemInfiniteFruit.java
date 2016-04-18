/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 10:12:55 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

public class ItemInfiniteFruit extends ItemRelic implements IManaUsingItem {

	public ItemInfiniteFruit() {
		super(LibItemNames.INFINITE_FRUIT);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return isBoot(p_77661_1_) ? EnumAction.DRINK : EnumAction.EAT;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
		if(p_77659_3_.canEat(false) && isRightPlayer(p_77659_3_, p_77659_1_)) {
			p_77659_3_.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, p_77659_1_);
		}
		return ActionResult.newResult(EnumActionResult.PASS, p_77659_1_);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		super.onUsingTick(stack, living, count);
		if(!(living instanceof EntityPlayer))
			return;
		EntityPlayer player = ((EntityPlayer) living);
		if(ManaItemHandler.requestManaExact(stack, player, 500, true)) {
			if(count % 5 == 0)
				player.getFoodStats().addStats(1, 1F);

			if(count == 5)
				if(player.canEat(false))
					ReflectionHelper.setPrivateValue(EntityLivingBase.class, player, 20, LibObfuscation.ITEM_IN_USE_COUNT);
		}
	}

	public static boolean isBoot(ItemStack par1ItemStack) {
		String name = par1ItemStack.getDisplayName().toLowerCase().trim();
		return name.equals("das boot");
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
