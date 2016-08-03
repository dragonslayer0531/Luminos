package tk.luminos.filesystem;

import tk.luminos.filesystem.serialization.LDatabase;
import tk.luminos.filesystem.serialization.LField;
import tk.luminos.filesystem.serialization.LObject;
import tk.luminos.filesystem.serialization.LString;

public class FileManager {
	
	public static final String shine_damper = "shine_damper";
	public static final String shine_damper_field = "sd";
	public static final String reflectivity = "reflectivity";
	public static final String reflectivity_field = "r";
	public static final String number_of_rows = "number_of_rows";
	public static final String number_of_rows_field = "nor";
	public static final String transparency = "transparency";
	public static final String transparency_field = "f";
	public static final String fake_lighting = "fake_lighting";
	public static final String fake_lighting_field = "fk";
	public static final String normal = "normal";
	public static final String normal_field = "n";
	public static final String double_sided = "double_sided";
	public static final String double_sided_field = "ds";
	
	private LDatabase db;	
	
	public FileManager(String OBJ_FILE, String TEXTURE_FILE) {
		db = Converter.objToLOF(OBJ_FILE, TEXTURE_FILE);
	}
	
	public void setDoubleSided() {
		LObject obj = new LObject(double_sided);
		obj.addField(LField.Boolean(double_sided_field, true));
		db.addObject(obj);
	}
	
	public void setShineDamper(float shine) {
		LObject obj = new LObject(shine_damper);
		obj.addField(LField.Float(shine_damper_field, shine));
		db.addObject(obj);
	}
	
	public void setReflectivity(float reflectivity) {
		LObject obj = new LObject(FileManager.reflectivity);
		obj.addField(LField.Float(reflectivity_field, reflectivity));
		db.addObject(obj);
	}
	
	public void hasTransparency(boolean transparent) {
		LObject obj = new LObject(transparency);
		obj.addField(LField.Boolean(transparency_field, transparent));
		db.addObject(obj);
	}
	
	public void hasFakeLighting(boolean lighting) {
		LObject obj = new LObject(fake_lighting);
		obj.addField(LField.Boolean(fake_lighting_field, lighting));
		db.addObject(obj);
	}
	
	public void setNormal(String normal) {
		LObject obj = new LObject(FileManager.normal);
		obj.addString(LString.Create(normal_field, normal));
		db.addObject(obj);
	}
	
	public void setNumberOfRows(int number_of_rows) {
		LObject obj = new LObject(FileManager.number_of_rows);
		obj.addField(LField.Integer(number_of_rows_field, number_of_rows));
		db.addObject(obj);
	}

}
