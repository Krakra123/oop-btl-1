package app.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.NavigableMap;

import app.controller.BookCollectionListController;
import app.controller.BookDetailsDisplayController;
import app.controller.BookItemDisplayController;
import app.controller.BookItemGroupDisplayController;
import app.data.Book;
import app.data.BookCollection;
import app.interfaces.ICallback;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

@SuppressWarnings({"exports", "FieldMayBeFinal"})
public class BookCollectionHandler {

    public static enum GroupByType {
        NONE,
        TITLE,
        AUTHOR,
        CATEGORY
    }

    public static enum SortByType {
        ASCENDING,
        DESCENDING
    }

    private static final String BOOK_COLLECTION_LIST_FXML = "BookCollectionList";
    private static final String BOOK_ITEM_GROUP_DISPLAY_FXML = "BookItemGroupDisplay";
    private static final String BOOK_ITEM_DISPLAY_FXML = "BookItemDisplay";
    private static final String BOOK_DETAILS_DISPLAY_FXML = "BookDetailsDisplay";
    
    private LoadableFXMLContent bookCollectionListPaneFXMLContent;
    public LoadableFXMLContent getBookCollectionListPaneFXMLContent() {
        return bookCollectionListPaneFXMLContent;
    }
    private BookCollectionListController bookCollectionListController;

    private LoadableFXMLContent bookDetailsDisplayFXMLContent;
    public LoadableFXMLContent getBookDetailsDisplayFXMLContent() {
        return bookDetailsDisplayFXMLContent;
    }
    private BookDetailsDisplayController bookDetailsDisplayController;

    public BookCollectionListController getListController() {
        return bookCollectionListController;
    }
    public BookDetailsDisplayController getDetailsController() {
        return bookDetailsDisplayController;
    }

    private BookCollection bookCollectionData;

    private List<LoadableFXMLContent> preBookGroupsContent = new ArrayList<>();

