/**
 * Interface Obervable du patron observateur
 */
public interface IObservable {
    void enregisterClient(IObserver client);
    void deconnecterClient(IObserver client);
    void envoyerMessageAuxClients(IObserver exception, String message);
}
