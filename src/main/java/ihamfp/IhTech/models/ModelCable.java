package ihamfp.IhTech.models;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.ImmutableSet;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelCable implements IModel {
	
	public ModelCable() {}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of(new ResourceLocation(ModIhTech.MODID, "blocks/blockstorage"));
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new ExampleBakedModel(state, format, bakedTextureGetter);
	}

}
