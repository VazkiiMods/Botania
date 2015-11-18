package thaumcraft.api.aura;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;

public class AuraHelper {
	
	
	/**
	 * Consume vis from the aura at the given location
	 * @param world
	 * @param pos
	 * @param aspect
	 * @param amount
	 * @return has anything been consumed
	 */
	public static boolean drainAura(World world, BlockPos pos, Aspect aspect, int amount) {
		return ThaumcraftApi.internalMethods.drainAura(world,pos, aspect, amount);
	}
	
	/**
	 * Consume vis from the aura at the given location
	 * @param world
	 * @param pos
	 * @param aspect
	 * @param amount
	 * @return how much was actually drained
	 */
	public static int drainAuraAvailable(World world, BlockPos pos, Aspect aspect, int amount) {
		return ThaumcraftApi.internalMethods.drainAuraAvailable(world,pos, aspect, amount);
	}
	
	/**
	 * Adds vis to the aura at the given location. This does not actually increase the aura instantly - 
	 * it merely adds a recharge ticked to the chunk that will add to the aura there the next time it is processed.	 
	 *  
	 * @param world
	 * @param pos
	 * @param aspect
	 * @param amount
	 */
	public static void addAura(World world, BlockPos pos, Aspect aspect, int amount) {
		ThaumcraftApi.internalMethods.addAura(world,pos, aspect, amount);
	}
	
	/**
	 * Get how much of a given aspect is in the aura at the given location.
	 * @param world
	 * @param pos
	 * @param aspect
	 * @return
	 */
	public static int getAura(World world, BlockPos pos, Aspect aspect) {
		return ThaumcraftApi.internalMethods.getAura(world,pos, aspect);
	}
	
	/**
	 * Gets the general aura baseline at the given location
	 * @param world
	 * @param pos
	 * @return
	 */
	public static int getAuraBase(World world, BlockPos pos) {
		return ThaumcraftApi.internalMethods.getAuraBase(world,pos);
	}
	
	/**
	 * Adds flux to the aura at the specified block position.
	 * @param world
	 * @param pos
	 * @param amount how much flux to add
	 * @param showEffect if set to true, a flux goo splash effect and sound will also be displayed. Use in moderation.
	 */
	public static void pollute(World world, BlockPos pos, int amount, boolean showEffect) {
		ThaumcraftApi.internalMethods.pollute(world,pos,amount,showEffect);
	}

	
}
