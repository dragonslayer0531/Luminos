package luminoscore.graphics.terrains;

public class TerrainType {
	
	public static enum Type {
		FLAT,
		PLAINS,
		HILLS,
		MOUNTAINS
	}
	
	public static float[] getData(Type type) {
		float data[] = new float[3];
		switch(type) {
		case FLAT:
			data[0] = 0;
			data[1] = 1;
			data[2] = 0;
			break;
		case PLAINS:
			data[0] = 25;
			data[1] = 5;
			data[2] = 0.05f;
			break;
		case HILLS:
			data[0] = 150;
			data[1] = 4;
			data[2] = .1f;
			break;
		case MOUNTAINS:
			data[0] = 100;
			data[1] = 2;
			data[2] = .15f;
		}
		
		return data;
			
	}
	
}
