package com.example.quinatzin.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
//import android.content.ReceiverCallNotAllowedException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;

//import static java.security.AccessController.getContext;

/**
 * Created by Quinatzin on 1/4/2018.
 */

public class InventoryProvider extends ContentProvider {

    /**
     * URI matcher code for the content URI for the instock table
     */
    private static final int INSTOCK = 100;

    /**
     * URI matcher code for the content URI for a single item in instock table
     */
    private static final int INSTOCK_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_STOCK, INSTOCK);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_STOCK + "/#", INSTOCK_ID);
    }

    /**
     * Database helper object
     */
    private InventoryDbHelper mDbHelper;
    /**
     * Tag for the log message
     */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI.  Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //Object of cursor to be used
        Cursor cursor;
        //uri assigned to matcher to use with switch statement.
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INSTOCK:
                cursor = database.query(InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INSTOCK_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //cursor used to display updated database
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * inset method used to call insertInventory
     * in order to do the database insertion into the table
     *
     * @param uri
     * @param contentValues
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INSTOCK:
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert Inventory item into the database with given content values
     */
    private Uri insertInventory(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(InventoryEntry.COLUMN_INVENTORY_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item name is required");
        }

        /**
         //Check that price is not null
         Integer price = values.getAsInteger(InventoryEntry.COLUMN_INVENTORY_PRICE);
         if(price == null){
         throw new IllegalArgumentException("Item price can not be empty");
         }

         //Checks that quantity is valid
         Integer quantity = values.getAsInteger(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
         if(quantity == null){
         throw new IllegalArgumentException("Quantity can not be less then 0");
         }
         */

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Insert the new item with given values
        long id = database.insert(InventoryEntry.TABLE_NAME, null, values);
        //If the ID is -1, then insertion failed. Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Update the data at the given selection and selection arguments, with the new ContentValues.
     */
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INSTOCK:
                return updateInventory(uri, values, selection, selectionArgs);
            case INSTOCK_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateInventory(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update inventory in the database with given content values.
     */
    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(InventoryEntry.COLUMN_INVENTORY_NAME)) {
            String name = values.getAsString(InventoryEntry.COLUMN_INVENTORY_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        // Otherwise, get writable database to update the data.
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        // if 1 or more rows were updated, then notify all listeners that the data given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            //getContext().getContentResolver.notifyChange(uri, null);
        }
        return rowsUpdated;
        //return rowsUpdated;
        // return database.update(InventoryEntry.TABLE_NAME, values, selection,selectionArgs);
    }


    /**
     * Delete the data at the given selection  and selection arguments
     */
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Get write able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Track the number of rows that were deleted
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INSTOCK:
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INSTOCK_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not support for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /**
     * Return the MIME type of data for the content URI.
     */
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INSTOCK:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INSTOCK_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
