package thaumcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;

public class OreDictionaryEntries {

	/**
	 * I included this in the API to make it simpler to see what items and blocks are ore dictionaried 
	 */
	public static void initializeOreDictionary() {
		OreDictionary.registerOre("oreAmber", new ItemStack(BlocksTC.oreAmber));
		OreDictionary.registerOre("oreCinnabar", new ItemStack(BlocksTC.oreCinnabar));
		
		OreDictionary.registerOre("logWood", new ItemStack(BlocksTC.log,1,0));
		OreDictionary.registerOre("logWood", new ItemStack(BlocksTC.log,1,3));
		OreDictionary.registerOre("plankWood", new ItemStack(BlocksTC.plank,1,0));
		OreDictionary.registerOre("plankWood", new ItemStack(BlocksTC.plank,1,1));
		OreDictionary.registerOre("slabWood", new ItemStack(BlocksTC.slabWood,1,0));
		OreDictionary.registerOre("slabWood", new ItemStack(BlocksTC.slabWood,1,1));
		OreDictionary.registerOre("treeSapling", new ItemStack(BlocksTC.sapling,1,0));
		OreDictionary.registerOre("treeSapling", new ItemStack(BlocksTC.sapling,1,1));
		
		OreDictionary.registerOre("shardAir", new ItemStack(ItemsTC.shard,1,0));
		OreDictionary.registerOre("shardFire", new ItemStack(ItemsTC.shard,1,1));
		OreDictionary.registerOre("shardWater", new ItemStack(ItemsTC.shard,1,2));
		OreDictionary.registerOre("shardEarth", new ItemStack(ItemsTC.shard,1,3));
		OreDictionary.registerOre("shardOrder", new ItemStack(ItemsTC.shard,1,4));
		OreDictionary.registerOre("shardEntropy", new ItemStack(ItemsTC.shard,1,5));
		OreDictionary.registerOre("shardTainted", new ItemStack(ItemsTC.shard,1,6));
		OreDictionary.registerOre("shardBalanced", new ItemStack(ItemsTC.shard,1,7));		
		OreDictionary.registerOre("nitor", new ItemStack(BlocksTC.nitor,1,OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("gemAmber", new ItemStack(ItemsTC.amber));
		OreDictionary.registerOre("quicksilver", new ItemStack(ItemsTC.quicksilver));
		OreDictionary.registerOre("nuggetGold", new ItemStack(ItemsTC.coin));
		
		OreDictionary.registerOre("nuggetIron", new ItemStack(ItemsTC.nuggets,1,0));
		OreDictionary.registerOre("nuggetCopper", new ItemStack(ItemsTC.nuggets,1,1));
		OreDictionary.registerOre("nuggetTin", new ItemStack(ItemsTC.nuggets,1,2));
		OreDictionary.registerOre("nuggetSilver", new ItemStack(ItemsTC.nuggets,1,3));
		OreDictionary.registerOre("nuggetLead", new ItemStack(ItemsTC.nuggets,1,4));
		OreDictionary.registerOre("nuggetQuicksilver", new ItemStack(ItemsTC.nuggets,1,5));
		OreDictionary.registerOre("nuggetThaumium", new ItemStack(ItemsTC.nuggets,1,6));
		OreDictionary.registerOre("nuggetVoid", new ItemStack(ItemsTC.nuggets,1,7));
		OreDictionary.registerOre("nuggetBrass", new ItemStack(ItemsTC.nuggets,1,8));		
		
		OreDictionary.registerOre("ingotThaumium", new ItemStack(ItemsTC.ingots,1,0));
		OreDictionary.registerOre("ingotVoid", new ItemStack(ItemsTC.ingots,1,1));
		OreDictionary.registerOre("ingotBrass", new ItemStack(ItemsTC.ingots,1,2));
		
		OreDictionary.registerOre("blockThaumium", new ItemStack(BlocksTC.metal,1,0));
		OreDictionary.registerOre("blockVoid", new ItemStack(BlocksTC.metal,1,1));
		OreDictionary.registerOre("blockBrass", new ItemStack(BlocksTC.metal,1,4));
		
		OreDictionary.registerOre("plateIron", new ItemStack(ItemsTC.plate,1,1));
		OreDictionary.registerOre("gearBrass", new ItemStack(ItemsTC.gear,1,0));
		OreDictionary.registerOre("plateBrass", new ItemStack(ItemsTC.plate,1,0));
		OreDictionary.registerOre("gearThaumium", new ItemStack(ItemsTC.gear,1,1));
		OreDictionary.registerOre("plateThaumium", new ItemStack(ItemsTC.plate,1,2));
		OreDictionary.registerOre("gearVoid", new ItemStack(ItemsTC.gear,1,2));
		OreDictionary.registerOre("plateVoid", new ItemStack(ItemsTC.plate,1,3));
				
		OreDictionary.registerOre("clusterIron", new ItemStack(ItemsTC.clusters,1,0));
		OreDictionary.registerOre("clusterGold", new ItemStack(ItemsTC.clusters,1,1));	
		OreDictionary.registerOre("clusterCopper", new ItemStack(ItemsTC.clusters,1,2));
		OreDictionary.registerOre("clusterTin", new ItemStack(ItemsTC.clusters,1,3));
		OreDictionary.registerOre("clusterSilver", new ItemStack(ItemsTC.clusters,1,4));
		OreDictionary.registerOre("clusterLead", new ItemStack(ItemsTC.clusters,1,5));
		OreDictionary.registerOre("clusterCinnabar", new ItemStack(ItemsTC.clusters,1,6));
	
	}

}
