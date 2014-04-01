/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 1, 2014, 2:46:38 PM (GMT)]
 */
package vazkii.botania.api.summoning;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class SummoningCrafting extends SummoningBase {

	private final ItemStack output;
	
	public SummoningCrafting(int cost, int time, int[] colors, ItemStack output, Object[] inputs) {
		super(cost, time, colors, inputs);
		this.output = output;
	}
	
	@Override
	public void onFinish(TileEntity shrine, TileEntity saisen) {
		// TODO
	}
}
