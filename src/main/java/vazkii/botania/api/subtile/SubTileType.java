package vazkii.botania.api.subtile;

import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * Analog to {@link net.minecraft.tileentity.TileEntityType}, but for subtiles
 */
public class SubTileType extends ForgeRegistryEntry<SubTileType> {
    public static final String SPECIAL_FLOWER_PREFIX = "flower.";
    public static final String GENERATING_TYPE = "botania.flowerType.generating";
    public static final String FUNCTIONAL_TYPE = "botania.flowerType.generating";
    public static final String MISC_TYPE = "botania.flowerType.misc";

    private final String type;
    private final Function<SubTileType, ? extends SubTileEntity> factory;
    private boolean showInCreative = true;

    /**
     * @param typeKey Translation key for this subtile type, usually {@link #GENERATING_TYPE} or {@link #FUNCTIONAL_TYPE}
     * @param factory Function to create new subtiles of this type
     */
    public SubTileType(String typeKey, Function<SubTileType, ? extends SubTileEntity> factory) {
        this.type = typeKey;
        this.factory = Preconditions.checkNotNull(factory);
    }

    public SubTileType setShowInCreative(boolean show) {
        this.showInCreative = show;
        return this;
    }

    public boolean shouldShowInCreative() {
        return showInCreative;
    }

    public String getTranslationKey(ItemStack stack) {
        String id = getRegistryName().toString().replaceAll(":", ".");
        return "block." + SPECIAL_FLOWER_PREFIX + id;
    }

    /**
     * Gets the lore text for the flower item, displayed in the item's tooltip.
     */
    @Nullable
    public ITextComponent getLore(ItemStack stack) {
        String id = getRegistryName().toString().replaceAll(":", ".");
        return new TextComponentTranslation("block." + SPECIAL_FLOWER_PREFIX + id + ".reference");
    }

    public SubTileEntity create() {
        return this.factory.apply(this);
    }

    public String getType() {
        return type;
    }

    /**
     * Adds additional text to the tooltip. This text is added before {@link #getLore(ItemStack)}
     */
    public void addTooltip(ItemStack stack, World world, List<ITextComponent> tooltip) {
        tooltip.add(new TextComponentTranslation(getType()).applyTextStyle(TextFormatting.BLUE));
    }
}
