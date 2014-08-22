/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 21, 2014, 5:44:07 PM (GMT)]
 */
package vazkii.botania.api.mana.spark;

import java.util.Collection;

public interface ISparkEntity {

	public ISparkAttachable getAttachedTile();

	public Collection<ISparkEntity> getTransfers();

	public void registerTransfer(ISparkEntity entity);

	public int getUpgrade();

	public void setUpgrade(int upgrade);

	public boolean areIncomingTransfersDone();

}
