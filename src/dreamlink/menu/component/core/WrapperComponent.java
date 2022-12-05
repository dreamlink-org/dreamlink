package dreamlink.menu.component.core;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;

public abstract class WrapperComponent extends BaseMenuComponent {

    public abstract BaseMenuComponent getComponent();

    @Override
    public Vector2i getInitialDimensions() {
        var component = this.getComponent();
        return component.getInitialDimensions();
    }

    @Override
    public Vector2i getDimensions() {
        var component = this.getComponent();
        return component.getDimensions();
    }

    @Override
    public void computeInitialDimensions() {
        var component = this.getComponent();
        component.computeInitialDimensions();
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        var component = this.getComponent();
        component.computeDimensions(availableSpace);
    }

    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i origin) {
        super.finalizeLayout(parent, origin);
        var component = this.getComponent();
        component.finalizeLayout(this, origin);
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return this.getComponent().getHoveredComponent();
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var component = this.getComponent();
        component.writeToSpriteBatch(spriteBatch);
    }

    
}
