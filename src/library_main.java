import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.mongodb.DB;
import com.mongodb.MongoClient;


public class library_main {
 static library_front front;
 static library_menu menu ;
 static library_create crt;
 static PDFManager pdf;
 static MongoClient mongoClient;
 static DB db;
 
 static public void DbConnection() {
	 try{   
         mongoClient = new MongoClient( "localhost" , 27017 );

         db = mongoClient.getDB( "test" );
         System.out.println("Connect to database successfully");
     }catch(Exception e){}
 }
 
 static class PDFManager {

	private PDFParser parser;
	private PDFTextStripper pdfStripper;
	private PDDocument pdDoc;
	private COSDocument cosDoc;

    private String Text;
    private String filePath;
    private File file;
	    

    public PDFManager() {}

	public String toText() throws IOException {
		this.pdfStripper = null;
	    this.pdDoc = null;
	    this.cosDoc = null;

	    file = new File(filePath);
	    parser = new PDFParser(new RandomAccessFile(file, "r")); // update for PDFBox V 2.0

	    parser.parse();
	    cosDoc = parser.getDocument();
	    pdfStripper = new PDFTextStripper();
	    pdDoc = new PDDocument(cosDoc);
	    pdDoc.getNumberOfPages();
	    pdfStripper.setStartPage(0);
	    pdfStripper.setEndPage(pdDoc.getNumberOfPages());
	    Text = pdfStripper.getText(pdDoc);
	    return Text;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public PDDocument getPdDoc() {
		return pdDoc;
	}
	    
	public void pdf(String PDF) {
		PDFManager pdfManager = new PDFManager();
	    pdfManager.setFilePath(PDF);
	    try {
	    	String text = pdfManager.toText();
	        Frame f = new Frame();
	        TextArea discuss = new TextArea();
	        discuss.setBounds(50,50,400,400);
	    	discuss.setText(text);
	    	discuss.setEditable(false);
	    	f.add(discuss);
	            
	            
            f.setSize(500,500);                             //frame declared
	   		f.setLayout(null);  
	    	f.setVisible(true);
	           
	        } catch (IOException ex) {}
    }
}
	
 static class library_front implements ActionListener {
	
	Frame f;
	TextField email;
	JPasswordField password;
	Button login,create,exit;
	Label l,l1,l2,l3;
	Font f1,f2;
	String result="";
	
	library_front() {
		
	    f = new Frame("Digital Library");                  //Frame declaration
	    f1 = new Font("Times of Roman", Font.PLAIN, 22);
		f2 = new Font("Times of Roman", Font.PLAIN, 16);
		l = new Label("SignIn to Your Account");           // Label  declared and added to login frame 
		l.setBounds(120, 50, 250, 30);
		l.setFont(f1);
		f.add(l);
		
		exit = new Button("Exit");
		exit.setBounds(380,40,100,30);
		exit.addActionListener(this);
		f.add(exit);
		
		email = new TextField();            			   // Email Text field declared and added to frame  
		email.setBounds(190,150,200,30);
		f.add(email);
		
		password = new JPasswordField();                  //password Password field declared and added to frame
		password.setBounds(190, 190, 200, 30);
		f.add(password);
		
		login = new Button("Login");                      //login button added to frame
		login.setBounds(180,280,100,30);
		login.addActionListener(this);
		f.add(login);
		
		create = new Button("Create Account");            //Create button added to frame 
		create.setBounds(290,280,100,30);
		create.addActionListener(this);
		f.add(create);
		
		l1 = new Label("Enter email : ");                //Label added to frame
		l1.setBounds(20, 150, 150, 30);
		l1.setFont(f2);
		f.add(l1);
		
		l2 = new Label("Enter Password");                //Label added to frame 
		l2.setBounds(20, 190, 150, 30);
		l2.setFont(f2);
		f.add(l2);
		
		f.setSize(500,500);                             //frame declared
		f.setLayout(null);  
		f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == login) {
			String user = email.getText();
			result = user;
			String pwd = new String(password.getPassword());
			menu = new library_menu(result);
			f.setVisible(false);
			if(user.contains("@gmail.com") && pwd!="") {
				result = user;
				email.setText("");
				password.setText("");
				f.setVisible(false);
				return;
			}
			else {
				email.setText("");
				password.setText("");
			}
		}
		else if(e.getSource() == create) {
			f.setVisible(false);
		    crt = new library_create();
			return ;
		}
		else if(e.getSource() == exit) {
			f.dispose();
		}
		return ;
		
	}
}

 static class library_create extends Frame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TextField name,email;
	JPasswordField password,re_password;
	Button back,create;
	Label l,l1,l2,l3,l4;
	Font f1,f2;
	boolean disable=false;
	
