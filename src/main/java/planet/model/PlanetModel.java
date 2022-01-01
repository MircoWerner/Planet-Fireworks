package planet.model;

import renderengine.camera.ACamera;
import renderengine.engine.Window;
import renderengine.entities.Entity;
import renderengine.entities.Light;
import renderengine.shader.ShaderProgram;
import renderengine.utils.Transformation;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Model of the planet that can be rendered.
 *
 * @author Mirco Werner
 */
public class PlanetModel {
    private ShaderProgram shaderProgram;

    private int vaoId;
    private int vertexVboId;
    private int colorVboId;
    private int normalVboId;
    private int indicesVboId;

    private int count;

    private final Entity entity = new Entity();

    private final boolean emitsLight;

    /**
     * Creates the planet.
     *
     * @param planet planet data
     * @throws Exception if initialization fails
     */
    public PlanetModel(APlanet planet) throws Exception {
        emitsLight = planet.emitsLight();
        init(planet);
    }

    private void init(APlanet planet) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader("shaders/planet_vert.glsl");
        shaderProgram.createFragmentShader("shaders/planet_frag.glsl");
        shaderProgram.link();
        shaderProgram.createUniform("transformationMatrix");
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("viewMatrix");
        shaderProgram.createUniform("lightPosition");
        shaderProgram.createUniform("lightColor");
        shaderProgram.createUniform("isLightSource");
        shaderProgram.unbind();

        // VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // vertex position vbo
        vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, planet.getVertices(), GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // color vbo
        colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        glBufferData(GL_ARRAY_BUFFER, planet.getColors(), GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // normal vbo
        normalVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, normalVboId);
        glBufferData(GL_ARRAY_BUFFER, planet.getNormals(), GL_STATIC_DRAW);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // index vbo
        indicesVboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, planet.getIndices(), GL_STATIC_DRAW);

        // unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        count = planet.getIndices().length;
    }

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    /**
     * Renders the planet.
     *
     * @param window window
     * @param camera camera
     * @param light  light
     */
    public void render(Window window, ACamera camera, Light light) {
        shaderProgram.bind();

        shaderProgram.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        shaderProgram.setUniform("projectionMatrix", Transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR));
        shaderProgram.setUniform("transformationMatrix", Transformation.getTransformationMatrix(entity));
        shaderProgram.setUniform("lightPosition", light.getPosition());
        shaderProgram.setUniform("lightColor", light.getColor());
        shaderProgram.setUniform("isLightSource", emitsLight ? 1 : 0);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    /**
     * Frees all memory allocated for the buffers.
     */
    public void cleanUp() {
        glDeleteBuffers(vertexVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(normalVboId);
        glDeleteBuffers(indicesVboId);
        glDeleteVertexArrays(vaoId);
        shaderProgram.cleanUp();
    }

    public Entity getEntity() {
        return entity;
    }
}
