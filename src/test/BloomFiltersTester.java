package test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import hashfunctions.HashFunction;
import implementation.HashTable;
import testresults.ConvergenceTestResult;
import testresults.SizeTestResult;
import testresults.SpeedTestResult;
import testresults.UniformityTestResult;

/**
 * Tester class that implements various tests made over a HashTable class. 
 * @author Bruno
 *
 */
public class BloomFiltersTester {
	
	
	/**
	 * Static getter that returns a new BloomFilterTester entity.
	 * @return new BloomFilterTester entity
	 */
	public static BloomFiltersTester produceTester(){
		return new BloomFiltersTester();
	}

	private int messageLength = 30;
	
	/**
	 * Tests uniformity of the hash function that is used by a given HashTable entity. 
	 * Does so by gradually adding given messages to the HashTable and delegating the HashTable to a UniformityTestResult 
	 * entity that saves the information . The UniformityTestResult entities are then saved to a LinkedList.
	 *  
	 * @param bloomFilter HashTable entity on which the tests will be carried out 
	 * @param messages test set that will be added to the hash table
	 * @param growthFactor factor that multiplies index of the last added message to determine the
	 * 			last index that will be added to a test sublist from the messages list in this iteration      
	 * 
	 * @return list of result data modeled in a UniformityTestResult entity
	 */
	public LinkedList<UniformityTestResult> testUniformity(HashTable bloomFilter, LinkedList<String> messages, int growthFactor){
		
		LinkedList<UniformityTestResult> results = new LinkedList<>();
		
		for(int i=0,j = 1; j<messages.size(); i=j, j*=growthFactor){
		
			bloomFilter.addAll(messages.subList(i, j));
			results.add(new UniformityTestResult(j, bloomFilter));
		
		}
		
		return results;
	}
	
	/**
	 * Generates a test list of m messages to send to the overloaded testUnformity method. Delegates the other arguments.   
	 * 
	 * @param bloomFilter to be delegated
	 * @param m size of the message list to be generated and delegated
	 * @param growthFactor to be delegated
	 * 
	 * @return result of the overloaded testUnifomiry function
	 */
	public LinkedList<UniformityTestResult> testUniformity(HashTable bloomFilter, int m, int growthFactor){
		return testUniformity(bloomFilter,generateMessages(m,messageLength), growthFactor);
	}
	
	
	/**
	 * Tests speed the given function needs to process a given list of strings.
	 * Saves the results in a List of SpeedTestResult entities.
	 * 
	 * @param function function to be timed
	 * @param messages messages the function will process
	 * @param growthFactor factor that multiplies index of the last added message to determine the
	 * 			last index that will be added to a test sublist from the messages list in this iteration	 
	 * 
	 * @return list of SpeedTestResult entities 
	 */
 	public LinkedList<SpeedTestResult> testSpeed(Consumer<List<String>> function, LinkedList<String> messages, int growthFactor){
 		
 		LinkedList<SpeedTestResult> results = new LinkedList<>();
		List<String> subList;
		long startingTime, currentTime = 0;
		
		for(int i=0,j=1 ; j<messages.size(); i=j, j+=growthFactor){			
		
			subList = messages.subList(i, j);
			startingTime = System.currentTimeMillis();
			
			function.accept(subList);
			currentTime += System.currentTimeMillis()-startingTime;
			results.add(new SpeedTestResult(j, currentTime));
		
		}
		
		return results;
 	}
 	
