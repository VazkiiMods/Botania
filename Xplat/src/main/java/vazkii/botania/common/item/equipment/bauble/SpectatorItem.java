/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.mixin.AbstractHorseAccessor;
import vazkii.botania.mixin.RandomizableContainerBlockEntityAccessor;

import java.util.List;

public class SpectatorItem extends BaubleItem {
	private static final int[] EMPTY_ENTITIES_ARRAY = new int[0];
	private static final long[] EMPTY_BLOCKPOS_ARRAY = new long[0];
	public static final String TAG_ENTITY_POSITIONS = "highlightPositionsEnt";
	public static final String TAG_BLOCK_POSITIONS = "highlightPositionsBlock";
	public static final int RANGE_ENTITIES = 24;
	public static final int RANGE_BLOCKS = 12;
	public static final int SCAN_INTERVAL_TICKS = 4;

	public SpectatorItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (!(living instanceof Player player)) {
			return;
		}

		if (living.level().isClientSide) {
			this.showScanResults(stack, player);
		} else if (living.tickCount % SCAN_INTERVAL_TICKS == 0) {
			this.scanForItems(stack, player);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity entity) {
		ItemNBTHelper.removeEntry(stack, TAG_BLOCK_POSITIONS);
		ItemNBTHelper.removeEntry(stack, TAG_ENTITY_POSITIONS);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean armor = !living.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
			bipedModel.head.translateAndRotate(ms);
			ms.translate(-0.35, -0.2, armor ? 0.05 : 0.1);
			ms.scale(0.75F, -0.75F, -0.75F);

			BakedModel model = MiscellaneousModels.INSTANCE.itemFinderGem;
			VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer()
					.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
		}
	}

	protected void showScanResults(ItemStack stack, Player player) {
		if (player != Proxy.INSTANCE.getClientPlayer()) {
			return;
		}

		// backward compatibility: this was a list tag before
		var blockPosLongs = ItemNBTHelper.verifyType(stack, TAG_BLOCK_POSITIONS, LongArrayTag.class)
				? ItemNBTHelper.getLongArray(stack, TAG_BLOCK_POSITIONS)
				: EMPTY_BLOCKPOS_ARRAY;

		for (var blockPosLong : blockPosLongs) {
			BlockPos pos = BlockPos.of(blockPosLong);
			float m = 0.02F;
			WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), false);
			player.level().addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for (int i : entities) {
			Entity e = player.level().getEntity(i);
			if (e != null && e.isAlive() && Math.random() < 0.6) {
				WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), Math.random() < 0.6);
				player.level().addParticle(data, e.getX() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.getY() + e.getBbHeight(), e.getZ() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0, 0.05F + 0.03F * (float) Math.random(), 0);
			}
		}
	}

	public void scanForItems(ItemStack stack, Player player) {
		ItemStack mainHandStack = player.getMainHandItem();
		ItemStack offHandStack = player.getOffhandItem();

		int[] entityIds = scanEntities(player, mainHandStack, offHandStack);
		ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, entityIds);

		long[] blockPositionLongs = scanBlockContainers(player, mainHandStack, offHandStack);
		ItemNBTHelper.setLongArray(stack, TAG_BLOCK_POSITIONS, blockPositionLongs);
	}

	private int[] scanEntities(Player player, ItemStack mainHandStack, ItemStack offHandStack) {
		boolean emptyHands = mainHandStack.isEmpty() && offHandStack.isEmpty();
		if (emptyHands && !player.isShiftKeyDown()) {
			return EMPTY_ENTITIES_ARRAY;
		}
		var entityIds = new IntArrayList();
		List<Entity> entities = player.level().getEntitiesOfClass(Entity.class, new AABB(player.blockPosition()).inflate(RANGE_ENTITIES));
		for (Entity e : entities) {
			if (e == player) {
				continue;
			}
			if (e instanceof ItemEntity item) {
				ItemStack entityStack = item.getItem();
				if (player.isShiftKeyDown() || equalStacks(entityStack, mainHandStack, offHandStack)) {
					entityIds.add(item.getId());
				}
			} else if (emptyHands) {
				continue;
			}
			if (e instanceof Player targetPlayer) {
				if (scanInventory(targetPlayer.getInventory(), mainHandStack, offHandStack)) {
					entityIds.add(targetPlayer.getId());
				} else {
					Container baubleInventory = BotaniaAPI.instance().getAccessoriesInventory(targetPlayer);
					if (scanInventory(baubleInventory, mainHandStack, offHandStack)) {
						entityIds.add(targetPlayer.getId());
					}
				}
			} else if (e instanceof AbstractChestedHorse horse && horse.hasChest()) {
				if (scanInventory(((AbstractHorseAccessor) horse).getInventory(), mainHandStack, offHandStack)) {
					entityIds.add(horse.getId());
				}
			} else if (e instanceof Allay allay && allay.hasItemInHand()) {
				if (equalStacks(allay.getMainHandItem(), mainHandStack, offHandStack)) {
					entityIds.add(allay.getId());
				}
			} else if (e instanceof Merchant villager) {
				for (MerchantOffer offer : villager.getOffers()) {
					if (equalStacks(offer.getBaseCostA(), mainHandStack, offHandStack)
							|| equalStacks(offer.getCostB(), mainHandStack, offHandStack)
							|| equalStacks(offer.getResult(), mainHandStack, offHandStack)) {
						entityIds.add(e.getId());
					}
				}
			} else if (e instanceof Container inv && (!(inv instanceof AbstractMinecartContainer minecart)
					|| minecart.getLootTable() == null)) {
				if (scanInventory(inv, mainHandStack, offHandStack)) {
					entityIds.add(e.getId());
				}
			}
		}
		entityIds.trim();
		return entityIds.elements();
	}

	private long[] scanBlockContainers(Player player, ItemStack mainHandStack, ItemStack offHandStack) {
		if (mainHandStack.isEmpty() && offHandStack.isEmpty()) {
			return EMPTY_BLOCKPOS_ARRAY;
		}
		var blockPositions = new LongArrayList();
		BlockPos.betweenClosedStream(new AABB(player.blockPosition()).inflate(RANGE_BLOCKS))
				.filter(pos -> scanBlock(player, pos, mainHandStack, offHandStack))
				.forEach(pos -> blockPositions.add(pos.asLong()));
		blockPositions.trim();
		return blockPositions.elements();
	}

	private boolean scanBlock(Player player, BlockPos pos, ItemStack mainHandStack, ItemStack offHandStack) {
		BlockEntity blockEntity = player.level().getBlockEntity(pos);
		return blockEntity instanceof Container inv && (!(blockEntity instanceof RandomizableContainerBlockEntity lootInv)
				|| ((RandomizableContainerBlockEntityAccessor) lootInv).getLootTable() == null)
				&& scanInventory(inv, mainHandStack, offHandStack);
	}

	private boolean equalStacks(ItemStack testStack, ItemStack referenceStack1, ItemStack referenceStack2) {
		return !testStack.isEmpty() && (ItemStack.isSameItemSameTags(testStack, referenceStack1)
				|| ItemStack.isSameItemSameTags(testStack, referenceStack2));
	}

	private boolean scanInventory(Container inv, ItemStack mainHandStack, ItemStack offHandStack) {
		for (int l = 0; l < inv.getContainerSize(); l++) {
			ItemStack inventoryStack = inv.getItem(l);
			// Some mods still set stuff to null apparently...
			if (inventoryStack != null && equalStacks(inventoryStack, mainHandStack, offHandStack)) {
				return true;
			}
		}
		return false;
	}

}
