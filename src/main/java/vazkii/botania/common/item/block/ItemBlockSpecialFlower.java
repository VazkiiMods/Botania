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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.IRecipeKeyProvider;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.api.subtile.signature.SubTileSignature;
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

	public ItemBlockSpecialFlower(Block block1) {
		super(block1);
		setHasSubtypes(true);
	}

	@Override
	public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
		boolean placed = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if(placed) {
			String type = getType(stack);
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileSpecialFlower) {
				TileSpecialFlower tile = (TileSpecialFlower) te;
				tile.setSubTile(type);
				tile.onBlockAdded(world, pos, newState);
				tile.onBlockPlacedBy(world, pos, newState, player, stack);
				if(!world.isRemote)
					world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
			}
		}

		return placed;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return BotaniaAPI.getSignatureForName(getType(stack)).getUnlocalizedNameForStack(stack);
	}

	@Nonnull
	@Override
	public String getUnlocalizedNameInefficiently(@Nonnull ItemStack par1ItemStack) {
		return getUnlocalizedNameInefficiently_(par1ItemStack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack par1ItemStack, World world, @Nonnull List<String> stacks, @Nonnull ITooltipFlag flag) {
		String type = getType(par1ItemStack);
		SubTileSignature sig = BotaniaAPI.getSignatureForName(type);

		sig.addTooltip(par1ItemStack, world, stacks);

		if(ConfigHandler.referencesEnabled) {
			String refUnlocalized = sig.getUnlocalizedLoreTextForStack(par1ItemStack);
			String refLocalized = I18n.format(refUnlocalized);
			if(!refLocalized.equals(refUnlocalized))
				stacks.add(TextFormatting.ITALIC + refLocalized);
		}
	}

	@Override
	public String getCreatorModId(ItemStack itemStack) {
		String mod = BotaniaAPI.subTileMods.get(getType(itemStack));
		if(mod != null)
			return mod;
		return LibMisc.MOD_ID;
	}

	@Nonnull
	public static String getType(ItemStack stack) {
		return stack.hasTagCompound() ? ItemNBTHelper.getString(stack, SubTileEntity.TAG_TYPE, "") : "";
	}

	public static ItemStack ofType(String type) {
		return ofType(new ItemStack(ModBlocks.specialFlower), type);
	}

	public static ItemStack ofType(ItemStack stack, String type) {
		ItemNBTHelper.setString(stack, SubTileEntity.TAG_TYPE, type);
		return stack;
	}

	@Override
	public String getKey(ItemStack stack) {
		return "flower." + getType(stack);
	}

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent evt) {
		if(evt.getItem().getItem().getItem() == Item.getItemFromBlock(ModBlocks.specialFlower)) {
			String type = getType(evt.getItem().getItem());
			Class subtile = BotaniaAPI.getSubTileMapping(type);

			if(SubTileGenerating.class.isAssignableFrom(subtile)) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "main/generating_flower"), "code_triggered");
			}

			if(SubTileFunctional.class.isAssignableFrom(subtile)) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "main/functional_flower"), "code_triggered");
			}

			if("".equals(type)) {
				PlayerHelper.grantCriterion((EntityPlayerMP) evt.getEntityPlayer(), new ResourceLocation(LibMisc.MOD_ID, "challenge/null_flower"), "code_triggered");
			}
		}
	}

}
