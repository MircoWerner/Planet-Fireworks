package planet;

import renderengine.engine.IRenderLogic;
import renderengine.engine.RenderEngine;

/**
 * Main method of the program.
 * Creates the window and starts the rendering.
 *
 * @author Mirco Werner
 */
public class PlanetRender {
    public static void main(String[] args) {
        try {
            IRenderLogic renderLogic = new PlanetRenderLogic();
            RenderEngine renderEngine = new RenderEngine("Planet - Fireworks", 1280, 720, renderLogic);
            renderEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
