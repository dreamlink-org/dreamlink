package dreamlink.window.button;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

public class Button {

    private static Map<String, Button> buttonMap = new HashMap<>();
    private static Map<Integer, Button> buttonIDMap = new HashMap<>();

    public static Button getButton(String name) {
        return Button.buttonMap.get(name);
    }

    public static Button getButton(int buttonID) {
        return Button.buttonIDMap.get(buttonID);
    }

    public static Button mouseLeft = new Button("mouse_left", GLFW.GLFW_MOUSE_BUTTON_LEFT, ButtonType.mouse);
    public static Button mouseRight = new Button("mouse_right", GLFW.GLFW_MOUSE_BUTTON_RIGHT, ButtonType.mouse);

    // all 26 characters
    public static Button keyA = new Button("key_a", GLFW.GLFW_KEY_A, ButtonType.keyboard);
    public static Button keyB = new Button("key_b", GLFW.GLFW_KEY_B, ButtonType.keyboard);
    public static Button keyC = new Button("key_c", GLFW.GLFW_KEY_C, ButtonType.keyboard);
    public static Button keyD = new Button("key_d", GLFW.GLFW_KEY_D, ButtonType.keyboard);
    public static Button keyE = new Button("key_e", GLFW.GLFW_KEY_E, ButtonType.keyboard);
    public static Button keyF = new Button("key_f", GLFW.GLFW_KEY_F, ButtonType.keyboard);
    public static Button keyG = new Button("key_g", GLFW.GLFW_KEY_G, ButtonType.keyboard);
    public static Button keyH = new Button("key_h", GLFW.GLFW_KEY_H, ButtonType.keyboard);
    public static Button keyI = new Button("key_i", GLFW.GLFW_KEY_I, ButtonType.keyboard);
    public static Button keyJ = new Button("key_j", GLFW.GLFW_KEY_J, ButtonType.keyboard);
    public static Button keyK = new Button("key_k", GLFW.GLFW_KEY_K, ButtonType.keyboard);
    public static Button keyL = new Button("key_l", GLFW.GLFW_KEY_L, ButtonType.keyboard);
    public static Button keyM = new Button("key_m", GLFW.GLFW_KEY_M, ButtonType.keyboard);
    public static Button keyN = new Button("key_n", GLFW.GLFW_KEY_N, ButtonType.keyboard);
    public static Button keyO = new Button("key_o", GLFW.GLFW_KEY_O, ButtonType.keyboard);
    public static Button keyP = new Button("key_p", GLFW.GLFW_KEY_P, ButtonType.keyboard);
    public static Button keyQ = new Button("key_q", GLFW.GLFW_KEY_Q, ButtonType.keyboard);
    public static Button keyR = new Button("key_r", GLFW.GLFW_KEY_R, ButtonType.keyboard);
    public static Button keyS = new Button("key_s", GLFW.GLFW_KEY_S, ButtonType.keyboard);
    public static Button keyT = new Button("key_t", GLFW.GLFW_KEY_T, ButtonType.keyboard);
    public static Button keyU = new Button("key_u", GLFW.GLFW_KEY_U, ButtonType.keyboard);
    public static Button keyV = new Button("key_v", GLFW.GLFW_KEY_V, ButtonType.keyboard);
    public static Button keyW = new Button("key_w", GLFW.GLFW_KEY_W, ButtonType.keyboard);
    public static Button keyX = new Button("key_x", GLFW.GLFW_KEY_X, ButtonType.keyboard);
    public static Button keyY = new Button("key_y", GLFW.GLFW_KEY_Y, ButtonType.keyboard);
    public static Button keyZ = new Button("key_z", GLFW.GLFW_KEY_Z, ButtonType.keyboard);

    public static Button key0 = new Button("key_0", GLFW.GLFW_KEY_0, ButtonType.keyboard);
    public static Button key1 = new Button("key_1", GLFW.GLFW_KEY_1, ButtonType.keyboard);
    public static Button key2 = new Button("key_2", GLFW.GLFW_KEY_2, ButtonType.keyboard);
    public static Button key3 = new Button("key_3", GLFW.GLFW_KEY_3, ButtonType.keyboard);
    public static Button key4 = new Button("key_4", GLFW.GLFW_KEY_4, ButtonType.keyboard);
    public static Button key5 = new Button("key_5", GLFW.GLFW_KEY_5, ButtonType.keyboard);
    public static Button key6 = new Button("key_6", GLFW.GLFW_KEY_6, ButtonType.keyboard);
    public static Button key7 = new Button("key_7", GLFW.GLFW_KEY_7, ButtonType.keyboard);
    public static Button key8 = new Button("key_8", GLFW.GLFW_KEY_8, ButtonType.keyboard);
    public static Button key9 = new Button("key_9", GLFW.GLFW_KEY_9, ButtonType.keyboard);

