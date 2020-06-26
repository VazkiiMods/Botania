/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.server.ServerWorld;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.world.SkyblockWorldEvents;

public class CommandSkyblockSpread {
	private static final int DEFAULT_RANGE = 200000;

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal("botania-skyblock-spread")
						.requires(s -> s.hasPermissionLevel(2))
						.then(Commands.argument("player", EntityArgument.player())
								.then(Commands.argument("range", IntegerArgumentType.integer(250, 1000000))
										.executes(ctx -> run(ctx, IntegerArgumentType.getInteger(ctx, "range"))))
								.executes(ctx -> run(ctx, DEFAULT_RANGE)))
		);
	}

	private static int run(CommandContext<CommandSource> ctx, int range) throws CommandSyntaxException {
		PlayerEntity player = EntityArgument.getPlayer(ctx, "player");
		int minDist = 100;
		BlockPos spawn = ((ServerWorld) player.world).func_241135_u_();
		int x, z;

		do {
			x = player.world.rand.nextInt(range) - range / 2 + spawn.getX();
			z = player.world.rand.nextInt(range) - range / 2 + spawn.getZ();
		} while (MathHelper.pointDistancePlane(x, z, spawn.getX(), spawn.getZ()) < minDist);

		SkyblockWorldEvents.spawnPlayer(player, new BlockPos(x, spawn.getY(), z), true);
		return 1;
	}
}
