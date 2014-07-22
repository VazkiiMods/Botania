/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 31, 2014, 3:02:58 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemLens extends ItemMod implements ILens {

	private static final int NORMAL = 0,
			SPEED = 1,
			POWER = 2,
			TIME = 3,
			EFFICIENCY = 4,
			BOUNCE = 5,
			GRAVITY = 6,
			MINE = 7,
			DAMAGE = 8,
			PHANTOM = 9,
			MAGNET = 10,
			EXPLOSIVE = 11,
			INFLUENCE = 12,
			WEIGHT = 13,
			PAINT = 14,
			FIRE = 15,
			PISTON = 16;

	private static final int PROP_NONE = 0,
			PROP_POWER = 1,
			PROP_ORIENTATION = 1 << 1,
			PROP_TOUCH = 1 << 2,
			PROP_INTERACTION = 1 << 3,
			PROP_DAMAGE = 1 << 4;

	private static final Map<Integer, Integer> props = new HashMap();

	static {
		setProps(NORMAL, PROP_NONE);
		setProps(SPEED, PROP_NONE);
		setProps(POWER, PROP_POWER);
		setProps(TIME, PROP_NONE);
		setProps(EFFICIENCY, PROP_NONE);
		setProps(BOUNCE, PROP_TOUCH);
		setProps(GRAVITY, PROP_ORIENTATION);
		setProps(MINE, PROP_TOUCH | PROP_INTERACTION);
		setProps(DAMAGE, PROP_DAMAGE);
		setProps(PHANTOM, PROP_TOUCH);
		setProps(MAGNET, PROP_ORIENTATION);
		setProps(EXPLOSIVE, PROP_DAMAGE | PROP_TOUCH | PROP_INTERACTION);
		setProps(INFLUENCE, PROP_NONE);
		setProps(WEIGHT, PROP_TOUCH | PROP_INTERACTION);
		setProps(PAINT, PROP_TOUCH | PROP_INTERACTION);
		setProps(FIRE, PROP_DAMAGE | PROP_TOUCH | PROP_INTERACTION);
		setProps(PISTON, PROP_TOUCH | PROP_INTERACTION);
	}

	static final List<Block> paintableBlocks = new ArrayList() {{
		add(Blocks.stained_glass);
		add(Blocks.stained_glass_pane);
		add(Blocks.stained_hardened_clay);
		add(Blocks.wool);
		add(Blocks.carpet);
		add(ModBlocks.unstableBlock);
		add(ModBlocks.manaBeacon);
	}
	};

	private static final String TAG_COLOR = "color";
	private static final String TAG_COMPOSITE_LENS = "compositeLens";

	public static IIcon iconGlass;

	public static final int SUBTYPES = 17;
	IIcon[] ringIcons;

	public ItemLens() {
		super();
		setUnlocalizedName(LibItemNames.LENS);
		setMaxStackSize(1);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new CompositeLensRecipe());
		RecipeSorter.register("botania:compositeLens", CompositeLensRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconGlass = IconHelper.forName(par1IconRegister, "lensInside");

		ringIcons = new IIcon[SUBTYPES];
		for(int i = 0; i < ringIcons.length; i++)
			ringIcons[i] = IconHelper.forName(par1IconRegister, LibItemNames.LENS_NAMES[i]);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < SUBTYPES; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 1 ? ringIcons[Math.min(SUBTYPES - 1, par1)] : iconGlass;
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return getIconFromDamageForRenderPass(par1, 0);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return par2 == 0 ? getLensColor(par1ItemStack) : 0xFFFFFF;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + LibItemNames.LENS_NAMES[Math.min(SUBTYPES - 1, par1ItemStack.getItemDamage())];
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		int storedColor = getStoredColor(par1ItemStack);
		if(storedColor != -1)
			par3List.add(String.format(StatCollector.translateToLocal("botaniamisc.color"), StatCollector.translateToLocal("botania.color" + storedColor)));
	}


	public String getItemShortTermName(ItemStack stack) {
		return StatCollector.translateToLocal(stack.getUnlocalizedName().replaceAll("item.", "item.botania:") + ".short");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens == null)
			return super.getItemStackDisplayName(stack);
		return String.format(StatCollector.translateToLocal("item.botania:compositeLens.name"), getItemShortTermName(stack), getItemShortTermName(compositeLens));
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		int storedColor = getStoredColor(stack);
		if(storedColor != -1)
			props.color = getLensColor(stack);

		switch(stack.getItemDamage()) {
		case SPEED : {
			props.motionModifier *= 2F;
			props.maxMana *= 0.75F;
			props.ticksBeforeManaLoss /= 3F;
			props.manaLossPerTick *= 2F;
			break;
		}
		case POWER : {
			props.maxMana *= 2;
			props.motionModifier *= 0.85F;
			props.manaLossPerTick *= 2F;
			break;
		}
		case TIME : {
			props.ticksBeforeManaLoss *= 2.25F;
			props.motionModifier *= 0.8F;
			break;
		}
		case EFFICIENCY : {
			props.manaLossPerTick /= 5F;
			props.ticksBeforeManaLoss *= 1.1F;
			break;
		}
		case GRAVITY : {
			props.gravity = 0.0015F;
			props.ticksBeforeManaLoss *= 1.2F;
			break;
		}
		}

		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).apply(compositeLens, props);
	}

	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;

		switch(stack.getItemDamage()) {
		case BOUNCE : {
			if(!isManaBlock && pos.entityHit == null) {
				ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
				if(coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) {
					Vector3 currentMovementVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);
					ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
					Vector3 normalVector = new Vector3(dir.offsetX, dir.offsetY, dir.offsetZ).normalize();
					Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

					burst.setMotion(movementVec.x, movementVec.y, movementVec.z);
					dead = false;
				}
			}
			break;
		}
		case MINE : {
			World world = entity.worldObj;
			int x = pos.blockX;
			int y = pos.blockY;
			int z = pos.blockZ;
			Block block = world.getBlock(x, y, z);
			TileEntity tile = world.getTileEntity(x, y, z);

			int meta = world.getBlockMetadata(x, y, z);
			float hardness = block.getBlockHardness(world, x, y, z);
			int mana = burst.getMana();

			ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
			if((coords.posX != x || coords.posY != y || coords.posZ != z) && !(tile instanceof IManaBlock) && block != null && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
				List<ItemStack> items = new ArrayList();

				items.addAll(block.getDrops(world, x, y, z, meta, 0));

				if(!burst.hasAlreadyCollidedAt(x, y, z)) {
					if(!burst.isFake() && !entity.worldObj.isRemote) {
						world.setBlockToAir(x, y, z);
						if(ConfigHandler.blockBreakParticles)
							entity.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));

						for(ItemStack stack_ : items)
							world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack_));
						burst.setMana(mana - 24);
					}
				}

				dead = false;
			}
			break;
		}
		case PHANTOM : {
			if(!isManaBlock) {
				dead = false;
				burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
			}
			break;
		}
		case EXPLOSIVE : {
			if(!burst.isFake()) {
				ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
				if(!entity.worldObj.isRemote && pos.entityHit == null && !isManaBlock && (pos.blockX != coords.posX || pos.blockY != coords.posY || pos.blockZ != coords.posZ))
					entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, burst.getMana() / 50F, true);
			} else dead = false;
			break;
		}
		case WEIGHT : {
			if(!burst.isFake()) {
				int x = pos.blockX;
				int y = pos.blockY;
				int z = pos.blockZ;
				Block block = entity.worldObj.getBlock(x, y, z);
				Block blockBelow = entity.worldObj.getBlock(x, y - 1, z);
				if(blockBelow.isAir(entity.worldObj, x, y - 1, z) && block.getBlockHardness(entity.worldObj, x, y, z) != -1 && entity.worldObj.getTileEntity(x, y, z) == null) {
					EntityFallingBlock falling = new EntityFallingBlock(entity.worldObj, x + 0.5, y + 0.5, z + 0.5, block, entity.worldObj.getBlockMetadata(x, y, z));
					if(!entity.worldObj.isRemote)
						entity.worldObj.spawnEntityInWorld(falling);
				}
			}
			break;
		}
		case PAINT : {
			int storedColor = getStoredColor(stack);
			if(!burst.isFake() && storedColor > -1 && storedColor < 17) {
				if(pos.entityHit != null && pos.entityHit instanceof EntitySheep) {
					int r = 20;
					int sheepColor = ((EntitySheep) pos.entityHit).getFleeceColor();
					List<EntitySheep> sheepList = entity.worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBox(pos.entityHit.posX - r, pos.entityHit.posY - r, pos.entityHit.posZ - r, pos.entityHit.posX + r, pos.entityHit.posY + r, pos.entityHit.posZ + r));
					for(EntitySheep sheep : sheepList) {
						if(sheep.getFleeceColor() == sheepColor)
							sheep.setFleeceColor(storedColor == 16 ? sheep.worldObj.rand.nextInt(16) : storedColor);
					}
					dead = true;
				} else {
					Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
					if(paintableBlocks.contains(block)) {
						int meta = entity.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
						List<ChunkCoordinates> coordsToPaint = new ArrayList();
						List<ChunkCoordinates> coordsFound = new ArrayList();

						ChunkCoordinates theseCoords = new ChunkCoordinates(pos.blockX, pos.blockY, pos.blockZ);
						coordsFound.add(theseCoords);

						do {
							List<ChunkCoordinates> iterCoords = new ArrayList(coordsFound);
							for(ChunkCoordinates coords : iterCoords) {
								coordsFound.remove(coords);
								coordsToPaint.add(coords);

								for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
									Block block_ = entity.worldObj.getBlock(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
									int meta_ = entity.worldObj.getBlockMetadata(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
									ChunkCoordinates coords_ = new ChunkCoordinates(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
									if(block_ == block && meta_ == meta && !coordsFound.contains(coords_) && !coordsToPaint.contains(coords_))
										coordsFound.add(coords_);
								}
							}
						} while(!coordsFound.isEmpty() && coordsToPaint.size() < 1000);

						for(ChunkCoordinates coords : coordsToPaint) {
							int placeColor = storedColor == 16 ? entity.worldObj.rand.nextInt(16) : storedColor;
							int metaThere = entity.worldObj.getBlockMetadata(coords.posX, coords.posY, coords.posZ);

							if(metaThere != placeColor) {
								if(!entity.worldObj.isRemote)
									entity.worldObj.setBlockMetadataWithNotify(coords.posX, coords.posY, coords.posZ, placeColor, 2);
								float[] color = EntitySheep.fleeceColorTable[placeColor];
								float r = color[0];
								float g = color[1];
								float b = color[2];
								for(int i = 0; i < 4; i++)
									Botania.proxy.sparkleFX(entity.worldObj, coords.posX + (float) Math.random(), coords.posY + (float) Math.random(), coords.posZ + (float) Math.random(), r, g, b, 0.6F + (float) Math.random() * 0.3F, 5);

							}
						}
					}
				}
			}
			break;
		}
		case FIRE : {
			ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
			if((coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) && !burst.isFake() && !isManaBlock) {
				ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
				if(entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ) == Blocks.portal)
					entity.worldObj.setBlock(pos.blockX, pos.blockY, pos.blockZ, Blocks.air);
				else {
					int x = pos.blockX + dir.offsetX;
					int y = pos.blockY + dir.offsetY;
					int z = pos.blockZ + dir.offsetZ;
					entity.worldObj.setBlock(x, y, z, Blocks.fire);
				}
			}

			break;
		}
		case PISTON : {
			ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
			if((coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) && !burst.isFake() && !isManaBlock && !entity.worldObj.isRemote) {
				ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit).getOpposite();
				int x = pos.blockX + dir.offsetX;
				int y = pos.blockY + dir.offsetY;
				int z = pos.blockZ + dir.offsetZ;

				if(entity.worldObj.isAirBlock(x, y, z) || entity.worldObj.getBlock(x, y, z).isReplaceable(entity.worldObj, x, y, z)) {
					Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
					int meta = entity.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
					TileEntity tile = entity.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);

					if(block.getMobilityFlag() == 0 && block != Blocks.obsidian && tile == null) {
						entity.worldObj.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
						entity.worldObj.setBlock(x, y, z, block, meta, 1 | 2);
						entity.worldObj.playAuxSFX(2001, pos.blockX, pos.blockY, pos.blockZ, Block.getIdFromBlock(block) + (meta << 12));
					}
				}
			}
			break;
		}
		}

		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			dead = ((ILens) compositeLens.getItem()).collideBurst(burst, pos, isManaBlock, dead, compositeLens);

		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;
		int storedColor = getStoredColor(stack);

		if(storedColor == 16 && entity.worldObj.isRemote)
			burst.setColor(getLensColor(stack));

		boolean magnetized = entity.getEntityData().hasKey("Botania:Magnetized");
		switch(stack.getItemDamage()) {
		case DAMAGE : {
			AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1, 1, 1);
			List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
			for(EntityLivingBase living : entities) {
				if(living instanceof EntityPlayer)
					continue;

				if(living.hurtTime == 0) {
					int mana = burst.getMana();
					if(mana >= 16) {
						burst.setMana(mana - 16);
						if(!burst.isFake() && !entity.worldObj.isRemote)
							living.attackEntityFrom(DamageSource.magic, 4);
						break;
					}
				}
			}
			break;
		}
		case MAGNET : {
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			int range = 3;

			magnetize : {
				for(int i = -range; i < range; i++)
					for(int j = -range; j < range; j++)
						for(int k = -range; k < range; k++)
							if(entity.worldObj.getTileEntity(i + x, j + y, k + z) instanceof IManaReceiver) {
								TileEntity tile = entity.worldObj.getTileEntity(i + x, j + y, k + z);

								if(magnetized) {
									int magX = entity.getEntityData().getInteger("Botania:MagnetizedX");
									int magY = entity.getEntityData().getInteger("Botania:MagnetizedY");
									int magZ = entity.getEntityData().getInteger("Botania:MagnetizedZ");
									if(tile.xCoord != magX || tile.yCoord != magY || tile.zCoord != magZ)
										continue;
								}

								IManaReceiver receiver = (IManaReceiver) tile;

								ChunkCoordinates srcCoords = burst.getBurstSourceChunkCoordinates();

								if(MathHelper.pointDistanceSpace(tile.xCoord, tile.yCoord, tile.zCoord, srcCoords.posX, srcCoords.posY, srcCoords.posZ) > 3 && receiver.canRecieveManaFromBursts() && !receiver.isFull()) {
									Vector3 burstVec = Vector3.fromEntity(entity);
									Vector3 tileVec = Vector3.fromTileEntityCenter(tile).add(0, -0.1, 0);
									Vector3 motionVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);

									Vector3 normalMotionVec = motionVec.copy().normalize();
									Vector3 magnetVec = tileVec.sub(burstVec).normalize();
									Vector3 differenceVec = normalMotionVec.sub(magnetVec).multiply(motionVec.mag() * 0.1);

									Vector3 finalMotionVec = motionVec.sub(differenceVec);
									if(!magnetized) {
										finalMotionVec.multiply(0.75);
										entity.getEntityData().setBoolean("Botania:Magnetized", true);
										entity.getEntityData().setInteger("Botania:MagnetizedX", tile.xCoord);
										entity.getEntityData().setInteger("Botania:MagnetizedY", tile.yCoord);
										entity.getEntityData().setInteger("Botania:MagnetizedZ", tile.zCoord);
									}

									burst.setMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
									break magnetize;
								}
							}
			}
			break;
		}
		case INFLUENCE : {
			if(!burst.isFake()) {
				double range = 3.5;
				List<Entity> movables = entity.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range));
				movables.addAll(entity.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range)));
				movables.addAll(entity.worldObj.getEntitiesWithinAABB(EntityManaBurst.class, AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range)));
				movables.addAll(entity.worldObj.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range)));
				movables.addAll(entity.worldObj.getEntitiesWithinAABB(EntityFallingBlock.class, AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range)));

				for(Entity movable : movables) {
					movable.motionX = entity.motionX;
					movable.motionY = entity.motionY;
					movable.motionZ = entity.motionZ;
				}
			}

			break;
		}
		}

		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).updateBurst(burst, compositeLens);
	}

	@Override
	public int getLensColor(ItemStack stack) {
		int storedColor = getStoredColor(stack);

		if(storedColor == -1)
			return 0xFFFFFF;

		if(storedColor == 16)
			return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 1F, 1F);

		float[] color = EntitySheep.fleeceColorTable[storedColor];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	public static int getStoredColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, -1);
	}

	public static ItemStack setLensColor(ItemStack stack, int color) {
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);
		return stack;
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}

	public static void setProps(int lens, int props_) {
		props.put(lens, props_);
	}

	public static boolean isBlacklisted(int lens1, int lens2) {
		return (props.get(lens1) & props.get(lens2)) != 0;
	}

	@Override
	public boolean canCombineLenses(ItemStack sourceLens, ItemStack compositeLens) {
		if(sourceLens.getItemDamage() == compositeLens.getItemDamage())
			return false;

		if(sourceLens.getItemDamage() == NORMAL || compositeLens.getItemDamage() == NORMAL)
			return false;

		if(isBlacklisted(sourceLens.getItemDamage(), compositeLens.getItemDamage()))
			return false;

		return true;
	}

	@Override
	public ItemStack getCompositeLens(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COMPOSITE_LENS, false);
		ItemStack lens = ItemStack.loadItemStackFromNBT(cmp);
		return lens;
	}

	@Override
	public ItemStack setCompositeLens(ItemStack sourceLens, ItemStack compositeLens) {
		NBTTagCompound cmp = new NBTTagCompound();
		compositeLens.writeToNBT(cmp);
		ItemNBTHelper.setCompound(sourceLens, TAG_COMPOSITE_LENS, cmp);

		return sourceLens;
	}
}
