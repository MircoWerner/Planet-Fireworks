package planet;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import planet.firework.FireworkModel;
import planet.model.Earth;
import planet.model.PlanetModel;
import planet.model.Sun;
import renderengine.camera.ThirdPersonCamera;
import renderengine.engine.IRenderLogic;
import renderengine.engine.MouseInput;
import renderengine.engine.Window;
import renderengine.entities.Light;
import renderengine.gui.font.Text;
import renderengine.renderer.FontRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The methods of this class are called for initialization of the rendering and for every render iteration.
 *
 * @author Mirco Werner
 */
public class PlanetRenderLogic implements IRenderLogic {
    private ThirdPersonCamera camera;
    private final Vector3f cameraInc;
    private static final float MOUSE_SENSITIVITY = 0.25f;
    private static final float CAMERA_POS_STEP = 3f;

    private Light light;

    private PlanetModel earth;
    private PlanetModel sun;
    private float sunPhi = 0f;
    private final float sunR = 200f;

    private final List<FireworkModel> fireworkModels = new ArrayList<>();
    private final Random random = new Random(System.currentTimeMillis());

    private FontRenderer fontRenderer;
    private final List<Text> texts = new ArrayList<>();

    public PlanetRenderLogic() {
        cameraInc = new Vector3f();
    }

    /**
     * This method is called once after the window is created.
     * Creates the planets.
     *
     * @param window window of the application
     * @throws Exception if an exception occurs in the initialization process
     */
    @Override
    public void init(Window window) throws Exception {
        camera = new ThirdPersonCamera();

        light = new Light(new Vector3f(sunR, 0, 0), new Vector3f(1f, 1f, 1f)); // white light

        earth = new PlanetModel(new Earth());
        sun = new PlanetModel(new Sun());
        sun.getEntity().setPosition(sunR, 0, 0);

        for (int i = 0; i < 10; i++) {
            fireworkModels.add(new FireworkModel(random));
        }

        fontRenderer = new FontRenderer();
        Text text = new Text(window, "HAPPY NEW YEAR!", 8);
        text.setPosition(-0.95f, -0.95f);
        text.setColor(1, 1, 0);
        texts.add(text);
    }

    /**
     * This method is called first in every iteration.
     * It handles the keyboard and mouse inputs for camera updates and handles GUI events.
     *
     * @param window     window of the application
     * @param mouseInput stores information about mouse events
     */
    @Override
    public void input(Window window, MouseInput mouseInput) {
        int factor = 1;
        if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            factor = 5;
        }
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -factor;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = factor;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -factor;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = factor;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -factor;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = factor;
        }

        float x = (float) (2 * mouseInput.getCurrentPos().x / (float) window.getWidth() - 1);
        float y = (float) -(2 * mouseInput.getCurrentPos().y / (float) window.getHeight() - 1);
        if (mouseInput.isOnLeftPressed()) {
//            userInterface.onMousePressed(Gui.Action.PRESS, x, y);
            mouseInput.setOnLeftPressedHandled();
        } else if (mouseInput.isOnLeftReleased()) {
//            userInterface.onMousePressed(Gui.Action.RELEASE, x, y);
            mouseInput.setOnLeftReleasedHandled();
        } else {
//            userInterface.onMousePressed(Gui.Action.HOVER, x, y);
        }
    }

    /**
     * This method is called second in every iteration.
     * It updates the camera based on the keyboard and mouse inputs.
     *
     * @param window             window of the application
     * @param timeSinceLastFrame time in milliseconds
     * @param mouseInput         stores information about mouse events
     */
    @Override
    public void update(Window window, long timeSinceLastFrame, MouseInput mouseInput) {
        float increment = timeSinceLastFrame / 1000f;

        // update camera position
        camera.moveCenter(cameraInc.x * CAMERA_POS_STEP * increment, cameraInc.y * CAMERA_POS_STEP * increment, cameraInc.z * CAMERA_POS_STEP * increment);

        // update camera based on mouse
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getMotionVec();
            camera.move(0.0f, rotVec.x * MOUSE_SENSITIVITY * increment, -rotVec.y * MOUSE_SENSITIVITY * increment);
        }
        if (mouseInput.getScrollVec().y != 0) {
            camera.move(-mouseInput.getScrollVec().y, 0.0f, 0.0f);
            mouseInput.resetScrollVec();
        }

        sunPhi += increment * 0.3f;
        if (sunPhi >= 2 * Math.PI) {
            sunPhi = 0;
        }
        light.setPosition(new Vector3f((float) (Math.cos(sunPhi) * sunR), 0, (float) (Math.sin(sunPhi) * sunR)));
        sun.getEntity().setPosition((float) (Math.cos(sunPhi) * sunR), 0, (float) (Math.sin(sunPhi) * sunR));

        fireworkModels.forEach(fireworkModel -> fireworkModel.update(increment));
    }

    /**
     * This method is called last in every iteration.
     * It renders all entities in the scene.
     *
     * @param window window of the application
     */
    @Override
    public void render(Window window) {
        if (window.isKeyPressed(GLFW_KEY_T)) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // enable wireframe rendering
        }
        earth.render(window, camera, light);
        sun.render(window, camera, light);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        fireworkModels.forEach(fireworkModel -> fireworkModel.render(window, camera));
        GL11.glDisable(GL11.GL_BLEND);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        fontRenderer.render(texts);
    }

    /**
     * This method is called when the window is closed.
     * The renderers are cleaned up.
     */
    @Override
    public void cleanUp() {
        earth.cleanUp();
        sun.cleanUp();
        fireworkModels.forEach(FireworkModel::cleanUp);

        fontRenderer.cleanUp();
        texts.forEach(Text::cleanUp);
    }

    /**
     * This method is called when the window is resized.
     * The GUI will be resized properly.
     *
     * @param window window of the application
     */
    @Override
    public void onWindowResized(Window window) {
        texts.forEach(text -> text.onWindowResized(window));
    }
}
