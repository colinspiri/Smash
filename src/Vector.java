//made by Colin Spiridonov
//version 2.0

public class Vector implements Comparable<Object> {
	//fields
	protected double x;
	protected double y;
	protected double magnitude; //magnitude is always positive; it represents ONLY magnitude
	protected int xdir = 0;
	protected int ydir = 0;

	//constructors
	public Vector(double tempx, double tempy) {
		x=tempx;
		y=tempy;
		calculateMag();
	}
	public Vector(double tempmag, Vector tempdir){
		magnitude=tempmag;
		calculateComponents();
	}

	//only used to recalculate fields after performing operations; not used outside the class
	private void calculateMag() {
		magnitude = Math.sqrt((x*x)+(y*y));
		xdir=(int)(x/Math.abs(x));
		ydir=(int)(y/Math.abs(y));
	}
	private void calculateComponents() {
		x = Math.sqrt((magnitude*magnitude)-(y*y));
		y = Math.sqrt((magnitude*magnitude)-(x*x));
		x*=xdir;
		y*=ydir;
		xdir=(int)(x/Math.abs(x));
		ydir=(int)(y/Math.abs(y));
	}

	//accessor methods
	public double getX() { return x; }
	public double getY() { return y; }
	public double getMag() { return magnitude; }
	public Vector getDirection() { return new Vector(xdir,ydir); }

	//modifier methods
	public void set(Vector v) {
		setX(v.getX());
		setY(v.getY());
		calculateMag();
	}
	public void setX(double tempx) {
		x=tempx;
		calculateMag();
	}
	public void setY(double tempy) {
		y=tempy;
		calculateMag();
	}
	public void setComponents(double tempx, double tempy) {
		setX(tempx);
		setY(tempy);
	}
	public void setMag(double tempmag) {
		magnitude=tempmag;
		calculateComponents();
	}
	public void setDirection(double tempx, double tempy) {
		xdir=(int)(x/Math.abs(x));
		ydir=(int)(y/Math.abs(y));
		calculateComponents();
	}

	//operations
	//note: add and subtract operate on x and y; multiply and divide operate on magnitude
	public void add(Vector v) {
		this.x+=v.x;
		this.y+=v.y;
		calculateMag();
	}
	public void add(double tempx, double tempy) { add(new Vector(tempx,tempy)); }
	public void sub(Vector v) {
		this.x-=v.x;
		this.y-=v.y;
		calculateMag();
	}
	public void sub(double tempx, double tempy) { sub(new Vector(tempx,tempy)); }
	public void mult(double d) {
		if(d<-1) throw new IllegalArgumentException("Can't divide a Vector by a negative number excluding -1");
		//if multiplied by -1, reverse direction
		if(d==-1) {
			xdir*=-1;
			ydir*=-1;
			calculateComponents();
		}
		else {
			magnitude*=d;
			calculateComponents();
		}
	}
	public void div(double d) {
		if(d<0) throw new IllegalArgumentException("Can't divide a Vector by a negative number");
		if(d==0) throw new IllegalArgumentException("Can't divide a Vector by zero");
		magnitude*=d;
		calculateComponents();
	}
	public void normalize() {
		magnitude=1;
		calculateComponents();
	}

	//other methods
	public String toString() {
		return "x: " + x + ", y: " + y + ", magnitude: " + magnitude;
	}
	public int compareTo(Object obj) {
		if((obj instanceof Vector)==false) throw new IllegalArgumentException("Cannot compare a non-Vector object to a Vector");

		Vector other = (Vector) obj;

		if(this.getMag()>other.getMag()) return 1;
		else if(this.getMag()<other.getMag()) return -1;
		else return 0;
	}
	public boolean equals(Object obj) {
		if((obj instanceof Vector)==false) throw new IllegalArgumentException("Cannot compare a non-Vector object to a Vector");
		
		Vector other = (Vector) obj;
		
		if(this.getX()==other.getY() && this.getY()==other.getY()) return true;
		else return false;
	}


}
