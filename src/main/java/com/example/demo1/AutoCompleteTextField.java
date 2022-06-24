package com.example.demo1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;

import java.lang.invoke.LambdaConversionException;
import java.util.*;

public class AutoCompleteTextField {
    /** The existing autocomplete entries. */
    private final SortedSet<String> entries;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    private Requete requete;
    private Button btn;

    private TextField textField;

    /**
     * sets the request to update.
     * @param requete the request to update
     */
    public void setRequete(Requete requete) {
        this.requete = requete;
    }

    /**
     * Set the 'recherche' button to turn it on/off when the text field contains a valid/invalid name
     * @param btn the button to set
     */
    public void setBtn(Button btn) {
        this.btn = btn;
    }

    /** Construct a new AutoCompleteTextField. */
    public AutoCompleteTextField(TextField textField) {

        entries = new TreeSet<>();
        entries.add("Paris");
        this.textField = textField;
        entriesPopup = new ContextMenu();
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValrechercherue, String s, String s2) {
                if (textField.getText().length() == 0)
                {
                    if (btn != null)
                        btn.setDisable(true);
                    entriesPopup.hide();
                } else
                {
                    LinkedList<String> searchResult = new LinkedList<>();
                    if (requete != null) {
                        String[] similar = requete.setScientific_name(textField.getText());
                    searchResult.addAll(Arrays.asList(similar));
                        //check if text is equal to one of the similar names*
                        boolean exactMatch = false;
                        for (String s1 : similar) {
                            if (s1 != null && s1.equals(textField.getText())) {
                                exactMatch = true;
                                break;
                            }
                        }

                        if (exactMatch) { if (btn != null) btn.setDisable(false); } else { if (btn != null) btn.setDisable(true); }
                    }
                    if (entries.size() > 0)
                    {
                        while (searchResult.remove(null));
                        populatePopup(searchResult);
                        if (!entriesPopup.isShowing())
                        {
                            entriesPopup.show(textField, Side.BOTTOM, 0, 0);
                        }
                    } else
                    {
                        entriesPopup.hide();
                    }
                }
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() { return entries; }



    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent) {
                    textField.setText(result);
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }
}
