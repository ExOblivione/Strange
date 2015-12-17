/*
 * The string is in the language, if the $ sign occures a reverse.
 * z$z is in the language, and zy$zy is not...
 */

import java.util.*;

/**
 *
 * @author Pardi
 */
public class RecogString {

    public static void main(String args[]) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = keyboard.nextLine();
        String one, two;

        int end = word.indexOf("$");

        if (word.contains("$")) {
            one = word.substring(0, end);
            System.out.println("The word is the first part of the word:" + one);
            two = word.substring(word.lastIndexOf("$") + 1);
            System.out.println("The word is the second part of the word:" + two);

            Stack stack = new Stack();

            for (int i = 0; i < one.length(); i++) {
                stack.push(one.charAt(i));
                System.out.println("The stack: " + stack);
            }

            String two2 = new StringBuilder(two).reverse().toString();
            System.out.println(two2);

            if (two2.equals(one)) {
                System.out.println("The input String is a part of the language.");
            } else {
                System.out.println("The input String is not part of the language.");
            }
        } else {
            System.out.println("There is no $ in this word: " + word);

        }

    }
}
