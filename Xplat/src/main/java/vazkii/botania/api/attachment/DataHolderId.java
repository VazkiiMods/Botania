package vazkii.botania.api.attachment;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

import java.util.function.Supplier;

/**
 * Abstract ID for a Botania data attachment. Attached data should be treated as immutable.
 *
 * @param <T> Type of the attached data.
 */
public final class DataHolderId<T> extends DataIdBase<T> {
	public DataHolderId(ResourceLocation id, Codec<T> codec) {
		super(id, codec);
	}

	public void setFor(Entity entity, T data) {
		BotaniaAPI.instance().setEntityData(this, entity, data);
	}

	public void setUnlessNull(Entity entity, @Nullable T data) {
		if (data != null) {
			setFor(entity, data);
		} else {
			removeFrom(entity);
		}
	}

	public T getOrDefault(Entity entity, T defaultValue) {
		T value = BotaniaAPI.instance().getEntityData(this, entity);
		return value != null ? value : defaultValue;
	}

	public T getOrDefault(Entity entity, Supplier<T> defaultSupplier) {
		T value = BotaniaAPI.instance().getEntityData(this, entity);
		return value != null ? value : defaultSupplier.get();
	}

	public void setUnlessDefault(Entity entity, T value, T defaultValue) {
		if (defaultValue.equals(value)) {
			removeFrom(entity);
		} else {
			setFor(entity, value);
		}
	}
}
