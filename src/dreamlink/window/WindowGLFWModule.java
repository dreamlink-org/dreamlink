package dreamlink.window;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.system.MemoryUtil;

import dreamlink.Config;

public class WindowGLFWModule {

    private static String windowTitle = "DreamLink";

    public long windowID;

    private double[] cursorXBuffer;
    private double[] cursorYBuffer;
    public Vector2i windowDimensions;

    public WindowGLFWModule() {
        this.cursorXBuffer = new double[1];
        this.cursorYBuffer = new double[1];
        this.windowDimensions = new Vector2i();
    }

    public void setup() {
        GLFWErrorCallback.createPrint(System.err).set();
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW.GLFW_FALSE);

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        var primaryMonitorID = GLFW.glfwGetPrimaryMonitor();
        var vidMode = GLFW.glfwGetVideoMode(primaryMonitorID);
        this.windowDimensions.set(vidMode.width(), vidMode.height());
        this.windowID = GLFW.glfwCreateWindow(
            this.windowDimensions.x,
            this.windowDimensions.y,
            WindowGLFWModule.windowTitle,
            Config.instance.borderlessWindowedMode ? MemoryUtil.NULL : primaryMonitorID,
            MemoryUtil.NULL
        );

        //get window size
        var windowXBuffer = new int[1];
        var windowYBuffer = new int[1];
        GLFW.glfwGetWindowSize(this.windowID, windowXBuffer, windowYBuffer);
        this.windowDimensions.set(windowXBuffer[0], windowYBuffer[0]);
        System.out.println("Window size: " + this.windowDimensions.x + "x" + this.windowDimensions.y);

        GLFW.glfwMakeContextCurrent(this.windowID);
        GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(this.windowID);
        GLFW.glfwSetInputMode(this.windowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public Vector2i getResolution() {
        return this.windowDimensions;
    }

    public void setShouldClose() {
        GLFW.glfwSetWindowShouldClose(this.windowID, true);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.windowID);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        GLFW.glfwSetKeyCallback(this.windowID, callback);
    }

    public void setCharCallback(GLFWCharCallbackI callback) {
        GLFW.glfwSetCharCallback(this.windowID, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        GLFW.glfwSetMouseButtonCallback(this.windowID, callback);
    }

    public void getMousePosition(Vector2i mousePosition) {
        GLFW.glfwGetCursorPos(
            this.windowID, 
            this.cursorXBuffer, 
            this.cursorYBuffer
        );

        mousePosition.set(
            (int)this.cursorXBuffer[0], 
            (int)this.cursorYBuffer[0]
        );
    }

    public void setMousePosition(Vector2i mousePosition) {
        GLFW.glfwSetCursorPos(
            this.windowID, 
            mousePosition.x, 
            mousePosition.y
        );
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(this.windowID);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        GLFW.glfwTerminate();
    }

}
