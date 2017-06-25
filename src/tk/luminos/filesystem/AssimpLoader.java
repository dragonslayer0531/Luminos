package tk.luminos.filesystem;

import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_AMBIENT;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_DIFFUSE;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_SPECULAR;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiGetMaterialTexture;
import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_FixInfacingNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.assimp.Assimp.aiTextureType_DIFFUSE;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;

import tk.luminos.graphics.Material;
import tk.luminos.graphics.Texture;
import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.models.ModelData;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.loaders.AssetCache;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.Vector4;
import tk.luminos.serialization.DBArray;
import tk.luminos.serialization.DBObject;
import tk.luminos.serialization.DBObjectType;
import tk.luminos.serialization.DBString;

/**
 * Loads models to the engine via Assimp
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class AssimpLoader {
	
	/**
	 * Flag to fix inward facing normals
	 */
	public static final int FIX_INFACING_NORMALS = aiProcess_FixInfacingNormals;
	
	/**
	 * Flag to join identical vertices in the model
	 */
	public static final int JOIN_IDENTICAL_VERTICES = aiProcess_JoinIdenticalVertices;
	
	/**
	 * Flag to triangulate all faces in the model
	 */
	public static final int TRIANGULATE = aiProcess_Triangulate;

	/**
	 * Loads a model file and a texture to a {@link TexturedModel}.  By default, this method joins identical vertices,
	 * triangulates the mesh, and fixes infacing normals.
	 * 
	 * @param modelPath			Path to the model
	 * @param texturePath		Path to the texture.  If the model contains a material library,
	 * 							the material library should point to the texture's folder.
	 * @return					Array of {@link TexturedModel}s that were contained by the
	 * 							model file.
	 */
	public static TexturedModel[] load(String modelPath, String texturePath) {
		return load(modelPath, texturePath, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
	}

	/**
	 * Loads a model file and a texture to a {@link TexturedModel}.  This method requires flags that will determine how
	 * how the mesh is handled.
	 * 
	 * @param modelPath			Path to the model
	 * @param texturePath		Path to the texture.  If the model contains a material library,
	 * 							the material library should point to the texture's folder.
	 * @param flags				Flags separated by the Binary OR operator, "|"
	 * @return					Array of {@link TexturedModel}s that were contained by the
	 * 							model file.
	 */
	public static TexturedModel[] load(String modelPath, String texturePath, int flags) {
		AIScene aiScene = aiImportFile(modelPath, flags);
		if (aiScene == null)
			throw new RuntimeException("Could not load model: " + modelPath + "\nWith flags " + flags);

		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		List<Material> materials = new ArrayList<Material>();
		for (int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMat(aiMaterial, materials, texturePath);
		}

		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		TexturedModel[] meshes = new TexturedModel[numMeshes];
		for (int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			TexturedModel mesh = processStaticMesh(aiMesh, materials);
			String name = modelPath.split("/")[modelPath.split("/").length - 1];
			String tex = texturePath.split("/")[texturePath.split("/").length - 1];
			DBObject obj = new DBObject(name, DBObjectType.MODEL);
			obj.addArray(DBArray.createFloatArray("vertices", mesh.getModelData().getVertices()));
			obj.addArray(DBArray.createFloatArray("textureCoords", mesh.getModelData().getTextureCoords()));
			obj.addArray(DBArray.createFloatArray("normals", mesh.getModelData().getNormals()));
			obj.addArray(DBArray.createIntegerArray("indices", mesh.getModelData().getIndices()));
			obj.addString(DBString.create("texture", tex));
			AssetCache.db.addObject(obj);
			meshes[i] = mesh;
		}
		AssetCache.db.serialize("assets.lum");
		return meshes;
	}
	
	private static TexturedModel processStaticMesh(AIMesh aiMesh, List<Material> materials) {
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textures = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();

		processVertices(aiMesh, vertices);
		processNormals(aiMesh, normals);
		processTextureCoordinates(aiMesh, textures);
		processIndices(aiMesh, indices);
		
		float[] vertArray = new float[vertices.size()];
		float[] normArray = new float[normals.size()];
		for (int i = 0; i < vertices.size(); i++) {
			vertArray[i] = vertices.get(i);
			normArray[i] = normals.get(i);
		}
		
		float[] textArray = new float[textures.size()];
		for (int i = 0; i < textures.size(); i++) 
			textArray[i] = textures.get(i);
		
		int[] indArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) 
			indArray[i] = indices.get(i);
		
		VertexArray vao = null;
		if (aiMesh.mNumAnimMeshes() == 0)
			vao = Loader.getInstance().load(vertArray, textArray, normArray, indArray);
		
		ModelData md = new ModelData(vertArray, textArray, normArray, indArray);
		
		assert (vao != null);
		
        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }
        TexturedModel model = new TexturedModel(vao, material, md);

        return model;
	}

	private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		while (aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			vertices.add(aiVertex.x());
			vertices.add(aiVertex.y());
			vertices.add(aiVertex.z());
		}
	}
	
	private static void processNormals(AIMesh aiMesh, List<Float> normals) {
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		while (aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			normals.add(aiVertex.x());
			normals.add(aiVertex.y());
			normals.add(aiVertex.z());
		}
	}

	private static void processTextureCoordinates(AIMesh aiMesh, List<Float> textures) {
		AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
		int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
		for (int i = 0; i < numTextCoords; i++) {
			AIVector3D textCoord = textCoords.get();
			textures.add(textCoord.x());
			textures.add(1 - textCoord.y());
		}
	}
	
	private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }

	private static void processMat(AIMaterial aiMaterial, List<Material> materials, String textureDir) {
		AIColor4D color = AIColor4D.create();
		AIString path = AIString.calloc();
		aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		String textPath = path.dataString();
		
		Texture texture = null;
		
		if (textPath != null && textPath.length() > 0) {
			try {
				texture = new Texture(textureDir + "/" + textPath);
			} catch (Exception e) {
				throw new NullPointerException("Could not load texture at: " + textureDir + "/" + textPath + "\n" + e.getMessage());
			}
		}
		else {
			try {
				texture = new Texture(textureDir);
			} catch (Exception e) {
				throw new NullPointerException("Could not load texture at: " + textureDir + "\n" + e.getMessage());
			}
		}
		
		Vector4 ambient = null;
		Vector4 diffuse = null;
		Vector4 specular = null;
		
		int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
		if (result == 0) 
			ambient = new Vector4(color.r(), color.g(), color.b(), color.a());

		result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
		if (result == 0) 
			diffuse = new Vector4(color.r(), color.g(), color.b(), color.a());

		result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
		if (result == 0)
			specular = new Vector4(color.r(), color.g(), color.b(), color.a());

		Material material = new Material();
		material.attachAmbient(ambient);
		material.attachDiffuse(diffuse);
		material.attachSpecular(specular);
		material.attachTexture(texture);
		
		materials.add(material);
	}

}
