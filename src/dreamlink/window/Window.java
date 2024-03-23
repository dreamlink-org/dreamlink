package dreamlink.window;

import dreamlink.window.button.Button;
import dreamlink.window.button.WindowButtonModule;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class Window {

    public static Window instance = new Window();

    public WindowButtonModule buttonModule = new WindowButtonModule(this);
    public WindowMouseModule mouseModule = new WindowMouseModule(this);
    public WindowTypedCharacterModule typedCharacterModule = new WindowTypedCharacterModule(this);
    public WindowGLCapabilitiesModule glCapabilitiesModule = new WindowGLCapabilitiesModule();
    public WindowALCapabilitiesModule alCapabilitiesModule = new WindowALCapabilitiesModule();
    public WindowGLFWModule glfwModule = new WindowGLFWModule();

    public void setup() {
        this.glfwModule.setup();
        this.buttonModule.setup();
        this.typedCharacterModule.setup();
        this.glCapabilitiesModule.setup();
        this.alCapabilitiesModule.setup();
    }

    public WindowGLFWModule getGLFWModule() {
        return this.glfwModule;
    }

    public Vector2i getResolution() {
        return this.glfwModule.getResolution();
    }

    public long getWindowID() {
        return this.glfwModule.windowID;
    }

    public void setShouldClose() {
        this.glfwModule.setShouldClose();
    }

    public boolean shouldClose() {
        return this.glfwModule.shouldClose();
    }

    public boolean isButtonDown(Button button) {
        return this.buttonModule.isButtonDown(button);
    }

    public boolean isButtonPressed(Button button) {
        return this.buttonModule.isButtonPressed(button);
    }

    public boolean isButtonReleased(Button button) {
        return this.buttonModule.isButtonReleased(button);
    }

    public Iterable<Button> getPressedButtons() {
        return this.buttonModule.getPressedButtons();
    }

    public Iterable<Character> getCharacters() {
        return this.typedCharacterModule.getTypedCharacters();
    }

    public Vector2i getWindowDimensions() {
        return this.glfwModule.windowDimensions;
    }

    public Vector2f getDeltaMousePosition() {
        return this.mouseModule.deltaPosition;
    }

    public Vector2i getMousePosition() {
        return this.mouseModule.position;
    }

    public void handleUserInputs() {
        this.buttonModule.advance();
        this.typedCharacterModule.advance();
        this.glfwModule.pollEvents();
        this.mouseModule.fetch();
    }

    public void swapBuffers() {
        this.glfwModule.swapBuffers();
    }

    public void destroy() {
        this.alCapabilitiesModule.destroy();
        this.glfwModule.destroy();
    }

}
