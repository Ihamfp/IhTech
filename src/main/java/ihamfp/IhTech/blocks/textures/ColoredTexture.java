package ihamfp.IhTech.blocks.textures;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ColoredTexture extends TextureAtlasSprite {
	private int color;
	
	protected ColoredTexture(String spriteName, int color) {
		super(spriteName);
	}
	
}
