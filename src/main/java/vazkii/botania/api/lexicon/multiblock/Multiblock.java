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
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Mult;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;

/**
 * This class describes a Mutiblock object. It's used to display a
 * multiblock in the lexicon and to show the player in a ghost-like
 * look in the world.
 */
public class Multiblock {
	
	public List<MultiblockComponent> components = new ArrayList();
	
	/**
	 * Adds a multiblock component to this multiblock. The component's x y z
	 * coords should be pivoted to the center of the structure. 
	 */
	public void addComponent(MultiblockComponent component) {
		components.add(component);
	}

	/**
	 * Constructs and adds a multiblock component to this multiblock. The x y z
	 * coords should be pivoted to the center of the structure.
	 */
	public void addComponent(int x, int y, int z, Block block, int meta) {
		components.add(new MultiblockComponent(new ChunkCoordinates(x, y, z), block, meta));
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
	
}
