package dreamlink.menu.component.core.border;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.StaticBackgroundComponentProvider;

public class SeparatorBorderComponent extends WrapperComponent {

    private static int padding = 3;

    private BaseMenuComponent component;

    public SeparatorBorderComponent(BaseMenuComponent component) {
        this.component = new BackgroundComponent(
            new StaticBackgroundComponentProvider(BorderSpriteTemplate.separator),
            new PaddingComponent(component, SeparatorBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

}
