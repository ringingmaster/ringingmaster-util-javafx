package org.ringingmaster.util.javafx.grid.canvas;

import com.google.common.base.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public class TooltipBehavior {

    private static String TOOLTIP_PROP_KEY = "javafx.scene.control.Tooltip";

    private static int TOOLTIP_XOFFSET = 10;
    private static int TOOLTIP_YOFFSET = 17;

    /*
     * There are two key concepts with Tooltip: activated and visible. A Tooltip
     * is activated as soon as a mouse move occurs over the target node. When it
     * becomes activated, we start off the ACTIVATION_TIMER. If the
     * ACTIVATION_TIMER expires before another mouse event occurs, then we will
     * show the popup. This timer typically lasts about 1 second.
     *
     * Once visible, we reset the ACTIVATION_TIMER and start the HIDE_TIMER.
     * This second timer will allow the tooltip to remain visible for some time
     * period (such as 5 seconds). If the mouse hasn't moved, and the HIDE_TIMER
     * expires, then the tooltip is hidden and the tooltip is no longer
     * activated.
     *
     * If another mouse move occurs, the ACTIVATION_TIMER starts again, and the
     * same rules apply as above.
     *
     * If a mouse exit event occurs while the HIDE_TIMER is ticking, we reset
     * the HIDE_TIMER. Thus, the tooltip disappears after 5 seconds from the
     * last mouse move.
     *
     * If some other mouse event occurs while the HIDE_TIMER is running, other
     * than mouse move or mouse enter/exit (such as a click), then the tooltip
     * is hidden, the HIDE_TIMER stopped, and activated set to false.
     *
     * If a mouse exit occurs while the HIDE_TIMER is running, we stop the
     * HIDE_TIMER and start the LEFT_TIMER, and immediately hide the tooltip.
     * This timer is very short, maybe about a 1/2 second. If the mouse enters a
     * new node which also has a tooltip before LEFT_TIMER expires, then the
     * second tooltip is activated and shown immediately (the ACTIVATION_TIMER
     * having been bypassed), and the HIDE_TIMER is started. If the LEFT_TIMER
     * expires and there is no mouse movement over a control with a tooltip,
     * then we are back to the initial steady state where the next mouse move
     * over a node with a tooltip installed will start the ACTIVATION_TIMER.
     */

    private Timeline activationTimer = new Timeline();
    private Timeline hideTimer = new Timeline();
    private Timeline leftTimer = new Timeline();

    /**
     * The Node with a tooltip over which the mouse is hovering. There can
     * only be one of these at a time.
     */
    private Node hoveredNode;

    /**
     * The tooltip that is currently activated. There can only be one
     * of these at a time.
     */
    private Tooltip activatedTooltip;

    /**
     * The tooltip that is currently visible. There can only be one
     * of these at a time.
     */
    private Tooltip visibleTooltip;

    /**
     * The last position of the mouse, in screen coordinates.
     */
    private double lastMouseX;
    private double lastMouseY;

    private boolean hideOnExit;
    private boolean cssForced = false;

    public TooltipBehavior(final boolean hideOnExit) {
        this.hideOnExit = hideOnExit;

        activationTimer.setOnFinished(event -> {
            // Show the currently activated tooltip and start the
            // HIDE_TIMER.
            assert activatedTooltip != null;
            final Window owner = getWindow(hoveredNode);
            final boolean treeVisible = isWindowHierarchyVisible(hoveredNode);

            // If the ACTIVATED tooltip is part of a visible window
            // hierarchy, we can go ahead and show the tooltip and
            // start the HIDE_TIMER.
            //
            // If the owner is null or invisible, then it either means a
            // bug in our code, the node was removed from a scene or
            // window or made invisible, or the node is not part of a
            // visible window hierarchy. In that case, we don't show the
            // tooltip, and we don't start the HIDE_TIMER. We simply let
            // ACTIVATED_TIMER expire, and wait until the next mouse
            // the movement to start it again.
            if (owner != null && owner.isShowing() && treeVisible &&
                    activatedTooltip != null && !Strings.isNullOrEmpty(activatedTooltip.getText())) {
                double x = lastMouseX;
                double y = lastMouseY;

                // The tooltip always inherits the nodeOrientation of
                // the Node that it is attached to (see RT-26147). It
                // is possible to override this for the Tooltip content
                // (but not the popup placement) by setting the
                // nodeOrientation on tooltip.getScene().getRoot().
                NodeOrientation nodeOrientation = hoveredNode.getEffectiveNodeOrientation();
                activatedTooltip.getScene().setNodeOrientation(nodeOrientation);
                if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
                    x -= activatedTooltip.getWidth();
                }

                activatedTooltip.show(owner, x + TOOLTIP_XOFFSET, y + TOOLTIP_YOFFSET);

                // RT-37107: Ensure the tooltip is displayed in a position
                // where it will not be under the mouse, even when the tooltip
                // is near the edge of the screen
                if ((y + TOOLTIP_YOFFSET) > activatedTooltip.getAnchorY()) {
                    // the tooltip has been shifted vertically upwards,
                    // most likely to be underneath the mouse cursor, so we
                    // need to shift it further by hiding and reshowing
                    // in another location
                    activatedTooltip.hide();

                    y -= activatedTooltip.getHeight();
                    activatedTooltip.show(owner, x + TOOLTIP_XOFFSET, y);
                }

                visibleTooltip = activatedTooltip;
                hoveredNode = null;
                if (activatedTooltip.getShowDuration() != null) {
                    hideTimer.getKeyFrames().setAll(new KeyFrame(activatedTooltip.getShowDuration()));
                }
                hideTimer.playFromStart();
            }

            // Once the activation timer has expired, the tooltip is no
            // longer in the activated state, it is only in the visible
            // state, so we go ahead and set activated to false
           //TODO activatedTooltip.setActivated(false);
            activatedTooltip = null;
        });

        hideTimer.setOnFinished(event -> {
            // Hide the currently visible tooltip.
            assert visibleTooltip != null;
            visibleTooltip.hide();
            visibleTooltip = null;
            hoveredNode = null;
        });

        leftTimer.setOnFinished(event -> {
            if (!hideOnExit) {
                // Hide the currently visible tooltip.
                assert visibleTooltip != null;
                visibleTooltip.hide();
                visibleTooltip = null;
                hoveredNode = null;
            }
        });
    }

    /**
     * Registers for mouse move events only. When the mouse is moved, this
     * handler will detect it and decide whether to start the ACTIVATION_TIMER
     * (if the ACTIVATION_TIMER is not started), restart the ACTIVATION_TIMER
     * (if ACTIVATION_TIMER is running), or skip the ACTIVATION_TIMER and just
     * show the tooltip (if the LEFT_TIMER is running).
     */
    private EventHandler<MouseEvent> MOVE_HANDLER = (MouseEvent event) -> {
        //Screen coordinates need to be actual for dynamic tooltip.
        //See Tooltip.setText

        lastMouseX = event.getScreenX();
        lastMouseY = event.getScreenY();

        // If the HIDE_TIMER is running, then we don't want this event
        // handler to do anything, or change any state at all.
        if (hideTimer.getStatus() == Timeline.Status.RUNNING) {
            return;
        }

        // Note that the "install" step will both register this handler
        // with the target node and also associate the tooltip with the
        // target node, by stashing it in the client properties of the node.
        hoveredNode = (Node) event.getSource();
        Tooltip t = (Tooltip) hoveredNode.getProperties().get(TOOLTIP_PROP_KEY);
        if (t != null) {
            // In theory we should never get here with an invisible or
            // non-existant window hierarchy, but might in some cases where
            // people are feeding fake mouse events into the hierarchy. So
            // we'll guard against that case.
            final Window owner = getWindow(hoveredNode);
            final boolean treeVisible = isWindowHierarchyVisible(hoveredNode);
            if (owner != null && treeVisible) {
                // Now we know that the currently HOVERED node has a tooltip
                // and that it is part of a visible window Hierarchy.
                // If LEFT_TIMER is running, then we make this tooltip
                // visible immediately, stop the LEFT_TIMER, and start the
                // HIDE_TIMER.
                if (leftTimer.getStatus() == Timeline.Status.RUNNING) {
                    if (visibleTooltip != null) visibleTooltip.hide();
                    visibleTooltip = t;
                    t.show(owner, event.getScreenX() + TOOLTIP_XOFFSET,
                            event.getScreenY() + TOOLTIP_YOFFSET);
                    leftTimer.stop();
                    if (t.getShowDuration() != null) {
                        hideTimer.getKeyFrames().setAll(new KeyFrame(t.getShowDuration()));
                    }
                    hideTimer.playFromStart();
                } else {
                    // Force the CSS to be processed for the tooltip so that it uses the
                    // appropriate timings for showDelay, showDuration, and hideDelay.
                    if (!cssForced) {
                        double opacity = t.getOpacity();
                        t.setOpacity(0);
                        t.show(owner);
                        t.hide();
                        t.setOpacity(opacity);
                        cssForced = true;
                    }

                    // Start / restart the timer and make sure the tooltip
                    // is marked as activated.
                   //TODO t.setActivated(true);
                    activatedTooltip = t;
                    activationTimer.stop();
                    if (t.getShowDelay() != null) {
                        activationTimer.getKeyFrames().setAll(new KeyFrame(t.getShowDelay()));
                    }
                    activationTimer.playFromStart();
                }
            }
        } else {
            // TODO should deregister, no point being here anymore!
        }
    };

    /**
     * Registers for mouse exit events. If the ACTIVATION_TIMER is running then
     * this will simply stop it. If the HIDE_TIMER is running then this will
     * stop the HIDE_TIMER, hide the tooltip, and start the LEFT_TIMER.
     */
    private EventHandler<MouseEvent> LEAVING_HANDLER = (MouseEvent event) -> {
        // detect bogus mouse exit events, if it didn't really move then ignore it
        if (activationTimer.getStatus() == Timeline.Status.RUNNING) {
            activationTimer.stop();
        } else if (hideTimer.getStatus() == Timeline.Status.RUNNING) {
            assert visibleTooltip != null;
            hideTimer.stop();
            if (hideOnExit) visibleTooltip.hide();
            Node source = (Node) event.getSource();
            Tooltip t = (Tooltip) source.getProperties().get(TOOLTIP_PROP_KEY);
            if (t != null) {
                if (t.getHideDelay() != null) {
                    leftTimer.getKeyFrames().setAll(new KeyFrame(t.getHideDelay()));
                }
                leftTimer.playFromStart();
            }
        }

        hoveredNode = null;
        activatedTooltip = null;
        if (hideOnExit) visibleTooltip = null;
    };

    /**
     * Registers for mouse click, press, release, drag events. If any of these
     * occur, then the tooltip is hidden (if it is visible), it is deactivated,
     * and any and all timers are stopped.
     */
    private EventHandler<MouseEvent> KILL_HANDLER = (MouseEvent event) -> {
        stopAll();
    };

    private void stopAll() {
        activationTimer.stop();
        hideTimer.stop();
        leftTimer.stop();
        if (visibleTooltip != null) visibleTooltip.hide();
        hoveredNode = null;
        activatedTooltip = null;
        visibleTooltip = null;
    }


    private ChangeListener<String> EMPTY_TOOLTIP_HANDLER = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        if (Strings.isNullOrEmpty(newValue)) {
            stopAll();
        }
    };

    void install(Node node, Tooltip t) {
        // Install the MOVE_HANDLER, LEAVING_HANDLER, and KILL_HANDLER on
        // the given node. Stash the tooltip in the node's client properties
        // map so that it is not gc'd. The handlers must all be installed
        // with a TODO weak reference so as not to cause a memory leak
        if (node == null) return;
        node.addEventHandler(MouseEvent.MOUSE_MOVED, MOVE_HANDLER);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, LEAVING_HANDLER);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, KILL_HANDLER);
        t.textProperty().addListener(EMPTY_TOOLTIP_HANDLER);
        node.getProperties().put(TOOLTIP_PROP_KEY, t);
    }


    /**
     * Gets the top level window associated with this node.
     *
     * @param node the node
     * @return the top level window
     */
    private Window getWindow(final Node node) {
        final Scene scene = node == null ? null : node.getScene();
        return scene == null ? null : scene.getWindow();
    }

    /**
     * Gets whether the entire window hierarchy is visible for this node.
     *
     * @param node the node to check
     * @return true if entire hierarchy is visible
     */
    private boolean isWindowHierarchyVisible(Node node) {
        boolean treeVisible = node != null;
        Parent parent = node == null ? null : node.getParent();
        while (parent != null && treeVisible) {
            treeVisible = parent.isVisible();
            parent = parent.getParent();
        }
        return treeVisible;
    }
}

