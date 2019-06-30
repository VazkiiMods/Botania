/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 15, 2014, 6:02:52 PM (GMT)]
 */
package vazkii.botania.client.gui.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/* We don't need to register a new ContainerType and can just piggyback off vanilla's,
 * because all we want to change is canInteractWith, which is only checked serverside.
 * So on the server we have this container while the client will think it's just interacting
 * with a normal WorkbenchContainer.
 */
public class ContainerCraftingHalo extends WorkbenchContainer {

	public ContainerCraftingHalo(int windowId, PlayerInventory playerInv) {
		super(windowId, playerInv);
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player) {
		return true;
	}
}
