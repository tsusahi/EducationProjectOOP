package Lab07;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Interface {
    public JPanel filterPanel;
    public JFrame bookList;
    public JButton add, delete, edit, filter, save, open, openXML;
    public JToolBar toolBar;
    public JScrollPane scrollPane, filteredScrollPane;
    public JTable books, filteredBooks;
    public JComboBox author;
    public JTextField textFieldAuthor, textFieldBookName, textFieldAvailability, textFieldRating, panelBookName;
    public String newAuthor;
    public String[] newAuthorString, newBook;
    public DefaultTableModel model, filteredModel;
    public String[][] data = {{"J.K. Rowling", "Harry Potter and the Prisoner of Azkaban", "9", "In stock"},
            {"Stephen King", "Green Mile", "9", "Out of stock"},
            {"Margaret Mitchell", "Gone with the Wind", "7", "In Stock"},
            {"Arthur Conan Doyle", "The Adventures of Sherlock Holmes", "8", "In stock"},
            {"Nora Sakovich", "Retinue of the King", "9", "Out of stock"},
            {"Alexandre Dumas", "Count of Monte Cristo", "8", "In stock"},
            {"George Martin", "Storm of Swords", "7", "In stock"},
            {"Mario Puzo", "The Godfather", "7", "In stock"},
            {"Fredrik Bakman", "The Second Life of Uwe", "8", "Out of stock"},
            {"Agatha Christie", "Ten Little Niggers", "9", "Out of stock"},
            {},
    };
    public String[] authorString = {"Author",
            "J.K. Rowling", "Stephen King", "Margaret Mitchell", "Nora Sakovich", "Alexandre Dumas",
            "George Martin", "Mario Puzo", "Fredrik Bakman", "Agatha Christie"
    };
    public int bookCount = 10;

    public void creatingInterface() {
        //Creating a window
        bookList = new JFrame("Library");
        bookList.setSize(800, 600);
        bookList.setLocation(590, 240);
        bookList.setVisible(true);
        bookList.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        bookList.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int Result = JOptionPane.showConfirmDialog(
                        bookList,
                        "Are you sure you want to exit the application?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);
                if (Result == JOptionPane.YES_OPTION) bookList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        //Creating a Save button
        Image saveIcon = new ImageIcon("./img/save1.png").getImage();
        save = new JButton(new ImageIcon(saveIcon));
        save.setToolTipText("Save");

        //Creating Open button
        Image openIcon = new ImageIcon("./img/open.png").getImage();
        open = new JButton(new ImageIcon(openIcon));
        open.setToolTipText("Open");

        //Creating a OpenXML button
        Image openXMLIcon = new ImageIcon("./img/openXML.png").getImage();
        openXML = new JButton(new ImageIcon(openXMLIcon));
        openXML.setToolTipText("Open");

        //Creating an Add button
        Image addIcon = new ImageIcon("./img/add1.png").getImage();
        add = new JButton(new ImageIcon(addIcon));
        add.setToolTipText("Add");

        //Creating an Edit button
        Image editIcon = new ImageIcon("./img/edit1.png").getImage();
        edit = new JButton(new ImageIcon(editIcon));
        edit.setToolTipText("Edit");

        //Creating a Delete button
        Image deleteIcon = new ImageIcon("./img/delete1.png").getImage();
        delete = new JButton(new ImageIcon(deleteIcon));
        delete.setToolTipText("Delete");

        //Creating a ToolBar
        toolBar = new JToolBar("Toolbar");
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(delete);
        toolBar.add(save);
        toolBar.add(open);
        toolBar.add(openXML);

        //Placement of the toolbar
        bookList.setLayout(new BorderLayout());
        bookList.add(toolBar, BorderLayout.NORTH);

        textFieldAuthor = new JTextField("Author");
        textFieldAuthor.setPreferredSize(new Dimension(120, 20));

        textFieldBookName = new JTextField("Book");
        textFieldBookName.setPreferredSize(new Dimension(120, 20));

        textFieldRating = new JTextField("Rating");
        textFieldRating.setPreferredSize(new Dimension(50, 20));

        textFieldAvailability = new JTextField("Availability");

        creatingDataTable();

        //Placement of the data table
        bookList.add(scrollPane, BorderLayout.CENTER);
        JPanel newBookPanel = new JPanel();
        newBookPanel.add(textFieldAuthor);
        newBookPanel.add(textFieldBookName);
        newBookPanel.add(textFieldRating);
        newBookPanel.add(textFieldAvailability);
        toolBar.add(newBookPanel);

        //Adding components to the panel
        JPanel filterPanel = new JPanel();
        filterPanel.add(author);
        filterPanel.add(panelBookName);
        filterPanel.add(filter);

        //Placement of panel
        bookList.add(filterPanel, BorderLayout.SOUTH);
        bookList.setVisible(true);
    }

    private void creatingDataTable() {
        //Creating a data table
        String[] columns = {"Author", "Book", "Rating", "Availability"};
        model = new DefaultTableModel(data, columns);
        books = new JTable(model);
        scrollPane = new JScrollPane(books);

        //Preparing for a drop-down list
        author = new JComboBox<>(authorString);
        panelBookName = new JTextField("Book name");
        filter = new JButton("Search");
    }
}
