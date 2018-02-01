package vazkii.botania.common.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.helper.Vector3;

public interface IProxy {
	void preInit(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	void postInit(FMLPostInitializationEvent event);

	void setEntryToOpen(LexiconEntry entry);

	void setToTutorialIfFirstLaunch();

	void setLexiconStack(ItemStack stack);

	boolean isTheClientPlayer(EntityLivingBase entity);

	EntityPlayer getClientPlayer();

	boolean isClientPlayerWearingMonocle();

	String getLastVersion();

	boolean openWikiPage(World world, Block block, RayTraceResult pos);

	void setMultiblock(World world, int x, int y, int z, double radius, Block block);

	void removeSextantMultiblock();

	long getWorldElapsedTicks();

	void setSparkleFXNoClip(boolean noclip);

	void setSparkleFXCorrupt(boolean corrupt);

	default void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m) {
		sparkleFX(x, y, z, r, g, b, size, m, false);
	}

	void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m, boolean fake);

	void setWispFXDistanceLimit(boolean limit);

	void setWispFXDepthTest(boolean depth);

	default void wispFX(double x, double y, double z, float r, float g, float b, float size) {
		wispFX(x, y, z, r, g, b, size, 0F);
	}

	default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(x, y, z, r, g, b, size, gravity, 1F);
	}

	default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}

	default void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		wispFX(x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	}

	void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul);

	default void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner);

	void addBoss(IBotaniaBoss boss);

	void removeBoss(IBotaniaBoss boss);

	int getClientRenderDistance();

	Object getEmptyModelBiped();
}
