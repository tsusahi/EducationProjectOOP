package Lab10;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * A class describing the logic of the application
 */
public class Action {

    Interface anInterface = new Interface();

    private static final Logger log = Logger.getLogger("Action.class");

    public void show() { //A method that creates and displays an application window
        log.info("Открытие экранной формы");
        anInterface.creatingInterface();
        addButtonClicked();
        editButtonAction();
        deleteButtonAction();
        filterButtonAction();
        saveButtonAction();
        openButtonAction();
        openXMLButtonAction();
    }

    /**Method of adding an element to an array*/
    public static <T> T[] append(T[] Arr, T Element) {
        T[] array = Arrays.copyOf(Arr, Arr.length + 1);
        array[Arr.length] = Element;
        return array;
    }

    /**Exception class of an empty name field*/
    private static class nameException extends Exception {
        public nameException() {
            super("No search name entered");
        }
    }

    /**Exception class of an empty author field*/
    private static class authorException extends Exception {
        public authorException() {
            super("Author of the book has not been entered");
        }
    }

    /**Input exception class*/
    private static class inputFieldsException extends Exception {
        public inputFieldsException() {
            super("Not all fields were filled in");
        }
    }

    /**Name field verification method*/
    private void checkName(JTextField bookName) throws nameException {
        String name = bookName.getText();
        if (name.contains("Book name")) throw new nameException();
        if (name.length() == 0) throw new nameException();
    }

    /**Author field verification method*/
    private void checkAuthor(JComboBox author) throws authorException {
        String Item = String.valueOf(author.getSelectedItem());
        if (Item.equals("Author")) throw new authorException();
    }

    /**Input field verification method*/
    private void checkInputFields(JTextField author, JTextField bookName, JTextField rating,
                                  JTextField Availability) throws inputFieldsException {
        String authorStr = author.getText();
        String bookNameStr = bookName.getText();
        String ratingStr = rating.getText();
        String availabilityStr = Availability.getText();
        if (authorStr.length() == 0 || bookNameStr.length() == 0 ||
                ratingStr.length() == 0 || availabilityStr.length() == 0) throw new inputFieldsException();
        if (authorStr.contains("Author") || bookNameStr.contains("Book name") ||
                ratingStr.contains("Availability")) throw new inputFieldsException();
    }

