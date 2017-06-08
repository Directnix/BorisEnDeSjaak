package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by thijs on 01-Jun-17.
 */

public class NFC
{
    private NfcAdapter mainNfcAdapter;

    public NFC(MainActivity main)
    {
        mainNfcAdapter = NfcAdapter.getDefaultAdapter(main);
        if(mainNfcAdapter == null)
        {
            //Toast.makeText(main, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            //main.finish();
            //return;
            System.out.println("NFC IS NOT SUPPORTED ON THIS DEVICE FAGGOT");
        }

        if(!mainNfcAdapter.isEnabled())
        {
            System.out.println("NFC is disabled");

        }else{
            System.out.println("NFC works!");
        }
        handleNFCIntent(main.getIntent());



    }

    private void handleNFCIntent(Intent intent)
    {
        //TODO: handle faggots
    }

    public void NfcSetup()
    {

    }

    public boolean writeTag(Tag tag, MainActivity main)
    {
        //NdefRecord appRecord = NdefRecord.createApplicationRecord("com.lgandroid.ddcnfc");
        NdefRecord appRecord = NdefRecord.createMimeRecord("application/com.hemantithide.borisendesjaak", new byte[0]);
        NdefMessage message = new NdefMessage(new NdefRecord[] { appRecord });
        TextView nfcTextView = (TextView)main.findViewById(R.id.nfc_nfcText_txtvw);

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    nfcTextView.setText("Read-only tag.");
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    nfcTextView.setText("Tag doesn't have enough free space.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                nfcTextView.setText("Tag written successfully.");
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        nfcTextView.setText("Tag written successfully!\nClose this app and scan tag.");
                        return true;
                    } catch (IOException e) {
                        nfcTextView.setText("Unable to format tag to NDEF.");
                        return false;
                    }
                } else {
                    nfcTextView.setText("Tag doesn't appear to support NDEF format.");
                    return false;
                }
            }
        } catch (Exception e) {
            nfcTextView.setText("Failed to write tag");
        }

        return false;
    }
}
