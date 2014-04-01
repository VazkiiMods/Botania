/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 1, 2014, 2:46:32 PM (GMT)]
 */
package vazkii.botania.api.summoning;

import java.util.Arrays;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

// TODO Document
public abstract class SummoningBase {
	
	public final int cost;
	public final int time;
	public final int[] colors;
	public final List<Object> inputs;

	public SummoningBase(int cost, int time, int[] colors, Object... inputs) {
		this.cost = cost;
		this.time = time;
		this.colors = colors;
		this.inputs = Arrays.asList(inputs);
	}
	
	public void onStart(TileEntity shrine, TileEntity saisen) {
		// NO-OP
	}
	
	public void onTick(TileEntity shrine, TileEntity saisen) {
		// NO-OP
	}
	
	public void onFinish(TileEntity shrine, TileEntity saisen) {
		// NO-OP
	}
}
