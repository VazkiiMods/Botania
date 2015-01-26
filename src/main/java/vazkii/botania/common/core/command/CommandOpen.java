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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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
	public void processCommand(ICommandSender sender, String[] args) {
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.getItem() instanceof ItemLexicon) {
				ItemLexicon.setForcedPage(stack, args[0]);
				ItemLexicon.setQueueTicks(stack, 5);
			} else sender.addChatMessage(new ChatComponentTranslation("botaniamisc.noLexicon").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		}
	}


	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return p_71519_1_ instanceof EntityPlayer;
	}

}
