package ondemandmbile.crypto_assistant;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertnyangate on 03/07/2017.
 */

public final class FontsOverride {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        replaceFonts(staticTypefaceFieldName,newTypeface);
//        try {
//            final Field staticField = Typeface.class
//                    .getDeclaredField(staticTypefaceFieldName);
//            staticField.setAccessible(true);
//            staticField.set(null, newTypeface);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    protected static void replaceFonts(String staticTypefaceFieldName,
                                       final Typeface newTypeface) {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put("sans-serif", newTypeface);
            newMap.put("sans", newTypeface);
            newMap.put("Roboto", newTypeface);
            newMap.put("Helvetica", newTypeface);
            newMap.put("monospace", newTypeface);
            newMap.put("serif", newTypeface);
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}