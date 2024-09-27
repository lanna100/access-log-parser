import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*Предыдущее задание:
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());*/


        int counterCorrectFilePath = 0;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (fileExists && !isDirectory) {
                System.out.println("Путь указан верно");
                counterCorrectFilePath++;
                System.out.println("Это файл номер " + counterCorrectFilePath);

                int lineCounter = 0;
                final int maxAllowedLineLength = 1024;
                int maxLength = 0;
                int minLength = maxAllowedLineLength;

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if (line.length() > maxAllowedLineLength) {
                            throw new LineLengthTooLongException("Строка длиннее " + maxAllowedLineLength + " символов: " + line);
                        }
                        lineCounter++;
                        if (length > maxLength) {
                            maxLength = length;
                        }
                        if (length < minLength) {
                            minLength = length;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Ошибка при чтении файла " + file + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
                System.out.println("Общее количество строк в файле: " + lineCounter);
                System.out.println("Длина самой длинной строки в файле: " + maxLength);
                System.out.println("Длина самой короткой строки в файле: " + minLength);
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
