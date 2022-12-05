package dreamlink.graphics.sprite;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import org.joml.Vector2i;
import org.joml.Vector4f;

import dreamlink.graphics.mesh.SpriteMesh;
import dreamlink.graphics.texture.sample.TextureSample;

public class SpriteBatch {

    private static Queue<Sprite> unusedFragments = new ArrayDeque<>();
    private static Comparator<Sprite> fragmentComparator = Comparator.comparing(Sprite::getSpriteHeight);

    private SpriteMesh mesh;
    private List<Sprite> spriteFragments;

    public SpriteBatch() {
        this.mesh = new SpriteMesh();
        this.spriteFragments = new ArrayList<>();
    }

    private Sprite getFreeSpriteFragment() {
        return SpriteBatch.unusedFragments.isEmpty()
            ? new Sprite()
            : SpriteBatch.unusedFragments.remove();
    }


    public void setup() {
        this.mesh.setup();
    }

    public int getSpriteCount() {
        return this.spriteFragments.size();
    }

    public void clear() {
        for(var spriteFragment : this.spriteFragments) {
            SpriteBatch.unusedFragments.add(spriteFragment);
        }

        this.spriteFragments.clear();
    }

    public void writeTextureSample(
        Vector2i position,
        Vector2i dimensions,
        SpriteHeight height,
        TextureSample textureSample,
        Vector4f color
    ) {
        this.spriteFragments.add(
            this.getFreeSpriteFragment().set(
                position,
                dimensions,
                height,
                textureSample,
                color
            )
        );
    }

    public void render() {
        this.spriteFragments.sort(SpriteBatch.fragmentComparator);
        this.mesh.clear();
        for(var spriteFragment : this.spriteFragments) {
            spriteFragment.writeToMesh(this.mesh);
        }
        this.mesh.buffer();
        this.mesh.render();
    }

    public void destroy() {
        this.clear();
        this.mesh.destroy();
    }

}
