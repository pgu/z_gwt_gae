package pgu.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class PlanningCell extends Composite {

    FlowPanel panel;

    public PlanningCell(final int row, final int col, final ToolbarPlanning toolbar) {
        panel = new FlowPanel();
        final Element element = panel.getElement();
        final Style style = element.getStyle();

        style.setHeight(30, Unit.PX);
        style.setWidth(60, Unit.PX);
        style.setOverflow(Overflow.HIDDEN);
        style.setBackgroundColor("white");
        if (col % 2 == 0) {
            style.setProperty("borderRight", "1px solid blue");
        } else {
            style.setProperty("borderRight", "1px dotted blue");
        }
        element.setId("p_" + row + col);

        if (0 != row && row % 2 == 0) {
            final MouseOutHandler exitCell = new MouseOutHandler() {

                @Override
                public void onMouseOut(final MouseOutEvent event) {
                    event.preventDefault();
                    if ("yellow".equals(style.getBackgroundColor())) {
                        style.setBackgroundColor("white");
                    }
                }

            };
            panel.addDomHandler(exitCell, MouseOutEvent.getType());

            final MouseOverHandler enterCell = new MouseOverHandler() {

                @Override
                public void onMouseOver(final MouseOverEvent event) {
                    GWT.log("cell over");
                    event.preventDefault();
                    if ("white".equals(style.getBackgroundColor())) {
                        style.setBackgroundColor(toolbar.hadDuplicate() ? toolbar.getColor() : "yellow");
                    }
                }

            };
            panel.addDomHandler(enterCell, MouseOverEvent.getType());

            final MouseUpHandler upCell = new MouseUpHandler() {

                @Override
                public void onMouseUp(final MouseUpEvent event) {
                    GWT.log("cell up");
                }
            };
            panel.addDomHandler(upCell, MouseUpEvent.getType());
        }

        initWidget(panel);
    }

    public PlanningCell disable() {
        panel.getElement().getStyle().setBackgroundColor("grey");
        return this;
    }

    public void addActivityLine() {
        final FlowPanel activity = new FlowPanel();
        final Element element = activity.getElement();
        final Style style = element.getStyle();

        style.setHeight(4, Unit.PX);
        style.setWidth(100, Unit.PCT);
        style.setOverflow(Overflow.HIDDEN);
        style.setBackgroundColor("red");
        style.setMarginTop(12, Unit.PX);
        panel.add(activity);
    }

    public void addTask(final String bgColor) {
        final FlowPanel task = new FlowPanel();
        final Element element = task.getElement();
        final Style style = element.getStyle();

        // style.setHeight(100, Unit.PCT);
        // style.setWidth(100, Unit.PCT);
        style.setOverflow(Overflow.HIDDEN);
        style.setBackgroundColor(bgColor);
        panel.add(task);
    }

}
