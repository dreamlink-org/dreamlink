package dreamlink.menu.component.core.border;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.StaticBackgroundComponentProvider;

public class WindowBorderComponent extends WrapperComponent {

    private static int padding = 3;

    private BaseMenuComponent component;

    public WindowBorderComponent(BaseMenuComponent component) {
        this.component = new BackgroundComponent(
            new StaticBackgroundComponentProvider(BorderSpriteTemplate.window),
            new PaddingComponent(component, WindowBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

}
