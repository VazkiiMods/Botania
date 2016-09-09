package vazkii.botania.common.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.function.Predicate;

public final class PlayerHelper {

    // Checks if either of the player's hands has an item.
    public static boolean hasAnyHeldItem(EntityPlayer player) {
        return player.getHeldItemMainhand() != null || player.getHeldItemOffhand() != null;
    }

    // Checks main hand, then off hand for this item.
    public static boolean hasHeldItem(EntityPlayer player, Item item) {
        return (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == item)
                || (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == item);
    }

    // Checks main hand, then off hand for this item class.
    public static boolean hasHeldItemClass(EntityPlayer player, Item template) {
        return hasHeldItemClass(player, template.getClass());
    }

    // Checks main hand, then off hand for this item class.
    public static boolean hasHeldItemClass(EntityPlayer player, Class<?> template) {
        return (player.getHeldItemMainhand() != null && template.isAssignableFrom(player.getHeldItemMainhand().getItem().getClass()))
                || (player.getHeldItemOffhand() != null && template.isAssignableFrom(player.getHeldItemOffhand().getItem().getClass()));
    }

    // Checks main hand, then off hand for this item. Null otherwise.
    public static ItemStack getFirstHeldItem(EntityPlayer player, Item item) {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        if(main != null && item == main.getItem()) {
            return main;
        } else if(offhand != null && item == offhand.getItem()) {
            return offhand;
        } else return null;
    }

    // Checks main hand, then off hand for this item class. Null otherwise.
    public static ItemStack getFirstHeldItemClass(EntityPlayer player, Class<?> template) {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        if(main != null && template.isAssignableFrom(main.getItem().getClass())) {
            return main;
        } else if(offhand != null && template.isAssignableFrom(offhand.getItem().getClass())) {
            return offhand;
        } else return null;
    }

    public static ItemStack getAmmo(EntityPlayer player, Predicate<ItemStack> ammoFunc) {
        // Mainly from ItemBow.findAmmo
        if (ammoFunc.test(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (ammoFunc.test(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (ammoFunc.test(itemstack))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }

    public static boolean hasAmmo(EntityPlayer player, Predicate<ItemStack> ammoFunc) {
        return getAmmo(player, ammoFunc) != null;
    }

    public static void consumeAmmo(EntityPlayer player, Predicate<ItemStack> ammoFunc) {
        ItemStack ammo = getAmmo(player, ammoFunc);
        if(ammo != null) {
            ammo.stackSize--;
            if(ammo.stackSize == 0)
                player.inventory.deleteStack(ammo);
        }
    }

    public static boolean hasItem(EntityPlayer player, Predicate<ItemStack> itemFunc) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (itemFunc.test(player.inventory.getStackInSlot(i)))
                return true;
        }
        return false;
    }

    private PlayerHelper() {}
}
