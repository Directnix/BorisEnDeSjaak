package com.hemantithide.borisendesjaak;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.widget.Toast;

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
            //this is bad, the phone doesnt have nfc.
            Toast.makeText(main, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            main.finish();
            return;
        }

        if(!mainNfcAdapter.isEnabled())
        {
            try
            {
                throw new Exception("NFC is disabled");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            //this is good
        }
        handleIntent(main.getIntent());


    }

    private void handleIntent(Intent intent)
    {
        //TODO: handle intent.
    }

    public void NfcSetup()
    {

    }
}
