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

import vazkii.botania.client.model.armor.ModelArmorElementium;
import vazkii.botania.client.model.armor.ModelArmorManasteel;
import vazkii.botania.client.model.armor.ModelArmorManaweave;
import vazkii.botania.client.model.armor.ModelArmorTerrasteel;
import vazkii.botania.client.render.tile.RenderTileCorporeaIndex;

import java.util.function.BiConsumer;

public class ModLayerDefinitions {
	public static void init(BiConsumer<ModelLayerLocation, LayerDefinition> consumer) {
		ModModelLayers.init(); // this would happen from the accesses below anyways, but good to make the lifecycle explicit
		consumer.accept(ModModelLayers.AVATAR, LayerDefinition.create(ModelAvatar.createMesh(), 32, 32));
		consumer.accept(ModModelLayers.BELLOWS, LayerDefinition.create(ModelBellows.createMesh(), 64, 32));
		consumer.accept(ModModelLayers.BREWERY, LayerDefinition.create(ModelBrewery.createMesh(), 32, 16));
		consumer.accept(ModModelLayers.CLOAK, LayerDefinition.create(ModelCloak.createMesh(), 64, 64));
		consumer.accept(ModModelLayers.CORPOREA_INDEX, LayerDefinition.create(RenderTileCorporeaIndex.createMesh(), 64, 32));
		consumer.accept(ModModelLayers.HOURGLASS, LayerDefinition.create(ModelHourglass.createMesh(), 64, 32));
		consumer.accept(ModModelLayers.ELEMENTIUM_INNER_ARMOR, LayerDefinition.create(ModelArmorElementium.createInsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.ELEMENTIUM_OUTER_ARMOR, LayerDefinition.create(ModelArmorElementium.createOutsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.MANASTEEL_INNER_ARMOR, LayerDefinition.create(ModelArmorManasteel.createInsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.MANASTEEL_OUTER_ARMOR, LayerDefinition.create(ModelArmorManasteel.createOutsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.MANAWEAVE_INNER_ARMOR, LayerDefinition.create(ModelArmorManaweave.createInsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.MANAWEAVE_OUTER_ARMOR, LayerDefinition.create(ModelArmorManaweave.createOutsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.PIXIE, LayerDefinition.create(ModelPixie.createMesh(), 32, 32));
		consumer.accept(ModModelLayers.PYLON_GAIA, LayerDefinition.create(ModelPylonGaia.createMesh(), 64, 64));
		consumer.accept(ModModelLayers.PYLON_MANA, LayerDefinition.create(ModelPylonMana.createMesh(), 64, 64));
		consumer.accept(ModModelLayers.PYLON_NATURA, LayerDefinition.create(ModelPylonNatura.createMesh(), 64, 64));
		consumer.accept(ModModelLayers.TERRASTEEL_INNER_ARMOR, LayerDefinition.create(ModelArmorTerrasteel.createInsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.TERRASTEEL_OUTER_ARMOR, LayerDefinition.create(ModelArmorTerrasteel.createOutsideMesh(), 64, 128));
		consumer.accept(ModModelLayers.TERU_TERU_BOZU, LayerDefinition.create(ModelTeruTeruBozu.createMesh(), 64, 32));
	}
}
