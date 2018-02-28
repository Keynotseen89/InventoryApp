package com.example.quinatzin.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Created by Quinatzin on 1/4/2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAB = InventoryDbHelper.class.getSimpleName();
    // Name of the database
    private static final String DATABASE_NAME = "inventory.db";
    // Version of the database
    private static final int DATABASE_VERSION = 2;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE  " + InventoryEntry.TABLE_NAME + " ("
            + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InventoryEntry.COLUMN_INVENTORY_NAME + " TEXT NOT NULL, "
            + InventoryEntry.COLUMN_INVENTORY_PRICE + " INTEGER NOT NULL DEFAULT 0, "
            + InventoryEntry.COLUMN_INVENTORY_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + InventoryEntry.COLUMN_INVENTORY_SUPPLIER + " TEXT NOT NULL, "
            + InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE + " TEXT NOT NULL, "
            + InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL + " TEXT NOT NULL, "
            + InventoryEntry.COLUMN_INVENTORY_IMAGE + " TEXT ); ";

    /**
     * onCreate method used to created data table with given values
     * id, item_name, item_price, and item_quantity
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       /* String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE  " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_INVENTORY_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_INVENTORY_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_INVENTORY_QUANTITY + " INTEGER NOT NULL DEFAULT 0); ";
    */
        //Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    /**
     * used to keep track from old to new version of this database
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //The database is still at version 1, so there's nothing to be done here yet.
        Log.d(LOG_TAB, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        if (oldVersion != newVersion) {
            //Drop table if existed, all data will be gone!!
            //   database.execSQL("DROP TABLE IF EXISTS " + VaccineEntry.TABLE_NAME);
            //  database.execSQL("DROP TABLE IF EXISTS " + VaccineEntry.MED_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Used to update item product when sale button is clicked
     *
     * @param productId
     * @param quantity
     */
    public void onItemSell(long productId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newAmount = 0;

        if (quantity > 0) {
            newAmount = (quantity - 1);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, newAmount);
        String selection = InventoryEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(productId)};
        db.update(InventoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    /**
     * Used to display current price after update
     *
     * @return
     */
    public Cursor readItem() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_IMAGE
        };

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}
