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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDice extends ItemRelic {

	private static final int[] SIDES_FOR_MOON_PHASES = new int[] {
		-1, 0, 1, 2, -1, 2, 3, 4
	};

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

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(isRightPlayer(player, stack) && !player.worldObj.isRemote) {
			int moonPhase = world.provider.getMoonPhase(world.getWorldTime());
			int side = SIDES_FOR_MOON_PHASES[moonPhase];
			int relic = side;
			if(hasRelicAlready(player, relic)) {
				List<Integer> possible = new ArrayList();
				List<Integer> alreadyHas = new ArrayList();
				for(int i = 0; i < 6; i++)
					if(hasRelicAlready(player, i))
						alreadyHas.add(i);
					else possible.add(i);

				if(alreadyHas.size() > 0)
					possible.add(alreadyHas.get(world.rand.nextInt(alreadyHas.size())));
				relic = possible.get(world.rand.nextInt(possible.size()));
			}

			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

			if(hasRelicAlready(player, relic)) {
				player.addChatMessage(new ChatComponentTranslation("botaniamisc.dudDiceRoll", relic + 1).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
				stack.stackSize--;
				return stack;
			}

			player.addChatMessage(new ChatComponentTranslation("botaniamisc.diceRoll", relic + 1).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
			return relicStacks[relic].copy();
		}

		return stack;
	}

	@Override
	public boolean shouldDamageWrongPlayer() {
		return false;
	}

	boolean hasRelicAlready(EntityPlayer player, int relic) {
		if(relic < 0 || relic > 5 || !(player instanceof EntityPlayerMP))
			return true;

		EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
		Item item = relicStacks[relic].getItem();
		IRelic irelic = (IRelic) item;
		Achievement achievement = irelic.getBindAchievement();
		return mpPlayer.func_147099_x().hasAchievementUnlocked(achievement);
	}

}
