/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import java.util.List;
import java.util.function.Predicate;

public interface StateIngredient extends Predicate<BlockState> {

	JsonObject serialize();

	void write(PacketByteBuf buffer);

	List<BlockState> getDisplayed();

}
