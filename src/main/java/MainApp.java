import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.lynden.gmapsfx.GeoCoding.AddressConverter;
import com.lynden.gmapsfx.GeoCoding.GoogleResponse;
import com.lynden.gmapsfx.GeoCoding.Result;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Example Application for creating and loading a GoogleMap into a JavaFX
 * application
 *
 * @author Rob Terpilowski
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainApp extends Application implements MapComponentInitializedListener {

    protected GoogleMapView mapComponent;
    protected GoogleMap map;

    double lat;
    double lng;

    private Button btnZoomIn;
    private Button btnZoomOut;
    private Label lblZoom;
    private Label lblCenter;
    private Label lblClick;
    private ComboBox<MapTypeIdEnum> mapTypeCombo;
	
	private MarkerOptions markerOptions;
	private ArrayList<Marker> myMarker;
	private Button btnHideMarker;
	private Button btnDeleteMarker;
    private Button btnClustering;
    private Button btnFileChooser;
    private ChoiceBox selectClustering;
    private CheckBox precentage;
    private CheckBox money;
    private CheckBox lda;
    private CheckBox normalize;
    private Button btnEvaluation;
    private TextArea results;
    private HBox btb;
    private Button clean;


    Slider k = new Slider(1,5,3);
    Slider m = new Slider(1,10,1.5);
    final Label numK = new Label("Number of Clusters:");
    final Label valueM = new Label("Fuzzyness m:");

    final Label kValue = new Label(
            Double.toString(k.getValue()));
    final Label mValue = new Label(
            Double.toString(m.getValue()));

    private Vectorlize vectorLize = null;
    private File selectedFile ;
    String filePath = "src/main/resources/training_v3.json";
    HashMap<String, City> cityData;
    int clusterFilter=0; //0 kmeans, 1 fcm
    int[] featureFilter={0,0,0,0};

    private int numCluster=3;
    private double fuzzyness=1.5;
    String[] colors= {"red","blue","yellow","green","black","white"};
    private ArrayList<Double> sse= new ArrayList<>();
    private ArrayList<Double> purity = new ArrayList<>();


    ArrayList<LatLong> locations = new ArrayList<>();


    @Override
    public void start(final Stage stage) throws Exception {

        mapComponent = new GoogleMapView();
        mapComponent.addMapInializedListener(this);

        myMarker =new ArrayList<>();

        btb= new HBox();
        BorderPane bp = new BorderPane();
        ToolBar tb_top = new ToolBar();
        ToolBar tb_left = new ToolBar();
        clean = new Button("clean map");
        clean.setOnAction(e -> {

            deleteMarker();
            mapInitialized();
                    });

        btnZoomIn = new Button("Zoom In");
             btnZoomIn.setOnAction(e -> {
                 map.zoomProperty().set(map.getZoom() + 1);
        });
        btnZoomIn.setDisable(true);

        btnZoomOut = new Button("Zoom Out");
             btnZoomOut.setOnAction(e -> {
                 map.zoomProperty().set(map.getZoom() - 1);
        });
        btnZoomOut.setDisable(true);

        lblZoom = new Label();
        lblCenter = new Label();
        lblClick = new Label();
        
        mapTypeCombo = new ComboBox<>();
             mapTypeCombo.setOnAction(e -> {
                 map.setMapType(mapTypeCombo.getSelectionModel().getSelectedItem());
        });
             mapTypeCombo.setDisable(true);
        
        Button btnType = new Button("Map type");
        btnType.setOnAction(e -> {
            map.setMapType(MapTypeIdEnum.HYBRID);
        });

		btnHideMarker = new Button("Hide Marker");
		btnHideMarker.setOnAction(e -> {hideMarker();});
		
		btnDeleteMarker = new Button("Delete Marker");
		btnDeleteMarker.setOnAction(e -> {deleteMarker();});

        btnClustering = new Button("RunClustering");
        btnClustering.setOnAction(e -> {

            vectorLize= null;

            try {
                vectorLize = new Vectorlize(filePath);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            if(featureFilter[0]==1)
                vectorLize.addPrecent();
            if(featureFilter[1]==1)
                try {
                    vectorLize.addAveragePrice();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }


            if(featureFilter[2]==1)
                try {
                    vectorLize.addLDA();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            if(featureFilter[3]==1)
                vectorLize.normalize();


            if(clusterFilter==0){
                try {

                    ArrayList<Vector> vc = vectorLize.getCityVectors();


                    runKMeans(numCluster,vc);
                } catch (FormClusterException e1) {
                    e1.printStackTrace();
                }

            }
            else if(clusterFilter  ==1) {
                ArrayList<Vector> vc = vectorLize.getCityVectors();
                for(ArrayList<Double> data :vectorLize.getVectors()){
                        System.out.println(vc.size()+" "+ data.size());
                }

                try {
                    runFCM(numCluster, fuzzyness, vc);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            btb.getChildren().clear();
//            System.out.println(numCluster+ " asdasdasdasdas");

            for(int i=0; i <numCluster;i++){
                Label cName = new Label("Cluster "+i);
                javafx.scene.shape.Circle circle =new javafx.scene.shape.Circle();
                circle.setRadius(5.0);
                circle.setFill(Color.valueOf(colors[i]));

                btb.getChildren().addAll(cName,circle);
            }

            addMarker();

        });

        btnFileChooser = new Button("Load Data");
        btnFileChooser.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            filePath = selectedFile.getPath();
            try {
                vectorLize = new Vectorlize(filePath);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            System.out.println(filePath);
            ArrayList<String> citys = new ArrayList<String>();
            for(Vector v: vectorLize.getCityVectors()){
                citys.add( v.city);
            }
            locations=getLatLong(citys);

            mapInitialized();
            addMarker();

        });


        selectClustering =  new ChoiceBox(FXCollections.observableArrayList(
                "KMeans", "FuzzyCMeans"));

        selectClustering.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                clusterFilter = newValue.intValue();
            }
        });

        precentage =new CheckBox();
        money =new CheckBox();
        lda =new CheckBox();
        normalize =new CheckBox();

        precentage.setText("Percentage Feature");
        money.setText("Ave. Price Feature");
        lda.setText("LDA Feature");
             normalize.setText("Normalization");

        precentage.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if (new_val)
                    featureFilter[0] = 1;
                else
                    featureFilter[0] = 0;
            }
        });

        money.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if (new_val)
                    featureFilter[1] = 1;
                else
                    featureFilter[1] = 0;
            }
        });

        lda.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if(new_val)
                    featureFilter[2] =1;
                else
                    featureFilter[2] =0;
            }
        });

        normalize.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if (new_val)
                    featureFilter[3] = 1;
                else
                    featureFilter[3] = 0;
            }
        });

        btnEvaluation= new Button("Evaluation");
             btnEvaluation.setOnAction(e -> {
                 final Stage dialog = new Stage();
                 dialog.initModality(Modality.APPLICATION_MODAL);
                 dialog.initOwner(stage);
                 VBox dialogVbox = new VBox(20);
                 String text ="";

                 if(clusterFilter==0)
                     text+= "K-Means Clustering Evaluation: \n";
                 else if (clusterFilter==1)
                     text+= "Fuzzy-C-Means Clustering Evaluation: \n";

                 text+= "Number of Clusters: "+ numCluster+"\n";

                 text+= "SSE: \n";

                 text+=sse.toString()+"\n";

                 text+="Purity: \n" + purity.toString();


                 dialogVbox.getChildren().add(new Text(text));
                 Scene dialogScene = new Scene(dialogVbox, 500, 200);

                 dialog.setScene(dialogScene);
                 dialog.show();
              });


        k.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                kValue.setText(new_val.intValue() + "");
                numCluster = Math.round(new_val.intValue());

            }
        });
        m.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                mValue.setText(String.format("%.2f", new_val));
                fuzzyness = new_val.doubleValue();
            }
        });

        results =new TextArea();
             results.setPrefSize(40,250);
        results.setWrapText(true);



        ///
        tb_top.getItems().addAll(btnZoomIn, btnZoomOut, mapTypeCombo,
                new Label("Zoom: "), lblZoom,
                new Label("Center: "), lblCenter,
                new Label("Click: "), lblClick,
                btnHideMarker, btnDeleteMarker,clean);
        tb_top.setOrientation(Orientation.HORIZONTAL);

        //function toolbar
        tb_left.getItems().addAll(btnFileChooser,selectClustering,precentage,money,lda,normalize,numK,k,kValue,valueM,m,mValue,btnClustering,btnEvaluation,results,btb);
        tb_left.setOrientation(Orientation.VERTICAL);

        bp.setTop(tb_top);
        bp.setLeft(tb_left);
        bp.setCenter(mapComponent);

        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void mapInitialized() {
        //Once the map has been loaded by the Webview, initialize the map details.
        LatLong center = new LatLong(47.606189, -122.335842);
        mapComponent.addMapReadyListener(() -> {
            // This call will fail unless the map is completely ready.
            checkCenter(center);
        });

        MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(true)
                .zoom(15)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .mapType(MapTypeIdEnum.TERRAIN);

        map = mapComponent.createMap(options);

        map.setHeading(123.2);
//        System.out.println("Heading is: " + map.getHeading() );

//        MarkerOptions markerOptions = new MarkerOptions();
//        LatLong markerLatLong = new LatLong(47.606189, -122.335842);
//        markerOptions.position(markerLatLong)
//                .title("My new Marker")
//                .animation(Animation.DROP)
//                .visible(true);
//
//        final Marker myMarker = new Marker(markerOptions);
//
//        markerOptions2 = new MarkerOptions();
//        LatLong markerLatLong2 = new LatLong(47.906189, -122.335842);
//        markerOptions2.position(markerLatLong2)
//                .title("My new Marker")
//                .visible(true);
//
//        myMarker2 = new Marker(markerOptions2);
//
//        map.addMarker(myMarker);
//        map.addMarker(myMarker2);



        InfoWindowOptions infoOptions = new InfoWindowOptions();
        infoOptions.content("<h2>Here's an info window</h2><h3>with some info</h3>")
                .position(center);

        InfoWindow window = new InfoWindow(infoOptions);
//        window.open(map, myMarker);


        map.fitBounds(new LatLongBounds(new LatLong(30, 120), center));
//        System.out.println("Bounds : " + map.getBounds());

        lblCenter.setText(map.getCenter().toString());
        map.centerProperty().addListener((ObservableValue<? extends LatLong> obs, LatLong o, LatLong n) -> {
            lblCenter.setText(n.toString());
        });

        lblZoom.setText(Integer.toString(map.getZoom()));
        map.zoomProperty().addListener((ObservableValue<? extends Number> obs, Number o, Number n) -> {
            lblZoom.setText(n.toString());
        });

//      map.addStateEventHandler(MapStateEventType.center_changed, () -> {
//			System.out.println("center_changed: " + map.getCenter());
//		});
//        map.addStateEventHandler(MapStateEventType.tilesloaded, () -> {
//			System.out.println("We got a tilesloaded event on the map");
//		});
        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            //System.out.println("LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
            lblClick.setText(ll.toString());
        });

        btnZoomIn.setDisable(false);
        btnZoomOut.setDisable(false);
        mapTypeCombo.setDisable(false);

        mapTypeCombo.getItems().addAll( MapTypeIdEnum.ALL );

