package thaumcraft.api;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ThaumcraftMaterials {

	public static ToolMaterial TOOLMAT_THAUMIUM = EnumHelper.addToolMaterial("THAUMIUM", 3, 500, 7F, 2.5f, 22);
	public static ToolMaterial TOOLMAT_VOID = EnumHelper.addToolMaterial("VOID", 4, 150, 8F, 3, 10);
	public static ToolMaterial TOOLMAT_ELEMENTAL = EnumHelper.addToolMaterial("THAUMIUM_ELEMENTAL", 3, 1500, 9F, 3, 18);
	public static ArmorMaterial ARMORMAT_THAUMIUM = EnumHelper.addArmorMaterial("THAUMIUM","THAUMIUM", 25, new int[] { 2, 6, 5, 2 }, 25);
	public static ArmorMaterial ARMORMAT_SPECIAL = EnumHelper.addArmorMaterial("SPECIAL","SPECIAL", 25, new int[] { 1, 3, 2, 1 }, 25);
	public static ArmorMaterial ARMORMAT_VOID = EnumHelper.addArmorMaterial("VOID","VOID", 10, new int[] { 3, 7, 6, 3 }, 10);
	
	public static final Material MATERIAL_TAINT = new MaterialTaint();
	
	public static class MaterialTaint extends Material
	{
	    public MaterialTaint()
	    {
	        super(MapColor.purpleColor);
	        setNoPushMobility();
	    }	    
	    
	    @Override
	    public boolean blocksMovement()
	    {
	        return true;
	    }
	    
	    @Override
		protected Material setNoPushMobility()
	    {
	        this.mobilityFlag = 1;
	        return this;
	    }
		
		private int mobilityFlag;
		
		@Override
		public int getMaterialMobility()
	    {
	        return this.mobilityFlag;
	    }
	    
	}
	
	
}
