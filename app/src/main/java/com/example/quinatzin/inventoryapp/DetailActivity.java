package com.example.quinatzin.inventoryapp;

/**
 * Created by Quinatzin on 1/5/2018.
 */

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.quinatzin.inventoryapp.data.InventoryDbHelper;

import org.w3c.dom.Text;

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

    // EditText to read supplier of information
    private EditText mSupplierNameText, mSupplierPhoneNumberText, mSupplierEmailText;

    private boolean mItemHasChanged = false;
    // Used to change background statues
    private boolean editButtonclick = false;

    // keylistener for each values
    private KeyListener nameListener, priceListener, quantityListener,
            supplierListener, supplierPhoneListener, supplierEmailListener;

    // Content URI for the existing inventory item
    private Uri mCurrentInventoryUri;

    /**
     * OnTouchListener is used to check if an
     * item has been changed in order to save or update
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

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
        mSupplierNameText = (EditText) findViewById(R.id.inventory_supplier_id);
        mSupplierPhoneNumberText = (EditText) findViewById(R.id.inventory_supplierPhone_id);
        mSupplierEmailText = (EditText) findViewById(R.id.inventory_supplierEmail_id);
        //editBackground(false);

        // set listener to text
        mNameDetailText.setOnTouchListener(mTouchListener);
        mPriceDetailText.setOnTouchListener(mTouchListener);
        mQuntityDetailText.setOnTouchListener(mTouchListener);
        mSupplierNameText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberText.setOnTouchListener(mTouchListener);
        mSupplierEmailText.setOnTouchListener(mTouchListener);

        // listener for input when editor is clicked
        nameListener = mNameDetailText.getKeyListener();
        priceListener = mPriceDetailText.getKeyListener();
        quantityListener = mQuntityDetailText.getKeyListener();
        supplierListener = mSupplierNameText.getKeyListener();
        supplierPhoneListener = mSupplierPhoneNumberText.getKeyListener();
        supplierEmailListener = mSupplierEmailText.getKeyListener();
    }


    /**
     * Inflate Menu item with new fields
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.edit_main, menu);
        return true;
    }

    /**
     * action for menu selection
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.editID:
                editItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.saveID:
                saveItem();
                finish();
                return true;
            case R.id.deleteID:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            // default:
            //   return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Display menu certain manu items when in new or
     * current detail items
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.deleteID);
            menuItem.setVisible(false);
            MenuItem menuEdit = menu.findItem(R.id.editID);
            menuEdit.setVisible(false);
        } else if (editButtonclick == false) {
            MenuItem menuItem = menu.findItem(R.id.saveID);
            menuItem.setVisible(false);
            //MenuItem menuDelete = menu.findItem(R.id.deleteID);
            //menuDelete.setVisible(false);
        } else {
            MenuItem menuSave = menu.findItem(R.id.saveID);
            menuSave.setVisible(true);
            MenuItem menuDelete = menu.findItem(R.id.deleteID);
            setVisible(true);
            MenuItem menuEdit = menu.findItem(R.id.editID);
            menuEdit.setVisible(false);
        }
        return true;
    }

    /**
     * changed text editable to true for updating
     */
    private void editItem() {
        setTitle("Editing");
        editButtonclick = true;
        //editBackground(false);

        mNameDetailText.setKeyListener(nameListener);
        mPriceDetailText.setKeyListener(priceListener);
        mQuntityDetailText.setKeyListener(quantityListener);
    }

    /**
     * update existing data by saving changes
     * save new data entered
     */
    private void saveItem() {
        String nameString = mNameDetailText.getText().toString().trim();
        String priceString = mPriceDetailText.getText().toString().trim();
        String quantityString = mQuntityDetailText.getText().toString().trim();

        if (mCurrentInventoryUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString)
                && TextUtils.isEmpty(quantityString)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENTORY_NAME, nameString);
        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantityString);

        //Check if this is a new item
        if (mCurrentInventoryUri == null) {
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            //Show a toast message to show if it failed to save or didn't
            if (newUri == null) {
                Toast.makeText(this, "Failed to save", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, " Saved ", Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, " Successful", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * delete selected data from table
     */
    private void deleteItem() {
        if (mCurrentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Delete Failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show();
            }
            // close activity
            finish();
        }
    }

    /**
     * Prompt the user to confirm deletion
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        // create and show the alertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean editBackground(boolean hasChanged) {
        if (hasChanged == true) {
            mNameDetailText.getBackground().setAlpha(0);
            mPriceDetailText.getBackground().setAlpha(0);
            mQuntityDetailText.getBackground().setAlpha(0);
        } else {
            mNameDetailText.getBackground().setAlpha(255);
            mPriceDetailText.getBackground().setAlpha(255);
            mQuntityDetailText.getBackground().setAlpha(255);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    //Discard changes dialog method
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        //Create an AlertDialog.Builder and set the message, and click listeners
        // for the position and nagative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unsaved Changes");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //Create and show AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Array for data in the inventory database
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL
        };

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
            int supplierColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
            int supplierPhoneColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE);
            int supplierEmailColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL);

            //Extract the data
            String name = data.getString(nameColumnIndex);
            int priceValue = data.getInt(priceColumnIndex);
            int quantityValue = data.getInt(quantityColumnIndex);
            String supplierName = data.getString(supplierColumnIndex);
            String supplierPhone = data.getString(supplierPhoneColumnIndex);
            String supplierEmail = data.getString(supplierEmailColumnIndex);


            //Set textField
            mNameDetailText.setText(name);
            mPriceDetailText.setText(Integer.toString(priceValue));
            mQuntityDetailText.setText(Integer.toString(quantityValue));
            mSupplierNameText.setText(supplierName);
            mSupplierPhoneNumberText.setText(supplierPhone);
            mSupplierEmailText.setText(supplierEmail);


            //Set textField to none Editable
            mNameDetailText.setKeyListener(null);
            mPriceDetailText.setKeyListener(null);
            mQuntityDetailText.setKeyListener(null);
            mSupplierNameText.setKeyListener(null);
            mSupplierPhoneNumberText.setKeyListener(null);
            mSupplierEmailText.setKeyListener(null);
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
