package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

public class Controller {
    @FXML
    ImageView imgImage, imgImage2;
    @FXML
    Slider size= new Slider();
    @FXML
    Button load= new Button();
    @FXML
    Button exit= new Button();
    @FXML
    Button greyscale= new Button();
    @FXML
    Button details= new Button();
    @FXML
    Button countCells= new Button();
    @FXML
    AnchorPane anchor= new AnchorPane();
    @FXML
    Button rectangles= new Button();
    @FXML
    Label reds= new Label();
    @FXML
    Label whites= new Label();
    @FXML
    Label total= new Label();
    public static UnionFind unionFind= new UnionFind();
    public static Image image;
    public static WritableImage image2;
    public HashSet<Integer>totalWhite= new HashSet<>();
    public HashSet<Integer>totalRed= new HashSet<>();
    public HashSet<Integer>noiseReductionSet=new HashSet<>();
    ColorAdjust colorAdjust= new ColorAdjust();
    private int redCells;
    private int whiteCells;
    private int totalCells;
    int[] noiseReduction;
    int[] red;
    int[] white;


        public Image uploadImage(ActionEvent event) {
            FileChooser fc = new FileChooser(); //fileChooser is instantiated.
            Window ownerWindow = null; //window ownerWindow is set to null.
            File selectedFile = fc.showOpenDialog(ownerWindow);
            //the file selectedFile is equal to fileChooser's showOpenDialog with ownerWindow passed as a parameter.

            if (selectedFile != null) { //if the selected file isn't null..
                System.out.println(selectedFile.toURI().toString());
                //selected file's path is printed to the console as a string.
                image = new Image(selectedFile.toURI().toString(), 400, 400, false, true);
               //new image is instantiated and given this url, and a height + width of 400.
                imgImage.setImage(image);
                //new image is set in the imageView imgImage.

            } else { //if selectedFile is null..
                System.out.println("Not a valid file."); //print to console that selection is invalid.
            }
            return image; //image is returned.
        }


        public WritableImage tricolor(){ //method to perform tricolour conversion of cell image
            PixelReader pixelReader = image.getPixelReader(); //new pixelReader is set up to read pixels of uploaded cell image
            image2 = new WritableImage( //image2 is set as a new WritableImage
                    (int) image.getWidth(), //same width as image
                    (int) image.getHeight()); //same height as image
            System.out.println("height:" +(int) image.getHeight() );
            System.out.println("width:" +(int) image.getWidth() );
            PixelWriter pixelWriter = image2.getPixelWriter(); //new pixelWriter is set up to write to the pixels of image2
            for (int y = 0; y < image2.getHeight(); y++) { //for int y=0, y is less than image2's height, where y increments..
                for (int x = 0; x < image2.getWidth(); x++) { //nested for loop where x=0, less than image2's width, and increments..
                    Color color = pixelReader.getColor(x, y); //new color instantiated to equal pixelReader's colours.
                    if(color.getBlue()>color.getRed()) //if the colours of the original image's blue value is greater than their red value..
                    pixelWriter.setColor(x, y, Color.PURPLE); //set this to purple in image2.
                    else if((color.getRed()*255)>150 && (color.getRed()*255)<255 && (color.getBlue()*255)>138 && (color.getBlue()*255)<200)
                     //if the original image's red values are between these values x 255....
                        pixelWriter.setColor(x, y, Color.RED);
                    //set this to red in image2.
                    else
                        pixelWriter.setColor(x, y, Color.WHITE); //anything not purple or red is set to white in image2.
                }
            }
            imgImage2.setImage(image2); //imageView imgImage2 is set with WritableImage image2.
            imgImage2.setEffect(colorAdjust); //imageView is set with colorAdjust effect.
            return image2; //image2 is returned.
        }

