/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 19, 2015, 10:05:24 PM (GMT)]
 */
package vazkii.botania.api.mana;

import java.util.UUID;

/**
 * This interface marks a TileEntity that can be identified through
 * an UUID. This UUID must presist between sessions.
 */
public interface IIdentifiable {

	public UUID getIdentifier();

}
