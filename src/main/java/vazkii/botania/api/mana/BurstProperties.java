/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 31, 2014, 3:49:30 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * The properties of a mana burst, when shot. This is passed to the lens
 * currently on the mana spreader to apply changes.
 */
public final class BurstProperties {

	public int maxMana;
	public int ticksBeforeManaLoss;
	public float manaLossPerTick;
	public float gravity;
	public float motionModifier;

	public int color;

	public BurstProperties(int maxMana, int ticksBeforeManaLoss, float manaLossPerTick, float gravity, float motionModifier, int color) {
		this.maxMana = maxMana;
		this.ticksBeforeManaLoss = ticksBeforeManaLoss;
		this.manaLossPerTick = manaLossPerTick;
		this.gravity = gravity;
		this.motionModifier = motionModifier;
		this.color = color;
	}

}
