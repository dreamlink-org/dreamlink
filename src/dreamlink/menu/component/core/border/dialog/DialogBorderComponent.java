package dreamlink.menu.component.core.border.dialog;

import dreamlink.graphics.sprite.template.BorderSpriteTemplate;
import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.background.BackgroundComponent;
import dreamlink.menu.component.core.background.IBackgroundComponentProvider;

public class DialogBorderComponent extends WrapperComponent {

    private class InternalBackgroundComponentProvider implements IBackgroundComponentProvider {

        @Override
        public ISpriteTemplate getSprite() {
            var state = DialogBorderComponent.this.provider.getDialogState();
            if(state == DialogState.disabled) {
                return BorderSpriteTemplate.dialogDisabled;
            }
            if(state == DialogState.blurred) {
                return BorderSpriteTemplate.dialogBlurred;
            }
            return BorderSpriteTemplate.dialog;
        }
    }

    private static int padding = 2;

    private IDialogBorderComponentProvider provider;
    private BaseMenuComponent component;

    public DialogBorderComponent(
        IDialogBorderComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = new BackgroundComponent(
            new InternalBackgroundComponentProvider(),
            new PaddingComponent(component, DialogBorderComponent.padding)
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
    
}
