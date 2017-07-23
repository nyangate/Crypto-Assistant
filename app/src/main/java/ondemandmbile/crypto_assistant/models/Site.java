package ondemandmbile.crypto_assistant.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by robertnyangate on 23/07/2017.
 */

public class Site extends RealmObject {
    private String url;
    @PrimaryKey
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
