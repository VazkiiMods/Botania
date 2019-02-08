package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockCraftyCrate extends BlockOpenCrate implements IWandHUD {

	public BlockCraftyCrate(Builder builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.CRATE_PATTERN, CratePattern.NONE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(BotaniaStateProps.CRATE_PATTERN);
	}

	@Nonnull
	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if(te instanceof TileCraftCrate) {
			TileCraftCrate tile = (TileCraftCrate) te;
			state = state.withProperty(BotaniaStateProps.CRATE_PATTERN, tile.pattern);
		}

		return state;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileCraftCrate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.craftCrate;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileCraftCrate) {
			TileCraftCrate craft = (TileCraftCrate) tile;

			int width = 52;
			int height = 52;
			int xc = mc.mainWindow.getScaledWidth() / 2 + 20;
			int yc = mc.mainWindow.getScaledHeight() / 2 - height / 2;

			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);

			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++) {
					int index = i * 3 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;

					boolean enabled = true;
					if(craft.pattern != CratePattern.NONE)
						enabled = craft.pattern.openSlots.get(index);

					Gui.drawRect(xp, yp, xp + 16, yp + 16, enabled ? 0x22FFFFFF : 0x22FF0000);

					ItemStack item = craft.getItemHandler().getStackInSlot(index);
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableRescaleNormal();
					mc.getItemRenderer().renderItemAndEffectIntoGUI(item, xp, yp);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
				}
		}
	}
}
