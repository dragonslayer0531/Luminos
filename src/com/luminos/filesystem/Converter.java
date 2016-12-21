package com.luminos.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.luminos.filesystem.serialization.LArray;
import com.luminos.filesystem.serialization.LDatabase;
import com.luminos.filesystem.serialization.LObject;
import com.luminos.filesystem.serialization.LString;
import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;

public class Converter {
	
	public static LDatabase objToLOF(String obj, String textureFile) {
		File file = new File(obj);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e)  {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
		List<Vector3f> positions = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try {
			
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					positions.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					textureArray = new float[positions.size() * 2];
					normalsArray = new float[positions.size() * 3];
					break;
				}
		
			}
			
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
				processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
				processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
				line = reader.readLine();
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[positions.size()*3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for(Vector3f vertex : positions){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for(int i=0;i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}
		
		LDatabase db = new LDatabase(file.getName().substring(0, file.getName().length()-4));
		LArray pos = LArray.Float("pos", verticesArray);
		LArray tex = LArray.Float("tex", textureArray);
		LArray nor = LArray.Float("nor", normalsArray);
		LArray ind = LArray.Integer("ind", indicesArray);
		LString img = LString.Create("img", textureFile);
		LObject LOF = new LObject("model");
		LOF.addArray(pos);
		LOF.addArray(tex);
		LOF.addArray(nor);
		LOF.addArray(ind);
		LObject IMG = new LObject("texture");
		IMG.addString(img);
		db.addObject(LOF);
		db.addObject(IMG);
		return db;
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
			float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;	
	}

}
