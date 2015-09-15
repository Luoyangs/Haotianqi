package com.haoxue.haotianqi.view;

import com.haoxue.haotianqi.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * 仿window的圆点加载进度
 */
public class SpotsDialog extends AlertDialog {

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    private int size;
    private AnimatedView[] spots;
    private AnimatorPlayer animator;

    /**创建一个弹出框*/
    public static SpotsDialog createDialog(Context context){
    	SpotsDialog dialog = new SpotsDialog(context);
    	return dialog;
    }
    
    private SpotsDialog(Context context) {
        this(context, R.style.SpotsDialogDefault);
    }

    private SpotsDialog(Context context, int theme) {
        super(context, theme);
    }

    private SpotsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cu_spotsdialog_layout);
        setCanceledOnTouchOutside(false);

        initProgress();
    }

    @Override
    protected void onStart() {
        super.onStart();

        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        animator.stop();
    }

    private void initProgress() {
        SpotsDialogLayout progress = (SpotsDialogLayout) findViewById(R.id.progress);
        size = progress.getSpotsCount();

        spots = new AnimatedView[size];
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.spot_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(getContext());
            v.setBackgroundResource(R.drawable.spot);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            progress.addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }
}

class AnimatorPlayer extends AnimatorListenerAdapter {

    private boolean interrupted = false;
    private Animator[] animators;

    public AnimatorPlayer(Animator[] animators) {
        this.animators = animators;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!interrupted) animate();
    }

    public void play() {
        animate();
    }

    public void stop() {
        interrupted = true;
    }

    private void animate() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.addListener(this);
        set.start();
    }
}

class AnimatedView extends View {

    private int target;

    public AnimatedView(Context context) {
        super(context);
    }

    public float getXFactor() {
        return getX() / target;
    }

    public void setXFactor(float xFactor) {
        setX(target * xFactor);
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }
}

class HesitateInterpolator implements Interpolator {

    private static double POW = 1.0/2.0;

    @Override
    public float getInterpolation(float input) {
        return input < 0.5
                ? (float) Math.pow(input * 2, POW) * 0.5f
                : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
    }
}
