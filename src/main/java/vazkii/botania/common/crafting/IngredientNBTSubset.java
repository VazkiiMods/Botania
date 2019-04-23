package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.stream.Stream;

// Like Forge IngredientNBT but does subset checking instead of exact match for NBT tags
public class IngredientNBTSubset extends Ingredient {
    private final ItemStack stack;
    public IngredientNBTSubset(ItemStack stack) {
        super(Stream.of(new Ingredient.SingleItemList(stack)));
        this.stack = stack;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if(input == null)
            return false;

        return stack.getItem() == input.getItem() && ItemNBTHelper.isTagSubset(stack.getTag(), input.getTag());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    public static class Serializer implements IIngredientSerializer<IngredientNBTSubset> {
        @Override
        public IngredientNBTSubset parse(PacketBuffer buffer) {
            return new IngredientNBTSubset(buffer.readItemStack());
        }

        @Override
        public IngredientNBTSubset parse(JsonObject json) {
            return new IngredientNBTSubset(CraftingHelper.getItemStack(json, true));
        }

        @Override
        public void write(PacketBuffer buffer, IngredientNBTSubset ingredient) {
            buffer.writeItemStack(ingredient.stack);
        }
    }
}
