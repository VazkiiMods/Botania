/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.lib;

public class LibItemAges {

    // Items dropped by an Open Crate start at this age, if the Crate has a Redstone signal applied
    public static final int OPEN_CRATE_REDSTONE_AGE = -200;

    // Items created by a Mana Pool start at this age
    public static final int INFUSION_PRODUCT_AGE = 105;

    // Items created by a Mana Pool may not be used by a flower until they are this age or higher.
    private static final int INFUSION_PRODUCT_USABLE = 110;

    // Items created by a Mana Pool may not be used by any Mana Pool until they are this age or higher.
    private static final int INFUSION_PRODUCT_RECRAFTABLE = 130;

    // Items of this age or higher may be used by a generating flower
    private static final int LEGAL_FOR_GENERATOR = 59;

    // Items of this age or higher may be used by a functional flower
    private static final int LEGAL_FOR_FUNCTIONAL = 60;


    public static boolean isLegalForGenerator(int age, int slowdown) {
        return ((age >= LEGAL_FOR_GENERATOR + slowdown) &&
                (age < INFUSION_PRODUCT_AGE || age >= INFUSION_PRODUCT_USABLE));
    }

    public static boolean isLegalForFunctional(int age, int slowdown) {
        return ((age >= LEGAL_FOR_FUNCTIONAL + slowdown) &&
                (age < INFUSION_PRODUCT_AGE || age >= INFUSION_PRODUCT_USABLE));
    }

    public static boolean isLegalForInfusion(int age) {
        return age < INFUSION_PRODUCT_AGE || age >= INFUSION_PRODUCT_RECRAFTABLE;
    }
}
