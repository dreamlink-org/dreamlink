package dreamlink.menu.component.core.border.tab;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.StaticBackgroundComponentProvider;

public class TabBodyBorderComponent extends WrapperComponent {

    private static int padding = 2;

    private BaseMenuComponent component;

    public TabBodyBorderComponent(BaseMenuComponent component) {
        this.component = new BackgroundComponent(
            new StaticBackgroundComponentProvider(BorderSpriteTemplate.tabBody),
            new PaddingComponent(component, TabBodyBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
