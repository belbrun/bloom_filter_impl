package hr.fer.projekt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TestUtils {
	
	public static HashSet<String> getWords() {
		Scanner sc = new Scanner(System.in);
		HashSet<String> words = new HashSet<>();
		
		while(true) {
			String line = sc.nextLine();
			
			if(line.isEmpty()) break;
			
			line = line.replaceAll("[^A-Za-z0-9\\s]", "");
			String[] lineParts = line.split(" ");
			
			for(String word : lineParts) {
				words.add(word);
			}
		}
		
		sc.close();
		
		return words;
	}
	
	public static void generateSequences(Path dna, String destination) throws IOException {
		List<String> lines = Files.readAllLines(dna);	
		
		StringBuilder sbHalf = new StringBuilder();
		StringBuilder sbDouble = new StringBuilder();
		StringBuilder sbTriple = new StringBuilder();
		
		int counterDouble = 0;
		int counterTriple = 0;
		
		for(String line : lines) {
			sbDouble.append(line);
			sbTriple.append(line);
			
			counterDouble++;
			counterTriple++;
			
			if(counterDouble == 2) {
				sbDouble.append(System.lineSeparator());
				counterDouble = 0;
			}	
			
			if(counterTriple == 3) {
				sbTriple.append(System.lineSeparator());
				counterTriple = 0;
			}
			
			sbHalf.append(line.substring(0, line.length() / 2) + System.lineSeparator() + line.substring(line.length() / 2) + System.lineSeparator());
		};
		
		Files.write(Paths.get(destination + "\\half.txt"), sbHalf.toString().getBytes());
		Files.write(Paths.get(destination + "\\double.txt"), sbDouble.toString().getBytes());
		Files.write(Paths.get(destination + "\\triple.txt"), sbTriple.toString().getBytes());
	}
}
