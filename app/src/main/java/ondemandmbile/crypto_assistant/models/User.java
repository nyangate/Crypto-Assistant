package ondemandmbile.crypto_assistant.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by robertnyangate on 15/08/2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String id;
    private String dateRegistered;
    private String lastSeen;
    private String userJsonObj;
    private double lessThan;
    private double greaterThan;

    public double getLessThan() {
        return lessThan;
    }

    public void setLessThan(double lessThan) {
        this.lessThan = lessThan;
    }

    public double getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(double greaterThan) {
        this.greaterThan = greaterThan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getUserJsonObj() {
        return userJsonObj;
    }

    public void setUserJsonObj(String userJsonObj) {
        this.userJsonObj = userJsonObj;
    }
}
