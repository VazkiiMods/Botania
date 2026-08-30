/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import vazkii.botania.common.item.relic.FruitOfGrisaiaItem;
import vazkii.botania.common.item.rod.SkiesRodItem;
import vazkii.botania.network.TriConsumer;

import java.util.Arrays;
import java.util.Locale;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaItemProperties {
	public static void init(TriConsumer<Item, ResourceLocation, ClampedItemPropertyFunction> consumer) {
		consumer.accept(BotaniaItems.TRINKET_CASE, botaniaRL("open"),
				(stack, world, entity, seed) -> stack.has(BotaniaDataComponents.ACTIVE_TRANSIENT) ? 1 : 0);
		consumer.accept(BotaniaItems.BLACK_HOLE_TALISMAN, botaniaRL("active"),
				(stack, world, entity, seed) -> stack.has(BotaniaDataComponents.ACTIVE) ? 1 : 0);
		consumer.accept(BotaniaItems.MANA_IN_A_BOTTLE, botaniaRL("swigs_taken"),
				(stack, world, entity, seed) -> {
					int swigsLeft = BottledManaItem.getSwigsLeft(stack) - 1;
					int totalSwigs = BottledManaItem.SWIGS - 1;
					return swigsLeft == totalSwigs ? 0.0F : Math.nextUp((totalSwigs - swigsLeft) / (float) totalSwigs);
				});

		ResourceLocation vuvuzelaId = botaniaRL("vuvuzela");
		ClampedItemPropertyFunction isVuvuzela = (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0;
		// same as goat horn:
		ResourceLocation tootingId = ResourceLocation.withDefaultNamespace("tooting");
		ClampedItemPropertyFunction isTooting = (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0;
		for (HornItem hornItem : Arrays.asList(BotaniaItems.HORN_OF_THE_WILD, BotaniaItems.HORN_OF_THE_CANOPY, BotaniaItems.HORN_OF_THE_COVERING)) {
			consumer.accept(hornItem, vuvuzelaId, isVuvuzela);
			consumer.accept(hornItem, tootingId, isTooting);
		}

		consumer.accept(BotaniaItems.LEXICA_BOTANIA, botaniaRL("elven"), (stack, world, living, seed) -> LexicaBotaniaItem.isElven(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.BISCUIT_OF_TOTALITY, botaniaRL("totalbiscuit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1 : 0);
		consumer.accept(BotaniaItems.SLIME_IN_A_BOTTLE, botaniaRL("active"),
				(stack, world, entity, seed) -> stack.has(BotaniaDataComponents.ACTIVE_TRANSIENT) ? 1 : 0);
		consumer.accept(BotaniaItems.LIFE_AGGREGATOR, botaniaRL("full"),
				(stack, world, entity, seed) -> LifeAggregatorItem.hasData(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.STONE_OF_TEMPERANCE, botaniaRL("active"),
				(stack, world, entity, seed) -> stack.has(BotaniaDataComponents.ACTIVE) ? 1 : 0);
		ClampedItemPropertyFunction wandBindModeProperty =
				(stack, world, entity, seed) -> WandOfTheForestItem.getBindMode(stack) ? 1 : 0;
		consumer.accept(BotaniaItems.WAND_OF_THE_FOREST, botaniaRL("bindmode"), wandBindModeProperty);
		consumer.accept(BotaniaItems.WAND_OF_THE_ELVEN_FOREST, botaniaRL("bindmode"), wandBindModeProperty);
		consumer.accept(BotaniaItems.MANUFACTORY_HALO, botaniaRL("active"),
				(stack, world, entity, seed) -> stack.has(BotaniaDataComponents.ACTIVE) ? 1 : 0);

		ResourceLocation poolFullId = botaniaRL("full");
		ClampedItemPropertyFunction poolFull = (stack, world, entity, seed) -> stack.has(BotaniaDataComponents.RENDER_FULL)
				|| stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaPoolBlock pool && pool.isCreative()
						? 1 : 0;
		BuiltInRegistries.BLOCK.stream().filter(ManaPoolBlock.class::isInstance)
				.filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(BotaniaAPI.MODID))
				.forEach(block -> consumer.accept(block.asItem(), poolFullId, poolFull));

		ClampedItemPropertyFunction brewGetter = (stack, world, entity, seed) -> {
			BaseBrewItem item = ((BaseBrewItem) stack.getItem());
			int swigsLeft = item.getSwigsLeft(stack) - 1;
			int totalSwigs = item.getSwigs(stack) - 1;
			return swigsLeft == totalSwigs ? 0.0F : Math.nextUp((totalSwigs - swigsLeft) / (float) totalSwigs);
		};
		consumer.accept(BotaniaItems.BREW_VIAL, botaniaRL("swigs_taken"), brewGetter);
		consumer.accept(BotaniaItems.BREW_FLASK, botaniaRL("swigs_taken"), brewGetter);

		ResourceLocation holidayId = botaniaRL("holiday");
		ClampedItemPropertyFunction holidayGetter = (stack, worldIn, entityIn, seed) -> ClientProxy.jingleTheBells ? 1 : 0;
		consumer.accept(BotaniaItems.MANAWEAVE_HELMET, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.MANAWEAVE_CHESTPLATE, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.MANAWEAVE_BOOTS, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.MANAWEAVE_LEGGINGS, holidayId, holidayGetter);

		ClampedItemPropertyFunction ringOnGetter = (stack, worldIn, entityIn, seed) -> {
			if (entityIn instanceof Player player) {
				if (player.getCooldowns().isOnCooldown(stack.getItem())) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		};
		consumer.accept(BotaniaItems.RING_OF_MAGNETIZATION, botaniaRL("active"), ringOnGetter);
		consumer.accept(BotaniaItems.GREATER_RING_OF_MAGNETIZATION, botaniaRL("active"), ringOnGetter);

		consumer.accept(BotaniaItems.ELEMENTIUM_SHEARS, botaniaRL("reddit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().equalsIgnoreCase("dammit reddit") ? 1F : 0F);
		consumer.accept(BotaniaItems.MANASTEEL_SWORD, botaniaRL("elucidator"),
				(stack, world, entity, seed) -> "the elucidator".equals(stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim()) ? 1 : 0);
		consumer.accept(BotaniaItems.TERRA_TRUNCATOR, botaniaRL("active"),
				(stack, world, entity, seed) -> entity instanceof Player player && !TerraTruncatorItem.shouldBreak(player) ? 0 : 1);
		consumer.accept(BotaniaItems.TERRA_SHATTERER, botaniaRL("tipped"),
				(stack, world, entity, seed) -> TerraShattererItem.isTipped(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.TERRA_SHATTERER, botaniaRL("active"),
				(stack, world, entity, seed) -> TerraShattererItem.isEnabled(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.FRUIT_OF_GRISAIA, botaniaRL("boot"),
				(stack, worldIn, entity, seed) -> FruitOfGrisaiaItem.isBoot(stack) ? 1F : 0F);
		consumer.accept(BotaniaItems.ROD_OF_THE_SKIES, botaniaRL("active"),
				(stack, world, living, seed) -> SkiesRodItem.isFlying(stack) ? 1 : 0);

		// [VanillaCopy] ItemProperties.BOW's minecraft:pulling and minecraft:pull properties
		ClampedItemPropertyFunction pulling = (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
		ClampedItemPropertyFunction pull = (stack, level, entity, seed) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
			}
		};
		consumer.accept(BotaniaItems.LIVINGWOOD_BOW, ResourceLocation.withDefaultNamespace("pulling"), pulling);
		consumer.accept(BotaniaItems.LIVINGWOOD_BOW, ResourceLocation.withDefaultNamespace("pull"), pull);
		consumer.accept(BotaniaItems.CRYSTAL_BOW, ResourceLocation.withDefaultNamespace("pulling"), pulling);
		consumer.accept(BotaniaItems.CRYSTAL_BOW, ResourceLocation.withDefaultNamespace("pull"), pull);
	}

	private BotaniaItemProperties() {}
}
