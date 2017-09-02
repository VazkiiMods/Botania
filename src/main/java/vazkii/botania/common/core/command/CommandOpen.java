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

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;

import javax.annotation.Nonnull;

public class CommandOpen extends CommandBase {

	@Nonnull
	@Override
	public String getName() {
		return "botania-open";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender) {
		return "<entry>";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemLexicon.class);
			if(!stack.isEmpty()) {
				ItemLexicon.setForcedPage(stack, args[0]);
				ItemLexicon.setQueueTicks(stack, 5);
			} else sender.sendMessage(new TextComponentTranslation("botaniamisc.noLexicon").setStyle(new Style().setColor(TextFormatting.RED)));
		}
	}


	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

}
