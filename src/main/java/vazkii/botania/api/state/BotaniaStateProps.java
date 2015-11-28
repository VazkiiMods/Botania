package vazkii.botania.api.state;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.state.enums.*;

import javax.annotation.Nullable;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public class BotaniaStateProps {

    /** Common properties to all blocks to use **/

    // Cardinal connection booleans (think fence or wall)
    public static final PropertyBool CONNECT_NORTH = PropertyBool.create("north");
    public static final PropertyBool CONNECT_SOUTH = PropertyBool.create("south");
    public static final PropertyBool CONNECT_WEST = PropertyBool.create("west");
    public static final PropertyBool CONNECT_EAST = PropertyBool.create("east");

    // The 16 Minecraft colors
    public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    // The four cardinal directions
    public static final PropertyEnum CARDINALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.Plane.HORIZONTAL);

    // All 6 directions
    public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumFacing.class);

    // Redstone power - boolean
    // Also for any other simple boolean "on/off" state
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    // Redstone power - int
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    /** Properties for certain kinds of blocks **/

    // The axis directions (think quartz pillars and wood logs)
    public static final PropertyEnum AXIS_FACING = BlockRotatedPillar.AXIS;

    // Extra Quartz Blocks
    public static final PropertyEnum QUARTZ_VARIANT = PropertyEnum.create("variant", QuartzVariant.class);

    /** Properties for individual blocks **/

    // Block18Stone,
    public static final PropertyEnum FUTURESTONE_VARIANT = PropertyEnum.create("variant", FutureStoneVariant.class);

    // Block18StoneWall
    public static final PropertyEnum FUTURESTONEWALL_VARIANT = PropertyEnum.create("variant", FutureStoneVariant.class, new Predicate<FutureStoneVariant>() {
        @Override
        public boolean apply(FutureStoneVariant variant) {
            // Just the four kinds (no polished, chiseled, etc.)
            return variant.ordinal() >= 0 && variant.ordinal() <= 3;
        }
    });

    // BlockAltGrass
    public static final PropertyEnum ALTGRASS_VARIANT = PropertyEnum.create("variant", AltGrassVariant.class);

    // BlockBiomeStoneA
    public static final PropertyEnum BIOMESTONE_VARIANT = PropertyEnum.create("variant", BiomeStoneVariant.class);

    // BlockBiomeStoneB
    public static final PropertyEnum BIOMEBRICK_VARIANT = PropertyEnum.create("variant", BiomeBrickVariant.class);

    // BlockCustomBrick
    public static final PropertyEnum CUSTOMBRICK_VARIANT = PropertyEnum.create("variant", CustomBrickVariant.class);

    // BlockDreamWood, BlockLivingWood
    public static final PropertyEnum LIVINGWOOD_VARIANT = PropertyEnum.create("variant", LivingWoodVariant.class);

    // BlockEnchanter
    public static final PropertyEnum ENCHANTER_DIRECTION = PropertyEnum.create("facing", EnumFacing.Axis.class, Predicates.not(Predicates.equalTo(EnumFacing.Axis.Y)));

    // BlockEndStoneBrick
    public static final PropertyEnum ENDBRICK_VARIANT = PropertyEnum.create("variant", EndBrickVariant.class);

    // BlockForestDrum
    public static final PropertyEnum DRUM_VARIANT = PropertyEnum.create("variant", DrumVariant.class);

    // BlockLivingRock
    public static final PropertyEnum LIVINGROCK_VARIANT = PropertyEnum.create("variant", LivingRockVariant.class);

    // BlockOpenCrate
    public static final PropertyEnum CRATE_VARIANT = PropertyEnum.create("variant", CrateVariant.class);

    // BlockPavement
    public static final PropertyEnum PAVEMENT_COLOR = PropertyEnum.create("color", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
        @Override
        public boolean apply(EnumDyeColor color) {
            return color == EnumDyeColor.WHITE || color == EnumDyeColor.BLACK
                    || color == EnumDyeColor.BLUE || color == EnumDyeColor.RED
                    || color == EnumDyeColor.YELLOW || color == EnumDyeColor.GREEN;
        }
    });

    // BlockPlatform
    public static final PropertyEnum PLATFORM_VARIANT = PropertyEnum.create("variant", PlatformVariant.class);

    // BlockPrism
    public static final PropertyBool HAS_LENS = PropertyBool.create("has_lens");

    // BlockPrismarine
    public static final PropertyEnum PRISMARINE_VARIANT = PropertyEnum.create("variant", PrismarineVariant.class);

    // BlockPylon
    public static final PropertyEnum PYLON_VARIANT = PropertyEnum.create("variant", PylonVariant.class);

    // BlockSpreader
    public static final PropertyEnum SPREADER_VARIANT = PropertyEnum.create("variant", SpreaderVariant.class);

    // BlockStorage
    public static final PropertyEnum STORAGE_VARIANT = PropertyEnum.create("variant", StorageVariant.class);

    private BotaniaStateProps() {}
}
