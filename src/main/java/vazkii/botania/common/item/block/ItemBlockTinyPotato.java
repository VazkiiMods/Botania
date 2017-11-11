/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 27, 2015, 3:44:10 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.Arrays;
import java.util.List;

public class ItemBlockTinyPotato extends ItemBlockMod {

	private static final List<String> TYPOS = Arrays.asList("vaskii", "vazki", "voskii", "vazkkii", "vazkki", "vazzki", "vaskki", "vozkii", "vazkil", "vaskil", "vazkill", "vaskill", "vaski");

	private static final String NOT_MY_NAME[] = {
			"Six letter word just to get me along",
			"It's a intricacy and I'm coding on my mod and I,",
			"I keep fixin', and keepin' it together",
			"People around gotta find something to play now",
			"Holding back, every mod's the same",
			"Don't wanna be a loser",
			"Listen to me, oh no, I don't break anything at all",
			"But with nothing to consider they forget my name",
			"'ame, 'ame, 'ame",
			"They call me Vaskii",
			"They call me Vazki",
			"They call me Voskii",
			"They call me Vazkki",
			"That's not my name",
			"That's not my name",
			"That's not my name",
			"That's not my name"
	};

	private static final String TAG_TICKS = "notMyNameTicks";

	public ItemBlockTinyPotato(Block block) {
		super(block);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int t, boolean idunno) {
		if(!world.isRemote && e instanceof EntityPlayer && e.ticksExisted % 30 == 0 && TYPOS.contains(stack.getDisplayName().toLowerCase())) {
			EntityPlayer player = (EntityPlayer) e;
			int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
			if(ticks < NOT_MY_NAME.length) {
				player.sendMessage(new TextComponentString(TextFormatting.RED + NOT_MY_NAME[ticks]));
				ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
			}
		}
	}

}
