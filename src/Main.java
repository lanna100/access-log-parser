import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Statistics statistics = new Statistics();
        int correctFilePathСounter = 0;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (fileExists && !isDirectory) {
                System.out.println("Путь указан верно");
                correctFilePathСounter++;
                System.out.println("Это файл номер " + correctFilePathСounter);

                int lineCounter = 0;
                final int maxAllowedLineLength = 1024;
                int totalRequests = 0;
                int googlebotCounter = 0;
                int yandexbotCounter = 0;

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if (length > maxAllowedLineLength) {
                            throw new LineLengthTooLongException("Строка длиннее " + maxAllowedLineLength + " символов: " + line);
                        }
                        lineCounter++;
                        totalRequests++;

                        try {
                            LogEntry logEntry = new LogEntry(line);
                            statistics.addEntry(logEntry);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Ошибка: " + ex.getMessage());
                            System.out.println("Неправильный формат строки: " + line);
                            continue;
                        }

                        //разбиваем на фрагменты по " и выделяем UserAgent фрагмент
                        String[] parts = line.split("\"");
                        if (parts.length >= 6) {
                            String userAgent = parts[5];
                            int openBracketIndex = userAgent.indexOf("(");
                            int closeBracketIndex = userAgent.indexOf(")");
                            if (openBracketIndex != -1 && closeBracketIndex != -1 && openBracketIndex < closeBracketIndex) {
                                String firstBrackets = userAgent.substring(userAgent.indexOf("(") + 1, userAgent.indexOf(")"));

                                //Разбиваем UserAgent на фрагменты по ; и убираем пробелы
                                String[] userAgentParts = firstBrackets.split(";");
                                if (userAgentParts.length >= 2) {
                                    String fragment = userAgentParts[1].replaceAll(" ", "");

                                    //отделяем в этом фрагменте часть до слэша
                                    String botName = fragment.split("/")[0].replaceAll(" ", "");

                                    // подсчитываем запросы от Google/Yandex ботов
                                    if (botName.equals("Googlebot")) {
                                        googlebotCounter++;
                                    } else if (botName.equals("YandexBot")) {
                                        yandexbotCounter++;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Ошибка при чтении файла " + file + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
                //System.out.println("Общее количество строк в файле: " + lineCounter);
                System.out.println("Общее количество запросов: " + totalRequests);
                double googlebotFraction = (double) googlebotCounter / totalRequests * 100;
                double yandexbotFraction = (double) yandexbotCounter / totalRequests * 100;
                System.out.printf("Доля запросов от Googlebot относительно общего числа сделанных запросов: %.2f%%\n", googlebotFraction);
                System.out.printf("Доля запросов от YandexBot относительно общего числа сделанных запросов: %.2f%%\n", yandexbotFraction);

                System.out.println("Cредний объём трафика сайта за час - " + statistics.getTrafficRate());
            }
            if (isDirectory) {
                System.out.println("Указан путь к папке, а не к файлу.");
                continue;
            }
            if (!fileExists) {
                System.out.println("Файл не существует");
            }
        }
    }
}
