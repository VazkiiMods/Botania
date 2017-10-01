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

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemDice extends ItemRelic {
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(isRightPlayer(player, stack)) {
			if(world.isRemote)
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

			List<Integer> possible = new ArrayList<>();
			for(int i = 0; i < 6; i++) {
				if(!hasRelicAlready(player, i))
					possible.add(i);
			}

			if(possible.isEmpty()) {
				player.sendMessage(new TextComponentTranslation("botaniamisc.dudDiceRoll", world.rand.nextInt(6) + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
				stack.shrink(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			} else {
				int relic = possible.get(world.rand.nextInt(possible.size()));
				player.sendMessage(new TextComponentTranslation("botaniamisc.diceRoll", relic + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
				return ActionResult.newResult(EnumActionResult.SUCCESS, relicStacks[relic].copy());
			}
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean shouldDamageWrongPlayer() {
		return false;
	}

	private boolean hasRelicAlready(EntityPlayer player, int relic) {
		if(relic < 0 || relic > relicStacks.length || !(player instanceof EntityPlayerMP))
			return true;

		EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
		Item item = relicStacks[relic].getItem();
		ResourceLocation advId = ((IRelic) item).getAdvancement();

		if(advId != null) {
			Advancement adv = mpPlayer.getServerWorld().getAdvancementManager().getAdvancement(advId);
			return adv != null && mpPlayer.getAdvancements().getProgress(adv).isDone();
		}

		return false;
	}

}
