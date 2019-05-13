import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FrameBuilder extends Frame {
    private JFrame frame;
    private JLabel fileLabel;
    private JTextField fileField;

    FrameBuilder(String name) {
        Font font = new Font("", Font.BOLD, 15);//
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(370,150);
        frame.setResizable(false);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        fileField = new JTextField(14);
        fileLabel = new JLabel("file direction");
        fileLabel.setFont(font);
        fileField.setFont(font);
        JButton parseBtn = new JButton("Parse");
        JButton searchBtn = new JButton("Search File");
        parseBtn.setFont(font);
        searchBtn.setFont(font);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(fileLabel);
        frame.getContentPane().add(fileField);
        frame.getContentPane().add(parseBtn);
        frame.getContentPane().add(searchBtn);
        parseBtn.addActionListener(new OnParseBtnClick());
        searchBtn.addActionListener(new OnFileChooserBtnClick());
        frame.setVisible(true);
    }

    class OnParseBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArticleParser.parseArticle(new File(fileField.getText()));
        }
    }

    class OnFileChooserBtnClick extends JFileChooser implements ActionListener{
        JFileChooser fc = new JFileChooser();
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(new FileChooserDemo());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileField.setText(fc.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    }
}
