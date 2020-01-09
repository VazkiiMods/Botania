/**
 * This class was created by <ToMe25> based on a class by <Vazkii>.
 * It's distributed as part of the Botania Mod.
 * Get the Source Code in github: https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 9, 2020, 12:38 (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemRegenIvy extends ItemMod {

	public static final String TAG_REGEN = "Botania_regenIvy";
	private static final int MANA_PER_DAMAGE = 200;

	public ItemRegenIvy(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void onTick(PlayerTickEvent event) {
		if (event.phase == Phase.END && !event.player.world.isRemote)
			for (int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
				ItemStack stack = event.player.inventory.getStackInSlot(i);
				if (!stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_REGEN, false)
						&& stack.getDamage() > 0
						&& ManaItemHandler.requestManaExact(stack, event.player, MANA_PER_DAMAGE, true))
					stack.setDamage(stack.getDamage() - 1);
			}
	}
}