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

public class SpeakerListComponent extends WrapperComponent {

    private class InternalListComponentProvider implements IStampPaletteComponentProvider {

        @Override
        public int getTotalStampCount() {
            return Player.instance.getZone().getSpeakerCount();
        }

        @Override
        public IStamp getStampByIndex(int index) {
            return Player.instance.getZone().getSpeakerByIndex(index);
        }
    }

    private static int itemSpacing = 5;

    private StampPaletteComponent speakersComponent;
    private BaseMenuComponent component;

    public SpeakerListComponent() {
        this.speakersComponent = new StampPaletteComponent(new InternalListComponentProvider());

        this.component = new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, SpeakerListComponent.itemSpacing)
            .addComponent(
                new BoxComponent(
                    this.speakersComponent,
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
