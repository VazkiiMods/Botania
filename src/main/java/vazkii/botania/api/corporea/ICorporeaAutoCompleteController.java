/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [June 8, 2015, 1:04:05 PM (GMT)]
 */
package vazkii.botania.api.corporea;

/**
 * An interface for systems which may control if the item auto complete system should be active
 */
public interface ICorporeaAutoCompleteController {

	/**
	 * Return true if auto completion should be enabled.
	 */
	public boolean shouldAutoComplete();

}
