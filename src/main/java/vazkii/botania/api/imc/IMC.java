/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.imc;

/**
 * This class defines the IMC methods accepted by Botania and their expected argument types.
 * This is just for documentation purposes, IMC is meant to be sent when you don't want to depend on the Botania API
 * classes.
 * 
 * @see net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
 * @see net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
 * @see net.minecraftforge.fml.InterModComms
 */
public class IMC {
	/**
	 * Maps a block tag to its weight in world generation. This is used for the Orechid flower.<br />
	 * The argument should be a {@link com.mojang.datafixers.util.Pair<net.minecraft.util.ResourceLocation, Integer>},
	 * any other types are ignored.<br />
	 * Check the Botania source to see default values.<br>
	 */
	public static final String REGISTER_ORE_WEIGHT = "register_ore_weight";

	/**
	 * Maps a block tag to its weight in nether world generation. This is used for the Orechid Ignem flower.<br />
	 * The argument should be a {@link com.mojang.datafixers.util.Pair<net.minecraft.util.ResourceLocation, Integer>},
	 * any other types are ignored.<br />
	 * Check the Botania source to see default values.<br>
	 */
	public static final String REGISTER_NETHER_ORE_WEIGHT = "register_nether_ore_weight";
}
