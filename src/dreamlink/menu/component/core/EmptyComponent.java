package dreamlink.menu.component.core;

import org.joml.Vector2i;

import dreamlink.utility.maths.Vector2iMaths;


public class EmptyComponent extends BaseMenuComponent {

    @Override
    public Vector2i getInitialDimensions() {
        return Vector2iMaths.zero;
    }

    @Override
    public Vector2i getDimensions() {
        return Vector2iMaths.zero;
    }

}
