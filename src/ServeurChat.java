
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
/**
 * La classe ServeurChat represente le serveur TCP de chat. Il implemente IObservable pour respecter le
 * patron observateur. Le ServeurChat est l'observé, et les observateurs sont les client du chat TCP ( classe Client )
 */
public class ServeurChat {

    private int port;
    private ServerSocket socket;
    private List<Client> clients;
    private BoiteAuLettres boiteAuLettres;
    private BoiteEnvoie boiteEnvoie;

    /**
     * Constructeur du Serveur de chat
     * @param port Port sur lequel le serveur de chat doit tourner
     */
    public ServeurChat(int port) {
        this.port = port;
        this.boiteAuLettres = new BoiteAuLettres();
        this.boiteEnvoie = new BoiteEnvoie(boiteAuLettres,this);
        clients = new LinkedList<Client>();
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Erreur dans la création du serveur");
        }
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
    public void enregisterClient(Client client) {
        this.clients.add(client);
        boiteAuLettres.put((client).getNom() + " est maintenant connecté\n");
        boiteEnvoie.envoyerMessage(client);
    }


    /**
     * Permet de deconnecter proprement un client
     * @param client
     */
    public void deconnecterClient(Client client) {
        this.clients.remove(client);
        try {
            client.getSocket().close();
        } catch (IOException e) {
            System.out.println("Problème dans la fermeture de " + ((Client)client).getNom() + "\n");
        }
        boiteAuLettres.put(client.getNom() + " est maintenant deconnecté\n");
        boiteEnvoie.envoyerMessage(client);
    }

    /**
     * Getters et setters
     * @return
     */

    public BoiteAuLettres getBoiteAuxLettres() {return this.boiteAuLettres;}

    public BoiteEnvoie getBoiteEnvoie() {return this.boiteEnvoie;}

    public List<Client> getClients() {
        return this.clients;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
