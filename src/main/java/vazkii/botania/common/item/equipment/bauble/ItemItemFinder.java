/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.EquipmentHandler;
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
		if (!(player instanceof PlayerEntity)) {
			return;
		}

		if (player.world.isRemote) {
			this.tickClient(stack, (PlayerEntity) player);
		} else {
			this.tickServer(stack, (PlayerEntity) player);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity living, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !living.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty();
		AccessoryRenderHelper.translateToHeadLevel(ms, living, partialTicks);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
		ms.translate(-0.4F, -1.4F, armor ? -0.3F : -0.25F);
		ms.scale(0.75F, 0.75F, 0.75F);

		IBakedModel model = MiscellaneousIcons.INSTANCE.itemFinderGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getEntitySolid());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
	}

	protected void tickClient(ItemStack stack, PlayerEntity player) {
		if (!Botania.proxy.isTheClientPlayer(player)) {
			return;
		}

		ListNBT blocks = ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, Constants.NBT.TAG_LONG, false);

		for (int i = 0; i < blocks.size(); i++) {
			BlockPos pos = BlockPos.fromLong(((LongNBT) blocks.get(i)).getLong());
			float m = 0.02F;
			WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), false);
			player.world.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for (int i : entities) {
			Entity e = player.world.getEntityByID(i);
			if (e != null && Math.random() < 0.6) {
				WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), Math.random() < 0.6);
				player.world.addParticle(data, e.getX() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.getY() + e.getHeight(), e.getZ() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0, 0.05F + 0.03F * (float) Math.random(), 0);
			}
		}
	}

	protected void tickServer(ItemStack stack, PlayerEntity player) {
		IntArrayList entPosBuilder = new IntArrayList();
		ListNBT blockPosBuilder = new ListNBT();

		scanForStack(player.getHeldItemMainhand(), player, entPosBuilder, blockPosBuilder);
		scanForStack(player.getHeldItemOffhand(), player, entPosBuilder, blockPosBuilder);

		int[] currentEnts = entPosBuilder.elements();

		ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, currentEnts);
		ItemNBTHelper.setList(stack, TAG_BLOCK_POSITIONS, blockPosBuilder);
	}

	private void scanForStack(ItemStack pstack, PlayerEntity player, IntArrayList entIdBuilder, ListNBT blockPosBuilder) {
		if (!pstack.isEmpty() || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
			for (Entity e : entities) {
				if (e == player) {
					continue;
				}
				if (e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() && !(e instanceof PlayerEntity)) {
					if (scanInventory(e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY), pstack)) {
						entIdBuilder.add(e.getEntityId());
					}

				} else if (e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					ItemStack istack = item.getItem();
					if (player.isSneaking() || istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack)) {
						entIdBuilder.add(item.getEntityId());
					}

				} else if (e instanceof IInventory) {
					IInventory inv = (IInventory) e;
					if (scanInventory(LazyOptional.of(() -> new InvWrapper(inv)), pstack)) {
						entIdBuilder.add(e.getEntityId());
					}

				} else if (e instanceof PlayerEntity) {
					PlayerEntity player_ = (PlayerEntity) e;
					LazyOptional<IItemHandler> playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
					LazyOptional<IItemHandlerModifiable> binv = EquipmentHandler.getAllWorn(player);
					if (scanInventory(playerInv, pstack) || scanInventory(binv, pstack)) {
						entIdBuilder.add(player_.getEntityId());
					}

				} else if (e instanceof VillagerEntity) {
					VillagerEntity villager = (VillagerEntity) e;
					for (MerchantOffer offer : villager.getOffers()) {
						if (equalStacks(pstack, offer.getBuyingStackFirst())
								|| equalStacks(pstack, offer.getBuyingStackSecond())
								|| equalStacks(pstack, offer.getSellingStack())) {
							entIdBuilder.add(villager.getEntityId());
						}
					}
				}
			}

			if (!pstack.isEmpty()) {
				range = 12;
				BlockPos pos = new BlockPos(player);
				for (BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))) {
					TileEntity tile = player.world.getTileEntity(pos_);
					if (tile != null) {
						boolean foundCap = false;
						for (Direction e : Direction.values()) {
							if (scanInventory(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e), pstack)) {
								blockPosBuilder.add(LongNBT.of(pos_.toLong()));
								foundCap = true;
								break;
							}
						}
						if (!foundCap && tile instanceof IInventory) {
							IInventory inv = (IInventory) tile;
							if (scanInventory(LazyOptional.of(() -> new InvWrapper(inv)), pstack)) {
								blockPosBuilder.add(LongNBT.of(pos_.toLong()));
							}
						}
					}
				}
			}
		}
	}

	private boolean equalStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	private boolean scanInventory(LazyOptional<? extends IItemHandler> optInv, ItemStack pstack) {
		return optInv.map(inv -> scanInventory(inv, pstack)).orElse(false);
	}

	private boolean scanInventory(IItemHandler inv, ItemStack pstack) {
		if (pstack.isEmpty()) {
			return false;
		}

		for (int l = 0; l < inv.getSlots(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			if (!istack.isEmpty() && equalStacks(istack, pstack)) {
				return true;
			}
		}
		return false;
	}

}
