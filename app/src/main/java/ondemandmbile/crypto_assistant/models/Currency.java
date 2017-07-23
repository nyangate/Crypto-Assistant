package ondemandmbile.crypto_assistant.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class Currency extends RealmObject {
    @PrimaryKey
    private String name;
    private double buy;
    private double sell;
    private String symbol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
