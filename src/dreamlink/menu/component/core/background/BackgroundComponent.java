package dreamlink.menu.component.core.background;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.sprite.SpriteHeight;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;

public class BackgroundComponent extends WrapperComponent {

    private IBackgroundComponentProvider provider;
    private BaseMenuComponent component;

    public BackgroundComponent(
        IBackgroundComponentProvider provider,
        BaseMenuComponent component
    ) {
        this.provider = provider;
        this.component = component;
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        var sprite = this.provider.getSprite();
        if(sprite == null) {
            return;
        }

        sprite.writeToSpriteBatch(
            spriteBatch,
            this.getPosition(),
            this.getDimensions(),
            SpriteHeight.menu
        );

        super.writeToSpriteBatch(spriteBatch);
    }
    
}
