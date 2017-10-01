/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 17, 2014, 5:34:35 PM (GMT)]
 */
package vazkii.botania.api.subtile.signature;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;

import java.util.List;

/**
 * A basic (and fallback) implementation of SubTileSignature.
 */
public class BasicSignature implements SubTileSignature {

	private final String name;

	public BasicSignature(String name) {
		this.name = name;
	}

	@Override
	public String getUnlocalizedNameForStack(ItemStack stack) {
		return unlocalizedName("");
	}

	@Override
	public String getUnlocalizedLoreTextForStack(ItemStack stack) {
		return unlocalizedName(".reference");
	}

	public String getName() {
		return name;
	}

	public String getType() {
		Class<? extends SubTileEntity> clazz = BotaniaAPI.getSubTileMapping(name);

		if(clazz == null)
			return "uwotm8";

		if(SubTileGenerating.class.isAssignableFrom(clazz))
			return "botania.flowerType.generating";

		if(SubTileFunctional.class.isAssignableFrom(clazz))
			return "botania.flowerType.functional";

		return "botania.flowerType.misc";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addTooltip(ItemStack stack, World world, List<String> tooltip) {
		tooltip.add(TextFormatting.BLUE + I18n.translateToLocal(getType()));
	}

	private String unlocalizedName(String end) {
		return "tile.botania:" + SubTileSignature.SPECIAL_FLOWER_PREFIX + name + end;
	}

}
