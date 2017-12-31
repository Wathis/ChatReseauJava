
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/**
 * La classe ServeurChat represente le serveur TCP de chat. Il implemente IObservable pour respecter le
 * patron observateur. Le ServeurChat est l'observé, et les observateurs sont les client du chat TCP ( classe Client )
 */
public class ServeurChat implements Runnable, IObservable {

    private int port;
    private ServerSocket socket;
    private List<IObserver> clients;
    private BoiteAuxLettres boiteAuxLettres;

    /**
     * Constructeur du Serveur de chat
     * @param port Port sur lequel le serveur de chat doit tourner
     */
    public ServeurChat(int port) {
        this.port = port;
        this.boiteAuxLettres = new BoiteAuxLettres();
        clients = new LinkedList<IObserver>();
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Erreur dans la création du serveur");
        }
        new Thread(this).start();
    }

    /**
     * Permet de lancer le serveur de chat et d'accepter les requetes des clients qui veulent rejoindre le serveur
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
        boiteAuxLettres.put(((Client)client).getNom() + " est maintenant connecté\n");
        envoyerMessage(client);
    }


    /**
     * Permet de deconnecter proprement un client
     * @param client
     */
    public void deconnecterClient(IObserver client) {
        this.clients.remove(client);
        try {
            ((Client)client).getSocket().close();
        } catch (IOException e) {
            System.out.println("Problème dans la fermeture de " + ((Client)client).getNom() + "\n");
        }
        boiteAuxLettres.put(((Client)client).getNom() + " est maintenant deconnecté\n");
        envoyerMessage(client);
    }

    /**
     * Permet d'envoyer un message à tous les clients actuellements connectés
     * @param exception Client a qui il ne faut pas envoyer le message
     */
    public void envoyerMessage(IObserver exception) {
        //On recupere le message dans la boite aux lettres
        String message = boiteAuxLettres.get();
        //On l'envoie a tous les clients
        Iterator iterateur = clients.iterator();
        while (iterateur.hasNext()) {
            IObserver client = (IObserver) iterateur.next();
            // Ne pas envoyer a celui passé en parametre ( L'exception est souvent celui qui a envoyé le message )
            if (!client.equals(exception)) {
                client.envoyer(message);
            }
        }
    }

    /**
     * Getters et setters
     * @return
     */

    public BoiteAuxLettres getBoiteAuxLettres() {return this.boiteAuxLettres;}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
