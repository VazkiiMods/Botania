/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 27, 2015, 2:37:22 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class describes a Mutiblock object. It's used to display a
 * multiblock in the lexicon and to show the player in a ghost-like
 * look in the world.
 */
public class Multiblock {

	public final List<MultiblockComponent> components = new ArrayList<>();
	public final List<ItemStack> materials = new ArrayList<>();

	public BlockPos minPos = BlockPos.ORIGIN;
	public BlockPos maxPos = BlockPos.ORIGIN;
	public BlockPos offPos = BlockPos.ORIGIN;

	public final HashMap<BlockPos, MultiblockComponent> locationCache = new HashMap<>();

	/**
	 * Adds a multiblock component to this multiblock. The component's x y z
	 * coords should be pivoted to the center of the structure.
	 */
	public void addComponent(MultiblockComponent component) {
		if(getComponentForLocation(component.getRelativePosition()) != null)
			throw new IllegalArgumentException("Location in multiblock already occupied");
		components.add(component);
		changeAxisForNewComponent(component.getRelativePosition());
		calculateCostForNewComponent(component);
		addComponentToLocationCache(component);
	}

	/**
	 * Constructs and adds a multiblock component to this multiblock. The x y z
	 * coords should be pivoted to the center of the structure.
	 */
	public void addComponent(BlockPos pos, IBlockState state) {
		addComponent(new MultiblockComponent(pos, state));
	}

	private void changeAxisForNewComponent(BlockPos pos) {
		if(pos.getX() < minPos.getX())
			minPos = new BlockPos(pos.getX(), minPos.getY(), minPos.getZ());
		else if(pos.getX() > maxPos.getX())
			maxPos = new BlockPos(pos.getX(), maxPos.getY(), maxPos.getZ());

		if(pos.getY() < minPos.getY())
			minPos = new BlockPos(minPos.getX(), pos.getY(), minPos.getZ());
		else if(pos.getY() > maxPos.getY())
			maxPos = new BlockPos(maxPos.getX(), pos.getY(), maxPos.getZ());

		if(pos.getZ() < minPos.getZ())
			minPos = new BlockPos(minPos.getX(), minPos.getY(), pos.getZ());
		else if(pos.getZ() > maxPos.getZ())
			maxPos = new BlockPos(maxPos.getX(), maxPos.getY(), pos.getZ());
	}

	private void calculateCostForNewComponent(MultiblockComponent comp) {
		ItemStack[] materials = comp.getMaterials();
		if(materials != null)
			for(ItemStack stack : materials)
				addStack(stack);
	}

	private void addStack(ItemStack stack) {
		if(stack.isEmpty())
			return;

		for(ItemStack oStack : materials)
			if(oStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(oStack, stack)) {
				oStack.grow(stack.getCount());
				return;
			}

		materials.add(stack);
	}

	public void setRenderOffset(BlockPos pos) {
		offPos = pos;
	}

	public List<MultiblockComponent> getComponents() {
		return components;
	}

	/**
	 * Rotates this multiblock by the angle passed in. For the best results, use
	 * only multiples of pi/2.
	 */
	public void rotate(double angle) {
		for(MultiblockComponent comp : getComponents())
			comp.rotate(angle);
		updateLocationCache();
	}

	public Multiblock copy() {
		Multiblock mb = new Multiblock();
		for(MultiblockComponent comp : getComponents())
			mb.addComponent(comp.copy());

		return mb;
	}

	/**
	 * Creates a length 4 array of all the rotations multiple of pi/2 required
	 * to render this multiblock in the world relevant to the 4 cardinal
	 * orientations.
	 */
	public Map<EnumFacing, Multiblock> createRotations() {
		Map<EnumFacing, Multiblock> ret = new EnumMap<>(EnumFacing.class);

		ret.put(EnumFacing.SOUTH, this);

		ret.put(EnumFacing.WEST, ret.get(EnumFacing.SOUTH).copy());
		ret.get(EnumFacing.WEST).rotate(Math.PI / 2);

		ret.put(EnumFacing.NORTH, ret.get(EnumFacing.WEST).copy());
		ret.get(EnumFacing.NORTH).rotate(Math.PI / 2);

		ret.put(EnumFacing.EAST, ret.get(EnumFacing.NORTH).copy());
		ret.get(EnumFacing.EAST).rotate(Math.PI / 2);

		return ret;
	}

	/**
	 * Makes a MultiblockSet from this Multiblock and its rotations using
	 * createRotations().
	 */
	public MultiblockSet makeSet() {
		return new MultiblockSet(this);
	}

	public int getXSize() {
		return Math.abs(minPos.getX()) + Math.abs(maxPos.getX()) + 1;
	}

	public int getYSize() {
		return Math.abs(minPos.getY()) + Math.abs(maxPos.getY()) + 1;
	}

	public int getZSize() {
		return Math.abs(minPos.getZ()) + Math.abs(maxPos.getZ()) + 1;
	}

	/**
	 * Rebuilds the location cache
	 */
	public void updateLocationCache() {
		locationCache.clear();
		for(MultiblockComponent comp : components)
			addComponentToLocationCache(comp);
	}

	/**
	 * Adds a single component to the location cache
	 */
	private void addComponentToLocationCache(MultiblockComponent comp) {
		BlockPos pos = comp.getRelativePosition();
		locationCache.put(pos, comp);
	}

	/**
	 * Gets the component for a given location
	 * @param pos
	 */
	public MultiblockComponent getComponentForLocation(BlockPos pos) {
		return locationCache.get(pos);
	}

}
