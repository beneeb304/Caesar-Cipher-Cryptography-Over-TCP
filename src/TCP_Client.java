/*
 * Description: A simple TCP client that sends a rectangle's length and width values
 * to the server and requests for corresponding area from the server.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Client
{
    public static void main(String[] args) throws Exception
    {
        //Create a char variable for the shift direction
        char chrShift = '1';
        //Create an int variable for the shift amount
        int intShift = 0;

        Socket socket = new Socket("localhost", 4000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())));

        System.out.println("----------SSN Lookup----------");

        //Get data from user
        Scanner sc = new Scanner((System.in));

        //Read in an R or L for shift direction
        while(chrShift != 'R' && chrShift != 'r' && chrShift != 'L' && chrShift != 'l') {
            System.out.println("Enter your Caesar cipher shift direction (R/L).");
            chrShift = sc.next().charAt(0);
        }

        //Read in an integer for shift direction
        while(true) {
            System.out.println("Enter your Caesar cipher shift amount as an integer (0 < x < 26).");
            String strShift = sc.next();

            try {
                //Convert to int
                intShift = Integer.parseInt(String.valueOf(strShift));

                //Check if it's greater than 0 and less than 26
                if(intShift > 0 && intShift < 26) {
                    break;
                }
            } catch(Exception e) {}
        }

        //Read in plain message
        Scanner scMessage = new Scanner(System.in);
        System.out.println("Enter the person's first and last names for SSN lookup.");
        String strPlainMessage = scMessage.nextLine();

        //Send to server
        out.println(chrShift + "\t" + intShift + "\t" + encryptData(strPlainMessage, chrShift, intShift));

        //Recieve from the server and decrypt
        System.out.println("The person's SSN is " + decryptData(in.readLine(), chrShift, intShift));

        sc.close();
        socket.close();
    }

    public static String encryptData(String strPlainText, char chrShift, int intShift) {
        //Variable for encrypted message
        String strEncryptedMessage = "";

        for(int i = 0; i < strPlainText.length(); i++) {
            //Cast to uppercase
            char chrEncrypt = Character.toUpperCase(strPlainText.charAt(i));

            //If the character is a letter, encrypt
            if(Character.isLetter(chrEncrypt)) {
                int intASCII = chrEncrypt;

                if(chrShift == 'R' || chrShift == 'r') {
                    intASCII += intShift;

                    //Uppercase letter that is out of bounds
                    if(intASCII > 90) {
                        intASCII -= 26;
                    }
                } else {
                    intASCII -= intShift;

                    //Uppercase letter that is out of bounds
                    if(intASCII < 65) {
                        intASCII += 26;
                    }
                }

                strEncryptedMessage += (char) intASCII;
            } else {
                strEncryptedMessage += strPlainText.charAt(i);
            }
        }
        return strEncryptedMessage;
    }

    public static String decryptData(String strCipherText, char chrShift, int intShift) {
        //Variable for encrypted message
        String strPlainText = "";

        for(int i = 0; i < strCipherText.length(); i++) {
            //Cast to uppercase
            char chrEncrypt = Character.toUpperCase(strCipherText.charAt(i));

            //If the character is a letter, encrypt
            if(Character.isLetter(chrEncrypt)) {
                int intASCII = chrEncrypt;

                if(chrShift == 'R' || chrShift == 'r') {
                    intASCII -= intShift;

                    //Uppercase letter that is out of bounds
                    if(intASCII < 65) {
                        intASCII += 26;
                    }
                } else {
                    intASCII += intShift;

                    //Uppercase letter that is out of bounds
                    if(intASCII > 90) {
                        intASCII -= 26;
                    }
                }

                strPlainText += (char) intASCII;
            } else {
                strPlainText += strCipherText.charAt(i);
            }
        }
        return strPlainText;
    }

}