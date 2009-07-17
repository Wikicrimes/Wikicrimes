package org.wikicrimes.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;
import java.io.InvalidObjectException;

public final class Horario implements Comparable, Serializable {
    private static int size = 0;

    private static int nextOrd = 0;

    private static Map nameMap = new HashMap(34);

    private static Horario first = null;

    private static Horario last = null;

    private final int ord;

    private final String label;

    private Horario prev;

    private Horario next;

    public static final Horario MEIANOITE = new Horario("0:00", 0);

    public static final Horario HORA_1 = new Horario("1:00", 1);

    public static final Horario HORA_2 = new Horario("2:00", 2);

    public static final Horario HORA_3 = new Horario("3:00", 3);

    public static final Horario HORA_4 = new Horario("4:00", 4);

    public static final Horario HORA_5 = new Horario("5:00", 5);

    public static final Horario HORA_6 = new Horario("6:00", 6);

    public static final Horario HORA_7 = new Horario("7:00", 7);

    public static final Horario HORA_8 = new Horario("8:00", 8);

    public static final Horario HORA_9 = new Horario("9:00", 9);

    public static final Horario HORA_10 = new Horario("10:00", 10);

    public static final Horario HORA_11 = new Horario("11:00", 11);

    public static final Horario MEIODIA = new Horario("12:00", 12);

    public static final Horario HORA_13 = new Horario("13:00", 13);

    public static final Horario HORA_14 = new Horario("14:00", 14);

    public static final Horario HORA_15 = new Horario("15:00", 15);

    public static final Horario HORA_16 = new Horario("16:00", 16);

    public static final Horario HORA_17 = new Horario("17:00", 17);

    public static final Horario HORA_18 = new Horario("18:00", 18);

    public static final Horario HORA_19 = new Horario("19:00", 19);

    public static final Horario HORA_20 = new Horario("20:00", 20);

    public static final Horario HORA_21 = new Horario("21:00", 21);

    public static final Horario HORA_22 = new Horario("22:00", 22);

    public static final Horario HORA_23 = new Horario("23:00", 23);

   

    /**
     * Constructs a new Horario with its label.<br>
     * (Uses default value for ord.)
     */
    private Horario(String label) {
	this(label, nextOrd);
    }

    /**
     * Constructs a new Horario with its label and ord value.
     */
    private Horario(String label, int ord) {
	this.label = label;
	this.ord = ord;

	++size;
	nextOrd = ord + 1;

	nameMap.put(label, this);

	if (first == null)
	    first = this;

	if (last != null) {
	    this.prev = last;
	    last.next = this;
	}

	last = this;
    }

    /**
     * Compares two Horario objects based on their ordinal values.
     * Satisfies requirements of interface java.lang.Comparable.
     */
    public int compareTo(Object obj) {
	return ord - ((Horario) obj).ord;
    }

    /**
     * Compares two Horario objects for equality.  Returns true
     * only if the specified Horario is equal to this one.
     */
    public boolean equals(Object obj) {
	return super.equals(obj);
    }

    /**
     * Returns a hash code value for this Horario.
     */
    public int hashCode() {
	return super.hashCode();
    }

    /**
     * Resolves deserialized Horario objects.
     * @throws InvalidObjectException if deserialization fails.
     */
    private Object readResolve() throws InvalidObjectException {
	Horario h = get(label);

	if (h != null)
	    return h;
	else {
	    String msg = "invalid deserialized object:  label = ";
	    throw new InvalidObjectException(msg + label);
	}
    }

    /**
     * Returns Horario with the specified label.
     * Returns null if not found.
     */
    public static Horario get(String label) {
	return (Horario) nameMap.get(label);
    }

    /**
     * Returns the label for this Horario.
     */
    public String toString() {
	return label;
    }

    /**
     * Always throws CloneNotSupportedException;  guarantees that
     * Horario objects are never cloned.
     *
     * @return (never returns)
     */
    protected Object clone() throws CloneNotSupportedException {
	throw new CloneNotSupportedException();
    }

    /**
     * Returns an iterator over all Horario objects in declared order.
     */
    public static Iterator iterator() {
	// annnonymous inner class
	return new Iterator() {
	    private Horario current = first;

	    public boolean hasNext() {
		return current != null;
	    }

	    public Object next() {
		Horario h = current;
		current = current.next();
		return h;
	    }

	    public void remove() {
		throw new UnsupportedOperationException();
	    }
	};
    }

    /**
     * Returns the ordinal value of this Horario.
     */
    public int ord() {
	return this.ord;
    }

    /**
     * Returns the number of declared Horario objects.
     */
    public static int size() {
	return size;
    }

    /**
     * Returns the first declared Horario.
     */
    public static Horario first() {
	return first;
    }

    /**
     * Returns the last declared Horario.
     */
    public static Horario last() {
	return last;
    }

    /**
     * Returns the previous Horario before this one in declared order.
     * Returns null for the first declared Horario.
     */
    public Horario prev() {
	return this.prev;
    }

    /**
     * Returns the next Horario after this one in declared order.
     * Returns null for the last declared Horario.
     */
    public Horario next() {
	return this.next;
    }
}