    private List<BookItemGroupDisplayController> curGrControllers;
    @SuppressWarnings("unused")
    public BookCollectionHandler(boolean editable) {
        bookCollectionListPaneFXMLContent = new LoadableFXMLContent(BOOK_COLLECTION_LIST_FXML);
        bookCollectionListController = bookCollectionListPaneFXMLContent.getData().getController(BookCollectionListController.class);

        bookDetailsDisplayFXMLContent = new LoadableFXMLContent(BOOK_DETAILS_DISPLAY_FXML);
        bookDetailsDisplayController = bookDetailsDisplayFXMLContent.getData().getController(BookDetailsDisplayController.class);
        bookDetailsDisplayController.reset(editable);

        bookDetailsDisplayFXMLContent.openOn(bookCollectionListController.detailsPane);
        bookDetailsDisplayFXMLContent.stickToWholeAnchorPane();

        bookCollectionListPaneFXMLContent.setEnableCallback(() -> { onEnable(); });
        
        curGrControllers = new ArrayList<>();
        AppManager.getInstance().getStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            adjustWidth();
        });
        AppManager.getInstance().getStage().iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            adjustWidth();
        });
        AppManager.getInstance().getStage().maximizedProperty().addListener((observable, oldValue, newValue) -> {
            adjustWidth();
        });
    }

    private void adjustWidth() {
        for (BookItemGroupDisplayController controller : curGrControllers) {
            controller.contentPane.setPrefWrapLength(AppManager.getInstance().getRootPane().getWidth() - 570);
            controller.contentPane.setPrefWidth(AppManager.getInstance().getRootPane().getWidth() - 570);
        }
    }

    public void update(BookCollection collection, GroupByType groupBy, SortByType sortBy) {
        bookCollectionData = collection;
        clear();
        updateCollectionDisplaying(collection.getBookGroups(groupBy), sortBy);
    }

    private void updateCollectionDisplaying(TreeMap<String, List<Book>> bookGroups, SortByType sort) {
        curGrControllers.clear();

        NavigableMap<String, List<Book>> neviMap;
        if (sort == SortByType.DESCENDING) neviMap = bookGroups.descendingMap();
        else neviMap = bookGroups;

        int index = 0;
        for (Map.Entry<String, List<Book>> bookGroup : neviMap.entrySet()) {
            if (index >= preBookGroupsContent.size()) {
                LoadableFXMLContent temp = new LoadableFXMLContent(BOOK_ITEM_GROUP_DISPLAY_FXML);
                preBookGroupsContent.add(temp);
                
                temp.setEnable();
                temp.stickToHorizontalAnchorPane();
            }

            try {
                LoadableFXMLContent content = preBookGroupsContent.get(index);
                BookItemGroupDisplayController controller = content.getData().getController(BookItemGroupDisplayController.class);

                curGrControllers.add(controller);

                bookCollectionListController.listPane.getChildren().add(content.getData().root);
                content.setPane(bookCollectionListController.listPane);
                content.setPane(bookCollectionListController.listPane);
                controller.groupLabel.setText(bookGroup.getKey().toUpperCase());
                
                adjustWidth();
                placeBookListItemOnGrid(bookGroup.getValue(), controller.contentPane);

            } catch (Exception e) {

            }

            index++;
        }
    }

    private void placeBookListItemOnGrid(List<Book> bookList, FlowPane pane) {
        pane.getChildren().clear();
        for (Book book : bookList) {
            try {
                LoadableFXMLContent bookItemDisplayFXMLContent = new LoadableFXMLContent(BOOK_ITEM_DISPLAY_FXML);
                BookItemDisplayController bookItemDisplayController = bookItemDisplayFXMLContent.getData().getController(BookItemDisplayController.class);

                pane.getChildren().add(bookItemDisplayFXMLContent.getData().root);
                bookItemDisplayFXMLContent.setEnable();
                bookItemDisplayController.update(book);
                bookItemDisplayController.setManager(this);
            } catch (Exception e) {

            }
        }
    }

    public void viewDetails(Book book) {
        if (!bookCollectionData.contains(book)) {
            System.err.println("There's no such book to view details");
        }

        bookDetailsDisplayController.update(book);
    }

    public Book getCurrentViewingBook() {
        return bookDetailsDisplayController.data;
    }

    private ICallback saveCallback;
    public void raiseSaveCallback() {
        if (saveCallback != null) {
            saveCallback.Call();
        }
    }
    public void setSaveCallback(ICallback callback) {
        saveCallback = callback;
    }

    private ICallback unsaveCallback;
    public void raiseUnSaveCallback() {
        if (unsaveCallback != null) {
            unsaveCallback.Call();
        }
    }
    public void setUnSaveCallback(ICallback callback) {
        unsaveCallback = callback;
    }

    private ICallback editCallback;
    public void raiseEditCallback() {
        if (editCallback != null) {
            editCallback.Call();
        }
    }
    public void setEditCallback(ICallback callback) {
        editCallback = callback;
    }

    private ICallback openCallback;
    public void raiseOpenCallback() {
        if (openCallback != null) {
            openCallback.Call();
        }
    }
    public void setOpenCallback(ICallback callback) {
        openCallback = callback;
    }

    public void lock() {
        bookCollectionListPaneFXMLContent.getData().root.setDisable(true);
        bookDetailsDisplayFXMLContent.getData().root.setDisable(true);
    }

    public void unlock() {
        bookCollectionListPaneFXMLContent.getData().root.setDisable(false);
        bookDetailsDisplayFXMLContent.getData().root.setDisable(false);
    }

    public void openOn(AnchorPane pane) {
        bookCollectionListPaneFXMLContent.openOn(pane);
    }

    public void hide() {
        bookCollectionListPaneFXMLContent.hide();
    }

    public void clear() {
        bookCollectionListController.listPane.getChildren().clear();
    }

    private void onEnable() {
        bookCollectionListPaneFXMLContent.stickToWholeAnchorPane();
        bookDetailsDisplayController.setManager(this);
    }
}
