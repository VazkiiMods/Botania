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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;

public class ItemItemFinder extends ItemBauble {

	private static final String TAG_ENTITY_POSITIONS = "highlightPositionsEnt";
	private static final String TAG_BLOCK_POSITIONS = "highlightPositionsBlock";

	public ItemItemFinder(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (!(player instanceof Player)) {
			return;
		}

		if (player.level.isClientSide) {
			this.tickClient(stack, (Player) player);
		} else {
			this.tickServer(stack, (Player) player);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !living.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
		bipedModel.head.translateAndRotate(ms);
		ms.translate(-0.35, -0.2, armor ? 0.05 : 0.1);
		ms.scale(0.75F, -0.75F, -0.75F);

		BakedModel model = MiscellaneousIcons.INSTANCE.itemFinderGem;
		VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
		Minecraft.getInstance().getBlockRenderer().getModelRenderer()
				.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}

	protected void tickClient(ItemStack stack, Player player) {
		if (!Botania.proxy.isTheClientPlayer(player)) {
			return;
		}

		ListTag blocks = ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, 4, false);

		for (int i = 0; i < blocks.size(); i++) {
			BlockPos pos = BlockPos.of(((LongTag) blocks.get(i)).getAsLong());
			float m = 0.02F;
			WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), false);
			player.level.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for (int i : entities) {
			Entity e = player.level.getEntity(i);
			if (e != null && Math.random() < 0.6) {
				WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), Math.random() < 0.6);
				player.level.addParticle(data, e.getX() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.getY() + e.getBbHeight(), e.getZ() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0, 0.05F + 0.03F * (float) Math.random(), 0);
			}
		}
	}

	protected void tickServer(ItemStack stack, Player player) {
		IntArrayList entPosBuilder = new IntArrayList();
		ListTag blockPosBuilder = new ListTag();

		scanForStack(player.getMainHandItem(), player, entPosBuilder, blockPosBuilder);
		scanForStack(player.getOffhandItem(), player, entPosBuilder, blockPosBuilder);

		int[] currentEnts = entPosBuilder.elements();

		ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, currentEnts);
		ItemNBTHelper.setList(stack, TAG_BLOCK_POSITIONS, blockPosBuilder);
	}

	private void scanForStack(ItemStack pstack, Player player, IntArrayList entIdBuilder, ListTag blockPosBuilder) {
		if (!pstack.isEmpty() || player.isShiftKeyDown()) {
			int range = 24;

			List<Entity> entities = player.level.getEntitiesOfClass(Entity.class, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
			for (Entity e : entities) {
				if (e == player) {
					continue;
				}
				if (e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					ItemStack istack = item.getItem();
					if (player.isShiftKeyDown() || istack.sameItem(pstack) && ItemStack.tagMatches(istack, pstack)) {
						entIdBuilder.add(item.getId());
					}
				} else if (e instanceof Player) {
					Player targetPlayer = (Player) e;
					Container binv = BotaniaAPI.instance().getAccessoriesInventory(targetPlayer);
					if (scanInventory(targetPlayer.getInventory(), pstack) || scanInventory(binv, pstack)) {
						entIdBuilder.add(targetPlayer.getId());
					}

				} else if (e instanceof Merchant) {
					Merchant villager = (Merchant) e;
					for (MerchantOffer offer : villager.getOffers()) {
						if (equalStacks(pstack, offer.getBaseCostA())
								|| equalStacks(pstack, offer.getCostB())
								|| equalStacks(pstack, offer.getResult())) {
							entIdBuilder.add(e.getId());
						}
					}
				} else if (e instanceof Container) {
					Container inv = (Container) e;
					if (scanInventory(inv, pstack)) {
						entIdBuilder.add(e.getId());
					}
				}
			}

			if (!pstack.isEmpty()) {
				range = 12;
				BlockPos pos = player.blockPosition();
				for (BlockPos pos_ : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range + 1, range + 1, range + 1))) {
					BlockEntity tile = player.level.getBlockEntity(pos_);
					if (tile != null) {
						if (tile instanceof Container) {
							Container inv = (Container) tile;
							if (scanInventory(inv, pstack)) {
								blockPosBuilder.add(LongTag.valueOf(pos_.asLong()));
							}
						}
					}
				}
			}
		}
	}

	private boolean equalStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.sameItem(stack2) && ItemStack.tagMatches(stack1, stack2);
	}

	private boolean scanInventory(Container inv, ItemStack pstack) {
		if (pstack.isEmpty()) {
			return false;
		}

		for (int l = 0; l < inv.getContainerSize(); l++) {
			ItemStack istack = inv.getItem(l);
			// Some mods still set stuff to null apparently...
			if (istack != null && !istack.isEmpty() && equalStacks(istack, pstack)) {
				return true;
			}
		}
		return false;
	}

}
