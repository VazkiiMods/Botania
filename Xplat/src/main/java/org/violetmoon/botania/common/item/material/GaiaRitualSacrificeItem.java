/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import vazkii.botania.common.entity.GaiaGuardianEntity;

public class GaiaRitualSacrificeItem extends Item {
	private final boolean hardMode;

	public GaiaRitualSacrificeItem(Properties props, boolean hardMode) {
		super(props);
		this.hardMode = hardMode;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return GaiaGuardianEntity.spawn(ctx.getPlayer(), ctx.getItemInHand(), ctx.getLevel(), ctx.getClickedPos(), hardMode)
				? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide())
				: InteractionResult.FAIL;
	}
}
