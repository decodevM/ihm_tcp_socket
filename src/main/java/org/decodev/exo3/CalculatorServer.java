package org.decodev.exo3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.BiFunction;

public class CalculatorServer {
    static HashMap<String, BiFunction<Double, Double, Double>> operations = new HashMap<>();
    static {
        operations.put("ADD", Double::sum);
        operations.put("MUL", (a, b) -> a * b);
        operations.put("SOUS", (a, b) -> a - b);
        operations.put("DIV", (a, b) -> a / b);
        operations.put("PUIS", Math::pow);
    }
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(6666)) {
            while (true) {

                Socket socket = server.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String request = "";
                while((request = dis.readUTF()) != null){

                    String operationRequest = request.trim();
                    System.out.println("Operation received from client: " + operationRequest);

                    String[] operationArgs = operationRequest.split(" ");
                    String resultMessage;

                    if (operationArgs.length == 3) {
                        String operation = operationArgs[0];
                        double a, b;
                        try {
                            a = Double.parseDouble(operationArgs[1]);
                            b = Double.parseDouble(operationArgs[2]);
                            if (operations.containsKey(operation.toUpperCase())) {
                                double result = operations.get(operation.toUpperCase()).apply(a, b);
                                resultMessage = formatResult(result);
                            } else {
                                resultMessage = "Invalid operation";
                            }
                        } catch (NumberFormatException e) {
                            resultMessage = "Invalid number format";
                        }
                    } else {
                        resultMessage = "Invalid operation request";
                    }
                    dos.writeUTF(resultMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to format the result (convert to integer if already an integer)
    private static String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }

}
