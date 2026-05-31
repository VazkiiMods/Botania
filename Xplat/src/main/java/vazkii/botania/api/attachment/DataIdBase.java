package vazkii.botania.api.attachment;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.internal_caps.BotaniaDataAttachments;

/**
 * Abstract parent class for a Botania data attachment identifier.
 *
 * @param <T> Type of the attached data.
 * @apiNote Not all data attachments are of a custom type, but they all have some sort of associated type that provides
 *          singleton instance of the corresponding data attachment ID.
 * @implNote Botania only attaches custom data to entities, like item entities, mobs, or primed TNT. There are two types
 *           of data attachment IDs, one that actually contains some form of data, and another that only serves as a
 *           marker to signify that some binary state is true.
 * @see BotaniaDataAttachments
 */
@ApiStatus.NonExtendable
public abstract class DataIdBase<T> {
	private final ResourceLocation id;
	private final Codec<T> codec;

	protected DataIdBase(ResourceLocation id, Codec<T> codec) {
		this.id = id;
		this.codec = codec;
	}

	public ResourceLocation getId() {
		return id;
	}

	public Codec<T> getCodec() {
		return codec;
	}

	public boolean existsFor(Entity entity) {
		return getFor(entity) != null;
	}

	@Nullable
	public T getFor(Entity entity) {
		return BotaniaAPI.instance().getEntityData(this, entity);
	}

	public void removeFrom(Entity entity) {
		BotaniaAPI.instance().removeEntityData(this, entity);
	}
}
