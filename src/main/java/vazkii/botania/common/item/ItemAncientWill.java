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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.IAncientWillContainer;

import java.util.List;
import java.util.Locale;

public class ItemAncientWill extends Item {
	public final IAncientWillContainer.AncientWillType type;

	public ItemAncientWill(IAncientWillContainer.AncientWillType variant, Settings props) {
		super(props);
		this.type = variant;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flag) {
		list.add(new TranslatableText("botaniamisc.craftToAddWill").formatted(Formatting.GREEN));
		list.add(new TranslatableText("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".shortDesc").formatted(Formatting.GRAY));
	}
}
