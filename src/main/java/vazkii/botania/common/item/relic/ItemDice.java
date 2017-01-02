/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 30, 2015, 6:52:35 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import javax.annotation.Nonnull;

import gnu.trove.list.array.TIntArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDice extends ItemRelic {

	private static final int[] SIDES_FOR_MOON_PHASES = { -1, 0, 1, 2, -1, 2, 3, 4 };

	public static ItemStack[] relicStacks;

	public ItemDice() {
		super(LibItemNames.DICE);

		relicStacks = new ItemStack[] {
				new ItemStack(ModItems.infiniteFruit),
				new ItemStack(ModItems.kingKey),
				new ItemStack(ModItems.flugelEye),
				new ItemStack(ModItems.thorRing),
				new ItemStack(ModItems.odinRing),
				new ItemStack(ModItems.lokiRing)
		};
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(isRightPlayer(player, stack) && !player.world.isRemote) {
			int moonPhase = world.provider.getMoonPhase(world.getWorldTime());
			int relic = SIDES_FOR_MOON_PHASES[moonPhase];
			if(hasRelicAlready(player, relic)) {
				TIntArrayList possible = new TIntArrayList();
				TIntArrayList alreadyHas = new TIntArrayList();
				for(int i = 0; i < 6; i++)
					if(hasRelicAlready(player, i))
						alreadyHas.add(i);
					else possible.add(i);

				if(alreadyHas.size() > 0)
					possible.add(alreadyHas.get(world.rand.nextInt(alreadyHas.size())));
				relic = possible.get(world.rand.nextInt(possible.size()));
			}

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

			if(hasRelicAlready(player, relic)) {
				player.sendMessage(new TextComponentTranslation("botaniamisc.dudDiceRoll", relic + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
				stack.stackSize--;
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}

			player.sendMessage(new TextComponentTranslation("botaniamisc.diceRoll", relic + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
			return ActionResult.newResult(EnumActionResult.SUCCESS, relicStacks[relic].copy());
		}

		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	@Override
	public boolean shouldDamageWrongPlayer() {
		return false;
	}

	private boolean hasRelicAlready(EntityPlayer player, int relic) {
		if(relic < 0 || relic > 5 || !(player instanceof EntityPlayerMP))
			return true;

		EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
		Item item = relicStacks[relic].getItem();
		IRelic irelic = (IRelic) item;
		Achievement achievement = irelic.getBindAchievement();
		return mpPlayer.getStatFile().hasAchievementUnlocked(achievement);
	}

}
