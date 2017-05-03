package tk.luminos.graphics.anim;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.maths.Matrix4;

public class JointData {
	
	public final int index;
	public final String nameID;
	public final Matrix4 bindLocalTransform;
	
	public final List<JointData> children = new ArrayList<JointData>();
	
	public JointData(int index, String nameID, Matrix4 bindLocalTransform) {
		this.index = index;
		this.nameID = nameID;
		this.bindLocalTransform = bindLocalTransform;
	}
	
	public void addChild(JointData child) {
		this.children.add(child);
	}

}
