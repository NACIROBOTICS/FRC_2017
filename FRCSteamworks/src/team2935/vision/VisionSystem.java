package team2935.vision;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class VisionSystem {
	private final int IMG_WIDTH = 340;
	private final int IMG_HEIGHT = 240;
	private int centerX;
	private UsbCamera camera;
	private VisionPipeline pipeline = new GripPipeline();
	private VisionThread visionThread;
	private final Object imgLock = new Object();
	public void robotInit(){
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setExposureManual(0);
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
	        if (!pipeline.filterContoursOutput().isEmpty()) {
	            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	            synchronized (imgLock) {
	                centerX = r.x + (r.width / 2);
	            }
	        }
	    });
}
}
