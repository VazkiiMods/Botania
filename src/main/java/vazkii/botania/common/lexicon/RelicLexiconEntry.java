/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 16, 2015, 10:52:36 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class RelicLexiconEntry extends BasicLexiconEntry {

	final Achievement a;

	public RelicLexiconEntry(String unlocalizedName, LexiconCategory category, Achievement a) {
		super(unlocalizedName, category);
		setKnowledgeType(BotaniaAPI.relicKnowledge);
		this.a = a;
		if(a != null)
			setIcon(a.theItemStack.copy());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isVisible() {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		return a == null || player.capabilities.isCreativeMode || player.getStatFileWriter().hasAchievementUnlocked(a);
	}

}
