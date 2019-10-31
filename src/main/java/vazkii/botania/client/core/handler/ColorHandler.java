package vazkii.botania.client.core.handler;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPlatform;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.Item16Colors;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ItemManaMirror;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemBloodPendant;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.item.material.ItemPetal;

import java.awt.*;

public final class ColorHandler {

	public static void init() {
		BlockColors blocks = Minecraft.getInstance().getBlockColors();

		// [VanillaCopy] BlockColors for vine
		IBlockColor vineColor = (state, world, pos, tint) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefault();
		blocks.register(vineColor, ModBlocks.solidVines);

		// Pool
		blocks.register(
				(state, world, pos, tintIndex) -> {
					if (((BlockPool) state.getBlock()).variant == BlockPool.Variant.FABULOUS) {
						float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
						return Color.HSBtoRGB(time * 0.005F, 0.6F, 1F);
					} else {
						if(world != null && pos != null) {
							TileEntity te = world.getTileEntity(pos);
							if(te instanceof TilePool) {
								return ((TilePool) te).color.colorValue;
							}
						}
						return DyeColor.WHITE.colorValue;
					}
				},
				ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool, ModBlocks.fabulousPool
				);

		// Spreader
		blocks.register(
				(state, world, pos, tintIndex) -> {
					float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
					return Color.HSBtoRGB(time * 5 % 360 / 360F, 0.4F, 0.9F);
				},
				ModBlocks.gaiaSpreader
				);

		// Petal Block
		blocks.register((state, world, pos, tintIndex) -> ((BlockPetalBlock) state.getBlock()).color.colorValue,
				ModBlocks.petalBlockWhite, ModBlocks.petalBlockOrange, ModBlocks.petalBlockMagenta, ModBlocks.petalBlockLightBlue,
				ModBlocks.petalBlockYellow, ModBlocks.petalBlockLime, ModBlocks.petalBlockPink, ModBlocks.petalBlockGray,
				ModBlocks.petalBlockSilver, ModBlocks.petalBlockCyan, ModBlocks.petalBlockPurple, ModBlocks.petalBlockBlue,
				ModBlocks.petalBlockBrown, ModBlocks.petalBlockGreen, ModBlocks.petalBlockRed, ModBlocks.petalBlockBlack
				);

		// Platforms
		blocks.register(
				(state, world, pos, tintIndex) -> {
					if (world != null && pos != null) {
						TileEntity tile = world.getTileEntity(pos);
						if(tile instanceof TilePlatform) {
							TilePlatform camo = (TilePlatform) tile;
							BlockState camoState = camo.camoState;
							if(camoState != null)
								return camoState.getBlock() instanceof BlockPlatform
										? 0xFFFFFF
												: Minecraft.getInstance().getBlockColors().getColor(camoState, world, pos, tintIndex);
						}
					}
					return 0xFFFFFF;
				}, ModBlocks.abstrusePlatform, ModBlocks.spectralPlatform, ModBlocks.infrangiblePlatform);

		ItemColors items = Minecraft.getInstance().getItemColors();

		items.register((s, t) -> Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 0.25F, 1F),
						ModItems.lifeEssence, ModItems.gaiaIngot);

		items.register((s, t) ->
		t == 1 ? DyeColor.byId(ItemTwigWand.getColor1(s)).colorValue
				: t == 2 ? DyeColor.byId(ItemTwigWand.getColor2(s)).colorValue
						: -1,
						ModItems.twigWand);

		IItemColor petalHandler = (s, t) -> ((ItemPetal) s.getItem()).color.colorValue;
		IItemColor dyeHandler = (s, t) -> ((Item16Colors) s.getItem()).color.colorValue;
		for(DyeColor color : DyeColor.values()) {
			items.register(petalHandler, ModItems.getPetal(color));
			items.register(dyeHandler, ModItems.getDye(color));
		}

		items.register((s, t) -> Minecraft.getInstance().getBlockColors().getColor(((BlockItem)s.getItem()).getBlock().getDefaultState(), null, null, t),
				ModBlocks.petalBlockWhite, ModBlocks.petalBlockOrange, ModBlocks.petalBlockMagenta, ModBlocks.petalBlockLightBlue,
				ModBlocks.petalBlockYellow, ModBlocks.petalBlockLime, ModBlocks.petalBlockPink, ModBlocks.petalBlockGray,
				ModBlocks.petalBlockSilver, ModBlocks.petalBlockCyan, ModBlocks.petalBlockPurple, ModBlocks.petalBlockBlue,
				ModBlocks.petalBlockBrown, ModBlocks.petalBlockGreen, ModBlocks.petalBlockRed, ModBlocks.petalBlockBlack,
				ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool, ModBlocks.fabulousPool, ModBlocks.gaiaSpreader);

