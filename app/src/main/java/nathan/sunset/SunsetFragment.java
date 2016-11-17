package nathan.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Owner on 11/10/2016.
 */

public class SunsetFragment extends Fragment {

    private ViewGroup mSceneRoot;
    private Scene mSkyScene;
    private Scene mSeaScene;
    private AutoTransition mSkyTransition;
    private AutoTransition mSeaTransition;

    private View mSunView;
    private View mFishView;
    private View mSkyView;
    private View mSeaView;

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
        mSceneRoot = (ViewGroup) view.findViewById(R.id.main);

        mSkyScene = Scene.getSceneForLayout(mSceneRoot, R.layout.sky_scene, getActivity());
        mSeaScene = Scene.getSceneForLayout(mSceneRoot, R.layout.sea_scene, getActivity());

        mSkyTransition = new AutoTransition();
        mSkyTransition.addListener(new SkyTransitionListener());

        mSeaTransition = new AutoTransition();
        mSeaTransition.addListener(new SeaTransitionListener());

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        TransitionManager.go(mSkyScene, mSkyTransition);

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
        heightAnimator.setInterpolator(new DecelerateInterpolator());

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

    private class SeaTransitionListener implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {}
        @Override
        public void onTransitionEnd(Transition transition) {
            View view = getView();
            mFishView = view.findViewById(R.id.fish);
            mSkyView = view.findViewById(R.id.sky);
            mSeaView = view.findViewById(R.id.sea);
            ObjectAnimator fishAnimator = ObjectAnimator.ofFloat(mFishView, "x",
                    mSeaView.getWidth(), 0-mFishView.getWidth())
                    .setDuration(4000);
            fishAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            fishAnimator.setInterpolator(new LinearInterpolator());
            fishAnimator.start();
            mFishView.setVisibility(View.VISIBLE);

            mSkyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransitionManager.go(mSkyScene, mSkyTransition);
                }
            });
        }
        @Override
        public void onTransitionCancel(Transition transition) {}
        @Override
        public void onTransitionPause(Transition transition) {}
        @Override
        public void onTransitionResume(Transition transition) {}

    }

    private class SkyTransitionListener implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {}
        @Override
        public void onTransitionEnd(Transition transition) {
            View view = getView();
            mSunView = view.findViewById(R.id.sun);
            mSkyView = view.findViewById(R.id.sky);
            mSeaView = view.findViewById(R.id.sea);

            ObjectAnimator sunAnimator = ObjectAnimator.ofFloat(mSunView, "rotation", 0, 90)
                    .setDuration(4000);
            sunAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            sunAnimator.setInterpolator(new LinearInterpolator());
            sunAnimator.start();

            float sunYEnd = mSunView.getY();
            float sunYStart = mSkyView.getHeight();

            ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                    .setDuration(3000);
            heightAnimator.setInterpolator(new DecelerateInterpolator());
            heightAnimator.start();

            mSunView.setVisibility(View.VISIBLE);

            mSkyView.setOnClickListener(new View.OnClickListener() {
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

            mSeaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransitionManager.go(mSeaScene, mSeaTransition);
                }
            });
        }
        @Override
        public void onTransitionCancel(Transition transition) {}
        @Override
        public void onTransitionPause(Transition transition) {}
        @Override
        public void onTransitionResume(Transition transition) {}

    }
}
