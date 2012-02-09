/*
 *     This file is part of Hypercube.
 *
 * Hypercube is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hypercube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hypercube.  If not, see <http://www.gnu.org/licenses/>.
 */



//**********************************************************************//
//If you want to edit this file and you're not familiar with the syntax,//
//you might want to check out: http://groovy.codehaus.org/Documentation //
//**********************************************************************//

package org.hypercube.groovy

import org.hydrogen.config.DisplayConfiguration
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PropertyInstanceNumbers;
import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hydrogen.displayinterface.PropertyInstanceTexts;
import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.displayinterface.input.Keyboard
import org.hydrogen.displayinterface.input.Keyboard.ModifierName;
import org.hydrogen.displayinterface.input.Momentum
import org.hydrogen.eventsystem.EventBus;
import org.hydrogen.eventsystem.EventHandler
import org.hydrogen.paintinterface.HierarchicalArea;

import org.hypercube.LogicLoadable
import org.hypercube.hyperwidget.ClientContainer

import org.hyperdrive.core.ClientEvent;
import org.hyperdrive.core.ManagedDisplay
import org.hyperdrive.core.RenderAreaPropertiesManipulator;
import org.hyperdrive.geo.GeoEvent
import org.hyperdrive.geo.GeoManagerLine
import org.hyperdrive.geo.GeoManagerLine.LineProperty
import org.hyperdrive.input.KeyBinding
import org.hyperdrive.widget.ClientManager
import org.hyperdrive.widget.ClientNameLabel
import org.hyperdrive.widget.CloseButton
import org.hyperdrive.widget.HideButton
import org.hyperdrive.widget.DragButton
import org.hyperdrive.widget.KeyDrivenAppLauncher
import org.hyperdrive.widget.MaximizeButton;
import org.hyperdrive.widget.ResizeButton

//TODO better documentation
/**
 * A <code>HypercubeLogicLoadable</code> is the entry point of the Groovy script that
 * defines the behavior of a simple floating window manager.
 * 
 * @author Erik De Rijcke
 * @since 0.0.1
 */
class HypercubeLogic implements LogicLoadable{

	//First function that is called upon startup, before any initialization is done.
	void preInit(DisplayConfiguration configuration) {
		//--------------------------------------------//
		//---QFusion (Qt) specific properties---------//
		//---These are fed to the Qt Jambi back-end---//
		//--------------------------------------------//
		//---QFusion theme---//
		configuration.getBackEndProperties().put "qfusion.style", "plastique"
		//configuration.getBackEndProperties().put "qfusion.stylesheet", "path to qt stylesheet"

		//---Only one graphicssystem can be enabled at a time---//
		//configuration.getBackEndProperties().put "qfusion.graphicssystem", "software"
		configuration.getBackEndProperties().put "qfusion.graphicssystem", "raster"
		//configuration.getBackEndProperties().put "qfusion.graphicssystem", "opengl"
		//configuration.getBackEndProperties().put "qfusion.graphicssystem", "opengl2"
	}

	//top bar
	def clientManager

	//background 'desktop'widget
	def virtRoot

	//bottom bar
	def appLauncher

	//This is called after initialization is done.
	void postInit(ManagedDisplay display) {

		//get a reference to the (virtual) virtRoot window
		virtRoot = display.getDefaultVirtualRootRenderArea()

		//initialize the keybindings
		initKeyBindings(display)

		//listen for and handle client events
		def newClientHandler = { event -> handleNewClientEvent event } as EventHandler
		display.addEventHandler newClientHandler, ClientEvent.CLIENT_INITIALIZED

		//create a simple taskbar
		//X11 specific client name property
		clientManager = new ClientManager("WM_NAME");
		clientManager.setParent virtRoot
		clientManager.setHeight 20
		clientManager.setWidth virtRoot.getWidth()
		clientManager.setRelativeX 0
		clientManager.setRelativeY 0
		clientManager.setVisibility true
		clientManager.requestReparent()
		clientManager.requestMoveResize()
		clientManager.requestVisibilityChange()

		//create an app launcher, much like dmenu.
		appLauncher = new KeyDrivenAppLauncher()
		appLauncher.setParent virtRoot
		appLauncher.setHeight 20
		appLauncher.setWidth virtRoot.getWidth()
		appLauncher.setRelativeX 0
		appLauncher.setRelativeY virtRoot.getHeight() - 20
		appLauncher.setVisibility true
		appLauncher.requestReparent()
		appLauncher.requestMoveResize()
		appLauncher.requestVisibilityChange()

		//This call will generate and queue new client events in case any clients
		// existed before starting this program.
		display.manageUnmanagedClientWindows()

		//start listening for client events
		display.startEventDispatcher()
	}

	//the default terminal
	def terminal = "xterm"

