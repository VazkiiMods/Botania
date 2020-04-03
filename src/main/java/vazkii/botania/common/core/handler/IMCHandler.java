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

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.common.impl.BotaniaAPIImpl;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IMCHandler {
	public static void handle(InterModProcessEvent evt) {
		((BotaniaAPIImpl) BotaniaAPI.instance()).oreWeights = handleOreWeights(evt.getIMCStream(IMC.REGISTER_ORE_WEIGHT::equals));
		((BotaniaAPIImpl) BotaniaAPI.instance()).netherOreWeights = handleOreWeights(evt.getIMCStream(IMC.REGISTER_NETHER_ORE_WEIGHT::equals));
	}

	private static Map<ResourceLocation, Integer> handleOreWeights(Stream<InterModComms.IMCMessage> msgs) {
		Map<ResourceLocation, Integer> map = msgs
				.filter(msg -> {
					Object thing = msg.getMessageSupplier().get();
					if (thing instanceof Pair) {
						return ((Pair) thing).getFirst() instanceof ResourceLocation
										&& ((Pair) thing).getSecond() instanceof Integer;
					}
					return false;
				})
				.map(msg -> (Pair<ResourceLocation, Integer>) msg.getMessageSupplier().get())
				.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		return ImmutableMap.copyOf(map);
	}
}
