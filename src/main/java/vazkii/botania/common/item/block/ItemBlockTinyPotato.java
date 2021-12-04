/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.ModPatterns;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.regex.Pattern;

public class ItemBlockTinyPotato extends BlockItem /*implements LoomPatternProvider*/ {

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

	public ItemBlockTinyPotato(Block block, Properties props) {
		super(block, props);
	}

	/*
	@Override
	public LoomPattern getPattern() {
		return ModPatterns.TINY_POTATO;
	}
	 */

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity e, int t, boolean idunno) {
		if (!world.isClientSide && e instanceof Player player && e.tickCount % 30 == 0
				&& TYPOS.matcher(stack.getHoverName().getString()).matches()) {
			int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
			if (ticks < NOT_MY_NAME.length) {
				player.sendMessage(new TextComponent(NOT_MY_NAME[ticks]).withStyle(ChatFormatting.RED), Util.NIL_UUID);
				ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
			}
		}
	}

	/* todo fabric
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.HEAD && entity instanceof PlayerEntity
				&& ContributorList.hasFlower(((PlayerEntity) entity).getGameProfile().getName().toLowerCase(Locale.ROOT));
	}
	*/
}
