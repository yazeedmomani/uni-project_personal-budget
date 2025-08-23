package layout.menus;

import db.Database;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

import app.App;
import layout.LayoutController;

public class TopMenu {
    private static Button settingsBtn, logoutBtn, editBtn, viewBtn;
    private static HBox root;
    private static Label welcomeLbl;
    private static Region space;

    public static void setButton(String mode){
        if(mode == null) return;

        if(mode.equals("view")){
            root.getChildren().remove(viewBtn);
            root.getChildren().add(2, editBtn);
        }
        if(mode.equals("edit")){
            root.getChildren().remove(editBtn);
            root.getChildren().add(2, viewBtn);
        }

    }

    public static void selectSettings(boolean select){
        if(select) settingsBtn.setId("topMenu_selected");
        if(!select) settingsBtn.setId("");
    }

    public static HBox getRoot(){
        settingsBtn = createIconButton("M12 15.516q1.453 0 2.484-1.031t1.031-2.484-1.031-2.484-2.484-1.031-2.484 1.031-1.031 2.484 1.031 2.484 2.484 1.031zM19.453 12.984l2.109 1.641q0.328 0.234 0.094 0.656l-2.016 3.469q-0.188 0.328-0.609 0.188l-2.484-0.984q-0.984 0.703-1.688 0.984l-0.375 2.625q-0.094 0.422-0.469 0.422h-4.031q-0.375 0-0.469-0.422l-0.375-2.625q-0.891-0.375-1.688-0.984l-2.484 0.984q-0.422 0.141-0.609-0.188l-2.016-3.469q-0.234-0.422 0.094-0.656l2.109-1.641q-0.047-0.328-0.047-0.984t0.047-0.984l-2.109-1.641q-0.328-0.234-0.094-0.656l2.016-3.469q0.188-0.328 0.609-0.188l2.484 0.984q0.984-0.703 1.688-0.984l0.375-2.625q0.094-0.422 0.469-0.422h4.031q0.375 0 0.469 0.422l0.375 2.625q0.891 0.375 1.688 0.984l2.484-0.984q0.422-0.141 0.609 0.188l2.016 3.469q0.234 0.422-0.094 0.656l-2.109 1.641q0.047 0.328 0.047 0.984t-0.047 0.984z");
        logoutBtn = createIconButton("M10 22.5c0 0.438 0.203 1.5-0.5 1.5h-5c-2.484 0-4.5-2.016-4.5-4.5v-11c0-2.484 2.016-4.5 4.5-4.5h5c0.266 0 0.5 0.234 0.5 0.5 0 0.438 0.203 1.5-0.5 1.5h-5c-1.375 0-2.5 1.125-2.5 2.5v11c0 1.375 1.125 2.5 2.5 2.5h4.5c0.391 0 1-0.078 1 0.5zM24.5 14c0 0.266-0.109 0.516-0.297 0.703l-8.5 8.5c-0.187 0.187-0.438 0.297-0.703 0.297-0.547 0-1-0.453-1-1v-4.5h-7c-0.547 0-1-0.453-1-1v-6c0-0.547 0.453-1 1-1h7v-4.5c0-0.547 0.453-1 1-1 0.266 0 0.516 0.109 0.703 0.297l8.5 8.5c0.187 0.187 0.297 0.438 0.297 0.703z");
        editBtn = createIconButton("M20.719 7.031l-1.828 1.828-3.75-3.75 1.828-1.828q0.281-0.281 0.703-0.281t0.703 0.281l2.344 2.344q0.281 0.281 0.281 0.703t-0.281 0.703zM3 17.25l11.063-11.063 3.75 3.75-11.063 11.063h-3.75v-3.75z");
        viewBtn = createIconButton("M12 9q1.219 0 2.109 0.891t0.891 2.109-0.891 2.109-2.109 0.891-2.109-0.891-0.891-2.109 0.891-2.109 2.109-0.891zM12 17.016q2.063 0 3.539-1.477t1.477-3.539-1.477-3.539-3.539-1.477-3.539 1.477-1.477 3.539 1.477 3.539 3.539 1.477zM12 4.5q3.703 0 6.703 2.063t4.313 5.438q-1.313 3.375-4.313 5.438t-6.703 2.063-6.703-2.063-4.313-5.438q1.313-3.375 4.313-5.438t6.703-2.063z");
        space = createSpace();
        welcomeLbl = createWelcomeLabel();

        root = new HBox(welcomeLbl, space, settingsBtn, logoutBtn);
        root.getStyleClass().add("topMenu");

        return root;
    }

    private static Label createWelcomeLabel(){
        String welcomeText = "Welcome " + Database.getCurrentUser().getName();

        Label label = new Label(welcomeText);
        label.getStyleClass().add("topMenu_lbl");
        return label;
    }

    private static Region createSpace(){
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        return space;
    }

    private static Button createIconButton(String iconPath){
        Button button = new Button();
        SVGPath icon = createIcon(iconPath);
        button.setGraphic(icon);
        button.getStyleClass().add("topMenu_btn");
        button.setOnAction(TopMenu::handleEvent);
        return button;
    }

    private static SVGPath createIcon(String path){
        SVGPath icon = new SVGPath();
        icon.setContent(path);
        icon.getStyleClass().add("topMenu_btn_icon");
        return icon;
    }

    private static void handleEvent(ActionEvent e){
        Object source = e.getSource();

        if (source == editBtn) LayoutController.setCurrentMode("edit");
        if (source == viewBtn) LayoutController.setCurrentMode("view");
        if (source == logoutBtn) App.logout();
        if (source == settingsBtn) LayoutController.setViewSettings(!LayoutController.getViewSettings());
    }
}
