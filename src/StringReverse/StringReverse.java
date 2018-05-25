package StringReverse;

/**
 * Created by veronika on 12.03.2018.
 */
public class StringReverse {


    public static void main(String[] args) {
        String candidate = "хуй";
        System.out.println(reverseByArray(candidate));
    }

    public static String reverseByArray(String s) {
        char[] a = s.toCharArray();
        char[] b = new char[a.length];
        for (int i = 0; i < a.length; i++) {
            b[a.length - 1 - i] = a[i];
        }
        return new String(b);

    }
}