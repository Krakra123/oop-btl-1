package app.managers;

import app.controller.UserLibraryUIController;
import app.data.BookCollection;
import app.managers.BookCollectionHandler.GroupByType;

@SuppressWarnings({"FieldMayBeFinal"})
public class UserLibraryManager extends BaseManager {
    
    private static final String USER_LIBRARY_UI = "UserLibraryUI";
    private static final int BOOK_NUMBER_DISPLAY_PER_ROW = 5;

    private LoadableFXMLContent userLibraryFXMLContent;
    public LoadableFXMLContent getUserLibraryFXMLContent() {
        return userLibraryFXMLContent;
    }
    private UserLibraryUIController userLibraryUIController;

    private BookCollectionHandler bookCollectionDisplay;

    public UserLibraryManager(AppManager manager) {
        super(manager);

        userLibraryFXMLContent = new LoadableFXMLContent(USER_LIBRARY_UI);
        userLibraryUIController = userLibraryFXMLContent.getData().getController(UserLibraryUIController.class);
        
        bookCollectionDisplay = new BookCollectionHandler();

        userLibraryFXMLContent.setEnableCallback(() -> { onEnable(); });
        userLibraryFXMLContent.setDisableCallback(() -> { onDisable(); });
    }

    public void updateBookCollectionDisplay(BookCollection collection, GroupByType groupBy) {
        bookCollectionDisplay.update(collection, groupBy);
    }

    private void onEnable() {
        bookCollectionDisplay.openOn(userLibraryUIController.bookListPane);
    }

    private void onDisable() {
        bookCollectionDisplay.hide();
    }
}
