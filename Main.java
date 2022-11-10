package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static File inputFile;
    static File outputFile;
    static boolean input = false;
    static boolean output = false;

    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dataType = "word";
        String sortingType = "natural";
        List<String> content = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if ("-sortingType".equals(args[i])) {
                if (i + 1 < args.length && ("natural".equals(args[i + 1]) || "byCount".equals(args[i + 1]))) {
                    sortingType = args[i + 1];
                    i++;
                } else {
                    System.out.println("No sorting type defined!");
                    return;
                }
            } else if ("-dataType".equals(args[i])) {
                if (i + 1 < args.length && ("long".equals(args[i + 1]) || "line".equals(args[i + 1]) || "word".equals(args[i + 1]))) {
                    dataType = args[i + 1];
                    i++;
                } else {
                    System.out.println("No data type defined!");
                    return;
                }
            } else if ("-inputFile".equals(args[i])) {
                inputFile = new File(args[i + 1]);
                input = true;
            } else if ("-outputFile".equals(args[i])) {
                outputFile = new File(args[i + 1]);
                output = true;
            } else {
                System.out.printf("\"%s\" is not a valid parameter. It will be skipped.\n", args[i]);
            }
        }
        if (input) {
            try (Scanner sc = new Scanner(inputFile)) {
                while (sc.hasNext()) {
                    content.add(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
            }
        } else {
            while (scanner.hasNext()) {
                content.add(scanner.nextLine());
            }
        }
        long percent;
        long total = 0;
        switch (dataType) {
            case "long":
                long number;
                if ("natural".equals(sortingType)) {
                    List<Long> numbersList = new ArrayList<>();
                    for (String str : content) {
                        try (Scanner sc = new Scanner(str)) {
                            while (sc.hasNext()) {
                                String word = sc.next();
                                try {
                                    number = Long.parseLong(word);
                                    numbersList.add(number);
                                    total++;
                                } catch (NumberFormatException e) {
                                    System.out.printf("\"%s\" is not a long. It will be skipped.\n", word);
                                }
                            }
                        }
                    }
                    Collections.sort(numbersList);
                    output(String.format("Total numbers: %d.\n", total));
                    output("Sorted data:");
                    for (long n : numbersList) {
                        output(" " + n);
                    }
                } else {
                    Map<Long, Long> numbersMap = new HashMap<>();
                    for (String str : content) {
                        try (Scanner sc = new Scanner(str)) {
                            while (sc.hasNext()) {
                                String word = sc.next();
                                try {
                                    number = Long.parseLong(word);
                                    numbersMap.put(number, numbersMap.getOrDefault(number, 0L) + 1);
                                    total++;
                                } catch (NumberFormatException e) {
                                    System.out.printf("\"%s\" is not a long. It will be skipped.\n", word);
                                }
                            }
                        }
                    }
                    output(String.format("Total numbers: %d.\n", total));
                    List<Map.Entry<Long, Long>> mapList = new ArrayList<>(numbersMap.entrySet());
                    mapList.sort(new NumberCountComparator());
                    for (Map.Entry<Long, Long> entry : mapList) {
                        percent = Math.round((double) entry.getValue() / total * 100);
                        output(String.format("%d: %d time(s), %d%%\n", entry.getKey(), entry.getValue(), percent));
                    }
                }
                break;
            case "line":
                if ("natural".equals(sortingType)) {
                    List<String> linesList = new ArrayList<>();
                    for (String line : content) {
                        linesList.add(line);
                        total++;
                    }
                    Collections.sort(linesList);
                    output(String.format("Total lines: %d.\n", total));
                    output("Sorted data:");
                    for (String str : linesList) {
                        output(str);
                    }
                } else {
                    Map<String, Long> linesMap = new HashMap<>();
                    for (String line : content) {
                        linesMap.put(line, linesMap.getOrDefault(line, 0L) + 1);
                        total++;
                    }
                    output(String.format("Total lines: %d.\n", total));
                    List<Map.Entry<String, Long>> mapList = new ArrayList<>(linesMap.entrySet());
                    mapList.sort(new StringCountComparator());
                    for (Map.Entry<String, Long> entry : mapList) {
                        percent = Math.round((double) entry.getValue() / total * 100);
                        output(String.format("%s: %d time(s), %d%%\n", entry.getKey(), entry.getValue(), percent));
                    }
                }
                break;
            case "word":
                String word;
                if ("natural".equals(sortingType)) {
                    List<String> wordsList = new ArrayList<>();
                    for (String line : content) {
                        try (Scanner sc = new Scanner(line)) {
                            while (sc.hasNext()) {
                                word = sc.next();
                                wordsList.add(word);
                                total++;
                            }
                        }
                    }
                    Collections.sort(wordsList);
                    output(String.format("Total words: %d.\n", total));
                    for (String str : wordsList) {
                        output(str);
                    }
                } else {
                    Map<String, Long> wordsMap = new HashMap<>();
                    for (String line : content) {
                        try (Scanner sc = new Scanner(line)) {
                            while (sc.hasNext()) {
                                word = sc.next();
                                wordsMap.put(word, wordsMap.getOrDefault(word, 0L) + 1);
                                total++;
                            }
                        }
                    }
                    output(String.format("Total words: %d.\n", total));
                    List<Map.Entry<String, Long>> mapList = new ArrayList<>(wordsMap.entrySet());
                    mapList.sort(new StringCountComparator());
                    for (Map.Entry<String, Long> entry : mapList) {
                        percent = Math.round((double) entry.getValue() / total * 100);
                        output(String.format("%s: %d time(s), %d%%\n", entry.getKey(), entry.getValue(), percent));
                    }
                }
                break;
        }
    }

    static void output(String line) {
        if (output) {
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.append(line);
            } catch (IOException e) {
                System.out.println("file not found");
            }
        } else {
            System.out.print(line);
        }
    }
}


class NumberCountComparator implements Comparator<Map.Entry<Long, Long>> {
    @Override
    public int compare(Map.Entry<Long, Long> entry1, Map.Entry<Long, Long> entry2) {
        if (entry1.getValue().equals(entry2.getValue())) {
            return Long.compare(entry1.getKey(), entry2.getKey());
        }
        return Long.compare(entry1.getValue(), entry2.getValue());
    }
}


class StringCountComparator implements Comparator<Map.Entry<String, Long>> {
    @Override
    public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
        if (entry1.getValue().equals(entry2.getValue())) {
            return entry1.getKey().compareTo(entry2.getKey());
        }
        return Long.compare(entry1.getValue(), entry2.getValue());
    }
}