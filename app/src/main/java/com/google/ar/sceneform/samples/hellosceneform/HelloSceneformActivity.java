/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.SelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private CustomARFragment arFragment;
    private ModelRenderable table1Renderable;
    private ModelRenderable table2Renderable;
    private ModelRenderable chairRenderable;
    private ModelRenderable selectedRenderable;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (CustomARFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        findViewById(R.id.btn_chair_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chairRenderable!=null){
                    selectedRenderable = chairRenderable;
                    ((Button) v).setTextColor(android.graphics.Color.BLUE);
                    ((Button) findViewById(R.id.btn_table_1)).setTextColor(android.graphics.Color.WHITE);
                    ((Button) findViewById(R.id.btn_table_2)).setTextColor(android.graphics.Color.WHITE);
                }
            }
        });
        findViewById(R.id.btn_table_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table1Renderable!=null){
                    selectedRenderable = table1Renderable;
                    ((Button) v).setTextColor(android.graphics.Color.BLUE);
                    ((Button) findViewById(R.id.btn_chair_1)).setTextColor(android.graphics.Color.WHITE);
                    ((Button) findViewById(R.id.btn_table_2)).setTextColor(android.graphics.Color.WHITE);
                }
            }
        });
        findViewById(R.id.btn_table_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table2Renderable!=null){
                    selectedRenderable = table2Renderable;
                    ((Button) v).setTextColor(android.graphics.Color.BLUE);
                    ((Button) findViewById(R.id.btn_chair_1)).setTextColor(android.graphics.Color.WHITE);
                    ((Button) findViewById(R.id.btn_table_1)).setTextColor(android.graphics.Color.WHITE);
                }
            }
        });

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.m_3324972_61494152)
                .build()
                .thenAccept(renderable -> chairRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.m_3243705_40985558)
                .build()
                .thenAccept(renderable -> table1Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.m_1061046047_61046047)
                .build()
                .thenAccept(renderable -> table2Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });



        arFragment.getArSceneView().getPlaneRenderer().getMaterial().thenAccept(
                material -> material.setFloat3(PlaneRenderer.MATERIAL_COLOR,
                        new Color(0.0f, 0.0f, 1.0f, 1.0f)));


        // Remove Visualizer of selection of object
        arFragment.getTransformationSystem().setSelectionVisualizer(new Visualizer());


        //arFragment.getArSceneView().getSession().getAllTrackables(Plane.class);


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (selectedRenderable==null) {
                        Toast.makeText(getApplicationContext(),"Select any model", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());



                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new AnimatorNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(selectedRenderable);
                    // disable live shadow casting
                    selectedRenderable.setShadowCaster(false);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(andy, "scaleX", 0.25f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(andy, "scaleY", 0.25f);
                    ObjectAnimator scaleDownZ = ObjectAnimator.ofFloat(andy, "scaleZ", 0.25f);

                    scaleDownX.setDuration(7000);   // Duration 7 seconds
                    scaleDownY.setDuration(7000);
                    scaleDownZ.setDuration(7000);

                    AnimatorSet scaleDown = new AnimatorSet();
                    scaleDown.play(scaleDownX).with(scaleDownY);
                    scaleDown.play(scaleDownX).with(scaleDownZ);  // 3 animations play simultaneously

                    scaleDown.start();



                });
    }


    class Visualizer implements SelectionVisualizer {

        @Override
        public void applySelectionVisual(BaseTransformableNode node) {

        }

        @Override
        public void removeSelectionVisual(BaseTransformableNode node) {

        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
