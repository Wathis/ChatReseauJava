public class BoiteEnvoie {

    private BoiteAuLettres boitesAuxLettres;
    private ServeurChat serveur;

    public BoiteEnvoie(BoiteAuLettres boiteAuLettres, ServeurChat serveur) {
        this.boitesAuxLettres = boiteAuLettres;
        this.serveur = serveur;
    }

    /**
     * Permet d'envoyer un message à tous les clients actuellements connectés
     * @param exception Client a qui il ne faut pas envoyer le message
     */
    public void envoyerMessage(Client exception) {
        //On recupere le message dans la boite aux lettres
        String message = boitesAuxLettres.get();
        //On l'envoie a tous les clients
        for (int i = 0; i < serveur.getClients().size(); i++) {
            // Ne pas envoyer a celui passé en parametre ( L'exception est souvent celui qui a envoyé le message )
            if (!serveur.getClients().get(i).equals(exception)) {
                serveur.getClients().get(i).envoyer(message);
            }
        }
    }

}
