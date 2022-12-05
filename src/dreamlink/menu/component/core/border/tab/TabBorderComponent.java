package dreamlink.menu.component.core.border.tab;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.IBackgroundComponentProvider;

public class TabBorderComponent extends WrapperComponent {

    private class InternalBackgroundComponentProvider implements IBackgroundComponentProvider {

        @Override
        public ISpriteTemplate getSprite() {
            return TabBorderComponent.this.provider.isTabSelected()
                ? BorderSpriteTemplate.tabSelected
                : BorderSpriteTemplate.tab;
        }

    }

    private static int padding = 2;

    private ITabBorderComponentProvider provider;
    private BaseMenuComponent component;

    public TabBorderComponent(
        ITabBorderComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = new BackgroundComponent(
            new InternalBackgroundComponentProvider(),
            new PaddingComponent(component, TabBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
