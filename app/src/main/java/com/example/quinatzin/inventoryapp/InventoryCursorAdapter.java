package com.example.quinatzin.inventoryapp;

/**
 * Created by Quinatzin Sintora on 1/4/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.quinatzin.inventoryapp.data.InventoryProvider;

public class InventoryCursorAdapter extends CursorAdapter {

    private MainActivity activity;
    /**
     * @param context
     * @param cursor
     */
    public InventoryCursorAdapter(MainActivity context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
        this.activity = context;

    }

    /**
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflate template
        TextView nameTextView, summaryTextView, quantityTextView;
        Button saleBtn = (Button) view.findViewById(R.id.saleButton);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_ID);

        nameTextView = view.findViewById(R.id.name);
        summaryTextView = view.findViewById(R.id.summary);
        quantityTextView = view.findViewById(R.id.quantity_summary);

        //Find the columns of pets attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
        final int itemQuantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY));

        // set the imageView to display current image in database
        imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_IMAGE))));
        //int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_IMAGE);

        //final int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);

        //Read the inventory attribute from the cursor for the current inventory
        String itemName = cursor.getString(nameColumnIndex);
        Integer itemPrice = cursor.getInt(priceColumnIndex);
        String itemPriceValue = Integer.toString(itemPrice);

        //String itemImage = cursor.getString(imageColumnIndex);

        //final Integer itemQuantity = cursor.getInt(quantityColumnIndex);

        //String itemQuantityValue = Integer.toString(itemQuantity);


        nameTextView.setText(itemName + " : ");
        summaryTextView.setText(" $" + itemPriceValue);
        quantityTextView.setText(" Quantity: " + String.valueOf(itemQuantity));

        //quantityTextView.setText(", Quantity:" + itemQuantityValue);

        final long id = cursor.getLong(cursor.getColumnIndex(InventoryEntry._ID));

        saleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                activity.onSaleBtn(id, itemQuantity);
            }
        });

    }


}
