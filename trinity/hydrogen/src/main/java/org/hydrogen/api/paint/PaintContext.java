package org.hydrogen.api.paint;

public interface PaintContext<P> {
	P getPaintPeer();

	PaintableRef getPaintableRef();

	Object queryPaintPeer(PaintableRef paintableRef);

	void bindPaintPeer(PaintableRef paintableRef, P paintPeer);

	// TODO more?
}
