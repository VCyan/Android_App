package example.gabo.com.testapp;

import android.provider.BaseColumns;

public class FishEntry implements BaseColumns {

    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "title";
    public static final String COLUMN_SPECIES = "language";
    public static final String COLUMN_AMOUNT = "score";

    private String name;
    private String species;
    private float amount;
    private int id;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


}
