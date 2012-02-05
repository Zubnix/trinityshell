package org.hyperdrive.unit.core;

public class TestTODO {
	// I --core events--:
	// 1) a received event on the (managed) display should be delegated to the
	// appropriate actor(s). -> EventDispatcher
	// 1a) button press notify
	// 1b) button release notify
	// 1c) configure request
	// 1d) destroy notify
	// 1e) focus notify
	// 1f) key pressed notify
	// 1g) key released notify
	// 1h) map request
	// 1i) mouse enter notify
	// 1j) mouse leave notify
	// 1k) property changed notify (relates to 4)
	// 1l) unmap notify
	// 2) specific events should be automatically handled by the abstract render
	// area -> AbstractRenderArea
	// 2a) destroy notify
	// 2b) propertychanged notify
	// 3) specific events should be automatically handled by the client ->
	// ClientWindow:
	// 3a) Map request
	// 3b) unmap notify
	// 3c) configure request

	// II -- core geoexecution--
	// 1) test overridden geoexecutor methods with both virtual and abstract
	// render areas as parent & children -> RenderAreaGeoExecutor:
	// 1a) destroy: without children, with children (mixed virtual & non
	// virtual)
	// 1b) lower: without children, with children (mixed virtual & non virtual)
	// 1c) raise: without children, with children (mixed virtual & non virtual)
	// 1d) updateparent: to virtual parent, to non virtual parent
	// 1e) updateplace: without children and virtual parent, with children
	// (mixed virtual & non virtual) and virtual parent, without children and
	// non virtual parent, with children (mixed virtual & non virtual) and non
	// virtual parent
	// 1f) updatesize
	// 1g) updatesizeplace: without children and virtual parent, with children
	// (mixed virtual & non virtual) and virtual parent, without children and
	// non virtual parent, with children (mixed virtual & non virtual) and non
	// virtual parent
	// 1h) updatevisibility: without children, with children (mixed virtual &
	// non virtual)

	// III --windowmanagementinfo--:
	// 1) quering the windowmanagementinfo should yield correct results ->
	// WindowManagementInfo:
	// 1a) client stacking: no clients, new client, lower client, raise client
	// 1b) client creation: no previous clients, previous clients
	// 1c) client focus: no focus, widget focus, client focus
	// 1d) client deletion: no previous clients, previous clients
	// 1e) virtualroot creation: no previous vroots, previous vroots
	// 1f) virtualroot activation: no previous vroots, previous vroots
	// 1g) virtualroot deletion: no previous vroots, previous vroots
	// 2) when the state of the windowmanagementinfo changes, the listeners
	// attached to it should be notified correctly:
	// 2a) client stacking: no clients, new client, lower client, raise client
	// 2b) client creation: no previous clients, previous clients
	// 2c) client focus: no focus, widget focus, client focus
	// 2d) client deletion: no previous clients, previous clients
	// 2e) virtualroot creation: no previous vroots, previous vroots
	// 2f) virtualroot activation: no previous vroots, previous vroots
	// 2g) virtualroot deletion: no previous vroots, previous vroots

	// IV --core misc--
	// 1) TODO RenderAreaPropertyDelegate
	// 2) TODO minwidth, maxwith, withinc, heightinc, geopref from
	// platformrenderarea etc.

	// XXX
	// 5 (->move this to geo testing): raising of a parent should fire a raise
	// event on
	// the child as
	// well.
	// 5a) a parent has a single child. child receives raise event.
	// 5b) a parent has multiple childs. childs receive raise
	// events starting from bottom to top.
}
