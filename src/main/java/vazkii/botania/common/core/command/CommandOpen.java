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

public class CommandOpen extends CommandBase {

	@Override
	public String getCommandName() {
		return "botania-open";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "<entry>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, ItemLexicon.class);
			if(stack != null) {
				ItemLexicon.setForcedPage(stack, args[0]);
				ItemLexicon.setQueueTicks(stack, 5);
			} else sender.addChatMessage(new TextComponentTranslation("botaniamisc.noLexicon").setStyle(new Style().setColor(TextFormatting.RED)));
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
