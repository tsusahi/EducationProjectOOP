package Lab04;

import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

public class GUI {
    //Declaring graphic classes
    private JFrame BookList;
    private DefaultTableModel Model, Model_1;
    private JButton Save, Add, Delete, Edit, Filter;
    private JToolBar ToolBar;
    private JScrollPane ScrollBar, ScrollBar_1;
    private JTable Books, Books_1;
    private JComboBox Author;
    private JTextField BookName, NewBookName, NewBookAuthor, NewBookAvailability, NewRating;
    private int BookCount = 10;
    private String NewAuthor;
    private String[] AuthorString, NewAuthorString, NewBook;
    private String[][] Data = {{"Джоан Роулинг","Гарри Поттер и узник Азкабана","4,8","В наличии"},
            {"Стивен Кинг","Зеленая миля","4,7","Нет в наличии"},
            {"Маргарет Митчелл","Унесенные ветром","4,7","В наличии"},
            {"Артур Конан Дойл","Приключения Шерлока Холмса","4,8","В наличии"},
            {"Нора Сакавич","Свита короля","4,7","Нет в наличии"},
            {"Александр Дюма","Граф Монте-Кристо","4,7","В наличии"},
            {"Джордж Мартин","Буря мечей","4,6","В наличии"},
            {"Марио Пьюзо","Крестный отец","4,6","В наличии"},
            {"Фредрик Бакман","Вторая жизнь Уве","4,7","Нет в наличии"},
            {"Агата Кристи","Десять негритят","4,6","Нет в наличии"}
    };

    public static <T> T[] append(T[] Arr, T Element) {
        T[] Array = Arrays.copyOf(Arr, Arr.length + 1);
        Array[Arr.length] = Element;
        return Array;
    }

    private static class NameException extends Exception {
        public NameException() {
            super ("No search name entered");
        }
    }

    private static class AuthorException extends Exception {
        public AuthorException() {
            super ("Author of the book has not been entered");
        }
    }

    private static class InputFieldsException extends Exception {
        public InputFieldsException() {
            super ("Not all fields were filled in");
        }
    }

    private void CheckName (JTextField BookName) throws NameException
    {
        String Name = BookName.getText();
        if (Name.contains("Book name")) throw new NameException();
        if (Name.length() == 0) throw new NameException();
    }

    private void CheckAuthor (JComboBox Author) throws AuthorException
    {
        String Item = String.valueOf(Author.getSelectedItem());
        if (Item.equals("Author")) throw new AuthorException();
    }

    private void CheckInputFields   (JTextField Author, JTextField BookName, JTextField Rating, JTextField Availability) throws InputFieldsException
    {
        String Author_str = Author.getText();
        String BookName_str = BookName.getText();
        String Rating_str = Rating.getText();
        String Availability_str = Availability.getText();
        if (Author_str.length() == 0 || BookName_str.length() == 0 || Rating_str.length() == 0 || Availability_str.length() == 0) throw new InputFieldsException();
        if (Author_str.contains("Author")|| BookName_str.contains("Book name") || Rating_str.contains("Availability")) throw new InputFieldsException();
    }

