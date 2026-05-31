package vazkii.botania.api.attachment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania marker attachment without any data. (The mere presence of the data attachment is the data.)
 *
 * @implNote Formally, the data attachment always is a reference to {@link Unit#INSTANCE}.
 */
public class DataMarkerId extends DataIdBase<Unit> {
	public DataMarkerId(ResourceLocation id) {
		super(id, Unit.CODEC);
	}

	public void addFor(Entity entity) {
		BotaniaAPI.instance().setEntityData(this, entity, Unit.INSTANCE);
	}
}
