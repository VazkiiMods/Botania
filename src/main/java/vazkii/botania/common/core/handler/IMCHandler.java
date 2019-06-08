package vazkii.botania.common.core.handler;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.IRegistryDelegate;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.ModWikiMessage;
import vazkii.botania.api.imc.OreWeightMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.wiki.IWikiProvider;
import vazkii.botania.api.wiki.WikiHooks;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IMCHandler {
	public static void handle(InterModProcessEvent evt) {
		BotaniaAPI.oreWeights = handleOreWeights(evt.getIMCStream(IMC.REGISTER_ORE_WEIGHT::equals));
		BotaniaAPI.oreWeightsNether = handleOreWeights(evt.getIMCStream(IMC.REGISTER_NETHER_ORE_WEIGHT::equals));
		WikiHooks.modWikis = handleWikis(evt.getIMCStream(IMC.REGISTER_MOD_WIKI::equals));
		BotaniaAPI.paintableBlocks = handlePaintable(evt.getIMCStream(IMC.REGISTER_PAINTABLE_BLOCK::equals));
		BotaniaAPI.manaInfusionRecipes = handleManaInfusion(evt.getIMCStream(IMC.REGISTER_MANA_INFUSION::equals));
	}

	private static Map<String, IWikiProvider> handleWikis(Stream<InterModComms.IMCMessage> msgs) {
		Map<String, IWikiProvider> map = msgs
				.filter(msg -> msg.getMessageSupplier().get() instanceof ModWikiMessage)
				.map(msg -> (ModWikiMessage) msg.getMessageSupplier().get())
				.collect(Collectors.toMap(ModWikiMessage::getModId, ModWikiMessage::getWiki));
		return ImmutableMap.copyOf(map);
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

	private static Map<ResourceLocation, RecipeManaInfusion> handleManaInfusion(Stream<InterModComms.IMCMessage> msgs) {
		Map<ResourceLocation, RecipeManaInfusion> map = msgs
				.filter(msg -> msg.getMessageSupplier().get() instanceof RecipeManaInfusion)
				.map(msg -> (RecipeManaInfusion) msg.getMessageSupplier().get())
				.collect(Collectors.toMap(RecipeManaInfusion::getId, r -> r));
		return ImmutableMap.copyOf(map);
	}
}
