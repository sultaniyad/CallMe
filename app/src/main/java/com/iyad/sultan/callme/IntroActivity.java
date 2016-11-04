package com.iyad.sultan.callme;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by salkhmis on 10/26/2016.
 */

public class IntroActivity extends AppIntro {
    private static final int RESET_DATA = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //   setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2106F3"));

        //Hi slide
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.hi), getResources().getString(R.string.few_step), R.drawable.ic_arrow_back_white, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
//ask Contacts Permission slide
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.contacts), getResources().getString(R.string.plz_allow_contacts), R.drawable.ic_contacts_white_48dp, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.phone), getResources().getString(R.string.plz_allow_contacts), R.drawable.ic_phone_white_48dp, ActivityCompat.getColor(IntroActivity.this,R.color.colorPrimary)));
        askForPermissions(new String[]{Manifest.permission.READ_CONTACTS},2);
        askForPermissions(new String[]{Manifest.permission.CALL_PHONE},3);


        addSlide(new LoadContactsData());
        addSlide(new Instruction_Fragment());

        showSkipButton(false);
      //  setProgressButtonEnabled(false);
        setFadeAnimation();

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
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

    }



}