    public static Button keyF1 = new Button("key_f1", GLFW.GLFW_KEY_F1, ButtonType.keyboard);
    public static Button keyF2 = new Button("key_f2", GLFW.GLFW_KEY_F2, ButtonType.keyboard);
    public static Button keyF3 = new Button("key_f3", GLFW.GLFW_KEY_F3, ButtonType.keyboard);
    public static Button keyF4 = new Button("key_f4", GLFW.GLFW_KEY_F4, ButtonType.keyboard);
    public static Button keyF5 = new Button("key_f5", GLFW.GLFW_KEY_F5, ButtonType.keyboard);
    public static Button keyF6 = new Button("key_f6", GLFW.GLFW_KEY_F6, ButtonType.keyboard);
    public static Button keyF7 = new Button("key_f7", GLFW.GLFW_KEY_F7, ButtonType.keyboard);
    public static Button keyF8 = new Button("key_f8", GLFW.GLFW_KEY_F8, ButtonType.keyboard);
    public static Button keyF9 = new Button("key_f9", GLFW.GLFW_KEY_F9, ButtonType.keyboard);
    public static Button keyF10 = new Button("key_f10", GLFW.GLFW_KEY_F10, ButtonType.keyboard);
    public static Button keyF11 = new Button("key_f11", GLFW.GLFW_KEY_F11, ButtonType.keyboard);
    public static Button keyF12 = new Button("key_f12", GLFW.GLFW_KEY_F12, ButtonType.keyboard);

    public static Button keyEscape = new Button("key_escape", GLFW.GLFW_KEY_ESCAPE, ButtonType.keyboard);
    public static Button keySpace = new Button("key_space", GLFW.GLFW_KEY_SPACE, ButtonType.keyboard);
    public static Button keyBackspace = new Button("key_backspace", GLFW.GLFW_KEY_BACKSPACE, ButtonType.keyboard);
    public static Button keyHome = new Button("key_home", GLFW.GLFW_KEY_HOME, ButtonType.keyboard);
    public static Button keyEnd = new Button("key_end", GLFW.GLFW_KEY_END, ButtonType.keyboard);
    public static Button keyDelete = new Button("key_delete", GLFW.GLFW_KEY_DELETE, ButtonType.keyboard);

    public static Button keyLeft = new Button("key_left", GLFW.GLFW_KEY_LEFT, ButtonType.keyboard);
    public static Button keyRight = new Button("key_right", GLFW.GLFW_KEY_RIGHT, ButtonType.keyboard);
    public static Button keyUp = new Button("key_up", GLFW.GLFW_KEY_UP, ButtonType.keyboard);
    public static Button keyDown = new Button("key_down", GLFW.GLFW_KEY_DOWN, ButtonType.keyboard);

    public static Button keyLeftShift = new Button("key_left_shift", GLFW.GLFW_KEY_LEFT_SHIFT, ButtonType.keyboard);
    public static Button keyLeftCtrl = new Button("key_left_ctrl", GLFW.GLFW_KEY_LEFT_CONTROL, ButtonType.keyboard);
    public static Button keyLeftAlt = new Button("key_left_alt", GLFW.GLFW_KEY_LEFT_ALT, ButtonType.keyboard);
    public static Button keyTab = new Button("key_tab", GLFW.GLFW_KEY_TAB, ButtonType.keyboard);
    public static Button keyEnter = new Button("key_enter", GLFW.GLFW_KEY_ENTER, ButtonType.keyboard);

    public int uniformButtonID;
    public String name;

    public Button(String name, int buttonID, ButtonType buttonType) {
        this.name = name;
        this.uniformButtonID = buttonID + buttonType.offset;
        Button.buttonMap.put(this.name, this);
        Button.buttonIDMap.put(this.uniformButtonID, this);
    }
    
}
