package com.mgg.demo.mggwidgets;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.mgg.demo.mggwidgets.widgets.AutoFitSurfaceView;
import com.mgg.demo.mggwidgets.widgets.RecordButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.hardware.camera2.CameraAccessException.CAMERA_DISABLED;
import static android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;

/**
 * created by mgg
 * 2020/6/28
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class RecordVideoActivity extends BaseActivity {
    private static final int RECORDER_VIDEO_BITRATE = 10_000_000;
    private static final int RECORDER_VIDEO_FPS = 30;
    //系统相册路径
    public static final String RECORD_CACHE_FOLDER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/";
    private static final Size videoSize = new Size(1920, 1080);
    CameraManager cameraManager;
    CameraCharacteristics characteristics;
    CameraCaptureSession session;
    CameraDevice cameraDevice;
    CaptureRequest previewRequest;
    CaptureRequest recordRequest;
    Surface recorderSurface;
    MediaRecorder recorder;
    HandlerThread cameraThread;
    Handler cameraHandler;
    File outputFile;
    String frontCameraId;
    String backCameraId;

    AutoFitSurfaceView surfaceView;
    RecordButton btnRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        initView();
        initHandler();
        initSurfaceHolder();
    }

    void initView() {
        surfaceView = findViewById(R.id.surface_view);
        btnRecord = findViewById(R.id.btn_record);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w = surfaceView.getWidth();
                int h = surfaceView.getHeight();
                Log.d("mgg", "onClick: " + w + "  " + h);
                if (btnRecord.getIsRecording()) {
                    btnRecord.setRecording(false);
                    stopRecord();
                } else {
                    btnRecord.setRecording(true);
                    startRecord();
                }
            }
        });
    }

    void initHandler() {
        cameraThread = new HandlerThread("CameraThread");
        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());
    }

    void initSurfaceHolder() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceView.setAspectRatio(videoSize.getWidth(), videoSize.getHeight());
                surfaceView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initCamera();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("mgg", "surfaceChanged: " + width + " " + height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    void initCamera() throws CameraAccessException {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        outputFile = new File(getFilesDir(), new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA).toString() + ".mp4");
        initCameraIds();
        if (!TextUtils.isEmpty(backCameraId)) {
            characteristics = cameraManager.getCameraCharacteristics(backCameraId);
        } else {
            throw new CameraAccessException(CAMERA_DISABLED);
        }
        openCamera();
    }

    void initCameraIds() throws CameraAccessException {
        String[] cameraIds = cameraManager.getCameraIdList();
        int length = cameraIds.length;
        for (int i = 0; i < length; i++) {
            characteristics = cameraManager.getCameraCharacteristics(cameraIds[i]);
            int supportedLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (supportedLevel == INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                continue;
//                    throw new CameraAccessException(CAMERA_DISABLED);//不支持Camera2
            }
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                if (TextUtils.isEmpty(frontCameraId))
                    frontCameraId = cameraIds[i];
            } else if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                if (TextUtils.isEmpty(backCameraId)) {
                    backCameraId = cameraIds[i];
                    StreamConfigurationMap configurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    Size[] sizes = configurationMap.getOutputSizes(MediaRecorder.class);
                    int length1 = sizes.length;
                    for (int j = 0; j < length1; j++) {
                        Log.d("mgg", "back camera supported size: " + sizes[j].getWidth() + " x " + sizes[j].getHeight());
                    }
                }
            }
        }
    }

    void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraManager.openCamera(backCameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    try {
                        initSession();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClosed(@NonNull CameraDevice camera) {
                    cameraDevice = null;
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    camera.close();
                    cameraDevice = null;
                }
            }, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    void initSession() throws CameraAccessException {
        recorderSurface = MediaCodec.createPersistentInputSurface();
        initMediaRecorder(recorderSurface);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.release();

        List<Surface> surfaceList = new ArrayList<>();
        surfaceList.add(surfaceView.getHolder().getSurface());
        surfaceList.add(recorderSurface);
        cameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                RecordVideoActivity.this.session = session;
                try {
                    CaptureRequest.Builder builder = session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    builder.addTarget(surfaceView.getHolder().getSurface());
                    previewRequest = builder.build();
                    session.setRepeatingRequest(previewRequest, null, cameraHandler);

                    builder = session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                    builder.addTarget(surfaceView.getHolder().getSurface());
                    builder.addTarget(recorderSurface);
                    builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range(RECORDER_VIDEO_FPS, RECORDER_VIDEO_FPS));
                    recordRequest = builder.build();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                finish();
            }
        }, cameraHandler);
    }

    void initMediaRecorder(Surface surface) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(outputFile.getAbsolutePath());
        recorder.setVideoEncodingBitRate(RECORDER_VIDEO_BITRATE);
        recorder.setVideoFrameRate(RECORDER_VIDEO_FPS);
        recorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setInputSurface(surface);
    }

    void startRecord() {
        try {
            session.setRepeatingRequest(recordRequest, null, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        initMediaRecorder(recorderSurface);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    void stopRecord() {
        recorder.stop();
        MediaScannerConnection.scanFile(
                this, new String[]{outputFile.getAbsolutePath()}, null, null);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4"));
        intent.setData(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", outputFile));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraThread.quitSafely();
        if (recorder != null)
            recorder.release();
        if (recorderSurface != null)
            recorderSurface.release();
    }
}
