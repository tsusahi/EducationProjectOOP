package Lab02;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookList {
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
    private JTextField BookName;
    private JButton Filter;

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
        String[] Columns = {"Author", "Book", "Availability"};
        String[][] Data = {{"Александр Дюма", "Три мушкетера", "Есть"}, {"Лев Толстой", "Анна каренина", "Есть"},
                {"Маргарет Митчелл", "Унесенные ветром", "Нет"}, {"Стивен Кинг","Кладбище домашних животных","Нет"},
                {"Чак Паланик","Бойцовский клуб","Нет"}};

        Model = new DefaultTableModel(Data, Columns);
        Books = new JTable(Model);
        ScrollBar = new JScrollPane(Books);

        //Размещение таблицы с данными
        BookList.add(ScrollBar, BorderLayout.CENTER);

        //Подготовка для выпадающего списка
        Author = new JComboBox(new String[]{"Author", "Александр Дюма", "Чак Паланик"});
        BookName = new JTextField("Book's name");
        Filter = new JButton("Search");

        //Добавление компонентов на панель
        JPanel FilterPanel = new JPanel();
        FilterPanel.add(Author);
        FilterPanel.add(BookName);
        FilterPanel.add(Filter);

        //Размещение панели
        BookList.add(FilterPanel, BorderLayout.SOUTH);

        //Визуализация экранной формы
        BookList.setVisible(true);
    }
}
