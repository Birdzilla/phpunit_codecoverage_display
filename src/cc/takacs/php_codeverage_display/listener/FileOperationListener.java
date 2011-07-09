package cc.takacs.php_codeverage_display.listener;

import cc.takacs.php_codeverage_display.display.DisplayHandler;
import com.intellij.openapi.vfs.*;

import java.io.File;

/**
 * @author Zsolt Takacs <zsolt@takacs.cc>
 */
public class FileOperationListener implements VirtualFileListener {
    private DisplayHandler displayHandler;

    public FileOperationListener(DisplayHandler displayHandler) {
        this.displayHandler = displayHandler;
    }

    public void beforePropertyChange(VirtualFilePropertyEvent event) {
        if (event.getPropertyName().equals("name")) {
            String newPath = event.getFile().getParent().getPath() + File.separator + event.getNewValue();
            displayHandler.reassignDisplay(event.getFile().getPath(), newPath);
        }
    }

    public void beforeFileMovement(VirtualFileMoveEvent event) {
        String newPath = event.getNewParent().getPath() + File.separator + event.getFileName();
        displayHandler.reassignDisplay(event.getFile().getPath(), newPath);
    }

    public void fileMoved(VirtualFileMoveEvent event) {
    }

    public void propertyChanged(VirtualFilePropertyEvent event) {
    }

    public void contentsChanged(VirtualFileEvent event) {
    }

    public void fileCreated(VirtualFileEvent event) {
    }

    public void fileDeleted(VirtualFileEvent event) {
    }

    public void fileCopied(VirtualFileCopyEvent event) {
    }

    public void beforeContentsChange(VirtualFileEvent event) {
    }

    public void beforeFileDeletion(VirtualFileEvent event) {
    }
}
