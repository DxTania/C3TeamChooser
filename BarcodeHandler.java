import java.util.Integer;

public class BarcodeHandler
{
    private int m_barcode;
    private static final int BARCODE = 0;
    private int m_isBarcodeSet;

    public BarcodeHandler()
    {
        m_isBarcodeSet = 0;
    }

    protected void reqBarcode()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "ONE_D_MODE");
        startActivityForResult(intent, BARCODE);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE)
        {
            if (resultCode == RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                m_barcode = Integer.parseInt(contents);
                m_isBarcodeSet = 1;
            }
            else
            {
                // bad!
                m_isBarcodeSet = -1;
                m_barcode = -1;
            }
        }
    }

    public int isBarcodeSet()
    {
        return m_isBarcodeSet;
    }

    public int getBarcode()
    {
        return m_barcode;
    }
}

