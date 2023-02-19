import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class Main {
    static String[] words = {"нуль", "один", "два", "три", "четыре", "пять", "шесть", "семь",
            "восемь", "девять", "десять"};

    public static void main(String[] args) {
        ServerSocket server_socket;
        try {
            server_socket = new ServerSocket(6000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int num;
        while (true) {
            try {
                Socket client_socket = server_socket.accept();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

                while (!client_socket.isClosed()) {
                    System.out.println("Создано соединение между сервером и клиентом");

                    try {
                        num = Integer.parseInt(input.readLine());
                    } catch (SocketException e){
                        System.out.println("Нет связи с клиентом!");
                        break;
                    }
                    if (num == -1) {
                        System.out.println("Получен запрос на уничтожение связи");
                        break;
                    }
                    System.out.println("Получено сообщение: " + num);
                    if (num > -1 && num < words.length) {
                        sendMessage(out, words[num]);
                    } else {
                        sendMessage(out, "Ошибка!");
                    }
                    System.out.println("Сервер отправил сообщение");
                }

                input.close();
                out.close();
                client_socket.close();
                System.out.println("Каналы и сокет закрыты");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendMessage(BufferedWriter out, String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}