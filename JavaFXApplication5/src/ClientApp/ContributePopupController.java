/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Utilities.FriendWishInfo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author randa
 */
public class ContributePopupController implements Initializable {

    @FXML
    private Label wishNameLabel;

    @FXML
    private Label remainingAmountLabel;

    @FXML
    private TextField amountField;

    @FXML
    private Label errorLabel;

    private FriendWishInfo wish;
    private Runnable onContributeCallback;

    public TextField getAmountField() {
        return amountField;
    }

    // Set the wish data and update the UI
    public void setWish(FriendWishInfo wish) {
        this.wish = wish;
        wishNameLabel.setText(wish.getName());
        double remainingAmount = wish.getPrice() - wish.getCollected();
        remainingAmountLabel.setText(String.format("Remaining Amount: $%.2f", remainingAmount));
    }

    public void setOnContributeCallback(Runnable callback) {
        this.onContributeCallback = callback;
    }

    @FXML
    private void handleContribute() {
        String amountText = amountField.getText();
        try {
            double contributionAmount = Double.parseDouble(amountText);
            if (contributionAmount <= 0) {
                errorLabel.setText("Invalid amount! Please enter a positive number.");
                return;
            }

            double remainingAmount = wish.getPrice() - wish.getCollected();
            if (contributionAmount > remainingAmount) {
                errorLabel.setText("Amount exceeds the remaining amount.");
                return;
            }

            // Close the window
            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.close();

            // Execute the callback to handle the contribution
            if (onContributeCallback != null) {
                onContributeCallback.run();
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter a valid number.");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}