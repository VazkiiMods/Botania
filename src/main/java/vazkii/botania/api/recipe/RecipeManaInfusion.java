/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 5:57:07 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

public class RecipeManaInfusion {
	@ObjectHolder("botania:alchemy_catalyst")
	public static Block alchemy;

	@ObjectHolder("botania:conjuration_catalyst")
	public static Block conjuration;

	private final ResourceLocation id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable
	private BlockState catalystState;

	public static RecipeManaInfusion conjuration(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
		RecipeManaInfusion ret = new RecipeManaInfusion(id, output, input, mana);
		ret.setCatalyst(conjuration.getDefaultState());
		return ret;
	}

	public static RecipeManaInfusion alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
		RecipeManaInfusion ret = new RecipeManaInfusion(id, output, input, mana);
		ret.setCatalyst(alchemy.getDefaultState());
		return ret;
	}

	public RecipeManaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		Preconditions.checkArgument(mana < 100000);
	}

	public final ResourceLocation getId() {
		return id;
	}

	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	@Nullable
	public BlockState getCatalyst() {
		return catalystState;
	}

	public void setCatalyst(BlockState catalyst) {
		catalystState = catalyst;
	}

	public Ingredient getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getManaToConsume() {
		return mana;
	}

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(id);
		input.write(buf);
		buf.writeItemStack(output, false);
		buf.writeVarInt(mana);
		buf.writeInt(catalystState == null ? -1 : Block.getStateId(catalystState));
	}

	public static RecipeManaInfusion read(PacketBuffer buf) {
		ResourceLocation id = buf.readResourceLocation();
		Ingredient input = Ingredient.read(buf);
		ItemStack output = buf.readItemStack();
		int mana = buf.readVarInt();
		int catalystId = buf.readInt();
		RecipeManaInfusion ret = new RecipeManaInfusion(id, output, input, mana);
		ret.setCatalyst(catalystId == -1 ? null : Block.getStateById(catalystId));
		return ret;
	}
}

