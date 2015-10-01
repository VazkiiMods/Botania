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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

/**
 * This class describes a Mutiblock object. It's used to display a
 * multiblock in the lexicon and to show the player in a ghost-like
 * look in the world.
 */
public class Multiblock {

	public List<MultiblockComponent> components = new ArrayList();
	public List<ItemStack> materials = new ArrayList();

	public int minX, minY, minZ, maxX, maxY, maxZ, offX, offY, offZ;

	public HashMap<List<Integer>, MultiblockComponent> locationCache = new HashMap<List<Integer>, MultiblockComponent>();

	/**
	 * Adds a multiblock component to this multiblock. The component's x y z
	 * coords should be pivoted to the center of the structure.
	 */
	public void addComponent(MultiblockComponent component) {
		if(getComponentForLocation(component.relPos.posX, component.relPos.posY, component.relPos.posZ) != null)
			throw new IllegalArgumentException("Location in multiblock already occupied");
		components.add(component);
		changeAxisForNewComponent(component.relPos.posX, component.relPos.posY, component.relPos.posZ);
		calculateCostForNewComponent(component);
		addComponentToLocationCache(component);
	}

	/**
	 * Constructs and adds a multiblock component to this multiblock. The x y z
	 * coords should be pivoted to the center of the structure.
	 */
	public void addComponent(int x, int y, int z, Block block, int meta) {
		addComponent(new MultiblockComponent(new ChunkCoordinates(x, y, z), block, meta));
	}

	private void changeAxisForNewComponent(int x, int y, int z) {
		if(x < minX)
			minX = x;
		else if(x > maxX)
			maxX = x;

		if(y < minY)
			minY = y;
		else if(y > maxY)
			maxY = y;

		if(z < minZ)
			minZ = z;
		else if(z > maxZ)
			maxZ = z;
	}

	private void calculateCostForNewComponent(MultiblockComponent comp) {
		ItemStack[] materials = comp.getMaterials();
		if(materials != null)
			for(ItemStack stack : materials)
				addStack(stack);
	}

	private void addStack(ItemStack stack) {
		if(stack == null)
			return;

		for(ItemStack oStack : materials)
			if(oStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(oStack, stack)) {
				oStack.stackSize += stack.stackSize;
				return;
			}

		materials.add(stack);
	}

	public void setRenderOffset(int x, int y, int z) {
		offX = x;
		offY = y;
		offZ = z;
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
	public Multiblock[] createRotations() {
		Multiblock[] blocks = new Multiblock[4];
		blocks[0] = this;
		blocks[1] = blocks[0].copy();
		blocks[1].rotate(Math.PI / 2);
		blocks[2] = blocks[1].copy();
		blocks[2].rotate(Math.PI / 2);
		blocks[3] = blocks[2].copy();
		blocks[3].rotate(Math.PI / 2);

		return blocks;
	}

	/**
	 * Makes a MultiblockSet from this Multiblock and its rotations using
	 * createRotations().
	 */
	public MultiblockSet makeSet() {
		return new MultiblockSet(this);
	}

	public int getXSize() {
		return Math.abs(minX) + Math.abs(maxX) + 1;
	}

	public int getYSize() {
		return Math.abs(minY) + Math.abs(maxY) + 1;
	}

	public int getZSize() {
		return Math.abs(minZ) + Math.abs(maxZ) + 1;
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
		ChunkCoordinates pos = comp.getRelativePosition();
		locationCache.put(Arrays.asList(
				pos.posX,
				pos.posY,
				pos.posZ
				),  comp);
	}

	/**
	 * Gets the component for a given location
	 */
	public MultiblockComponent getComponentForLocation(int x, int y, int z) {
		return locationCache.get(Arrays.asList(x, y, z));
	}
}
