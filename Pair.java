/*
 * Author: My Tran
 * Filename: Pair.java
 * Description: Data container class to store a key and a value member with arbitrary types
 */
public class Pair<F, S> {

    private final F first;  // Member stores the key of the pair object
    private final S second; // Member stores the value of the pair object

    /*
     * Constructor
     * Creates instance of object with non-default key and value
     */
    public Pair(F key, S value) {
        first = key;
        second = value;
    }

    /*
     * Getter for the key member
     */
    public F getKey() {
        return first;
    }

    /*
     * Getter for the value member
     */
    public S getValue() {
        return second;
    }
}
