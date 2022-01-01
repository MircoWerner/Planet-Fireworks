package planet.model;

import org.joml.SimplexNoise;
import org.joml.Vector3f;

/**
 * This class contains mesh data of a planet.
 *
 * @author Mirco Werner
 */
public abstract class APlanet {
    protected float[] vertices;
    protected float[] normals;
    protected float[] colors;
    protected int[] indices;

    protected final int pointPhi;
    protected final int pointTheta;

    /**
     * Creates the planet.
     *
     * @param pointPhi   amount of points vertically
     * @param pointTheta amount of points horizontally
     */
    public APlanet(int pointPhi, int pointTheta) {
        this.pointPhi = pointPhi;
        this.pointTheta = pointTheta;
        createPlanet();
    }

    /**
     * Creates the mesh data of the planet.
     */
    protected abstract void createPlanet();

    /**
     * Returns SimplexNoise in [0,1] range.
     *
     * @param x x
     * @param y y
     * @param z z
     * @return SimplexNoise in [0,1]
     */
    protected float noise(float x, float y, float z) {
        return (SimplexNoise.noise(x, y, z) + 1) / 2f;
    }

    /**
     * Calculates the normal of a vertex based on the orientation of the adjacent triangles.
     *
     * @param phi   phi index
     * @param theta theta index
     * @return normal
     */
    protected Vector3f calculateNormal(int phi, int theta) {
        int index = phi * pointTheta + theta;
        int left = index - 1;
        int right = index + 1;
        int top = index + pointTheta;
        int bottom = index - pointTheta;

        if (theta == 0) {
            left = top - 1;
        }
        if (theta == pointTheta - 1) {
            right = bottom + 1;
        }

        int topLeft = left + pointTheta;
        int topRight = right + pointTheta;
        int bottomLeft = left - pointTheta;
        int bottomRight = right - pointTheta;

        Vector3f xyzSelf = getXYZ(index);

        Vector3f xyzLeft = getXYZ(left);
        Vector3f xyzRight = getXYZ(right);
        Vector3f xyzBottom = getXYZ(bottom);
        Vector3f xyzTop = getXYZ(top);

        Vector3f xyzTopLeft = getXYZ(topLeft);
        Vector3f xyzTopRight = getXYZ(topRight);
        Vector3f xyzBottomLeft = getXYZ(bottomLeft);
        Vector3f xyzBottomRight = getXYZ(bottomRight);

        Vector3f normal = new Vector3f(0);
        normal.add(calcNormal(xyzLeft, xyzBottomLeft, xyzBottom, xyzSelf));
        normal.add(calcNormal(xyzBottom, xyzBottomRight, xyzRight, xyzSelf));
        normal.add(calcNormal(xyzRight, xyzTopRight, xyzTop, xyzSelf));
        normal.add(calcNormal(xyzTop, xyzTopLeft, xyzLeft, xyzSelf));
        return normal.normalize();
    }

    private Vector3f calcNormal(Vector3f pos1, Vector3f pos2, Vector3f pos3, Vector3f position) {
        Vector3f p1 = new Vector3f(pos1).sub(position);
        Vector3f p2 = new Vector3f(pos2).sub(position);
        Vector3f p3 = new Vector3f(pos3).sub(position);
        return p1.cross(p2).add(p2.cross(p3));
    }

    /**
     * Returns the xyz coordinates of the vertex.
     *
     * @param index index
     * @return xyz coordinates
     */
    protected Vector3f getXYZ(int index) {
        return new Vector3f(vertices[index * 3], vertices[index * 3 + 1], vertices[index * 3 + 2]);
    }

    /**
     * Linear interpolation between a and b.
     *
     * @param a start
     * @param b end
     * @param t in [0,1]
     * @return a + t * (b-a)
     */
    protected float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    /**
     * Linear interpolation between a and b.
     *
     * @param a start
     * @param b end
     * @param t in [0,1]
     * @return a + t * (b-a)
     */
    protected Vector3f lerp(Vector3f a, Vector3f b, float t) {
        return new Vector3f(b).sub(a).mul(t).add(a);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getColors() {
        return colors;
    }

    public int[] getIndices() {
        return indices;
    }

    /**
     * Returns xyz coordinates on the sphere.
     *
     * @param r     radius
     * @param phi   phi
     * @param theta theta
     * @return xyz coordinates
     */
    protected Vector3f toXYZ(float r, float phi, float theta) {
        // better use fibonacci sphere for equally distributed points, but that makes triangulation much harder...
        // that is why we use normal sphere coordinates here :)
        float x = (float) (r * Math.cos(phi) * Math.sin(theta));
        float y = (float) (r * Math.sin(phi));
        float z = (float) (r * Math.cos(phi) * Math.cos(theta));
        return new Vector3f(x, y, z);
    }

    /**
     * Returns if this planet is a light source (i.e. sun).
     *
     * @return true if it is a light source, false otherwise
     */
    public abstract boolean emitsLight();
}
