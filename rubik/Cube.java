package rubik;

public class Cube {

	// the 3 axes
	public final static int X_AXIS = 0;
	public final static int Y_AXIS = 1;
	public final static int Z_AXIS = 2;

	// rotations through the different axes
	public final static int[][] ROTATION_AXES = { // 3 axes, 4 positions
	{ Y_AXIS, Z_AXIS, Y_AXIS, Z_AXIS }, // rotate through x axis
			{ X_AXIS, Z_AXIS, X_AXIS, Z_AXIS }, // rotate through y axis
			{ X_AXIS, Y_AXIS, X_AXIS, Y_AXIS } // rotate through z axis
	};

	public final static boolean[][] ROTATION_POLARITIES = { // 3 axes, 4 positions
	{ true, false, false, true }, // rotate through x axis
			{ true, true, false, false }, // rotate through y axis
			{ true, false, false, true } // rotate through z axis
	};

	// orientation
	private int axis = X_AXIS; // xyz
	private boolean polarity; // 1 or 0
	private int rotation; // 0-3

	// initial position
	private int[] initExposedAxis = new int[3];
	private boolean[] initExposedPolarity = new boolean[3];

	// current position
	private final int[] exposedAxis = new int[3];
	private final boolean[] exposedPolarity = new boolean[3];

	public Cube(int exposedAxis0, int exposedAxis1, int exposedAxis2,
			boolean exposedPolarity0, boolean exposedPolarity1,
			boolean exposedPolarity2) {
		
		initExposedAxis[0] = exposedAxis0;
		initExposedAxis[1] = exposedAxis1;
		initExposedAxis[2] = exposedAxis2;
		initExposedPolarity[0] = exposedPolarity0;
		initExposedPolarity[1] = exposedPolarity1;
		initExposedPolarity[2] = exposedPolarity2;

		setAxis(X_AXIS);
		setPolarity(true);
		setRotation(0);
		setExposedAxes(initExposedAxis, initExposedPolarity);
	}

	/**
	 * Copy constructor
	 * 
	 * @param other
	 */
	public Cube(Cube other) {
		this.initExposedAxis = other.initExposedAxis;
		this.initExposedPolarity = other.initExposedPolarity;
		
		setAxis(other.axis);
		setPolarity(other.polarity);
		setRotation(other.rotation);
		
		setExposedAxes(other.exposedAxis, other.exposedPolarity);
	}

	
	/**
	 * @return true if cube is in original position, false otw
	 */
	public boolean isHome() throws Exception {

		int i, j;
		int matchCount;
		int exposedAxisCount;
		int[] tmpAxis = new int[3];
		boolean[] tmpPolarity = new boolean[3];

		matchCount = 0;
		for (i = 0; i < 3; i++) {
			tmpAxis[i] = exposedAxis[i];
			tmpPolarity[i] = exposedPolarity[i];
		}
		exposedAxisCount = 0;
		for (i = 0; i < 3; i++) {
			if (exposedAxis[i] == initExposedAxis[i]
					&& exposedPolarity[i] == initExposedPolarity[i]) {
				matchCount++;
				tmpAxis[i] = 0;
			}
			if (exposedAxis[i] != -1) {
				exposedAxisCount++;
			}
		}
		if (matchCount > 3) {
			throw new Exception();
		}
		if (matchCount == 3) {

			// we don't care about the axis of the center cubes
			if (exposedAxisCount > 1 && axis != X_AXIS)
				return false;

			return true;
		} else {
			return false;
		}
	}

	public boolean isCube(int exposedAxis0, int exposedAxis1, int exposedAxis2,
			boolean exposedPolarity0, boolean exposedPolarity1,
			boolean exposedPolarity2) {

		if (exposedAxis0 != initExposedAxis[0]) {
			return false;
		}
		if (exposedAxis1 != initExposedAxis[1]) {
			return false;
		}
		if (exposedAxis2 != initExposedAxis[2]) {
			return false;
		}
		if (exposedPolarity0 != initExposedPolarity[0]) {
			return false;
		}
		if (exposedPolarity1 != initExposedPolarity[1]) {
			return false;
		}
		if (exposedPolarity2 != initExposedPolarity[2]) {
			return false;
		}
		return true;
	}


	public void setAxis(int a) {
		if (a != X_AXIS && a != Y_AXIS && a != Z_AXIS) {
			throw new IllegalArgumentException();
		} else {
			axis = a;
		}
	}

	public int getAxis() {
		return axis;
	}

	public void setPolarity(boolean p) {
		polarity = p;
	}

	public boolean getPolarity() {
		return polarity;
	}

	public void setRotation(int r) {
		if (r < 0 || r > 3) {
			throw new IllegalArgumentException();
		} else {
			rotation = r;
		}
	}

	public int getRotation() {
		return rotation;
	}

	public void setExposedAxes(int[] axis, boolean[] polarity) {

		int i;
		boolean gotX, gotY, gotZ;

		gotX = gotY = gotZ = false;
		for (i = 0; i < 3; i++) {

			exposedAxis[i] = axis[i];
			exposedPolarity[i] = polarity[i];

			switch (axis[i]) {
			case X_AXIS:
				if (gotX) {
					throw new IllegalArgumentException();
				}
				gotX = true;
				break;
			case Y_AXIS:
				if (gotY) {
					throw new IllegalArgumentException();
				}
				gotY = true;
				break;
			case Z_AXIS:
				if (gotZ) {
					throw new IllegalArgumentException();
				}
				gotZ = true;
				break;
			case -1:
				break;
			default:
				throw new IllegalArgumentException();
			}

		}
		if (!gotX && !gotY && !gotZ) {
			throw new IllegalArgumentException();
		}
	}

