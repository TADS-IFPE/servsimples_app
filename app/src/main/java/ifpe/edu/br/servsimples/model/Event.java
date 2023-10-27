/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.model;

import java.util.HashMap;
import java.util.Map;

public class Event {

    public static final int TYPE_PUBLISH = 0;
    public static final int TYPE_SUBSCRIBE = 1;

    private Long id;
    private int type;
    private String description;
    private Long start;
    private Long end;
    private final Map<Long, Boolean> subscribersIds = new HashMap<>();
    private Service service = new Service();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Map<Long, Boolean> getSubscribersIds() {
        return subscribersIds;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
