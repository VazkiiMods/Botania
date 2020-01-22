/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [2020-01-13, 17:34 (UTC+2)]
 */
package vazkii.botania.common.network;

import java.util.function.IntSupplier;

public interface ILoginPacket extends IntSupplier {
	
	void setLoginIndex(int index);

	int getLoginIndex();

	@Override
	default int getAsInt() {
		return getLoginIndex();
	}
}
