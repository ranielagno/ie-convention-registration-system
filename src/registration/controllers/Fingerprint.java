package registration.controllers;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Fingerprint implements Initializable{

    @FXML
    private JFXButton submit;

    @FXML
    private ImageView picture;

    @FXML
    private Label far;

    //For fingerprint scanner
    private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    public static String TEMPLATE_PROPERTY = "template";
    private DPFPTemplate template;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    DPFPFeatureSet features;

    @FXML
    void submitFingerprint(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/xml/closing.fxml"));
        loader.setController(new Closing());

        Parent root = loader.load();
        Stage stage = (Stage) submit.getScene().getWindow();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }

    public void start() {
        picture.setVisible(true);
        submit.setDisable(true);
        init();
        updateStatus();
        capturer.startCapture();
    }

    private void init() {
        capturer.addDataListener(new DPFPDataListener() {
            @Override
            public void dataAcquired(DPFPDataEvent dpfpDataEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        drawPicture(convertSampleToBitmap(dpfpDataEvent.getSample()));
                        process(dpfpDataEvent.getSample());
                    }
                });

            }
        });

        capturer.addReaderStatusListener(new DPFPReaderStatusListener() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent dpfpReaderStatusEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Status: The fingerprint reader was connected.");
                    }
                });
            }

            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent dpfpReaderStatusEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Status: The fingerprint reader was disconnected.");
                    }
                });

            }

        });

        capturer.addSensorListener(new DPFPSensorAdapter() {
            @Override public void fingerTouched(final DPFPSensorEvent e) {
                Platform.runLater(new Runnable() {	public void run() {
                    System.out.println("Status: The fingerprint reader was touched.");
                }});
            }
            @Override public void fingerGone(final DPFPSensorEvent e) {
                Platform.runLater(new Runnable() {	public void run() {
                    System.out.println("Status: The finger was removed from the fingerprint reader.");
                }});
            }
        });
        capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
            @Override public void onImageQuality(final DPFPImageQualityEvent e) {
                Platform.runLater(new Runnable() {	public void run() {
                    if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
                        System.out.println("Status: The quality of the fingerprint sample is good.");
                    else
                        System.out.println("Status: The quality of the fingerprint sample is poor.");
                }});
            }
        });
    }

    protected void process(DPFPSample sample){

        features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

        // Check quality of the sample and add to enroller if it's good
        if (features != null) try
        {
            System.out.println("The fingerprint feature set was created. ");
            enroller.addFeatures(features);        // Add feature set to template.
        } catch (DPFPImageQualityException ex) {
        } finally {
            updateStatus();

            // Check if template has been created.
            switch (enroller.getTemplateStatus()) {
                case TEMPLATE_STATUS_READY:    // report success and stop capturing
                    setTemplate(enroller.getTemplate());
                    enroller.clear();
                    capturer.stopCapture();
                    picture.setVisible(false);
                    submit.setDisable(false);
                    JOptionPane.showMessageDialog(null, "Fingerprints Successfully Entered");
                    far.setText("Click Submit Button");
                    break;

                case TEMPLATE_STATUS_FAILED:    // report failure and restart capturing
                    enroller.clear();
                    capturer.stopCapture();
                    updateStatus();
                    setTemplate(null);
                    JOptionPane.showMessageDialog(null,
                            "The fingerprint template is not valid. Repeat fingerprint enrollment.",
                            "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
                    capturer.startCapture();
                    break;
            }

        }

    }

    private void drawPicture(BufferedImage image){
        Image i = SwingFXUtils.toFXImage(image, null);
        picture.setImage(i);
        picture.setFitHeight(350);
        picture.setFitWidth(300);

    }

    protected BufferedImage convertSampleToBitmap(DPFPSample sample){
        return (BufferedImage) DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }


    private DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose dataPurposeVerification) {

        DPFPFeatureExtraction extraction = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extraction.createFeatureSet(sample,dataPurposeVerification);
        } catch (DPFPImageQualityException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void updateStatus() {
        far.setText(String.format("Fingerprint samples needed: %1$s", enroller.getFeaturesNeeded()));
    }

    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        this.pcs.firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        start();
    }
}
