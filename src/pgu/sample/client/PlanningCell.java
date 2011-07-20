package pgu.sample.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class PlanningCell extends Composite {

    FlowPanel panel;

    public PlanningCell(final int row, final int col) {
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

}
