import java.io.*;

/*
 * Group 3: Louis Nguyen, Sarah King, Kha Hoang, Khoa Pham
 * Group responsible for this program: Louis Nguyen, Khoa Pham
 * Assignment Name: Programming Project
 * Description: This program takes in a file that was previously compressed
 * and decompress that file using Huffman coding.
 */

public class Decompress_a_File {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream source = new FileInputStream(args[0]);
        ObjectInputStream sourceObj = new ObjectInputStream(source);
        String[] huffmanObj = (String[]) (sourceObj.readObject());

        /*
         * After reading the HuffmanTree Object, we can go ahead and look for our
         * encoded text. We're actually reading through the whole file
         * but we can trim later by looking for an integer we wrote to our file.
         */

        StringBuilder encodedText = getEncoded(source, sourceObj.readInt());
        StringBuilder result = decodeToText(encodedText, huffmanObj);
        if (result == null) {
            System.out.print("Error! Decode to text failed!");
            System.exit(1);
        }

        FileOutputStream output = new FileOutputStream(args[1]);
        output.write(result.toString().getBytes());
        output.close();
    }

    /*
     * Returns a binary integer from the data received
     */
    public static String getBits(int value) {
        value = value % 256; // 256 possible values
        String binaryInteger = "";
        int i = 0;
        int tmp = value >> i;
        for (int j = 0; j < 8; j++) {
            binaryInteger = (tmp & 1) + binaryInteger;
            i++;
            tmp = value >> i;
        }
        return binaryInteger;
    }

    /*
     * Decodes the encoded String into readable text
     */
    public static StringBuilder decodeToText(StringBuilder encodedText, String[] huffmanObj) {
        StringBuilder result = new StringBuilder();
        while (encodedText.length() != 0) {
            for (int i = 0; i < huffmanObj.length; i++) {
                if ((huffmanObj[i] != null) && (encodedText.indexOf(huffmanObj[i]) == 0)) {
                    result.append((char) i);
                    encodedText.delete(0, huffmanObj[i].length());
                }
            }
        }
        if (result.length() == 0) {
            return null;
        }
        return result;
    }

    /*
     * Takes in the compressed file from the FileInputStream and reads the data from
     * the file to build a String of binary digits 
     */
    public static StringBuilder getEncoded(FileInputStream source, int cutOff) throws IOException {
        StringBuilder encoded = new StringBuilder("");
        int readerPosition = 0;
        while ((readerPosition = source.read()) != -1) {
            encoded.append(getBits(readerPosition));
        }
        // System.out.println(encoded.length());
        encoded.delete(cutOff, encoded.length());
        return encoded;
    }
}