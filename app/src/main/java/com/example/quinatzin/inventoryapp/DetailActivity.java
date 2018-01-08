package com.example.quinatzin.inventoryapp;

/**
 * Created by Quinatzin on 1/5/2018.
 */

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.quinatzin.inventoryapp.data.InventoryDbHelper;
import static com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry.TABLE_NAME;

/**
 * Allows user to view full information about current inventor instock
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the inventory data loader
     */
    private static final int EXISTING_INVENTORY_LOADER = 0;

    // EditText to read Name of Product
    private EditText mNameDetailText;

    // EditText to read Price of Item
    private EditText mPriceDetailText;

    // EditText to read Quantity of Item
    private EditText mQuntityDetailText;

    // Content URI for the existing inventory item
    private Uri mCurrentInventoryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Examine the intent that was used to launch this activity,
        //display more information about the product.
        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();


       /* //Find all relevant views that we will need to read
        mNameDetailText = (EditText) findViewById(R.id.inventory_name_id);
        mPriceDetailText = (EditText) findViewById(R.id.inventory_price_id);
        mQuntityDetailText = (EditText) findViewById(R.id.inventory_quantity_id);
    */
        if (mCurrentInventoryUri != null) {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
        mNameDetailText = (EditText) findViewById(R.id.inventory_name_id);
        mPriceDetailText = (EditText) findViewById(R.id.inventory_price_id);
        mQuntityDetailText = (EditText) findViewById(R.id.inventory_quantity_id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Array for data in the inventory database
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY};

        //return the projection in a CursorLoader
        return new CursorLoader(this,
                mCurrentInventoryUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            //Find the index of each item in the rows
            int nameColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
            int priceColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            int quantityColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);

            //Extract the data
            String name = data.getString(nameColumnIndex);

            int priceValue = data.getInt(priceColumnIndex);
            //String price = Integer.toString(priceValue);

            int quantityValue = data.getInt(quantityColumnIndex);
            //String quantity = Integer.toString(quantityValue);
            // String price = Integer.toString(data.getInt(priceColumnIndex));
           // String quantity = Integer.toString(data.getInt(quantityColumnIndex));

            //Set textField
            mNameDetailText.setText(name);
            mPriceDetailText.setText(Integer.toString(priceValue));
            mQuntityDetailText.setText(Integer.toString(quantityValue));


            //Set textField to none Editable
            mNameDetailText.setKeyListener(null);
            mPriceDetailText.setKeyListener(null);
            mQuntityDetailText.setKeyListener(null);
            //String price = Integer.toString(priceColumnIndex);
            //Integer price = getIntent()
            //Integer itemPrice = cursor.getInt(priceColumnIndex);
            //String itemPriceValue = Integer.toString(itemPrice);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // If the loader is invalidated, clear out
       /* mNameDetailText.setText("");
        mPriceDetailText.setText("");
        mQuntityDetailText.setText("");*/
    }
}
