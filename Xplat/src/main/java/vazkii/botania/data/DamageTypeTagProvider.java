package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagProvider extends TagsProvider<DamageType> {

	public DamageTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.DAMAGE_TYPE, lookupProvider);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.tag(BotaniaTags.DamageTypes.RING_OF_ODIN_IMMUNE)
				.add(DamageTypes.DROWN)
				.add(DamageTypes.FALL)
				.add(DamageTypes.IN_WALL)
				.add(DamageTypes.STARVE)
				.add(DamageTypes.FLY_INTO_WALL)
				.addOptionalTag(DamageTypeTags.IS_FIRE.location());

		/*
		Optional tag workaround for error:
		java.lang.IllegalArgumentException: Couldn't define tag botania:ring_of_odin_immune as it is missing following references: #minecraft:is_fire
		*/

		this.tag(DamageTypeTags.BYPASSES_ARMOR)
				.add(BotaniaDamageTypes.PLAYER_ATTACK_ARMOR_PIERCING)
				.add(BotaniaDamageTypes.RELIC_DAMAGE);

		this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(BotaniaDamageTypes.RELIC_DAMAGE);
		this.tag(DamageTypeTags.NO_IMPACT).add(BotaniaDamageTypes.RELIC_DAMAGE);
		this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(BotaniaDamageTypes.RELIC_DAMAGE);
		this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(BotaniaDamageTypes.RELIC_DAMAGE);

		this.tag(DamageTypeTags.IS_EXPLOSION).add(BotaniaDamageTypes.KEY_EXPLOSION);
	}
}
