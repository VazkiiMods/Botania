/**
 * This class was created by <Pokefenn>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileHydroangeas extends SubTileGenerating {

    private static final String TAG_BURN_TIME = "burnTime";
    int burnTime = 0;
    int currentTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        int range = 1;

        boolean didSomething = false;

        if (burnTime == 0) {
            if (supertile.getWorldObj().getWorldTime() % 40L == 0) {
                if (mana < getMaxMana() && !supertile.getWorldObj().isRemote)
                    for (int i = -range; i <= range; i++)
                        for (int j = -range; j <= range; j++) {
                            if (supertile.getWorldObj().getBlock(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j) == getBlockToSearchFor() && supertile.getWorldObj().getBlockMetadata(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j) == 0) {
                                supertile.getWorldObj().setBlockToAir(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j);
                                didSomething = true;
                                burnTime += getBurnTime();

                                break;

                            }
                        }

                if (didSomething)
                    sync();
            }
        } else {
            if (supertile.getWorldObj().rand.nextInt(8) == 0)
                doBurnParticles();
            burnTime--;
        }
    }

    public void doBurnParticles() {
        Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.05F, 0.05F, 0.7F, (float) Math.random() / 6, (float) -Math.random() / 60);
    }

    public Block getBlockToSearchFor() {
        return Blocks.water;
    }

    public int getBurnTime() {
        return 10;
    }

    @Override
    public int getMaxMana() {
        return 150;
    }

    @Override
    public int getColor() {
        return 0x532FE0;
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconData.hydroangeas;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);

        cmp.setInteger(TAG_BURN_TIME, burnTime);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInteger(TAG_BURN_TIME);
    }

    @Override
    public boolean canGeneratePassively() {
        return burnTime > 0;
    }

    @Override
    public int getDelayBetweenPassiveGeneration() {
        return 4;
    }

}
