import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Client implements Runnable {

    private Socket socket;
    private String nom;
    private BufferedReader input;
    private BufferedWriter output;


    public Client(Socket socket, String nom) {
        this.socket = socket;
        this.nom = nom;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    

    public void envoyer(String msg) {
        try {

            output.write(msg);
            output.flush();
        } catch (java.io.IOException e) {

            e.printStackTrace();
        }
    }

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
