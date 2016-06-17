package com.example.paranoid.geoloc_gid;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseContext extends ContextWrapper{

    Context mycontext;
    public DataBaseContext(Context base) {
        super(base);
        mycontext = base;
    }

    private boolean checkdatabase(String name) {

        boolean checkdb = false;
        try {
            File dbfile = new File(mycontext.getFilesDir(), name);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
        }
        return checkdb;
    }

    public void createdatabase(String name) {
        boolean dbexist = checkdatabase(name);
        if(!dbexist) {
            try {
                copydatabase(name);
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copydatabase(String name) throws IOException {
        //Open your local db as the input stream
        InputStream myinput = mycontext.getAssets().open(name);

        //Open the empty db as the output stream
        FileOutputStream outputStream = mycontext.openFileOutput(name, Context.MODE_PRIVATE);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            outputStream.write(buffer,0,length);
        }

        //Close the streams
        outputStream.flush();
        outputStream.close();
        myinput.close();
    }

    @Override
    public File getDatabasePath(String name){
        boolean dbexist = checkdatabase(name);
        if (!dbexist)
            createdatabase(name);

        return new File(mycontext.getFilesDir(), name);
    }
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory){
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}