	//Keybindings are defined here.
	def initKeyBindings(ManagedDisplay display){
		//MOD_1 is the alt key.
		new KeyBinding( display, Momentum.STARTED, "e", [ModifierName.MOD_1]){
			void action(){
				//launch xterm
				terminal.execute()
			}
		}

		new KeyBinding( display, Momentum.STARTED, "w", [ModifierName.MOD_1]){
			void action(){
				//launch dmenu like menu for launching an application
				appLauncher.startKeyListening()
			}
		}
	}


	//What happens when a new client appears is defined here.
	def handleNewClientEvent(ClientEvent newClientEvent){
		//Create a container.
		//This widget will contain the new client window and other widgets required to resize,
		//close and move the client window.
		def clientContainer = new ClientContainer()
		clientContainer.setRelativeY 20
		clientContainer.setRelativeX 0
		clientContainer.requestMove()
		clientContainer.setParent virtRoot
		clientContainer.requestReparent()
		clientContainer.setVisibility true
		clientContainer.requestVisibilityChange()
		def geoManagerContainer = new GeoManagerLine( clientContainer, false, false )
		clientContainer.setGeoManager geoManagerContainer

		//Create a new drag button and attach it to the client container.
		def dragButton = new DragButton()
		dragButton.setParent clientContainer
		dragButton.requestReparent()
		dragButton.setHeight 20
		dragButton.requestResize()
		dragButton.setVisibility true
		dragButton.requestVisibilityChange()
		def geoManagerdragButton = new GeoManagerLine( dragButton, true, false )
		dragButton.setGeoManager geoManagerdragButton
		geoManagerContainer.addManagedChild dragButton, new LineProperty(0)

		//Create a new close button and attach it to the client container.
		def closeButton = new CloseButton()
		closeButton.setWidth 20
		closeButton.setHeight 20
		closeButton.setParent dragButton
		closeButton.requestReparent()
		closeButton.setVisibility true
		closeButton.requestVisibilityChange()

		def minimizeButton = new HideButton()
		minimizeButton.setWidth 20
		minimizeButton.setHeight 20
		minimizeButton.setRelativeX 20
		minimizeButton.setParent dragButton
		minimizeButton.requestReparent()
		minimizeButton.setVisibility true
		minimizeButton.requestVisibilityChange()

		//reparent the client window to our newly created client container.
		def client = newClientEvent.getRenderArea()
		client.setParent clientContainer
		client.requestReparent()
		client.setVisibility true
		client.requestVisibilityChange()
		geoManagerContainer.addManagedChild client, new LineProperty(100)

		//add a client visible handler to the client
		def clientVisibilityHandler = { event -> handleClientVisibilityEvent event, clientContainer} as EventHandler
		client.addEventHandler clientVisibilityHandler, GeoEvent.VISIBILITY

		//add a client resize handler to the client
		def clientResizeRequestHandler = { event -> handleClientResizeRequest event, clientContainer } as EventHandler
		client.addEventHandler clientResizeRequestHandler, GeoEvent.RESIZE_REQUEST
		client.addEventHandler clientResizeRequestHandler, GeoEvent.MOVE_RESIZE_REQUEST

		//add a client destroy handler to the client
		def clientDestroyedHandler = { event -> handleClientDestroyedEvent event, clientContainer } as EventHandler
		client.addEventHandler clientDestroyedHandler, GeoEvent.DESTROYED

		//add a client raise handler to the client on raise request
		def clientRaiseHandler = { event -> handleClientRaise event, clientContainer } as EventHandler
		client.addEventHandler clientRaiseHandler, GeoEvent.RAISE_REQUEST
		client.addEventHandler clientRaiseHandler, GeoEvent.RAISE

		//add a client lower handler to the client
		def clientLowerHandler = { event -> handleClientLower event, clientContainer } as EventHandler
		client.addEventHandler clientLowerHandler, GeoEvent.LOWER_REQUEST
		client.addEventHandler clientLowerHandler, GeoEvent.LOWER

		//add a client name label to the container.
		//X11 specific client name
		def clientNameLabel = new ClientNameLabel("WM_NAME")
		clientNameLabel.setParent clientContainer
		clientNameLabel.requestReparent()
		clientNameLabel.setHeight 20
		clientNameLabel.requestResize()
		clientNameLabel.setVisibility true
		clientNameLabel.requestVisibilityChange()

		//set a horizontal layout manager to the name label
		def geoManagerNameLabel = new GeoManagerLine(clientNameLabel, true, true)
		clientNameLabel.setGeoManager geoManagerNameLabel
		geoManagerContainer.addManagedChild clientNameLabel, new LineProperty(0)

		//add a resize button to the name label.
		def resizeButton = new ResizeButton()
		resizeButton.setWidth 20
		resizeButton.setHeight 20
		resizeButton.setParent clientNameLabel
		resizeButton.requestReparent()
		resizeButton.setVisibility true
		resizeButton.requestVisibilityChange()
		geoManagerNameLabel.addManagedChild resizeButton, new LineProperty(0)

		//Create a new maximize button and attach it to the client container
		def maximizeButton = new MaximizeButton()
		maximizeButton.setWidth 20
		maximizeButton.setHeight 20
		maximizeButton.setParent clientNameLabel
		maximizeButton.requestReparent()
		maximizeButton.setVisibility true
		maximizeButton.requestVisibilityChange()
		//specifiy the maximize region, this region is read every time maximalization is done.
		maximizeButton.setMaximizeToArea(new MaximizeButton.MaximizeArea(){
					int getAbsoluteX() {
						return 0
					}

					int getAbsoluteY() {
						return clientManager.getHeight()
					}

					int getWidth() {
						return virtRoot.getWidth()
					}

					int getHeight() {
						return virtRoot.getHeight() - ( clientManager.getHeight() + appLauncher.getHeight() )
					}
				})
		geoManagerNameLabel.addManagedChild maximizeButton, new LineProperty(0)

		//add a raise & inputrequestor handler to the dragbar and resize button on mouse button pressed.
		def raiseHandler = { event -> requestRaiseAndInput event, clientContainer, client } as EventHandler
		dragButton.addEventHandler raiseHandler, ButtonNotifyEvent.PRESSED_TYPE, 0
		resizeButton.addEventHandler raiseHandler, ButtonNotifyEvent.PRESSED_TYPE, 0
		//add raise handler to client on mouse button press.
		//When the mouse is grabbed for the client,
		//the raise handler will be activated.
		client.addEventHandler raiseHandler, ButtonNotifyEvent.PRESSED_TYPE, 0

		//assign the control widgets their targets
		dragButton.setTargetRenderArea clientContainer
		closeButton.setTargetRenderArea client
		minimizeButton.setTargetRenderArea clientContainer
		maximizeButton.setTargetRenderArea clientContainer
		resizeButton.setTargetRenderArea client
		clientNameLabel.setTargetWindow client

		//add the new client to the client manager
		clientManager.manageClient client

		//update the client's size and place
		client.requestMoveResize()

		//install a mouse enter/leave tracker on the client.
		def mouseEnterHandler = { event -> handleMouseEnter event, clientContainer, client } as EventHandler
		client.addEventHandler mouseEnterHandler, MouseEnterLeaveNotifyEvent.ENTER_TYPE
		def mouseLeaveHandler = { event -> handleMouseLeave event, clientContainer, client } as EventHandler
		client.addEventHandler mouseLeaveHandler,  MouseEnterLeaveNotifyEvent.LEAVE_TYPE

		//raise the client and give it the input focus
		client.requestRaise()
		client.giveInputFocus()
	}