        public void countCells() { //method for reading and processing cells as one dimensional arrays.
            int height = (int) image.getHeight(); //height is equal to image height.
            int width = (int) image.getWidth(); //width is equal to image width.
            int[] data = new int[height * width]; //array data is a one dimensional int array the size of height x width.
            noiseReduction = new int[height * width]; //array noiseReduction is a one dimensional int array of same size.
            red = new int[height * width]; //red is a one dimensional int array of same size.
            white = new int[height * width]; //white is a one dimensional int array of same size.

            PixelReader pixelReader = image2.getPixelReader(); //new pixelReader to read tricolour image's colour values.

            for (int i = 0; i < data.length; i++) {
                data[i] = -1; //data[i] is iteratively set to -1 (no value)
                noiseReduction[i] = 0; //noiseReduction[i] is iteratively set to 0 (no value)
                red[i] = i; //red iteratively populates with i.
                white[i] = i; //white iteratively populates with i.
            }

            for (int row = 0; row < height; row++) { //for int row, where row is 0 and less than height, and increments..

                for (int col = 0; col < width; col++) { //for int column, where column is 0 and less than width, and increments..
                    Color color = pixelReader.getColor(col, row); //new colour which gets the colour values for row and col.
                    if (color.equals(Color.WHITE)) { //if the colour is white, all arrays are given a -1 value (nothing there)
                        data[width * row + col] = -1; //n/a
                        red[width * row + col] = -1; //n/a
                        white[width * row + col] = -1; //n/a
                    }
                    if (color.equals(Color.RED)) { //if the colour is red, white is given a -1 value (nothing there)
                        white[width * row + col] = -1;
                    }
                    if (color.equals(Color.PURPLE)) { //if the colour is purple, red is given a -1 value (nothing there)
                        red[width * row + col] = -1;
                    }
                }

            }
            for (int i = 0; i < data.length; i++) {
                if (white[i] != -1) { //if a white value isn't -1 (something is there)
                    totalWhite.add(unionFind.findIterative(white, i)); //this is added to the hashSet totalWhite
                    noiseReduction[unionFind.findIterative(white, i)]++; //it is also added to noiseReduction to keep track.
                    noiseReductionSet.add(unionFind.findIterative(noiseReduction, i));
                    if ((i + 1) < white.length && white[i + 1] != -1 && (i + 1) % width != 0) {
                        //if i+1 is less than white's length and white at i+1 is not -1 (something is there) and i+1 mod width isn't 0
                        //(this is for handling row/col edge cases)
                        UnionFind.quickUnion(white, i, i + 1); //i and what is next to it (i+1) are unioned.
                    }
                    if ((i + width) < white.length && white[i + width] != -1) {
                        //if i + width is less than white's length and white at i+ width isn't -1 (something is there)
                        UnionFind.quickUnion(white, i, i + width); //i and i+width are unioned.

                    }
                }
                if (red[i] != -1) { //if a red value isn't -1 (something is there)
                    totalRed.add(unionFind.findIterative(red, i)); //this is added to the hashSet totalRed
                    noiseReduction[unionFind.findIterative(red, i)]++; //it is also added to noiseReduction to keep track.
                    noiseReductionSet.add(unionFind.findIterative(noiseReduction,i));
                    if ((i + 1) < red.length && red[i + 1] != -1 && (i + 1) % width != 0) {
                        //if i+1 is less than red's length and red at i+1 is not -1 (something is there) and i+1 mod width isn't 0
                        //(this is for handling row/col edge cases)
                        UnionFind.quickUnion(red, i, i + 1); //i and what is next to it (i+1) are unioned
                    }
                    if ((i + width) < red.length && red[i + width] != -1) {
                        //if i + width is less than red's length and red at i+ width isn't -1 (something is there)
                        UnionFind.quickUnion(red, i, i + width); //i and i+width are unioned.
                    }

                }
            }
                Iterator<Integer> iterator = totalWhite.iterator(); //a new iterator is initialized to iterate through totalWhite.
            System.out.println();
                while (iterator.hasNext()) { //while the iterator has a next value....
                    int num = iterator.next(); //num is equal to the iterator's next value.
                    if (noiseReduction[num] < 30) { //if noise reduction with num passed is less than 30
                        System.out.println(num + " : "+ noiseReduction[num]); //contents less than 30 are printed.

                    }
                }
                    Iterator<Integer> iterator2 = totalRed.iterator(); //a new iterator is initialized to iterate through totalRed.
            int num = iterator2.next(); //num is equal to the iterator's next value.

            while (iterator2.hasNext()) { //while the iterator has a next value....
                        if (noiseReduction[num] < 30) { //if noise reduction with num passed is less than 30
                            System.out.println(num + " : "+ noiseReduction[num]); //contents less than 30 are printed.

                        }
                        num=iterator2.next();
                    }
        }

    public void initialize(){ //sets up slider that reads the user-defined amount of pixels in a cell
        size.setMax(100); //max is 100.
        size.setMin(30); //min is 30 (anything less than 30 pixels is discarded)
        size.setBlockIncrement(0.1);
        size.setMajorTickUnit(0.5);
        size.snapToTicksProperty();

    }

