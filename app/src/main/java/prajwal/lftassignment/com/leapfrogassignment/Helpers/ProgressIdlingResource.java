package prajwal.lftassignment.com.leapfrogassignment.Helpers;

import android.support.test.espresso.IdlingResource;
import prajwal.lftassignment.com.leapfrogassignment.LoginActivity;

/**
 * Created by prajwal on 7/18/2017.
 */

public class ProgressIdlingResource implements IdlingResource {   private ResourceCallback resourceCallback;
    private LoginActivity loginActivity;
    private LoginActivity.ProgressListener progressListener;

    public ProgressIdlingResource(LoginActivity activity){
        this.loginActivity = activity;

        progressListener = new LoginActivity.ProgressListener() {
            @Override
            public void onProgressShown() {
            }
            @Override
            public void onProgressDismissed() {
                if (resourceCallback == null){
                    return ;
                }
                //Called when the resource goes from busy to idle.
                resourceCallback.onTransitionToIdle();
            }
        };

        loginActivity.setProgressListener (progressListener);
    }
    @Override
    public String getName() {
        return "My idling resource";
    }

    @Override
    public boolean isIdleNow() {
        // the resource becomes idle when the progress has been dismissed
        return !loginActivity.isInProgress();
    }



    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}