package Lab06;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI {
    public WindowListener Window;
    public JPanel FilterPanel;
    public JFrame BookList;
    public JButton Add, Delete, Edit, Filter, Save, Open, OpenXML;
    public JToolBar ToolBar;
    public JScrollPane ScrollPane, FilteredScrollPane;
    public JTable Books, FilteredBooks;
    public JComboBox Author;
    public JTextField TAuthor, TBookName, TAvailability, TRating, BookName;
    public String NewAuthor;
    public String[] NewAuthorString, NewBook;
    public DefaultTableModel Model, FilteredModel;
    public String[][] Data = {{"J.K. Rowling", "Harry Potter and the Prisoner of Azkaban", "9", "In stock"},
            {"Stephen King", "Green Mile", "9", "Out of stock"},
            {"Margaret Mitchell", "Gone with the Wind", "7", "In Stock"},
            {"Arthur Conan Doyle", "The Adventures of Sherlock Holmes", "8", "In stock"},
            {"Nora Sakovich", "Retinue of the King", "9", "Out of stock"},
            {"Alexandre Dumas", "Count of Monte Cristo", "8", "In stock"},
            {"George Martin", "Storm of Swords", "7", "In stock"},
            {"Mario Puzo", "The Godfather", "7", "In stock"},
            {"Fredrik Bakman", "The Second Life of Uwe", "8", "Out of stock"},
            {"Agatha Christie", "Ten Little Niggers", "9", "Out of stock"}
    };
    public String[] AuthorString = {"Author",
            "J.K. Rowling", "Stephen King", "Margaret Mitchell", "Nora Sakovich", "Alexandre Dumas",
            "George Martin", "Mario Puzo", "Fredrik Bakman", "Agatha Christie"
    };
    public int BookCount = 10;

    public void CreatingGUI() {
        //Creating a window
        BookList = new JFrame("Library");
        BookList.setSize(800, 600);
        BookList.setLocation(590, 240);
        BookList.setVisible(true);
        BookList.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        BookList.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int Result = JOptionPane.showConfirmDialog(
                        BookList,
                        "Are you sure you want to exit the application?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);
                if (Result == JOptionPane.YES_OPTION) BookList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        //Creating a Save button
        Image SaveIcon = new ImageIcon("./img/save1.png").getImage();
        Save = new JButton(new ImageIcon(SaveIcon));
        Save.setToolTipText("Save");

        //Creating a Open button
        Image OpenIcon = new ImageIcon("./img/open.png").getImage();
        Open = new JButton(new ImageIcon(OpenIcon));
        Open.setToolTipText("Open");

        //Creating a OpenXML button
        Image OpenXMLIcon = new ImageIcon("./img/openXML.png").getImage();
        OpenXML = new JButton(new ImageIcon(OpenXMLIcon));
        OpenXML.setToolTipText("Open");

        //Creating an Add button
        Image AddIcon = new ImageIcon("./img/add1.png").getImage();
        Add = new JButton(new ImageIcon(AddIcon));
        Add.setToolTipText("Add");

        //Creating an Edit button
        Image EditIcon = new ImageIcon("./img/edit1.png").getImage();
        Edit = new JButton(new ImageIcon(EditIcon));
        Edit.setToolTipText("Edit");

        //Creating a Delete button
        Image DeleteIcon = new ImageIcon("./img/delete1.png").getImage();
        Delete = new JButton(new ImageIcon(DeleteIcon));
        Delete.setToolTipText("Delete");

        //Creating a ToolBar
        ToolBar = new JToolBar("Toolbar");
        ToolBar.add(Add);
        ToolBar.add(Edit);
        ToolBar.add(Delete);
        ToolBar.add(Save);
        ToolBar.add(Open);
        ToolBar.add(OpenXML);

        //Placement of the toolbar
        BookList.setLayout(new BorderLayout());
        BookList.add(ToolBar, BorderLayout.NORTH);

        TAuthor = new JTextField("Author");
        TAuthor.setPreferredSize(new Dimension(120, 20));

        TBookName = new JTextField("Book");
        TBookName.setPreferredSize(new Dimension(120, 20));

        TRating = new JTextField("Rating");
        TRating.setPreferredSize(new Dimension(50, 20));

        TAvailability = new JTextField("Availability");

        CreatingDataTable();

        //Placement of the data table
        BookList.add(ScrollPane, BorderLayout.CENTER);
        JPanel NewBookPanel = new JPanel();
        NewBookPanel.add(TAuthor);
        NewBookPanel.add(TBookName);
        NewBookPanel.add(TRating);
        NewBookPanel.add(TAvailability);
        ToolBar.add(NewBookPanel);

        //Adding components to the panel
        JPanel FilterPanel = new JPanel();
        FilterPanel.add(Author);
        FilterPanel.add(BookName);
        FilterPanel.add(Filter);

        //Placement of panel
        BookList.add(FilterPanel, BorderLayout.SOUTH);
        BookList.setVisible(true);
    }

    private void CreatingDataTable() {
        //Creating a data table
        String[] Columns = {"Author", "Book", "Rating", "Availability"};
        Model = new DefaultTableModel(Data, Columns);
        Books = new JTable(Model);
        ScrollPane = new JScrollPane(Books);

        //Preparing for a drop-down list
        Author = new JComboBox<>(AuthorString);
        BookName = new JTextField("Book name");
        Filter = new JButton("Search");
    }
}
