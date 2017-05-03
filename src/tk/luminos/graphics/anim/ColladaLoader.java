package tk.luminos.graphics.anim;

import tk.luminos.filesystem.xml.XMLNode;
import tk.luminos.filesystem.xml.XMLParser;
import tk.luminos.utilities.File;

public class ColladaLoader {
	
	public static AnimatedModelData loadColladaModel(File colladaFile, int maxWeights) throws Exception {
		XMLNode node = XMLParser.loadXMLFile(colladaFile);
		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();
		
		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();
		
		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();
		
		return new AnimatedModelData(meshData, jointsData);
	}
	
	public static AnimationData loadColladaAnimation(File colladaFile) throws Exception {
		XMLNode node = XMLParser.loadXMLFile(colladaFile);
		XMLNode animNode = node.getChild("library_animations");
		XMLNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}

}
