package fit.nsu.santaev.diplom.utils;

import org.opencv.core.Mat;

public class Hystogram {

	public Hystogram() {
	}

	public static double[] getHystogram(Mat frame){
		double[] hyst = new double[128 * 3];
		int count = 0;
		byte[] pixel = new byte[4];
		for (int i = 0; i < frame.cols(); i = i + 15){
			for (int j = 0; j < frame.rows(); j = j + 15){
				count++;
				frame.get(j, i, pixel);
				if ((int)pixel[0] + 128 >= 256 || 0 > (int)pixel[0] + 128){
					//count = 0;
				}
				hyst[((int)pixel[0] + 128) / 2]++;
				hyst[((int)pixel[1] + 128 + 256 * 1) / 2]++;
				hyst[((int)pixel[2] + 128 + 256 * 2) / 2]++;
			}
		}
		for (int i = 0; i < hyst.length; i++){
			hyst[i] = hyst[i] / count;
		}
		return hyst;
	}
	
	public static double getDifference(double[] hyst, double[] hyst2){
		double sum = 0;
		for (int i = 0; i < hyst.length; i++){
			sum += Math.abs(hyst[i] - hyst2[i]);
		}
		return sum;
	}
	
	public static void addToMath(double[] hyst, Mat frame, int r, int g, int b){
		int height = frame.height();
		for (int i = 0; i < hyst.length; i++){
			int h = (int) (height * 20 * hyst[i]);
			//frame.put(i, h, new byte[]{(byte) 255});
			for (int j = 0; j < h; j++){
				frame.put(j, i, new byte[]{(byte) r, (byte) g, (byte) b, 0});
				/*if (i < hyst.length / 3){
					frame.put(j, delta + i, new byte[]{(byte) 255, 0, 0, 0});
				} else 
				if (i >= hyst.length / 3 && i < hyst.length / 3 * 2){
					frame.put(j, delta + i, new byte[]{0, (byte) 255, 0, 0});
				} else {
					frame.put(j, delta + i, new byte[]{0, 0, (byte) 255, 0});
				}*/
			}
			//hyst[pixel[0]]++;
		}
		return;
	}
}