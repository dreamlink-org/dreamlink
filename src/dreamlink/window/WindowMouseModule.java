package dreamlink.window;

import org.joml.Vector2f;
import org.joml.Vector2i;

import dreamlink.Config;
import dreamlink.simulation.Simulation;

public class WindowMouseModule {

    public Vector2i position;
    public Vector2f deltaPosition;
    private Window window;
    private boolean isCentered;

    public WindowMouseModule(Window window) {
        this.window = window;
        this.position = new Vector2i();
        this.deltaPosition = new Vector2f();
    }

    public void fetch() {
        var glfwModule = this.window.getGLFWModule();
        glfwModule.getMousePosition(this.position);

        this.position.set(
            (int)((float)this.position.x * Config.instance.resolution.x / glfwModule.windowDimensions.x),
            (int)((float)this.position.y * Config.instance.resolution.y / glfwModule.windowDimensions.y)
        );


        var gameState = Simulation.instance.getGameState();
        if(gameState.showCursor()) {
            this.deltaPosition.set(0f);
            this.isCentered = false;
        } else {
            var windowDimensions = this.window.glfwModule.windowDimensions;
            var centeredPosition = new Vector2i(
                windowDimensions.x / 2,
                windowDimensions.y / 2
            );

            glfwModule.setMousePosition(centeredPosition);
            if(this.isCentered) {
                this.deltaPosition.set(
                    (float)this.position.x / Config.instance.resolution.x - 0.5f,
                    (float)this.position.y / Config.instance.resolution.y - 0.5f
                );
            }
            this.isCentered = true;
        }
    }

}
