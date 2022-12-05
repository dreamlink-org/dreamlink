package dreamlink.menu.component.simulation.edit;

import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.simulation.edit.stamppalette.IStampPaletteComponentProvider;
import dreamlink.menu.component.simulation.edit.stamppalette.StampPaletteComponent;
import dreamlink.player.Player;
import dreamlink.zone.IStamp;

public class DoorListComponent extends WrapperComponent {

    private class InternalListComponentProvider implements IStampPaletteComponentProvider {

        @Override
        public int getTotalStampCount() {
            return Player.instance.getZone().getDoorCount();
        }

        @Override
        public IStamp getStampByIndex(int index) {
            return Player.instance.getZone().getDoorByIndex(index);
        }
    }

    private static int itemSpacing = 5;

    private StampPaletteComponent doorsComponent;
    private BaseMenuComponent component;

    public DoorListComponent() {
        this.doorsComponent = new StampPaletteComponent(new InternalListComponentProvider());

        this.component = new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, DoorListComponent.itemSpacing)
            .addComponent(
                new BoxComponent(
                    this.doorsComponent,
                    BoxDimension.grow(),
                    BoxDimension.grow()
                )
            );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }
}
