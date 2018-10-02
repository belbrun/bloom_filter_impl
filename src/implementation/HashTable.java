package implementation;

import java.util.BitSet;
import java.util.Collection;
import hashfunctions.HashFunction;

/**
 *Implementation of the Bloom's original model of a hash-table which allows adding an element and testing 
 *if the hash-table contains the element. 
 *Implementation is effective in the terms of memory usage, and has variable hashing functions which allow
 *different performances in other factors.
 *Contains methods which can calculate theoretical percentages of expected false positives and number of zeroes.  
 * @author Bruno
 *
 */
public class HashTable {
	private BitSet table;
	private int size;
	private int hashesPerMessage;
	private int messagesStored = 0;	
	private HashFunction hashFunction;



	
	/**
	 * Default constructor. Takes the size of the hash-table, and number of hashes that will be done for each message. 
	 * Also takes a hash function the Bloom filter will use for generating addresses.
	 * @param size size of the hash-table
	 * @param hashesPerMessage number of hashes per message
	 * @param hashFunction hash function that will generate addresses
	 */
	public HashTable(int size, int hashesPerMessage, HashFunction hashFunction){
		this.size = size;
		this.hashesPerMessage = hashesPerMessage;
		table = new BitSet(size);
		this.hashFunction = hashFunction;
	
	}
	
	/**
	 * Constructor that creates a Bloom filter hash table with a requested theoretical percentage of false positives after 
	 * a given number of messages is stored to the hash table.
	 * Also takes a hash function the Bloom filter will use for generating addresses.
	 * @param messagesToBeStored number of messages that are going to be stored to the bloom filter 
	 * @param requestedFPPercentage theoretical false positive percentage after the given number of messages is stored in
	 * 								the hash table
	 * @param hashFunction hash function that will generate addresses
	 */
	public HashTable(int messagesToBeStored, double requestedFPPercentage, HashFunction hashFunction){
		this((int)(-1*messagesToBeStored*Math.log(requestedFPPercentage)/Math.pow(Math.log(2),2)),
				(int)(-1*Math.log(requestedFPPercentage)/Math.log(2)),
				hashFunction);
	}
	
	
	
	/**
	 * Adds an element to the hash-table. 
	 * Adding an element is implemented in a way that the element is feed to a previously
	 * specified number of hash functions, then bits on the locations determined by the hashes are set to 1.
	 * @param message message to be stored to the hash-table
	 */
	public void add(String message){
		
		int hashes[] = hashFunction.getHashFunction().apply(message, hashesPerMessage);
		
		for(int hash: hashes){
		
			if(hash<0) hash*=-1;
			table.set(hash%size);
		
		}
		
		messagesStored++;
	}
	
	/**
	 * Adds the given collection to the hash-table by performing the add
	 * method on each member of the collection.
	 * @param messages messages to be added to the hash table
	 */
	public void addAll(Collection<String> messages){
		for(String message: messages){
			add(message);
		}
	}
	
	
	/**
	 * Check if the given message is contained in the hash-table (false positives possible).
	 * Feeds the given message to the previously specified number of hash functions, then checks the 
	 * bits on the locations specified by hash values. If all of the bits are set to 1, returns true,
	 * otherwise returns false.
	 * @param message message to check the hash-table for
	 * @return true if the message is contained (or in the case of a false positive), false otherwise
	 */
	public boolean contains(String message){
		
		int hashes[] = hashFunction.getHashFunction().apply(message, hashesPerMessage);
		
		for(int hash: hashes){
		
			if(!table.get(hash%size)){
				return false;
			}
		
		}
		
		return true;
	}
	
	/**
	 * Calculates theoretical number of bits that would remain zero after storing the number of messages that is stored
	 * in this hash-table.
	 * @return theoretical percentage of the hash-table bits set to zero 
	 */
	public double expectedZeroes(){
		return Math.pow(1-(double)hashesPerMessage/size, messagesStored);
	}
	
	/**
	 * Calculates theoretical percentage of false positive values that would be returned by the contains method
	 * due to the number of messages that have been stored to the hash-table.
	 * It is also the theoretical probability of getting a false positive value when calling the contains method.
	 * @return expected percentage of false positive values returned by the contains function
	 */
	public double expectedFalsePositives(){
		return Math.pow(1-expectedZeroes(), hashesPerMessage);
	}	
		
	/**
	 * Removes all the elements and resets the hash table.
	 */
	public void clear(){
		table.clear();
	}
	
	
	public long getNumberOfFull(){
		return table.stream().count();
	}
	
	public long getNumberOfEmpty(){
		return size - table.stream().count();
	}

	public int getHashesPerMessage() {
		return hashesPerMessage;
	}

	public int getSize() {
		return size;
	}
	
	public HashFunction getHashFunction(){
		return hashFunction;
	}
	
	public int getMessagesStored(){
		return messagesStored;
	}
	
	public String toString(){
		return hashFunction.getName() + " size: " + size + " hashes per message: " + hashesPerMessage; 
	}
	
	
	
}



