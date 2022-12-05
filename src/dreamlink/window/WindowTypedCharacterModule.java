package dreamlink.window;

import java.util.ArrayList;
import java.util.List;

public class WindowTypedCharacterModule {

    private List<Character> typedCharacters;
    private Window window;

    public WindowTypedCharacterModule(Window window) {
        this.window = window;
        this.typedCharacters = new ArrayList<>();
    }

    private void onCharEvent(long window, int codePoint) {
        if(codePoint <= 127) {
            this.typedCharacters.add((char)codePoint);
        }
    }

    public void setup() {
        var glfwSystem = this.window.getGLFWModule();
        glfwSystem.setCharCallback(this::onCharEvent);
    }

    public Iterable<Character> getTypedCharacters() {
        return this.typedCharacters;
    }

    public void advance() {
        this.typedCharacters.clear();
    }

}
