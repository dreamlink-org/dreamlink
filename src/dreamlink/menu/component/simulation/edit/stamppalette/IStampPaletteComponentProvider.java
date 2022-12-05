package dreamlink.menu.component.simulation.edit.stamppalette;

import dreamlink.zone.IStamp;

public interface IStampPaletteComponentProvider  {

    public int getTotalStampCount();

    public IStamp getStampByIndex(int index);
    
}
