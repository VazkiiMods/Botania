/**
 * This class was created by <quaternary>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.api.corporea;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** 
 * CorporeaIndexRequestEvent is fired when a player attempts to request an item from a corporea index.
 * If the event is cancelled, the request will not be performed.
 * */
@Cancelable
public class CorporeaIndexRequestEvent extends Event {
	public final EntityPlayerMP requester;
	public String request;
	public int requestCount;
	public ICorporeaSpark indexSpark;
	
	public CorporeaIndexRequestEvent(EntityPlayerMP requester, String request, int requestCount, ICorporeaSpark indexSpark) {
		this.requester = requester;
		this.request = request;
		this.requestCount = requestCount;
		this.indexSpark = indexSpark;
	}
}
