/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.model;

public class Cost {
    private String value;
    private String time;

    public Cost() {
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public String getTime() {
        return time;
    }
}
