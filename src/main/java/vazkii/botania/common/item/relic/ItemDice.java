/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;

import vazkii.botania.api.item.IRelic;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemDice extends ItemRelic {
	public ItemDice(Settings props) {
		super(props);
	}

	public static Item[] getRelics() {
		return new Item[] {
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
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (isRightPlayer(player, stack)) {
			if (world.isClient) {
				return TypedActionResult.success(stack);
			}

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

			List<Integer> possible = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				if (!hasRelicAlready(player, i)) {
					possible.add(i);
				}
			}

			if (!possible.isEmpty()) {
				int relic = possible.get(world.random.nextInt(possible.size()));
				player.sendSystemMessage(new TranslatableText("botaniamisc.diceRoll", relic + 1).formatted(Formatting.DARK_GREEN), Util.NIL_UUID);
				return TypedActionResult.success(new ItemStack(getRelics()[relic]));
			} else {
				int roll = world.random.nextInt(6) + 1;
				Identifier tableId = ResourceLocationHelper.prefix("dice/roll_" + roll);
				LootTable table = world.getServer().getLootManager().getTable(tableId);
				LootContext context = new LootContext.Builder(((ServerWorld) world))
						.parameter(LootContextParameters.THIS_ENTITY, player)
						.parameter(LootContextParameters.ORIGIN, player.getPos())
						.luck(player.getLuck())
						.build(LootContextTypes.GIFT);

				List<ItemStack> generated = table.generateLoot(context);
				for (ItemStack drop : generated) {
					if (!player.inventory.insertStack(drop)) {
						player.dropItem(drop, false);
					}
				}
				String langKey = generated.isEmpty() ? "botaniamisc.dudDiceRoll" : "botaniamisc.diceRoll";
				player.sendSystemMessage(new TranslatableText(langKey, roll).formatted(Formatting.DARK_GREEN), Util.NIL_UUID);

				stack.decrement(1);
				return TypedActionResult.success(stack);
			}
		}

		return TypedActionResult.pass(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext flags) {
		super.appendTooltip(stack, world, tooltip, flags);
		tooltip.add(new LiteralText(""));
		TooltipHandler.addOnShift(tooltip, () -> {
			String name = stack.getTranslationKey() + ".poem";
			for (int i = 0; i < 4; i++) {
				tooltip.add(new TranslatableText(name + i).formatted(Formatting.GRAY, Formatting.ITALIC));
			}
		});
	}

	@Override
	public boolean shouldDamageWrongPlayer() {
		return false;
	}

	private boolean hasRelicAlready(PlayerEntity player, int relic) {
		if (relic < 0 || relic > 6 || !(player instanceof ServerPlayerEntity)) {
			return true;
		}

		ServerPlayerEntity mpPlayer = (ServerPlayerEntity) player;
		Item item = getRelics()[relic];
		Identifier advId = ((IRelic) item).getAdvancement();

		if (advId != null) {
			Advancement adv = player.world.getServer().getAdvancementLoader().get(advId);
			return adv != null && mpPlayer.getAdvancementTracker().getProgress(adv).isDone();
		}

		return false;
	}

}
