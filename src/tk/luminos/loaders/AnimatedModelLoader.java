package tk.luminos.loaders;

import static tk.luminos.graphics.anim.ColladaLoader.loadColladaModel;

import tk.luminos.graphics.Material;
import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.anim.AnimatedModel;
import tk.luminos.graphics.anim.AnimatedModelData;
import tk.luminos.graphics.anim.Joint;
import tk.luminos.graphics.anim.JointData;
import tk.luminos.graphics.anim.MeshData;
import tk.luminos.utilities.File;

public class AnimatedModelLoader {
	
	public static AnimatedModel loadModel(File model, File texture) throws Exception {
		AnimatedModelData amd = loadColladaModel(model, 3);
		VertexArray vao = createVAO(amd.getMesh());
		Material tex = loadTexture(texture);
		return new AnimatedModel(vao, tex, createJoints(amd.getJoints().head), amd.getJoints().jointCount);
	}

	private static Joint createJoints(JointData joints) {
		Joint joint = new Joint(joints.index, joints.nameID, joints.bindLocalTransform);
		for (JointData child : joints.children)
			joint.addChild(createJoints(child));
		return joint;
	}

	private static Material loadTexture(File texture) throws Exception {
		Material mat = new Material();
		mat.attachDiffuse(new Loader().loadTexture(texture.getName()));
		return mat;
	}

	private static VertexArray createVAO(MeshData mesh) {
		VertexArray vao = new VertexArray();
		vao.bind();
		vao.createIndexBuffer(mesh.getIndices());
		vao.createAttribute(0, mesh.getVertices(), 3);
		vao.createAttribute(1, mesh.getTexture(), 2);
		vao.createAttribute(2, mesh.getNormals(), 3);
		vao.createAttribute(3, mesh.getJointIDs(), 3);
		vao.createAttribute(4, mesh.getVertexWeights(), 3);
		vao.unbind();
		return vao;
	}

}
