package pdfbox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class FxTest extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public static void createPdf() throws IOException {
		
		  //Creating PDF document object 
	      PDDocument document = new PDDocument();    
	       
	      //Saving the document
	      document.save("D:/PdfBox_Examples/my_doc.pdf");
	    
	      //Closing the document  
	      document.close();

	}
	
	public static void add1Page() throws IOException {
		   
	      //Loading an existing document 
	      File file = new File("D:/PdfBox_Examples/sample.pdf"); 
	      PDDocument document = PDDocument.load(file); 
	        
	      //Adding a blank page to the document 
	      document.addPage(new PDPage());  

	      //Saving the document 
	      document.save("D:/PdfBox_Examples/sample.pdf");

	      //Closing the document  
	      document.close(); 
	        
	}  
	
	public static void add10Page() throws IOException {
	       
	      //Creating PDF document object 
	      PDDocument document = new PDDocument();

	      for (int i=0; i<10; i++) {
	         //Creating a blank page 
	         PDPage blankPage = new PDPage();

	         //Adding the blank page to the document
	         document.addPage( blankPage );
	      } 
	     
	      //Saving the document
	      document.save("D:/PdfBox_Examples/sample.pdf");
	      
	      //Closing the document
	      document.close();

	} 
	
	public static void removePage() throws IOException {
	       
		//Loading an existing document
	    File file = new File("D:/PdfBox_Examples/sample.pdf");
	    PDDocument document = PDDocument.load(file);
	       
	    //Listing the number of existing pages
	    int noOfPages= document.getNumberOfPages();
	    System.out.print(noOfPages);
	      
        //Removing the pages
	    document.removePage(2);

	    //Saving the document
	    document.save("D:/PdfBox_Examples/sample.pdf");

	    //Closing the document
	    document.close();

	} 

	@Override
	public void start(Stage primaryStage) throws Exception {
		FlowPane root = new FlowPane();
        root.setPadding(new Insets(20));
        root.setHgap(50);
        root.setVgap(30);
        
        // --- TextField -----------
        TextField textField = new TextField();
        textField.setPrefWidth(350);
 
        // --- Button 1 ------------
        Button button1 = new Button("Create file");
        button1.setPrefSize(150, 40);
        
        button1.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				textField.setText("PDF created");
				try {
					createPdf();
				} catch(IOException e) {
					System.out.println("IO error!");
				}
			}
		});
 
        DropShadow shadow = new DropShadow();
 
        // Adding the shadow when the mouse cursor is on
        button1.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button1.setEffect(shadow);
            }
        });
 
        // Removing the shadow when the mouse cursor is off
        button1.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button1.setEffect(null);
            }
        });
 
        // --- Button 2 ------------
        Button button2 = new Button("Add1 page");
        button2.setPrefSize(150, 40);
        
        button2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				textField.setText("1 page added");
				try {
					add1Page();
				} catch(IOException e) {
					System.out.println("IO error!");
				}
			}
		});
 
        // Adding the shadow when the mouse cursor is on
        button2.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button2.setEffect(shadow);
            }
        });
 
        // Removing the shadow when the mouse cursor is off
        button2.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button2.setEffect(null);
            }
        });
        
        // --- Button 3 ------------
        Button button3 = new Button("Add10 page");
        button3.setPrefSize(150, 40);
        
        button3.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				textField.setText("10 page added");
				try {
					add10Page();
				} catch(IOException e) {
					System.out.println("IO error!");
				}
			}
		});
 
        // Adding the shadow when the mouse cursor is on
        button3.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button3.setEffect(shadow);
            }
        });
 
        // Removing the shadow when the mouse cursor is off
        button3.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button3.setEffect(null);
            }
        });
        
        // --- Button 4 ------------
        Button button4 = new Button("Remove page");
        button4.setPrefSize(150, 40);
        
        button4.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				textField.setText("page removed");
				try {
					removePage();
				} catch(IOException e) {
					System.out.println("IO error!");
				}
			}
		});
 
        // Adding the shadow when the mouse cursor is on
        button4.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button4.setEffect(shadow);
            }
        });
 
        // Removing the shadow when the mouse cursor is off
        button4.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button4.setEffect(null);
            }
        });
        
        root.getChildren().addAll(button1, button2, button3, button4, textField);
 
        primaryStage.setTitle("Using PdfBox with JavaFx");
 
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

	}

}
