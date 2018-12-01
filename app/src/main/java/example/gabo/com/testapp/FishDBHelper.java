package example.gabo.com.testapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FishDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDatabase";


    private static final String DESTROY = "DROP TABLE IF EXISTS " + FishEntry.TABLE_NAME;
    private static final String DEST_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    private static final String SQL_CREATE = "CREATE TABLE " +FishEntry.TABLE_NAME + "("+
            FishEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            FishEntry.COLUMN_NAME + " TEXT, "+
            FishEntry.COLUMN_AMOUNT + " FLOAT, "+
            FishEntry.COLUMN_SPECIES + " TEXT)";

    private static final String SQL_CREATE_USERTABLE = "CREATE TABLE "+ UserEntry.TABLE_NAME + "("+
            UserEntry.COLUMN_USER + " TEXT PRIMARY KEY, " +
            UserEntry.COLUMN_PASS + " TEXT)";

    private static int DATABASE_VERSION = 6;

    public FishDBHelper(Context context){
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_USERTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DESTROY);
        db.execSQL(DEST_USER);
        onCreate(db);
    }
}