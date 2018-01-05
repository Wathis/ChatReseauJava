import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ServeurChat serveur = new ServeurChat(6565);
        serveur.lancer();
        String reponse = "";
        Scanner sc = new Scanner(System.in);
        do {
            reponse = sc.nextLine();
        } while (!reponse.equals("/shutdown"));
        serveur.stop();
    }
}
