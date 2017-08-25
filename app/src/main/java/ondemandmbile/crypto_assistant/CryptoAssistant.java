package ondemandmbile.crypto_assistant;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ondemandmbile.crypto_assistant.models.User;

/**
 * Created by robertnyangate on 22/07/2017.
 */

public class CryptoAssistant extends Application {
    public static final String FCMTOKEN = "fcmtoken";
    public static final SimpleDateFormat SDF_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().
                        build();
        Realm.setDefaultConfiguration(config);
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/questrial_regular.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/quicksand_bold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "serif", "fonts/quicksand_regular.ttf");
        FontsOverride.setDefaultFont(this, "arial", "fonts/quicksand_regular.ttf");
//        FontsOverride.setDefaultFont(this, "monospace", "fonts/quicksand_bold.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/quicksand_bold.ttf");

        Logger.d("tokenized");
        try {
            Logger.d(FirebaseInstanceId.getInstance().getToken());
            setUser();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void setUser() {
        Realm realm= Realm.getDefaultInstance();
        if(realm.where(User.class).findAll().isEmpty()){

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user=new User();
                    user.setId(UUID.randomUUID().toString());
                    user.setToken(FirebaseInstanceId.getInstance().getToken());
                    user.setDateRegistered(SDF_FULL.format(new Date()));
                    user.setLastSeen(SDF_FULL.format(new Date()));
                    realm.copyToRealmOrUpdate(user);
                }
            });

        }else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user=realm.where(User.class).findFirst();
                    user.setToken(FirebaseInstanceId.getInstance().getToken());
                    user.setLastSeen(SDF_FULL.format(new Date()));
                    realm.copyToRealmOrUpdate(user);
                }
            });
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        User user=realm.where(User.class).findFirst();
        myRef.child(user.getId()).setValue(""+convertUser());
        realm.close();
    }

    public static JSONObject convertUser(){
        Realm realm= Realm.getDefaultInstance();
        User user=realm.where(User.class).findFirst();
        try {
            JSONObject jUser=new JSONObject();
            jUser.accumulate("id",user.getId());
            jUser.accumulate("token",user.getToken());
            jUser.accumulate("lessThan",user.getLessThan());
            jUser.accumulate("greaterThan",user.getGreaterThan());
            jUser.accumulate("lastseen",user.getLastSeen());
            jUser.accumulate("registered",user.getDateRegistered());
            jUser.accumulate("userJsonObj",user.getUserJsonObj());
            return jUser;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            realm.close();
        }
        return new JSONObject();
    }

}
