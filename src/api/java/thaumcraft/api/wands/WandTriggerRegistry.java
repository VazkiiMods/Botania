package thaumcraft.api.wands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * This class serves a similar function to IWandable in that it allows wands to interact
 * with object in the world. In this case it is most useful for adding interaction with non-mod
 * blocks where you can't control what happens in their code.
 * Example where it is used is in crafting the thaumonomicon from a bookshelf and the
 * crucible from a cauldron
 * 
 * @author azanor
 *
 */
public class WandTriggerRegistry {
	
	private static HashMap<String,HashMap<IBlockState,List>> triggers = new HashMap<String,HashMap<IBlockState,List>>();
	private static final String DEFAULT = "default";

	/**
	 * Registers an action to perform when a casting wand right clicks on a specific block. 
	 * A manager class needs to be created that implements IWandTriggerManager.
	 * @param manager
	 * @param event a logical number that you can use to differentiate different events or actions
	 * @param blockState
	 * @param meta send -1 as a wildcard value for all possible meta values
	 * @param modid a unique identifier. It is best to register your own triggers using your mod id to avoid conflicts with mods that register triggers for the same block
	 */
	public static void registerWandBlockTrigger(IWandTriggerManager manager, int event, IBlockState state, String modid) {
		if (!triggers.containsKey(modid)) {
			triggers.put(modid, new HashMap<IBlockState,List>());
		}
		HashMap<IBlockState,List> temp = triggers.get(modid);
		temp.put(state,Arrays.asList(manager,event));
		triggers.put(modid, temp);
	}
	
	/**
	 * for legacy support
	 */
	public static void registerWandBlockTrigger(IWandTriggerManager manager, int event, IBlockState state) {
		registerWandBlockTrigger(manager, event, state, DEFAULT);
	}
	
	/**
	 * Checks all trigger registries if one exists for the given block and meta
	 * @param blockState
	 * @param meta
	 * @return
	 */
	public static boolean hasTrigger(IBlockState state) {
		for (String modid:triggers.keySet()) {
			HashMap<IBlockState,List> temp = triggers.get(modid);
			if (temp.containsKey(state)) return true;
		}
		return false;
	}
	
	/**
	 * modid sensitive version
	 */
	public static boolean hasTrigger(IBlockState state, String modid) {
		if (!triggers.containsKey(modid)) return false;
		HashMap<IBlockState,List> temp = triggers.get(modid);
		if (temp.containsKey(state)) return true;
		return false;
	}
	
	
	/**
	 * This is called by the onItemUseFirst function in wands. 
	 * Parameters and return value functions like you would expect for that function.
	 * @param world
	 * @param wand
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @param blockState
	 * @param meta
	 * @return
	 */
	public static boolean performTrigger(World world, ItemStack wand, EntityPlayer player, 
			BlockPos pos, EnumFacing side, IBlockState state) {
		
		for (String modid:triggers.keySet()) {
			HashMap<IBlockState,List> temp = triggers.get(modid);
			List l = temp.get(state);
			if (l==null) continue;
			
			IWandTriggerManager manager = (IWandTriggerManager) l.get(0);
			int event = (Integer) l.get(1);
			boolean result = manager.performTrigger(world, wand, player, pos, side, event);
			if (result) return true;
		}
		return false;
	}
	
	/**
	 * modid sensitive version
	 */
	public static boolean performTrigger(World world, ItemStack wand, EntityPlayer player, 
			BlockPos pos, EnumFacing side, IBlockState state, String modid) {
		if (!triggers.containsKey(modid)) return false;
		HashMap<IBlockState,List> temp = triggers.get(modid);
		List l = temp.get(state);
		if (l==null) return false;
		
		IWandTriggerManager manager = (IWandTriggerManager) l.get(0);
		int event = (Integer) l.get(1);
		return manager.performTrigger(world, wand, player, pos, side, event);
	}
		
}
