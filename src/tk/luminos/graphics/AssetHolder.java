package tk.luminos.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Holds asset locations for serialization
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class AssetHolder {
	
	private static Map<Integer, String> assetsOnGPU = new HashMap<Integer, String>();
	
	/**
	 * Adds an object to the asset map
	 * 
	 * @param gpuID			GPU ID of asset
	 * @param fileLocation	File location of asset
	 */
	public static void add(Integer gpuID, String fileLocation) {
		AssetHolder.assetsOnGPU.put(gpuID, fileLocation);
	}
	
	/**
	 * Gets the file location of asset based on GPU ID
	 * 
	 * @param gpuID		GPU ID of Asset to search for
	 * @return			Asset file location
	 */
	public static String getValue(Integer gpuID) {
		return AssetHolder.assetsOnGPU.get(gpuID);
	}
	
	/**
	 * Removes an asset from the asset map
	 * 
	 * @param gpuID		GPU ID of asset to remove
	 */
	public static void remove(Integer gpuID) {
		AssetHolder.assetsOnGPU.remove(gpuID);
	}
	
	/**
	 * Removes an asset from the asset map
	 * 
	 * @param gpuID			GPU ID of asset to remove
	 * @param fileLocation	File location of asset to remove
	 */
	public static void remove(Integer gpuID, String fileLocation) {
		AssetHolder.assetsOnGPU.remove(gpuID, fileLocation);
	}
	
	/**
	 * Checks if an asset is contained by the map
	 * 
	 * @param gpuID		GPU ID of asset to test for
	 * @return			If the asset is contained by the map
	 */
	public static boolean contains(Integer gpuID) {
		return AssetHolder.assetsOnGPU.containsKey(gpuID);
	}
	
	/**
	 * Checks if the map of assets is empty
	 * 
	 * @return			If the assets map is empty
	 */
	public static boolean isEmpty() {
		return AssetHolder.assetsOnGPU.isEmpty();
	}
	
	/**
	 * Clears the asset map
	 */
	public static void clear() {
		AssetHolder.assetsOnGPU.clear();
	}

}
