/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 2:04:15 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IRecipeKeyProvider;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.api.subtile.SubTileType;
import vazkii.botania.common.BotaniaRegistries;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemBlockSpecialFlower extends ItemBlockMod implements IRecipeKeyProvider {

	public ItemBlockSpecialFlower(Block block1, Properties props) {
		super(block1, props);
	}

	@Override
	public boolean placeBlock(BlockItemUseContext ctx,  @Nonnull IBlockState newState) {
		boolean placed = super.placeBlock(ctx, newState);
		if(placed) {
			World world = ctx.getWorld();
			BlockPos pos = ctx.getPos();
			ItemStack stack = ctx.getItem();

			SubTileType type = getType(ctx.getItem());
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileSpecialFlower) {
				TileSpecialFlower tile = (TileSpecialFlower) te;
				tile.setSubTile(type);
				tile.onBlockAdded(world, pos, newState);
				tile.onBlockPlacedBy(world, pos, newState, ctx.getPlayer(), stack);
				if(!world.isRemote)
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
			}
		}

		return placed;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return getType(stack).getTranslationKey(stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack par1ItemStack, World world, @Nonnull List<ITextComponent> stacks, @Nonnull ITooltipFlag flag) {
		SubTileType type = getType(par1ItemStack);
		type.addTooltip(par1ItemStack, world, stacks);

		if(ConfigHandler.CLIENT.referencesEnabled.get()) {
			ITextComponent lore = type.getLore(par1ItemStack);
			if(lore != null)
				stacks.add(lore.applyTextStyle(TextFormatting.ITALIC));
		}
	}

	@Override
	public String getCreatorModId(ItemStack itemStack) {
		return getType(itemStack).getRegistryName().getNamespace();
	}

	public static SubTileType getType(ItemStack stack) {
		ResourceLocation id = BotaniaAPI.DUMMY_SUBTILE_NAME;
		if(stack.hasTag()) {
			id = new ResourceLocation(ItemNBTHelper.getString(stack, SubTileEntity.TAG_TYPE, BotaniaAPI.DUMMY_SUBTILE_NAME.toString()));
		}
		return BotaniaRegistries.SUBTILES.getValue(id);
	}

	public static ItemStack ofType(ResourceLocation type) {
		return ofType(BotaniaRegistries.SUBTILES.getValue(type));
	}

	public static ItemStack ofType(SubTileType type) {
		return ofType(new ItemStack(ModBlocks.specialFlower), type);
	}

	public static ItemStack ofType(ItemStack stack, SubTileType type) {
		ItemNBTHelper.setString(stack, SubTileEntity.TAG_TYPE, type.getRegistryName().toString());
		return stack;
	}

	@Override
	public String getKey(ItemStack stack) {
		return "flower." + getType(stack);
	}

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent evt) {
		if(evt.getItem().getItem().getItem() == Item.getItemFromBlock(ModBlocks.specialFlower)) {
			SubTileType type = getType(evt.getItem().getItem());

			/* todo 1.13 use tags after flattening
			Class subtile = BotaniaAPI.getSubTileMapping(type);
			if(SubTileGenerating.class.isAssignableFrom(subtile)) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "main/generating_flower"), "code_triggered");
			}

			if(SubTileFunctional.class.isAssignableFrom(subtile)) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "main/functional_flower"), "code_triggered");
			}
			*/

			if(BotaniaAPI.DUMMY_SUBTILE_NAME.equals(type.getRegistryName())) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "challenge/null_flower"), "code_triggered");
			}
		}
	}

}
