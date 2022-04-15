/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import vazkii.botania.api.item.ISparkEntity;

import javax.annotation.Nullable;

import java.util.Collection;

/**
 * An Entity that implements this is considered a Mana Spark.
 */
public interface IManaSpark extends ISparkEntity {
	/**
	 * Get the thing this spark is attached to, if any
	 */
	@Nullable
	ISparkAttachable getAttachedTile();

	/**
	 * Gets a collection of all Sparks this is tranfering to.
	 */
	Collection<IManaSpark> getTransfers();

	/**
	 * Registers the Spark passed in as a Spark meant for mana to be transfered towards.
	 */
	void registerTransfer(IManaSpark entity);

	SparkUpgradeType getUpgrade();

	void setUpgrade(SparkUpgradeType upgrade);

	/**
	 * See {@link ISparkAttachable#areIncomingTranfersDone()}
	 */
	boolean areIncomingTransfersDone();
}
