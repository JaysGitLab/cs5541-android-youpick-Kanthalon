package nathan.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Owner on 11/10/2016.
 */

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private View mSeaView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private int mBlueSeaColor;
    private int mSunsetSeaColor;
    private int mNightSeaColor;

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
        mSeaView = view.findViewById(R.id.sea);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mBlueSeaColor = resources.getColor(R.color.blue_sea);
        mSunsetSeaColor = resources.getColor(R.color.sunset_sea);
        mNightSeaColor = resources.getColor(R.color.night_sea);

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
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunsetSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mBlueSeaColor, mSunsetSeaColor)
                .setDuration(3000);
        sunsetSeaAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mSunsetSeaColor, mNightSeaColor)
                .setDuration(1500);
        nightSeaAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.play(heightAnimator)
                .with(sunsetSeaAnimator)
                .before(nightSeaAnimator);
        animatorSet.start();
    }

    private void startSunriseAnimation() {
        float sunYEnd = mSunView.getTop();
        float sunYStart = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunriseSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunriseSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mSunsetSeaColor, mBlueSeaColor)
                .setDuration(3000);
        sunriseSeaAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mNightSeaColor, mSunsetSeaColor)
                .setDuration(1500);
        nightSeaAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator)
                .with(sunriseSkyAnimator)
                .after(nightSkyAnimator);
        animatorSet.play(heightAnimator)
                .with(sunriseSeaAnimator)
                .after(nightSeaAnimator);
        animatorSet.start();
    }
}