        public void defineCellSize(){ //allows the user to define, greater than 30 inclusive, how many pixels are in a cell.

           int cellSize= (int) size.getValue(); //cellSize is defined as the current value of the size slider before the method
            totalCells=whiteCells=redCells=0;   //button is pushed by the user.

            System.out.println(cellSize);
            Iterator<Integer> iterator = totalRed.iterator(); //a new iterator is initialized to iterate through the totalRed hashset.
            while(iterator.hasNext()) { //while the iterator has a next value....
                int num = iterator.next(); //the int num is assigned the iterator's next value.
                if (noiseReduction[num] >= cellSize) { //if noiseReduction passed num is greater than or equal to cellSize decided by the user
                    redCells += (noiseReduction[num] / cellSize); //redCells increments and the result is noiseReduction passed num divided by cellSize.
                }
            }
            Iterator<Integer> iterator2 = totalWhite.iterator(); //a new iterator is initialized to iterate through the totalWhite hashset.
            while(iterator2.hasNext()) { //while the iterator has a next value....
                int num2 = iterator2.next(); //the int num2 is assigned the iterator's next value.
                if (noiseReduction[num2] >= cellSize) { //if noiseReduction passed num2 is greater than or equal to cellSize decided by the user
                    whiteCells += noiseReduction[num2] / cellSize; //whiteCells increments and the result is noiseReduction passed num2 divided by cellSize.
                }
            }
            totalCells= redCells+ whiteCells; //totalCells is defined as being the sum of the red
            reds.setText("Red Cells: " + redCells); //red cells output to GUI.
            whites.setText("White Cells: " + whiteCells ); //white cells output to GUI.
            total.setText("Total Cells: " + totalCells); //totalCells output to GUI.
        }


    public void rectangleRedClusters(){ //Draws blue rectangles around red blood cell clusters.
        PixelWriter pixelWriter= image2.getPixelWriter(); //New PixelWriter for writing rectangles to tricolour image.
        Iterator<Integer> iterator=totalRed.iterator(); //iterator to iterate through the contents of totalRed hashset
        int w = (int) image2.getWidth(); //int w set to width value of image2.
        for (int i=0; i< red.length; i++) {
            if(i%w==0){
                System.out.println();
            }
            System.out.print(unionFind.findIterative(red, i)+ " ");
        }
        Iterator<Integer> iterator2=totalRed.iterator();
        while(iterator2.hasNext()) {// while there is an existing next value....

            System.out.println(iterator2.next()); // num is equal to the iterator's next value.
        }
        int value=1; //value for labelling rectangles.
        while(iterator.hasNext()) {// while there is an existing next value....

            int num = iterator.next(); // num is equal to the iterator's next value.

            int leftX = w; //leftX set to w (width of image)
            int bottomY = -1; //bottomY set to -1 (to be overwritten)
            int rightX = 1; //bottomY set to -1 (to be overwritten)
            int topY = (int) image2.getHeight(); //topY set to value of image height.


            for (int i = 0; i < red.length; i++) { //for i=0, where i is less than the length of the red array and increments....
                if (UnionFind.findIterative(red, i) == num && (i / w) < topY) { //if the result of unionfind, passed red and i, equals num and i/w is less than topY....
                    topY = (int) (i / w); //topY is set to i/w (cast to int).
                }
                if (UnionFind.findIterative(red, i) == num && i % w < leftX) { //if the result of unionfind equals num and i mod w is less than leftX (w)
                    leftX = (int) (i % w); //leftX is set to i mod w (cast to int).
                }

                if (UnionFind.findIterative(red, i) == num && i % w > rightX) { //if the result of unionfind equals num and i mod w is greater than rightX
                    rightX = (int) (i % w); //rightX is set to i mod w (cast to int).
                }

                if (UnionFind.findIterative(red, i) == num && i / w > bottomY) { //if the result of unionfind equals num and i divided by w is greater than bottomY
                    bottomY = (int) (i / w); //bottomY is set to i divided by w (cast to int)
                }
            }

            for (int i = leftX; i < rightX; i++) { //for int i set to leftX, where i is less than rightX, and i increments..
                pixelWriter.setColor(i, topY, Color.BLUE); //pixelWriter draws a new blue line with parameters i, topY.
            }

            for (int i = topY; i < bottomY; i++) { //for int i set to topY, where i is less than bottomY, and i increments..
                pixelWriter.setColor(leftX, i, Color.BLUE); //pixelWriter draws a new blue line with parameters leftX, y.

            }
            for (int i = topY; i < bottomY; i++) { //for int i set to topY, where i is less than bottomY, and i increments..
                pixelWriter.setColor(rightX, i, Color.BLUE); //pixelWriter draws a new blue line with parameters rightX, i.
            }

            for (int i = leftX; i < rightX; i++) { //for int i set to leftX, where i is less than rightX, and i increments..
                pixelWriter.setColor(i, bottomY, Color.BLUE); //pixelWriter draws a new blue line with parameters i, bottomY.
            }

            //Drawing labels....
            Label n=new Label(value +""); //new label with value passed as parameter.
            n.setLayoutX((leftX/2)+280); //layoutX of label is positioned to leftX/2 +280
            n.setLayoutY((bottomY/2)+100); //layoutY of label is positioned to bottomY/2 +100
            ((Pane)imgImage2.getParent()).getChildren().add(n); //labels are added to imgImage2 imageview's parent.
            value++; //value increments.
        }
    }


