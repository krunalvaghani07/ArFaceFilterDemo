package com.example.arfacedemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isadded=false;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomAR customAR = (CustomAR) getSupportFragmentManager().findFragmentById(R.id.arfragment);
        ModelRenderable.builder().
                build().thenAccept(renderable ->{
                    modelRenderable= renderable;
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(true);
        });
        Texture.builder().setSource(this,R.drawable.fox_face_mesh_texture)
                .build()
                .thenAccept(texture ->this.texture=texture );
        customAR.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        customAR.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            if(modelRenderable==null || texture==null){
                return;
            }
            Frame frame = customAR.getArSceneView().getArFrame();
            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);
            for(AugmentedFace augmentedFace : augmentedFaces){
                if(isadded){
                    return;
                }
                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customAR.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);
                isadded=true;
            }
        });
    }
}