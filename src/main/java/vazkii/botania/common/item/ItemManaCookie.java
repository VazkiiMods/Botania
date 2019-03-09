/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 21, 2014, 8:44:35 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemManaCookie extends ItemFood {

	public ItemManaCookie(Properties props) {
		super(0, 0.1F, false, props);
		setPotionEffect(new PotionEffect(MobEffects.SATURATION, 20, 0), 1F);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "totalbiscuit"),
				(stack, worldIn, entityIn) -> stack.getDisplayName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
	}
}
