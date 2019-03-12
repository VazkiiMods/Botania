/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2014, 9:50:14 PM (GMT)]
 */
package vazkii.botania.common.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;

public class CommandOpen {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			Commands.literal("botania-open")
					.then(Commands.argument("entry", StringArgumentType.string())
							.executes(CommandOpen::run))
		);
	}

	private static int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntityPlayerMP player = ctx.getSource().asPlayer();
		ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemLexicon.class);
		if(!stack.isEmpty()) {
			ItemLexicon.setForcedPage(stack, StringArgumentType.getString(ctx, "entry"));
			ItemLexicon.setQueueTicks(stack, 5);
			return 1;
		} else {
			ctx.getSource().sendErrorMessage(new TextComponentTranslation("botaniamisc.noLexicon"));
			return 0;
		}
	}

}
