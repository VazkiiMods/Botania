/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.PlatformBlock;
import vazkii.botania.common.block.block_entity.PlatformBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.bauble.TaintedBloodPendantItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.common.item.material.MysticalPetalItem;
import vazkii.botania.mixin.client.MinecraftAccessor;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public final class ColorHandler {

	public static final float MANA_HUE = 0.528F;

	public interface BlockHandlerConsumer {
		void register(BlockColor handler, Block... blocks);
	}

	public interface ItemHandlerConsumer {
		void register(ItemColor handler, ItemLike... items);
	}

	private static Block[] getModBlocks(Predicate<Block> blockPredicate) {
		return BuiltInRegistries.BLOCK.stream()
				.filter(blockPredicate)
				.filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(BotaniaAPI.MODID))
				.toArray(Block[]::new);
	}

	public static void submitBlocks(BlockHandlerConsumer blocks) {
		// [VanillaCopy] BlockColors for vine
		blocks.register((state, world, pos, tintIndex) -> world != null && pos != null
				? BiomeColors.getAverageFoliageColor(world, pos)
				: FoliageColor.getDefaultColor(),
				BotaniaBlocks.solidVines);

		// Pool
		blocks.register(
				(state, world, pos, tintIndex) -> {
					if (tintIndex != 0 || !(state.getBlock() instanceof ManaPoolBlock poolBlock)) {
						return -1;
					}

					Optional<Integer> color = poolBlock.getOptionalColor().map(
							c -> FastColor.ARGB32.lerp(0.8f, 0xFFFFFF, MysticalPetalItem.getPetalLikeColor(c)));
					if (poolBlock.isFabulous()) {
						float time = (ClientTickHandler.getEntityTicksInGame() + ClientTickHandler.getEntityPartialTick()) * 0.005F;
						float posOffset = pos != null ? new Random(state.getSeed(pos)).nextFloat() : 0;
						int fabulousColor = Mth.hsvToRgb((time + posOffset) % 1f, 0.4F, 1F);
						return color.map(c -> FastColor.ARGB32.multiply(fabulousColor, c)).orElse(fabulousColor);
					}
					return color.orElse(-1);
				},
				getModBlocks(block -> block instanceof ManaPoolBlock pool && (pool.color != null || pool.isFabulous()))
		);

		// Platforms
		blocks.register(
				(state, world, pos, tintIndex) -> {
					if (world != null && pos != null) {
						BlockEntity tile = world.getBlockEntity(pos);
						if (tile instanceof PlatformBlockEntity camo) {
							BlockState camoState = camo.getCamoState();
							if (camoState != null) {
								return camoState.getBlock() instanceof PlatformBlock
										? 0xFFFFFF
										: Minecraft.getInstance().getBlockColors().getColor(camoState, world, pos, tintIndex);
							}
						}
					}
					return 0xFFFFFF;
				},
				BotaniaBlocks.abstrusePlatform, BotaniaBlocks.spectralPlatform, BotaniaBlocks.infrangiblePlatform);
	}

	public static void submitItems(ItemHandlerConsumer items) {
		items.register((stack, tintIndex) -> tintIndex == 0
				? FastColor.ARGB32.opaque(Mth.hsvToRgb(ClientTickHandler.getUiAnimationTicks() % 180 / 180, 0.25f, 1.0f))
				: -1,
				BotaniaItems.lifeEssence, BotaniaItems.gaiaIngot);

		items.register((stack, tintIndex) -> tintIndex == 0
				? FastColor.ARGB32.opaque(Mth.hsvToRgb(ClientTickHandler.getUiAnimationTicks() % 180 / 180, 0.4f, 0.9f))
				: -1,
				BotaniaBlocks.gaiaSpreader);

		items.register((stack, tintIndex) -> switch (tintIndex) {
			case 1 -> FastColor.ARGB32.opaque(MysticalPetalItem.getPetalLikeColor(WandOfTheForestItem.getColor1(stack)));
			case 2 -> FastColor.ARGB32.opaque(MysticalPetalItem.getPetalLikeColor(WandOfTheForestItem.getColor2(stack)));
			default -> -1;
		},
				BotaniaItems.twigWand, BotaniaItems.dreamwoodWand);

		items.register((stack, tintIndex) -> tintIndex == 0
				? Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex)
				: -1,
				getModBlocks(block -> block instanceof ManaPoolBlock pool && (pool.color != null || pool.isFabulous())));

		items.register((stack, tintIndex) -> {
			if (tintIndex == 1) {
				var manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
				return FastColor.ARGB32.opaque(Mth.hsvToRgb(MANA_HUE, manaItem != null ? (float) manaItem.getMana() / (float) Math.max(1, manaItem.getMaxMana()) : 0, 1));
			}
			return -1;
		}, BotaniaItems.manaMirror, BotaniaItems.manaTablet);

		items.register((stack, tintIndex) -> tintIndex == 0
				? FastColor.ARGB32.opaque(Mth.hsvToRgb(0.55F, ((float) stack.getMaxDamage() - (float) stack.getDamageValue()) / (float) stack.getMaxDamage() * 0.5F, 1F))
				: -1,
				BotaniaItems.spellCloth);

		items.register((stack, tintIndex) -> {
			if (tintIndex != 1) {
				return -1;
			}

			Brew brew = ((BrewItem) stack.getItem()).getBrew(stack);
			if (brew == BotaniaBrews.fallbackBrew) {
				return stack.getItem() instanceof TaintedBloodPendantItem ? 0xFFC6000E : 0xFF989898;
			}

			float speed = stack.is(BotaniaItems.brewFlask) || stack.is(BotaniaItems.brewVial) ? 0.1f : 0.2f;
			int add = (int) (Mth.sin(ClientTickHandler.getUiAnimationTicks() * speed) * 24);

			return addToColor(brew.getColor(stack), add);
		}, BotaniaItems.bloodPendant, BotaniaItems.incenseStick, BotaniaItems.brewFlask, BotaniaItems.brewVial);

		items.register((stack, tintIndex) -> {
			ItemStack lens = ManaBlasterItem.getLens(stack);
			if (!lens.isEmpty() && tintIndex == 0) {
				return ((MinecraftAccessor) Minecraft.getInstance()).getItemColors().getColor(lens, tintIndex);
			}

			if (tintIndex == 2) {
				BurstProperties props = ((ManaBlasterItem) stack.getItem()).getBurstProps(Minecraft.getInstance().player, stack, false, InteractionHand.MAIN_HAND);
				int add = FastColor.as8BitChannel(Mth.sin(ClientTickHandler.getUiAnimationTicks() / 5) * 0.15f);

				return addToColor(props.color, add);
			} else {
				return -1;
			}
		}, BotaniaItems.manaGun);

		items.register((stack, tintIndex) -> tintIndex == 1
				? FastColor.ARGB32.opaque(Mth.hsvToRgb(0.75f, 1,
						1.5f - Math.min(1, Mth.sin(ClientTickHandler.getUiAnimationTicks() / 2) * 0.5f + 1.2f)))
				: -1,
				BotaniaItems.enderDagger);

		items.register((stack, tintIndex) -> tintIndex == 1 && TerraShattererItem.isEnabled(stack)
				? FastColor.ARGB32.opaque(Mth.hsvToRgb(0.375f,
						Math.min(1, Mth.sin(ClientTickHandler.getUiAnimationTicks() / 4) * 0.5f + 1f),
						1))
				: -1,
				BotaniaItems.terraPick);

		ItemColor lensHandler = (stack, tintIndex) -> tintIndex == 0
				? FastColor.ARGB32.opaque(((LensItem) stack.getItem()).getLensColor(stack, Minecraft.getInstance().level))
				: -1;
		items.register(lensHandler, BotaniaItems.lensNormal, BotaniaItems.lensSpeed, BotaniaItems.lensPower, BotaniaItems.lensTime,
				BotaniaItems.lensEfficiency, BotaniaItems.lensBounce, BotaniaItems.lensGravity, BotaniaItems.lensMine,
				BotaniaItems.lensDamage, BotaniaItems.lensPhantom, BotaniaItems.lensMagnet, BotaniaItems.lensExplosive,
				BotaniaItems.lensInfluence, BotaniaItems.lensWeight, BotaniaItems.lensPaint, BotaniaItems.lensFire,
				BotaniaItems.lensPiston, BotaniaItems.lensLight, BotaniaItems.lensWarp, BotaniaItems.lensRedirect,
				BotaniaItems.lensFirework, BotaniaItems.lensFlare, BotaniaItems.lensMessenger, BotaniaItems.lensTripwire,
				BotaniaItems.lensStorm);
	}

	private static int addToColor(int color, int add) {
		int r = Mth.clamp(FastColor.ARGB32.red(color) + add, 0, 255);
		int g = Mth.clamp(FastColor.ARGB32.green(color) + add, 0, 255);
		int b = Mth.clamp(FastColor.ARGB32.blue(color) + add, 0, 255);

		return FastColor.ARGB32.color(r, g, b);
	}

	private ColorHandler() {}

}
