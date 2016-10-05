/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 3, 2015, 5:55:36 PM (GMT)]
 */
package vazkii.botania.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.stats.StatisticsManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.achievement.ModAchievements;

public class GuiAchievementsHacky extends GuiAchievements {

	public GuiAchievementsHacky(GuiScreen screen, StatisticsManager stats) {
		super(screen, stats);
		ReflectionHelper.setPrivateValue(GuiAchievements.class, this, ModAchievements.pageIndex, "currentPage");
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.get(1).displayString = ModAchievements.botaniaPage.getName();
	}

}
