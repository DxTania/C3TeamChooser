package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;

public class BarcodeHandler
{
    /**
     * m_barcode is the variable
     * which stores the value read
     * by the barcode scanner intent
     */
    private int m_barcode;
    /**
     * m_isBarcodeSet indicates whether
     * or not the activity has been called yet
     * to read a barcode
     */
    private int m_isBarcodeSet;

    // global static variables
    private static final int BARCODE = 0;

    /**
     * BarcodeHandler constructor
     * This doesn't do much, it just initializes m_barcode to
     * some default value and sets m_isBarcodeSet to 0
     */
    public BarcodeHandler()
    {
        m_barcode = 0;
        m_isBarcodeSet = 0;
    }

    /**
     * reqBarcode will initialize a barcode reader Intent
     * and then launch it. The results are handled at
     * onActivityResult
     *
     * @seez onActivityResult()
     */
    protected void reqBarcode()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "ONE_D_MODE");
        //startActivityForResult(intent, BARCODE);
    }

    /**
     * onActivityResult handles the intent which was launched by
     * reqBarcode
     *
     * @seez reqBarcode()
     */
    protected void onActivityResult (int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == BARCODE)
        {
            if (resultCode == 0)// RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                m_barcode = Integer.parseInt(contents);
                m_isBarcodeSet = 1;
            }
            else
            {
                // bad!
                m_isBarcodeSet = 1;
                m_barcode = -1;
            }
        }
    }

    /**
     * a simple getter for m_isBarcodeSet
     */
    public int isBarcodeSet()
    {
        return m_isBarcodeSet;
    }

    /**
     * a simple getter for m_barcode
     */
    public int getBarcode()
    {
        return m_barcode;
    }
}

