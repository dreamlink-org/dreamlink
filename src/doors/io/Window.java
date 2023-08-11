package doors.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL42;
import org.lwjgl.system.MemoryUtil;

import doors.utility.vector.Vector2fl;
import doors.utility.vector.Vector2in;

public class Window {

    private static String TITLE = "Doors";

    public static Window WINDOW = new Window();

    public Vector2in dimensions = new Vector2in();
    public long windowID;
    public long currentTick;
    
    public Vector2fl cursorPosition = new Vector2fl();
    private double[] cursorXPositionBuffer = new double[1];
    private double[] cursorYPositionBuffer = new double[1];

    public Window() {
        GLFWErrorCallback.createPrint(System.err).set();
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        var primaryMonitorID = GLFW.glfwGetPrimaryMonitor();
        var vidMode = GLFW.glfwGetVideoMode(primaryMonitorID);
        this.dimensions.set(vidMode.width(), vidMode.height());
        this.windowID = GLFW.glfwCreateWindow(this.dimensions.x, this.dimensions.y, TITLE, primaryMonitorID, MemoryUtil.NULL);

        GLFW.glfwMakeContextCurrent(this.windowID);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.windowID);

        GL.createCapabilities();
        GL42.glClearColor(0f, 0f, 0f, 1f);
        GL42.glEnable(GL42.GL_BLEND);
        GL42.glEnable(GL42.GL_DEPTH_TEST);
        GL42.glBlendFunc(GL42.GL_SRC_ALPHA, GL42.GL_ONE_MINUS_SRC_ALPHA);
        GL42.glPixelStorei(GL42.GL_UNPACK_ALIGNMENT, 1);
        GLFW.glfwSetInputMode(this.windowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);

        // Debug stuff to toggle on/off if stuff breaks
        // GL42.glDisable(GL42.GL_CULL_FACE);
        // GL42.glPolygonMode(GL42.GL_FRONT_AND_BACK, GL42.GL_LINE);
    }

    public void noop() {
        // This no-op function call allows us to force the Window singleton
        // to be initialized earlier than it would be otherwise. This is important
        // as without this class, other GLFW/GL calls will fail.
    }

    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(this.windowID, GLFW.GLFW_FOCUSED) == 1;
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

    public void update() {
        this.currentTick = System.currentTimeMillis();
        GLFW.glfwSwapBuffers(this.windowID);
        GLFW.glfwPollEvents();
        GLFW.glfwGetCursorPos(this.windowID, this.cursorXPositionBuffer, this.cursorYPositionBuffer);
        this.cursorPosition.set(
            (float)this.cursorXPositionBuffer[0],
            (float)this.cursorYPositionBuffer[0]
        ).div(this.dimensions);
    }

}
