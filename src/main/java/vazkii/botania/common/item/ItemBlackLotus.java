/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class ItemBlackLotus extends Item implements IManaDissolvable {

	private static final int MANA_PER = 8000;
	private static final int MANA_PER_T2 = 100000;

	public ItemBlackLotus(Settings props) {
		super(props);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.getItem() == ModItems.blackerLotus;
	}

	@Override
	public void onDissolveTick(IManaPool pool, ItemStack stack, ItemEntity item) {
		if (pool.isFull() || pool.getCurrentMana() == 0) {
			return;
		}

		BlockEntity tile = (BlockEntity) pool;
		boolean t2 = stack.getItem() == ModItems.blackerLotus;

		if (!item.world.isClient) {
			pool.receiveMana(t2 ? MANA_PER_T2 : MANA_PER);
			stack.decrement(1);
			PacketBotaniaEffect.sendNearby(item, PacketBotaniaEffect.EffectType.BLACK_LOTUS_DISSOLVE, item.getX(), tile.getPos().getY() + 0.5, item.getZ());
		}

		item.playSound(ModSounds.blackLotus, 0.5F, t2 ? 0.1F : 1F);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		list.add(new TranslatableText("botaniamisc.lotusDesc").formatted(Formatting.GRAY));
	}

}
