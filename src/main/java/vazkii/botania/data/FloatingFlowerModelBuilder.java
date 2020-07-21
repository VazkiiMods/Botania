/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import vazkii.botania.client.model.FloatingFlowerModel;

/*
public class FloatingFlowerModelBuilder extends ModelBuilder<FloatingFlowerModelBuilder> {
	private ModelFile flowerModel;

	public FloatingFlowerModelBuilder(Identifier outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}

	public FloatingFlowerModelBuilder withFlowerModel(ModelFile model) {
		this.flowerModel = model;
		return this;
	}

	@Override
	public JsonObject toJson() {
		JsonObject ret = super.toJson();
		ret.addProperty("loader", FloatingFlowerModel.Loader.ID.toString());
		JsonObject submodel = new JsonObject();
		submodel.addProperty("parent", flowerModel.getLocation().toString());
		ret.add("flower", submodel);
		return ret;
	}
}
*/
