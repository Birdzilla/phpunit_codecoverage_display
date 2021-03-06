package cc.takacs.php_codeverage_display.config;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Zsolt Takacs <zsolt@takacs.cc>
 */
public class ConfigPanel {
    public JPanel panel;
    private JButton browseCloverXmlButton;
    public JTextField cloverLocation;
    public JPanel coveredColor;
    public JPanel uncoveredColor;
    public JCheckBox lineCheckBox;
    public JCheckBox sideCheckBox;
    public JTextField remoteDir;
    public JTextField localDir;
    public JButton browseLocalDir;
    public JCheckBox dirTranslation;
    public JCheckBox useCoverageSuite;
    // checkbox used to enable & disable error highlighting
    // which usually looks like errors
    public JCheckBox errorCheckBox;
    public JCheckBox useColorScheme;
    private JLabel coveredColorLabel;
    private JLabel uncoveredColorLabel;
    private PickerListener coveredColorListener = new PickerListener(panel, coveredColor);
    private PickerListener uncoveredColorListener = new PickerListener(panel, uncoveredColor);

    public ConfigPanel() {
        browseCloverXmlButton.addActionListener(
                new BrowseListener(this.cloverLocation, FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor())
        );
        browseLocalDir.addActionListener(
                new BrowseListener(this.localDir, FileChooserDescriptorFactory.createSingleFolderDescriptor())
        );

        setColorListeners(true);
        dirTranslation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                localDir.setEnabled(dirTranslation.isSelected());
                remoteDir.setEnabled(dirTranslation.isSelected());
                browseLocalDir.setEnabled(dirTranslation.isSelected());
            }
        });
        useColorScheme.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isNotSelected = e.getStateChange() != ItemEvent.SELECTED;
                coveredColorLabel.setEnabled(isNotSelected);
                uncoveredColorLabel.setEnabled(isNotSelected);
                setColorListeners(isNotSelected);
            }
        });
    }

    private void setColorListeners(boolean enabled) {
        if (enabled) {
            coveredColor.addMouseListener(coveredColorListener);
            uncoveredColor.addMouseListener(uncoveredColorListener);
        } else {
            coveredColor.removeMouseListener(coveredColorListener);
            uncoveredColor.removeMouseListener(uncoveredColorListener);
        }
    }

    private class PickerListener implements MouseListener {
        private JPanel panel;
        private JPanel picker;

        public PickerListener(JPanel panel, JPanel picker) {
            this.panel = panel;
            this.picker = picker;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            Color picked = JColorChooser.showDialog(panel, "", picker.getBackground());

            if (picked != null) {
                picker.setBackground(picked);
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
        }
    }

    private class BrowseListener implements ActionListener {
        JTextField field;
        FileChooserDescriptor descriptor;

        public BrowseListener(JTextField field, FileChooserDescriptor descriptor) {
            this.field = field;
            this.descriptor = descriptor;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            VirtualFile[] files = FileChooser.chooseFiles(descriptor, null, null);

            if (files.length > 0) {
                field.setText(files[0].getPath());
            }
        }
    }
}