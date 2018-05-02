package hce.handpay.com.hce

import android.app.Activity
import android.os.Bundle
import android.nfc.NfcAdapter
import android.os.Build
import android.annotation.TargetApi
import android.util.Log
import android.widget.TextView



class MainActivity : Activity(),LoyaltyCardReader.AccountCallback {

    val TAG = "MyCardReader"
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    var READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    var mLoyaltyCardReader: LoyaltyCardReader?=null
    private var mAccountField: TextView? = null
    companion object {
        var sb = StringBuilder()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAccountField = findViewById<TextView>(R.id.data_tv)
        mLoyaltyCardReader = LoyaltyCardReader(this)
        enableReaderMode()
    }


    public override fun onPause() {
        super.onPause()
        disableReaderMode()
    }

    public override fun onResume() {
        super.onResume()
        enableReaderMode()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun enableReaderMode() {
        Log.i(TAG, "Enabling reader mode")
        val activity = this
        val nfc = NfcAdapter.getDefaultAdapter(activity)
        nfc?.enableReaderMode(activity, mLoyaltyCardReader, READER_FLAGS, null)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun disableReaderMode() {
        Log.i(TAG, "Disabling reader mode")
        val nfc = NfcAdapter.getDefaultAdapter(this)
        nfc?.disableReaderMode(this)
    }

    override fun onAccountReceived() {
        runOnUiThread { mAccountField!!.text = sb.toString() }
    }
}
