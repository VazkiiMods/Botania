/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.state.enums.*;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public class BotaniaStateProps {

    /** Unlisted properties **/

    // The property for specialFlower subtile id
    public static final PropertyString SUBTILE_ID = new PropertyString("subtile_id");

    // The property for platform held blockstate id
    public static final PropertyState HELD_STATE = new PropertyState("held_state");

    /** Common properties to all blocks to use **/

    // The 16 Minecraft colors
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    // The four cardinal directions
    public static final PropertyEnum<EnumFacing> CARDINALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.Plane.HORIZONTAL);

    // All 6 directions
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    // Redstone power - boolean
    // Also for any other simple boolean "on/off" state
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    // Redstone power - int
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    /** Properties for certain kinds of blocks **/

    // The axis directions (think quartz pillars and wood logs)
    public static final PropertyEnum<EnumFacing.Axis> AXIS_FACING = BlockRotatedPillar.AXIS;

    // Extra Quartz Blocks
    public static final PropertyEnum<QuartzVariant> QUARTZ_VARIANT = PropertyEnum.create("variant", QuartzVariant.class);

    /** Properties for individual blocks **/

    // Block18Stone
    public static final PropertyEnum<FutureStoneVariant> FUTURESTONE_VARIANT = PropertyEnum.create("variant", FutureStoneVariant.class);

    // Block18StoneWall
    public static final PropertyEnum<FutureStoneVariant> FUTURESTONEWALL_VARIANT = PropertyEnum.create("variant", FutureStoneVariant.class, new Predicate<FutureStoneVariant>() {
        @Override
        public boolean apply(FutureStoneVariant variant) {
            // Just the four kinds (no polished, chiseled, etc.)
            return variant.ordinal() >= 0 && variant.ordinal() <= 3;
        }
    });

    // BlockAlfPortal
    public static final PropertyEnum<AlfPortalState> ALFPORTAL_STATE = PropertyEnum.create("state", AlfPortalState.class);

    // BlockAltar
    public static final PropertyEnum<AltarVariant> ALTAR_VARIANT = PropertyEnum.create("variant", AltarVariant.class);

    // BlockAltGrass
    public static final PropertyEnum<AltGrassVariant> ALTGRASS_VARIANT = PropertyEnum.create("variant", AltGrassVariant.class);

    // BlockBiomeStoneA
    public static final PropertyEnum<BiomeStoneVariant> BIOMESTONE_VARIANT = PropertyEnum.create("variant", BiomeStoneVariant.class);

    // BlockBiomeStoneB
    public static final PropertyEnum<BiomeBrickVariant> BIOMEBRICK_VARIANT = PropertyEnum.create("variant", BiomeBrickVariant.class);

    // BlockBiomeStoneWall
    public static final PropertyEnum<BiomeStoneVariant> BIOMESTONEWALL_VARIANT = PropertyEnum.create("variant", BiomeStoneVariant.class, new Predicate<BiomeStoneVariant>() {
        @Override
        public boolean apply(BiomeStoneVariant input) {
            return input.getName().contains("cobble");
        }
    });

    // BlockCustomBrick
    public static final PropertyEnum<CustomBrickVariant> CUSTOMBRICK_VARIANT = PropertyEnum.create("variant", CustomBrickVariant.class);

    // BlockDreamWood, BlockLivingWood
    public static final PropertyEnum<LivingWoodVariant> LIVINGWOOD_VARIANT = PropertyEnum.create("variant", LivingWoodVariant.class);

    // BlockEnchanter
    public static final PropertyEnum<EnumFacing.Axis> ENCHANTER_DIRECTION = PropertyEnum.create("facing", EnumFacing.Axis.class, Predicates.not(Predicates.equalTo(EnumFacing.Axis.Y)));

    // BlockEndStoneBrick
    public static final PropertyEnum<EndBrickVariant> ENDBRICK_VARIANT = PropertyEnum.create("variant", EndBrickVariant.class);

    // BlockForestDrum
    public static final PropertyEnum<DrumVariant> DRUM_VARIANT = PropertyEnum.create("variant", DrumVariant.class);

    // BlockGhostRail
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> RAIL_DIRECTION = BlockRailPowered.SHAPE;

    // BlockLightRelay
    public static final PropertyEnum<LuminizerVariant> LUMINIZER_VARIANT = PropertyEnum.create("variant", LuminizerVariant.class);

    // BlockLivingRock
    public static final PropertyEnum<LivingRockVariant> LIVINGROCK_VARIANT = PropertyEnum.create("variant", LivingRockVariant.class);

    // BlockModDoubleFlower
    public static final PropertyEnum<EnumDyeColor> DOUBLEFLOWER_VARIANT_1 = PropertyEnum.create("variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
        @Override
        public boolean apply(EnumDyeColor input) {
            // White to gray
            return input.ordinal() >= 0 && input.ordinal() <= 7;
        }
    });

    public static final PropertyEnum<EnumDyeColor> DOUBLEFLOWER_VARIANT_2 = PropertyEnum.create("variant", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
        @Override
        public boolean apply(EnumDyeColor input) {
            // Silver to black
            return input.ordinal() >= 8 && input.ordinal() <= 15;
        }
    });

    // BlockOpenCrate
    public static final PropertyEnum<CrateVariant> CRATE_VARIANT = PropertyEnum.create("variant", CrateVariant.class);
    public static final PropertyEnum<CratePattern> CRATE_PATTERN = PropertyEnum.create("pattern", CratePattern.class);

    // BlockPavement
    public static final PropertyEnum<EnumDyeColor> PAVEMENT_COLOR = PropertyEnum.create("color", EnumDyeColor.class, new Predicate<EnumDyeColor>() {
        @Override
        public boolean apply(EnumDyeColor color) {
            return color == EnumDyeColor.WHITE || color == EnumDyeColor.BLACK
                    || color == EnumDyeColor.BLUE || color == EnumDyeColor.RED
                    || color == EnumDyeColor.YELLOW || color == EnumDyeColor.GREEN;
        }
    });

    // BlockPlatform
    public static final PropertyEnum<PlatformVariant> PLATFORM_VARIANT = PropertyEnum.create("variant", PlatformVariant.class);

    // BlockPool
    public static final PropertyEnum<PoolVariant> POOL_VARIANT = PropertyEnum.create("variant", PoolVariant.class);

    // BlockPrism
    public static final PropertyBool HAS_LENS = PropertyBool.create("has_lens");

    // BlockPrismarine
    public static final PropertyEnum<PrismarineVariant> PRISMARINE_VARIANT = PropertyEnum.create("variant", PrismarineVariant.class);

    // BlockPylon
    public static final PropertyEnum<PylonVariant> PYLON_VARIANT = PropertyEnum.create("variant", PylonVariant.class);

    // BlockSpreader
    public static final PropertyEnum<SpreaderVariant> SPREADER_VARIANT = PropertyEnum.create("variant", SpreaderVariant.class);

    // BlockStorage
    public static final PropertyEnum<StorageVariant> STORAGE_VARIANT = PropertyEnum.create("variant", StorageVariant.class);

    // BlockTinyPotato
    public static final PropertyEnum<PotatoVariant> POTATO_VARIANT = PropertyEnum.create("variant", PotatoVariant.class);

    private BotaniaStateProps() {}

}
