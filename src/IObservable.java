/**
 * Interface Obervable du patron observateur
 */
public interface IObservable {
    void enregisterClient(IObserver client);
    void deconnecterClient(IObserver client);
    void envoyerMessage(IObserver exception);
}
