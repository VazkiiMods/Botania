/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.regex.Pattern;

public class ItemBlockTinyPotato extends BlockItem {

	private static final Pattern TYPOS = Pattern.compile(
			"(?!^vazkii$)" // Do not match the properly spelled version 
					+ "^v[ao]{1,2}[sz]{0,2}[ak]{1,2}(i){1,2}l{0,2}$",
			Pattern.CASE_INSENSITIVE
	);

	private static final String[] NOT_MY_NAME = {
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

	public ItemBlockTinyPotato(Block block, Settings props) {
		super(block, props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity e, int t, boolean idunno) {
		if (!world.isClient && e instanceof PlayerEntity && e.age % 30 == 0
				&& TYPOS.matcher(stack.getName().getString()).matches()) {
			PlayerEntity player = (PlayerEntity) e;
			int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
			if (ticks < NOT_MY_NAME.length) {
				player.sendSystemMessage(new LiteralText(NOT_MY_NAME[ticks]).formatted(Formatting.RED), Util.NIL_UUID);
				ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
			}
		}
	}

}
