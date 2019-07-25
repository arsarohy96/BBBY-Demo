package com.google.ar.sceneform.samples.hellosceneform;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.ArrayList;
import java.util.List;

public class AnimatorNode extends TransformableNode {

    private boolean isSelected = false;
    @Nullable
    private ObjectAnimator orbitAnimation = null;

    AnimatorNode(TransformationSystem transformationSystem) {
        super(transformationSystem);
        List<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator orbitAnimation = createAnimator();
        orbitAnimation.setTarget(this);
        orbitAnimation.setDuration(2000);
        animatorList.add(orbitAnimation);

        ObjectAnimator orbitAnimation1 = sclaeAnimation();
        orbitAnimation1.setTarget(this);
        //orbitAnimation1.setDuration(1000);
        animatorList.add(orbitAnimation1);

        AnimatorSet as = new AnimatorSet();
        as.playTogether(animatorList);
        //as.start();
        getScaleController().setEnabled(false);
        //getRotationController().setEnabled(false);
        //getTranslationController().setEnabled(false);
    }


    @Override
    public boolean select() {
//        isSelected = !isSelected;
//        if (isSelected) {
//            getTranslationController().setEnabled(true);
//            getRotationController().setEnabled(true);
//            startAnimation();
//        } else {
//            stopAnimation();
//            getRotationController().setEnabled(false);
//            getTranslationController().setEnabled(false);
//
//        }
        Log.d("Node", "select");
        return super.select();
    }

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {

        super.onTap(hitTestResult, motionEvent);
        Log.d("TouchListener", "onTap");

    }


    private void startAnimation() {
        if (orbitAnimation != null)
            orbitAnimation.cancel();
        orbitAnimation = upDownAnimation();
        orbitAnimation.setTarget(this);
        orbitAnimation.setDuration(2000);
        orbitAnimation.start();
    }

    private void stopAnimation() {
        if (orbitAnimation == null) {
            return;
        }
        orbitAnimation.cancel();
        setLocalPosition(new Vector3(0f, 0f, 0f));
        orbitAnimation = null;
    }


    private static ObjectAnimator createAnimator() {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.
        Quaternion orientation1 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0);
        Quaternion orientation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 120);
        Quaternion orientation3 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 240);
        Quaternion orientation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 360);

        ObjectAnimator orbitAnimation = new ObjectAnimator();
        orbitAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);

        // Next, give it the localRotation property.
        orbitAnimation.setPropertyName("localRotation");

        // Use Sceneform's QuaternionEvaluator.
        orbitAnimation.setEvaluator(new QuaternionEvaluator());

        //  Allow orbitAnimation to repeat forever
        orbitAnimation.setRepeatCount(0);
        orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
        orbitAnimation.setInterpolator(new LinearInterpolator());
        orbitAnimation.setAutoCancel(true);

        return orbitAnimation;
    }

    private ObjectAnimator upDownAnimation() {
        Vector3 vector1 = new Vector3(0f, .1f, 0f);
        ObjectAnimator dropAnimation = new ObjectAnimator();
        dropAnimation.setObjectValues(vector1);
        dropAnimation.setPropertyName("localPosition");
        dropAnimation.setEvaluator(new Vector3Evaluator());
        dropAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        dropAnimation.setRepeatMode(ObjectAnimator.REVERSE);
        dropAnimation.setInterpolator(new DecelerateInterpolator());
        dropAnimation.setAutoCancel(true);
        return dropAnimation;
    }

    private ObjectAnimator sclaeAnimation() {
        Vector3 vector1 = new Vector3(0f, 0f, 0f);
        vector1.scaled(0.01f);
        Vector3 vector2 = new Vector3(1f, 1f, 1f);
        vector2.scaled(1.0f);
        ObjectAnimator scaleAnimation = new ObjectAnimator();
        scaleAnimation.setObjectValues(vector1, vector2);
        scaleAnimation.setPropertyName("localScale");
        scaleAnimation.setEvaluator(new Vector3Evaluator());
//        scaleAnimation.setRepeatCount(ObjectAnimator.INFINITE);
//        scaleAnimation.setRepeatMode(ObjectAnimator.REVERSE);
//        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        //scaleAnimation.setAutoCancel(true);
        return scaleAnimation;
    }
}
