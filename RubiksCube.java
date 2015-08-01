import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;

public class RubiksCube {
 
    private int homeCount;
    private int maxHomeCubeCount;
    private Vector cubeVector = new Vector();
    private Random rand = new Random();
    int i;
    int count;
    int mynum;
    int axis;
    boolean polarity, clockwise;
    int bestAxis;
    boolean bestPolarity, bestClockwise;
	
    public RubiksCube() throws Exception {

	// Initialize the center cubes
	cubeVector.add(new Cube(Cube.X_AXIS, -1, -1, true, false, false));
	cubeVector.add(new Cube(Cube.X_AXIS, -1, -1, false, false, false));
	cubeVector.add(new Cube(Cube.Y_AXIS, -1, -1, true, false, false));
	cubeVector.add(new Cube(Cube.Y_AXIS, -1, -1, false, false, false));
	cubeVector.add(new Cube(Cube.Z_AXIS, -1, -1, true, false, false));
	cubeVector.add(new Cube(Cube.Z_AXIS, -1, -1, false, false, false));

	// Edges - top tier
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, -1, true, true, false));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, -1, false, true, false));
	cubeVector.add(new Cube(-1, Cube.Y_AXIS, Cube.Z_AXIS, false, true, true));
	cubeVector.add(new Cube(-1, Cube.Y_AXIS, Cube.Z_AXIS, false, true, false));

	// Edges - middle tier
	cubeVector.add(new Cube(Cube.X_AXIS, -1, Cube.Z_AXIS, true, false, true));
	cubeVector.add(new Cube(Cube.X_AXIS, -1, Cube.Z_AXIS, true, false, false));
	cubeVector.add(new Cube(Cube.X_AXIS, -1, Cube.Z_AXIS, false, false, true));
	cubeVector.add(new Cube(Cube.X_AXIS, -1, Cube.Z_AXIS, false, false, false));
	
	// Edges - bottom tier
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, -1, true, false, false));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, -1, false, false, false));
	cubeVector.add(new Cube(-1, Cube.Y_AXIS, Cube.Z_AXIS, false, false, true));
	cubeVector.add(new Cube(-1, Cube.Y_AXIS, Cube.Z_AXIS, false, false, false));

	// Corners - top tier
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, true, true, true));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, true, true, false));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, false, true, true));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, false, true, false));

	// Corners - bottom tier
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, true, false, true));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, true, false, false));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, false, false, true));
	cubeVector.add(new Cube(Cube.X_AXIS, Cube.Y_AXIS, Cube.Z_AXIS, false, false, false));

	// randomize the rubik's cube
	System.out.println("randomizing");
	for (i = 0; i < 20; i++) {
	    axis = rand.nextInt() % 3;
	    if (axis < 0) {
		axis = 0 - axis;
	    }
	    polarity = rand.nextBoolean();
	    clockwise = rand.nextBoolean();
	    rotate(cubeVector, axis, polarity, clockwise);
	}
    }


    private void tryRotation(Vector cubeVector, int axis, boolean polarity, boolean clockwise) 
	throws Exception {
	int count = rotate(cubeVector, axis, polarity, clockwise);
	rotate(cubeVector, axis, polarity, clockwise ? false : true);
	if (count > maxHomeCubeCount) {
	    maxHomeCubeCount = count;
	    bestAxis = axis;
	    bestPolarity = polarity;
	    bestClockwise = clockwise;
	}
    }

    private int rotate(Vector cubeVector, int axis, boolean polarity, boolean clockwise) throws Exception {
	Iterator it = cubeVector.iterator();
	boolean solved = true;
	    
	int homeCubeCount = 0;
	//System.out.println("axis = " + axis + " pol = " + polarity + " cw = " + clockwise);
	while (it.hasNext()) {
	    Cube c = (Cube)it.next();
	    //c.dump();
	    if (c.rotate(axis, polarity, clockwise)) {
		homeCubeCount++;
	    }
	    else {
		solved = false;
	    }
	}
	//System.out.println("home cube count = " + homeCubeCount);
	return homeCubeCount;
    }

    private boolean analyze(Vector cubeVector) throws Exception {
	Iterator it = cubeVector.iterator();
	boolean solved = true;
	    
	int homeCubeCount = 0;
	while (it.hasNext()) {
	    Cube c = (Cube)it.next();
	    if (c.isHome()) {
		homeCubeCount++;
	    }
	}
	System.out.println("home cube count = " + homeCubeCount);
	return solved;
    }

    private void solve() throws Exception {
	System.out.println("solving");
	analyze(cubeVector);
	maxHomeCubeCount = 0;
	for (axis = 0; axis < 3; axis++) {
	    tryRotation(cubeVector, axis, false, false);
	    tryRotation(cubeVector, axis, false, true);
	    tryRotation(cubeVector, axis, true, false);
	    tryRotation(cubeVector, axis, true, true);
	}
	analyze(cubeVector);
    }

    public static void main(String args[]) throws Exception {
	RubiksCube rc = new RubiksCube();
	try {
	    rc.solve();
	} catch (Exception e) {
	    System.out.println("Exception " + e);
	}
    }


}

   
