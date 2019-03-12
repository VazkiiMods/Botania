/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2014, 8:06:49 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandShare {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
				Commands.literal("botania-share")
						.then(Commands.argument("entry", StringArgumentType.string())
								.executes(CommandShare::run))
		);
	}

	private static int run(CommandContext<CommandSource> ctx) {
		// todo 1.13
		/*
		String json = I18n.translateToLocal("botaniamisc.shareMsg");
		json = json.replaceAll("%name%", sender.getName());
		json = json.replaceAll("%entry%", args[0]);
		json = json.replaceAll("%entryname%", I18n.translateToLocal(StringArgumentType.getString(ctx, "entry")));

		ITextComponent component = ITextComponent.Serializer.jsonToComponent(json);
		ctx.getSource().getServer().getPlayerList().sendMessage(component);
		*/
		return 1;
	}
}
