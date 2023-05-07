package Lab09;

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
    private JFrame bookList;
    private JPanel filterPanel;
    private DefaultTableModel tableModel;
    private JButton save;
    private JButton add;
    private JButton delete;
    private JButton edit;
    private JToolBar toolBar;
    private JScrollPane scrollPane;
    private JTable books;
    private JComboBox comboBox;
    private JTextField bookName, newBookAuthor, newBookName, newBookAvailability, newNumber;
    private JButton filter;
    private String newAuthor;
    private String[] authorString, newAuthorString, newBook;
    private String[][] data = {{"1","Михаил Булгаков","Мастер и Маргарита", "В наличии"},
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

    private int bookCount = 10;

    public static <T> T[] append(T[] Arr, T Element) {
        T[] Array = Arrays.copyOf(Arr, Arr.length + 1);
        Array[Arr.length] = Element;
        return Array;
    }

    public void show() {
        //Создание объектов графических классов

        //Создание окна
        bookList = new JFrame("Book list");
        bookList.setSize(800, 600);
        bookList.setLocation(368, 43);
        bookList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Создание кнопки Save
        Image saveIcon = new ImageIcon("./img/save1.png").getImage();
        save = new JButton(new ImageIcon(saveIcon));
        save.setToolTipText("Save");

        //Создание кнопки Add
        Image addIcon = new ImageIcon("./img/add1.png").getImage();
        add = new JButton(new ImageIcon(addIcon));
        add.setToolTipText("Add");

        //Создание кнопки Delete
        Image deleteIcon = new ImageIcon("./img/delete1.png").getImage();
        delete = new JButton(new ImageIcon(deleteIcon));
        delete.setToolTipText("Delete");

        //Создание кнопки Edit
        Image editIcon = new ImageIcon("./img/edit1.png").getImage();
        edit = new JButton(new ImageIcon(editIcon));
        edit.setToolTipText("Edit");

        //Создание Toolbar и добавление кнопок на него
        toolBar = new JToolBar("Toolbar");
        toolBar.add(save);
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(delete);

        //Размещение панели инструментов
        bookList.setLayout(new BorderLayout());
        bookList.add(toolBar, BorderLayout.NORTH);

        //Создание таблицы с данными
        String[] columns = {"Number", "Author","Book", "Availability"};

        tableModel = new DefaultTableModel(data, columns);
        books = new JTable(tableModel);
        scrollPane = new JScrollPane(books);

        newNumber = new JTextField("Number");
        newNumber.setPreferredSize(new Dimension(50,20));

        newBookAuthor = new JTextField("Author");
        newBookAuthor.setPreferredSize(new Dimension(120, 20));

        newBookName = new JTextField("Book");
        newBookName.setPreferredSize(new Dimension(120, 20));

        newBookAvailability = new JTextField("Availability");

        //Размещение таблицы с данными
        bookList.add(scrollPane, BorderLayout.CENTER);
        JPanel newBookPanel = new JPanel();
        newBookPanel.add(newNumber);
        newBookPanel.add(newBookAuthor);
        newBookPanel.add(newBookName);
        newBookPanel.add(newBookAvailability);

        toolBar.add(newBookPanel);

        //Подготовка для выпадающего списка
        authorString = new String[] {"Автор",
                "Михаил Булгаков","Достоевский Ф.М.","Антуан де Сент-Экзюпери","Эрих Мария Ремарк","Джером Сэлинджер",
                "Михаил Лермонтов","Артур Конан Дойл","Оскар Уайльд","Джон Рональд Руэл Толкин"};
        comboBox = new JComboBox(authorString);
        bookName = new JTextField("Book's name");
        filter = new JButton("Search");

        //Добавление компонентов на панель
        JPanel filterPanel = new JPanel();
        filterPanel.add(comboBox);
        filterPanel.add(bookName);
        filterPanel.add(filter);

        //Размещение панели
        bookList.add(filterPanel, BorderLayout.SOUTH);

        deleteButtonAction();
        addButtonAction();
        editButtonAction();
        saveButtonAction();

        bookList.add(filterPanel, BorderLayout.SOUTH);
        bookList.setVisible(true);
    }
    private void deleteButtonAction() {
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int RemoveRowIndex = books.getSelectedRow();
                int NewBookCount = 0;

                if (RemoveRowIndex > -1 & RemoveRowIndex < bookCount) {
                    String AuthorNameToDelete = data[RemoveRowIndex][1];
                    int flag = 0;
                    tableModel.removeRow(RemoveRowIndex);
                    String[][] NewData = new String[bookCount - 1][];

                    for (int i = 0; i < bookCount; i++) {
                        if (i != RemoveRowIndex) {
                            NewData[NewBookCount] = data[i];
                            NewBookCount += 1;
                        }
                    }

                    data = NewData;
                    bookCount -= 1;

                    for (int i = 0; i < bookCount; i++) {
                        if (AuthorNameToDelete.equals(data[i][1]) || AuthorNameToDelete.equals(authorString[0])) {
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        int NewAuthorCount = 0;
                        newAuthorString = new String[authorString.length - 1];

                        for (String s : authorString) {
                            if (!AuthorNameToDelete.equals(s)) {
                                NewAuthorCount += 1;
                            }
                        }

                        authorString = newAuthorString;

                        if (authorString.length == 0) {
                            authorString = new String[] {"Author"};
                        }

                        filterPanel.remove(comboBox);
                        comboBox = new JComboBox(authorString);
                        filterPanel.add(comboBox, 0);
                        bookList.setVisible(true);
                    }
                }
            }
        });
    }
    private void addButtonAction() {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBook = new String[] {
                        newNumber.getText(),
                        newBookAuthor.getText(),
                        newBookName.getText(),
                        newBookAvailability.getText(),
                };

                data = append(data, newBook);
                tableModel.addRow(newBook);
                newAuthor = newBook[1];
                int flag = 0;
                for (String s : authorString) {
                    if (newAuthor.equals(s)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    newAuthorString = new String[authorString.length + 1];
                    System.arraycopy(authorString, 0, newAuthorString, 0, authorString.length);
                    newAuthorString[authorString.length] = newAuthor;
                    authorString = newAuthorString;
                    filterPanel.remove(comboBox);
                    comboBox = new JComboBox(authorString);
                    filterPanel.add(comboBox, 0);
                    bookList.setVisible(true);
                }
                bookCount += 1;
            }
        });
    }
    private void editButtonAction() {
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int EditRowIndex = books.getSelectedRow();
                newBook = new String[]{
                        newNumber.getText(),
                        newBookAuthor.getText(),
                        newBookName.getText(),
                        newBookAvailability.getText(),
                };

                data = append(data, newBook);
                tableModel.addRow(newBook);

                newAuthor = newBook[1];
                int flag0 = 0;
                for (String s : authorString) {
                    if (newAuthor.equals(s)) {
                        flag0 = 1;
                        break;
                    }
                }

                if (flag0 == 0) {
                    newAuthorString = new String[authorString.length + 1];
                    System.arraycopy(authorString, 0, newAuthorString, 0, authorString.length);
                    newAuthorString[authorString.length] = newAuthor;
                    authorString = newAuthorString;
                    filterPanel.remove(comboBox);
                    comboBox = new JComboBox(authorString);
                    filterPanel.add(comboBox, 0);
                    bookList.setVisible(true);
                }

                bookCount += 1;
                int NewBookCount = 0;

                if (EditRowIndex > -1 & EditRowIndex < bookCount) {
                    String AuthorNameToDelete = data[EditRowIndex][1];
                    int flag = 0;
                    tableModel.removeRow(EditRowIndex);
                    String[][] NewData = new String[bookCount - 1][];

                    for (int i = 0; i < bookCount; i++) {
                        if(i != EditRowIndex) {
                            NewData[NewBookCount] = data[i];
                            NewBookCount += 1;
                        }
                    }

                    data = NewData;
                    bookCount -= 1;

                    for (int i = 0; i < bookCount; i++) {
                        if (AuthorNameToDelete.equals(data[i][1]) || AuthorNameToDelete.equals(authorString[0])) {
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        int NewAuthorCount = 0;
                        newAuthorString = new String[authorString.length - 1];

                        for (String s : authorString) {
                            if (!AuthorNameToDelete.equals(s)) {
                                newAuthorString[NewAuthorCount] = s;
                                NewAuthorCount += 1;
                            }
                        }

                        authorString = newAuthorString;

                        if (authorString.length == 0) {
                            authorString = new String[] {"Author"};
                        }

                        filterPanel.remove(comboBox);
                        comboBox = new JComboBox(authorString);
                        filterPanel.add(comboBox, 0);
                        bookList.setVisible(true);
                    }
                }
            }
        });
    }
    private void saveButtonAction() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try(FileWriter writer = new FileWriter("SavedBookList.txt", false)) {
                    for (int i = 0; i < bookCount; i++) {
                        for (int j = 0; j < 4; j++) {
                            writer.write(data[i][j]);
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
    }
    public static class Util {
        public static boolean isEmpty(String value) {
            return value == null || "".equals(value);
        }
        public static int sum(int x, int y) {
            return x + y;
        }
        public static boolean checkName(String bName) {
            return bName == null || bName.equals("Test");
        }

    }
}

