package ch.makery.address.view;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ch.makery.address.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;




public class GameOverviewController {

    //Magic Square Variable
    @FXML
    private Label Generation;
    @FXML
    private Label Error;
    @FXML
    private RadioButton Yes;//是否有限制
    @FXML
    private RadioButton  No;
    @FXML
    private Slider slider;
    @FXML
    private CheckBox Try_Big_ones;
    @FXML
    private Label Dimension;//几阶
    @FXML
    private Label MagicSum;

    //数独变量
    @FXML
    private Label S_Generation;
    @FXML
    private Label S_Error;
    @FXML
    private Label Puzzle;// example: 考虑到3阶有不同的题,题号
    @FXML
    private Slider S_slider;
    @FXML
    private Label S_Dimension;//几阶



    ToggleGroup Yes_No=new ToggleGroup();

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public GameOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        //初始化数独
        // Initialize the  table
        //setMin和setMax分别定义滑块上的最小值和最大值。
        //setValue方法设置滑块的当前值。并且当前值应该小于最大值和大于最小值。
        slider.setMin(0);
        slider.setMax(20);
        slider.setValue(5);
        //setShowTickMarks和setShowTickLabels定义滑块的视觉外观。并启用标记和标签。
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        //主要刻度标记之间的单位距离通过setMajorTickUnit方法设置为50。
        slider.setMajorTickUnit(5);
        //通过setMinorTickCount方法将任意两个主刻度之间的次刻度数指定为5。
        slider.setMinorTickCount(4);
        //setBlockIncrement方法定义当用户点击轨道时拇指移动的距离。
        slider.setBlockIncrement(1);
        //listener
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                Dimension.setText(String.valueOf(new_val));
            }
        });

        //setting Yes and No
        Yes.setToggleGroup(Yes_No);
        Yes.setUserData(true);
        Yes.setSelected(true);
        No.setToggleGroup(Yes_No);
        No.setUserData(false);
        Yes_No.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (Yes_No.getSelectedToggle() != null) {
                    System.out.println(Yes_No.getSelectedToggle().getUserData().toString());
                }
            }
        });

        //数独
        S_slider.setMin(3);
        S_slider.setMax(5);
        S_slider.setValue(3);
        S_slider.setShowTickLabels(true);
        S_slider.setShowTickMarks(true);
        S_slider.setSnapToTicks(true);
        //主要刻度标记之间的单位距离通过setMajorTickUnit方法设置为50。
        S_slider.setMajorTickUnit(1);
        S_slider.setMinorTickCount(0);
        //setBlockIncrement方法定义当用户点击轨道时拇指移动的距离。
        S_slider.setBlockIncrement(1);
        //listener
        S_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                S_Dimension.setText(String.valueOf(new_val));
            }
        });





    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }

}
