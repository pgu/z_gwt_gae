package pgu.sample.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pgu.sample.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * automatiquement place dans une case:
 * http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#InsertPanelExample
 * http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#GridConstrainedExample
 * http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#PuzzleExample
 * 
 * automatiquement deplace les autres cases:
 * http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#FlowPanelExample
 * 
 * list2list: http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#DualListExample
 * 
 * toolbar selection + dnd:
 * http://allen-sauer.com/com.allen_sauer.gwt.dnd.demo.DragDropDemo/DragDropDemo.html#PaletteExample
 * 
 * 
 */
public class Z_gwt_gae implements EntryPoint {
    /**
     * The message displayed to the user when the server cannot be reached or returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network " + "connection and try again.";
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    private static final int COL_NAMES = 0;
    private static final int ROW_HOURS = 0;
    private static final int ROW_PERSONS_TITLE = 0;
    private final FlexTable tableHours = new FlexTable();
    private final FlexCellFormatter cellFormatterHours = tableHours.getFlexCellFormatter();

    @Override
    public void onModuleLoad() {

        // ////////////////////////////////////////////////////////////////
        // table de gauche avec les noms de personnes
        // ////////////////////////////////////////////////////////////////
        final FlexTable tablePersons = new FlexTable();
        final FlexCellFormatter cellFormatterPersons = tablePersons.getFlexCellFormatter();
        tablePersons.setCellSpacing(0);
        tablePersons.setCellPadding(0);

        tablePersons.setHTML(ROW_PERSONS_TITLE, COL_NAMES,
                "<div style=\"height:30px;padding-top:10px;overflow:hidden;font-weight:bold;\">Persons</div>");
        final List<Person> persons = Arrays.asList(new Person("John Isner", 5, 12), //
                new Person("René Char", 4, 12), //
                new Person("Jacques Lelièvre", 7, 13), //
                new Person("Nicolas Mahut", 6, 13));

        int row = ROW_PERSONS_TITLE + 1;
        for (final Person person : persons) {
            tablePersons.setHTML(row, COL_NAMES, "<div style=\"height:30px;overflow:hidden;\">" + person.getName()
                    + "</div>");
            tablePersons.setHTML(row + 1, COL_NAMES, "<div style=\"height:30px;overflow:hidden;\">&nbsp;</div>");

            // cellFormatterPersons.setRowSpan(row, COL_NAMES, 2);
            cellFormatterPersons.setVerticalAlignment(row, COL_NAMES, HasVerticalAlignment.ALIGN_BOTTOM);
            row += 2;
        }

        // ////////////////////////////////////////////////////////////////
        // table centrale, le planning
        // ////////////////////////////////////////////////////////////////
        tableHours.setCellSpacing(0);
        tableHours.setCellPadding(0);

        final List<Integer> hours_0_23 = new ArrayList<Integer>();
        for (int i = 0; i < 24; i++) {
            hours_0_23.add(i);
        }

        final int nb_col = 2 * hours_0_23.size(); // 48
        // build day table
        for (int col = 0; col < nb_col; col++) { // 0 - 48

            // //////////////////////////////////////////////////////////////
            if (col < 24) {
                tableHours.setWidget(ROW_HOURS, col, new HourHeader(col));
                cellFormatterHours.setHorizontalAlignment(ROW_HOURS, col, HasHorizontalAlignment.ALIGN_CENTER);
                // vertical alignment ne fonctionne pas
                cellFormatterHours.setColSpan(ROW_HOURS, col, 2);
            }

            // //////////////////////////////////////////////////////////////
            // first row: the activity line
            // second row: the tasks line

            for (int personIndex = ROW_HOURS + 1; personIndex < persons.size() * 2; personIndex += 2) {

                final int row1 = personIndex;
                final int row2 = personIndex + 1;

                if (col == 0) { // colonne before 0h00
                    tableHours.setWidget(row1, 0, new PlanningCell(row1, 0).disable());
                    tableHours.setWidget(row2, 0, new PlanningCell(row2, 0).disable());
                } else {
                    tableHours.setWidget(row1, col, new PlanningCell(row1, col));
                    tableHours.setWidget(row2, col, new PlanningCell(row2, col));
                }
            }
        }

        // build activity for each person
        int personIdx = 0;
        for (int personRowIndex = ROW_HOURS + 1; personRowIndex < persons.size() * 2; personRowIndex += 2) {
            final Person person = persons.get(personIdx++);
            final int col_start = (int) (person.getStart() * 2);
            final int col_end = (int) (person.getEnd() * 2);
            for (int col = col_start; col < col_end; col++) {
                ((PlanningCell) tableHours.getWidget(personRowIndex, col)).addActivityLine();
            }

        }

        // ////////////////////////////////////////////////////////////////
        // container
        // ////////////////////////////////////////////////////////////////
        final HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);

        final ScrollPanel scrollPanelDesigns = new ScrollPanel();
        tablePersons.setWidth("100%");
        scrollPanelDesigns.add(tablePersons);
        scrollPanelDesigns.setWidth("100px");
        hp.add(scrollPanelDesigns);

        final ScrollPanel scrollPanelDays = new ScrollPanel();
        tableHours.setWidth("100%");
        scrollPanelDays.add(tableHours);
        scrollPanelDays.setWidth("600px");
        hp.add(scrollPanelDays);

        RootPanel.get().add(hp);
        DOM.scrollIntoView(tableHours.getWidget(ROW_HOURS, 10).getElement());
    }

    public void onModuleLoadOld() {
        final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        nameField.setText("GWT User");
        final Label errorLabel = new Label();

        // We can add style names to widgets
        sendButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("nameFieldContainer").add(nameField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);

        // Focus the cursor on the name field when the app loads
        nameField.setFocus(true);
        nameField.selectAll();

        // Create the popup dialog box
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button("Close");
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        final VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        // Add a handler to close the DialogBox
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                sendButton.setFocus(true);
            }
        });

        // Create a handler for the sendButton and nameField
        class MyHandler implements ClickHandler, KeyUpHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            @Override
            public void onClick(final ClickEvent event) {
                sendNameToServer();
            }

            /**
             * Fired when the user types in the nameField.
             */
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendNameToServer();
                }
            }

            /**
             * Send the name from the nameField to the server and wait for a response.
             */
            private void sendNameToServer() {
                // First, we validate the input.
                errorLabel.setText("");
                final String textToServer = nameField.getText();
                if (!FieldVerifier.isValidName(textToServer)) {
                    errorLabel.setText("Please enter at least four characters");
                    return;
                }

                // Then, we send the input to the server.
                sendButton.setEnabled(false);
                textToServerLabel.setText(textToServer);
                serverResponseLabel.setText("");
                greetingService.greetServer(textToServer, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(final Throwable caught) {
                        // Show the RPC error message to the user
                        dialogBox.setText("Remote Procedure Call - Failure");
                        serverResponseLabel.addStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(SERVER_ERROR);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }

                    @Override
                    public void onSuccess(final String result) {
                        dialogBox.setText("Remote Procedure Call");
                        serverResponseLabel.removeStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(result);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }
                });
            }
        }

        // Add a handler to send the name to the server
        final MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler);
        nameField.addKeyUpHandler(handler);
    }
}
