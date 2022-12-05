package dreamlink.menu.component.core.background;

import dreamlink.graphics.sprite.template.ISpriteTemplate;

public class StaticBackgroundComponentProvider implements IBackgroundComponentProvider {

    private ISpriteTemplate sprite;

    public StaticBackgroundComponentProvider(ISpriteTemplate sprite) {
        this.sprite = sprite;
    }

    @Override
    public ISpriteTemplate getSprite() {
        return this.sprite;
    }
    
}
