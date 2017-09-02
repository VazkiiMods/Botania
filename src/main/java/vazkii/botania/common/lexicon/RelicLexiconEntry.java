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

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class RelicLexiconEntry extends BasicLexiconEntry {
	private final ResourceLocation advancement;

	public RelicLexiconEntry(String unlocalizedName, LexiconCategory category, ResourceLocation advancement) {
		super(unlocalizedName, category);
		setKnowledgeType(BotaniaAPI.relicKnowledge);
		this.advancement = advancement;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isVisible() {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if(advancement == null || player.capabilities.isCreativeMode) {
			return true;
		} else {
			Advancement adv = player.connection.getAdvancementManager().getAdvancementList().getAdvancement(advancement);
			AdvancementProgress progress = player.connection.getAdvancementManager().advancementToProgress.get(adv);
			return progress != null && progress.isDone();
		}
	}

}
