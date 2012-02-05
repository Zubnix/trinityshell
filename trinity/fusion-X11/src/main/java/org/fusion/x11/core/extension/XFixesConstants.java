package org.fusion.x11.core.extension;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public final class XFixesConstants {
	public static final int WindowRegionBounding = 0;
	public static final int WindowRegionClip = 1;

	/**
	 * An <code>XFixesConstants</code> can not be instantiated. Calling this
	 * will result in an <code>InstantiationError</code>.
	 */
	private XFixesConstants() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}
}
