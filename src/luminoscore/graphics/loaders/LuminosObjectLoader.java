package luminoscore.graphics.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.textures.ModelTexture;
import luminoscore.physics.collisions.collider.AABB;
import luminoscore.util.math.vector.Vector3f;

public class LuminosObjectLoader {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/19/2016
	 */
	
	/*
	 * @param file Defines entity file to be loaded.  Must be Luminos Object File format
	 * @param loader Defines the VAOLoader to be used
	 * @param imgLoader Defines the ImageLoader to be used
	 * @return TexturedModel
	 * 
	 * Takes LOF file and creates a textured model
	 */
	public static TexturedModel loadToTexturedModel(File file, VAOLoader loader, ImageLoader imgLoader) {
		FileReader reader = null;
		try {
			if(!file.getName().endsWith(".lof")) return null;
			reader = new FileReader(file);
		} catch(FileNotFoundException e) {
			System.err.println("Could not load file");
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String line;

		List<Float> vertices = new ArrayList<Float>();
		List<Float> textures = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();
		AABB aabb = null;
		String image = null;
		boolean mip = false;
		boolean hasAABB = false;
		
		try {
			while(true) {
				line = br.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("vert ")) {
					for(int i = 1; i < currentLine.length; i++) {
						vertices.add(Float.parseFloat(currentLine[i]));
					}
				} else if (line.startsWith("text ")) {
					for(int i = 1; i < currentLine.length; i++) {
						textures.add(Float.parseFloat(currentLine[i]));
					}
				} else if (line.startsWith("norm ")) {
					for(int i = 1; i < currentLine.length; i++) {
						normals.add(Float.parseFloat(currentLine[i]));
					}
				} else if (line.startsWith("ind ")) {
					for(int i = 1; i < currentLine.length; i++) {
						indices.add(Integer.parseInt(currentLine[i]));
					}
				} else if (line.startsWith("img ")) {
					image = currentLine[1];
				} else if (line.startsWith("mip ")) {
					mip = Boolean.parseBoolean(currentLine[1]);
				} else if (line.startsWith("aabb ")) {
					hasAABB = true;
					aabb = new AABB(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])),
							new Vector3f(Float.parseFloat(currentLine[4]), Float.parseFloat(currentLine[5]), Float.parseFloat(currentLine[6])));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		float[] verticesArray = new float[vertices.size() * 3];
		float[] normalsArray = new float[normals.size() * 3];
		float[] textureArray = new float[textures.size() * 3];
		int[] indicesArray = new int[indices.size()];
		
		int pointer = 0;
		for(Float f : vertices) {
			verticesArray[pointer++] = f;
		} pointer = 0;
		
		for(Float n : normals) {
			normalsArray[pointer++] = n;
		} pointer = 0;
		
		for(Float t : textures) {
			textureArray[pointer++] = t;
		} pointer = 0;
		
		for(Integer i : indices) {
			indicesArray[pointer] = i;
		}
		
		if(image == null) {
			return null;
		}
		
		if(hasAABB) {
			return new TexturedModel(loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray, 3, aabb), new ModelTexture(imgLoader.loadTexture(image, mip)));
		}
		
		return new TexturedModel(loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray, 3), new ModelTexture(imgLoader.loadTexture(image, mip)));
	}

}
