package nathan.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Owner on 11/10/2016.
 */

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private AnimatorSet mSunsetAnimatorSet = new AnimatorSet();
    private AnimatorSet mSunriseAnimatorSet = new AnimatorSet();

    private boolean sunUp = true;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        ObjectAnimator sunAnimator = ObjectAnimator.ofFloat(mSunView, "rotation", 0, 90)
                .setDuration(4000);
        sunAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        sunAnimator.setInterpolator(new LinearInterpolator());
        sunAnimator.start();

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunUp) {
                    startSunsetAnimation();
                } else {
                    startSunriseAnimation();
                }
                sunUp = !sunUp;
            }
        });

        return view;
    }

    private void startSunsetAnimation() {
        float sunYStart = mSunView.getY();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,
                        "backgroundColor",
                        ((ColorDrawable)mSkyView.getBackground()).getColor(),
                        mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mSunsetAnimatorSet = new AnimatorSet();
        mSunsetAnimatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        mSunriseAnimatorSet.cancel();
        mSunsetAnimatorSet.start();
    }

    private void startSunriseAnimation() {
        float sunYEnd = mSunView.getTop();
        float sunYStart = mSunView.getY();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunriseSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,
                        "backgroundColor",
                        ((ColorDrawable)mSkyView.getBackground()).getColor(),
                        mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mSunriseAnimatorSet = new AnimatorSet();
        mSunriseAnimatorSet.play(heightAnimator)
                .with(sunriseSkyAnimator)
                .after(nightSkyAnimator);
        mSunsetAnimatorSet.cancel();
        mSunriseAnimatorSet.start();
    }
}
