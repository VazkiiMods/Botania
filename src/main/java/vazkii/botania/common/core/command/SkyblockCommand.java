/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.world.IslandPos;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockSavedData;
import vazkii.botania.common.world.SkyblockWorldEvents;

import java.util.UUID;

public class SkyblockCommand {
	private static final SimpleCommandExceptionType NOT_SKYBLOCK_WORLD = new SimpleCommandExceptionType(
			new TranslationTextComponent("botaniamisc.command.skyblock.world"));
	private static final SimpleCommandExceptionType NO_ISLAND = new SimpleCommandExceptionType(
			new TranslationTextComponent("botaniamisc.command.skyblock.noisland"));

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		// This isn't what we consider the "primary" name. It's just here to be a reminder for old /botania-skyblock-spread users.
		// However some Mojang code seems to assume that aliases are made alphabetically...
		LiteralArgumentBuilder<CommandSource> commandBuilder = Commands.literal("botania-skyblock")
				.requires(s -> s.hasPermissionLevel(2))
				.then(Commands.literal("help")
						.executes(SkyblockCommand::printHelp))
				.then(Commands.literal("island")
						.then(Commands.argument("player", EntityArgument.player())
								.executes(SkyblockCommand::createIsland)))
				.then(Commands.literal("spawn")
						.executes(SkyblockCommand::teleportToSpawn))

				.then(Commands.literal("visit")
						.then(Commands.argument("player", EntityArgument.player())
								.executes(ctx -> teleportToIsland(ctx, EntityArgument.getPlayer(ctx, "player")))
						)
						.then(Commands.argument("playerUuid", UUIDArgument.func_239194_a_())
								.suggests((ctx, builder) -> ISuggestionProvider.suggest(
										SkyblockSavedData.get(ctx.getSource().getWorld()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> teleportToIsland(ctx, UUIDArgument.func_239195_a_(ctx, "playerUuid")))
						)
				)

				.then(Commands.literal("regen-island")
						.then(Commands.argument("player", EntityArgument.player())
								.executes(ctx -> rebuildIsland(ctx, EntityArgument.getPlayer(ctx, "player")))
						)
						.then(Commands.argument("playerUuid", UUIDArgument.func_239194_a_())
								.suggests((ctx, builder) -> ISuggestionProvider.suggest(
										SkyblockSavedData.get(ctx.getSource().getWorld()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> rebuildIsland(ctx, UUIDArgument.func_239195_a_(ctx, "playerUuid")))
						)
				);
		LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
		dispatcher.register(Commands.literal("gardenofglass").redirect(command));
		dispatcher.register(Commands.literal("gog").redirect(command));
	}

	private static int printHelp(CommandContext<CommandSource> ctx) {
		for (int i = 0; i < 5; i++) {
			ctx.getSource().sendFeedback(new TranslationTextComponent("botaniamisc.command.skyblock.help." + i), false);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int doTeleportToIsland(CommandContext<CommandSource> ctx, UUID owner, ITextComponent feedback) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().asPlayer();
		return doTeleportToIsland(ctx, player, owner, feedback);
	}

	private static int doTeleportToIsland(CommandContext<CommandSource> ctx, ServerPlayerEntity player, UUID owner, ITextComponent feedback) throws CommandSyntaxException {
		ServerWorld world = getSkyblockWorld(ctx);
		IslandPos pos = getIslandForUUID(owner, SkyblockSavedData.get(world));

		BlockPos blockPos = pos.getCenter();

		player.teleport(world, blockPos.getX() + 0.5, blockPos.getY(),
				blockPos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
		ctx.getSource().sendFeedback(feedback, true);
		return Command.SINGLE_SUCCESS;
	}

	private static int createIsland(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgument.getPlayer(ctx, "player");
		SkyblockSavedData data = SkyblockSavedData.get(getSkyblockWorld(ctx));
		UUID uuid = player.getUniqueID();

		if (data.skyblocks.containsValue(uuid)) {
			doTeleportToIsland(ctx, player, uuid, new TranslationTextComponent("botaniamisc.command.skyblock.island.teleported",
					ctx.getSource().getDisplayName()));
			return Command.SINGLE_SUCCESS;
		}

		SkyblockWorldEvents.spawnPlayer(player, data.create(uuid));
		ctx.getSource().sendFeedback(new TranslationTextComponent("botaniamisc.command.skyblock.island.success", player.getDisplayName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int doRebuildIsland(CommandContext<CommandSource> ctx, UUID player, ITextComponent feedback) throws CommandSyntaxException {
		ServerWorld world = getSkyblockWorld(ctx);
		IslandPos pos = getIslandForUUID(player, SkyblockSavedData.get(world));

		SkyblockWorldEvents.createSkyblock(world, pos.getCenter());
		ctx.getSource().sendFeedback(feedback, true);
		return Command.SINGLE_SUCCESS;
	}

	// Helper methods
	private static IslandPos getIslandForUUID(UUID player, SkyblockSavedData data) throws CommandSyntaxException {
		IslandPos pos = data.skyblocks.inverse().get(player);
		if (pos == null) {
			throw NO_ISLAND.create();
		}
		return pos;
	}

	private static ServerWorld getSkyblockWorld(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerWorld world = ctx.getSource().getWorld();
		if (!SkyblockChunkGenerator.isWorldSkyblock(world)) {
			throw NOT_SKYBLOCK_WORLD.create();
		}
		return world;
	}

	// Translation feedback helpers
	private static int teleportToIsland(CommandContext<CommandSource> ctx, PlayerEntity owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner.getUniqueID(), new TranslationTextComponent("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner.getName()));
	}

	private static int teleportToIsland(CommandContext<CommandSource> ctx, UUID owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner, new TranslationTextComponent("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner));
	}

	private static int teleportToSpawn(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, Util.DUMMY_UUID, new TranslationTextComponent("botaniamisc.command.skyblock.spawn.success",
				ctx.getSource().getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<CommandSource> ctx, ServerPlayerEntity owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner.getUniqueID(), new TranslationTextComponent("botaniamisc.command.skyblock.regenisland.success", owner.getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<CommandSource> ctx, UUID owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner, new TranslationTextComponent("botaniamisc.command.skyblock.regenisland.success", owner));
	}
}
