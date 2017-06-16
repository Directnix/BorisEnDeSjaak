package com.hemantithide.borisendesjaak;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Daniel on 04/06/2017.
 */

public class ShopInfoLayout extends FrameLayout {

    private MainActivity main;

    enum Item { RANDOM_NAME, FREE_PICTURE, CUSTOM_NAME }
    private Item item;

    private TextView tv_title;
    private TextView tv_description;
    private TextView tv_price;
    private TextView tv_ducats;

    private int itemPrice;
    private Button purchaseButton;

    public ShopInfoLayout(@NonNull Context context) {
        super(context);
    }

    public ShopInfoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopInfoLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setMain(MainActivity main) {
        this.main = main;
    }

    void init(final Item item) {

        tv_title = (TextView) findViewById(R.id.shopinfo_item_title);
        tv_description = (TextView) findViewById(R.id.shopinfo_item_description);
        tv_price = (TextView) findViewById(R.id.shopinfo_txtvw_price);
        tv_ducats = (TextView) findViewById(R.id.shopinfo_txtvw_ducats);

        tv_title.setTypeface(MainActivity.tf);
        tv_description.setTypeface(MainActivity.tf);
        tv_price.setTypeface(MainActivity.tf);
        tv_ducats.setTypeface(MainActivity.tf);

        purchaseButton = (Button) findViewById(R.id.shopinfo_btn_purchase);
        purchaseButton.setTypeface(MainActivity.tf);
        purchaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                main.purchase(item);
            }
        });

        switch(item) {

            case RANDOM_NAME:
                tv_title.setText(getResources().getString(R.string.shop_btn_random_name));
                tv_description.setText(getResources().getString(R.string.item_info_random_name));
                itemPrice = 100;
                break;
            case CUSTOM_NAME:
                tv_title.setText(getResources().getString(R.string.shop_btn_custom_name));
                tv_description.setText(getResources().getString(R.string.item_info_custom_name));
                itemPrice = 1001;
                break;
            case FREE_PICTURE:
                tv_title.setText(getResources().getString(R.string.shop_btn_free_picture));
                tv_description.setText(getResources().getString(R.string.item_info_free_picture));
                itemPrice = 500;
        }

        tv_price.setText(getResources().getString(R.string.item_price) + ":\t" + itemPrice);
        tv_ducats.setText(MainActivity.user.ducats + "");

        if(MainActivity.user.ducats > itemPrice) {
            purchaseButton.setClickable(true);
            purchaseButton.setAlpha(1);
        } else {
            purchaseButton.setClickable(false);
            purchaseButton.setAlpha(0.25f);
        }
    }
}
