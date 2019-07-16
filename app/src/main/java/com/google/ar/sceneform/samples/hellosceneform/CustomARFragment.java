package com.google.ar.sceneform.samples.hellosceneform;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.PlaneDiscoveryController;

import java.util.Collection;
import java.util.Objects;

// When you want to customize Config of default AR
// then you have to extend like this and set config file.
public class CustomARFragment extends ArFragment {
    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(Objects.requireNonNull(session));
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        //Here you can set which Plane detection you want
        config.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL);
        return config;
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        Collection<Plane> planeList = Objects.requireNonNull(getArSceneView().getSession()).getAllTrackables(Plane.class);
        for (Plane p : planeList){
            Log.d("Tested", "Got a plane -" + p.getTrackingState().toString());
        }
    }

}