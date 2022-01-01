package renderengine.renderer;

import org.lwjgl.opengl.GL11;
import renderengine.entities.Entity;
import renderengine.gui.font.Text;
import renderengine.shader.ShaderProgram;
import renderengine.utils.Transformation;

import java.util.List;

/**
 * @author Mirco Werner
 */
public class FontRenderer extends ARenderer {
    public FontRenderer() throws Exception {
        super();
    }

    @Override
    protected String getVertexShaderResource() {
        return "shaders/text_vertex.glsl";
    }

    @Override
    protected String getFragmentShaderResource() {
        return "shaders/text_fragment.glsl";
    }

    @Override
    protected void createShaderUniforms(ShaderProgram shaderProgram) throws Exception {
        shaderProgram.createUniform("transformationMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("color");
    }

    @Override
    protected void setAdditionalUniforms(ShaderProgram shaderProgram) {
        shaderProgram.setUniform("texture_sampler", 0);
    }

    @Override
    protected void setAdditionalUniformsForEachEntity(ShaderProgram shaderProgram, Entity entity) {

    }

    public void render(List<Text> characters) {
        shaderProgram.bind();

        setAdditionalUniforms(shaderProgram);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (Text text : characters) {
            shaderProgram.setUniform("transformationMatrix", Transformation.getTransformationMatrix(text));
            shaderProgram.setUniform("color", text.getColor());
            text.getMesh2D().render();
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        shaderProgram.unbind();
    }
}
