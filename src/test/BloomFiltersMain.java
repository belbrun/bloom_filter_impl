package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import hashfunctions.HashFunction;
import implementation.HashTable;

/**
 * Main class that starts the test.
 * @author Bruno
 *
 */
public class BloomFiltersMain {

	static Workbook workbook;
	static Sheet sheet;
	
	
	
	public static void main(String[] args) {
		
		int N = 90000, k = 3, m = 8400, growthFactor = 2, testMessageLength = 50; 
		
		LinkedList<HashTable> bloomFilters = generateBloomFilters(N, k, HashFunction.values());
		BloomFiltersTester tester = BloomFiltersTester.produceTester();						
		
		FileOutputStream outputStream = setupOutputStream();
	
		setupExcellWorkbook();		
		ExcellWriter writer = new ExcellWriter(sheet, 0);		
	
		System.out.println("Generating test messages");		
		LinkedList<String> messages = tester.generateMessages(m, testMessageLength);
		
					
		testUniformity(bloomFilters, tester, messages, writer, growthFactor);
				
		testSpeed(bloomFilters, tester, messages, writer, growthFactor);
		
		messages = loadTestMessages("./testdata/single.txt");		
		
		testFalsePositives(messages, tester, writer);
		
		testBloomFilterSize(tester, messages, writer);
		
		writeToWorkbook(outputStream);
				
		System.out.println("Testing done.");
	}
	
	
	
	/**
	 * Generates HashTable entities (Bloom filters) with the given parameters. 
	 * Generates one entity for each hash function in the given field
	 * 
	 * @param N size
	 * @param k number of hashes per message
	 * @param hashFunctions field of hash functions to generate the hash tables with 
	 * 
	 * @return list of HashTable entities (Bloom filters) with given parameters
	 */
	public static LinkedList<HashTable> generateBloomFilters(int N, int k, HashFunction[] hashFunctions){
		
		LinkedList<HashTable> bloomFilters = new LinkedList<>();
		
		for(HashFunction hashFunction: hashFunctions){
			bloomFilters.add(new HashTable(N, k, hashFunction));
		}
		
		return bloomFilters;		
	}
	
	/**
	 * Sets up the output stream and creates the file name according to the current time.
	 * 
	 * @return file output stream connected to a created file name
	 */
	public static FileOutputStream setupOutputStream(){
		
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "test"+System.currentTimeMillis()+".xlsx";
		
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileLocation);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Output stream ready");
		return outputStream;
	}
	
	/**
	 * Initializes the static Excell workbook, adds and modifies a new sheet in the workbook.
	 */
	public static void setupExcellWorkbook(){
		
		workbook = new XSSFWorkbook();
		
		sheet = workbook.createSheet("Tests");
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
		
		System.out.println("excell workbook ready");
	}
	
	/**
	 * Tests uniformity of a list of Bloom filters and writes the test results using an ExcellWriter entity.
	 * 
	 * @param bloomFilters list of HashTable entities that are to be tested 
	 * @param tester BloomFilterTester entity to carry the test out
	 * @param messages set of messages to be used for the tests
	 * @param writer writes the results to an Excell sheet
	 * @param growthFactor factor to determine the test subgroups
	 */
	public static void testUniformity(LinkedList<HashTable> bloomFilters, BloomFiltersTester tester,LinkedList<String> messages, ExcellWriter writer, int growthFactor){
		
		System.out.println("Starting uniformity tests:");
		
		for(HashTable bloomFilter: bloomFilters){
			System.out.println("testing uniformity for bloom filter: " + bloomFilter.toString());
			
			writer.processUniformityTestResults(bloomFilter,tester.testUniformity(bloomFilter, messages, growthFactor));
		} 
	}
	
	/**
	 * Tests speed of a list of Bloom filters and writes the test results using an ExcellWriter entity.
	 * Also creates a new HashSet entity and does the same
	 * 
	 * @param bloomFilters list of HashTable entities that are to be tested 
	 * @param tester BloomFilterTester entity to carry the test out
	 * @param messages set of messages to be used for the tests
	 * @param writer writes the results to an Excell sheet
	 * @param growthFactor factor to determine the test subgroups
	 */
	public static void testSpeed(LinkedList<HashTable> bloomFilters, BloomFiltersTester tester,LinkedList<String> messages, ExcellWriter writer, int growthFactor){
		
		System.out.println("Starting speed tests:");
		
		for(HashTable bloomFilter: bloomFilters){
			System.out.println("testing speed for bloom filter: " + bloomFilter.toString());
			
			bloomFilter.clear();
			writer.processSpeedTestResults(bloomFilter, tester.testSpeed((t)->bloomFilter.addAll(t), messages, 2000));
		}
		
		HashSet<String> hashSet = new HashSet<>();
		writer.processSpeedTestResults(bloomFilters.getLast(), tester.testSpeed((t)->hashSet.addAll(t), messages, 2000));

	}
	
	/**
	 * Loads messages from a given string which holds a file path.
	 * 
	 * @param sourcePath path to the file which holds the messages
	 * 
	 * @return list of messages loaded from the file
	 */
	public static LinkedList<String> loadTestMessages(String sourcePath){
		
		LinkedList<String> messages = new LinkedList<>();
		System.out.println("Loading resources from: " + sourcePath);
		
		Path inputPath = Paths.get(sourcePath);
		try {
			messages.addAll(Files.readAllLines(inputPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage() + "Unable to load resources");
		}
		
		return messages;
	}
	
	/**
	 * Tests false positive convergence to the theoretical value and writes the results to an Excell sheet. 
	 * Creates a HashTable to use for testing.
	 * 
	 * @param messages set of messages to be used for the tests
	 * @param tester BloomFilterTester entity to carry the test out
	 * @param writer writes the results to an Excell sheet
	 */
	public static void testFalsePositives(LinkedList<String> messages, BloomFiltersTester tester, ExcellWriter writer){
		System.out.println("FP convergence test");
		HashTable bf = new HashTable(messages.size(), 0.01, HashFunction.Murmur3);
		
		bf.addAll(messages);
		
		writer.processConvergenceTestResults(bf,tester.testFPConvergence(messages, 0.01, 50));
	}
	
	/**
	 * Tests size and hashes per message behavior for a variety of false positive percentages and writes the result to an Excell sheet.
	 * 
 	 * @param tester BloomFilterTester entity to carry the test out
	 * @param messages set of messages to be used for the tests
	 * @param writer writes the results to an Excell sheet
	 */
	public static void testBloomFilterSize(BloomFiltersTester tester, LinkedList<String> messages, ExcellWriter writer){
		System.out.println("Testing false positives variations. Messages to be stored: " + messages.size());
		writer.processSizeTestResults(tester.testSizeForFP(messages, 16));
	}
	
	/**
	 * Writes the data in the static Sheet entity to a real Excell file connected with the output stream.
	 * Closes the stream.
	 * 
	 * @param outputStream stream to write to
	 */
	public static void writeToWorkbook(FileOutputStream outputStream){
		
		System.out.println("Writing to workbook");
		
		try {
		
			workbook.write(outputStream);
			workbook.close();	
			
			outputStream.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
