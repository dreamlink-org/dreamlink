package dreamlink.menu.component.core.choice;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.menu.component.core.BaseMenuComponent;

public class ChoiceComponent<T> extends BaseMenuComponent {

    private IChoiceComponentProvider<T> provider;
    private Vector2i initialDimensions;
    private Vector2i dimensions;
    private Map<T, BaseMenuComponent> componentMap;

    public ChoiceComponent(IChoiceComponentProvider<T> provider) {
        this.provider = provider;
        this.initialDimensions = new Vector2i();
        this.dimensions = new Vector2i();
        this.componentMap = new HashMap<>();
    }

    public ChoiceComponent<T> addComponent(T key, BaseMenuComponent component) {
        this.componentMap.put(key, component);
        return this;
    }

    public BaseMenuComponent getSelectedComponent() {
        return this.componentMap.get(this.provider.getSelectedChoiceKey());
    }

    @Override
    public Vector2i getInitialDimensions() {
        return this.initialDimensions;
    }

    @Override
    public Vector2i getDimensions() {
        return this.dimensions;
    }

    @Override
    public void computeInitialDimensions() {
        this.initialDimensions.set(0);

        for(var component : this.componentMap.values()) {
            component.computeInitialDimensions();
            this.initialDimensions.max(component.getInitialDimensions());
        }
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        this.dimensions.set(0);

        for(var component : this.componentMap.values()) {
            component.computeDimensions(availableSpace);
            this.dimensions.max(component.getDimensions());
        }
    }

    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        super.finalizeLayout(parent, position);
        for(var component : this.componentMap.values()) {
            component.finalizeLayout(this, position);
        }
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var selectedComponent = this.getSelectedComponent();
        if(selectedComponent != null) {
            selectedComponent.writeToSpriteBatch(spriteBatch);
        }
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        var selectedComponent = this.getSelectedComponent();
        return selectedComponent != null 
            ? selectedComponent.getHoveredComponent() 
            : null;
    }
    
}
