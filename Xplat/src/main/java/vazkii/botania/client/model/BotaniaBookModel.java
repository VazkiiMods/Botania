package vazkii.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

// [VanillaCopy] BookModel, with z-fighting fixes
public class BotaniaBookModel extends Model {
	private static final String LEFT_PAGES = "left_pages";
	private static final String RIGHT_PAGES = "right_pages";
	private static final String FLIP_PAGE_1 = "flip_page1";
	private static final String FLIP_PAGE_2 = "flip_page2";
	private final ModelPart root;
	private final ModelPart leftLid;
	private final ModelPart rightLid;
	private final ModelPart leftPages;
	private final ModelPart rightPages;
	private final ModelPart flipPage1;
	private final ModelPart flipPage2;

	public BotaniaBookModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.root = root;
		this.leftLid = root.getChild("left_lid");
		this.rightLid = root.getChild("right_lid");
		this.leftPages = root.getChild(LEFT_PAGES);
		this.rightPages = root.getChild(RIGHT_PAGES);
		this.flipPage1 = root.getChild(FLIP_PAGE_1);
		this.flipPage2 = root.getChild(FLIP_PAGE_2);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("left_lid",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-6, -5, -0.005f, 6, 10, 0.005f),
				PartPose.offset(0, 0, -1));
		partdefinition.addOrReplaceChild("right_lid",
				CubeListBuilder.create().texOffs(16, 0)
						.addBox(0, -5, -0.005f, 6, 10, 0.005f),
				PartPose.offset(0, 0, 1));
		partdefinition.addOrReplaceChild("seam",
				CubeListBuilder.create().texOffs(12, 0)
						.addBox(-1, -5, /* 0 in vanilla: */ 0.005f, 2, 10, 0.005f),
				PartPose.rotation(0, (float) (Math.PI / 2), 0));
		partdefinition.addOrReplaceChild(LEFT_PAGES,
				CubeListBuilder.create().texOffs(0, 10)
						.addBox(0, -4, -0.99f, 5, 8, 1),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild(RIGHT_PAGES,
				CubeListBuilder.create().texOffs(12, 10)
						.addBox(0, -4, -0.01f, 5, 8, 1),
				PartPose.ZERO);
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(24, 10)
				.addBox(0, -4, 0, 5, 8, 0.005f);
		partdefinition.addOrReplaceChild(FLIP_PAGE_1, cubelistbuilder, PartPose.ZERO);
		partdefinition.addOrReplaceChild(FLIP_PAGE_2, cubelistbuilder, PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(float time, float rightPageFlipAmount, float leftPageFlipAmount, float bookOpenAmount) {
		float f = (Mth.sin(time * 0.02F) * 0.1F + 1.25F) * bookOpenAmount;
		this.leftLid.yRot = (float) Math.PI + f;
		this.rightLid.yRot = -f;
		this.leftPages.yRot = f;
		this.rightPages.yRot = -f;
		this.flipPage1.yRot = f - f * 2.0F * rightPageFlipAmount;
		this.flipPage2.yRot = f - f * 2.0F * leftPageFlipAmount;
		this.leftPages.x = Mth.sin(f);
		this.rightPages.x = Mth.sin(f);
		this.flipPage1.x = Mth.sin(f);
		this.flipPage2.x = Mth.sin(f);
	}
}