//        LatLong[] ary = new LatLong[]{markerLatLong, markerLatLong2};
//        MVCArray mvc = new MVCArray(ary);

//        PolylineOptions polyOpts = new PolylineOptions()
//                .path(mvc)
//                .strokeColor("red")
//                .strokeWeight(2);

//        Polyline poly = new Polyline(polyOpts);
//        map.addMapShape(poly);
//        map.addUIEventHandler(poly, UIEventType.click, (JSObject obj) -> {
//            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
////            System.out.println("You clicked the line at LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
//        });

//        LatLong poly1 = new LatLong(47.429945, -122.84363);
//        LatLong poly2 = new LatLong(47.361153, -123.03040);
//        LatLong poly3 = new LatLong(47.387193, -123.11554);
//        LatLong poly4 = new LatLong(47.585789, -122.96722);
//        LatLong[] pAry = new LatLong[]{poly1, poly2, poly3, poly4};
//        MVCArray pmvc = new MVCArray(pAry);
//
//        PolygonOptions polygOpts = new PolygonOptions()
//                .paths(pmvc)
//                .strokeColor("blue")
//                .strokeWeight(2)
//                .editable(false)
//                .fillColor("lightBlue")
//                .fillOpacity(0.5);
//
//        Polygon pg = new Polygon(polygOpts);
//        map.addMapShape(pg);
//        map.addUIEventHandler(pg, UIEventType.click, (JSObject obj) -> {
//            //polygOpts.editable(true);
//            pg.setEditable(!pg.getEditable());
//        });

