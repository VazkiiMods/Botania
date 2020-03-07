/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.imc;

import net.minecraft.util.ResourceLocation;

public class OreWeightMessage {
	private final ResourceLocation blockTag;
	private final Integer weight;

	/**
	 * @param blockTag ID of a block tag to register
	 * @param weight   Weight of the tag
	 */
	public OreWeightMessage(ResourceLocation blockTag, Integer weight) {
		this.blockTag = blockTag;
		this.weight = weight;
	}

	public ResourceLocation getOre() {
		return blockTag;
	}

	public Integer getWeight() {
		return weight;
	}
}
