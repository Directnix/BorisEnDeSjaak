package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by thijs on 01-Jun-17.
 */

public class NFC
{
    private NfcAdapter mainNfcAdapter;
    private ProgressDialog dialog;
    private String TAG = "NFC class";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    public NFC(MainActivity main)
    {
        dialog = new ProgressDialog(main);
        dialog.setMessage("tag it you cunt");
        dialog.show();


        mainNfcAdapter = NfcAdapter.getDefaultAdapter(main);
        if (mainNfcAdapter == null)
        {
            //Toast.makeText(main, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            //main.finish();
            //return;
            System.out.println("NFC IS NOT SUPPORTED ON THIS DEVICE FAGGOT");
        }

        if (!mainNfcAdapter.isEnabled())
        {
            System.out.println("NFC is disabled");

        } else
        {
            System.out.println("NFC works!");
        }
        handleNFCIntent(main.getIntent());


    }

    public NfcAdapter getMainNfcAdapter()
    {
        return mainNfcAdapter;
    }

    private void handleNFCIntent(Intent intent)
    {
        String action = intent.getAction();
        System.out.println(action);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type))
            {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else
            {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList)
            {
                if (searchedTech.equals(tech))
                {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }


            //TODO: handle faggots
        }

    }
}
    class NdefReaderTask extends AsyncTask<Tag, Void, String>
{
    private final String TAG = "NdefReaderTask";

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException
    {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            JSONObject json = null;
            try{
                json = new JSONObject(result);
            } catch (Exception ex){
                System.err.println(ex);
            }

            String nameParent="";

            if(json!=null){
                try {
                    nameParent = json.getString("nameP");
                } catch (Exception e){
                    System.err.println(e);
                }
            }
        }
    }
}