//        LatLong centreC = new LatLong(47.545481, -121.87384);
//        CircleOptions cOpts = new CircleOptions()
//                .center(centreC)
//                .radius(50000)
//                .strokeColor("green")
//                .strokeWeight(2)
//                .fillColor("orange")
//                .fillOpacity(0.3);
//
//        Circle c = new Circle(cOpts);
//        map.addMapShape(c);
//        map.addUIEventHandler(c, UIEventType.click, (JSObject obj) -> {
//            c.setEditable(!c.getEditable());
//        });

//        LatLongBounds llb = new LatLongBounds(new LatLong(47.533893, -122.89856), new LatLong(47.580694, -122.80312));
//        RectangleOptions rOpts = new RectangleOptions()
//                .bounds(llb)
//                .strokeColor("black")
//                .strokeWeight(2)
//                .fillColor("null");
//
//        Rectangle rt = new Rectangle(rOpts);
//        map.addMapShape(rt);
//
//        LatLong arcC = new LatLong(47.227029, -121.81641);
//        double startBearing = 0;
//        double endBearing = 30;
//        double radius = 30000;
//
//        MVCArray path = ArcBuilder.buildArcPoints(arcC, startBearing, endBearing, radius);
//        path.push(arcC);
//
//        Polygon arc = new Polygon(new PolygonOptions()
//                .paths(path)
//                .strokeColor("blue")
//                .fillColor("lightBlue")
//                .fillOpacity(0.3)
//                .strokeWeight(2)
//                .editable(false));
//
//        map.addMapShape(arc);
//        map.addUIEventHandler(arc, UIEventType.click, (JSObject obj) -> {
//            arc.setEditable(!arc.getEditable());
//        });