		items.register((s, t) -> t == 1 ? Color.HSBtoRGB(0.528F, (float) ((ItemManaMirror) ModItems.manaMirror).getMana(s) / (float) TilePool.MAX_MANA, 1F) : -1, ModItems.manaMirror);

		items.register((s, t) -> t == 1 ? Color.HSBtoRGB(0.528F, (float) ((ItemManaTablet) ModItems.manaTablet).getMana(s) / (float) ItemManaTablet.MAX_MANA, 1F) : -1, ModItems.manaTablet);

		items.register((s, t) -> Color.HSBtoRGB(0.55F, ((float) s.getMaxDamage() - (float) s.getDamage()) / (float)s.getMaxDamage() * 0.5F, 1F), ModItems.spellCloth);

		items.register((s, t) -> {
			if(t != 1)
				return -1;

			Brew brew = ((IBrewItem) s.getItem()).getBrew(s);
			if(brew == BotaniaAPI.fallbackBrew)
				return s.getItem() instanceof ItemBloodPendant ? 0xC6000E : 0x989898;

			Color color = new Color(brew.getColor(s));
			double speed = s.getItem() == ModItems.brewFlask || s.getItem() == ModItems.brewVial ? 0.1 : 0.2;
			int add = (int) (Math.sin(ClientTickHandler.ticksInGame * speed) * 24);

			int r = Math.max(0, Math.min(255, color.getRed() + add));
			int g = Math.max(0, Math.min(255, color.getGreen() + add));
			int b = Math.max(0, Math.min(255, color.getBlue() + add));

			return r << 16 | g << 8 | b;
		}, ModItems.bloodPendant, ModItems.incenseStick, ModItems.brewFlask, ModItems.brewVial);

		items.register((s, t) -> {
			ItemStack lens = ItemManaGun.getLens(s);
			if(!lens.isEmpty() && t == 0)
				return Minecraft.getInstance().getItemColors().getColor(lens, t);

			if(t == 2) {
				BurstProperties props = ((ItemManaGun) s.getItem()).getBurstProps(Minecraft.getInstance().player, s, false, Hand.MAIN_HAND);
				Color color = new Color(props.color);

				float mul = (float) (Math.sin((double) ClientTickHandler.ticksInGame / 5) * 0.15F);
				int c = (int) (255 * mul);

				return new Color(Math.max(0, Math.min(255, color.getRed() + c)), Math.max(0, Math.min(255, color.getGreen() + c)), Math.max(0, Math.min(255, color.getBlue() + c))).getRGB();
			} else return -1;
		}, ModItems.manaGun);

		items.register((s, t) -> t == 1 ? Color.HSBtoRGB(0.75F, 1F, 1.5F - (float) Math.min(1F, Math.sin(Util.milliTime() / 100D) * 0.5 + 1.2F)) : -1, ModItems.enderDagger);

		items.register((s, t) -> t == 1 && ItemTerraPick.isEnabled(s) ? Color.HSBtoRGB(0.375F, (float) Math.min(1F, Math.sin(Util.milliTime() / 200D) * 0.5 + 1F), 1F) : -1, ModItems.terraPick);

		dyeHandler = (s, t) -> t == 0 ? ((ItemLens) s.getItem()).getLensColor(s) : -1;
		items.register(dyeHandler, ModItems.lensNormal, ModItems.lensSpeed, ModItems.lensPower, ModItems.lensTime, ModItems.lensEfficiency, ModItems.lensBounce,
				ModItems.lensGravity, ModItems.lensMine, ModItems.lensDamage, ModItems.lensPhantom, ModItems.lensMagnet,
				ModItems.lensExplosive, ModItems.lensWeight, ModItems.lensPaint, ModItems.lensFire, ModItems.lensPiston,
				ModItems.lensLight, ModItems.lensWarp, ModItems.lensRedirect, ModItems.lensFirework, ModItems.lensFlare,
				ModItems.lensMessenger, ModItems.lensTripwire, ModItems.lensStorm);
	}

	private ColorHandler() {}

}
