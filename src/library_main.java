import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;	
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class library_main {
 static library_front front;
 static library_menu menu ;
 static library_create crt;
 static PDFManager pdf;
 
 static DB db;

 static String user_name = new String("Vinay");
 

 static public void DbConnection() {
	 try{   
		 MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

         db = mongoClient.getDB( "library" );
         System.out.println("Connect to database successfully");
     }catch(Exception e){}
 }
 
 static class PDFManager implements ActionListener {

	private PDFParser parser;
	private PDFTextStripper pdfStripper;
	private PDDocument pdDoc;
	private COSDocument cosDoc;
    private JTextField discussText;
    private String Text;
    private String filePath;
    private File file;
    JFrame f;
    TextArea discuss;
    String discussion_path;
    JButton back,send;
    
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
	String get_discussion1(String duscussionPath) {
		String discussion  = new String("");
		try {
			InputStream is = new FileInputStream(duscussionPath);
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
	    
	public void pdf(String PDF,String discussionPath) {
		discussion_path = new String(discussionPath);
		PDFManager pdfManager = new PDFManager();
	    pdfManager.setFilePath(PDF);
	    try {
	    	String text = pdfManager.toText();
	        f = new JFrame();
	        back = new JButton("Back");
	        back.setBounds(880,50,100,30);
	        back.addActionListener(this);
			f.add(back);
			
			JLabel discuss_panel = new JLabel("Discussions");
			discuss_panel.setBounds(690,120,160,30);
			f.add(discuss_panel);
			
			discuss = new TextArea();
			discuss.setBounds(690,150,300,400);
			discuss.setText(get_discussion1(discussionPath));
			discuss.setEditable(false);
			f.add(discuss);
			
			discussText = new JTextField();
			discussText.setBounds(690,560,230,30);
			f.add(discussText);
	        
			send = new JButton("send");
			send.setBounds(940,560,50,30);
			send.addActionListener(this);
			f.add(send);
			
	        TextArea discuss1 = new TextArea(600,550);
	        discuss1.setBounds(50,50,600,550);
	    	discuss1.setText(text);
	    	discuss1.setEditable(false);
	    	f.add(discuss1);
	        
            f.setSize(1000,700);                             //frame declared
	   		f.getContentPane().setLayout(null);  
	    	f.setVisible(true);
	           
	        } catch (IOException ex) {}
    }
	public void actionPerformed(ActionEvent e)  {
		if(e.getSource() == back) {
			f.setVisible(false);
			menu.frame.setVisible(true);
		}
		else if(e.getSource() == send) {
			String discussion  = new String("");
			  String chat = new String(discussText.getText());
			  if(!discussText.getText().isBlank()) {
				  discussText.setText("");
			  	try {
				  	chat ="\n"+user_name+" : "+chat+"\n";
			  		discussion = get_discussion1(discussion_path);
			  		discussion += chat;
			  		discuss.setText(discussion);
			  		FileWriter fw = new FileWriter(discussion_path);
			  		BufferedWriter bw = new BufferedWriter(fw);
			  		bw.write(discussion);
			  		bw.close();
			  	} 
			  	catch(Exception error) {System.out.println(error);}
			  }
		}
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
		try {
			String user = email.getText();
			String pwd = new String(password.getPassword());
			DBCollection coll = db.getCollection("users");
			BasicDBObject login = new BasicDBObject();
			ArrayList<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			obj.add(new BasicDBObject("email", user));
			obj.add(new BasicDBObject("password", pwd));
			login.put("$and", obj);
			DBObject OBJ = coll.findOne(login);
			if(OBJ.get("name")!=null) {
				user_name = new String((String) OBJ.get("name"));
				email.setText("");
				password.setText("");
				menu = new library_menu();
				f.setVisible(false);
			}
			else {
				email.setText("");
				password.setText("");
				f.setVisible(false);
			}
		}
			catch(Exception error) {
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
			name.setText("");
			password.setText("");
			re_password.setText("");
			email.setText("");
			setVisible(false);
			front.f.setVisible(true);
		}
		
		else if(e.getSource() == create) {
			String user = name.getText();
			String mail = email.getText();
			String pass = new String(password.getPassword());
			String re_pass = new String(re_password.getPassword());
			
			try {
				if(user.equals("")) 
					email.setText("");
				else {
					if(mail.contains("@gmail.com")) {
						if(pass.equals(re_pass)) {
							DBCollection coll = db.getCollection("users");
							BasicDBObject whereQuery = new BasicDBObject();
							whereQuery.put("email", mail);
							DBCursor cursor = coll.find(whereQuery);
							if(cursor.count()==0) {
								BasicDBObject doc = new BasicDBObject("name", user)
								          .append("email", mail)
								          .append("password", pass);
								coll.insert(doc);
								name.setText("");
								password.setText("");
								re_password.setText("");
								email.setText("");
								setVisible(false);
								front.f.setVisible(true);
							}
							else {
								 password.setText("");
								 re_password.setText("");
								 email.setText("");
							}
						}
						else {
							 name.setText("");
							 password.setText("");
							 re_password.setText("");
							 email.setText("");
						}
					}
					else {
						password.setText("");
						re_password.setText("");
					}
				}
					
			}
			catch(NumberFormatException exc) {
				password.setText("");
				re_password.setText("");
			}
			catch(Exception exc) {
				name.setText("");
				email.setText("");
				password.setText("");
				re_password.setText("");
			}
		}
		
	}
}

 
 static class library_menu implements ActionListener {
	private JFrame frame;
	private JLabel name,discuss_panel;
	private Font f1;
	private TextArea discuss;
	private JTextField text;
	private TextField textField;
	private JButton logout,send,btnSearch;
    private JPanel panel1,panel2,panel3;
    private JButton[] allbtn = new JButton[14];
    private String[] famous_books = new String[10];
    private String[] search = new String[5];
    private String[] all_books = new String[100];
    private int total_search = 0;
    private JButton[] famousbtn = new JButton[10];
    private JButton[] searchbtn = new JButton[5];
    private JTabbedPane tabbedPane;	
	
	public void all_books() {
		 try {
			 
			 DBCollection coll = db.getCollection("pdf");
			 BasicDBObject allQuery = new BasicDBObject();
			 BasicDBObject fields = new BasicDBObject();
			 fields.put("name", 1);
			 DBCursor cursor = coll.find(allQuery, fields);
			 int i=-1;
			 while (cursor.hasNext()) {
				 i++;
				 DBObject obj = cursor.next();
				 all_books[i] = (String) obj.get("name");
			 }
		 }catch (Exception e) {
			 System.out.print("Error Occured...!");
		 }
		 
	 }

	 public void famous_books() {
		 
			DBCollection coll = db.getCollection("pdf");
			DBCursor car = coll.find(new BasicDBObject(),new BasicDBObject("name",1)).sort(new BasicDBObject("views", -1));
			int i=0;
			try {
				while (car.hasNext()) {
					if(i<10) {
						DBObject obj = car.next();
						famous_books[i++] = (String) obj.get("name"); 
					}
				
					else
						break;
				}
			} finally {
				car.close();
			}
	}
	 
	 
	public void search(String book_name) {
			 try {
				 DBCollection coll = db.getCollection("pdf");
				 BasicDBObject query = new BasicDBObject();
				 query.put("$search",book_name);
				 DBCursor cursor = coll.find(new BasicDBObject("$text",query));
				 int i=0;
				 total_search=0;
				 while(cursor.hasNext()) {
					 DBObject obj = cursor.next();
					 total_search++;
					 search[i++] = (String)obj.get("name");
				 }
			 }
			 catch (Exception e) {
				 System.out.println(e);
			 }
		 } 

	public JPanel add_famous_books() {
		JPanel panel = new JPanel();
		 int k=0,height=70;
		 
		 for(int i=0;i<5;i++) {
			 JButton btn = new JButton(famous_books[k]);
			 send = new JButton("send");
				send.setBounds(950,560,50,30);
				send.addActionListener(this);
				frame.add(send);btn.setBounds(20, 42+(i*height), 250,30);
			 btn.addActionListener(this);
			 famousbtn[k] = btn;
			 panel.add(famousbtn[k]);
			 k++;
		 }
		 for(int i=0;i<5;i++) {
			 JButton btn = new JButton(famous_books[k]);
			 btn.setBounds(315, 42+(i*height), 250,30);
			 btn.addActionListener(this);
			 famousbtn[k] = btn;
			 panel.add(famousbtn[k]);
			 k++;
		 }
		 return panel;
	 }
	
	public JPanel add_all_books() {
		JPanel panel = new JPanel();
		 int k=0,height=55;
		 
		 for(int i=0;i<7;i++) {
			 JButton btn = new JButton(all_books[k]);
			 btn.setBounds(20, 25+(i*height), 250,30);
			 btn.addActionListener(this);
			 allbtn[k] = btn;
			 panel.add(allbtn[k]);
			 k++;
		 }
		 for(int i=0;i<7;i++) {
			 JButton btn = new JButton(all_books[k]);
			 btn.setBounds(315, 25+(i*height), 250,30);
			 btn.addActionListener(this);
			 allbtn[k] = btn;
			 panel.add(allbtn[k]);
			 k++;
		 }
		 return panel;
	}
	public JPanel add_search_books() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		 int k=0,discussTextheight=40;
		 
		 for(int i=0;i<total_search;i++) {
			 JButton btn = new JButton(search[k]);
			 btn.setBounds(20,70+(i*discussTextheight), 250,30);
			 btn.addActionListener(this);
			 searchbtn[k] = btn;
			 panel.add(searchbtn[k]);
			 k++;
		 }
		 return panel;
	}
	
	library_menu(){
		
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		
		f1 = new Font("Times of Roman", Font.PLAIN, 20);
		
		name  = new JLabel(user_name);
		name.setBounds(10,50,110,30);
		name.setFont(f1);
		frame.add(name);
		
		logout = new JButton("Log Out");
		logout.setBounds(880,50,100,30);
		logout.addActionListener(this);
		frame.add(logout);
		
		discuss_panel = new JLabel("Discussions");
		discuss_panel.setBounds(690,120,160,30);
		discuss_panel.setFont(f1);
		frame.add(discuss_panel);
		
		discuss = new TextArea();
		discuss.setBounds(690,150,300,400);
		discuss.setText(get_discussion());
		discuss.setEditable(false);
		frame.add(discuss);
		
		text = new JTextField();
		text.setBounds(690,560,250,30);
		frame.add(text);
		
		send = new JButton("send");
		send.setBounds(950,560,50,30);
		send.addActionListener(this);
		frame.add(send);
				
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(32, 111, 585, 445);
		frame.getContentPane().add(tabbedPane);
		
		panel1 = new JPanel();
		famous_books();
		panel1 = add_famous_books();
		tabbedPane.addTab("Famous Books", null, panel1, null);
		panel1.setLayout(null);
		
		
		panel2 = new JPanel();
		all_books();
		panel2 = add_all_books();
		tabbedPane.addTab("All Books", null, panel2, null);
		panel2.setLayout(null);
		
		panel3 = new JPanel();
		tabbedPane.addTab("Search", null, panel3, null);
		panel3.setLayout(null);
		
		textField = new TextField();
		textField.setBounds(20, 20, 250, 30);
		panel3.add(textField);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(this);
		btnSearch.setBounds(275, 20, 93, 30);
		panel3.add(btnSearch);
		
		frame.setVisible(true);
		frame.setSize(1000,700);
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
	
	public void setSearchPanel() {
		try {
		tabbedPane.remove(panel3);
		panel3 = add_search_books();
		textField = new TextField();
		textField.setBounds(20, 20, 250, 30);
		panel3.add(textField);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(this);
		btnSearch.setBounds(275, 20, 93, 30);
		panel3.add(btnSearch);
		tabbedPane.addTab("Search", null, panel3, null);
		tabbedPane.setSelectedIndex(2);
		}catch(Exception e) {
			System.out.print(e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e. getSource();
		JButton b = null;
		b = (JButton)o;
		
		switch(b.getText()) {
		case "Log Out" : menu.frame.dispose();
						 return;
		case "send" : String discussion  = new String("");
					  String chat = new String(text.getText());
					  if(!text.getText().isBlank()) {
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
					   break;
		case "Search" : if(!textField.getText().isBlank()) {
							String book_name = textField.getText();
							search(book_name);
							setSearchPanel();
						}
						break;
		default : String book = b.getText();
				  DBCollection coll = db.getCollection("pdf");
				  BasicDBObject query = new BasicDBObject();
			      query.put("name",new BasicDBObject("$eq",book));
			      DBObject obj = coll.findOne(query);
			      coll.update(query,new BasicDBObject("$inc",new BasicDBObject("views",1)));
			      pdf.pdf((String)obj.get("url"),(String)obj.get("discussion"));
			      frame.setVisible(false);
		}
	}
}

 public static void main(String[] args) throws IOException{
	 DbConnection();
	 pdf = new PDFManager();
	 front = new library_front();
 }	 

}