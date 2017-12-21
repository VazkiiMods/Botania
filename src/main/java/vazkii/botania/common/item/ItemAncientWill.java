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

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;
import java.util.Locale;

public class ItemAncientWill extends ItemMod {
	public final IAncientWillContainer.AncientWillType type;

	public ItemAncientWill(IAncientWillContainer.AncientWillType variant) {
		super(LibItemNames.ANCIENT_WILL + "_" + variant.name().toLowerCase(Locale.ROOT));
		this.type = variant;
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
		addStringToTooltip(I18n.format("botaniamisc.craftToAddWill"), list);
		addStringToTooltip(I18n.format("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".shortDesc"), list);
	}

	public void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}
}
