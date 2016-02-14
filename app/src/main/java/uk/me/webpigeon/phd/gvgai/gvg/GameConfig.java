package uk.me.webpigeon.phd.gvgai.gvg;

import java.io.Serializable;

/**
 * Created by webpigeon on 14/02/16.
 */
public class GameConfig implements Serializable {
    private String name;
    public String gameDef;
    public String[] levelDef;

    public GameConfig(String name){
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
