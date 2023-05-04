package Lab03;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class GUI {
    //Объявление графических классов
    private JFrame BookList;
    private DefaultTableModel Model;
    private JButton Save;
    private JButton Add;
    private JButton Delete;
    private JButton Edit;
    private JToolBar ToolBar;
    private JScrollPane ScrollBar;
    private JTable Books;
    private JComboBox Author;
    private JTextField BookName, NewBookAuthor, NewBookName, NewBookAvailability, NewNumber;
    private JButton Filter;
    private String NewAuthor;
    private String[] AuthorString, NewAuthorString, NewBook;
    private String[][] Data = {{"1","Михаил Булгаков","Мастер и Маргарита", "В наличии"},
            {"2","Достоевский Ф.М.","Преступление и наказание", "В наличии"},
            {"3","Антуан де Сент-Экзюпери","Маленький принц", "Нет в наличии"},
            {"4","Михаил Булгаков","Собачье сердце", "В наличии"},
            {"5","Эрих Мария Ремарк","Три товарища", "В наличии"},
            {"6","Джером Сэлинджер","Над пропастью во ржи", "Нет в наличии"},
            {"7","Михаил Лермонтов","Герой нашего времени", "Нет в наличии"},
            {"8","Артур Конан Дойл","Приключение Шерлока Холмса", "В наличии"},
            {"9","Оскар Уайльд","Портрет Дориана Грея", "Нет в наличии"},
            {"10","Джон Рональд Руэл Толкин","Властелин колец", "В наличии"}
    };

    private int BookCount = 10;

    public static <T> T[] append(T[] Arr, T Element) {
        T[] Array = Arrays.copyOf(Arr, Arr.length + 1);
        Array[Arr.length] = Element;
        return Array;
    }

    public void show() {
        //Создание объектов графических классов

        //Создание окна
        BookList = new JFrame("Book list");
        BookList.setSize(800, 600);
        BookList.setLocation(368, 43);
        BookList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Создание кнопки Save
        Save = new JButton(new ImageIcon("./img/save.png"));
        Save.setToolTipText("Save");

        //Создание кнопки Add
        Add = new JButton(new ImageIcon("./img/add.png"));
        Add.setToolTipText("Add");

        //Создание кнопки Delete
        Delete = new JButton(new ImageIcon("./img/delete.png"));
        Delete.setToolTipText("Delete");

        //Создание кнопки Edit
        Edit = new JButton(new ImageIcon("./img/edit.png"));
        Edit.setToolTipText("Edit");

        //Создание Toolbar и добавление кнопок на него
        ToolBar = new JToolBar("Toolbar");
        ToolBar.add(Save);
        ToolBar.add(Add);
        ToolBar.add(Edit);
        ToolBar.add(Delete);

        //Размещение панели инструментов
        BookList.setLayout(new BorderLayout());
        BookList.add(ToolBar, BorderLayout.NORTH);

        //Создание таблицы с данными
        String[] Columns = {"Number", "Author","Book", "Availability"};

        Model = new DefaultTableModel(Data, Columns);
        Books = new JTable(Model);
        ScrollBar = new JScrollPane(Books);

        NewNumber = new JTextField("Number");
        NewNumber.setPreferredSize(new Dimension(50,20));

        NewBookAuthor = new JTextField("Author");
        NewBookAuthor.setPreferredSize(new Dimension(120, 20));

        NewBookName = new JTextField("Book");
        NewBookName.setPreferredSize(new Dimension(120, 20));

        NewBookAvailability = new JTextField("Availability");

        //Размещение таблицы с данными
        BookList.add(ScrollBar, BorderLayout.CENTER);
        JPanel NewBookPanel = new JPanel();
        NewBookPanel.add(NewNumber);
        NewBookPanel.add(NewBookAuthor);
        NewBookPanel.add(NewBookName);
        NewBookPanel.add(NewBookAvailability);

        ToolBar.add(NewBookPanel);

        //Подготовка для выпадающего списка
        AuthorString = new String[] {"Автор",
                "Михаил Булгаков","Достоевский Ф.М.","Антуан де Сент-Экзюпери","Эрих Мария Ремарк","Джером Сэлинджер",
                "Михаил Лермонтов","Артур Конан Дойл","Оскар Уайльд","Джон Рональд Руэл Толкин"};
        Author = new JComboBox(AuthorString);
        BookName = new JTextField("Book's name");
        Filter = new JButton("Search");

        //Добавление компонентов на панель
        JPanel FilterPanel = new JPanel();
        FilterPanel.add(Author);
        FilterPanel.add(BookName);
        FilterPanel.add(Filter);

        //Размещение панели
        BookList.add(FilterPanel, BorderLayout.SOUTH);


        //Добавление новой книги
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewBook = new String[] {
                        NewNumber.getText(),
                        NewBookAuthor.getText(),
                        NewBookName.getText(),
                        NewBookAvailability.getText(),
                };

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
            }
        });

        //Сохранение списка книг
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

        //Удаление книги из списка
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

        //Редактирование книги
        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int EditRowIndex = Books.getSelectedRow();
                NewBook = new String[]{
                        NewNumber.getText(),
                        NewBookAuthor.getText(),
                        NewBookName.getText(),
                        NewBookAvailability.getText(),
                };

                Data = append(Data, NewBook);
                Model.addRow(NewBook);

                NewAuthor = NewBook[1];
                int flag0 = 0;
                for (String s : AuthorString) {
                    if (NewAuthor.equals(s)) {
                        flag0 = 1;
                        break;
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

        BookList.add(FilterPanel, BorderLayout.SOUTH);
        BookList.setVisible(true);
    }
}

