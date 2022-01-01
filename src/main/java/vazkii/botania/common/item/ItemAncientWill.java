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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IAncientWillContainer;

import java.util.List;
import java.util.Locale;

public class ItemAncientWill extends Item {
	public final IAncientWillContainer.AncientWillType type;

	public ItemAncientWill(IAncientWillContainer.AncientWillType variant, Properties props) {
		super(props);
		this.type = variant;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		list.add(new TranslatableComponent("botaniamisc.craftToAddWill").withStyle(ChatFormatting.GREEN));
		list.add(new TranslatableComponent("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".shortDesc").withStyle(ChatFormatting.GRAY));
	}
}
