import java.util.ArrayList;

public class Logic {
	
	public static double average(ArrayList<Double> list) {
        int sum = 0;
        for(Double d : list) {
        	sum+=d;
        }
        return sum/list.size();
    }
	
	public static int random100() {
		return (int)(Math.random()*100);
	}
	
	public static double distanceFrom(double x1, double y1, double x2, double y2){
		double distance = 0;
		distance = Math.sqrt( Math.pow( y2-y1, 2)+ Math.pow( x2-x1, 2) );
		return distance;
	}
	
	public static double normalize(double num){
		return num/Math.abs(num);
	}
	
	public static double map(double value1, double start1, double end1, double start2, double end2){
		double value2 = 0;
		double newend1 = end1-start1;
		double newend2 = end2-start2;
		
		value2 = ((value1-start1)*newend2)/newend1;
		
		return start2+value2;
	}
	
	public static boolean isInteger(String s) {
		boolean isInteger = true;
		for(int i=0; i<s.length(); i++) {
			String temp = s.substring(i,i+1);
			if((temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5") || temp.equals("6") || temp.equals("7") || temp.equals("8") || temp.equals("9"))==false) {
				isInteger=false;
			}
		}
		
		return isInteger;
	}
	
	public static void removeElements(ArrayList<String> list) {
		while(list.size()>0) list.remove(0);
	}
	

}