 	/**
	 * Generates a test list of m messages to send to the overloaded testSpeed method. Delegates the other arguments.   
	 * 
	 * @param bloomFilter to be delegated
	 * @param m size of the message list to be generated and delegated
	 * @param growthFactor to be delegated
	 * 
	 * @return result of the overloaded testSpeed function
	 */
 	public LinkedList<SpeedTestResult> testSpeed(Consumer<List<String>> function, int m, int growthFactor){
		return testSpeed(function,generateMessages(m,messageLength ), growthFactor);
	}
 	
	

 	
 	/**
 	 * Generates Bloom filter HashTables with different theoretical false positive percentages and tests those percentages.
 	 * Saves test results to a list of SizeTestResult entities. 
 	 *
 	 * @param messages messages to be added to every generated HashTable
 	 * @param maxPercentage percentage of false positives goes from maxPercetage to 0.5% or higher and drops by subjecting 
 	 * 						the last value of the false positive percentage to a fourth root function
 	 * 						
 	 * @return list of SizeTestResult entities which hold all useful test result data
 	*/
 	public LinkedList<SizeTestResult> testSizeForFP(LinkedList<String> messages, double maxPercentage){
 		
 		LinkedList<String> testSet = generateMessages(messages.size(), messages.getFirst().length());
 		LinkedList<SizeTestResult> results = new LinkedList<>();
 		double factor = Math.sqrt(Math.sqrt(2));
 		
 		for(double i = maxPercentage; i>0.5;i/=factor){
 		
 			HashTable bloomFilter = new HashTable(messages.size(), i/100, HashFunction.Murmur3);
 			
 			bloomFilter.addAll(messages);
 			results.add(new SizeTestResult(bloomFilter.getSize(), bloomFilter.getHashesPerMessage(), bloomFilter.expectedFalsePositives(), 
 					(double)countFalsePositives(bloomFilter, testSet, messages)/testSet.size()));
 		
 		}
 		
 		return results;
 	}
 	
 	/**
 	 * Tests convergence of results of false positive tests to a theoretical false positive value of the Blooms filter HashTable.
 	 * Adds the newly calculated average false positive percentage to a new ConvergenceTestResult to be added to the list.
 	 *  
 	 * @param messages to be added to the HashTable
 	 * @param falsePositivesPercentage theoretical false positive percentage to test the convergence for 
 	 * @param numOfTests number of test to be made 
 	 * 
 	 * @return list of ConvergenceTestResult entities
 	 */
 	public LinkedList<ConvergenceTestResult> testFPConvergence(LinkedList<String> messages, double falsePositivesPercentage, int numOfTests){
 		
 		LinkedList<ConvergenceTestResult> results = new LinkedList<>();
 		HashTable bloomFilter = new HashTable(messages.size(), falsePositivesPercentage, HashFunction.Murmur3);
 		
 		bloomFilter.addAll(messages);
 		
 		for(int i = 0; i< numOfTests;++i){
 		
 			LinkedList<String> testSet = generateMessages(messages.size(), messages.getFirst().length());
 			
 			if(i==0){
 	 			results.add(new ConvergenceTestResult(i, (double)countFalsePositives(bloomFilter, testSet, messages)/testSet.size()));
 			} else {
 				results.add(new ConvergenceTestResult(i, 
 						(results.getLast().getAvarageFPPercentage()*i + (double)countFalsePositives(bloomFilter, testSet, messages)/testSet.size())/(i+1)));
 			}
 			
 		}
 		return results; 		
 	}
 	
 	
 	/**
 	 * Counts false positive results in the Bloom filter HashTable for a given set of test messages.
 	 * Does so by calling HashTables contains method and then looking for the same test message in the
 	 * list of messages added to the HashTable.
 	 * 
 	 * @param bloomFilter Bloom filter that will be tested for false positives 
 	 * @param testSet set of messages that will be used for testing for false positives
 	 * @param messages set of messages that is already added to the HashTable
 	 * 
 	 * @return number of false positives found in the Bloom filter
 	 */
 	public int countFalsePositives(HashTable bloomFilter, LinkedList<String> testSet, LinkedList<String> messages){
 		
 		int falsePositives = 0;
 		HashSet<String> searchSet = new HashSet<>(messages);
		
 		for(String testMessage: testSet){
		
 			if(bloomFilter.contains(testMessage) && !searchSet.contains(testMessage)){
				falsePositives++;
			}
		
 		}
		
 		return falsePositives;
 	}
	
 	/**
 	 * Generates a given number of random "A","C","G","T" sequences of the given length.  
 	 * 
 	 * @param numOfMessages number of sequences to generate
 	 * @param messageLength length of every generated sequence
 	 * 
 	 * @return list of generated strings
 	 */
	public LinkedList<String> generateMessages(int numOfMessages, int messageLength){
		
		LinkedList<String> messages = new LinkedList<>();
		Random random = new Random(System.currentTimeMillis());
		
		for(int i = 0; i<numOfMessages;++i){
		
			String message = "";
			for(int j = 0; j<messageLength;++j){
			
				switch(random.nextInt(4)){
					case 0:
						message += "A";
						break;
					case 1:
						message += "C";
						break;
					case 2:
						message += "G";
						break;
					case 3:
						message += "T";
						break;
				}
			}
			
			messages.add(message);
		}
		
		return messages;
	}	
	
}
