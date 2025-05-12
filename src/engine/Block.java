// TEMP BLOCK -> TEST GRAPHICS

package engine;

import graphics.Texture;

public class Block {
    private final Texture tex;
    public Block(Texture tex) { this.tex = tex; }
    public Texture getTexture() { return tex; }
}
