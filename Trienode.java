/*Test your implementation on different scenarios with edge cases to be sure that your
program handles all possible inputs.

Your implementation may be tested on different dictionaries having any number of
words. Your implementation should work in constant time (or linear time with respect
to number of characters of the strings given), otherwise you will get no credits.

For this assignment, you ARE ALLOWED to use the codes given in our textbook and/or
our lecture slides. 
You ARE NOT ALLOWED to use any codes from somewhere else (e.g.,
from the internet, other textbooks, other slides, etc.). You ARE NOT ALLOWED to use
external libraries including the libraries given in the textbook.*/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String dictionary;

        /* System.out.println("write down the words for dictionary."); */

        dictionary = input.nextLine();

        String words[] = dictionary.split(" ");// inputun kelimelerini ayirdi
        Trienode trie = new Trienode();
        for (String word : words) {
            trie.insertWord(word);
        }

        while (true) {
            String newWord = input.nextLine();// kullanici kelime girerek test ediyor
            newWord = newWord.toLowerCase();
            if (newWord.equals("exit"))
                break;
            if (trie.searchWord(newWord) == true) {
                System.out.println("Correct Word");
            }
            if (trie.searchWord(newWord) == false) {
                List<String> suggestions = trie.getSuggestion(newWord);
                if (suggestions.isEmpty())
                    System.out.println("No Suggestions");
                else
                    System.out.print("Misspelled? ");

                for (int i = 0; i < suggestions.size(); i++) {
                    System.out.print(suggestions.get(i) + " ");
                }
            }

        }

    }

}

public class Trienode {
    int R = 256;// ASCII
    Node root = new Node();

    class Node {

        Node[] next = new Node[R];
        boolean isWord = false;
    }

    public void insertWord(String word) {
        Node node = root;

        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            int index = c;
            if (node.next[index] == null) {
                node.next[index] = new Node();// eğer bos ise harfi ekliyor
            }
            node = node.next[index];// devam ediyor

        }
        node.isWord = true;// for loop bitince kelime oldugunu belirtiyor.
    }

    public boolean searchWord(String word) {
        word = word.toLowerCase();
        Node node = root;// roottan basliyor

        for (int i = 0; i < word.length(); ++i) {// her bir harf icin kontrol edecegiz
            char c = word.charAt(i);
            int index = c;// harfin ascii degeri aliniyor
            if (node.next[index] == null)
                return false; // yol yoksa o harften false
            if (node.next[index] != null) {
                node = node.next[index];// eger varsa devam etcek ve bir alt nodea gececek

            }

        }
        if (node.isWord == true)
            return true;// loop sonunda eger nodedaki kelime tamsa true dondurcek degilse false
        return false;
    }

    public List<String> getSuggestion(String word) {
        word = word.toLowerCase();
        Node node = root;

        String prefix = "";

        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            int index = c;
            if (node.next[index] == null)
                break;// devam etmiyorsa loop duruyor
            else
                node = node.next[index];// devam ediyorsa sonraki nodea geciyor
            prefix = prefix + c;// gittigi yere kadar elimizde stringi tutuyor

        }
        List<String> result = new ArrayList<>();

        getWords(node, prefix, result);

        return result;
    }

    public void getWords(Node n, String prefix, List<String> result) {

        if (result.size() == 3)
            return;// 3 tane oneri varsa yeterli
        if (n.isWord)
            result.add(prefix);// onerileri result listesine ekliyor

        for (char c = 0; c < 256; c++) {// her ascii icin
            if (n.next[c] != null) {// bu harfle devam eden yol var demek oluyor
                String prefix2 = prefix + c;// yeni yoıl icin
                getWords(n.next[c], prefix2, result);// recursive yaparak aynı
            }

        }

    }
}
