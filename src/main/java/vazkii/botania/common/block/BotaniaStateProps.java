package vazkii.botania.common.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;

/**
 * Holds all Botania block state properties
 */
public class BotaniaStateProps {

    /** Common properties to all blocks **/

    // The 16 Minecraft colors
    public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    // Only the four cardinal directions
    public static final PropertyEnum CARDINALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.Plane.HORIZONTAL);

    // All 6 directions
    public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumFacing.class);

    // Redstone power - boolean
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    // Redstone power - int
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    /** Properties for individual blocks **/

    // BlockAltGrass
    public static final PropertyEnum ALTGRASS_VARIANT = PropertyEnum.create("variant", BlockAltGrass.AltGrassType.class);

    private BotaniaStateProps() {}
}
