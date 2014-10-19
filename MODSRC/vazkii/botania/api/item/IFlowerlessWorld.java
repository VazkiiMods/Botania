package vazkii.botania.api.item;

/**
 * A World Provider that implements this will not have Botania flowers generated.
 */
public interface IFlowerlessWorld {

    /**
     *
     * @return False to not have flowers generate, True to have them generate
     */
    public boolean generateFlowers();
}
