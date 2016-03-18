package vazkii.botania.client.core.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockCamo;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCamo;
import vazkii.botania.common.item.IColorable;
import vazkii.botania.common.item.ModItems;

public final class ColorHandler {

    public static void init() {
        BlockColors blocks = Minecraft.getMinecraft().getBlockColors();

        // 16 colors
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex) {
                        return state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
                    }
                },
                ModBlocks.specialFlower, ModBlocks.manaBeacon, ModBlocks.petalBlock, ModBlocks.unstableBlock);

        // Platforms
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                        TileEntity tile = world.getTileEntity(pos);
                        if(tile instanceof TileCamo) {
                            TileCamo camo = (TileCamo) tile;
                            IBlockState camoState = camo.camoState;
                            if(camoState != null)
                                return camoState.getBlock() instanceof BlockCamo ? 0xFFFFFF : Minecraft.getMinecraft().getBlockColors().colorMultiplier(camoState, world, pos, tintIndex);
                        }
                        return 0xFFFFFF;
                    }
                }, ModBlocks.platform);

        ItemColors items = Minecraft.getMinecraft().getItemColors();

        items.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                Item item = stack.getItem();
                if (item instanceof IColorable) {
                    return ((IColorable) item).getColorFromItemStack(stack, tintIndex);
                } else {
                    return 0xFFFFFF;
                }
            }
        }, ModItems.dye, ModItems.petal, ModItems.manaGun, ModItems.manaMirror, ModItems.manaTablet, ModItems.signalFlare,
                ModItems.spellCloth, ModItems.brewFlask, ModItems.brewVial, ModItems.incenseStick, ModItems.bloodPendant, ModItems.enderDagger,
                ModItems.terraPick, ModItems.lens, Item.getItemFromBlock(ModBlocks.manaBeacon), Item.getItemFromBlock(ModBlocks.petalBlock), Item.getItemFromBlock(ModBlocks.unstableBlock));
    }

    private ColorHandler() {}

}
