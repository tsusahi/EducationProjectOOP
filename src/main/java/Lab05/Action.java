package Lab05;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Action {

    GUI G = new GUI();
    public void Show() {
        G.CreatingGUI();
        AddButtonClicked();
        EditButtonAction();
        DeleteButtonAction();
        FilterButtonAction();
        SaveButtonAction();
        OpenButtonAction();
    }

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
    private void CheckInputFields   (JTextField Author, JTextField BookName, JTextField Rating,
                                     JTextField Availability) throws InputFieldsException {
        String Author_str = Author.getText();
        String BookName_str = BookName.getText();
        String Rating_str = Rating.getText();
        String Availability_str = Availability.getText();
        if (Author_str.length() == 0 || BookName_str.length() == 0 ||
                Rating_str.length() == 0 || Availability_str.length() == 0) throw new InputFieldsException();
        if (Author_str.contains("Author")|| BookName_str.contains("Book name") ||
                Rating_str.contains("Availability")) throw new InputFieldsException();
    }
    private void AddButtonClicked() {
        G.Add.addActionListener(e -> {
            G.NewBook = new String[] {
                    G.TAuthor.getText(),
                    G.TBookName.getText(),
                    G.TRating.getText(),
                    G.TAvailability.getText(),
            };

            try {
                CheckInputFields(G.TAuthor, G.TBookName, G.TRating, G.TAvailability);
                G.Data = append(G.Data, G.NewBook);
                G.Model.addRow(G.NewBook);
                G.NewAuthor = G.NewBook[1];
                int flag = 0;

                for (String s : G.AuthorString) {
                    if (G.NewAuthor.equals(s)) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    G.NewAuthorString = new String[G.AuthorString.length + 1];
                    System.arraycopy(G.AuthorString, 0, G.NewAuthorString, 0, G.AuthorString.length);
                    G.NewAuthorString[G.AuthorString.length] = G.NewAuthor;
                    G.AuthorString = G.NewAuthorString;
                    G.FilterPanel.remove(G.Author);
                    G.Author = new JComboBox<>(G.AuthorString);
                    G.FilterPanel.add(G.Author, 0);
                    G.BookList.setVisible(true);
                }

                G.BookCount += 1;

            } catch (InputFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }
    private void DeleteButtonAction() {
        G.Delete.addActionListener(e -> {
            int RemoveRowIndex = G.Books.getSelectedRow();
            int NewBookCount = 0;

            if (RemoveRowIndex > -1 & RemoveRowIndex < G.BookCount) {
                String AuthorNameToDelete = G.Data[RemoveRowIndex][1];
                int flag = 0;
                G.Model.removeRow(RemoveRowIndex);
                String[][] NewData = new String[G.BookCount - 1][];

                for (int i = 0; i < G.BookCount; i++) {
                    if (i != RemoveRowIndex) {
                        NewData[NewBookCount] = G.Data[i];
                        NewBookCount += 1;
                    }
                }

                G.Data = NewData;
                G.BookCount -= 1;

                for (int i = 0; i < G.BookCount; i++) {
                    if (AuthorNameToDelete.equals(G.Data[i][1]) || AuthorNameToDelete.equals(G.AuthorString[0])) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    int NewAuthorCount = 0;
                    G.NewAuthorString = new String[G.AuthorString.length - 1];

                    for (String s : G.AuthorString) {
                        if (!AuthorNameToDelete.equals(s)) {
                            G.NewAuthorString[NewAuthorCount] = s;
                            NewAuthorCount += 1;
                        }
                    }

                    G.AuthorString = G.NewAuthorString;

                    if (G.AuthorString.length == 0) {
                        G.AuthorString = new String[] {"Author"};
                    }

                    G.FilterPanel.remove(G.Author);
                    G.Author = new JComboBox<>(G.AuthorString);
                    G.FilterPanel.add(G.Author, 0);
                    G.BookList.setVisible(true);
                }
            }
        });
    }
    private void EditButtonAction() {
        G.Edit.addActionListener(e -> {
            int EditRowIndex = G.Books.getSelectedRow();
            G.NewBook = new String[]{
                    G.TAuthor.getText(),
                    G.TBookName.getText(),
                    G.TRating.getText(),
                    G.TAvailability.getText(),
            };

            G.Data = append(G.Data, G.NewBook);
            G.Model.addRow(G.NewBook);

            G.NewAuthor = G.NewBook[1];
            int flag0 = 0;
            for (String s : G.AuthorString) {
                if (G.NewAuthor.equals(s)) {
                    flag0 = 1;
                }
            }

            if (flag0 == 0) {
                G.NewAuthorString = new String[G.AuthorString.length + 1];
                System.arraycopy(G.AuthorString, 0, G.NewAuthorString, 0, G.AuthorString.length);
                G.NewAuthorString[G.AuthorString.length] = G.NewAuthor;
                G.AuthorString = G.NewAuthorString;
                G.FilterPanel.remove(G.Author);
                G.Author = new JComboBox<>(G.AuthorString);
                G.FilterPanel.add(G.Author, 0);
                G.BookList.setVisible(true);
            }

            G.BookCount += 1;
            int NewBookCount = 0;

            if (EditRowIndex > -1 & EditRowIndex < G.BookCount) {
                String AuthorNameToDelete = G.Data[EditRowIndex][1];
                int flag = 0;
                G.Model.removeRow(EditRowIndex);
                String[][] NewData = new String[G.BookCount - 1][];

                for (int i = 0; i < G.BookCount; i++) {
                    if(i != EditRowIndex) {
                        NewData[NewBookCount] = G.Data[i];
                        NewBookCount += 1;
                    }
                }

                G.Data = NewData;
                G.BookCount -= 1;

                for (int i = 0; i < G.BookCount; i++) {
                    if (AuthorNameToDelete.equals(G.Data[i][1]) || AuthorNameToDelete.equals(G.AuthorString[0])) {
                        flag = 1;
                    }
                }

                if (flag == 0) {
                    int NewAuthorCount = 0;
                    G.NewAuthorString = new String[G.AuthorString.length - 1];

                    for (String s : G.AuthorString) {
                        if (!AuthorNameToDelete.equals(s)) {
                            G.NewAuthorString[NewAuthorCount] = s;
                            NewAuthorCount += 1;
                        }
                    }

                    G.AuthorString = G.NewAuthorString;

                    if (G.AuthorString.length == 0) {
                        G.AuthorString = new String[] {"Author"};
                    }

                    G.FilterPanel.remove(G.Author);
                    G.Author = new JComboBox<>(G.AuthorString);
                    G.FilterPanel.add(G.Author, 0);
                    G.BookList.setVisible(true);
                }
            }
        });
    }
    private void FilterButtonAction() {
        G.Filter.addActionListener(e -> {
            try {
                CheckName(G.BookName);
                CheckAuthor(G.Author);
                String[][] FilteredData = new String[11][];
                int j = 0;
                String Item = String.valueOf(G.Author.getSelectedItem());
                String Item1 =  G.BookName.getText();

                for (int i = 0; i < G.BookCount; i++) {
                    if (Item1.equals(G.Data[i][1]) || Item.equals(G.Data[i][0])) {
                        FilteredData[j] = G.Data[i];
                        j += 1;
                    }
                }

                String[] Columns_1 = {"Author", "Book", "Rating", "Availability"};
                G.FilteredModel = new DefaultTableModel(FilteredData, Columns_1);
                G.FilteredBooks = new JTable(G.FilteredModel);
                G.FilteredScrollPane = new JScrollPane(G.FilteredBooks);
                UIManager.put("OptionPane.minimumSize",new Dimension(800,300));
                JOptionPane.showMessageDialog(null, G.FilteredScrollPane, "Found", JOptionPane.PLAIN_MESSAGE);
            }
            catch(NameException | AuthorException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }
    private void SaveButtonAction() {
        G.Save.addActionListener(e -> {
            try {
                String FileFormat = "*.csv";
                FileDialog Save = new FileDialog(G.BookList, "Open file", FileDialog.SAVE);
                Save.setFile(FileFormat);
                Save.setVisible(true);
                String FileName = Save.getDirectory() + Save.getFile();
                if (!FileName.endsWith(".csv")){
                    FileName += ".csv";
                }
                if(Save.getFile() == null) return;
                BufferedWriter writer = new BufferedWriter (new FileWriter(FileName));
                for (int i = 0; i < G.Model.getRowCount(); i++){
                    for (int j = 0; j < G.Model.getColumnCount(); j++) {
                        writer.write ((String)G.Model.getValueAt(i, j));
                        if (j != G.Model.getColumnCount() - 1){
                            writer.write(",");
                        }
                    }
                    writer.write("\n");
                }
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }
    private void OpenButtonAction() {
        G.Open.addActionListener(e -> {
            try {
                String fileFormat = "*.csv";
                String line = "";
                String splitBy = ",";
                FileDialog load = new FileDialog(G.BookList, "Open file", FileDialog.LOAD);
                load.setFile(fileFormat);
                load.setVisible(true);
                String fileName = load.getDirectory() + load.getFile();
                if(load.getFile() == null) return;
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                int rows = G.Model.getRowCount();
                for (int i = 0; i < rows; i++) G.Model.removeRow(0);
                while(line != null) {
                    line = reader.readLine();
                    if(line != null) {
                        String[] lineToString = line.split(splitBy);
                        G.Model.addRow(lineToString);
                    }
                }
                reader.close();
            }
            catch (IOException ex) {ex.printStackTrace();}
        });
    }
}
