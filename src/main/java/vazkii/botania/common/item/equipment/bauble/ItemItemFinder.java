/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.village.TradeOffer;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;

public class ItemItemFinder extends ItemBauble {

	private static final String TAG_ENTITY_POSITIONS = "highlightPositionsEnt";
	private static final String TAG_BLOCK_POSITIONS = "highlightPositionsBlock";

	public ItemItemFinder(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (!(player instanceof PlayerEntity)) {
			return;
		}

		if (player.world.isClient) {
			this.tickClient(stack, (PlayerEntity) player);
		} else {
			this.tickServer(stack, (PlayerEntity) player);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity living, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !living.getEquippedStack(EquipmentSlot.HEAD).isEmpty();
		bipedModel.head.rotate(ms);
		ms.translate(-0.35, -0.2, armor ? 0.05 : 0.1);
		ms.scale(0.75F, -0.75F, -0.75F);

		BakedModel model = MiscellaneousIcons.INSTANCE.itemFinderGem;
		VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
	}

	protected void tickClient(ItemStack stack, PlayerEntity player) {
		if (!Botania.proxy.isTheClientPlayer(player)) {
			return;
		}

		ListTag blocks = ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, 4, false);

		for (int i = 0; i < blocks.size(); i++) {
			BlockPos pos = BlockPos.fromLong(((LongTag) blocks.get(i)).getLong());
			float m = 0.02F;
			WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), false);
			player.world.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for (int i : entities) {
			Entity e = player.world.getEntityById(i);
			if (e != null && Math.random() < 0.6) {
				WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), Math.random() < 0.6);
				player.world.addParticle(data, e.getX() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.getY() + e.getHeight(), e.getZ() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0, 0.05F + 0.03F * (float) Math.random(), 0);
			}
		}
	}

	protected void tickServer(ItemStack stack, PlayerEntity player) {
		IntArrayList entPosBuilder = new IntArrayList();
		ListTag blockPosBuilder = new ListTag();

		scanForStack(player.getMainHandStack(), player, entPosBuilder, blockPosBuilder);
		scanForStack(player.getOffHandStack(), player, entPosBuilder, blockPosBuilder);

		int[] currentEnts = entPosBuilder.elements();

		ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, currentEnts);
		ItemNBTHelper.setList(stack, TAG_BLOCK_POSITIONS, blockPosBuilder);
	}

	private void scanForStack(ItemStack pstack, PlayerEntity player, IntArrayList entIdBuilder, ListTag blockPosBuilder) {
		if (!pstack.isEmpty() || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.world.getNonSpectatingEntities(Entity.class, new Box(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
			for (Entity e : entities) {
				if (e == player) {
					continue;
				}
				if (e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					ItemStack istack = item.getStack();
					if (player.isSneaking() || istack.isItemEqualIgnoreDamage(pstack) && ItemStack.areTagsEqual(istack, pstack)) {
						entIdBuilder.add(item.getEntityId());
					}
				} else if (e instanceof PlayerEntity) {
					PlayerEntity targetPlayer = (PlayerEntity) e;
					Inventory binv = BotaniaAPI.instance().getAccessoriesInventory(targetPlayer);
					if (scanInventory(targetPlayer.inventory, pstack) || scanInventory(binv, pstack)) {
						entIdBuilder.add(targetPlayer.getEntityId());
					}

				} else if (e instanceof AbstractTraderEntity) {
					AbstractTraderEntity villager = (AbstractTraderEntity) e;
					for (TradeOffer offer : villager.getOffers()) {
						if (equalStacks(pstack, offer.getOriginalFirstBuyItem())
								|| equalStacks(pstack, offer.getSecondBuyItem())
								|| equalStacks(pstack, offer.getMutableSellItem())) {
							entIdBuilder.add(villager.getEntityId());
						}
					}
				} else if (e instanceof Inventory) {
					Inventory inv = (Inventory) e;
					if (scanInventory(inv, pstack)) {
						entIdBuilder.add(e.getEntityId());
					}
				}
			}

			if (!pstack.isEmpty()) {
				range = 12;
				BlockPos pos = player.getBlockPos();
				for (BlockPos pos_ : BlockPos.iterate(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))) {
					BlockEntity tile = player.world.getBlockEntity(pos_);
					if (tile != null) {
						if (tile instanceof Inventory) {
							Inventory inv = (Inventory) tile;
							if (scanInventory(inv, pstack)) {
								blockPosBuilder.add(LongTag.of(pos_.asLong()));
							}
						}
					}
				}
			}
		}
	}

	private boolean equalStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.isItemEqualIgnoreDamage(stack2) && ItemStack.areTagsEqual(stack1, stack2);
	}

	private boolean scanInventory(Inventory inv, ItemStack pstack) {
		if (pstack.isEmpty()) {
			return false;
		}

		for (int l = 0; l < inv.size(); l++) {
			ItemStack istack = inv.getStack(l);
			if (!istack.isEmpty() && equalStacks(istack, pstack)) {
				return true;
			}
		}
		return false;
	}

}
