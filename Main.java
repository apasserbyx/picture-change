import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageConverterGUI extends JFrame {

    private JTextField inputPathField;
    private JComboBox<String> formatComboBox;

    public ImageConverterGUI() {
        setTitle("图片格式转换器");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // 输入文件选择
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPathField = new JTextField();
        JButton inputButton = new JButton("选择输入图片");
        inputButton.addActionListener(e -> selectFile(inputPathField));
        inputPanel.add(inputPathField, BorderLayout.CENTER);
        inputPanel.add(inputButton, BorderLayout.EAST);
        add(inputPanel);

        // 格式选择
        JPanel formatPanel = new JPanel(new FlowLayout());
        formatPanel.add(new JLabel("选择格式:"));
        formatComboBox = new JComboBox<>(new String[]{"jpg", "png", "bmp"});
        formatPanel.add(formatComboBox);
        add(formatPanel);

        // 转换按钮
        JButton convertButton = new JButton("转换");
        convertButton.addActionListener(new ConvertActionListener());
        add(convertButton);
    }

    private void selectFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private class ConvertActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputPath = inputPathField.getText();
            String formatName = (String) formatComboBox.getSelectedItem();

            if (inputPath.isEmpty() || formatName == null) {
                JOptionPane.showMessageDialog(ImageConverterGUI.this, "请选择输入文件和格式。", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                File inputFile = new File(inputPath);
                BufferedImage inputImage = ImageIO.read(inputFile);
                if (inputImage == null) {
                    throw new IOException("无效的输入图片文件。");
                }

                // 生成输出文件名
                String outputPath = generateOutputFileName(inputFile, formatName);

                // 转换并保存图片
                boolean result = ImageIO.write(inputImage, formatName, new File(outputPath));
                if (!result) {
                    throw new IOException("无法以指定格式保存图片。");
                }
                JOptionPane.showMessageDialog(ImageConverterGUI.this, "图片转换成功！\n保存路径: " + outputPath, "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ImageConverterGUI.this, "错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String generateOutputFileName(File inputFile, String formatName) {
            String inputFileName = inputFile.getName();
            int dotIndex = inputFileName.lastIndexOf('.');
            String baseName = (dotIndex == -1) ? inputFileName : inputFileName.substring(0, dotIndex);
            return inputFile.getParent() + File.separator + baseName + "_converted." + formatName;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageConverterGUI converterGUI = new ImageConverterGUI();
            converterGUI.setVisible(true);
        });
    }
}
