/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 17, 2015, 5:07:25 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

public class RecipePureDaisy {

	public static final int DEFAULT_TIME = 150;

	private final ResourceLocation id;
	private final Object input;
	private final BlockState outputState;
	private final int time;

	public RecipePureDaisy(ResourceLocation id, Object input, BlockState state) {
		this(id, input, state, DEFAULT_TIME);
	}

	/**
	 * @param id    The ID for this recipe.
	 * @param input The input for the recipe. Can be a Block, BlockState, or Tag&lt;Block&gt;.
	 * @param state The blockstate to be placed upon recipe completion.
	 * @param time  The amount of time in ticks to complete this recipe. Note that this is ticks on your block, not total time.
	 *              The Pure Daisy only ticks one block at a time in a round robin fashion.
	 */
	public RecipePureDaisy(ResourceLocation id, Object input, BlockState state, int time) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.id = id;
		this.input = input;
		this.outputState = state;
		this.time = time;
		if(input != null
				&& !(input instanceof Block)
				&& !(input instanceof BlockState)
				&& !(input instanceof Tag && checkBlockTag((Tag) input)))
			throw new IllegalArgumentException("input must be a Tag<Block>, Block, or IBlockState");
	}

	private static boolean checkBlockTag(Tag<?> tag) {
		return tag.getAllElements().stream().allMatch(o -> o instanceof Block);
	}

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	public boolean matches(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy, BlockState state) {
		if(input instanceof Block)
			return state.getBlock() == input;

		if(input instanceof BlockState)
			return state == input;

		return ((Tag<Block>) input).contains(state.getBlock());
	}

	/**
	 * Returns true if the block was placed (and if the Pure Daisy should do particles and stuffs).
	 * Should only place the block if !world.isRemote, but should return true if it would've placed
	 * it otherwise. You may return false to cancel the normal particles and do your own.
	 */
	public boolean set(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy) {
		if(!world.isRemote)
			world.setBlockState(pos, outputState);
		return true;
	}

	public Object getInput() {
		return input;
	}

	public BlockState getOutputState() {
		return outputState;
	}

	public int getTime() {
		return time;
	}

	public ResourceLocation getId() {
		return id;
	}

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(id);
		if (input instanceof Tag) {
			buf.writeVarInt(0);
			buf.writeResourceLocation(((Tag) input).getId());
		} else if (input instanceof Block) {
			buf.writeVarInt(1);
			buf.writeVarInt(Registry.BLOCK.getId((Block) input));
		} else {
			buf.writeVarInt(2);
			buf.writeVarInt(Block.getStateId((BlockState) input));
		}
		buf.writeVarInt(Block.getStateId(outputState));
		buf.writeVarInt(time);
	}

	public static RecipePureDaisy read(PacketBuffer buf) {
		ResourceLocation id = buf.readResourceLocation();
		Object input;
		switch (buf.readVarInt()) {
			case 0: input = new BlockTags.Wrapper(buf.readResourceLocation()); break;
			case 1: input = Registry.BLOCK.getByValue(buf.readVarInt()); break;
			case 2: input = Block.getStateById(buf.readVarInt()); break;
			default: throw new RuntimeException("Unknown input discriminator");
		}
		BlockState output = Block.getStateById(buf.readVarInt());
		int time = buf.readVarInt();
		return new RecipePureDaisy(id, input, output, time);
	}

}
