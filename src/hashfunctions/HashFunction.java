package hashfunctions;

import java.util.LinkedList;
import java.util.function.BiFunction;

import implementation.HashFunctions;

public enum HashFunction {
	Murmur3("Murmur3",HashFunctions::murmur3),SHA256("SHA-256",HashFunctions::SHA256),FNV("FNV",HashFunctions::FNVwithLCG),Random("Random", HashFunctions::Random);

	
	private BiFunction<String, Integer, int[]> hashFunction;
	private String name;

	
	
	
	
	private HashFunction(String name, BiFunction<String, Integer, int[]> hashFunction) {
		this.name = name;
		this.hashFunction = hashFunction;
	}
	
	
	public static HashFunction[] getAll(){
		return new HashFunction[]{HashFunction.Murmur3,HashFunction.FNV};
	}


	public BiFunction<String, Integer, int[]> getHashFunction() {
		return hashFunction;
	}


	public String getName() {
		return name;
	}	
	
	
}
