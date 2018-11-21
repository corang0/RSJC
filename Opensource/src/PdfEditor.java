import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PdfEditor extends Application {

	private static final double WIDTH = 600;
	private static final double HEIGHT = 450;
	private static final double PREVIEW_WIDTH = WIDTH * 0.35;
	private static final double PREVIEW_HEIGHT = HEIGHT * 0.60;
	private static final double SMALL_PREVIEW_WIDTH = WIDTH * 0.075;
	private static final double SMALL_PREVIEW_HEIGHT = HEIGHT * 0.12;
	private static final double PREVIEW_SPACING = WIDTH * 0.015;
	private static final double BUTTON_SIZE = WIDTH * 0.11;
	private static final double TEXTFIELD_LENGTH = WIDTH * 0.435;
	private static final double BUTTON2_WIDTH = WIDTH * 0.133;
	private static final double BUTTON2_HEIGHT = HEIGHT * 0.04;
	private static final double BUTTON_IMAGE_SIZE = BUTTON_SIZE * 0.7;

	private static PDDocument pddocument;
	private static File f;
	
	private static Label label1;
	private static Label label2;
	private static Label label3;
	private static Label label4;
	private static Label label5;
	private static Label pageLabel;
	
	public void clear() throws MalformedURLException {
		File file = new File("./image/empty.png");
		String str = file.toURI().toURL().toString();
        Image image = new Image(str, PREVIEW_WIDTH, PREVIEW_HEIGHT, false, true);
        ImageView imageView = new ImageView(image);
        label1.setGraphic(imageView);
		
		Image image2 = new Image(str, SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT, false, true);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image2);
        ImageView imageView4 = new ImageView(image2);
        ImageView imageView5 = new ImageView(image2);
        label2.setGraphic(imageView2);
        label3.setGraphic(imageView3);
        label4.setGraphic(imageView4);
        label5.setGraphic(imageView5);
	}
	
	public void load() throws IOException {
		final FileChooser fileChooser = new FileChooser();
		f = fileChooser.showOpenDialog(null);
		pddocument = PDDocument.load(f);
		
		clear();

		PDFRenderer renderer = new PDFRenderer(pddocument);
		BufferedImage image = renderer.renderImage(0);
		
		Image image2 = SwingFXUtils.toFXImage(image, null);
		ImageView imageView = new ImageView(image2);
		imageView.setFitWidth(PREVIEW_WIDTH);
		imageView.setFitHeight(PREVIEW_HEIGHT);
		label1.setGraphic(imageView);
		
		image = renderer.renderImage(1);
		image2 = SwingFXUtils.toFXImage(image, null);
		imageView = new ImageView(image2);
		imageView.setFitWidth(SMALL_PREVIEW_WIDTH);
		imageView.setFitHeight(SMALL_PREVIEW_HEIGHT);
		label2.setGraphic(imageView);
		
		image = renderer.renderImage(2);
		image2 = SwingFXUtils.toFXImage(image, null);
		imageView = new ImageView(image2);
		imageView.setFitWidth(SMALL_PREVIEW_WIDTH);
		imageView.setFitHeight(SMALL_PREVIEW_HEIGHT);
		label3.setGraphic(imageView);

		image = renderer.renderImage(3);
		image2 = SwingFXUtils.toFXImage(image, null);
		imageView = new ImageView(image2);
		imageView.setFitWidth(SMALL_PREVIEW_WIDTH);
		imageView.setFitHeight(SMALL_PREVIEW_HEIGHT);
		label4.setGraphic(imageView);
		
		image = renderer.renderImage(4);
		image2 = SwingFXUtils.toFXImage(image, null);
		imageView = new ImageView(image2);
		imageView.setFitWidth(SMALL_PREVIEW_WIDTH);
		imageView.setFitHeight(SMALL_PREVIEW_HEIGHT);
		label5.setGraphic(imageView);
		
		pageLabel.setText("  페이지 수: " + String.valueOf(pddocument.getNumberOfPages()));
	}
	
	public void save() {
		final FileChooser fileChooser = new FileChooser();
		File savefile = fileChooser.showSaveDialog(null);
		try {
			pddocument.save(savefile.getPath().concat(".pdf"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void mergePDFs() throws IOException { // 원하는 PDF들을 합치기
		final FileChooser fileChooser = new FileChooser();
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		int n = files.size();
		System.out.println(n);
		PDDocument[] doc = new PDDocument[n+1];

		doc[0] = pddocument;
		for (int i = 1; i < n+1; i++)
			doc[i] = PDDocument.load(files.get(i-1));
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		File file = fileChooser.showSaveDialog(null);
		String savePath = file.getPath();
		PDFmerger.setDestinationFileName(savePath + ".pdf");

		
		PDFmerger.addSource(f);
		for (int i = 0; i < n; i++)
			PDFmerger.addSource(files.get(i));

		PDFmerger.mergeDocuments();

		for (int i = 1; i < n+1; i++)
			doc[i].close();
	}

	public static void splitPDF() throws IOException { // pdf를 각 페이지 단위로 분리
		final FileChooser fileChooser = new FileChooser();
		Splitter splitter = new Splitter();
		List<PDDocument> Pages = splitter.split(pddocument);
		Iterator<PDDocument> iterator = Pages.listIterator();
		int i = 1;
		File f = fileChooser.showSaveDialog(null);
		String savePath = f.getPath();

		while (iterator.hasNext()) {
			PDDocument pd = iterator.next();
			pd.save(savePath + i++ + ".pdf");
		}

		System.out.println("Multiple PDF’s created");
	}
	
	public static void encryptPDF() throws IOException { // pdf 암호화 하기
		TextInputDialog dialog = new TextInputDialog();
		 
        dialog.setTitle("encryptPDF");
        dialog.setHeaderText("Enter your password:");
        dialog.setContentText("Password:");
 
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
        	AccessPermission ap = new AccessPermission();
			StandardProtectionPolicy spp = new StandardProtectionPolicy(name, name, ap);
			spp.setEncryptionKeyLength(128);
			try {
				pddocument.protect(spp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PDF has been encripted");
        });
	}
	
	public static void extractStringfromPDF() throws IOException { // 텍스트 추출하기
		final FileChooser filechooser = new FileChooser();
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(pddocument);
		
		File savefile = filechooser.showSaveDialog(null);
		File txtFile = new File(savefile.getPath() + ".txt");
		FileWriter fw = new FileWriter(txtFile, true);
		fw.write(text);
		fw.flush();
		fw.close();
	}
	
	public static void transformPdf(int choose, Stage primaryStage) throws IOException {
		Label secondLabel = new Label("Select Page");
		
		int pages = pddocument.getNumberOfPages();
		
		final Spinner<Integer> spinner = new Spinner<Integer>();
		 
        final int initialValue = 1;
 
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, pages, initialValue);
 
        spinner.setValueFactory(valueFactory);
        spinner.setPrefSize(BUTTON2_WIDTH * 2.5, BUTTON2_HEIGHT);
		 
        VBox secondaryLayout = new VBox();
        secondaryLayout.setPadding(new Insets(10,10,10,10));
        secondaryLayout.setSpacing(10);
        secondaryLayout.getChildren().addAll(secondLabel, spinner);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        Stage newWindow = new Stage();
        newWindow.setTitle("Convert");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(primaryStage);
        newWindow.setResizable(false);

        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        
        Button button1 = new Button("확인");
        button1.setPrefSize(BUTTON2_WIDTH * 1.2, BUTTON2_HEIGHT);
        button1.setOnAction(e -> {
        	int num = spinner.getValue();
        	newWindow.close();
        	
        	final FileChooser fileChooser = new FileChooser();

    		PDFRenderer renderer = new PDFRenderer(pddocument);
    		BufferedImage image;
			try {
				image = renderer.renderImage(num - 1);
				if (choose == 1) {
	    			File savefile = fileChooser.showSaveDialog(null);
	    			String str = savefile.getPath();
	    			ImageIO.write(image, "PNG", new File(str.concat(".png")));
	    		} else if (choose == 2) {
	    			File savefile = fileChooser.showSaveDialog(null);
	    			String str = savefile.getPath();
	    			ImageIO.write(image, "JPEG", new File(str.concat(".jpg")));
	    		}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        Button button2 = new Button("취소");
        button2.setPrefSize(BUTTON2_WIDTH * 1.2, BUTTON2_HEIGHT);
        button2.setOnAction(e -> {
        	newWindow.close();
        });
        
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(button1, button2);
        
        secondaryLayout.getChildren().add(hbox);

        newWindow.show();
	}
	
	public static void insertWMark() throws IOException {
		final FileChooser fileChooser = new FileChooser();

		File watermark = fileChooser.showOpenDialog(null);
		String wm = watermark.getPath();

		HashMap<Integer, String> overlapProps = new HashMap<Integer, String>();
		for (int i = 0; i < pddocument.getNumberOfPages(); i++) {
			overlapProps.put(i + 1, wm);
		}

		Overlay overlay = new Overlay();
		overlay.setInputPDF(pddocument);
		overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
		overlay.overlay(overlapProps);
	}

	public static void insertCover() throws IOException {
		final FileChooser fileChooser = new FileChooser();
		PDPage page = pddocument.getPage(0);

		File image = fileChooser.showOpenDialog(null);
		PDImageXObject pdImage = PDImageXObject.createFromFile(image.getPath(), pddocument);
		PDPageContentStream contents = new PDPageContentStream(pddocument, page);

		contents.drawImage(pdImage, 100, 100);
		contents.close();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox root = new HBox();
		root.setSpacing(0);
		root.setPadding(new Insets(0));
		
		VBox leftVBox = new VBox();
		leftVBox.setSpacing(0);
		leftVBox.setPadding(new Insets(0));
		
		VBox rightVBox = new VBox();
		rightVBox.setSpacing(0);
		rightVBox.setPadding(new Insets(0));
		
		// --- Left Side --------------
		HBox leftHBox1 = new HBox();
        leftHBox1.setSpacing(0);
        leftHBox1.setPadding(new Insets(40,20,0,40));
        
        label1 = new Label();
        label1.setPrefSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        File file = new File("./image/empty.png");
		String str = file.toURI().toURL().toString();
        Image image = new Image(str, PREVIEW_WIDTH, PREVIEW_HEIGHT, false, true);
        ImageView imageView = new ImageView(image);
        label1.setGraphic(imageView);
              
        leftHBox1.getChildren().add(label1);
        
        HBox leftHBox2 = new HBox();
        leftHBox2.setSpacing(PREVIEW_SPACING);
        leftHBox2.setPadding(new Insets(20,20,20,40));
        
        label2 = new Label();
        label2.setPrefSize(SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT);
        label3 = new Label();
        label3.setPrefSize(SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT);
        label4 = new Label();
        label4.setPrefSize(SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT);
        label5 = new Label();
        label5.setPrefSize(SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT);
        Image image2 = new Image(str, SMALL_PREVIEW_WIDTH, SMALL_PREVIEW_HEIGHT, false, true);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image2);
        ImageView imageView4 = new ImageView(image2);
        ImageView imageView5 = new ImageView(image2);
        label2.setGraphic(imageView2);
        label3.setGraphic(imageView3);
        label4.setGraphic(imageView4);
        label5.setGraphic(imageView5);
        
        leftHBox2.getChildren().addAll(label2, label3, label4, label5);    
        
        HBox leftHBox3 = new HBox();
        leftHBox3.setPadding(new Insets(0,0,0,100));
        pageLabel = new Label();
        pageLabel.setPrefWidth(90);
        pageLabel.setStyle("-fx-border-color: #000000; -fx-border-width: 0.5px; -fx-background-color: #ffffff");
        leftHBox3.getChildren().add(pageLabel);
        
        leftVBox.getChildren().addAll(leftHBox1, leftHBox2, leftHBox3);
		
        // --- Right Side --------------
		FlowPane flowPane = new FlowPane();
		flowPane.setPadding(new Insets(40,20,10,20));
		flowPane.setHgap(30);
		flowPane.setVgap(30);
        
        // --- TextField -----------
        TextField textField = new TextField();
        textField.setPrefWidth(TEXTFIELD_LENGTH);
        
        // --- Button --------------
        for (int i = 0; i < 8 ; i++) {
        	Button button = new Button();
        	button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        	
        	if (i == 0) {
        		button.setOnAction(e -> {
					try {
						mergePDFs();
						textField.setText("PDF merged");
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button1.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("선택한 pdf 파일들을 하나로 합친 새로운 pdf 파일을 새로 만든다.");
                button.setTooltip(tooltip_userName);
        	}
        	
        	if (i == 1) {
        		button.setOnAction(e -> {
					try {
						splitPDF();
						textField.setText("PDF splited");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button2.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("선택한 pdf파일을 페이지별로 분리해서 1페이지짜리 파일들을 페이지 수 만큼 pdf 파일을 만든다.");
                button.setTooltip(tooltip_userName);
        	}
        	
        	if (i == 2) {
        		button.setOnAction(e -> {
					try {
						encryptPDF();
						textField.setText("PDF encrypted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button3.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE * 0.8, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("선택한 pdf파일에 비밀번호를 건다.");
                button.setTooltip(tooltip_userName);
        	}
        		
        	if (i == 3) {
        		button.setOnAction(e -> {
					try {
						extractStringfromPDF();
						textField.setText("PDF -> txt converted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button4.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE, BUTTON_IMAGE_SIZE * 0.7, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("pdf파일의 모든 페이지의 글자를 텍스트 파일에 저장한다.");
                button.setTooltip(tooltip_userName);
        	}
        	
        	if (i == 4) {
        		button.setOnAction(e -> {
					try {
						transformPdf(1, primaryStage);
						textField.setText("PDF -> png converted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button5.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("pdf파일에서 원하는 페이지를 선택하여  png 파일로 변환해준다.");
                button.setTooltip(tooltip_userName);
        	}
        	
        	if (i == 5) {
        		button.setOnAction(e -> {
					try {
						transformPdf(2, primaryStage);
						textField.setText("PDF -> jpg converted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button6.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("pdf파일에서 원하는 페이지를 선택하여  jpg 파일로 변환해준다.");
                button.setTooltip(tooltip_userName);
        	}
        		
        	if (i == 6) {
        		button.setOnAction(e -> {
					try {
						insertWMark();
						textField.setText("Watermark inserted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button7.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE * 0.8, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("pdf파일에서 맨 앞에 원하는 png를 넣어 표지를 만들어준다.");
                button.setTooltip(tooltip_userName);
        	}
        		
        	if (i == 7) {
        		button.setOnAction(e -> {
					try {
						insertCover();
						textField.setText("Cover inserted");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
        		
        		File filei = new File("./image/button8.png");
        		String stri = filei.toURI().toURL().toString();
                Image imagei = new Image(stri, BUTTON_IMAGE_SIZE * 0.8, BUTTON_IMAGE_SIZE, false, true);
                ImageView imageViewi = new ImageView(imagei);
                button.setGraphic(imageViewi);
                
                Tooltip tooltip_userName=new Tooltip("pdf파일의 모든 페이지에 원하는 워터마크를 넣어준다.");
                button.setTooltip(tooltip_userName);
        	}
     
            DropShadow shadow = new DropShadow();
     
            // Adding the shadow when the mouse cursor is on
            button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> button.setEffect(shadow));
     
            // Removing the shadow when the mouse cursor is off
            button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> button.setEffect(null));
            
            flowPane.getChildren().add(button);
        }
        
        flowPane.getChildren().add(textField);
        
        HBox rightHBox1 = new HBox();
        rightHBox1.setSpacing(10);
        rightHBox1.setPadding(new Insets(20));
        
        Button rhButton1 = new Button("불러오기");
        rhButton1.setPrefSize(BUTTON2_WIDTH, BUTTON2_HEIGHT);
        rhButton1.setOnAction(e -> {
			try {
				load();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        Button rhButton2 = new Button("저장하기");
        rhButton2.setPrefSize(BUTTON2_WIDTH, BUTTON2_HEIGHT);
        rhButton2.setOnAction(e -> save());
        Button rhButton3 = new Button("나가기");
        rhButton3.setPrefSize(BUTTON2_WIDTH, BUTTON2_HEIGHT);
        rhButton3.setOnAction(e -> primaryStage.close());
        
        rightHBox1.getChildren().addAll(rhButton1, rhButton2, rhButton3);
        
        rightVBox.getChildren().addAll(flowPane, rightHBox1);
        
        root.getChildren().addAll(leftVBox, rightVBox);
        
        primaryStage.setTitle("PDF Editor");
 
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}

}