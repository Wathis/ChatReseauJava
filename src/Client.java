import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Client implements Runnable, IObserver {

    private Socket socket;
    private String nom;
    private BufferedReader input;
    private BufferedWriter output;
    private ServeurChat serveur;


    public Client(Socket socket, ServeurChat serveur) {
        this.socket = socket;
        this.serveur = serveur;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.demanderNom();
        //On enregistre le client auprès du serveur
        serveur.enregisterClient(this);
        this.communiquerAvecClient();
        serveur.deconnecterClient(this);
    }

    /**
     * Boucle permettant de communiquer avec le client
     */
    private void communiquerAvecClient() {
        String message;
        do {
            message = ecouter();
            //On envoie le message a tous les clients connectés, sauf le client actuel
            if (!message.equals("/q")) {
                serveur.envoyerMessageAuxClients(this, "[" + this.nom + "] " + message + "\n");
            }
        } while (!message.equals("/q")); //On quitte quand la reponse du client est "/q"
    }

    /**
     * Permet de connaitre le nom du client pour qu'il soit identifie
     */
    private void demanderNom() {
        envoyer("Quel est votre nom ?\n");
        this.nom = ecouter();
    }


    /**
     * Permet d'envoyer un message au client
     * @param msg
     */
    public void envoyer(String msg) {
        try {
            output.write(msg);
            output.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'attendre un message du client, methode bloquante
     * @return
     */
    public String ecouter() {
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    public void setOutput(BufferedWriter output) {
        this.output = output;
    }
}
