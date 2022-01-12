/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.network.EffectType;
import vazkii.botania.network.clientbound.PacketBotaniaEffect;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public class ItemBlackLotus extends Item implements IManaDissolvable {

	private static final int MANA_PER = 8000;
	private static final int MANA_PER_T2 = 100000;

	public ItemBlackLotus(Properties props) {
		super(props);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) || stack.is(ModItems.blackerLotus);
	}

	@Override
	public void onDissolveTick(IManaPool pool, ItemStack stack, ItemEntity item) {
		if (pool.isFull() || pool.getCurrentMana() == 0) {
			return;
		}

		BlockPos pos = pool.tileEntity().getBlockPos();
		boolean t2 = stack.is(ModItems.blackerLotus);

		if (!item.level.isClientSide) {
			pool.receiveMana(t2 ? MANA_PER_T2 : MANA_PER);
			stack.shrink(1);
			IXplatAbstractions.INSTANCE.sendToTracking(item, new PacketBotaniaEffect(EffectType.BLACK_LOTUS_DISSOLVE, pos.getX(), pos.getY() + 0.5, pos.getZ()));
		}

		item.playSound(ModSounds.blackLotus, 1F, t2 ? 0.1F : 1F);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		list.add(new TranslatableComponent("botaniamisc.lotusDesc").withStyle(ChatFormatting.GRAY));
	}

}
