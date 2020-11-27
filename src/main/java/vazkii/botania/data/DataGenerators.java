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
import net.minecraft.command.CommandSource;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

	public static void registerCommands(CommandDispatcher<ServerCommandSource> disp) {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
			return;
		}

		LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("botania_gendata")
			.then(CommandManager.literal("confirm").executes(ctx -> {
				Path p = inferOutputPath();
				ctx.getSource().sendFeedback(new LiteralText("Generating data into " + p + "..."), false);
				try {
					gatherData(p);
					ctx.getSource().sendFeedback(new LiteralText("Done"), false);
					return Command.SINGLE_SUCCESS;
				} catch (IOException e) {
					Botania.LOGGER.error("Failed to generate data", e);
					ctx.getSource().sendError(new LiteralText("Failed, see logs"));
					return 0;
				}
			}))
			.executes(ctx -> {
				Path p = inferOutputPath();
				Text yes = new LiteralText("[yes]")
					.styled(s -> s.withColor(Formatting.GREEN)
						.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/botania_gendata confirm")));
				Text msg = new LiteralText(String.format("Will generate data into [%s]. Ok? ", p)).append(yes);
				ctx.getSource().sendFeedback(msg, false);
				return Command.SINGLE_SUCCESS;
			});
		disp.register(command);
	}

	private static void gatherData(Path output) throws IOException {
		DataGenerator generator = new DataGenerator(output, Collections.emptyList());
		generator.install(new BlockLootProvider(generator));
		BlockTagProvider blockTagProvider = new BlockTagProvider(generator);
		generator.install(blockTagProvider);
		generator.install(new ItemTagProvider(generator, blockTagProvider));
		generator.install(new EntityTagProvider(generator));
		generator.install(new StonecuttingProvider(generator));
		generator.install(new RecipeProvider(generator));
		generator.install(new SmeltingProvider(generator));
		generator.install(new ElvenTradeProvider(generator));
		generator.install(new ManaInfusionProvider(generator));
		generator.install(new PureDaisyProvider(generator));
		generator.install(new BrewProvider(generator));
		generator.install(new PetalProvider(generator));
		generator.install(new RuneProvider(generator));
		generator.install(new TerraPlateProvider(generator));
		/* todo 1.16-fabric
		if (evt.includeClient()) {
			generator.install(new BlockstateProvider(generator, evt.getExistingFileHelper()));
			generator.install(new FloatingFlowerModelProvider(generator, evt.getExistingFileHelper()));
			generator.install(new ItemModelProvider(generator, evt.getExistingFileHelper()));
		}
		*/
		generator.run();
	}

}
