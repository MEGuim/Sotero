package Canelas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author CFCanelas
 */
public class Arbitros implements Serializable {

    public Map<String, Inimigo> inimigos = new HashMap<String, Inimigo>();

    public Arbitros(Map<String, Inimigo> arbitros) {
        inimigos = new HashMap<String, Inimigo>();
        for (Inimigo arbitro : arbitros.values()) {
            inimigos.put(arbitro.name, arbitro);
        }
    }
}
