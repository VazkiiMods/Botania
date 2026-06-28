/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.test;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

import java.util.ArrayList;
import java.util.List;

public class TranslationKeysTest {
	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyBlockHasValidDescriptionId(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		BuiltInRegistries.BLOCK.keySet().stream()
				.filter(id -> BotaniaAPI.MODID.equals(id.getNamespace()))
				.forEach(id -> {
					Block block = BuiltInRegistries.BLOCK.get(id);
					validateComponent(id, "name of block", block.getName(), missing);
					String descriptionId = block.getDescriptionId();
					if (!Language.getInstance().has(descriptionId)) {
						BotaniaAPI.LOGGER.error("Missing block translation key {} for block {}", descriptionId, id);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing block description IDs (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyItemHasValidDescriptionId(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		BuiltInRegistries.ITEM.keySet().stream()
				.filter(id -> BotaniaAPI.MODID.equals(id.getNamespace()))
				.forEach(id -> {
					Item item = BuiltInRegistries.ITEM.get(id);

					validateComponent(id, "description for item", item.getDescription(), missing);
					validateComponent(id, "name for item", item.getName(item.getDefaultInstance()), missing);
					try {
						List<Component> tooltipLines = new ArrayList<>();
						item.appendHoverText(
								item.getDefaultInstance(),
								// TODO: pretend we are pressing all the modifiers (this is platform-specific)
								new Item.TooltipContext() {
									@Override
									public HolderLookup.Provider registries() {
										return helper.getLevel().registryAccess();
									}

									@Override
									public float tickRate() {
										return 0;
									}

									@Override
									public @Nullable MapItemSavedData mapData(MapId mapId) {
										return null;
									}
								},
								tooltipLines,
								TooltipFlag.ADVANCED
						);
						for (Component tooltipLine : tooltipLines) {
							validateComponent(id, "tooltip for item", tooltipLine, missing);
						}
					} catch (Exception e) {
						BotaniaAPI.LOGGER.error("Failed to analyze tooltip of item {}. (Does it assume client context?)", id, e);
						// close enough...
						missing.increment();
					}

					String descriptionId = item.getDescriptionId();
					if (item instanceof BlockItem blockItem
							&& blockItem.getBlock().getDescriptionId().equals(descriptionId)) {
						// ignore missing block item descriptions if they are also reported by the block test
						return;
					}
					if (!Language.getInstance().has(descriptionId)) {
						BotaniaAPI.LOGGER.error("Missing item translation key {} for item {}", descriptionId, id);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing item description IDs (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	private void validateComponent(ResourceLocation id, String type, Component component, MutableInt missing) {
		ComponentContents contents = component.getContents();
		if (contents instanceof TranslatableContents translatableContents
				&& !Language.getInstance().has(translatableContents.getKey())) {
			BotaniaAPI.LOGGER.error("Missing translation key {} in {} {}",
					translatableContents.getKey(), type, id);
			missing.increment();
		}
		for (Component sibling : component.getSiblings()) {
			validateComponent(id, type, sibling, missing);
		}
	}
}
