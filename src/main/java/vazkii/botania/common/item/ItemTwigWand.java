/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 20, 2014, 7:42:46 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class ItemTwigWand extends Item16Colors implements ICoordBoundItem {

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_BOUND_TILE_X = "boundTileX";
	private static final String TAG_BOUND_TILE_Y = "boundTileY";
	private static final String TAG_BOUND_TILE_Z = "boundTileZ";
	private static final String TAG_BIND_MODE = "bindMode";
	private static final BlockPos UNBOUND_POS = new BlockPos(0, -1, 0);

	public ItemTwigWand() {
		super(LibItemNames.TWIG_WAND);
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("botania", "bindmode"), (stack, worldIn, entityIn) -> getBindMode(stack) ? 1 : 0);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);
		Block block = world.getBlockState(pos).getBlock();
		BlockPos boundPos = getBoundTile(stack);
		TileEntity boundTile = world.getTileEntity(boundPos);

		if(player.isSneaking()) {
			// Try to complete a binding
			if(boundPos.getY() != -1 && !pos.equals(boundPos)) {
				if (boundTile instanceof IWandBindable) {
					if(((IWandBindable) boundTile).bindTo(player, stack, pos, side)) {
						Vector3 orig = new Vector3(boundPos.getX() + 0.5, boundPos.getY() + 0.5, boundPos.getZ() + 0.5);
						Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
						doParticleBeam(world, orig, end);

						VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, boundPos);
						setBoundTile(stack, UNBOUND_POS);
					}

					return EnumActionResult.SUCCESS;
				} else {
					setBoundTile(stack, UNBOUND_POS);
				}
			}

			if(player.canPlayerEdit(pos, side, stack)
					&& (!(block instanceof BlockCommandBlock) || player.canUseCommandBlock())
					&& block.rotateBlock(world, pos, side)) {
				player.swingArm(hand);
				return EnumActionResult.SUCCESS;
			}
		}

		if(block == Blocks.LAPIS_BLOCK && ConfigHandler.enchanterEnabled) {
			EnumFacing.Axis axis = null;
			if(TileEnchanter.canEnchanterExist(world, pos, EnumFacing.Axis.X))
				axis = EnumFacing.Axis.X;
			else if(TileEnchanter.canEnchanterExist(world, pos, EnumFacing.Axis.Z))
				axis = EnumFacing.Axis.Z;

			if(axis != null) {
				if(!world.isRemote) {
					world.setBlockState(pos, ModBlocks.enchanter.getDefaultState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, axis), 1 | 2);
					world.playSound(null, pos, ModSounds.enchanterForm, SoundCategory.BLOCKS, 0.5F, 0.6F);
					PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/enchanter_make"), "code_triggered");
				} else {
					for(int i = 0; i < 50; i++) {
						float red = (float) Math.random();
						float green = (float) Math.random();
						float blue = (float) Math.random();

						double x = (Math.random() - 0.5) * 6;
						double y = (Math.random() - 0.5) * 6;
						double z = (Math.random() - 0.5) * 6;

						float velMul = 0.07F;

						Botania.proxy.wispFX(pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
					}
				}

				return EnumActionResult.SUCCESS;
			}
		}

		if(block instanceof IWandable) {
			TileEntity tile = world.getTileEntity(pos);
			boolean bindable = tile instanceof IWandBindable;

			boolean wanded;
			if(getBindMode(stack) && bindable && player.isSneaking() && ((IWandBindable) tile).canSelect(player, stack, pos, side)) {
				if(boundPos.equals(pos))
					setBoundTile(stack, UNBOUND_POS);
				else setBoundTile(stack, pos);

				if(world.isRemote) {
					player.swingArm(hand);
					player.playSound(ModSounds.ding, 0.11F, 1F);
				}

				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(player, stack, world, pos, side);
				if(wanded && world.isRemote)
					player.swingArm(hand);
			}

			return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}

		if(!world.isRemote && ((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.containsKey(player.getUniqueID())) {
			BlockPistonRelay.DimWithPos bindPos = ((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.get(player.getUniqueID());
			BlockPistonRelay.DimWithPos currentPos = new BlockPistonRelay.DimWithPos(world.provider.getDimension(), pos);

			((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.remove(player.getUniqueID());
			((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.put(bindPos, currentPos);
			BlockPistonRelay.WorldData.get(world).markDirty();

			PacketHandler.sendToNearby(world, pos,
					new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
							bindPos.blockPos.getX() + 0.5, bindPos.blockPos.getY() + 0.5, bindPos.blockPos.getZ() + 0.5,
							pos.getX(), pos.getY(), pos.getZ()));
			world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	public static void doParticleBeam(World world, Vector3 orig, Vector3 end) {
		if(!world.isRemote)
			return;

		Vector3 diff = end.subtract(orig);
		Vector3 movement = diff.normalize().multiply(0.05);
		int iters = (int) (diff.mag() / movement.mag());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vector3 currentPos = orig;
		for(int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			Color color = Color.getHSBColor(hue, 1F, 1F);
			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			Botania.proxy.setSparkleFXNoClip(true);
			Botania.proxy.sparkleFX(currentPos.x, currentPos.y, currentPos.z, r, g, b, 0.5F, 4);
			Botania.proxy.setSparkleFXNoClip(false);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
		BlockPos coords = getBoundTile(par1ItemStack);
		TileEntity tile = world.getTileEntity(coords);
		if(tile == null || !(tile instanceof IWandBindable))
			setBoundTile(par1ItemStack, UNBOUND_POS);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking()) {
			if(!world.isRemote)
				setBindMode(stack, !getBindMode(stack));
			else player.playSound(ModSounds.ding, 0.1F, 1F);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < 16; i++)
				stacks.add(forColors(i, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return getUnlocalizedNameLazy(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		list.add(I18n.format(getModeString(stack)));
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	public static ItemStack forColors(int color1, int color2) {
		ItemStack stack = new ItemStack(ModItems.twigWand);
		ItemNBTHelper.setInt(stack, TAG_COLOR1, color1);
		ItemNBTHelper.setInt(stack, TAG_COLOR2, color2);

		return stack;
	}

	public static int getColor1(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0);
	}

	public static int getColor2(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0);
	}

	public static void setBoundTile(ItemStack stack, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_X, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Y, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Z, pos.getZ());
	}

	public static BlockPos getBoundTile(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
		return new BlockPos(x, y, z);
	}

	public static boolean getBindMode(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true);
	}

	public static void setBindMode(ItemStack stack, boolean bindMode) {
		ItemNBTHelper.setBoolean(stack, TAG_BIND_MODE, bindMode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.wandMode." + (getBindMode(stack) ? "bind" : "function");
	}

	@Override
	public BlockPos getBinding(ItemStack stack) {
		BlockPos bound = getBoundTile(stack);
		if(bound.getY() != -1)
			return bound;

		RayTraceResult pos = Minecraft.getMinecraft().objectMouseOver;
		if(pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos.getBlockPos());
			if(tile != null && tile instanceof ITileBound) {
				BlockPos coords = ((ITileBound) tile).getBinding();
				return coords;
			}
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
