/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Collections;
import java.util.List;

@Mixin(FlatPresetsScreen.class)
public class MixinFlatPresetsScreen {

	@Invoker("func_238640_a_")
	public static void botania_addFlatWorldPreset(ITextComponent name, IItemProvider icon, RegistryKey<Biome> biome, List<Structure<?>> structures, boolean b1, boolean b2, boolean b3, FlatLayerInfo... layers) {
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		botania_addFlatWorldPreset(new TranslationTextComponent("botaniamisc.preset.botanistReady"),
				ModItems.twigWand,
				Biomes.PLAINS,
				Collections.emptyList(),
				// idk what these are I just copied them from Redstone Ready
				false, false, false,
				new FlatLayerInfo(64, ModBlocks.livingrockBrick),
				new FlatLayerInfo(1, Blocks.BEDROCK));
	}

}