    public void rectangleRedCells(){ //Draws blue rectangles around red blood cell clusters.
        //rectangles around red CLUSTERS must be blue.
        PixelWriter pixelWriter= image2.getPixelWriter(); //New PixelWriter for writing rectangles to tricolour image.
        Iterator<Integer> iterator=totalRed.iterator(); //iterator to iterate through the contents of totalRed hashset
        int w = (int) image2.getWidth();
        for (int i=0; i< noiseReduction.length; i++) {
            if(i%w==0){
                System.out.println();
            }
            System.out.print(unionFind.findIterative(noiseReduction, i)+ " ");
        }
        Iterator<Integer> iterator2=totalRed.iterator();
        while(iterator2.hasNext()) {// while there is an existing next value....

            System.out.println(iterator2.next()); // num is equal to the iterator's next value.
        }
        int value=1;
        while(iterator.hasNext()) {// while there is an existing next value....

            int num = iterator.next(); // num is equal to the iterator's next value.

            int leftX = w;
            int bottomY = -1;
            int rightX = 1;
            int topY = (int) image2.getHeight();


            for (int i = 0; i < noiseReduction.length; i++) {
                if (UnionFind.findIterative(noiseReduction, i) == num && (i / w) < topY) {
                    topY = (int) (i / w);
                }
                if (UnionFind.findIterative(noiseReduction, i) == num && i % w < leftX) {
                    leftX = (int) (i % w);
                }

                if (UnionFind.findIterative(noiseReduction, i) == num && i % w > rightX) {
                    rightX = (int) (i % w);
                }

                if (UnionFind.findIterative(noiseReduction, i) == num && i / w > bottomY) {
                    bottomY = (int) (i / w);
                }
            }

            for(int i= leftX; i < rightX; i++){
                pixelWriter.setColor(i, topY, Color.LIMEGREEN);
            }

            for (int i= topY; i < bottomY; i++ ){
                pixelWriter.setColor(leftX, i, Color.LIMEGREEN);

            }
            for (int i= topY; i < bottomY; i++){
                pixelWriter.setColor(rightX, i, Color.LIMEGREEN);
            }

            for(int i= leftX; i < rightX; i++){
                pixelWriter.setColor(i, bottomY, Color.LIMEGREEN);
            }

            //label
            Label n=new Label(value +"");
            n.setLayoutX((leftX/2)+280);
            n.setLayoutY((bottomY/2)+100);
            ((Pane)imgImage2.getParent()).getChildren().add(n);
            value++;


        }
    }

        public void rectangleWhiteClusters(){ //method for drawing green rectangles around white blood cells (very similar
            //to red blood cell cluster method above).
            PixelWriter pixelWriter= image2.getPixelWriter();
            Iterator<Integer> iterator=totalWhite.iterator();

            int value=1;
            while(iterator.hasNext()) {// while there is an existing next value....

                int num = iterator.next(); // num is equal to the iterator's next value.
                int w= (int) image.getWidth();

                int leftX = w;
                int bottomY = -1;
                int rightX = 1;
                int topY= (int) image.getHeight();


                for (int i = 0; i < white.length; i++) {
                    if (UnionFind.findIterative(white, i) == num && (i / w) < topY) {
                        topY = (int) (i / w);
                    }
                    if (UnionFind.findIterative(white, i) == num && i % w < leftX) {
                        leftX = (int) (i % w);
                    }

                    if (UnionFind.findIterative(white, i) == num && i % w > rightX) {
                        rightX = (int) (i % w);
                    }

                    if (UnionFind.findIterative(white, i) == num && i / w > bottomY) {
                        bottomY = (int) (i / w);
                    }
                }

                for(int i= leftX; i < rightX; i++){
                    pixelWriter.setColor(i, topY, Color.LIMEGREEN);
                }

                for (int i= topY; i < bottomY; i++ ){
                    pixelWriter.setColor(leftX, i, Color.LIMEGREEN);

                }
                for (int i= topY; i < bottomY; i++){
                    pixelWriter.setColor(rightX, i, Color.LIMEGREEN);
                }

                for(int i= leftX; i < rightX; i++){
                    pixelWriter.setColor(i, bottomY, Color.LIMEGREEN);
                }

                //label
                Label n=new Label(value +"");
                n.setLayoutX((leftX/2)+286);
                n.setLayoutY((bottomY/2)+100);
                ((Pane)imgImage2.getParent()).getChildren().add(n);
                value++;
            }
        }

        public void runRect(){ //runs all rectangle drawing-related methods at once.
          rectangleRedClusters();
          rectangleWhiteClusters();
          //rectangleRedCells();

        }



    public void exit(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();

    }
    
    }
