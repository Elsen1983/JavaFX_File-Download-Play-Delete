package main;

public interface Files_Observable {
    void add(Files_Observer o);
    void remove(Files_Observer o);
    void notifyObservers();

}
