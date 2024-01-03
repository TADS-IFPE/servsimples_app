/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */

package ifpe.edu.br.servsimples.ui.agenda;

import ifpe.edu.br.servsimples.model.User;

public class AppointmentWrapper {
    private User client;
    private User professional;

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getProfessional() {
        return professional;
    }

    public void setProfessional(User professional) {
        this.professional = professional;
    }
}
