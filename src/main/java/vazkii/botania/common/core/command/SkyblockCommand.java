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
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.world.IslandPos;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockSavedData;
import vazkii.botania.common.world.SkyblockWorldEvents;

import java.util.UUID;

public class SkyblockCommand {
	private static final SimpleCommandExceptionType NOT_SKYBLOCK_WORLD = new SimpleCommandExceptionType(
			new TranslatableText("botaniamisc.command.skyblock.world"));
	private static final SimpleCommandExceptionType NO_ISLAND = new SimpleCommandExceptionType(
			new TranslatableText("botaniamisc.command.skyblock.noisland"));

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		// This isn't what we consider the "primary" name. It's just here to be a reminder for old /botania-skyblock-spread users.
		// However some Mojang code seems to assume that aliases are made alphabetically...
		LiteralArgumentBuilder<ServerCommandSource> commandBuilder = CommandManager.literal("botania-skyblock")
				.requires(s -> s.hasPermissionLevel(2))
				.then(CommandManager.literal("help")
						.executes(SkyblockCommand::printHelp))
				.then(CommandManager.literal("island")
						.then(CommandManager.argument("player", EntityArgumentType.player())
								.executes(SkyblockCommand::createIsland)))
				.then(CommandManager.literal("spawn")
						.executes(SkyblockCommand::teleportToSpawn))

				.then(CommandManager.literal("visit")
						.then(CommandManager.argument("player", EntityArgumentType.player())
								.executes(ctx -> teleportToIsland(ctx, EntityArgumentType.getPlayer(ctx, "player")))
						)
						.then(CommandManager.argument("playerUuid", UuidArgumentType.uuid())
								.suggests((ctx, builder) -> CommandSource.suggestMatching(
										SkyblockSavedData.get(ctx.getSource().getWorld()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> teleportToIsland(ctx, UuidArgumentType.getUuid(ctx, "playerUuid")))
						)
				)

				.then(CommandManager.literal("regen-island")
						.then(CommandManager.argument("player", EntityArgumentType.player())
								.executes(ctx -> rebuildIsland(ctx, EntityArgumentType.getPlayer(ctx, "player")))
						)
						.then(CommandManager.argument("playerUuid", UuidArgumentType.uuid())
								.suggests((ctx, builder) -> CommandSource.suggestMatching(
										SkyblockSavedData.get(ctx.getSource().getWorld()).skyblocks
												.values().stream().map(UUID::toString),
										builder))
								.executes(ctx -> rebuildIsland(ctx, UuidArgumentType.getUuid(ctx, "playerUuid")))
						)
				);
		LiteralCommandNode<ServerCommandSource> command = dispatcher.register(commandBuilder);
		dispatcher.register(CommandManager.literal("gardenofglass").redirect(command));
		dispatcher.register(CommandManager.literal("gog").redirect(command));
	}

	private static int printHelp(CommandContext<ServerCommandSource> ctx) {
		for (int i = 0; i < 5; i++) {
			ctx.getSource().sendFeedback(new TranslatableText("botaniamisc.command.skyblock.help." + i), false);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int doTeleportToIsland(CommandContext<ServerCommandSource> ctx, UUID owner, Text feedback) throws CommandSyntaxException {
		ServerWorld world = getSkyblockWorld(ctx);
		IslandPos pos = getIslandForUUID(owner, SkyblockSavedData.get(world));

		ServerPlayerEntity player = ctx.getSource().getPlayer();
		BlockPos blockPos = pos.getCenter();

		player.teleport(world, blockPos.getX() + 0.5, blockPos.getY(),
				blockPos.getZ() + 0.5, player.yaw, player.pitch);
		ctx.getSource().sendFeedback(feedback, true);
		return Command.SINGLE_SUCCESS;
	}

	private static int createIsland(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
		SkyblockSavedData data = SkyblockSavedData.get(getSkyblockWorld(ctx));
		UUID uuid = player.getUuid();

		if (data.skyblocks.containsValue(uuid)) {
			doTeleportToIsland(ctx, uuid, new TranslatableText("botaniamisc.command.skyblock.island.teleported",
					ctx.getSource().getDisplayName()));
			return Command.SINGLE_SUCCESS;
		}

		SkyblockWorldEvents.spawnPlayer(player, data.create(uuid));
		ctx.getSource().sendFeedback(new TranslatableText("botaniamisc.command.skyblock.island.success", player.getDisplayName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int doRebuildIsland(CommandContext<ServerCommandSource> ctx, UUID player, Text feedback) throws CommandSyntaxException {
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

	private static ServerWorld getSkyblockWorld(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerWorld world = ctx.getSource().getWorld();
		if (!SkyblockChunkGenerator.isWorldSkyblock(world)) {
			throw NOT_SKYBLOCK_WORLD.create();
		}
		return world;
	}

	// Translation feedback helpers
	private static int teleportToIsland(CommandContext<ServerCommandSource> ctx, PlayerEntity owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner.getUuid(), new TranslatableText("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner.getName()));
	}

	private static int teleportToIsland(CommandContext<ServerCommandSource> ctx, UUID owner) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, owner, new TranslatableText("botaniamisc.command.skyblock.teleport.success",
				ctx.getSource().getDisplayName(), owner));
	}

	private static int teleportToSpawn(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		return doTeleportToIsland(ctx, Util.NIL_UUID, new TranslatableText("botaniamisc.command.skyblock.spawn.success",
				ctx.getSource().getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner.getUuid(), new TranslatableText("botaniamisc.command.skyblock.regenisland.success", owner.getDisplayName()));
	}

	private static int rebuildIsland(CommandContext<ServerCommandSource> ctx, UUID owner) throws CommandSyntaxException {
		return doRebuildIsland(ctx, owner, new TranslatableText("botaniamisc.command.skyblock.regenisland.success", owner));
	}
}
