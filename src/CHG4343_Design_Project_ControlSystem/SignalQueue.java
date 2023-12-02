package CHG4343_Design_Project_ControlSystem;
public class SignalQueue {
    private Signal[] signals;
    public SignalQueue() {
        this.signals = new Signal[0];
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

    /*private Signal[] sort(Signal[] signals) {
        if(signals == null) throw new IllegalArgumentException("Can not sort array of signals, array is null");
        Signal[] sortedSignals = new Signal[signals.length];
        boolean sorted = false;

    }*/
}
