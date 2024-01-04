/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.model;

public class Notification {

    private Long id;
    private String message = "";
    private boolean isNew = true;
    private long timestamp;
    private long clientId;
    private long professionalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void isNew(boolean isNew) {
        this.isNew = isNew;
    }
}
