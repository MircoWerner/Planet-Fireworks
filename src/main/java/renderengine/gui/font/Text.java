package renderengine.gui.font;

import org.joml.Vector2f;
import org.joml.Vector3f;
import renderengine.engine.Window;
import renderengine.mesh.FontMesh2D;
import renderengine.mesh.Texture;

/**
 * This class needs work!
 *
 * @author Mirco Werner
 */
public class Text {
    private final String text;
    private final float fontSize;

    protected final Vector2f position = new Vector2f(0.0f, 0.0f);
    protected final Vector2f scale = new Vector2f(1.0f, 1.0f);
    private final FontMesh2D mesh2D;

    protected final Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);

    public Text(Window window, String text, float fontSize) throws Exception {
        this.text = text;
        this.fontSize = fontSize;

        Character.initialize();
        Texture font = Texture.loadTexture("fonts/arial.png");

        float[] vertices = new float[12 * text.length()];
        float[] texVertices = new float[12 * text.length()];

        float xCursor = 0;
        for (int i = 0; i < text.length(); i++) {
            Character character = Character.characters.get(text.charAt(i));

            Vector2f topLeft = new Vector2f(character.charPosition.x / (float) font.getWidth(), character.charPosition.y / (float) font.getHeight());
            float xInc = character.charDimension.x / (float) font.getWidth();
            float yInc = character.charDimension.y / (float) font.getHeight();
            Vector2f topRight = new Vector2f(topLeft.x + xInc, topLeft.y);
            Vector2f bottomRight = new Vector2f(topLeft.x + xInc, topLeft.y + yInc);
            Vector2f bottomLeft = new Vector2f(topLeft.x, topLeft.y + yInc);

            vertices[i * 12] = xCursor;
            vertices[i * 12 + 1] = yInc;
            vertices[i * 12 + 2] = xCursor;
            vertices[i * 12 + 3] = 0;
            vertices[i * 12 + 4] = xCursor + xInc / 2.f;
            vertices[i * 12 + 5] = yInc;

            vertices[i * 12 + 6] = xCursor + xInc / 2.f;
            vertices[i * 12 + 7] = 0;
            vertices[i * 12 + 8] = xCursor + xInc / 2.f;
            vertices[i * 12 + 9] = yInc;
            vertices[i * 12 + 10] = xCursor;
            vertices[i * 12 + 11] = 0;

            texVertices[i * 12] = topLeft.x;
            texVertices[i * 12 + 1] = topLeft.y;
            texVertices[i * 12 + 2] = bottomLeft.x;
            texVertices[i * 12 + 3] = bottomLeft.y;
            texVertices[i * 12 + 4] = topRight.x;
            texVertices[i * 12 + 5] = topRight.y;

            texVertices[i * 12 + 6] = bottomRight.x;
            texVertices[i * 12 + 7] = bottomRight.y;
            texVertices[i * 12 + 8] = topRight.x;
            texVertices[i * 12 + 9] = topRight.y;
            texVertices[i * 12 + 10] = bottomLeft.x;
            texVertices[i * 12 + 11] = bottomLeft.y;

            xCursor += character.charXAdvance / (float) font.getWidth() / 2.2f;
        }

        mesh2D = new FontMesh2D(
                vertices,
                texVertices,
                font
        );

        onWindowResized(window);
    }

    private static final float extendX = 16f * 5;
    private static final float extendY = 9f * 5;

    public void onWindowResized(Window window) {
        scale.set((fontSize * extendX) / (float) window.getWidth(), (fontSize * extendY) / (float) window.getHeight());
    }

    public FontMesh2D getMesh2D() {
        return mesh2D;
    }

    public void cleanUp() {
        mesh2D.cleanUp();
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setScale(float x, float y) {
        scale.set(x, y);
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float x, float y, float z) {
        color.set(x, y, z);
    }
}
