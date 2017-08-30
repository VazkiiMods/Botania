/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 8, 2014, 7:02:48 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

/**
 * Have a LexiconEntry implement this to signify it's an "Addon entry", as
 * in, one provided by an Addon. This allows it to draw a subtitle of
 * sorts, to prevent the [Mod tag here] nonsense that happened with thaumcraft
 * addons. It can also be used for other purposes, such as stating an
 * entry is WIP.
 */
public interface IAddonEntry {

	/**
	 * Returns the <b>unlocalized</b> subtitle to show below the title. Here you'd
	 * return something like "(This Entry is provided by the Botanic Tinkerer addon)".
	 */
	public String getSubtitle();

}
