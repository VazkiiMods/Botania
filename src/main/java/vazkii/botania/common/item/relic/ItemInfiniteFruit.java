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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemInfiniteFruit extends ItemRelic implements IManaUsingItem {

	public ItemInfiniteFruit() {
		super(LibItemNames.INFINITE_FRUIT);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "boot"), (stack, worldIn, entityIn) -> ItemInfiniteFruit.isBoot(stack) ? 1F : 0F);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return isBoot(stack) ? EnumAction.DRINK : EnumAction.EAT;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(player.canEat(false) && isRightPlayer(player, stack)) {
			player.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		super.onUsingTick(stack, living, count);
		if(!(living instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) living;
		if(ManaItemHandler.requestManaExact(stack, player, 500, true)) {
			if(count % 5 == 0)
				player.getFoodStats().addStats(1, 1F);

			if(count == 5)
				if(player.canEat(false))
					player.activeItemStackUseCount = 20;
		}
	}

	private static boolean isBoot(ItemStack par1ItemStack) {
		String name = par1ItemStack.getDisplayName().toLowerCase(Locale.ROOT).trim();
		return name.equals("das boot");
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/infinite_fruit");
	}

}
