package planet.model;

import org.joml.SimplexNoise;
import org.joml.Vector3f;

/**
 * Earth like planet.
 *
 * @author Mirco Werner
 */
public class Earth extends APlanet {
    public Earth() {
        super(1000, 1000);
    }

    @Override
    protected void createPlanet() {
        vertices = new float[pointPhi * pointTheta * 3];
        normals = new float[pointPhi * pointTheta * 3];
        colors = new float[pointPhi * pointTheta * 3];
        indices = new int[pointPhi * pointTheta * 6];
        int vertexPointer = 0;
        for (int phi = 0; phi < pointPhi; phi++) {
            for (int theta = 0; theta < pointTheta; theta++) {
                // vertices
                float actualPhi = lerp((float) (-Math.PI / 2f), (float) (Math.PI / 2f), phi / (float) (pointPhi - 1));
                float actualTheta = lerp(0, (float) (2 * Math.PI), theta / (float) pointTheta);
                Vector3f xyz = toXYZ(1, actualPhi, actualTheta);
                float actualR =
                        SimplexNoise.noise(xyz.x, xyz.y, xyz.z)
                                + 0.5f * SimplexNoise.noise(xyz.x * 2f, xyz.y * 2f, xyz.z * 2f)
                                + 0.25f * SimplexNoise.noise(xyz.x * 4f, xyz.y * 4f, xyz.z * 4f)
                                + 0.125f * SimplexNoise.noise(xyz.x * 8f, xyz.y * 8f, xyz.z * 8f);
                Vector3f actualXYZ = toXYZ(Math.max(actualR + 10, 10), actualPhi, actualTheta);
                vertices[vertexPointer * 3] = actualXYZ.x;
                vertices[vertexPointer * 3 + 1] = actualXYZ.y;
                vertices[vertexPointer * 3 + 2] = actualXYZ.z;

                // colors
                Vector3f color = toColor(actualR);
                colors[vertexPointer * 3] = color.x;
                colors[vertexPointer * 3 + 1] = color.y;
                colors[vertexPointer * 3 + 2] = color.z;

                // normals
                Vector3f normal = new Vector3f(xyz).normalize();
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                vertexPointer++;
            }
        }

        vertexPointer = 0;
        for (int phi = 0; phi < pointPhi - 1; phi++) {
            for (int theta = 0; theta < pointTheta; theta++) {
                int bottomLeft = phi * pointTheta + theta;
                int bottomRight = bottomLeft + 1;
                int topLeft = bottomLeft + pointTheta;
                int topRight = topLeft + 1;
                if (theta == pointTheta - 1) {
                    bottomRight = bottomLeft + 1 - pointTheta;
                    topRight = topLeft + 1 - pointTheta;
                }

                indices[vertexPointer * 6] = bottomLeft;
                indices[vertexPointer * 6 + 1] = topRight;
                indices[vertexPointer * 6 + 2] = topLeft;

                indices[vertexPointer * 6 + 3] = bottomLeft;
                indices[vertexPointer * 6 + 4] = bottomRight;
                indices[vertexPointer * 6 + 5] = topRight;

                vertexPointer++;
            }
        }

        vertexPointer = 0;
        for (int phi = 1; phi < pointPhi - 1; phi++) {
            for (int theta = 0; theta < pointTheta; theta++) {
                // normals
                Vector3f normal = calculateNormal(phi, theta);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                vertexPointer++;
            }
        }
    }

    @Override
    public boolean emitsLight() {
        return false;
    }

    private static final Vector3f SNOW = new Vector3f(1, 1, 1);
    private static final Vector3f STONE = new Vector3f(0.6f, 0.6f, 0.6f);
    private static final Vector3f GRASS = new Vector3f(0, 1, 0);
    private static final Vector3f SAND = new Vector3f(1, 1, 0);
    private static final Vector3f WATER = new Vector3f(0, 0, 1);

    private Vector3f toColor(float r) {
//        if (r > 0f) {
//            return new Vector3f(r, r, r);
//        }
//        return lerp(WATER, new Vector3f(0, 0, 0), -r);

        if (r > 0f) {
            return lerp(new Vector3f(GRASS).mul(r), new Vector3f(SNOW).mul(r), r);
        }
        return lerp(WATER, new Vector3f(0, 0, 0), -r);

//        if (r > 0.9f) {
//            // snow
//            return lerp(SNOW, STONE, (1f - r) * 10);
//        } else if (r > 0.8) {
//            // stone
//            return lerp(STONE, GRASS, (0.9f - r) * 10);
//        } else if (r > 0.6) {
//            // grass
//            return lerp(GRASS, SAND, (0.8f - r) * 5);
//        } else if (r > 0.1) {
//            // sand
//            return lerp(SAND, WATER, (0.6f - r) * 2);
//        } else {
//            // water
//            return new Vector3f(0, 0, 1);
//        }
    }
}
