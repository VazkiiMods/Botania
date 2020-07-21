/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.block.Blocks;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModSubtiles;

import javax.annotation.Nonnull;

import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase<OrechidRecipeWrapper> {
	public static final Identifier UID = prefix("orechid");

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.orechid), new ItemStack(Blocks.STONE, 64),
				I18n.translate("botania.nei.orechid"));
	}

	@Nonnull
	@Override
	public Identifier getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends OrechidRecipeWrapper> getRecipeClass() {
		return OrechidRecipeWrapper.class;
	}

	@Override
	protected Map<Identifier, Integer> getOreWeights() {
		return BotaniaAPI.instance().getOreWeights();
	}

}
