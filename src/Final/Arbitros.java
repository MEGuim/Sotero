/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author meguim
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
