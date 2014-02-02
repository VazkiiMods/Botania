/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 5:01:19 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * Any TileEntity that implements this is considered a mana collector, by
 * which nearby generating flowers will pump mana into it.<br><br>
 * 
 * <b>Implementation Instructions:</b><br>
 * - Override invalidate() and onChunkUnload(), calling <i>ManaNetworkEvent.removeCollector(this);</i> on both.<br>
 * - On the first tick of onUpdate(), call </i>ManaNetworkEvent.addCollector(this);<i>
 */
public interface IManaCollector extends IManaReceiver {

	/**
	 * Called every tick on the client case the player is holding a Wand of the Forest.
	 */
	public void onClientDisplayTick();

}
