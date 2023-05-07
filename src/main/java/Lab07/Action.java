package Lab07;

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

public class Action {

    Interface anInterface = new Interface();

    public void show() {
        anInterface.creatingInterface();
        addButtonClicked();
        editButtonAction();
        deleteButtonAction();
        filterButtonAction();
        saveButtonAction();
        openButtonAction();
        openXMLButtonAction();
    }

    public static <T> T[] append(T[] Arr, T Element) {
        T[] array = Arrays.copyOf(Arr, Arr.length + 1);
        array[Arr.length] = Element;
        return array;
    }

    private static class nameException extends Exception {
        public nameException() {
            super("No search name entered");
        }
    }

    private static class authorException extends Exception {
        public authorException() {
            super("Author of the book has not been entered");
        }
    }

    private static class inputFieldsException extends Exception {
        public inputFieldsException() {
            super("Not all fields were filled in");
        }
    }

    private void checkName(JTextField bookName) throws nameException {
        String name = bookName.getText();
        if (name.contains("Book name")) throw new nameException();
        if (name.length() == 0) throw new nameException();
    }

    private void checkAuthor(JComboBox author) throws authorException {
        String Item = String.valueOf(author.getSelectedItem());
        if (Item.equals("Author")) throw new authorException();
    }

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

    private void addButtonClicked() {
        anInterface.add.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    private void deleteButtonAction() {
        anInterface.delete.addActionListener(e -> {
            int RemoveRowIndex = anInterface.books.getSelectedRow();
            int NewBookCount = 0;

            if (RemoveRowIndex > -1 & RemoveRowIndex < anInterface.bookCount) {
                String AuthorNameToDelete = anInterface.data[RemoveRowIndex][1];
                int flag = 0;
                anInterface.model.removeRow(RemoveRowIndex);
                String[][] NewData = new String[anInterface.bookCount - 1][];

                for (int i = 0; i < anInterface.bookCount; i++) {
                    if (i != RemoveRowIndex) {
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

    private void editButtonAction() {
        anInterface.edit.addActionListener(e -> {
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

    private void filterButtonAction() {
        anInterface.filter.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    private void saveButtonAction() {
        anInterface.save.addActionListener(e -> {
            creatingSaveSelection();
        });
    }

    private void openButtonAction() {
        anInterface.open.addActionListener(e -> {
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
                ex.printStackTrace();
            }
        });
    }

    private void creatingSaveSelection() {
        Object[] Options = {"HTML","PDF", "XML",  "CSV"};
        int n = JOptionPane.showOptionDialog(anInterface.bookList, "In what format do you want to save the file?",
                "Save selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,
                Options,
                Options[0]);
        if (n == 0) {
            saveHTMLReport();
        } else if (n == 1) {
            savePDFReport();
        } else if (n == 2) {
            saveXMLAction();
        } else if (n == 3) {
            saveCSVAction();
        }
    }

    private void saveCSVAction() {
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
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void saveHTMLReport() {
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
            throw new RuntimeException(e);
        }
    }

    private void saveXMLAction() {
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
                ex1.printStackTrace();
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    private void savePDFReport() {
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
            throw new RuntimeException(e);
        }
    }

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
