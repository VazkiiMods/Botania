/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import vazkii.botania.common.Botania;
import vazkii.botania.data.recipes.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class DataGenerators {
	private static Path inferOutputPath() {
		return Paths.get("../src/generated/resources").toAbsolutePath().normalize();
	}

	public static void registerCommands(CommandDispatcher<CommandSourceStack> disp) {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
			return;
		}

		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("botania_gendata")
				.then(Commands.literal("confirm").executes(ctx -> {
					Path p = inferOutputPath();
					ctx.getSource().sendSuccess(new TextComponent("Generating data into " + p + "..."), false);
					try {
						gatherData(p);
						ctx.getSource().sendSuccess(new TextComponent("Done"), false);
						return Command.SINGLE_SUCCESS;
					} catch (Exception e) {
						Botania.LOGGER.error("Failed to generate data", e);
						ctx.getSource().sendFailure(new TextComponent("Failed to generate data, see logs"));
						return 0;
					}
				}))
				.executes(ctx -> {
					Path p = inferOutputPath();
					Component yes = new TextComponent("[yes]")
							.withStyle(s -> s.withColor(ChatFormatting.GREEN)
									.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/botania_gendata confirm")));
					Component msg = new TextComponent(String.format("Will generate data into [%s]. Ok? ", p)).append(yes);
					ctx.getSource().sendSuccess(msg, false);
					return Command.SINGLE_SUCCESS;
				});
		disp.register(command);
	}

	private static void gatherData(Path output) throws IOException {
		DataGenerator generator = new DataGenerator(output, Collections.emptyList());
		gatherData(generator);
		generator.run();
	}

	static void gatherData(DataGenerator generator) {
		generator.addProvider(new BlockLootProvider(generator));
		BlockTagProvider blockTagProvider = new BlockTagProvider(generator);
		generator.addProvider(blockTagProvider);
		generator.addProvider(new ItemTagProvider(generator, blockTagProvider));
		generator.addProvider(new EntityTagProvider(generator));
		generator.addProvider(new StonecuttingProvider(generator));
		generator.addProvider(new RecipeProvider(generator));
		generator.addProvider(new SmeltingProvider(generator));
		generator.addProvider(new ElvenTradeProvider(generator));
		generator.addProvider(new ManaInfusionProvider(generator));
		generator.addProvider(new PureDaisyProvider(generator));
		generator.addProvider(new BrewProvider(generator));
		generator.addProvider(new PetalProvider(generator));
		generator.addProvider(new RuneProvider(generator));
		generator.addProvider(new TerraPlateProvider(generator));
		generator.addProvider(new OrechidProvider(generator));
		generator.addProvider(new BlockstateProvider(generator));
		generator.addProvider(new FloatingFlowerModelProvider(generator));
		generator.addProvider(new ItemModelProvider(generator));
		generator.addProvider(new AdvancementProvider(generator));
	}

}
