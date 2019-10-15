/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 06, 2019]
 */
package vazkii.botania.api.imc;

/**
 * This class defines the IMC methods accepted by Botania and their expected argument types
 * @see net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
 * @see net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
 * @see net.minecraftforge.fml.InterModComms
 */
public class IMC {
    /**
     * Maps a block tag to its weight in world generation. This is used for the Orechid flower.<br />
     * The argument should be a {@link OreWeightMessage}, any other types are ignored.<br />
     * Check the Botania source to see default values.<br>
     */
    public static final String REGISTER_ORE_WEIGHT = "register_ore_weight";

    /**
     * Maps a block tag to its weight in nether world generation. This is used for the Orechid flower.<br />
     * The argument should be a {@link OreWeightMessage}, any other types are ignored.<br />
     * Check the Botania source to see default values.<br>
     */
    public static final String REGISTER_NETHER_ORE_WEIGHT = "register_nether_ore_weight";

    /**
     * Registers a block as paintable under the paint lens.<br />
     * The argument should be a {@link PaintableBlockMessage}, any other types are ignored.
     */
    public static final String REGISTER_PAINTABLE_BLOCK = "register_paintable_block";

    /**
     * Registers a mana infusion recipe at the mana pool.<br />
     * The argument should be a {@link vazkii.botania.api.recipe.RecipeManaInfusion}, any other types are ignored.
     */
    public static final String REGISTER_MANA_INFUSION = "register_mana_infusion";
}
