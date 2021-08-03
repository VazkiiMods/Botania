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

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.common.world.IslandPos;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockSavedData;
import vazkii.botania.common.world.SkyblockWorldEvents;

import java.util.UUID;

public class SkyblockCommand {
	private static final SimpleCommandExceptionType NOT_SKYBLOCK_WORLD = new SimpleCommandExceptionType(
			new TranslatableComponent("botaniamisc.command.skyblock.world"));
	private static final SimpleCommandExceptionType NO_ISLAND = new SimpleCommandExceptionType(
			new TranslatableComponent("botaniamisc.command.skyblock.noisland"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		// This isn't what we consider the "primary" name. It's just here to be a reminder for old /botania-skyblock-spread users.
		// However some Mojang code seems to assume that aliases are made alphabetically...
		LiteralArgumentBuilder<CommandSourceStack> commandBuilder = Commands.literal("botania-skyblock")
				.requires(s -> s.hasPermission(2))
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
						.then(Commands.argument("playerUuid", UuidArgument.uuid())
								.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
										SkyblockSavedData.get(ctx.getSource().getLevel()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> teleportToIsland(ctx, UuidArgument.getUuid(ctx, "playerUuid")))
						)
				)

				.then(Commands.literal("regen-island")
						.then(Commands.argument("player", EntityArgument.player())
								.executes(ctx -> rebuildIsland(ctx, EntityArgument.getPlayer(ctx, "player")))
						)
						.then(Commands.argument("playerUuid", UuidArgument.uuid())
								.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
										SkyblockSavedData.get(ctx.getSource().getLevel()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> rebuildIsland(ctx, UuidArgument.getUuid(ctx, "playerUuid")))
						)
				);
		LiteralCommandNode<CommandSourceStack> command = dispatcher.register(commandBuilder);
		dispatcher.register(Commands.literal("gardenofglass").redirect(command));
		dispatcher.register(Commands.literal("gog").redirect(command));
	}

	private static int printHelp(CommandContext<CommandSourceStack> ctx) {
		for (int i = 0; i < 5; i++) {
			ctx.getSource().sendSuccess(new TranslatableComponent("botaniamisc.command.skyblock.help." + i), false);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int doTeleportToIsland(CommandContext<CommandSourceStack> ctx, UUID owner, Component feedback) throws CommandSyntaxException {
		ServerPlayer player = ctx.getSource().getPlayerOrException();
		return doTeleportToIsland(ctx, player, owner, feedback);
	}

	private static int doTeleportToIsland(CommandContext<CommandSourceStack> ctx, ServerPlayer player, UUID owner, Component feedback) throws CommandSyntaxException {
		ServerLevel world = getSkyblockWorld(ctx);
		IslandPos pos = getIslandForUUID(owner, SkyblockSavedData.get(world));

		BlockPos blockPos = pos.getCenter();

		player.teleportTo(world, blockPos.getX() + 0.5, blockPos.getY(),
				blockPos.getZ() + 0.5, player.yRot, player.xRot);
		ctx.getSource().sendSuccess(feedback, true);
		return Command.SINGLE_SUCCESS;
	}

	private static int createIsland(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
		SkyblockSavedData data = SkyblockSavedData.get(getSkyblockWorld(ctx));
		UUID uuid = player.getUUID();

		if (data.skyblocks.containsValue(uuid)) {
			doTeleportToIsland(ctx, player, uuid, new TranslatableComponent("botaniamisc.command.skyblock.island.teleported",
					ctx.getSource().getDisplayName()));
			return Command.SINGLE_SUCCESS;
		}

		SkyblockWorldEvents.spawnPlayer(player, data.create(uuid));
		ctx.getSource().sendSuccess(new TranslatableComponent("botaniamisc.command.skyblock.island.success", player.getDisplayName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int doRebuildIsland(CommandContext<CommandSourceStack> ctx, UUID player, Component feedback) throws CommandSyntaxException {
		ServerLevel world = getSkyblockWorld(ctx);
		IslandPos pos = getIslandForUUID(player, SkyblockSavedData.get(world));

		SkyblockWorldEvents.createSkyblock(world, pos.getCenter());
		ctx.getSource().sendSuccess(feedback, true);
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

	private static ServerLevel getSkyblockWorld(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerLevel world = ctx.getSource().getLevel();
		if (!SkyblockChunkGenerator.isWorldSkyblock(world)) {
			throw NOT_SKYBLOCK_WORLD.create();
		}
		return world;
	}

	// Translation feedback helpers
	private static int teleportToIsland(CommandContext<CommandSourceStack> ctx, Player owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner.getUUID(), new TranslatableComponent("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner.getName()));
	}

	private static int teleportToIsland(CommandContext<CommandSourceStack> ctx, UUID owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner, new TranslatableComponent("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner));
	}

	private static int teleportToSpawn(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, Util.NIL_UUID, new TranslatableComponent("botaniamisc.command.skyblock.spawn.success",
				ctx.getSource().getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<CommandSourceStack> ctx, ServerPlayer owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner.getUUID(), new TranslatableComponent("botaniamisc.command.skyblock.regenisland.success", owner.getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<CommandSourceStack> ctx, UUID owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner, new TranslatableComponent("botaniamisc.command.skyblock.regenisland.success", owner));
	}
}
