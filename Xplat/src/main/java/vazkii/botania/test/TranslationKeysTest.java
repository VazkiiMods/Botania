/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.test;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import org.apache.commons.lang3.mutable.MutableInt;

import vazkii.botania.api.BotaniaAPI;

public class TranslationKeysTest {
	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyBlockHasValidDescriptionId(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		BuiltInRegistries.BLOCK.keySet().stream()
				.filter(id -> BotaniaAPI.MODID.equals(id.getNamespace()))
				.forEach(id -> {
					String descriptionId = BuiltInRegistries.BLOCK.get(id).getDescriptionId();
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
}
