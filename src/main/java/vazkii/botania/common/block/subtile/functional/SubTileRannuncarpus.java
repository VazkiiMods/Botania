/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 1, 2014, 6:08:25 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SubTileRannuncarpus extends SubTileFunctional {

	private static final int RANGE = 2;
	private static final int RANGE_Y = 3;
	private static final int RANGE_PLACE_MANA = 8;
	private static final int RANGE_PLACE = 6;
	private static final int RANGE_PLACE_Y = 6;

	private static final int RANGE_PLACE_MANA_MINI = 3;
	private static final int RANGE_PLACE_MINI = 2;
	private static final int RANGE_PLACE_Y_MINI = 2;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0)
			return;

		if(ticksExisted % 10 == 0) {
			BlockData filter = getUnderlyingBlock();

			boolean scanned = false;
			List<ChunkCoordinates> validPositions = new ArrayList();

			int rangePlace = getRange();
			int rangePlaceY = getRangeY();

			int x = supertile.xCoord;
			int y = supertile.yCoord;
			int z = supertile.zCoord;

			List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - RANGE, y - RANGE_Y, z - RANGE, x + RANGE + 1, y + RANGE_Y, z + RANGE + 1));
			int slowdown = getSlowdownFactor();
			for(EntityItem item : items) {
				if(item.age < (60 + slowdown) || item.isDead)
					continue;

				ItemStack stack = item.getEntityItem();
				Item stackItem = stack.getItem();
				if(stackItem instanceof ItemBlock || stackItem instanceof ItemReed || stackItem instanceof ItemRedstone || stackItem instanceof IFlowerPlaceable) {
					if(!scanned) {
						for(int i = -rangePlace; i < rangePlace + 1; i++)
							for(int j = -rangePlaceY; j < rangePlaceY + 1; j++)
								for(int l = -rangePlace; l < rangePlace + 1; l++) {
									int xp = x + i;
									int yp = y + j;
									int zp = z + l;
									Block blockAbove = supertile.getWorldObj().getBlock(xp, yp + 1, zp);

									if(filter.equals(supertile.getWorldObj(), xp, yp, zp) && (blockAbove.isAir(supertile.getWorldObj(), xp, yp + 1, zp) || blockAbove.isReplaceable(supertile.getWorldObj(), xp, yp + 1, zp)))
										validPositions.add(new ChunkCoordinates(xp, yp + 1, zp));
								}

						scanned = true;
					}


					if(!validPositions.isEmpty() && !supertile.getWorldObj().isRemote) {
						ChunkCoordinates coords = validPositions.get(supertile.getWorldObj().rand.nextInt(validPositions.size()));

						Block blockToPlace = null;
						if(stackItem instanceof IFlowerPlaceable)
							blockToPlace = ((IFlowerPlaceable) stackItem).getBlockToPlaceByFlower(stack, this, coords.posX, coords.posY, coords.posZ);
						if(stackItem instanceof ItemBlock)
							blockToPlace = ((ItemBlock) stackItem).field_150939_a;
						else if(stackItem instanceof ItemReed)
							blockToPlace = ReflectionHelper.getPrivateValue(ItemReed.class, (ItemReed) stackItem, LibObfuscation.REED_ITEM);
						else if(stackItem instanceof ItemRedstone)
							blockToPlace = Blocks.redstone_wire;

						if(blockToPlace != null) {
							if(blockToPlace.canPlaceBlockAt(supertile.getWorldObj(), coords.posX, coords.posY, coords.posZ)) {
								supertile.getWorldObj().setBlock(coords.posX, coords.posY, coords.posZ, blockToPlace, stack.getItemDamage(), 1 | 2);
								if(ConfigHandler.blockBreakParticles)
									supertile.getWorldObj().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(blockToPlace) + (stack.getItemDamage() << 12));
								validPositions.remove(coords);

								TileEntity tile = supertile.getWorldObj().getTileEntity(coords.posX, coords.posY, coords.posZ);
								if(tile != null && tile instanceof ISubTileContainer) {
									ISubTileContainer container = (ISubTileContainer) tile;
									String subtileName = ItemBlockSpecialFlower.getType(stack);
									container.setSubTile(subtileName);
									SubTileEntity subtile = container.getSubTile();
									subtile.onBlockPlacedBy(supertile.getWorldObj(), coords.posX, coords.posY, coords.posZ, null, stack);
								}

								if(stackItem instanceof IFlowerPlaceable)
									((IFlowerPlaceable) stackItem).onBlockPlacedByFlower(stack, this, coords.posX, coords.posY, coords.posZ);

								if(!supertile.getWorldObj().isRemote) {
									stack.stackSize--;
									if(stack.stackSize == 0)
										item.setDead();
								}

								if(mana > 1)
									mana--;
								return;
							}
						}
					}
				}
			}
		}
	}

	public BlockData getUnderlyingBlock() {
		return new BlockData(supertile.getWorldObj(), supertile.xCoord, supertile.yCoord - (supertile instanceof IFloatingFlower ? 1 : 2), supertile.zCoord);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		BlockData filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(Item.getItemFromBlock(filter.block), 1, filter.meta);
		int color = getColor();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(recieverStack != null && recieverStack.getItem() != null) {
			String stackName = recieverStack.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 30;

			mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
			RenderHelper.enableGUIStandardItemLighting();
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, recieverStack, x, y);
			RenderHelper.disableStandardItemLighting();
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), getRange());
	}

	public int getRange() {
		return mana > 0 ? RANGE_PLACE_MANA : RANGE_PLACE;
	}

	public int getRangeY() {
		return RANGE_PLACE_Y;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0xFFB27F;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.rannuncarpus;
	}

	public static class Mini extends SubTileRannuncarpus {
		@Override public int getRange() { return mana > 0 ? RANGE_PLACE_MANA_MINI : RANGE_PLACE_MINI; }
		@Override public int getRangeY() { return RANGE_PLACE_Y_MINI; }
	}

	static class BlockData {

		Block block;
		int meta;

		public BlockData(World world, int x, int y, int z) {
			block = world.getBlock(x, y, z);
			meta = world.getBlockMetadata(x, y, z);
		}

		public boolean equals(BlockData data) {
			return block == data.block && meta == data.meta;
		}

		public boolean equals(World world, int x, int y, int z) {
			return equals(new BlockData(world, x, y, z));
		}

	}

}
