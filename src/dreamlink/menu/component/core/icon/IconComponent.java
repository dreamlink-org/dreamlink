package dreamlink.menu.component.core.icon;


import org.joml.Vector2i;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.EmptyComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;

public class IconComponent extends WrapperComponent {

    private BaseMenuComponent component;

    public IconComponent(
        IIconComponentProvider provider,
        Vector2i dimensions
    ) {
        this.component = new BackgroundComponent(
            provider::getSprite,
            new BoxComponent(
                new EmptyComponent(),
                BoxDimension.fixed(dimensions.x),
                BoxDimension.fixed(dimensions.y)
            )
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
