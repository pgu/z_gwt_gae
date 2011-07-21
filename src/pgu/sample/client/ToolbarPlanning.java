package pgu.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarPlanning extends Composite {

    private final FlexTable toolbar = new FlexTable();
    private final FlowPanel colorDuplicate = new FlowPanel();
    private final Style styleDuplicate;

    boolean isMouseDown = false;

    public ToolbarPlanning() {

        int i = 0;
        addOption(i++, "Montage TG", "grey");
        addOption(i++, "Réimplantation", "black");
        addOption(i++, "Inventaire tournant", "blue");
        addOption(i++, "Audit de prix", "green");
        addOption(i++, "Pose des labels", "red");

        setDebug();

        styleDuplicate = colorDuplicate.getElement().getStyle();
        styleDuplicate.setHeight(0, Unit.PX);
        styleDuplicate.setWidth(0, Unit.PX);
        styleDuplicate.setPosition(Position.ABSOLUTE);
        styleDuplicate.setZIndex(1000);
        RootPanel.get().add(colorDuplicate);

        initWidget(toolbar);

    }

    Label mouseX = new Label();
    Label mouseY = new Label();
    Label duplicX = new Label();
    Label duplicY = new Label();
    Label resultX = new Label();
    Label resultY = new Label();

    private void setDebug() {
        final FlowPanel debug = new FlowPanel();

        final Style style = debug.getElement().getStyle();
        style.setTop(400, Unit.PX);
        style.setLeft(0, Unit.PX);
        style.setPosition(Position.ABSOLUTE);

        final HorizontalPanel hpMouse = new HorizontalPanel();
        hpMouse.add(new Label(" mouse : "));
        hpMouse.add(mouseX);
        hpMouse.add(mouseY);

        final HorizontalPanel hpDuplicate = new HorizontalPanel();
        hpDuplicate.add(new Label(" duplic : "));
        hpDuplicate.add(duplicX);
        hpDuplicate.add(duplicY);

        final HorizontalPanel hpResult = new HorizontalPanel();
        hpResult.add(new Label(" result : "));
        hpResult.add(resultX);
        hpResult.add(resultY);

        final VerticalPanel vp = new VerticalPanel();
        vp.add(hpMouse);
        vp.add(hpDuplicate);
        vp.add(hpResult);
        debug.add(vp);

        RootPanel.get().add(debug);
    }

    private void addOption(final int col, final String text, final String bgColor) {
        final Label label = new Label(text);
        final FlowPanel color = new FlowPanel();
        final Style style = color.getElement().getStyle();
        style.setHeight(20, Unit.PX);
        style.setWidth(20, Unit.PX);
        style.setBackgroundColor(bgColor);
        final VerticalPanel vp = new VerticalPanel();
        vp.add(color);
        vp.add(label);
        final Style styleVp = vp.getElement().getStyle();
        styleVp.setProperty("border", "1px solid grey");
        toolbar.setWidget(0, col, vp);

        // create a duplicate
        final MouseDownHandler mouseDownOnOption = new MouseDownHandler() {

            @Override
            public void onMouseDown(final MouseDownEvent event) {
                event.preventDefault();
                isMouseDown = true;

                RootPanel.get().addDomHandler(new MouseUpHandler() {

                    @Override
                    public void onMouseUp(final MouseUpEvent event) {
                        event.preventDefault();
                        isMouseDown = false;
                    }
                }, MouseUpEvent.getType());

                styleDuplicate.setHeight(20, Unit.PX);
                styleDuplicate.setWidth(20, Unit.PX);

                styleDuplicate.setBackgroundColor(bgColor);
                final int clientY = event.getClientY();
                styleDuplicate.setTop(clientY - 10, Unit.PX);
                final int clientX = event.getClientX();
                styleDuplicate.setLeft(clientX - 10, Unit.PX);

                styleDuplicate.setVisibility(Visibility.VISIBLE);

                // follow the mouse
                final MouseMoveHandler followMouse = new MouseMoveHandler() {

                    @Override
                    public void onMouseMove(final MouseMoveEvent event) {
                        event.preventDefault();
                        if (isMouseDown) {

                            final int curClientX = event.getClientX();
                            int duplicateX = Integer.parseInt(styleDuplicate.getLeft().replace("px", ""));

                            final boolean isFarFromMouseX = isFarFromMouse(curClientX, duplicateX);

                            if (isFarFromMouseX) {
                                styleDuplicate.setLeft(curClientX - 10, Unit.PX);
                                duplicateX = curClientX;
                            }

                            final int curClientY = event.getClientY();
                            int duplicateY = Integer.parseInt(styleDuplicate.getTop().replace("px", ""));

                            final boolean isFarFromMouseY = isFarFromMouse(curClientY, duplicateY);

                            if (isFarFromMouseY) {
                                styleDuplicate.setTop(curClientY - 10, Unit.PX);
                                duplicateY = curClientY;
                            }
                            mouseX.setText(" X: " + curClientX + ", ");
                            duplicX.setText(" X: " + duplicateX + ", ");
                            resultX.setText(" X: " + Math.abs(curClientX - duplicateX) + ", ");
                            resultX.getElement().getStyle().setColor(isFarFromMouseX ? "white" : "black");
                            resultX.getElement().getStyle().setBackgroundColor(isFarFromMouseX ? "green" : "white");
                            resultX.getElement().getStyle()
                                    .setFontWeight(isFarFromMouseX ? FontWeight.BOLD : FontWeight.NORMAL);

                            mouseY.setText(" Y: " + curClientY);
                            duplicY.setText(" Y: " + duplicateY);
                            resultY.setText(" Y: " + Math.abs(curClientY - duplicateY));
                            resultY.getElement().getStyle().setColor(isFarFromMouseY ? "white" : "black");
                            resultY.getElement().getStyle().setBackgroundColor(isFarFromMouseY ? "green" : "white");
                            resultY.getElement().getStyle()
                                    .setFontWeight(isFarFromMouseY ? FontWeight.BOLD : FontWeight.NORMAL);

                        } else {
                            styleDuplicate.setVisibility(Visibility.HIDDEN);
                        }
                    }

                    private boolean isFarFromMouse(final int curClient, final int client) {
                        return Math.abs(curClient - client) > 11 || Math.abs(curClient - client) < 3;
                    }

                };
                colorDuplicate.addDomHandler(followMouse, MouseMoveEvent.getType());

                // disappear on mouseup
                final MouseUpHandler disappearDuplicate = new MouseUpHandler() {

                    @Override
                    public void onMouseUp(final MouseUpEvent event) {
                        GWT.log("toolbar up");
                        event.preventDefault();
                        styleDuplicate.setVisibility(Visibility.HIDDEN);
                    }
                };
                colorDuplicate.addDomHandler(disappearDuplicate, MouseUpEvent.getType());

            }

        };
        vp.addDomHandler(mouseDownOnOption, MouseDownEvent.getType());

    }

    public boolean hadDuplicate() {
        final boolean hadDuplicate = "visible".equals(styleDuplicate.getVisibility().toLowerCase());
        styleDuplicate.setVisibility(Visibility.HIDDEN);
        return hadDuplicate;
    }

    public String getColor() {
        return styleDuplicate.getBackgroundColor();
    }
}
