package di_rover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time

        try (final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<Integer>> futures = new ArrayList<>();

            for (String text : texts) {
                final Future<Integer> task = threadPool.submit(() -> showText(text));
                futures.add(task);
            }

            Integer maxRange = 0;

            for (Future<Integer> future : futures) {
                final Integer resultOfTask = future.get();

                if (resultOfTask > maxRange) {
                    maxRange = resultOfTask;
                }
            }

            System.out.println("Max range: " + maxRange);

            long endTs = System.currentTimeMillis(); // end time

            System.out.println("Time: " + (endTs - startTs) + "ms");

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int showText(String text) {
        int maxSize = 0;
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < text.length(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    if (text.charAt(k) == 'b') {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                }
            }
        }
        System.out.println(text.substring(0, 100) + " -> " + maxSize);

        return maxSize;
    }


}