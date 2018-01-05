package com.example.quinatzin.inventoryapp.data;

import android.content.ContentResolver;
import android.graphics.Path;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Quinatzin on 1/4/2018.
 */

public final class InventoryContract {

    // Empty constructor
    private InventoryContract() {
    }

    // content authority used for uri
    public static final String CONTENT_AUTHORITY = "com.example.quinatzin.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Name of the database table path
    public static final String PATH_STOCK = "instock";

    // Inner class called InventoryEntry
    public static final class InventoryEntry implements BaseColumns {
        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STOCK);

        /**
         *  Table name of database
         */
        public static final String TABLE_NAME = "instock";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of inventory item
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCK;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single inventory item
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCK;

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_INVENTORY_NAME = "name";
        public static final String COLUMN_INVENTORY_PRICE = "price";
        public static final String COLUMN_INVENTORY_QUANTITY = "quantity";

    }

}
