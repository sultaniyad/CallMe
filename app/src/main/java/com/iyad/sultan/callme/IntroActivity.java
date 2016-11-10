package com.iyad.sultan.callme;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;


import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by salkhmis on 10/26/2016.
 */

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Hi slide
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.hi), getResources().getString(R.string.few_step), R.drawable.ic_sentiment_satisfied_white_48px, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
//ask Contacts Permission slide
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.contacts), getResources().getString(R.string.plz_allow_contacts), R.drawable.ic_contacts_white_48px, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.phone), getResources().getString(R.string.plz_allow_contacts), R.drawable.ic_phone_white_48px, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
        askForPermissions(new String[]{Manifest.permission.READ_CONTACTS},2);
        askForPermissions(new String[]{Manifest.permission.CALL_PHONE},3);


        addSlide(new LoadContactsData());
        addSlide(new Instruction_Fragment());

        showSkipButton(false);
      // setProgressButtonEnabled(false);
      //  setFadeAnimation();

        /*
Available animations:

setFadeAnimation()
setZoomAnimation()
setFlowAnimation()
setSlideOverAnimation()
setDepthAnimation()
*/
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }




}

