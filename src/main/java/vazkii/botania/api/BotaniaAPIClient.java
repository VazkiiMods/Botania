/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.apache.logging.log4j.LogManager;

import vazkii.botania.api.item.IFloatingFlower;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class for API calls that must be made clientside
 */
@Environment(EnvType.CLIENT)
public interface BotaniaAPIClient {
	Supplier<BotaniaAPIClient> INSTANCE = Suppliers.memoize(() -> {
		try {
			return (BotaniaAPIClient) Class.forName("vazkii.botania.client.impl.BotaniaAPIClientImpl")
					.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find BotaniaAPIClientImpl, using a dummy");
			return new BotaniaAPIClient() {};
		}
	});

	static BotaniaAPIClient instance() {
		return INSTANCE.get();
	}

	/**
	 * Registers your model for island type islandType here.
	 * Call this during {@link net.minecraftforge.client.event.ModelRegistryEvent}.
	 *
	 * @param islandType The islandtype to register
	 * @param model      The model, only {@link ResourceLocation} allowed, no {@link ModelResourceLocation} allowed.
	 */
	default void registerIslandTypeModel(IFloatingFlower.IslandType islandType, ResourceLocation model) {}

	/**
	 * @return An immutable and live view of the registered island type model map
	 */
	default Map<IFloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.emptyMap();
	}

	/**
	 * Draw a mana bar on the screen
	 */
	default void drawSimpleManaHUD(PoseStack ms, int color, int mana, int maxMana, String name) {}

	/**
	 * Performs the effects of {@link #drawSimpleManaHUD}, then renders {@code bindDisplay}, and a checkmark or x-mark
	 * dependong on the value of {@code properlyBound}.
	 */
	default void drawComplexManaHUD(PoseStack ms, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {}
}
