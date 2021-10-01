import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Handle_Client implements Runnable{
    Socket socket;

    Handle_Client(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            //Get client's message
            Scanner sc = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String strClientMessage = in.readLine();

            //Parse the data from client message (split with tab)
            String[] strParsed = strClientMessage.split(("\t"));
            char chrShift = strParsed[0].charAt(0);
            int intShift = Integer.parseInt(strParsed[1]);

            //Decrypt the cipher
            String strName = decryptData(strParsed[2], chrShift, intShift);

            //Read Person/SSN File, lookup SSN
            String strSSN = lookupSSN(strName, readFile());

            //Encrypt the SSN and return to client
            out.println(encryptData(strSSN, chrShift, intShift));
        } catch(Exception e) {}
    }

    public static ArrayList<String> readFile() throws IOException {
        //Get local file that stores the SSNs
        File myObj = new File("SSNs.txt");

        //Read the file contents into an arraylist
        Scanner myReader = new Scanner(myObj);
        ArrayList<String> arrFile = new ArrayList<String>();

        while (myReader.hasNextLine()) {
            arrFile.add(myReader.nextLine().toUpperCase());
        }
        myReader.close();

        return arrFile;
    }

    public static String lookupSSN(String strName, ArrayList<String> arrayList) {
        //Return string
        String strReturn = "";
        boolean blnFound = false;

        //Cycle through each element in the arraylist
        for (String strElement: arrayList) {
            //Check if the current element matches the name
            if(strElement.substring(0, strElement.indexOf(",")).equals(strName)) {
                //Get the SSN (after comma in file)
                strReturn = strElement.substring(strElement.indexOf(",") + 1).trim();

                //Set flag
                blnFound = true;
            }
        }

        //If the name was not found, return "Not Found"
        if(!blnFound) {strReturn = "Not Found";}

        return strReturn;
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