	library_create() {
		
		f1 = new Font("Times of Roman", Font.PLAIN, 20);
		f2 = new Font("Times of Roman", Font.PLAIN, 14);
		
		l = new Label("Creat Your Account");
		l.setBounds(150, 50, 200, 30);
		l.setFont(f1);
		add(l);
		
		name = new TextField();
		name.setBounds(190,150,200,30);
		add(name);
		
		email = new TextField();
		email.setBounds(190, 190, 200, 30);
		add(email);
		
		password = new JPasswordField ();
		password.setBounds(190,230,200,30);
		add(password);
		
		re_password = new JPasswordField ();
		re_password.setBounds(190,270,200,30);
		add(re_password);
		
		back = new Button("Back");
		back.setBounds(180,320,100,30);
		back.addActionListener(this);
		add(back);
		
		create = new Button("create");
		create.setBounds(290,320,100,30);
		create.addActionListener(this);
		add(create);
		
		l1  = new Label("Enter Name");
		l1.setBounds(20, 150, 150, 30);
		l1.setFont(f2);
		add(l1);
		
		l2 = new Label("Enter email : ");
		l2.setBounds(20, 190, 150, 30);
		l2.setFont(f2);
		add(l2);
		
		l3 = new Label("Enter Password");
		l3.setBounds(20, 230, 150, 30);
		l3.setFont(f2);
		add(l3);
		
		l4 = new Label("Reapet Password");
		l4.setBounds(20,270,150,30);
		l4.setFont(f2);
		add(l4);
		
		setSize(500,500);
		setLayout(null);  
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == back) {
			disable = true;
			dispose();
		}
		
		else if(e.getSource() == create) {
			String user = email.getText();
			try {
				long long_pwd = Long.valueOf(new String(password.getPassword()));
				long long_re_pwd = Long.valueOf(new String(re_password.getPassword()));
				if(user.contains("@gmail.com")) {
					if(long_pwd==long_re_pwd) {
						disable = true;
						dispose();
						return ;
					}
					else {
						password.setText("");
						re_password.setText("");
					}
				}	
				
				else 
					email.setText("");
			}
			catch(NumberFormatException exc) {
				password.setText("");
				re_password.setText("");
			}
			
		}
		
	}
}

 static class library_menu implements ActionListener {
	Frame f;
	String user_name;
	Label name,discuss_panel;
	Button discussion,logout,send;
	Font f1,f2,f3;
	TextArea discuss;
	TextField text;
	
	library_menu(String user){
		
		user_name = new String(user);
		f = new Frame("Digital Library");
		
		f1 = new Font("Times of Roman", Font.PLAIN, 20);
		f2 = new Font("Times of Roman", Font.PLAIN, 16);
		
		name  = new Label(user);
		name.setBounds(10,50,110,30);
		name.setFont(f1);
		f.add(name);
		
		logout = new Button("Log Out");
		logout.setBounds(380,50,100,30);
		logout.addActionListener(this);
		f.add(logout);
		
		discuss_panel = new Label("Discussion");
		discuss_panel.setBounds(290,120,160,30);
		discuss_panel.setFont(f1);
		f.add(discuss_panel);
		
		discuss = new TextArea();
		discuss.setBounds(290,150,200,300);
		discuss.setText(get_discussion());
		discuss.setEditable(false);
		f.add(discuss);
		
		text = new TextField();
		text.setBounds(290,460,150,30);
		f.add(text);
		
		send = new Button("send");
		send.setBounds(450,460,50,30);
		send.addActionListener(this);
		f.add(send);
				
		f.setSize(500,500);
		f.setLayout(null);
		f.setVisible(true);
	}
	
	String get_discussion() {
		String discussion  = new String("");
		try {
			InputStream is = new FileInputStream("./discussion.txt");
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			while(line != null) {
				sb.append(line).append("\n");
				line = buf.readLine(); 
			} 
			discussion = sb.toString();
			buf.close();
		}catch(IOException error) {}
		catch(ArrayIndexOutOfBoundsException error) {}
		return discussion;
	}
	
	public void actionPerformed(ActionEvent e) {
			if (e.getSource() == name) {
				name.setText("Clicked"); 
			}
			else if(e.getSource() == logout) {
				menu.f.dispose();
				front.f.setVisible(true);
				return;
			}
			else if(e.getSource() == send) {
				String discussion  = new String("");
				String chat = new String(text.getText());
				if(!chat.equals("")) {
					text.setText("");
					try {
						chat ="\n"+user_name+" : "+chat+"\n";
						discussion = get_discussion();
						discussion += chat;
						discuss.setText(discussion);
						FileWriter fw = new FileWriter("./discussion.txt");
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(discussion);
						bw.close();
					} 
					catch(IOException error) {}
					catch(ArrayIndexOutOfBoundsException error) {}
				}
			}
	}
}

 public static void main(String[] args) throws IOException{
	 pdf = new PDFManager();
	 pdf.pdf("pdf.pdf");
	 DbConnection();
 }	 
}