package thaumcraft.api.aspects;

import net.minecraft.util.EnumFacing;


/**
 * @author Azanor
 * This interface is used by tiles that use or transport vis. 
 * Only tiles that implement this interface will be able to connect to vis conduits or other thaumic devices
 */
public interface IEssentiaTransport {
	/**
	 * Is this tile able to connect to other vis users/sources on the specified side?
	 * @param face
	 * @return
	 */
	public boolean isConnectable(EnumFacing face);
	
	/**
	 * Is this side used to input essentia?
	 * @param face
	 * @return
	 */
	boolean canInputFrom(EnumFacing face);
	
	/**
	 * Is this side used to output essentia?
	 * @param face
	 * @return
	 */
	boolean canOutputTo(EnumFacing face);
			
	/**
	 * Sets the amount of suction this block will apply
	 * @param suction
	 */
	public void setSuction(Aspect aspect, int amount);
	
	/**
	 * Returns the type of suction this block is applying. 
	 * @param loc
	 * 		the location from where the suction is being checked
	 * @return
	 * 		a return type of null indicates the suction is untyped and the first thing available will be drawn
	 */
	public Aspect getSuctionType(EnumFacing face);
	
	/**
	 * Returns the strength of suction this block is applying. 
	 * @param loc
	 * 		the location from where the suction is being checked
	 * @return
	 */
	public int getSuctionAmount(EnumFacing face);
	
	/**
	 * remove the specified amount of essentia from this transport tile
	 * @return how much was actually taken
	 */
	public int takeEssentia(Aspect aspect, int amount, EnumFacing face);
	
	/**
	 * add the specified amount of essentia to this transport tile
	 * @return how much was actually added
	 */
	public int addEssentia(Aspect aspect, int amount, EnumFacing face);
	
	/**
	 * What type of essentia this contains
	 * @param face
	 * @return
	 */
	public Aspect getEssentiaType(EnumFacing face);
	
	/**
	 * How much essentia this block contains
	 * @param face
	 * @return
	 */
	public int getEssentiaAmount(EnumFacing face);
	
	
	/**
	 * Essentia will not be drawn from this container unless the suction exceeds this amount.
	 * @return the amount
	 */
	public int getMinimumSuction();

	
	
}

