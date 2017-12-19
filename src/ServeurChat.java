
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class ServeurChat {

    private int port;
    private ServerSocket socket;
    private List<Client> clients;


    public ServeurChat(int port) {
        this.port = port;
        clients = new LinkedList<Observateur>();
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
                new Thread(new Client(this, socketClient)).start();
            } catch (IOException e) {
                System.out.println("Erreur dans l'accept d'un client");
            }
        }
    }

    /**
     * Permet de donner la liste des clients actuellement connectés
     * @return le toString de la liste des client connectés
     */
    public String listeClientConnectes() {
        StringBuilder str = new StringBuilder();
        str.append("Membres connectés : \n");
        for (int i = 0; i < clients.size(); i++) {
            str.append("\t- " + clients.get(i).getName() + "\n");
        }
        return str.toString();
    }

    /**
     * Permet d'envoyer un message à tous les clients actuellements connectés
     * @param exception Client a qui il ne faut pas envoyer le message
     * @param message Message
     */
    public void envoyerMessageAuxClients(Client exception, String message) {
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