//        LatLong ll = new LatLong(-41.2, 145.9);
//        LocationElevationRequest ler = new LocationElevationRequest(new LatLong[]{ll});
//
//        ElevationService es = new ElevationService();
//        es.getElevationForLocations(ler, new ElevationServiceCallback() {
//            @Override
//            public void elevationsReceived(ElevationResult[] results, ElevationStatus status) {
////                System.out.println("We got results from the Location Elevation request:");
//                for (ElevationResult er : results) {
//                    System.out.println("LER: " + er.getElevation());
//                }
//            }
//        });

//        LatLong lle = new LatLong(-42.2, 145.9);
//        PathElevationRequest per = new PathElevationRequest(new LatLong[]{ll, lle}, 3);
//
//        ElevationService esb = new ElevationService();
//        esb.getElevationAlongPath(per, new ElevationServiceCallback() {
//            @Override
//            public void elevationsReceived(ElevationResult[] results, ElevationStatus status) {
////                System.out.println("We got results from the Path Elevation Request:");
//                for (ElevationResult er : results) {
//                    System.out.println("PER: " + er.getElevation());
//                }
//            }
//        });

//        MaxZoomService mzs = new MaxZoomService();
//        mzs.getMaxZoomAtLatLng(lle, new MaxZoomServiceCallback() {
//            @Override
//            public void maxZoomReceived(MaxZoomResult result) {
//                System.out.println("Max Zoom Status: " + result.getStatus());
//                System.out.println("Max Zoom: " + result.getMaxZoom());
//            }
//        });


    }

    private void addMarker(){
        int i=0;
        for(LatLong l: locations){

            MarkerOptions markerOptions = new MarkerOptions();

            LatLong markerLatLong = l;
            markerOptions.position(markerLatLong)
                    .title("My new Marker")
                    .visible(true);

            myMarker.add(new Marker(markerOptions));


        }

        for(Marker marker: myMarker){

            map.addMarker(marker);

        }

    }

	private void hideMarker() {
//		System.out.println("deleteMarker");
        for(Marker maker: myMarker) {

            boolean visible = maker.getVisible();

            //System.out.println("Marker was visible? " + visible);

            maker.setVisible(!visible);
        }

//				markerOptions2.visible(Boolean.FALSE);
//				myMarker2.setOptions(markerOptions2);
//		System.out.println("deleteMarker - made invisible?");
	}

	private void deleteMarker() {
		//System.out.println("Marker was removed?");
        for(Marker maker: myMarker)
		map.removeMarker(maker);

        myMarker.clear();
	}

    private void checkCenter(LatLong center) {
//        System.out.println("Testing fromLatLngToPoint using: " + center);
//        Point2D p = map.fromLatLngToPoint(center);
//        System.out.println("Testing fromLatLngToPoint result: " + p);
//        System.out.println("Testing fromLatLngToPoint expected: " + mapComponent.getWidth()/2 + ", " + mapComponent.getHeight()/2);
    }

    private void runKMeans(int k, ArrayList<Vector> vc) throws FormClusterException {

        ArrayList<ArrayList<Double>> seeds = new ArrayList<>();
        for(int i=0; i<k;i++){
            Random rand = new Random();
            int randomNum = rand.nextInt(vc.size()-1);
            System.out.println(randomNum);
            seeds.add(vc.get(randomNum).data);

        }



        KMeansClustering kMeansClustering = new KMeansClustering(k,vc);
        ArrayList<Cluster> clusters;
        String result="";


        System.out.println("FORMING CLUSTERS");
        ArrayList<ArrayList<Double>> answers;
        try {
            answers = kMeansClustering.formClusters(seeds);

        } catch (Exception e) {
            throw new FormClusterException();
        }
        if (answers == null) {
            System.out.println("formClusters method in KMeansClustering.java is returning a null value.");
            throw new FormClusterException();
        }

        int index=0;
        for(Cluster c : kMeansClustering.getClusters()){
            result+="clusters:"+index+": ";
            for(Vector v: c.vectors)
                result+= v.city+" ";
            result+="\n";
            index++;
        }

        Evaluation eva = null;
        try {
            eva = new Evaluation(kMeansClustering.getClusters());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sse= kMeansClustering.getSSE();
        purity= eva.purity();



        result+= "centroids: "+answers.toString()+"\n";
        result+= "SSE: " + kMeansClustering.getSSE().stream().mapToDouble(Double::doubleValue).sum()+"\n";
        result+="Purity: "+ purity.toString();
        results.setText(result);



        showClusters(kMeansClustering.getClusters());

    }


    private void runFCM(int k, double m, ArrayList<Vector> vc) throws IOException {

        FuzzyCMeansClustering FCM = new FuzzyCMeansClustering(k,m,0.5, vc);
        FCM.runFcm_myself();
        String result="";


        for(int i=0; i<k;i++){
            result+="clusters:"+i+": ";
            for(Vector city: FCM.getClusters().get(i).vectors)
                result+=city.city+" ";
            result+="\n";
        }

        Evaluation eva = new Evaluation(FCM.getClusters());
        sse= FCM.getSSE();
        purity= eva.purity();

        result+="centroids: "+FCM.getCentroids().toString()+"\n";
        result+="SSE: "+ FCM.getSSE().stream().mapToDouble(Double::doubleValue).sum()+"\n";
        result+="purity: "+purity.toString();

        results.setText(result);
        showClusters(FCM.getClusters());
    }

    private ArrayList<LatLong> getLatLong(ArrayList<String> citys){
        ArrayList<LatLong> latLong = new ArrayList<>();

        for(String city: citys){

            GoogleResponse res = null;
            try {
                res = new AddressConverter().convertToLatLong(city);
            } catch (IOException e) {
                e.printStackTrace();
            }


            ArrayList<String> lat = new ArrayList<>();
            ArrayList<String> lng= new ArrayList<>();
            if(res.getStatus().equals("OK"))
            {
                for(Result result : res.getResults())
                {
                    lat.add(result.getGeometry().getLocation().getLat());
                    lng.add(result.getGeometry().getLocation().getLng());

                }


               LatLong l= new LatLong(Double.parseDouble(lat.get(0)),Double.parseDouble(lng.get(0)));
                System.out.println(lat.toString()+" "+ lng.toString());
                latLong.add(l);

            }


        }


        return latLong;
    }


    private void showClusters(ArrayList<Cluster> clusters){


        mapInitialized();
        int i=0;

        for(Cluster c: clusters){
            ArrayList<String> citys = new ArrayList<>();
            System.out.println(clusters.size());


            for(Vector v: c.vectors){
                citys.add(v.city);
            }

            ArrayList<LatLong> l= getLatLong(citys);



            for(LatLong ll: l){

                LatLong centreC = ll;
                CircleOptions cOpts = new CircleOptions()
                        .center(centreC)
                        .radius(10000)
                        .strokeColor(colors[i])
                        .strokeWeight(20)
                        .fillColor(colors[i])
                        .fillOpacity(0.3);

                Circle circle = new Circle(cOpts);
                map.addMapShape(circle);
                map.addUIEventHandler(circle, UIEventType.click, (JSObject obj) -> {
                    circle.setEditable(!circle.getEditable());
                });

            }

            i++;

        }






    }
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.useSystemProxies", "true");
        launch(args);
    }

}
