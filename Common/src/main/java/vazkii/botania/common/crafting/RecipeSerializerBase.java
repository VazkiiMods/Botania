package vazkii.botania.common.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

/**
 * This class exists purely to (soft) implement the IForgeRegistryEntry methods
 */
public abstract class RecipeSerializerBase<T extends Recipe<?>> implements RecipeSerializer<T> {
	@Nullable
	private ResourceLocation registryName;

	// [SoftImplement] IForgeRegistryEntry
	public RecipeSerializerBase<T> setRegistryName(ResourceLocation name) {
		registryName = name;
		return this;
	}

	// [SoftImplement] IForgeRegistryEntry
	@Nullable
	public ResourceLocation getRegistryName() {
		return registryName;
	}

	// [SoftImplement] IForgeRegistryEntry
	@SuppressWarnings("unchecked")
	public Class<RecipeSerializer<?>> getRegistryType() {
		Class<?> clazz = RecipeSerializer.class;
		return (Class<RecipeSerializer<?>>) clazz;
	}

}
