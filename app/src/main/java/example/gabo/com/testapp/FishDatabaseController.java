package example.gabo.com.testapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FishDatabaseController {

    FishDBHelper helper;
    SQLiteDatabase db;

    public FishDatabaseController(Context context){
        helper = new FishDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insert(FishEntry fish){

        ContentValues values = new ContentValues();
        values.put(FishEntry.COLUMN_NAME, fish.getName());
        values.put(FishEntry.COLUMN_SPECIES, fish.getSpecies());
        values.put(FishEntry.COLUMN_AMOUNT, fish.getAmount());

        long inserted = db.insert(FishEntry.TABLE_NAME , null, values);
        return inserted;
    }

    public long insert(UserEntry user){

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER, user.getUser());
        values.put(UserEntry.COLUMN_PASS, user.getPassword());

        long inserted = db.insert(UserEntry.TABLE_NAME , null, values);
        return inserted;
    }

    public long delete(UserEntry user){
        String where =user.COLUMN_ID+ " = ?";
        String[] whereArgs = {
                ""+user.getId(),

        };
        long deleted = db.delete(UserEntry.TABLE_NAME, where, whereArgs);
        return deleted;
    }

    public Cursor selectUsers(String selection, String[] selectionArgs){
        String columns[] = {
                //UserEntry.COLUMN_ID,
                UserEntry.COLUMN_USER,
                UserEntry.COLUMN_PASS,
                UserEntry.COLUMN_PATTERN
        };
        Cursor cursor = db.query(UserEntry.TABLE_NAME, columns,selection, selectionArgs, null, null,null);

        return cursor;
    }

    public Cursor select(String selection , String selectionArgs[]){

        SQLiteDatabase database = helper.getReadableDatabase();

        String[] columns = {
                FishEntry.COLUMN_NAME,
                FishEntry.COLUMN_AMOUNT,
                FishEntry.COLUMN_SPECIES
        };

        String order = FishEntry.COLUMN_NAME +" DESC";

        Cursor cursor = database.query(
                FishEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                order
        );

        return cursor;
    }

    public Cursor selectFishes(String selection, String[] selectionArgs){
        String columns[] = {
                FishEntry.COLUMN_ID,
                FishEntry.COLUMN_NAME,
                FishEntry.COLUMN_SPECIES,
                FishEntry.COLUMN_AMOUNT
        };
        Cursor cursor = db.query(FishEntry.TABLE_NAME, columns,selection, selectionArgs, null, null,null);

        return cursor;
    }

    public long delete(FishEntry fish){
        String where =FishEntry.COLUMN_ID+ " = ?";
        String[] whereArgs = {
                ""+fish.getId(),

        };
        long deleted = db.delete(FishEntry.TABLE_NAME, where, whereArgs);
        return deleted;
    }

    public void close(){
        db.close();
    }

    public long update(FishEntry fish){
        ContentValues values = new ContentValues();
        values.put(FishEntry.COLUMN_NAME, fish.getName());
        values.put(FishEntry.COLUMN_AMOUNT, fish.getAmount());
        values.put(FishEntry.COLUMN_SPECIES, fish.getSpecies());

        String where =FishEntry.COLUMN_ID+ " = ?";
        String[] whereArgs = {
                ""+fish.getId(),

        };

        long updated = db.update(FishEntry.TABLE_NAME, values,where,whereArgs);
        return updated;
    }

    public void copyAppDbToDownloadFolder(Activity ctx) throws IOException {
        Log.d("","Started");
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                Log.d("","Middle");
                String currentDBPath = "/data/data/example.gabo.com.testapp/databases/movieDatabase";
                String backupDBPath = "backupname.db";
                File curreDB = new File(currentDBPath);
                File backuDB = new File(sd, backupDBPath);
                Log.d("","Finished middle");
                if (curreDB.exists()) {
                    FileChannel src = new FileInputStream(curreDB).getChannel();
                    FileChannel dst = new FileOutputStream(backuDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.d("","Finished");
                }
            }
    }
}
