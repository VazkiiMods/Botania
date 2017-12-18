/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2014, 7:45:37 PM (GMT)]
 */
package vazkii.botania.common.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {}

	@Override
	public void setEntryToOpen(LexiconEntry entry) {}

	@Override
	public void setToTutorialIfFirstLaunch() {}

	@Override
	public void setLexiconStack(ItemStack stack) {}

	@Override
	public boolean isTheClientPlayer(EntityLivingBase entity) {
		return false;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return false;
	}

	@Override
	public String getLastVersion() {
		return LibMisc.BUILD;
	}

	@Override
	public boolean openWikiPage(World world, Block block, RayTraceResult pos) {
		return false;
	}

	@Override
	public void setMultiblock(World world, int x, int y, int z, double radius, Block block) {}

	@Override
	public void removeSextantMultiblock() {}

	@Override
	public long getWorldElapsedTicks() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getTotalWorldTime();
	}

	@Override
	public void setSparkleFXNoClip(boolean noclip) {}

	@Override
	public void setSparkleFXCorrupt(boolean corrupt) {}

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {}

	@Override
	public void setWispFXDistanceLimit(boolean limit) {}

	@Override
	public void setWispFXDepthTest(boolean depth) {}

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {}

	@Override
	public void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {}

	@Override
	public void addBoss(IBotaniaBoss boss) {}

	@Override
	public void removeBoss(IBotaniaBoss boss) {}

	@Override
	public int getClientRenderDistance() {
		return 0;
	}

	@Override
	public Object getEmptyModelBiped() {
		return null;
	}

}
