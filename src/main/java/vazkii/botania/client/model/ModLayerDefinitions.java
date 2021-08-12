/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;

public class ModLayerDefinitions {
	// todo 1.17-fabric call from a mixin or event
	public static void init(BiConsumer<ModelLayerLocation, LayerDefinition> consumer) {
		consumer.accept(ModModelLayers.AVATAR, LayerDefinition.create(ModelAvatar.createMesh(), 32, 32));
		consumer.accept(ModModelLayers.PIXIE, LayerDefinition.create(ModelPixie.createMesh(), 32, 32));
	}
}
