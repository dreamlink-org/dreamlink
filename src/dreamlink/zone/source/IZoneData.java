package dreamlink.zone.source;

import java.nio.file.Path;

public interface IZoneData extends AutoCloseable {

    public Path getPath();

    @Override
    public void close();
    
}
