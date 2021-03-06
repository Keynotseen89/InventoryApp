package com.example.quinatzin.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.content.CursorLoader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.quinatzin.inventoryapp.data.InventoryDbHelper;
import com.example.quinatzin.inventoryapp.data.InventoryProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;

    //Global CursorAdapter
    InventoryCursorAdapter mCursorAdapter;
    ListView inventoryListView;
    InventoryDbHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new InventoryDbHelper(this);

        // Setup FAB to open Detail Activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        //Find the ListView which will be populated with the instock data
        inventoryListView = findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);
        //Find and set empty view on the ListView, so it only shows when the list has 0 items


        //Setup an a Adapter to create a list item for each row of inventory stock
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        //Setup the item click listener
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(currentInventoryUri);
                startActivity(intent);
            }
        });

        //inventoryData();
        //kick off the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);

    }

    /**
     * reduce quantity by one
     */
    public void onSaleBtn(long id, int quantity) {
        mDatabase.onItemSell(id, quantity);
        mCursorAdapter.swapCursor(mDatabase.readItem());

    }


    /**
     * Helper method to insert hardcoded inventory item into database. For debuging
     */
    private void insertInventory() {
        //Create a ContentValue object where column names are keys
        // and "Galaxy" instock attributes are the values.
        ContentValues contentValues = new ContentValues();
        Uri newUri;

        String supplierName = "Johne Smite";
        String phoneNumber = "(909)222-1544";
        String supplierEmail = "examplesupplier@textemail.com";
        // contentValues.put(InventoryEntry._ID, 1);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "Iphone X");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 1000);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 100);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/iphone10");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

        //contentValues.put(InventoryEntry._ID, 2);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "RAZOR");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 600);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 50);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/razerphone");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

        //contentValues.put(InventoryEntry._ID, 3);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "Samsung Galaxy Note 3");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 300);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 20);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/note3");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

        //contentValues.put(InventoryEntry._ID, 4);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "Samsung Note 8");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 1000);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 40);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/note8");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

        //contentValues.put(InventoryEntry._ID, 5);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "Iphone 8");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 600);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 50);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/iphone8");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

        //contentValues.put(InventoryEntry._ID, 6);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_NAME, "Samsung s6");
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 700);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 45);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, supplierName);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneNumber);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, supplierEmail);
        contentValues.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, "android.resource://com.example.quinatzin.inventoryapp/drawable/samsung_s6");
        newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

    }

    /**
     * method used to delete all entry form "Instock" table from Inventory database
     */
    private void deleteAllInventory() {
        //used to keep track of amount of data that was deleted
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + "rows deleted from instock database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.insertID:
                insertInventory();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.deleteID:
                deleteAllInventory();
                return true;
            // default:
            //   return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a projection of Columns to grab and display in ListView
     *
     * @param id
     * @param args
     * @return CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_IMAGE
        };
        return new CursorLoader(this,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    /**
     * grabs the data from the Loader and swaps it into mCursorAdapter
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    /**
     * Clears the Loader
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }

}
