public class BoiteAuxLettres {

    private String messageAEnvoyer;

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
