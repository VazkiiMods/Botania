/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

import javax.annotation.Nonnull;

/*
 * We don't need to register a new ContainerType and can just piggyback off vanilla's,
 * because all we want to change is canInteractWith, which is only checked serverside.
 * So on the server we have this container while the client will think it's just interacting
 * with a normal WorkbenchContainer.
 */
public class ContainerCraftingHalo extends CraftingMenu {

	public ContainerCraftingHalo(int windowId, Inventory playerInv, ContainerLevelAccess wp) {
		super(windowId, playerInv, wp);
	}

	@Override
	public boolean stillValid(@Nonnull Player player) {
		return true;
	}
}
