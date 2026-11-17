/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.configdata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record MobAttributeModifier(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {

	public static final Codec<MobAttributeModifier> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute")
							.forGetter(mam -> mam.attribute),
					Codec.DOUBLE.fieldOf("amount").forGetter(mam -> mam.amount),
					AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(mam -> mam.operation)
			).apply(instance, MobAttributeModifier::new)
	);

	public AttributeModifier createAttributeModifier(ResourceLocation id) {
		return new AttributeModifier(id.withSuffix("_" + operation.getSerializedName()), amount, operation);
	}

	@Override
	public String toString() {
		return "MobAttributeModifier{" +
				", attribute=" + attribute.unwrapKey() +
				", amount=" + amount +
				", operation=" + operation +
				'}';
	}

	public static DataResult<List<MobAttributeModifier>> validateList(List<MobAttributeModifier> modifiersList) {
		Set<Pair<Holder<Attribute>, AttributeModifier.Operation>> uniqueAttributes = new LinkedHashSet<>();
		for (var modifier : modifiersList) {
			if (!uniqueAttributes.add(Pair.of(modifier.attribute, modifier.operation))) {
				return DataResult.error(() -> "Found duplicate %s operation for attribute %s"
						.formatted(modifier.operation, modifier.attribute.unwrapKey().orElseThrow().location()), modifiersList);
			}
		}

		return DataResult.success(modifiersList);
	}
}
