package rubik;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class RubiksCube {
 
    private static int maxHomeCubeCount;
    private static int homeCubeCount;
    
    private Vector<Cube> cubeVector = new Vector<Cube>();
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


	private void tryRotation(Vector<Cube> cubeVector, int axis, boolean polarity,
			boolean clockwise, int rescursionCount) throws Exception {
		int count = rotate(cubeVector, axis, polarity, clockwise);
		rotate(cubeVector, axis, polarity, clockwise ? false : true);
		if (count > maxHomeCubeCount) {
			maxHomeCubeCount = count;
			bestAxis = axis;
			bestPolarity = polarity;
			bestClockwise = clockwise;
		}
		System.out.println("rescursionCount = " + rescursionCount);
		rescursionCount--;
		if (rescursionCount > 0) {
			tryRotation(cubeVector,  axis,  polarity,
			 clockwise,  rescursionCount);
		}
	}

	private int rotate(Vector<Cube> cubeVector, int axis, boolean polarity,
			boolean clockwise) throws Exception {
		Iterator<Cube> it = cubeVector.iterator();
		boolean solved = true;

		int homeCubeCount = 0;
		// System.out.println("axis = " + axis + " pol = " + polarity + " cw = "
		// + clockwise);
		while (it.hasNext()) {
			Cube c = (Cube) it.next();
			// c.dump();
			if (c.rotate(axis, polarity, clockwise)) {
				homeCubeCount++;
			} else {
				solved = false;
			}
		}
		// System.out.println("home cube count = " + homeCubeCount);
		return homeCubeCount;
	}

	private boolean analyze(Vector<Cube> cubeVector) throws Exception {
		Iterator<Cube> it = cubeVector.iterator();
		boolean solved = true;

	    homeCubeCount = 0;
		while (it.hasNext()) {
			Cube c = (Cube) it.next();
			if (c.isHome()) {
				homeCubeCount++;
			}
		}
		System.out.println("home cube count = " + homeCubeCount);
		System.out.println("maxHomeCubeCount = " + maxHomeCubeCount);
		if (homeCubeCount == 26) {
			System.out.println("SOLVED!!");
		}
		return solved;
	}

	private void solve() throws Exception {
		System.out.println("trying all rotations...");
		for (axis = 0; axis < 3; axis++) {
			tryRotation(cubeVector, axis, false, false, 1);
			tryRotation(cubeVector, axis, false, true, 1);
			tryRotation(cubeVector, axis, true, false, 1);
			tryRotation(cubeVector, axis, true, true, 1);
		}
		analyze(cubeVector);
		rotate(cubeVector, this.bestAxis, this.bestPolarity, this.bestClockwise);

		if (maxHomeCubeCount <= homeCubeCount) {
			Thread.sleep(3000L);
			System.out.println("recursing");
			solve();
		}
	}

    public static void main(String args[]) throws Exception {
	RubiksCube rc = new RubiksCube();
	maxHomeCubeCount = 0;

	try {
			while (true) {
				rc.solve();
				Thread.sleep(3000L);
			}
	} catch (Exception e) {
	    System.out.println("Exception " + e);
	}
    }


}

   
