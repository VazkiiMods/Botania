/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.impl.BotaniaAPIImpl;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacketOrechidData {
	private final List<OrechidOutput> normal;
	private final List<OrechidOutput> nether;

	private PacketOrechidData(List<OrechidOutput> normal, List<OrechidOutput> nether) {
		this.normal = normal;
		this.nether = nether;
	}

	public static PacketOrechidData create() {
		return new PacketOrechidData(BotaniaAPI.instance().getOrechidWeights(), BotaniaAPI.instance().getNetherOrechidWeights());
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(normal.size());
		for (OrechidOutput output : normal) {
			output.getOutput().write(buf);
			buf.writeVarInt(output.getWeight());
		}
		buf.writeVarInt(nether.size());
		for (OrechidOutput output : nether) {
			output.getOutput().write(buf);
			buf.writeVarInt(output.getWeight());
		}
	}

	public static PacketOrechidData decode(PacketBuffer buf) {
		int count = buf.readVarInt();
		List<OrechidOutput> normal = Stream.generate(() -> {
			StateIngredient ingr = StateIngredientHelper.read(buf);
			int weight = buf.readVarInt();
			return new OrechidOutput(weight, ingr);
		}).limit(count).collect(Collectors.toList());
		count = buf.readVarInt();
		List<OrechidOutput> nether = Stream.generate(() -> {
			StateIngredient ingr = StateIngredientHelper.read(buf);
			int weight = buf.readVarInt();
			return new OrechidOutput(weight, ingr);
		}).limit(count).collect(Collectors.toList());
		return new PacketOrechidData(normal, nether);
	}

	public static void handle(PacketOrechidData msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			ctx.get().enqueueWork(() -> {
				Botania.LOGGER.debug("Received orechid data with {} standard and {} nether results", msg.normal.size(), msg.nether.size());
				BotaniaAPIImpl.weights = msg.normal;
				BotaniaAPIImpl.netherWeights = msg.nether;
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
