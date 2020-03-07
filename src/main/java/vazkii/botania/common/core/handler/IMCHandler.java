/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.IRegistryDelegate;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.OreWeightMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IMCHandler {
	public static void handle(InterModProcessEvent evt) {
		BotaniaAPI.oreWeights = handleOreWeights(evt.getIMCStream(IMC.REGISTER_ORE_WEIGHT::equals));
		BotaniaAPI.oreWeightsNether = handleOreWeights(evt.getIMCStream(IMC.REGISTER_NETHER_ORE_WEIGHT::equals));
		BotaniaAPI.paintableBlocks = handlePaintable(evt.getIMCStream(IMC.REGISTER_PAINTABLE_BLOCK::equals));
	}

	private static Map<ResourceLocation, Integer> handleOreWeights(Stream<InterModComms.IMCMessage> msgs) {
		Map<ResourceLocation, Integer> map = msgs
				.filter(msg -> msg.getMessageSupplier().get() instanceof OreWeightMessage)
				.map(msg -> (OreWeightMessage) msg.getMessageSupplier().get())
				.collect(Collectors.toMap(OreWeightMessage::getOre, OreWeightMessage::getWeight));
		return ImmutableMap.copyOf(map);
	}

	private static Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> handlePaintable(Stream<InterModComms.IMCMessage> msgs) {
		Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> map = msgs
				.filter(msg -> msg.getMessageSupplier().get() instanceof PaintableBlockMessage)
				.map(msg -> (PaintableBlockMessage) msg.getMessageSupplier().get())
				.collect(Collectors.toMap(PaintableBlockMessage::getBlock, PaintableBlockMessage::getTransformer));
		return ImmutableMap.copyOf(map);
	}
}
