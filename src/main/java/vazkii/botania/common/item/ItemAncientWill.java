/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2015, 10:59:45 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.IAncientWillContainer;

import java.util.List;
import java.util.Locale;

public class ItemAncientWill extends Item {
	public final IAncientWillContainer.AncientWillType type;

	public ItemAncientWill(IAncientWillContainer.AncientWillType variant, Properties props) {
		super(props);
		this.type = variant;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(new TranslationTextComponent("botaniamisc.craftToAddWill").applyTextStyle(TextFormatting.GREEN));
		list.add(new TranslationTextComponent("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".shortDesc").applyTextStyle(TextFormatting.GRAY));
	}
}
