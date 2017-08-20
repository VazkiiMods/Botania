/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 18, 2014, 10:05:39 PM (GMT)]
 */
package vazkii.botania.client.model;

import java.util.function.Function;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import vazkii.botania.api.state.enums.PylonVariant;

public class ModelPylonMana extends ModelBase implements IModel {

	final ModelRenderer platef;
    final ModelRenderer plateb;
    final ModelRenderer platel;
    final ModelRenderer plater;
	
    final ModelRenderer shardlf;
    final ModelRenderer shardrf;
    final ModelRenderer shardlb;
    final ModelRenderer shardrb;
    
	public ModelPylonMana() {
		
		textureWidth = 64;
		textureHeight = 64;

		//plates
		platef = new ModelRenderer(this, 36, 0);
        platef.setRotationPoint(0.0F, 16.0F, 0.0F);
        platef.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
        plateb = new ModelRenderer(this, 36, 0);
        plateb.setRotationPoint(0.0F, 16.0F, 0.0F);
        plateb.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
        setRotation(plateb, 0.0F, 3.141592653589793F, 0.0F);
		platel = new ModelRenderer(this, 36, 0);
        platel.setRotationPoint(0.0F, 16.0F, 0.0F);
        platel.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
        setRotation(platel, 0.0F, 1.5707963267948966F, 0.0F);
        plater = new ModelRenderer(this, 36, 0);
        plater.setRotationPoint(0.0F, 16.0F, 0.0F);
        plater.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2, 0.0F);
        setRotation(plater, 0.0F, -1.5707963267948966F, 0.0F);
        
        //shards
        shardlf = new ModelRenderer(this, 0, 21);
        shardlf.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlf.addBox(-5.0F, -9.0F, -5.0F, 5, 16, 3, 0.0F);
        shardrf = new ModelRenderer(this, 16, 21);
        shardrf.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrf.addBox(2.0F, -12.0F, -5.0F, 3, 16, 3, 0.0F);
        shardlb = new ModelRenderer(this, 0, 0);
        shardlb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardlb.addBox(-5.0F, -10.0F, 0.0F, 6, 16, 5, 0.0F);
        shardrb = new ModelRenderer(this, 22, 0);
        shardrb.setRotationPoint(0.0F, 16.0F, 0.0F);
        shardrb.addBox(3.0F, -11.0F, 0.0F, 2, 16, 5, 0.0F);
	}

	public void renderCrystal(PylonVariant variant) {
		float f = 1F / 16F;
		
		shardlf.render(f);
        shardrf.render(f);
        shardlb.render(f);
        shardrb.render(f);
	}

	public void renderRing(PylonVariant variant) {
		float f = 1F / 16F;
		
        platef.render(f);
        plateb.render(f);
        platel.render(f);
        plater.render(f);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void renderGems(PylonVariant variant) {}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		// TODO Auto-generated method stub
		return null;
	}

}