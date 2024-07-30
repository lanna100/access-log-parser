import java.io.File;
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
