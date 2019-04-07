package vazkii.botania.api.imc;

import net.minecraft.util.ResourceLocation;

public class OreWeightMessage {
    private final ResourceLocation blockTag;
    private final Integer weight;

    /**
     * @param blockTag ID of a block tag to register
     * @param weight Weight of the tag
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