    public void Show() {
        //Creating graphic objects

        //Creating a window
        BookList = new JFrame("Library");
        BookList.setSize(800, 600);
        BookList.setLocation(590,240);
        BookList.setVisible(true);

        //Creating a Save button
        Save = new JButton(new ImageIcon("./img/save.jpg"));
        Save.setToolTipText("Save");

        //Creating a Add button
        Add = new JButton(new ImageIcon("./img/add.jpg"));
        Add.setToolTipText("Add");

        //Creating a Edit button
        Edit = new JButton(new ImageIcon("./img/edit.jpg"));
        Edit.setToolTipText("Edit");

        //Creating a Delete button
        Delete = new JButton(new ImageIcon("./img/delete.jpg"));
        Delete.setToolTipText("Delete");

        //Creating a ToolBar
        ToolBar = new JToolBar("Toolbar");
        ToolBar.add(Save);
        ToolBar.add(Add);
        ToolBar.add(Edit);
        ToolBar.add(Delete);

        //Placement of the toolbar
        BookList.setLayout(new BorderLayout());
        BookList.add(ToolBar, BorderLayout.NORTH);

        //Creating a data table
        String[] Columns = {"Author", "Book", "Rating", "Availability"};

        Model = new DefaultTableModel(Data, Columns);
        Books = new JTable(Model);
        ScrollBar = new JScrollPane(Books);

        NewBookAuthor = new JTextField("Author");
        NewBookAuthor.setPreferredSize(new Dimension(120, 20));

        NewBookName = new JTextField("Book");
        NewBookName.setPreferredSize(new Dimension(120, 20));

        NewRating = new JTextField("Rating");
        NewRating.setPreferredSize(new Dimension(50,20));

        NewBookAvailability = new JTextField("Availability");

        //Placement of the data table
        BookList.add(ScrollBar, BorderLayout.CENTER);
        JPanel NewBookPanel = new JPanel();
        NewBookPanel.add(NewBookAuthor);
        NewBookPanel.add(NewBookName);
        NewBookPanel.add(NewRating);
        NewBookPanel.add(NewBookAvailability);
        ToolBar.add(NewBookPanel);

        //Preparing for a drop-down list
        AuthorString = new String[] {"Author",
                "Джоан Роулинг","Стивен Кинг","Маргарет Митчелл","Нора Сакавич","Александр Дюма",
                "Джордж Мартин","Артур Конан Дойл","Марио Пьюзо","Фредрик Бакман","Агата Кристи"};
        Author = new JComboBox(AuthorString);
        BookName = new JTextField("Book name");
        Filter = new JButton("Search");

        //Adding components to the panel
        JPanel FilterPanel = new JPanel();
        FilterPanel.add(Author);
        FilterPanel.add(BookName);
        FilterPanel.add(Filter);

        //Placement of panel
        BookList.add(FilterPanel, BorderLayout.SOUTH);

        //Adding new book
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewBook = new String[] {
                        NewBookAuthor.getText(),
                        NewBookName.getText(),
                        NewRating.getText(),
                        NewBookAvailability.getText(),
                };

                try {
                    CheckInputFields(NewBookAuthor, NewBookName, NewRating, NewBookAvailability);
                    Data = append(Data, NewBook);
                    Model.addRow(NewBook);
                    NewAuthor = NewBook[1];
                    int flag = 0;

                    for (String s : AuthorString) {
                        if (NewAuthor.equals(s)) {
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        NewAuthorString = new String[AuthorString.length + 1];
                        System.arraycopy(AuthorString, 0, NewAuthorString, 0, AuthorString.length);
                        NewAuthorString[AuthorString.length] = NewAuthor;
                        AuthorString = NewAuthorString;
                        FilterPanel.remove(Author);
                        Author = new JComboBox(AuthorString);
                        FilterPanel.add(Author, 0);
                        BookList.setVisible(true);
                    }

                    BookCount += 1;

                } catch (InputFieldsException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        //Save book
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try(FileWriter writer = new FileWriter("SavedBookList.txt", false)) {
                    for (int i = 0; i < BookCount; i++) {
                        for (int j = 0; j < 4; j++) {
                            writer.write(Data[i][j]);
                            writer.write(";");
                        }
                        writer.write("\n");
                    }
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //Delete book
        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int RemoveRowIndex = Books.getSelectedRow();
                int NewBookCount = 0;

                if (RemoveRowIndex > -1 & RemoveRowIndex < BookCount) {
                    String AuthorNameToDelete = Data[RemoveRowIndex][1];
                    int flag = 0;
                    Model.removeRow(RemoveRowIndex);
                    String[][] NewData = new String[BookCount - 1][];

                    for (int i = 0; i < BookCount; i++) {
                        if (i != RemoveRowIndex) {
                            NewData[NewBookCount] = Data[i];
                            NewBookCount += 1;
                        }
                    }

                    Data = NewData;
                    BookCount -= 1;

                    for (int i = 0; i < BookCount; i++) {
                        if (AuthorNameToDelete.equals(Data[i][1]) || AuthorNameToDelete.equals(AuthorString[0])) {
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        int NewAuthorCount = 0;
                        NewAuthorString = new String[AuthorString.length - 1];

                        for (String s : AuthorString) {
                            if (!AuthorNameToDelete.equals(s)) {
                                NewAuthorString[NewAuthorCount] = s;
                                NewAuthorCount += 1;
                            }
                        }

                        AuthorString = NewAuthorString;

                        if (AuthorString.length == 0) {
                            AuthorString = new String[] {"Author"};
                        }

                        FilterPanel.remove(Author);
                        Author = new JComboBox(AuthorString);
                        FilterPanel.add(Author, 0);
                        BookList.setVisible(true);
                    }
                }
            }
        });

        //EditBook
        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int EditRowIndex = Books.getSelectedRow();
                NewBook = new String[]{
                        NewBookAuthor.getText(),
                        NewBookName.getText(),
                        NewRating.getText(),
                        NewBookAvailability.getText(),
                };

                Data = append(Data, NewBook);
                Model.addRow(NewBook);

                NewAuthor = NewBook[1];
                int flag0 = 0;
                for (String s : AuthorString) {
                    if (NewAuthor.equals(s)) {
                        flag0 = 1;
                    }
                }

                if (flag0 == 0) {
                    NewAuthorString = new String[AuthorString.length + 1];
                    System.arraycopy(AuthorString, 0, NewAuthorString, 0, AuthorString.length);
                    NewAuthorString[AuthorString.length] = NewAuthor;
                    AuthorString = NewAuthorString;
                    FilterPanel.remove(Author);
                    Author = new JComboBox(AuthorString);
                    FilterPanel.add(Author, 0);
                    BookList.setVisible(true);
                }

                BookCount += 1;
                int NewBookCount = 0;

                if (EditRowIndex > -1 & EditRowIndex < BookCount) {
                    String AuthorNameToDelete = Data[EditRowIndex][1];
                    int flag = 0;
                    Model.removeRow(EditRowIndex);
                    String[][] NewData = new String[BookCount - 1][];

                    for (int i = 0; i < BookCount; i++) {
                        if(i != EditRowIndex) {
                            NewData[NewBookCount] = Data[i];
                            NewBookCount += 1;
                        }
                    }

                    Data = NewData;
                    BookCount -= 1;

                    for (int i = 0; i < BookCount; i++) {
                        if (AuthorNameToDelete.equals(Data[i][1]) || AuthorNameToDelete.equals(AuthorString[0])) {
                            flag = 1;
                        }
                    }

                    if (flag == 0) {
                        int NewAuthorCount = 0;
                        NewAuthorString = new String[AuthorString.length - 1];

                        for (String s : AuthorString) {
                            if (!AuthorNameToDelete.equals(s)) {
                                NewAuthorString[NewAuthorCount] = s;
                                NewAuthorCount += 1;
                            }
                        }

                        AuthorString = NewAuthorString;

                        if (AuthorString.length == 0) {
                            AuthorString = new String[] {"Author"};
                        }

                        FilterPanel.remove(Author);
                        Author = new JComboBox(AuthorString);
                        FilterPanel.add(Author, 0);
                        BookList.setVisible(true);
                    }
                }
            }
        });

        //Search
        Filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CheckName(BookName);
                    CheckAuthor(Author);
                    String[][] Data_1 = new String[11][];
                    int j = 0;
                    String Item = String.valueOf(Author.getSelectedItem());
                    String Item1 =  BookName.getText();

                    for (int i = 0; i < BookCount; i++) {
                        if (Item1.equals(Data[i][1]) || Item.equals(Data[i][0])) {
                            Data_1[j] = Data[i];
                            j += 1;
                        }
                    }

                    String[] Columns_1 = {"Author", "Book", "Rating", "Availability"};
                    Model_1 = new DefaultTableModel(Data_1, Columns_1);
                    Books_1 = new JTable(Model_1);
                    ScrollBar_1 = new JScrollPane(Books_1);
                    UIManager.put("OptionPane.minimumSize",new Dimension(800,300));
                    JOptionPane.showMessageDialog(null, ScrollBar_1, "Found", JOptionPane.PLAIN_MESSAGE);
                }
                catch(NameException ex1) {
                    JOptionPane.showMessageDialog(null, ex1.getMessage());
                }
                catch(AuthorException myEx) {
                    JOptionPane.showMessageDialog(null, myEx.getMessage());
                }
            }
        });

        BookList.add(FilterPanel,BorderLayout.SOUTH);
        BookList.setVisible(true);
    }
}
