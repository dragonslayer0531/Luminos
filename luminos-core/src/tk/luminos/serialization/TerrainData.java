package tk.luminos.serialization;

import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.TerrainTexturePack;
import static tk.luminos.gameobjects.Terrain.VERTEX_COUNT;

public class TerrainData {

    public float[] vertices;
    public float[] textures;
    public float[] normals;
    public int[] indices;
    public float x;
    public float z;
    public TerrainTexturePack texturePack;

    public Terrain toTerrain() {
        Terrain t = new Terrain(x, z, vertices, normals, textures, indices, texturePack);
        t.heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                t.heights[j][i] = vertices[3 * (i * VERTEX_COUNT + j) + 1];
            }
        }
        t.genBlendMap();
        return t;
    }

}
