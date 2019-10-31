package vazkii.botania.common.block.decor.stairs;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lib.LibMisc;

public class BlockModStairs extends StairsBlock {

	public BlockModStairs(BlockState state, Block.Properties builder) {
		super(state, builder);
	}
}
