package implementation;

import hashfunctions.FNVwithLCGHash;
import hashfunctions.MurmurHash3;
import hashfunctions.SHA256Hash;


/**
 * Static class that uses hash function implementing classes and delegates their results for further usage.
 * 
 * @author Bruno
 *
 */
public class HashFunctions {
	
	private static int m = 31415926;
	
	private static int murmur3hash(String message,int seed){
		return MurmurHash3.murmurhash3_x86_32(message, 0, message.getBytes().length, seed);
	}
	
	/**
	 * Returns requested number of hashes for a given message using Murmur3 hash function.
	 * @param message message to generate the hasehs from
	 * @param k number of hashes wanted
	 * @return array holding k hash results
	 */
	public static int[] murmur3(String message,int k){
		 
		int[] hashes = new int[k];
	        int seed = 0;
	        int pos = 0;
	    
	        while (pos < k) {
	        
	        	seed = HashFunctions.murmur3hash(message,seed);
	            int hash = Math.abs(seed);
	            
	            if (hash != -1) {
	                hashes[pos++] = hash;
	            }
	        }
	        
	        return hashes;
	}
	
	/**
	 * Returns requested number of hashes for a given message using SHA-256 hash function.
	 * @param message message to generate the hasehs from
	 * @param k number of hashes wanted
	 * @return array holding k hash results
	 */
	public static int[] SHA256(String message,int k){
		return SHA256Hash.hashCrypt(message.getBytes(), m, k);
	}
	
	/**
	 * Returns requested number of hashes for a given message using FNV hash function with LCG hash variation method.
	 * @param message message to generate the hasehs from
	 * @param k number of hashes wanted
	 * @return array holding k hash results
	 */
	public static int[] FNVwithLCG(String message, int k){
		return FNVwithLCGHash.hashSimpleLCG(message.getBytes(), m, k);
	}
	
	/**
	 * Returns requested number of hashes for a given message using Java Random class number generator.
	 * @param message message to generate the hasehs from
	 * @param k number of hashes wanted
	 * @return array holding k hash results
	 */
	public static int[] Random(String message, int k){
		
		java.util.Random r = new java.util.Random(System.currentTimeMillis());
		int[] hashes = new int[k];
		
		for(int i=0; i<k;++i){		
			hashes[i] = r.nextInt();
		}
		
		return hashes;
	}
		
}
	
	