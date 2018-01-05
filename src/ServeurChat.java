
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
    }

    /**
     * Permet de lancer le serveur de chat
     */
    public void lancer() {
        new Thread(this).start();
        //On attend la reponse du client
        String reponse = "";
        Scanner sc = new Scanner(System.in);
        do {
            reponse = sc.nextLine();
        } while (!reponse.equals("/shutdown"));
        stop();
    }

    /**
     * Permet de fermer le serveur de chat proprement
     */
    public void stop() {
        Iterator iterator = clients.iterator();
        //On ferme tous les clients
        while(iterator.hasNext()) {
            deconnecterClient((IObserver) iterator.next());
        }
        System.exit(0);
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
     * Permet de donner la liste des clients actuellements connéctés au serveur
     * @return
     */
    public String listePersonnesConnectees() {
        StringBuilder str = new StringBuilder();
        Iterator iterator = clients.iterator();
        if (!iterator.hasNext()) {
            str.append("[INFO] Aucun utilisateur n'est actuellement connecté\n");
        } else {
            str.append("[INFO] Liste des utilisateurs connectés :\n");
            while (iterator.hasNext())  {
                Client client = (Client) iterator.next();
                str.append("\t- " + client.getNom() + "\n");
            }
        }
        return str.toString();
    }

    /**
     * Enregistrer un nouveau client dans les observateurs
     * Permet de respecter le patron obervateur, ainsi, quand un obervateur sera créé, il devra s'enregistrer auprès
     * de la classe qu'il observe comme le dit le patron observateur
     * @param client
     */
    public void enregisterClient(IObserver client) {
        this.clients.add(client);
        boiteAuxLettres.put(((Client)client).getNom() + " est maintenant connecté\n");
        envoyerMessage(client);
    }


    /**
     * Permet de deconnecter proprement un client
     * Permet ainsi a tout observateur d'arrêter d'observer la classe serveur comme le veut le patron observateur
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
     * Synchronized car un Thread doit executer toute la méthode sans interuptions
     * @param exception Client a qui il ne faut pas envoyer le message
     */
    public synchronized void envoyerMessage(IObserver exception) {
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
