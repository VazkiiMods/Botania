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

import vazkii.botania.client.model.armor.ElementiumArmorModel;
import vazkii.botania.client.model.armor.ManasteelArmorModel;
import vazkii.botania.client.model.armor.ManaweaveRobeModel;
import vazkii.botania.client.model.armor.TerrasteelArmorModel;
import vazkii.botania.client.render.block_entity.CorporeaIndexBlockEntityRenderer;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BotaniaLayerDefinitions {
	public static void init(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
		consumer.accept(BotaniaModelLayers.AVATAR, () -> LayerDefinition.create(AvatarModel.createMesh(), 32, 32));
		consumer.accept(BotaniaModelLayers.BELLOWS, () -> LayerDefinition.create(BellowsModel.createMesh(), 64, 32));
		consumer.accept(BotaniaModelLayers.BREWERY, () -> LayerDefinition.create(BotanicalBreweryModel.createMesh(), 32, 16));
		consumer.accept(BotaniaModelLayers.CLOAK, () -> LayerDefinition.create(CloakModel.createMesh(), 64, 64));
		consumer.accept(BotaniaModelLayers.CORPOREA_INDEX, () -> LayerDefinition.create(CorporeaIndexBlockEntityRenderer.createMesh(), 64, 32));
		consumer.accept(BotaniaModelLayers.HOURGLASS, () -> LayerDefinition.create(HourglassModel.createMesh(), 64, 32));
		consumer.accept(BotaniaModelLayers.ELEMENTIUM_INNER_ARMOR, () -> LayerDefinition.create(ElementiumArmorModel.createInsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.ELEMENTIUM_OUTER_ARMOR, () -> LayerDefinition.create(ElementiumArmorModel.createOutsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.MANASTEEL_INNER_ARMOR, () -> LayerDefinition.create(ManasteelArmorModel.createInsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.MANASTEEL_OUTER_ARMOR, () -> LayerDefinition.create(ManasteelArmorModel.createOutsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.MANAWEAVE_INNER_ARMOR, () -> LayerDefinition.create(ManaweaveRobeModel.createInsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.MANAWEAVE_OUTER_ARMOR, () -> LayerDefinition.create(ManaweaveRobeModel.createOutsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.PIXIE, () -> LayerDefinition.create(PixieModel.createMesh(), 32, 32));
		consumer.accept(BotaniaModelLayers.PYLON_GAIA, () -> LayerDefinition.create(GaiaPylonModel.createMesh(), 64, 64));
		consumer.accept(BotaniaModelLayers.PYLON_MANA, () -> LayerDefinition.create(ManaPylonModel.createMesh(), 64, 64));
		consumer.accept(BotaniaModelLayers.PYLON_NATURA, () -> LayerDefinition.create(NaturaPylonModel.createMesh(), 64, 64));
		consumer.accept(BotaniaModelLayers.TERRASTEEL_INNER_ARMOR, () -> LayerDefinition.create(TerrasteelArmorModel.createInsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.TERRASTEEL_OUTER_ARMOR, () -> LayerDefinition.create(TerrasteelArmorModel.createOutsideMesh(), 64, 128));
		consumer.accept(BotaniaModelLayers.TERU_TERU_BOZU, () -> LayerDefinition.create(TeruTeruBozuModel.createMesh(), 64, 32));
	}
}
