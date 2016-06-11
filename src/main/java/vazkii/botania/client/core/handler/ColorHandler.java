package vazkii.botania.client.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.api.state.enums.SpreaderVariant;
import vazkii.botania.common.block.BlockCamo;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCamo;
import vazkii.botania.common.item.IColorable;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;

public final class ColorHandler {

    public static void init() {
        BlockColors blocks = Minecraft.getMinecraft().getBlockColors();
        Map<RegistryDelegate<Block>, IBlockColor> map = ReflectionHelper.getPrivateValue(BlockColors.class, blocks, "blockColorMap");

        // Steal vine colorer
        blocks.registerBlockColorHandler(map.get(Blocks.VINE.delegate), ModBlocks.solidVines);

        // 16 colors
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                        return state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
                    }
                },
                ModBlocks.specialFlower, ModBlocks.manaBeacon, ModBlocks.petalBlock, ModBlocks.unstableBlock);

        // Pool
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                        if (state.getValue(BotaniaStateProps.POOL_VARIANT) == PoolVariant.FABULOUS) {
                            float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
                            return MathHelper.hsvToRGB(time * 0.005F, 0.6F, 1F);
                        } else {
                            return state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
                        }
                    }
                },
                ModBlocks.pool
        );

        // Spreader
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                        if(state.getValue(BotaniaStateProps.SPREADER_VARIANT) != SpreaderVariant.GAIA)
                            return 0xFFFFFF;
                        float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
                        return MathHelper.hsvToRGB((time * 5) % 360 / 360F, 0.4F, 0.9F);
                    }
                },
                ModBlocks.spreader
        );

        // Platforms
        blocks.registerBlockColorHandler(
                new IBlockColor() {
                    @Override
                    public int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
                        if (world != null && pos != null) {
                            TileEntity tile = world.getTileEntity(pos);
                            if(tile instanceof TileCamo) {
                                TileCamo camo = (TileCamo) tile;
                                IBlockState camoState = camo.camoState;
                                if(camoState != null)
                                    return camoState.getBlock() instanceof BlockCamo ? 0xFFFFFF : Minecraft.getMinecraft().getBlockColors().colorMultiplier(camoState, world, pos, tintIndex);
                            }
                        }
                        return 0xFFFFFF;
                    }
                }, ModBlocks.platform);

        ItemColors items = Minecraft.getMinecraft().getItemColors();

        items.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(@Nonnull ItemStack stack, int tintIndex) {
                Item item = stack.getItem();
                if (item instanceof IColorable) {
                    return ((IColorable) item).getColorFromItemStack(stack, tintIndex);
                } else {
                    return 0xFFFFFF;
                }
            }
        }, ModItems.manaResource, ModItems.twigWand, ModItems.dye, ModItems.petal, ModItems.manaGun, ModItems.manaMirror, ModItems.manaTablet, ModItems.signalFlare,
                ModItems.spellCloth, ModItems.brewFlask, ModItems.brewVial, ModItems.incenseStick, ModItems.bloodPendant, ModItems.enderDagger,
                ModItems.terraPick, ModItems.lens, Item.getItemFromBlock(ModBlocks.manaBeacon), Item.getItemFromBlock(ModBlocks.petalBlock),
                Item.getItemFromBlock(ModBlocks.unstableBlock), Item.getItemFromBlock(ModBlocks.pool), Item.getItemFromBlock(ModBlocks.spreader));
    }

    private ColorHandler() {}

}
