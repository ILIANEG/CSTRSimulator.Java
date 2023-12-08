package CHG4343_Design_Project_ControlSystem;

import java.util.LinkedList;

/**
 * Signal queue to store, add and take signals
 */
public class SignalQueue {
    private LinkedList<Signal> signals; // linked list that stores signals

    /**
     * Constructor for signal queue, initializing an empty queue
     */
    public SignalQueue() {
        this.signals = new LinkedList<Signal>();
    }
    public SignalQueue(SignalQueue source) {
        LinkedList<Signal> tmpList = new LinkedList<Signal>();
        for(int i = 0; i < source.signals.size(); i++) {
            tmpList.add(source.signals.get(i).clone());
        }
        this.signals = tmpList;
    }
    public SignalQueue clone() {
        return new SignalQueue(this);
    }
    public int size() {
        return signals.size();
    }
    public boolean isEmpty() {
        return this.size() == 0;
    }
    public void add(Signal s) {
        if(s == null) throw new IllegalArgumentException("Invalid signal is being added to the signal queue");
        this.signals.add(s.clone());
    }
    public Signal pop() {
        if(this.isEmpty()) return null;
        Signal retSignal = this.signals.getFirst().clone();
        this.signals.removeFirst();
        return retSignal;
    }

    public Signal peek() {
        if(this.isEmpty()) return null;
        return this.signals.getFirst().clone();
    }
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        SignalQueue s = (SignalQueue) comparator;
        if(this.signals.size() != s.signals.size()) return false;
        for(int i = 0; i < this.signals.size(); i++) {
            if(!this.signals.get(i).equals(s.signals.get(i))) return false;
        }
        return true;
    }
}
