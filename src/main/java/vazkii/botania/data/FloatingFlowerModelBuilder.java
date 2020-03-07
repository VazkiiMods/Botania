package vazkii.botania.data;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vazkii.botania.client.model.FloatingFlowerModel;

public class FloatingFlowerModelBuilder extends ModelBuilder<FloatingFlowerModelBuilder> {
	private ModelFile flowerModel;

	public FloatingFlowerModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
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
