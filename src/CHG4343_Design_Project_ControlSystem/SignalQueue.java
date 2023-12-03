package CHG4343_Design_Project_ControlSystem;
public class SignalQueue {
    private Signal[] signals;
    public SignalQueue() {
        this.signals = new Signal[0];
    }
    public SignalQueue(SignalQueue source) {
        this.signals = new Signal[source.signals.length];
        for(int i = 0; i < this.signals.length; i++) {
            this.signals[i] = source.signals[i];
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
    public Signal get() {
        if(this.isEmpty()) return null;
        Signal[] tmpSignals = new Signal[this.signals.length - 1];
        for(int i = 1; i < this.signals.length; i++) {
            tmpSignals[i] = this.signals[i];
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
                if(signals[i].getTime() < signals[i+1].getTime()) {
                    sorted = false;
                    Signal tmpSignal = signals[i];
                    signals[i+1] = signals[i];
                    signals[i] = tmpSignal;
                }
            }
        }
    }
    public double checkLastTime() {
        if(this.isEmpty()) return -1;
        return this.signals[0].getTime();
    }
}
