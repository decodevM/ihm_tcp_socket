package org.decodev.additional;//package org.decodev.supplimentaire;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EmployeeServer {

    private static final String DATA_FILE_PATH = "src/main/resources/employee.txt";

    public static void main(String[] args) {
        System.out.println("Server started, Waiting for connection...");
        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                handleClient(socket);
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            String inputValueFromClient;
            while ((inputValueFromClient = dis.readUTF()) != null) {
                processInput(inputValueFromClient, dos);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private static void processInput(String input, DataOutputStream dos) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            String message = "";
            int sendingOption = 0; // 0 for message 1 for count
            int count = 0;

            String[] inputParts = input.split("\\|");
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String fullName = parts[0] + " " + parts[1];
                    String genre = parts[2];
                    String birthdate = parts[3];
                    String date = parts[4];

                    if(inputParts[0].equalsIgnoreCase("1")){

                        message = processPart1(inputParts[1],genre , fullName, birthdate, date);
                        sendingOption = 0;
                        if(!message.equalsIgnoreCase("Not Found")){
                            break;
                        }
                    }else if(inputParts[0].equalsIgnoreCase("2")){
                        count += processPart2(inputParts[1], birthdate);
                        sendingOption = 1;
                    }else if(inputParts[0].equalsIgnoreCase("3")){
                        count += processPart3(inputParts[1], birthdate, date);
                        sendingOption = 1;
                    }
                }
            }
            if(sendingOption == 0) {
                dos.writeUTF(message);
                dos.flush();
            } else if(sendingOption == 1) {
                dos.writeUTF(String.valueOf(count));
                dos.flush();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error processing input: " + e.getMessage());
        }
    }

    private static String processPart1(String input, String employeeGenre, String fullName, String birthdate, String date) throws IOException {
        if (fullName.toLowerCase().equals(input.toLowerCase())) {
            int currentYear = 2024;
            int age = currentYear - Integer.parseInt(birthdate);
            int workYears = currentYear - Integer.parseInt(date);
            return (employeeGenre.equalsIgnoreCase("H") && (age >= 60 || workYears >= 35))
                    || (employeeGenre.equalsIgnoreCase("F") && (age >= 55 || workYears >= 30))
                    ? "Retired"
                    : "Not retired";
        }
        return "Not Found";
    }

    private static int processPart2(String input, String birthdate) {
        return input.equalsIgnoreCase(birthdate) ? 1 : 0;
    }

    private static int processPart3(String input, String birthdate, String date) {
        String[] dateParts = input.split("-");
        return birthdate.equalsIgnoreCase(dateParts[0]) && date.equalsIgnoreCase(dateParts[1]) ? 1 : 0;
    }
}
