package com.hemantithide.borisendesjaak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class cheatMenu extends AppCompatActivity
{
    private Button add100;
    private Button addAmount;
    private Button enterPassword;

    private EditText amount;
    private EditText password;

    private FrameLayout loginFrame;
    private FrameLayout cheatFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat_menu);
        loginFrame = (FrameLayout)findViewById(R.id.cheat_login_frame);
        cheatFrame = (FrameLayout)findViewById(R.id.cheat_cheats_frame);

        password = (EditText)findViewById(R.id.cheat_password_psswrd);

        cheatFrame.setVisibility(View.INVISIBLE);

        password = (EditText)findViewById(R.id.cheat_password_psswrd);

        enterPassword = (Button)findViewById(R.id.cheat_enterpass_btn);
        enterPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (String.valueOf(password.getText()).equals("blauwewafel"))
                {
                    loginFrame.setVisibility(View.INVISIBLE);
                    cheatFrame.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(cheatMenu.this, "Wrong password fag!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        add100 = (Button)findViewById(R.id.cheats_add_btn);
        add100.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.user.addToDucats(100);
                MainActivity.user.save(getApplicationContext());
                Toast.makeText(cheatMenu.this, "Added 100 coins fag",
                        Toast.LENGTH_SHORT).show();

            }
        });

        amount = (EditText)findViewById(R.id.cheat_amount_text);

        addAmount = (Button)findViewById(R.id.cheat_amountAdd_btn);
        addAmount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.user.addToDucats(Integer.valueOf(String.valueOf(amount.getText())));
                MainActivity.user.save(getApplicationContext());
                String coins = "Added "+String.valueOf(amount.getText())+" coins";
                Toast.makeText(cheatMenu.this, coins ,
                        Toast.LENGTH_SHORT).show();
            }
        });

        MainActivity.setCheatClickerZero();



    }
}