	int[] getExposedAxis() {
		return exposedAxis;
	}

	boolean[] getExposedPolarity() {
		return exposedPolarity;
	}

	/**
	 * @return true if cube is in original position, false otw
	 */
	public boolean rotate(int axis, boolean polarity, boolean clockwise)
			throws Exception {

		int matchedAxis = -1;
		int axisToChange = -1;
		int i, j;
		int leadingAxis = -1, trailingAxis = -1;

		if (axis != X_AXIS && axis != Y_AXIS && axis != Z_AXIS) {
			throw new IllegalArgumentException();
		}
		// walk the exposed axes
		for (i = 0; i < 3; i++) {
			if (axis == exposedAxis[i] && polarity == exposedPolarity[i]) {
				matchedAxis = i;
			} else if (exposedAxis[i] != -1) {
				// System.out.println("i = " + i);

				if (axisToChange == -1) {
					axisToChange = i; // assume for now
				} else {

					// find "trailing axis"
					int index = 0;
					int increment = 1;
					if (clockwise) {
						index = 3;
						increment = -1;
					}
					leadingAxis = trailingAxis = -1;
					// System.out.println("i = " + i + " index = " + index);
					for (j = 0; j < 4; j++) {
						// System.out.println("j = " + j);
						if (ROTATION_AXES[axis][index] == exposedAxis[axisToChange]
								&& ROTATION_POLARITIES[axis][index] == exposedPolarity[axisToChange]) {
							if (leadingAxis == -1) {
								leadingAxis = axisToChange;
							} else {
								trailingAxis = axisToChange;
								break; // got it
							}
						} else if (ROTATION_AXES[axis][index] == exposedAxis[i]
								&& ROTATION_POLARITIES[axis][index] == exposedPolarity[i]) {
							if (leadingAxis == -1) {
								leadingAxis = i;
							} else {
								trailingAxis = i;
								break; // got it
							}
						} else {
							// System.out.println("leadingAxis = " +
							// leadingAxis);

							if (leadingAxis != -1) {
								if (j != 1) {
									// throw new Exception(); // invalid cube
								}
								trailingAxis = leadingAxis; // switch
								break;
							}
						}
						index += increment;
					}
				}
			}
		}
		if (matchedAxis == -1) {
			// System.out.println("NO ROTATE");
			return isHome(); // this cube is not affected
		}
		// System.out.println("ROTATE");
		if (trailingAxis != -1) {
			// must be a corner (3 exposures), invert polarity
			exposedPolarity[trailingAxis] = (exposedPolarity[trailingAxis] ? false
					: true);
		} else if (axisToChange != -1) {
			// must be an edge (2 exposures), turn to next position
			CubeOrientation co = new CubeOrientation(exposedAxis[axisToChange],
					exposedPolarity[axisToChange]);
			co.rotate(axis, clockwise);
			exposedAxis[axisToChange] = co.getAxis();
			exposedPolarity[axisToChange] = co.getPolarity();
		}

		// almost done, now just change orientation
		if (axis == this.axis) {
			// just rotate
			if (clockwise == polarity) {
				rotation++;
				if (rotation == 4) {
					rotation = 0;
				}
			} else {
				rotation--;
				if (rotation == -1) {
					rotation = 3;
				}
			}
		} else {
			// need to rotate
			CubeOrientation co = new CubeOrientation(this.axis, this.polarity);
			co.rotate(axis, clockwise);
			this.axis = co.getAxis();
			this.polarity = co.getPolarity();
		}
		return isHome(); // this cube is not affected

	}

	public void dump() throws Exception {
		int[] exposedAxis;
		boolean[] exposedPolarity;

		System.out.println("axis = " + axisName(this.getAxis())
				+ " polarity = " + this.getPolarity() + " rotation = "
				+ this.getRotation());

		exposedAxis = this.getExposedAxis();
		exposedPolarity = this.getExposedPolarity();
		if (exposedAxis.length != 3 || exposedPolarity.length != 3) {
			throw new Exception();
		}
		System.out.println("exposed axes = " + axisName(exposedAxis[0]) + " "
				+ axisName(exposedAxis[1]) + " " + axisName(exposedAxis[2]));
		System.out.println("exposed pol  = " + exposedPolarity[0] + " "
				+ exposedPolarity[1] + " " + exposedPolarity[2]);
		System.out.println("");
	}

	private static String axisName(int axis) throws Exception {
		switch (axis) {
		case -1:
			return "N/A  ";
		case Cube.X_AXIS:
			return "X    ";
		case Cube.Y_AXIS:
			return "Y    ";
		case Cube.Z_AXIS:
			return "Z    ";
		default:
			throw new Exception();
		}
	}

	private class CubeOrientation {
		private int axis; // x y or z (0-2)
		private boolean polarity;

		public CubeOrientation(int axis, boolean polarity) {
			this.axis = axis;
			this.polarity = polarity;
		}

		public void rotate(int turningAxis, boolean clockwise) throws Exception {

			int j;

			for (j = 0; j < 4; j++) {
				if (ROTATION_AXES[turningAxis][j] == this.axis
						&& ROTATION_POLARITIES[turningAxis][j] == this.polarity) {
					if (clockwise) {
						j++;
						if (j == 4)
							j = 0;
					} else {
						j--;
						if (j == -1)
							j = 3;
					}
					this.axis = ROTATION_AXES[turningAxis][j];
					this.polarity = ROTATION_POLARITIES[turningAxis][j];
					break;
				}
			}
			if (j == 4) {
				throw new Exception();
			}
		}

		public int getAxis() {
			return this.axis;
		}

		public boolean getPolarity() {
			return this.polarity;
		}

	}

}
