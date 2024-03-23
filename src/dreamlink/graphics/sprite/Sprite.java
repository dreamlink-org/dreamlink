package dreamlink.graphics.sprite;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import dreamlink.graphics.mesh.SpriteMesh;
import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.window.Window;

public class Sprite {

    private Vector2i position;
    private Vector2i dimensions;
    private SpriteHeight spriteHeight;
    private TextureSample textureSample;
    private Vector4f color;

    public Sprite() {
        this.position = new Vector2i();
        this.dimensions = new Vector2i();
        this.color = new Vector4f();
    }

    public int getSpriteHeight() {
        return this.spriteHeight.height;
    }

    public Sprite set(
        Vector2i position, 
        Vector2i dimensions, 
        SpriteHeight height,
        TextureSample textureSample, 
        Vector4f color
    ) {
        this.position.set(position);
        this.dimensions.set(dimensions);
        this.spriteHeight = height;
        this.textureSample = textureSample;
        this.color.set(color);
        this.spriteHeight = height;
        return this;
    }

    public void writeToMesh(SpriteMesh mesh) {
        var resolution = Window.instance.getResolution();
        var invertedY = resolution.y - this.position.y - this.dimensions.y;
        var spritePosition = new Vector2f(
            (float)this.position.x / resolution.x * 2f - 1f,
            (float)invertedY / resolution.y * 2f - 1f
        );

        var spriteDimensions = new Vector2f(
            (float)this.dimensions.x / resolution.x * 2f, 
            (float)this.dimensions.y / resolution.y * 2f
        );

        mesh.addQuad(
            spritePosition,
            spriteDimensions,
            this.textureSample,
            this.color
        );
    }
    
}
