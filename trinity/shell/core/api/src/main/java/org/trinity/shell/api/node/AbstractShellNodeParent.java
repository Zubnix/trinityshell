package org.trinity.shell.api.node;

import java.util.HashSet;
import java.util.Set;

import org.trinity.shell.api.node.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.node.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.node.event.ShellNodeEvent;
import org.trinity.shell.api.node.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;

public abstract class AbstractShellNodeParent extends AbstractShellNode implements ShellNodeParent {

	private final EventBus nodeEventBus;

	private final Set<ShellNode> children = new HashSet<ShellNode>();

	private ShellLayoutManager shellLayoutManager;

	public AbstractShellNodeParent(final EventBus nodeEventBus) {
		super(nodeEventBus);
		this.nodeEventBus = nodeEventBus;
	}

	@Override
	public ShellNode[] getChildren() {
		return this.children.toArray(new ShellNode[this.children.size()]);
	}

	protected void updateChildrenPosition() {
		for (final ShellNode child : getChildren()) {
			final int childX = child.getX();
			final int childY = child.getY();
			child.getShellNodeExecutor().move(	childX,
												childY);
		}
	}

	@Override
	public ShellLayoutManager getLayoutManager() {
		return this.shellLayoutManager;
	}

	@Override
	public void setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		this.shellLayoutManager = shellLayoutManager;
		this.nodeEventBus.register(shellLayoutManager);
	}

	@Override
	protected void doMove(final boolean execute) {
		super.doMove(execute);
		updateChildrenPosition();
	}

	@Override
	protected void doMoveResize(final boolean execute) {
		super.doMoveResize(execute);
		updateChildrenPosition();
		layout();
	}

	@Override
	protected void doResize(final boolean execute) {
		super.doResize(execute);
		layout();
	}

	@Override
	public void handleChildReparentEvent(final ShellNode child) {
		ShellNodeEvent shellNodeEvent;
		if (this.children.contains(child)) {
			this.children.remove(child);
			// child.removeShellNodeEventHandler(this);
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformation());
			this.nodeEventBus.post(shellNodeEvent);
		} else {
			this.children.add(child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformation());
			this.nodeEventBus.post(shellNodeEvent);
		}
	}

	@Override
	public void layout() {
		final ShellLayoutManager layoutManager = getLayoutManager();
		if (layoutManager == null) {
			return;
		}
		getLayoutManager().layout(this);
	}
}
