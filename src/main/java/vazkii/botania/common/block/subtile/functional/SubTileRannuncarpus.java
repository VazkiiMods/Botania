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
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
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

		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		if(ticksExisted % 10 == 0) {
			IBlockState filter = getUnderlyingBlock();

			boolean scanned = false;
			List<BlockPos> validPositions = new ArrayList<>();

			int rangePlace = getRange();
			int rangePlaceY = getRangeY();

			BlockPos pos = supertile.getPos();

			List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE_Y, -RANGE), supertile.getPos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));
			int slowdown = getSlowdownFactor();
			for(EntityItem item : items) {
				if(item.age < 60 + slowdown || item.isDead || item.getItem().isEmpty())
					continue;

				ItemStack stack = item.getItem();
				Item stackItem = stack.getItem();
				if(stackItem instanceof ItemBlock || stackItem instanceof ItemBlockSpecial || stackItem instanceof ItemRedstone || stackItem instanceof IFlowerPlaceable) {
					if(!scanned) {
						for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-rangePlace, -rangePlaceY, -rangePlace), pos.add(rangePlace, rangePlaceY, rangePlace))) {
							IBlockState stateAbove = supertile.getWorld().getBlockState(pos_.up());
							Block blockAbove = stateAbove.getBlock();
							BlockPos up = pos_.up();
							if(filter == supertile.getWorld().getBlockState(pos_)
									&& (blockAbove.isAir(stateAbove, supertile.getWorld(), up)
											|| blockAbove.isReplaceable(supertile.getWorld(), up)))
								validPositions.add(up);
						}

						scanned = true;
					}


					if(!validPositions.isEmpty()) {
						BlockPos coords = validPositions.get(supertile.getWorld().rand.nextInt(validPositions.size()));

						IBlockState stateToPlace = null;
						if(stackItem instanceof IFlowerPlaceable)
							stateToPlace = ((IFlowerPlaceable) stackItem).getBlockToPlaceByFlower(stack, this, coords);
						if(stackItem instanceof ItemBlock) {
							int blockMeta = stackItem.getMetadata(stack.getItemDamage());

							if(stackItem instanceof ItemPiston) // Workaround because the blockMeta ItemPiston gives crashes getStateFromMeta
								blockMeta = 0;

							stateToPlace = ((ItemBlock) stackItem).getBlock().getStateFromMeta(blockMeta);
						}
						else if(stackItem instanceof ItemBlockSpecial)
							stateToPlace = ((ItemBlockSpecial) stackItem).getBlock().getDefaultState();
						else if(stackItem instanceof ItemRedstone)
							stateToPlace = Blocks.REDSTONE_WIRE.getDefaultState();

						if(stateToPlace != null) {
							if(stateToPlace.getBlock().canPlaceBlockAt(supertile.getWorld(), coords)) {
								supertile.getWorld().setBlockState(coords, stateToPlace, 1 | 2);
								if(ConfigHandler.blockBreakParticles)
									supertile.getWorld().playEvent(2001, coords, Block.getStateId(stateToPlace));
								validPositions.remove(coords);
								ItemBlock.setTileEntityNBT(supertile.getWorld(), null, coords, stack);

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

								stack.shrink(1);

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

	public IBlockState getUnderlyingBlock() {
		return supertile.getWorld().getBlockState(supertile.getPos().down(supertile instanceof  IFloatingFlower ? 1 : 2));
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		super.renderHUD(mc, res);

		IBlockState filter = getUnderlyingBlock();
		ItemStack recieverStack = new ItemStack(Item.getItemFromBlock(filter.getBlock()), 1, filter.getBlock().getMetaFromState(filter));
		int color = getColor();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(!recieverStack.isEmpty()) {
			String stackName = recieverStack.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 30;

			mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
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

}
