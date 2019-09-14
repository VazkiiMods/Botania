package vazkii.botania.common.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.fx.ParticleData;
import vazkii.botania.common.core.helper.Vector3;

public interface IProxy {
	default void registerHandlers() {}

	void setEntryToOpen(LexiconEntry entry);

	void setToTutorialIfFirstLaunch();

	void setLexiconStack(ItemStack stack);

	boolean isTheClientPlayer(LivingEntity entity);

	PlayerEntity getClientPlayer();

	boolean isClientPlayerWearingMonocle();

	String getLastVersion();

	boolean openWikiPage(World world, Block block, BlockRayTraceResult pos);

	void setMultiblock(World world, int x, int y, int z, double radius, Block block);

	void removeSextantMultiblock();

	long getWorldElapsedTicks();

	void setSparkleFXNoClip(boolean noclip);

	void setSparkleFXCorrupt(boolean corrupt);

	default void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m) {
		sparkleFX(x, y, z, r, g, b, size, m, false);
	}

	void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m, boolean fake);

	default void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner);

	void addBoss(IBotaniaBoss boss);

	void removeBoss(IBotaniaBoss boss);

	int getClientRenderDistance();

	Object getEmptyModelBiped();
}
