package vazkii.botania.client;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemInfiniteFruit;
import vazkii.botania.common.item.rod.ItemTornadoRod;
import vazkii.botania.network.TriConsumer;

import java.util.Locale;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaItemProperties {
	public static void init(TriConsumer<ItemLike, ResourceLocation, ClampedItemPropertyFunction> consumer) {
		consumer.accept(ModItems.blackHoleTalisman, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, ItemBlackHoleTalisman.TAG_ACTIVE, false) ? 1 : 0);
		consumer.accept(ModItems.manaBottle, prefix("swigs_taken"),
				(stack, world, entity, seed) -> ItemBottledMana.SWIGS - ItemBottledMana.getSwigsLeft(stack));

		ResourceLocation vuvuzelaId = prefix("vuvuzela");
		ClampedItemPropertyFunction isVuvuzela = (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0;
		consumer.accept(ModItems.grassHorn, vuvuzelaId, isVuvuzela);
		consumer.accept(ModItems.leavesHorn, vuvuzelaId, isVuvuzela);
		consumer.accept(ModItems.snowHorn, vuvuzelaId, isVuvuzela);

		consumer.accept(ModItems.lexicon, prefix("elven"), (stack, world, living, seed) -> ItemLexicon.isElven(stack) ? 1 : 0);
		consumer.accept(ModItems.manaCookie, prefix("totalbiscuit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
		consumer.accept(ModItems.slimeBottle, prefix("active"),
				(stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getBoolean(ItemSlimeBottle.TAG_ACTIVE) ? 1.0F : 0.0F);
		consumer.accept(ModItems.spawnerMover, prefix("full"),
				(stack, world, entity, seed) -> ItemSpawnerMover.hasData(stack) ? 1 : 0);
		consumer.accept(ModItems.temperanceStone, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, ItemTemperanceStone.TAG_ACTIVE, false) ? 1 : 0);
		consumer.accept(ModItems.twigWand, prefix("bindmode"),
				(stack, world, entity, seed) -> ItemTwigWand.getBindMode(stack) ? 1 : 0);

		ResourceLocation poolFullId = prefix("full");
		ClampedItemPropertyFunction poolFull = (stack, world, entity, seed) -> {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			boolean renderFull = ((BlockPool) block).variant == BlockPool.Variant.CREATIVE || stack.hasTag() && stack.getTag().getBoolean("RenderFull");
			return renderFull ? 1F : 0F;
		};
		consumer.accept(ModBlocks.manaPool, poolFullId, poolFull);
		consumer.accept(ModBlocks.dilutedPool, poolFullId, poolFull);
		consumer.accept(ModBlocks.creativePool, poolFullId, poolFull);
		consumer.accept(ModBlocks.fabulousPool, poolFullId, poolFull);

		ClampedItemPropertyFunction brewGetter = (stack, world, entity, seed) -> {
			ItemBrewBase item = ((ItemBrewBase) stack.getItem());
			return item.getSwigs() - item.getSwigsLeft(stack);
		};
		consumer.accept(ModItems.brewVial, prefix("swigs_taken"), brewGetter);
		consumer.accept(ModItems.brewFlask, prefix("swigs_taken"), brewGetter);

		ResourceLocation holidayId = prefix("holiday");
		ClampedItemPropertyFunction holidayGetter = (stack, worldIn, entityIn, seed) -> ClientProxy.jingleTheBells ? 1 : 0;
		consumer.accept(ModItems.manaweaveHelm, holidayId, holidayGetter);
		consumer.accept(ModItems.manaweaveChest, holidayId, holidayGetter);
		consumer.accept(ModItems.manaweaveBoots, holidayId, holidayGetter);
		consumer.accept(ModItems.manaweaveLegs, holidayId, holidayGetter);

		ClampedItemPropertyFunction ringOnGetter = (stack, worldIn, entityIn, seed) -> ItemMagnetRing.getCooldown(stack) <= 0 ? 1 : 0;
		consumer.accept(ModItems.magnetRing, prefix("active"), ringOnGetter);
		consumer.accept(ModItems.magnetRingGreater, prefix("active"), ringOnGetter);

		consumer.accept(ModItems.elementiumShears, prefix("reddit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().equalsIgnoreCase("dammit reddit") ? 1F : 0F);
		consumer.accept(ModItems.manasteelSword, prefix("elucidator"),
				(stack, world, entity, seed) -> "the elucidator".equals(stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim()) ? 1 : 0);
		consumer.accept(ModItems.terraAxe, prefix("active"),
				(stack, world, entity, seed) -> entity instanceof Player && !ItemTerraAxe.shouldBreak((Player) entity) ? 0 : 1);
		consumer.accept(ModItems.terraPick, prefix("tipped"),
				(stack, world, entity, seed) -> ItemTerraPick.isTipped(stack) ? 1 : 0);
		consumer.accept(ModItems.terraPick, prefix("active"),
				(stack, world, entity, seed) -> ItemTerraPick.isEnabled(stack) ? 1 : 0);
		consumer.accept(ModItems.infiniteFruit, prefix("boot"),
				(stack, worldIn, entity, seed) -> ItemInfiniteFruit.isBoot(stack) ? 1F : 0F);
		consumer.accept(ModItems.tornadoRod, prefix("active"),
				(stack, world, living, seed) -> ItemTornadoRod.isFlying(stack) ? 1 : 0);

		ClampedItemPropertyFunction pulling = (ClampedItemPropertyFunction) ItemProperties.getProperty(Items.BOW, new ResourceLocation("pulling"));
		ClampedItemPropertyFunction pull = (stack, worldIn, entity, seed) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				ItemLivingwoodBow item = ((ItemLivingwoodBow) stack.getItem());
				return entity.getUseItem() != stack
						? 0.0F
						: (stack.getUseDuration() - entity.getUseItemRemainingTicks()) * item.chargeVelocityMultiplier() / 20.0F;
			}
		};
		consumer.accept(ModItems.livingwoodBow, new ResourceLocation("pulling"), pulling);
		consumer.accept(ModItems.livingwoodBow, new ResourceLocation("pull"), pull);
		consumer.accept(ModItems.crystalBow, new ResourceLocation("pulling"), pulling);
		consumer.accept(ModItems.crystalBow, new ResourceLocation("pull"), pull);
	}

	private BotaniaItemProperties() {}
}
