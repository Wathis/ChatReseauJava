
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class ServeurChat implements IObservable {

    private int port;
    private ServerSocket socket;
    private List<IObserver> clients;


    public ServeurChat(int port) {
        this.port = port;
        clients = new LinkedList<IObserver>();
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Erreur dans la création du serveur");
        }
    }

    /**
     * Permet de lancer le serveur de chat et d'accepter les requetes des clients
     */
    public void run() {
        System.out.println("Serveur de chat en écoute sur le port " + port + "...");
        while (true) {
            try {
                Socket socketClient = socket.accept();
                // Gerer le nouveau client accepté sur un nouveau thread
                new Thread(new Client( socketClient,this)).start();
            } catch (IOException e) {
                System.out.println("Erreur dans l'accept d'un client\n");
            }
        }
    }

    /**
     * Enregistrer un nouveau client dans les observateurs
     * @param client
     */
    public void enregisterClient(IObserver client) {
        this.clients.add(client);
        envoyerMessageAuxClients(client,((Client)client).getNom() + " est maintenant connecté\n");
    }


    /**
     * Permet de deconnecter proprement un client
     * @param client
     */
    public void deconnecterClient(IObserver client) {
        this.clients.remove(client);
        try {
            ( (Client) client).getSocket().close();
        } catch (IOException e) {
            System.out.println("Problème dans la fermeture de " + ((Client)client).getNom() + "\n");
        }
        envoyerMessageAuxClients(client,((Client)client).getNom() + " est maintenant deconnecté\n");
    }

    /**
     * Permet d'envoyer un message à tous les clients actuellements connectés
     * @param exception Client a qui il ne faut pas envoyer le message
     * @param message Message
     */
    public void envoyerMessageAuxClients(IObserver exception, String message) {
        for (int i = 0; i < clients.size(); i++) {
            // Ne pas envoyer a celui passé en parametre ( L'exception est souvent celui qui a envoyé le message )
            if (!clients.get(i).equals(exception)) {
                clients.get(i).envoyer(message);
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
