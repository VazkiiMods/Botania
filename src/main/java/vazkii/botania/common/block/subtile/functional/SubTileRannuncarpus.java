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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.MethodHandles;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.ArrayList;
import java.util.List;

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
			List<BlockPos> validPositions = new ArrayList<>();

			int rangePlace = getRange();
			int rangePlaceY = getRangeY();

			BlockPos pos = supertile.getPos();
			
			List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE_Y, -RANGE), supertile.getPos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();
			for(EntityItem item : items) {
				int age;
				try {
					age = (int) MethodHandles.itemAge_getter.invokeExact(item);
				} catch (Throwable t) {
					continue;
				}

				if(age < (60 + slowdown) || item.isDead)
					continue;

				ItemStack stack = item.getEntityItem();
				Item stackItem = stack.getItem();
				if(stackItem instanceof ItemBlock || stackItem instanceof ItemBlockSpecial || stackItem instanceof ItemRedstone || stackItem instanceof IFlowerPlaceable) {
					if(!scanned) {
						for(int i = -rangePlace; i < rangePlace + 1; i++)
							for(int j = -rangePlaceY; j < rangePlaceY + 1; j++)
								for(int l = -rangePlace; l < rangePlace + 1; l++) {
									BlockPos pos_ = pos.add(i, j, l);
									IBlockState stateAbove = supertile.getWorld().getBlockState(pos_.up());
									Block blockAbove = stateAbove.getBlock();

									if(filter.equals(supertile.getWorld(), pos_) && (blockAbove.isAir(stateAbove, supertile.getWorld(), pos_.up()) || blockAbove.isReplaceable(supertile.getWorld(), pos_.up())))
										validPositions.add(pos_.up());
								}

						scanned = true;
					}


					if(!validPositions.isEmpty() && !supertile.getWorld().isRemote) {
						BlockPos coords = validPositions.get(supertile.getWorld().rand.nextInt(validPositions.size()));

						Block blockToPlace = null;
						if(stackItem instanceof IFlowerPlaceable)
							blockToPlace = ((IFlowerPlaceable) stackItem).getBlockToPlaceByFlower(stack, this, coords);
						if(stackItem instanceof ItemBlock)
							blockToPlace = ((ItemBlock) stackItem).block;
						else if(stackItem instanceof ItemBlockSpecial)
							blockToPlace = ReflectionHelper.getPrivateValue(ItemBlockSpecial.class, (ItemBlockSpecial) stackItem, LibObfuscation.REED_ITEM);
						else if(stackItem instanceof ItemRedstone)
							blockToPlace = Blocks.redstone_wire;

						if(blockToPlace != null) {
							if(blockToPlace.canPlaceBlockAt(supertile.getWorld(), coords)) {
								supertile.getWorld().setBlockState(coords, blockToPlace.getStateFromMeta(stack.getItemDamage()), 1 | 2);
								if(ConfigHandler.blockBreakParticles)
									supertile.getWorld().playAuxSFX(2001, coords, Block.getStateId(blockToPlace.getStateFromMeta(stack.getItemDamage())));
								validPositions.remove(coords);

								TileEntity tile = supertile.getWorld().getTileEntity(coords);
								if(tile != null && tile instanceof ISubTileContainer) {
									ISubTileContainer container = (ISubTileContainer) tile;
									String subtileName = ItemBlockSpecialFlower.getType(stack);
									container.setSubTile(subtileName);
									SubTileEntity subtile = container.getSubTile();
									subtile.onBlockPlacedBy(supertile.getWorld(), coords, supertile.getWorld().getBlockState(coords), null, stack);
								}

								if(stackItem instanceof IFlowerPlaceable)
									((IFlowerPlaceable) stackItem).onBlockPlacedByFlower(stack, this, coords);

								if(!supertile.getWorld().isRemote) {
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
		return new BlockData(supertile.getWorld(), supertile.getPos().down(supertile instanceof IFloatingFlower ? 1 : 2));
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		BlockData filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(Item.getItemFromBlock(filter.state.getBlock()), 1, filter.state.getBlock().getMetaFromState(filter.state));
		int color = getColor();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(recieverStack != null && recieverStack.getItem() != null) {
			String stackName = recieverStack.getDisplayName();
			int width = 16 + mc.fontRendererObj.getStringWidth(stackName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 30;

			mc.fontRendererObj.drawStringWithShadow(stackName, x + 20, y + 5, color);
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(recieverStack, x, y);
			RenderHelper.disableStandardItemLighting();
		}

		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
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

		final IBlockState state;

		public BlockData(World world, BlockPos pos) {
			state = world.getBlockState(pos);
		}

		public boolean equals(BlockData data) {
			return this.state == data.state;
		}

		public boolean equals(World world, BlockPos pos) {
			return equals(new BlockData(world, pos));
		}

	}

}
