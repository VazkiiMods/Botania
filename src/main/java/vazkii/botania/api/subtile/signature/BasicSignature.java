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

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;

/**
 * A basic (and fallback) implementation of SubTileSignature.
 */
public class BasicSignature extends SubTileSignature {

	final String name;

	public BasicSignature(String name) {
		this.name = name;
	}

	@Override
	public void registerIcons(IIconRegister register) {
		BotaniaAPI.internalHandler.registerBasicSignatureIcons(name, register);
	}

	@Override
	public IIcon getIconForStack(ItemStack stack) {
		return BotaniaAPI.internalHandler.getSubTileIconForName(name);
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

		if(clazz.getAnnotation(PassiveFlower.class) != null)
			return "botania.flowerType.passiveGenerating";

		if(SubTileGenerating.class.isAssignableFrom(clazz))
			return "botania.flowerType.generating";

		if(SubTileFunctional.class.isAssignableFrom(clazz))
			return "botania.flowerType.functional";

		return "botania.flowerType.misc";
	}

	@Override
	public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
		tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal(getType()));
	}

	private String unlocalizedName(String end) {
		return "tile.botania:" + SubTileSignature.SPECIAL_FLOWER_PREFIX + name + end;
	}

}
