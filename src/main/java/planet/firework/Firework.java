package planet.firework;

import org.joml.Vector2f;

/**
 * Firework particle data. Contains mesh information and the distribution of the particles.
 *
 * @author Mirco Werner
 */
public class Firework {
    private float[] vertices;
    private float[] phis;
    private float[] thetas;
    private int[] indices;

    /**
     * Create particle data.
     *
     * @param particles number of particles
     */
    public Firework(int particles) {
        createParticleMesh(particles);
    }

    private void createParticleMesh(int particles) {
        vertices =
                new float[]{
                        -0.5f, 0.5f, 0.5f,
                        0.5f, 0.5f, 0.5f,
                        0.5f, -0.5f, 0.5f,
                        -0.5f, -0.5f, 0.5f,

                        -0.5f, 0.5f, -0.5f,
                        0.5f, 0.5f, -0.5f,
                        0.5f, -0.5f, -0.5f,
                        -0.5f, -0.5f, -0.5f,
                };

        phis = new float[particles];
        thetas = new float[particles];
        for (int i = 0; i < particles; i++) {
            Vector2f phiTheta = toPhiTheta(i, particles);
            phis[i] = phiTheta.x;
            thetas[i] = phiTheta.y;
        }

        indices = new int[]{
                // front
                0, 3, 2,
                0, 2, 1,
                // back
                4, 6, 7,
                4, 5, 6,
                // top
                4, 0, 1,
                4, 1, 5,
                // bottom
                7, 2, 3,
                7, 6, 2,
                // left
                0, 7, 3,
                0, 4, 7,
                // right
                1, 2, 6,
                1, 6, 5,
        };
    }

    private Vector2f toPhiTheta(int i, int n) {
        float goldenRatio = (float) (1 + Math.sqrt(5)) / 2f;
        float theta = (float) (2 * Math.PI * i / goldenRatio);
        float phi = (float) Math.acos(1 - 2 * (i + 0.5) / n);
        return new Vector2f(phi, theta);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getPhis() {
        return phis;
    }

    public float[] getThetas() {
        return thetas;
    }

    public int[] getIndices() {
        return indices;
    }
}
