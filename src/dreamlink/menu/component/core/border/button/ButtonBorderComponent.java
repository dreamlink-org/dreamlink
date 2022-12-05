package dreamlink.menu.component.core.border.button;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.IBackgroundComponentProvider;

public class ButtonBorderComponent extends WrapperComponent {

    private class InternalBackgroundComponentProvider implements IBackgroundComponentProvider {

        @Override
        public ISpriteTemplate getSprite() {
            var state = ButtonBorderComponent.this.provider.getButtonState();

            if(state == ButtonState.pressed) {
                return BorderSpriteTemplate.buttonPressed;
            }

            if(state == ButtonState.disabled) {
                return BorderSpriteTemplate.buttonDisabled;
            }
            
            return BorderSpriteTemplate.button;
        }

    }

    private static int padding = 2;

    private IButtonBorderComponentProvider provider;
    private BaseMenuComponent component;

    public ButtonBorderComponent(
        IButtonBorderComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = new BackgroundComponent(
            new InternalBackgroundComponentProvider(),
            new PaddingComponent(component, ButtonBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

}
