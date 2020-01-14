/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Map;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase<OrechidRecipeWrapper> {
	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "orechid");
	
	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.orechid), new ItemStack(Blocks.STONE, 64), 
				I18n.format("botania.nei.orechid"));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends OrechidRecipeWrapper> getRecipeClass() {
		return OrechidRecipeWrapper.class;
	}

	@Override
	protected Map<ResourceLocation, Integer> getOreWeights() {
		return BotaniaAPI.oreWeights;
	}
}
