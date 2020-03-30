/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacketSyncRecipes {
	private Map<ResourceLocation, RecipePetals> petal;
	private Map<ResourceLocation, RecipeRuneAltar> runeAltar;

	public PacketSyncRecipes(Map<ResourceLocation, RecipePetals> petal, Map<ResourceLocation, RecipeRuneAltar> runeAltar) {
		this.petal = petal;
		this.runeAltar = runeAltar;
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(petal.size());
		for (RecipePetals recipe : petal.values()) {
			recipe.write(buf);
		}
		buf.writeVarInt(runeAltar.size());
		for (RecipeRuneAltar recipe : runeAltar.values()) {
			recipe.write(buf);
		}
	}

	public static PacketSyncRecipes decode(PacketBuffer buf) {
		int petalCount = buf.readVarInt();
		Map<ResourceLocation, RecipePetals> petal = Stream.generate(() -> RecipePetals.read(buf))
				.limit(petalCount)
				.collect(Collectors.toMap(RecipePetals::getId, r -> r));
		int runeCount = buf.readVarInt();
		Map<ResourceLocation, RecipeRuneAltar> rune = Stream.generate(() -> RecipeRuneAltar.read(buf))
				.limit(runeCount)
				.collect(Collectors.toMap(RecipeRuneAltar::getId, r -> r));
		return new PacketSyncRecipes(petal, rune);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> {
				if (Minecraft.getInstance().isSingleplayer()) {
					return;
				}
				BotaniaAPI.petalRecipes = petal;
				BotaniaAPI.runeAltarRecipes = runeAltar;
			});
		}
		ctx.get().setPacketHandled(true);
	}

	// The login packet wraps the proper one because directly extending it will cause a freeze on the handshake.
	public static class Login implements ILoginPacket {
		private final PacketSyncRecipes wrapped;
		private int loginIndex;

		public Login() {
			this(new PacketSyncRecipes(BotaniaAPI.petalRecipes, BotaniaAPI.runeAltarRecipes));
		}

		public Login(PacketSyncRecipes packet) {
			this.wrapped = packet;
		}

		public static Login decode(PacketBuffer buf) {
			return new Login(PacketSyncRecipes.decode(buf));
		}

		public void encode(PacketBuffer buf) {
			wrapped.encode(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			wrapped.handle(ctx);
			PacketHandler.HANDLER.reply(new PacketAck(), ctx.get());
		}

		@Override
		public void setLoginIndex(int index) {
			this.loginIndex = index;
		}

		@Override
		public int getLoginIndex() {
			return loginIndex;
		}
	}
}
