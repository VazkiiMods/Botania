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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemDice extends ItemRelic {
	public ItemDice(Properties props) {
		super(props);
	}

	public static Item[] getRelics() {
		return new Item[]{
				ModItems.infiniteFruit,
				ModItems.kingKey,
				ModItems.flugelEye,
				ModItems.thorRing,
				ModItems.odinRing,
				ModItems.lokiRing
		};
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(isRightPlayer(player, stack)) {
			if(world.isRemote)
				return ActionResult.success(stack);

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

			List<Integer> possible = new ArrayList<>();
			for(int i = 0; i < 6; i++) {
				if(!hasRelicAlready(player, i))
					possible.add(i);
			}

			if(possible.isEmpty()) {
				player.sendMessage(new TranslationTextComponent("botaniamisc.dudDiceRoll", world.rand.nextInt(6) + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
				stack.shrink(1);
				return ActionResult.success(stack);
			} else {
				int relic = possible.get(world.rand.nextInt(possible.size()));
				player.sendMessage(new TranslationTextComponent("botaniamisc.diceRoll", relic + 1).setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
				return ActionResult.success(new ItemStack(getRelics()[relic]));
			}
		}

		return ActionResult.pass(stack);
	}

	@Override
	public boolean shouldDamageWrongPlayer() {
		return false;
	}

	private boolean hasRelicAlready(PlayerEntity player, int relic) {
		if(relic < 0 || relic > 6 || !(player instanceof ServerPlayerEntity))
			return true;

		ServerPlayerEntity mpPlayer = (ServerPlayerEntity) player;
		Item item = getRelics()[relic];
		ResourceLocation advId = ((IRelic) item).getAdvancement();

		if(advId != null) {
			Advancement adv = ServerLifecycleHooks.getCurrentServer().getAdvancementManager().getAdvancement(advId);
			return adv != null && mpPlayer.getAdvancements().getProgress(adv).isDone();
		}

		return false;
	}

}
