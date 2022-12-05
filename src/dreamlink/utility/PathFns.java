package dreamlink.utility;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFns {

    public static Path sanitizeUserPath(String userPathStr) {
        if(userPathStr.startsWith("~")) {
            userPathStr = userPathStr.replaceFirst("~", System.getProperty("user.home"));
        }

        var userPath = Paths.get(userPathStr);
        return userPath.isAbsolute()
            ? userPath
            : Paths.get(System.getProperty("user.dir")).resolve(userPath);
    }
    
}
