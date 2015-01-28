/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 28, 2015, 4:41:43 PM (GMT)]
 */
package vazkii.botania.common.achievement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class AchievementMod extends Achievement {

	public static List<Achievement> achievements = new ArrayList();
	
	public AchievementMod(String name, int x, int y, ItemStack icon, Achievement parent) {
		super("achievement.botania:" + name, "botania:" + name, x, y, icon, parent);
		achievements.add(this);
		registerStat();
	}
	
	public AchievementMod(String name, int x, int y, Item icon, Achievement parent) {
		this(name, x, y, new ItemStack(icon), parent);
	}
	
	public AchievementMod(String name, int x, int y, Block icon, Achievement parent) {
		this(name, x, y, new ItemStack(icon), parent);
	}

}
