package CHG4343_Design_Project_ControlSystem;
public class SignalQueue {
    private Signal[] signals;
    public SignalQueue() {
        this.signals = new Signal[0];
    }
    public SignalQueue(SignalQueue source) {
        this.signals = new Signal[source.signals.length];
        for(int i = 0; i < this.signals.length; i++) {
            this.signals[i] = source.signals[i].clone();
        }
    }
    public SignalQueue clone() {
        return new SignalQueue(this);
    }
    public int size() {
        return signals.length;
    }
    public boolean isEmpty() {
        return this.size() == 0;
    }
    public boolean add(Signal s) {
        if(s == null) return false;
        Signal[] tmpSignals = new Signal[this.signals.length + 1];
        for(int i = 0; i < signals.length; i++) {
            tmpSignals[i] = this.signals[i];
        }
        tmpSignals[this.signals.length] = s.clone();
        this.signals = tmpSignals;
        return true;
    }
    public Signal pop() {
        if(this.isEmpty()) return null;
        Signal[] tmpSignals = new Signal[this.signals.length - 1];
        for(int i = 1; i < this.signals.length; i++) {
            tmpSignals[i-1] = this.signals[i];
        }
        Signal retSignal = this.signals[0];
        this.signals = tmpSignals;
        return retSignal;
    }
    private void sort(Signal[] signals) {
        if(signals == null) throw new IllegalArgumentException("Can not sort array of signals, array is null");
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < this.signals.length-1; i++) {
                if(signals[i].time < signals[i+1].time) {
                    sorted = false;
                    Signal tmpSignal = signals[i];
                    signals[i+1] = signals[i];
                    signals[i] = tmpSignal;
                }
            }
        }
    }
    public Signal peek() {
        if(this.isEmpty()) return null;
        return this.signals[0];
    }
    public boolean equals(Object comparator) {
        if(comparator == null || comparator.getClass() != this.getClass()) return false;
        SignalQueue signalQ = (SignalQueue) comparator;
        if(this.signals.length != signalQ.signals.length) return false;
        for(int i = 0; i < this.signals.length; i++) {
            if(this.signals[i] != signalQ.signals[i]) return false;
        }
        return true;
    }
}
