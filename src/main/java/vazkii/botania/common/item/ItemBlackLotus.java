/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class ItemBlackLotus extends Item implements IManaDissolvable {

	private static final int MANA_PER = 8000;
	private static final int MANA_PER_T2 = 100000;

	public ItemBlackLotus(Properties props) {
		super(props);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getItem() == ModItems.blackerLotus;
	}

	@Override
	public void onDissolveTick(IManaPool pool, ItemStack stack, ItemEntity item) {
		if (pool.isFull() || pool.getCurrentMana() == 0) {
			return;
		}

		TileEntity tile = (TileEntity) pool;
		boolean t2 = stack.getItem() == ModItems.blackerLotus;

		if (!item.world.isRemote) {
			pool.receiveMana(t2 ? MANA_PER_T2 : MANA_PER);
			stack.shrink(1);
			PacketHandler.sendToNearby(item.world, item, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.BLACK_LOTUS_DISSOLVE, item.getPosX(), tile.getPos().getY() + 0.5, item.getPosZ()));
		}

		item.playSound(ModSounds.blackLotus, 0.5F, t2 ? 0.1F : 1F);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		list.add(new TranslationTextComponent("botaniamisc.lotusDesc").func_240699_a_(TextFormatting.GRAY));
	}

}
