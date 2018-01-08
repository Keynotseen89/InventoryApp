package com.example.quinatzin.inventoryapp;

/**
 * Created by Quinatzin Sintora on 1/4/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.quinatzin.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    /**
     * @param context
     * @param cursor
     */
    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
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
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.summary);

        //Find the columns of pets attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);

        //Read the inventory attribute from the cursor for the current inventory
        String itemName = cursor.getString(nameColumnIndex);
        Integer itemPrice = cursor.getInt(priceColumnIndex);
        String itemPriceValue = Integer.toString(itemPrice);
        nameTextView.setText(itemName);
        summaryTextView.setText(itemPriceValue);
    }
}
