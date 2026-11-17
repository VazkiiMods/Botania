/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

/**
 * Defines mana costs for preventing damage to an item or repairing its durability after it received damage.
 * Damage prevention applies after enchantments. (i.e. Unbreaking effectively reduces mana costs)
 * Repair functionality restores one point of durability per tick, if the player has enough mana to cover the costs.
 *
 * @param manaPerPreventedDamage    Mana cost per prevented point of damage. Zero disables damage prevention.
 * @param manaPerRepairedDurability Mana cost per repaired point of durability. Zero disables repair.
 */
public record ManaRepair(int manaPerPreventedDamage, int manaPerRepairedDurability) {
	public static final Codec<ManaRepair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("manaPerPreventedDamage").forGetter(ManaRepair::manaPerPreventedDamage),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("manaPerRepairedDurability").forGetter(ManaRepair::manaPerRepairedDurability)
	).apply(instance, ManaRepair::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ManaRepair> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ManaRepair::manaPerPreventedDamage,
			ByteBufCodecs.VAR_INT, ManaRepair::manaPerRepairedDurability,
			ManaRepair::new
	);
}
