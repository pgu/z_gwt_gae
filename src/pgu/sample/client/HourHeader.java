package pgu.sample.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class HourHeader extends Composite {

    FlowPanel panel;

    public HourHeader(final int col) {
        panel = new FlowPanel();
        final Element element = panel.getElement();
        final Style style = element.getStyle();
        // height:30px;width:40px;overflow:hidden;background-color:grey;border-left:1px solid silver;border-bottom:1px
        // solid silver;font-weight:bold;
        style.setHeight(30, Unit.PX);
        // style.setWidth(120, Unit.PX);
        style.setWidth(100, Unit.PCT);
        style.setOverflow(Overflow.HIDDEN);
        style.setBackgroundColor("blue");
        style.setFontWeight(FontWeight.BOLD);
        style.setColor("white");
        style.setPaddingTop(10, Unit.PX);
        final int hour = col;
        element.setInnerText(hour + "h00");
        element.setId("hour_" + hour);

        initWidget(panel);
    }
}
