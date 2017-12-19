public interface Sujet {


    void enregistrerObservateur(Observateur observateur);
    void supprimerObservateur(Observateur observateur);
    void notifierObservateurs(String message, Observateur exception);

}