    /**The logic of the add button*/
    private void addButtonClicked() {
        anInterface.add.addActionListener(e -> {
            log.info("Добавление записи в таблицу");
            anInterface.newBook = new String[]{
                    anInterface.textFieldAuthor.getText(),
                    anInterface.textFieldBookName.getText(),
                    anInterface.textFieldRating.getText(),
                    anInterface.textFieldAvailability.getText(),
            };

            try {
                checkInputFields(anInterface.textFieldAuthor, anInterface.textFieldBookName, anInterface.textFieldRating, anInterface.textFieldAvailability);
                anInterface.data = append(anInterface.data, anInterface.newBook);
                anInterface.model.addRow(anInterface.newBook);
                anInterface.newAuthor = anInterface.newBook[1];
                int flag = 0;

                for (String s : anInterface.authorString) {
                    if (anInterface.newAuthor.equals(s)) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    anInterface.newAuthorString = new String[anInterface.authorString.length + 1];
                    System.arraycopy(anInterface.authorString, 0, anInterface.newAuthorString, 0, anInterface.authorString.length);
                    anInterface.newAuthorString[anInterface.authorString.length] = anInterface.newAuthor;
                    anInterface.authorString = anInterface.newAuthorString;
                    anInterface.filterPanel.remove(anInterface.author);
                    anInterface.author = new JComboBox<>(anInterface.authorString);
                    anInterface.filterPanel.add(anInterface.author, 0);
                    anInterface.bookList.setVisible(true);
                }

                anInterface.bookCount += 1;

            } catch (inputFieldsException ex) {
                log.error("Ошибка ввода", ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    /**The logic of the delete button*/
    private void deleteButtonAction() {
        anInterface.delete.addActionListener(e -> {
            log.info("Удаление записи из таблицы");
            int selectedRow = anInterface.books.getSelectedRow();
            int newBookCount = 0;

            if (selectedRow > -1 & selectedRow <= anInterface.bookCount) {

                String[][] newData = new String[anInterface.bookCount - 1][];
                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (i != selectedRow) {
                        newData[newBookCount] = anInterface.data[i];
                        newBookCount += 1;
                    }
                }

                String authorNameToDelete = anInterface.data[selectedRow][0];
                int flag = 0;
                anInterface.model.removeRow(selectedRow);
                anInterface.data = newData;
                anInterface.bookCount -= 1;

                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (authorNameToDelete.equals(anInterface.data[i][0]) || authorNameToDelete.equals(anInterface.authorString[0])) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    int NewAuthorCount = 0;
                    anInterface.newAuthorString = new String[anInterface.authorString.length - 1];

                    for (String s : anInterface.authorString) {
                        if (!authorNameToDelete.equals(s)) {
                            anInterface.newAuthorString[NewAuthorCount] = s;
                            NewAuthorCount += 1;
                        }
                    }

                    anInterface.authorString = anInterface.newAuthorString;

                    if (anInterface.authorString.length == 0) {
                        anInterface.authorString = new String[]{"Author"};
                    }

                    anInterface.filterPanel.remove(anInterface.author);
                    anInterface.author = new JComboBox<>(anInterface.authorString);
                    anInterface.filterPanel.add(anInterface.author, 0);
                    anInterface.bookList.setVisible(true);
                }
            }
        });
    }

    /**The logic of the edit button*/
    private void editButtonAction() {
        anInterface.edit.addActionListener(e -> {
            log.info("Редактирование записи в таблице");
            int EditRowIndex = anInterface.books.getSelectedRow();
            anInterface.newBook = new String[]{
                    anInterface.textFieldAuthor.getText(),
                    anInterface.textFieldBookName.getText(),
                    anInterface.textFieldRating.getText(),
                    anInterface.textFieldAvailability.getText(),
            };

            anInterface.data = append(anInterface.data, anInterface.newBook);
            anInterface.model.addRow(anInterface.newBook);

            anInterface.newAuthor = anInterface.newBook[1];
            int flag0 = 0;
            for (String s : anInterface.authorString) {
                if (anInterface.newAuthor.equals(s)) {
                    flag0 = 1;
                    break;
                }
            }

            if (flag0 == 0) {
                anInterface.newAuthorString = new String[anInterface.authorString.length + 1];
                System.arraycopy(anInterface.authorString, 0, anInterface.newAuthorString, 0, anInterface.authorString.length);
                anInterface.newAuthorString[anInterface.authorString.length] = anInterface.newAuthor;
                anInterface.authorString = anInterface.newAuthorString;
                anInterface.filterPanel.remove(anInterface.author);
                anInterface.author = new JComboBox<>(anInterface.authorString);
                anInterface.filterPanel.add(anInterface.author, 0);
                anInterface.bookList.setVisible(true);
            }

            anInterface.bookCount += 1;
            int NewBookCount = 0;

            if (EditRowIndex > -1 & EditRowIndex < anInterface.bookCount) {
                String AuthorNameToDelete = anInterface.data[EditRowIndex][1];
                int flag = 0;
                anInterface.model.removeRow(EditRowIndex);
                String[][] NewData = new String[anInterface.bookCount - 1][];

                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (i != EditRowIndex) {
                        NewData[NewBookCount] = anInterface.data[i];
                        NewBookCount += 1;
                    }
                }

                anInterface.data = NewData;
                anInterface.bookCount -= 1;

                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (AuthorNameToDelete.equals(anInterface.data[i][1]) || AuthorNameToDelete.equals(anInterface.authorString[0])) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    int NewAuthorCount = 0;
                    anInterface.newAuthorString = new String[anInterface.authorString.length - 1];

                    for (String s : anInterface.authorString) {
                        if (!AuthorNameToDelete.equals(s)) {
                            anInterface.newAuthorString[NewAuthorCount] = s;
                            NewAuthorCount += 1;
                        }
                    }

                    anInterface.authorString = anInterface.newAuthorString;

                    if (anInterface.authorString.length == 0) {
                        anInterface.authorString = new String[]{"Author"};
                    }

                    anInterface.filterPanel.remove(anInterface.author);
                    anInterface.author = new JComboBox<>(anInterface.authorString);
                    anInterface.filterPanel.add(anInterface.author, 0);
                    anInterface.bookList.setVisible(true);
                }
            }
        });
    }

    /**The logic of the filter button*/
    private void filterButtonAction() {
        anInterface.filter.addActionListener(e -> {
            log.info("Фильтрование записей таблицы");
            try {
                checkName(anInterface.panelBookName);
                checkAuthor(anInterface.author);
                String[][] FilteredData = new String[11][];
                int j = 0;
                String Item = String.valueOf(anInterface.author.getSelectedItem());
                String Item1 = anInterface.panelBookName.getText();

                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (Item1.equals(anInterface.data[i][1]) || Item.equals(anInterface.data[i][0])) {
                        FilteredData[j] = anInterface.data[i];
                        j += 1;
                    }
                }

                String[] Columns_1 = {"Author", "Book", "Rating", "Availability"};
                anInterface.filteredModel = new DefaultTableModel(FilteredData, Columns_1);
                anInterface.filteredBooks = new JTable(anInterface.filteredModel);
                anInterface.filteredScrollPane = new JScrollPane(anInterface.filteredBooks);
                UIManager.put("OptionPane.minimumSize", new Dimension(800, 300));
                JOptionPane.showMessageDialog(null, anInterface.filteredScrollPane, "Found", JOptionPane.PLAIN_MESSAGE);
            } catch (nameException | authorException ex) {
                log.error("Ошибка поиска", ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    /**The logic of the save button*/
    private void saveButtonAction() {
        anInterface.save.addActionListener(e -> {
            creatingSaveSelection();
        });
    }

    /**The logic of the open button*/
    private void openButtonAction() {
        anInterface.open.addActionListener(e -> {
            log.info("Открытие файла");
            try {
                String fileFormat = "*.csv";
                String line = "";
                String splitBy = ",";
                FileDialog load = new FileDialog(anInterface.bookList, "Open file", FileDialog.LOAD);
                load.setFile(fileFormat);
                load.setVisible(true);
                String fileName = load.getDirectory() + load.getFile();
                if (load.getFile() == null) return;
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                int rows = anInterface.model.getRowCount();
                for (int i = 0; i < rows; i++) anInterface.model.removeRow(0);
                while (line != null) {
                    line = reader.readLine();
                    if (line != null) {
                        String[] lineToString = line.split(splitBy);
                        anInterface.model.addRow(lineToString);
                    }
                }
                reader.close();
            } catch (IOException ex) {
                log.error("Ошибка открытия файла", ex);
                ex.printStackTrace();
            }
        });
    }

    /**A method that allows you to choose how to save a file*/
    private void creatingSaveSelection() {
        Object[] Options = {"HTML","PDF", "XML",  "CSV"};
        int n = JOptionPane.showOptionDialog(anInterface.bookList, "In what format do you want to save the file?",
                "Save selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,
                Options,
                Options[0]);
        if (n == 0) {
            log.info("Генерация отчета");
            saveHTMLReport();
        } else if (n == 1) {
            log.info("Генерация отчета");
            savePDFReport();
        } else if (n == 2) {
            log.info("Сохранение XML файла");
            saveXMLAction();
        } else if (n == 3) {
            log.info("Сохранение CSV файла");
            saveCSVAction();
        }
    }

    /**The logic of the save csv file button*/
    private void saveCSVAction() {
        log.info("Сохранение CSV файла");
        try {
            String FileFormat = "*.csv";
            FileDialog Save = new FileDialog(anInterface.bookList, "Open file", FileDialog.SAVE);
            Save.setFile(FileFormat);
            Save.setVisible(true);
            String FileName = Save.getDirectory() + Save.getFile();
            if (!FileName.endsWith(".csv")) {
                FileName += ".csv";
            }
            if (Save.getFile() == null) return;
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileName));
            for (int i = 0; i < anInterface.model.getRowCount(); i++) {
                for (int j = 0; j < anInterface.model.getColumnCount(); j++) {
                    writer.write((String) anInterface.model.getValueAt(i, j));
                    if (j != anInterface.model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException ex) {
            log.error("Ошибка сохранения CSV файла", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    /**The logic of the save html file button*/
    private void saveHTMLReport() {
        log.info("Создание HTML отчета");
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 50, 50, 50, 50);
            PdfPTable pTable = new PdfPTable(4);
            PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
            BaseFont font = BaseFont.createFont("C:\\Users\\tsusa\\IdeaProjects\\EducationProjectOOP\\src\\main\\resources\\SFProText-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font1 = new com.itextpdf.text.Font(font, 14);

            for (int i = 0; i < anInterface.model.getRowCount(); i++) {
                pTable.addCell(new Phrase((String) anInterface.model.getValueAt(i, 0), font1));
                pTable.addCell(new Phrase((String) anInterface.model.getValueAt(i, 1), font1));
                pTable.addCell(new Phrase((String) anInterface.model.getValueAt(i, 2), font1));
                pTable.addCell(new Phrase((String) anInterface.model.getValueAt(i, 3), font1));
            }

            document.open();
            document.add(pTable);
            document.close();

        } catch (DocumentException | IOException e) {
            log.error("Ошибка при создании HTML отчета", e);
            throw new RuntimeException(e);
        }
    }

    /**The logic of the save xml file button*/
    private void saveXMLAction() {
        log.info("Сохранение XML файла");
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Node window = doc.createElement("Window");
            doc.appendChild(window);
            for (int i = 0; i < anInterface.model.getRowCount(); i++) {
                Element dataEmploy = doc.createElement("dataEmploy");
                window.appendChild(dataEmploy);
                dataEmploy.setAttribute("Author", (String) anInterface.model.getValueAt(i, 0));
                dataEmploy.setAttribute("Book", (String) anInterface.model.getValueAt(i, 1));
                dataEmploy.setAttribute("Rating", (String) anInterface.model.getValueAt(i, 2));
                dataEmploy.setAttribute("Availability", (String) anInterface.model.getValueAt(i, 3));
            }
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                java.io.FileWriter fileWriter = new FileWriter("dataEmploy.xml");
                transformer.transform(new DOMSource(doc), new StreamResult(fileWriter));
                JOptionPane.showMessageDialog(null, "Data uploaded to a file dataEmploy.xml");
            } catch (TransformerException | IOException ex1) {
                log.error("Ошибка записи в XML файл", ex1);
                ex1.printStackTrace();
            }
        } catch (ParserConfigurationException ex) {
            log.error("Ошибка конфигурации парсера в сохранении XML файла", ex);
            ex.printStackTrace();
        }
    }

    /**The logic of the save pdf file button*/
    private void savePDFReport() {
        log.info("Создание PDF отчета");
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("report.html"));
            printWriter.println("<TABLE BORDER><TR><TH>name<TH>bookName<TH>rating<TH>availability<TH></TR>");
            for (int i = 0; i < anInterface.model.getRowCount(); i++) {
                if (anInterface.model.getValueAt(i,0) != null) {
                    printWriter.println("<TR><TD>" + anInterface.model.getValueAt(i,0) +
                            "<TD>" + anInterface.model.getValueAt(i,1) +
                            "<TD>" + anInterface.model.getValueAt(i,2) +
                            "<TD>" + anInterface.model.getValueAt(i,3));
                }
            }
            printWriter.close();
        } catch (IOException e) {
            log.error("Ошибка при создании PDF отчета", e);
            throw new RuntimeException(e);
        }
    }

    /**The logic of the open xml file button*/
    private void openXMLButtonAction() {
        anInterface.openXML.addActionListener(e -> {
            try { // Creating a document parser
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = dBuilder.parse(new File("dataEmploy.xml")); // Document normalization

                doc.getDocumentElement().normalize(); // Getting a list of items with the name
                NodeList nlDataEmploy = doc.getElementsByTagName("dataEmploy"); // The cycle of viewing the item list and writing data to the table
                for (int temp = 0; temp < nlDataEmploy.getLength(); temp++) { // Selecting the next item in the list
                    Node elem = nlDataEmploy.item(temp); // Getting a list of document attributes
                    NamedNodeMap attrs = elem.getAttributes(); // Reading element attributes
                    String Author = attrs.getNamedItem("Author").getNodeValue();
                    String Book = attrs.getNamedItem("Book").getNodeValue();
                    String Rating = attrs.getNamedItem("Rating").getNodeValue(); // Writing data to a table
                    String Availability = attrs.getNamedItem("Availability").getNodeValue();
                    anInterface.model.addRow(new String[] {Author, Book, Rating, Availability});
                }
                JOptionPane.showMessageDialog(anInterface.bookList, "Ð”Ð°Ð½Ð½Ñ‹Ðµ Ð·Ð°Ð³Ñ€ÑƒÐ¶ÐµÐ½Ñ‹ Ð¸Ð· Ñ„Ð°Ð¹Ð»Ð° dataEmploy.xml");
            } catch (ParserConfigurationException | SAXException | IOException ex2) {
                ex2.printStackTrace();
            } // Parser error handling when reading data from an XML file
        });



    }

}