	//called when the mouse cursor leaves a client
	def handleMouseLeave(event, clientContainer, client){
		if(client.hasInputFocus()){
			//do nothing
		}else{
			//release mouse grab
			client.getPlatformRenderArea()
					.stopMouseInputCatching()
		}
	}

	//called when the mouse cursor enters a client window
	def handleMouseEnter(event, clientContainer, client){
		if(client.hasInputFocus()){
			//release mouse grab
			client.getPlatformRenderArea()
					.stopMouseInputCatching()
		}else{
			//grab mouse for this client,
			client.getPlatformRenderArea()
					.catchAllMouseInput()
		}
	}

	//called when the mouse button is pressed on the dragbar & resize button
	def requestRaiseAndInput(event, clientContainer, client){
		if (event.getInput() .getMomentum() == Momentum.STARTED) {
			clientContainer.requestRaise()
			client.giveInputFocus()

		}
		client.getPlatformRenderArea()
				.stopMouseInputCatching()
	}

	def handleClientRaise(event, clientContainer){
		clientContainer.requestRaise()
	}

	def handleClientLower(event, clientContainer){
		clientContainer.requestLower()
	}

	def handleClientVisibilityEvent(event, clientContainer){
		boolean visible = event.getTransformation().isVisible1()
		clientContainer.setVisibility visible
		clientContainer.requestVisibilityChange()
	}

	def handleClientResizeRequest(event, clientContainer){
		def width = event.getTransformation().getWidth1()
		def height = event.getTransformation().getHeight1()

		//+2 pixels for the left and right border
		//FIXME add option to geomanager line to allow for spacing between container and its layed out children.
		clientContainer.setWidth width //+2

		//+40(=2*20) for the top and bottom control area
		clientContainer.setHeight height+40
		clientContainer.requestResize()
	}

	def handleClientDestroyedEvent(event, clientContainer){
		clientContainer.doDestroy()
	}
}