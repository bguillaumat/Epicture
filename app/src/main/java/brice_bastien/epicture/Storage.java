package brice_bastien.epicture;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

class Storage {

    String[] openData(Context context) throws IOException {
        String FILENAME = "data";
        String token = "";
        String username = "";
        String etc = "";
        FileInputStream fos = context.openFileInput(FILENAME);
        Scanner sc = new Scanner(fos);
        if (sc.hasNextLine()) token = sc.nextLine();
        if (sc.hasNextLine()) username = sc.nextLine();
        if (sc.hasNextLine()) etc = sc.nextLine();
        fos.close();
        String res[] = new String[4];
        res[0] = token;
        res[1] = username;
        res[2] = etc;
        return res;
    }

    void closeData(Context context, String Token, String UserName, String Etc) {
        String FILENAME = "data";
        String finalStr = (Token + '\n' + UserName + '\n' + Etc + '\n');
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            fos.write(finalStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void destroyData(Context context) {
        String FILENAME = "data";
        context.deleteFile(FILENAME);
    }
}
