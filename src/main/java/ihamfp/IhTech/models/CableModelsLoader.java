package ihamfp.IhTech.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class CableModelsLoader implements ICustomModelLoader {
	
	private static final List<String> accepts = new ArrayList<String>() {{
		add("blockCable1x");
	}};
	
	public static final ModelCable CABLE1_MODEL = new ModelCable();
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (!modelLocation.getResourceDomain().equals(ModIhTech.MODID)) return false;
		if (!accepts.contains(modelLocation.getResourcePath())) return false;
		return true;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		if (modelLocation.getResourcePath().equals("blockCable1x")) {
			return CABLE1_MODEL;
		}
		return null;
	}

}
