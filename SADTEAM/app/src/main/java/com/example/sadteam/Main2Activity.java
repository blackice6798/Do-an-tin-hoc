package com.example.sadteam;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;


import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Button btn3;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    //final static private int REQUEST_CAMERA = 1;
    //private String mCameraFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn = findViewById(R.id.btn);
        gvGallery = (GridView)findViewById(R.id.gv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /* Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("-mm-ss");
                String newPicFile = "Bild" + df.format(date)+ ".jpg";
                String outPath = "/sdcard" + newPicFile;
                File outFile = new File(outPath);
                mCameraFileName = outFile.toString();
                Uri outuri = Uri.fromFile(outFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
                startActivityForResult(intent, REQUEST_CAMERA);*/
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dunganh();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == REQUEST_CAMERA ){
            if (resultCode == Activity.RESULT_OK){
                Uri uri = null;
                if (data != null){
                    uri = data.getData();
                }
                if (uri == null && mCameraFileName != null){
                    uri = Uri.fromFile(new File(mCameraFileName));
                }
                File file = new File(mCameraFileName);
                if (!file.exists()){
                    file.mkdir();
                }
            }
        }*/
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //@Override
    protected void dunganh(){
        Mat img1 = Imgcodecs.imread("mnt/sdcard/IMG-20121228.jpg");
        Mat img2 = Imgcodecs.imread("mnt/sdcard/IMG-20121228-1.jpg");

        try{
            double l2_norm = Core.norm( img1, img2 );
            //tv.setText(l2_norm+"");
        } catch(Exception e) {
            //image is not a duplicate
        }
        try {
            Scalar blah = Core.sumElems(img2);
            Scalar blah1 = Core.sumElems(img1);
            String b = blah.toString();
            String b1 = blah1.toString();
            System.out.println(b + " " + b1);
            double comp = b.compareTo(b1);
            //tv.setText(""+comp);
        }
        SURF  detector = SURF.create(400,4,3,false,false); // = FeatureDetector.create(FeatureDetector.SURF);
        //FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
        //Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGBA2RGB);
        //Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGBA2RGB);

        DescriptorExtractor SurfExtractor = DescriptorExtractor
                .create(DescriptorExtractor.SURF);


        //extract keypoints
        MatOfKeyPoint keypoints, logoKeypoints;
        long time= System.currentTimeMillis();
        detector.detect(img1, keypoints);
        Log.d("LOG!", "number of query Keypoints= " + keypoints.size());
        detector.detect(img2, logoKeypoints);
        Log.d("LOG!", "number of logo Keypoints= " + logoKeypoints.size());
        Log.d("LOG!", "keypoint calculation time elapsed" + (System.currentTimeMillis() -time));

        //Descript keypoints
        long time2 = System.currentTimeMillis();
        Mat descriptors = new Mat();
        Mat logoDescriptors = new Mat();
        Log.d("LOG!", "logo type" + img2.type() + "  intype" + img1.type());
        SurfExtractor.compute(img1, keypoints, descriptors);
        SurfExtractor.compute(img2, logoKeypoints, logoDescriptors);
        Log.d("LOG!", "Description time elapsed" + (System.currentTimeMillis()- time2));
    }
    }
}
