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
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacketSyncRecipes {
	private Map<ResourceLocation, RecipeBrew> brew;
	private Map<ResourceLocation, RecipeManaInfusion> manaInfusion;
	private Map<ResourceLocation, RecipePetals> petal;
	private Map<ResourceLocation, RecipePureDaisy> pureDaisy;
	private Map<ResourceLocation, RecipeRuneAltar> runeAltar;

	public PacketSyncRecipes(Map<ResourceLocation, RecipeBrew> brew, Map<ResourceLocation, RecipeManaInfusion> manaInfusion, Map<ResourceLocation, RecipePetals> petal, Map<ResourceLocation, RecipePureDaisy> pureDaisy, Map<ResourceLocation, RecipeRuneAltar> runeAltar) {
		this.brew = brew;
		this.manaInfusion = manaInfusion;
		this.petal = petal;
		this.pureDaisy = pureDaisy;
		this.runeAltar = runeAltar;
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(brew.size());
		for (RecipeBrew recipe : brew.values()) {
			recipe.write(buf);
		}
		buf.writeVarInt(manaInfusion.size());
		for (RecipeManaInfusion recipe : manaInfusion.values()) {
			recipe.write(buf);
		}
		buf.writeVarInt(petal.size());
		for (RecipePetals recipe : petal.values()) {
			recipe.write(buf);
		}
		buf.writeVarInt(pureDaisy.size());
		for (RecipePureDaisy recipe : pureDaisy.values()) {
			recipe.write(buf);
		}
		buf.writeVarInt(runeAltar.size());
		for (RecipeRuneAltar recipe : runeAltar.values()) {
			recipe.write(buf);
		}
	}

	public static PacketSyncRecipes decode(PacketBuffer buf) {
		int brewCount = buf.readVarInt();
		Map<ResourceLocation, RecipeBrew> brew = Stream.generate(() -> RecipeBrew.read(buf))
				.limit(brewCount)
				.collect(Collectors.toMap(RecipeBrew::getId, r -> r));
		int manaInfusionCount = buf.readVarInt();
		Map<ResourceLocation, RecipeManaInfusion> manaInfusion = Stream.generate(() -> RecipeManaInfusion.read(buf))
				.limit(manaInfusionCount)
				.collect(Collectors.toMap(RecipeManaInfusion::getId, r -> r));
		int petalCount = buf.readVarInt();
		Map<ResourceLocation, RecipePetals> petal = Stream.generate(() -> RecipePetals.read(buf))
				.limit(petalCount)
				.collect(Collectors.toMap(RecipePetals::getId, r -> r));
		int pureDaisyCount = buf.readVarInt();
		Map<ResourceLocation, RecipePureDaisy> pureDaisy = Stream.generate(() -> RecipePureDaisy.read(buf))
				.limit(pureDaisyCount)
				.collect(Collectors.toMap(RecipePureDaisy::getId, r -> r));
		int runeCount = buf.readVarInt();
		Map<ResourceLocation, RecipeRuneAltar> rune = Stream.generate(() -> RecipeRuneAltar.read(buf))
				.limit(runeCount)
				.collect(Collectors.toMap(RecipeRuneAltar::getId, r -> r));
		return new PacketSyncRecipes(brew, manaInfusion, petal, pureDaisy, rune);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> {
				if (Minecraft.getInstance().isSingleplayer()) {
					return;
				}
				BotaniaAPI.brewRecipes = brew;
				BotaniaAPI.manaInfusionRecipes = manaInfusion;
				BotaniaAPI.petalRecipes = petal;
				BotaniaAPI.pureDaisyRecipes = pureDaisy;
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
			this(new PacketSyncRecipes(BotaniaAPI.brewRecipes,
					BotaniaAPI.manaInfusionRecipes, BotaniaAPI.petalRecipes,
					BotaniaAPI.pureDaisyRecipes, BotaniaAPI.runeAltarRecipes));
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
