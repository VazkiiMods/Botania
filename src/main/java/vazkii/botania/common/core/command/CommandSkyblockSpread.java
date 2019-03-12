/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 17, 2015, 5:13:06 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
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
								.executes(ctx -> run(ctx.getSource().asPlayer(), IntegerArgumentType.getInteger(ctx, "range"))))
							.executes(ctx -> run(ctx.getSource().asPlayer(), DEFAULT_RANGE)))
		);
	}

	private static int run(EntityPlayerMP player, int range) {
		int minDist = 100;
		BlockPos spawn = player.world.getSpawnPoint();
		int x, z;

		do {
			x = player.world.rand.nextInt(range) - range / 2 + spawn.getX();
			z = player.world.rand.nextInt(range) - range / 2 + spawn.getZ();
		} while(MathHelper.pointDistancePlane(x, z, spawn.getX(), spawn.getZ()) < minDist);

		SkyblockWorldEvents.spawnPlayer(player, new BlockPos(x, spawn.getY(), z), true);
		return 1;
	}
}
