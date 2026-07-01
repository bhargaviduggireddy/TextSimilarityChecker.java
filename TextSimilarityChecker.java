import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TextSimilarityChecker {

    // A basic set of English stop words to filter out common noise words
    private static final Set<String> STOP_WORDS = new HashSet<>();
    static {
        String[] words = {"the", "a", "an", "and", "or", "but", "is", "are", "was", "were", "to", "of", "in", "for", "on", "with"};
        for (String w : words) {
            STOP_WORDS.add(w);
        }
    }

    public static void main(String[] args) {
        // --- MINI GUIDE (a): Read input files ---
        // Change these file names to test your own text files in VS Code
        String file1Path = "doc1.txt"; 
        String file2Path = "doc2.txt";

        System.out.println("=== Text Similarity Checker (Plagiarism Detector) ===");
        System.out.println("Reading files: " + file1Path + " and " + file2Path + "\n");

        try {
            String text1 = readFile(file1Path);
            String text2 = readFile(file2Path);

            // --- MINI GUIDE (b & c): Tokenize, clean, and compute frequency ---
            Map<String, Integer> wordFreq1 = getWordFrequency(text1);
            Map<String, Integer> wordFreq2 = getWordFrequency(text2);

            // --- MINI GUIDE (c & e): Compute Cosine Similarity percentage ---
            double similarityScore = calculateCosineSimilarity(wordFreq1, wordFreq2);
            double similarityPercentage = similarityScore * 100;

            // --- MINI GUIDE (f): Export / Display results ---
            System.out.println("------------------------------------------------");
            System.out.printf("Similarity Result: %.2f%%\n", similarityPercentage);
            System.out.println("------------------------------------------------");
            
            if (similarityPercentage > 70) {
                System.out.println("⚠️ Alert: High text similarity detected! (Possible Plagiarism)");
            } else if (similarityPercentage > 30) {
                System.out.println("ℹ️ Notice: Moderate text similarity detected.");
            } else {
                System.out.println("✅ Clear: Low text similarity. The content looks original.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error: Could not read the files.");
            System.out.println("Make sure you have created '" + file1Path + "' and '" + file2Path + "' in your project folder.");
        }
    }

    /**
     * Helper method to read the entire contents of a file into a String
     */
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString();
    }

    /**
     * Tokenizes text, converts to lowercase, removes punctuation, and filters out stop words.
     */
    private static Map<String, Integer> getWordFrequency(String text) {
        Map<String, Integer> freqMap = new HashMap<>();
        
        // Split text by spaces and any non-alphabet characters
        String[] words = text.toLowerCase().split("[^a-zA-Z0-9]+");

        for (String word : words) {
            if (word.trim().isEmpty()) {
                continue;
            }
            // --- MINI GUIDE (b): Remove stopwords ---
            if (!STOP_WORDS.contains(word)) {
                freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
            }
        }
        return freqMap;
    }

    /**
     * Calculates the cosine  similarity score between two frequency maps.
     */
    private static double calculateCosineSimilarity(Map<String, Integer> doc1, Map<String, Integer> doc2) {
        // Get unique words from both documents combined
        Set<String> uniqueWords = new HashSet<>();
        uniqueWords.addAll(doc1.keySet());
        uniqueWords.addAll(doc2.keySet());

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (String word : uniqueWords) {
            int freq1 = doc1.getOrDefault(word, 0);
            int freq2 = doc2.getOrDefault(word, 0);

            dotProduct += freq1 * freq2;
            normA += Math.pow(freq1, 2);
            normB += Math.pow(freq2, 2);
        }

        if (normA == 0 || normB == 0) {
            return 0.0; // Avoid division by zero if an empty file is processed
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}