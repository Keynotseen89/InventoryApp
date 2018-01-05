package com.example.quinatzin.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Created by Quinatzin on 1/4/2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAB = InventoryDbHelper.class.getSimpleName();
    // Name of the database
    private static final String DATABASE_NAME = "inventory.db";
    // Version of the database
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate method used to created data table with given values
     * id, item_name, item_price, and item_quantity
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE  " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_INVENTORY_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_INVENTORY_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_INVENTORY_QUANTITY + " INTEGER NOT NULL DEFAULT 0); ";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    /**
     * used to keep track from old to new version of this database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //The database is still at version 1, so there's nothing to be done here yet.
    }
}
