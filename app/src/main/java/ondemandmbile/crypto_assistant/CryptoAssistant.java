package ondemandmbile.crypto_assistant;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class CryptoAssistant extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().
                        build();
        Realm.setDefaultConfiguration(config);
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/questrial_regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/quicksand_medium.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "serif", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "arial", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "monospace", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/quicksand_bold.ttf");
    }
}
