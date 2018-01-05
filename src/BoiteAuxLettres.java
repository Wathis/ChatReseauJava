/**
 * Cette classe permet de stocker le dernier message a envoyer pour permettre un synchronisation
 * dans l'envoie de message
 */

public class BoiteAuxLettres {

    private String messageAEnvoyer;

    /**
     * Permet de mettre quelque chose dans l'attribut qui contient le message a envoyer
     * @param message
     */
    public synchronized void put(String message) {
        while (messageAEnvoyer != null) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        messageAEnvoyer = message;

        //On reveille tous les autres threads
        notifyAll();
        return;
    }


    /**
     * Permet de consomer le contenu de attribut qui contient le message a envoyer
     */

    public synchronized String get() {
        while (messageAEnvoyer == null) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        String tmp = messageAEnvoyer.toString();
        messageAEnvoyer = null;

        //On reveille tous les autres threads
        notifyAll();
        return tmp;
    }

}
