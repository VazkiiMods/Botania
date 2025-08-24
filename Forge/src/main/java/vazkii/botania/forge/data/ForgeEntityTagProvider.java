package vazkii.botania.forge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.entity.BotaniaEntities;

import java.util.concurrent.CompletableFuture;

public class ForgeEntityTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
	public ForgeEntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
			ExistingFileHelper existingFileHelper) {
		super(packOutput, Registries.ENTITY_TYPE, lookupProvider,
				(entityType) -> entityType.builtInRegistryHolder().key(), BotaniaAPI.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		tag(forge("bosses")).add(BotaniaEntities.DOPPLEGANGER);
	}

	private static TagKey<EntityType<?>> forge(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", name));
	}
}
