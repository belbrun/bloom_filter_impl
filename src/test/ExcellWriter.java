package test;

import java.util.LinkedList;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import implementation.HashTable;
import testresults.ConvergenceTestResult;
import testresults.SizeTestResult;
import testresults.SpeedTestResult;
import testresults.TestResult;
import testresults.UniformityTestResult;

/**
 * Writes various forms of test results to an Excell sheet from the given starting cell in the sheet.
 * 
 * @author Bruno
 *
 */
public class ExcellWriter {

	private Sheet sheet;
	private Row row;
	private Cell cell;
	private int startingCell;

	/**
	 * Default constructor. Takes a sheet to write into and the index of the cell from which to start writing.
	 * 
	 * @param sheet sheet to be written into
	 * @param startingCell cell to start from
	 */
	public ExcellWriter(Sheet sheet, int startingCell) {
		this.sheet = sheet;
		this.startingCell = startingCell;
	}

	/**
	 * Creates a new row and sets cell value of the first cell in the row to a given string.
	 * 
	 * @param rowText String to put in the first cell of the row
	 */
	private void createRow(String rowText) {
		row = sheet.createRow(startingCell++);
		cell = row.createCell(0);
		cell.setCellValue(rowText);
	}

	/**
	 * Writes test results to the last used row of the table.
	 * Uses a given function to get the wanted value from a TestResult extending entity.
	 * 
	 * @param function function to get the wanted value
	 * @param testResults list which contains the test result
	 */
	private void writeTestResults(Function<Integer, Number> function,
			LinkedList<? extends TestResult> testResults) {
		for (int i = 1; i < testResults.size(); ++i) {
			cell = row.createCell(i);
			cell.setCellValue(function.apply(i).doubleValue());
		}
	}

	/**
	 * Writes uniformity test results to an Excell sheet.
	 * 
	 * @param bloomFilter HashTable entity on which the test were carried out
	 * @param testResults list of test results
	 */
	public void processUniformityTestResults(HashTable bloomFilter, LinkedList<UniformityTestResult> testResults) {

		createRow("UNIFORMITY TEST");

		createRow(bloomFilter.toString());

		createRow("Number of generated messages");
		writeTestResults((i) -> testResults.get(i).getM(), testResults);

		createRow("Number of bits set to 0");
		writeTestResults((i) -> testResults.get(i).getNumOfEmpty(), testResults);

		createRow("Number of bits set to 1");
		writeTestResults((i) -> testResults.get(i).getNumOfFull(), testResults);

		createRow("Percentage of bits set to 0");
		writeTestResults((i) -> testResults.get(i).getPercentageOfEmpty(), testResults);

		createRow("Expected percentage");
		writeTestResults((i) -> testResults.get(i).getExpectedZeroes(), testResults);

	}
	

	/**
	 * Writes speed test results to an Excell sheet.
	 * 
	 * @param bloomFilter HashTable entity on which the test were carried out
	 * @param testResults list of test results
	 */
	public void processSpeedTestResults(HashTable bloomFilter, LinkedList<SpeedTestResult> testResults) {

		createRow("SPEED TEST");

		createRow(bloomFilter.toString());

		createRow("Number of generated messages");
		writeTestResults((i) -> testResults.get(i).getMessagesAdded(), testResults);

		createRow("Elapsed time / ms");
		writeTestResults((i) -> testResults.get(i).getTimeElapsed(), testResults);

	}

	

	/**
	 * Writes size test results to an Excell sheet.
	 * 
	 * @param testResults list of test results
	 */
	public void processSizeTestResults(LinkedList<SizeTestResult> testResults) {


		createRow("TESTING CHANGES ON THE BF SIZE AND K GIVEN FP RATE");
		
		createRow("Number of messages tested is equal to the number of messages saved in the bloom filter, hash function is set to Murmur3");

		createRow("Bloom Filter size");
		writeTestResults((i) -> testResults.get(i).getSize(), testResults);

		createRow("Hashes per message");
		writeTestResults((i) -> testResults.get(i).getHashesPerMessage(), testResults);

		createRow("Theoretical FP percentage");
		writeTestResults((i) -> testResults.get(i).getTheroeticalFPPercentage(), testResults);

		createRow("Tested FP percentage");
		writeTestResults((i) -> testResults.get(i).getTestedFPPercentage(), testResults);

	}

	/**
	 * Writes convergence test results to an Excell sheet.
	 * 
	 * @param bloomFilter HashTable entity on which the test were carried out
	 * @param testResults list of test results
	 */
	public void processConvergenceTestResults(HashTable bloomFilter, LinkedList<ConvergenceTestResult> testResults) {

		createRow("Convergence of false positives");
		createRow(bloomFilter.toString());

		createRow(Double.toString(bloomFilter.expectedFalsePositives()));

		createRow("Number of tests");
		writeTestResults((i) -> testResults.get(i).getNumOfTestsDone(),testResults);

		createRow("Avarage FP Percentage");
		writeTestResults((i) -> testResults.get(i).getAvarageFPPercentage(),testResults);

	}

}
