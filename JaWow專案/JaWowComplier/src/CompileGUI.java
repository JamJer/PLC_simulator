import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;

import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CompileGUI {
	private JFrame frmJawowCompiler;
	private JTextPane textPaneCodeText = new JTextPane();
	private JTextPane textAreaInfoResult = new JTextPane();
	private StyledDocument doc = textAreaInfoResult.getStyledDocument();
	private SimpleAttributeSet keyWord = new SimpleAttributeSet();
	private SimpleAttributeSet keyWord2 = new SimpleAttributeSet();
	private SimpleAttributeSet keyWord3 = new SimpleAttributeSet();
	private JLabel lblPrintFileName = new JLabel("Not loaded any file..");
	private LinkedList<String> fileStrlls = null;
	private LinkedList<varValue> codeVarlls = null;
	private LexicalAnalyzer lexical_proc = null;
	private CodeGenerator codeGenerate_proc = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
			SyntheticaLookAndFeel.setFont("Dialog", 12);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompileGUI window = new CompileGUI();
					window.frmJawowCompiler.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CompileGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJawowCompiler = new JFrame();
		frmJawowCompiler.setTitle("JaWow Compiler");
		frmJawowCompiler.setBounds(100, 100, 824, 514);
		frmJawowCompiler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBold(keyWord, true);
		StyleConstants.setForeground(keyWord2, new Color(2, 255, 246));
		StyleConstants.setBold(keyWord2, true);
		StyleConstants.setForeground(keyWord3, new Color(234, 191, 112));
		StyleConstants.setBold(keyWord3, true);
		
		JPanel panelUpBound = new JPanel();
		frmJawowCompiler.getContentPane().add(panelUpBound, BorderLayout.NORTH);

		JButton btnOpenFileSource = new JButton("");
		btnOpenFileSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = getOpenFileDialog();
				if (selectedFile != null) {
					lblPrintFileName.setText(selectedFile.getName());
					lblPrintFileName.setForeground(Color.GREEN);
					try {
						String temp = "";
						fileStrlls = getLoadFileList(selectedFile);
						for (int i = 0; i < fileStrlls.size(); i++) {
							temp += fileStrlls.get(i) + "\r\n";
						}
						textPaneCodeText.setText(temp);
						showInfo("\r\nFile load successfully.", null);
						System.out.println(textPaneCodeText.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					showInfo("\r\nOpen file canceled.", null);
				}
			}
		});
		btnOpenFileSource.setIcon(new ImageIcon(
				CompileGUI.class.getResource("/de/javasoft/plaf/synthetica/standard/images/treeOpenIcon.png")));
		panelUpBound.add(btnOpenFileSource);

		JButton btnRun = new JButton("");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textPaneCodeText.getText().isEmpty()) {
					resetCodells(textPaneCodeText.getText());
					for (int i = 0; i < fileStrlls.size(); i++)
						System.out.println(fileStrlls.get(i));
					showInfo("\r\n[LEXEME]-Starting Lexical parsing....", null);
					lexical_proc = new LexicalAnalyzer();
					lexical_proc.doLex(fileStrlls);
					showInfo("\r\n[LEXEME]-Lexical parsing done....", null);
					showInfo("\r\n[LEXEME]-Lexical parsing result: ", null);
					if (lexical_proc.isLexSuccess()) {
						showInfo("SUCCESS", keyWord2);
						codeVarlls = lexical_proc.getVarValue();
						showInfo("\r\n[GENERATING]-Starting Code generating....", null);
						codeGenerate_proc = new CodeGenerator(fileStrlls, codeVarlls);
						showInfo("\r\n[GENERATING]-Executing Code....", null);
						codeGenerate_proc.RunCode();
						showInfo("\r\n[GENERATING]-Code execution done....", null);
						showInfo("\r\n[GENERATING]-Showing execution result:", null);
						showResult();
					} else {
						showInfo("ERROR", keyWord);
						showInfo("\r\n[LEXEME]-Lexical error messenge: ", keyWord);
						showErrorMssenge(lexical_proc.getErrorMessage(),"[LEXEME]");
					}
				} else {
					showInfo("\r\nLoad a empty code text. Please type something.", keyWord);
				}
			}
		});
		btnRun.setIcon(new ImageIcon("C:\\Users\\JAM\\Desktop\\right-arrow.png"));
		panelUpBound.add(btnRun);

		JLabel lblFileName = new JLabel("File Name:");
		panelUpBound.add(lblFileName);

		lblPrintFileName.setForeground(Color.RED);
		panelUpBound.add(lblPrintFileName);

		JScrollPane scrollPaneCodeText = new JScrollPane();
		scrollPaneCodeText.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		frmJawowCompiler.getContentPane().add(scrollPaneCodeText, BorderLayout.CENTER);
		textPaneCodeText.setFont(new Font("Dialog", Font.BOLD, 18));
		textPaneCodeText.setEditable(true);
		textPaneCodeText.setDocument(new ColorCodeText().getModifiedDocment());
		textPaneCodeText.setBackground(Color.BLACK);
		scrollPaneCodeText.setViewportView(textPaneCodeText);

		textAreaInfoResult.setBackground(Color.BLACK);
		textAreaInfoResult.setForeground(Color.GREEN);
		JScrollPane scrollPaneInfoResult = new JScrollPane();

		textAreaInfoResult
				.setText("Welcome to use.\r\nPlease type something in text field or import .txt file to compile.");
		scrollPaneInfoResult.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPaneInfoResult.setViewportView(textAreaInfoResult);
		frmJawowCompiler.getContentPane().add(scrollPaneInfoResult, BorderLayout.SOUTH);
	}

	private void showErrorMssenge(LinkedList<String> err,String logo){
		for(int i=0;i<err.size();i++)
			showInfo("\r\n"+logo+"-"+err.get(i), keyWord);
	}
	
	private void showResult() {
		for (int i = 0; i < codeVarlls.size(); i++){
			showInfo("\r\n[GENERATING]-VARIABLE: ", null);
			showInfo(codeVarlls.get(i).getVar(), keyWord3);
			showInfo(" VALUE:", null);
			showInfo(" "+codeVarlls.get(i).getValue(), keyWord3);
		}
	}

	private File getOpenFileDialog() {
		int result;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Choose a TXT file");
		fileChooser.addChoosableFileFilter(new fileFilter("txt", ".txt ¤å¦rÀÉ"));
		fileChooser.setAcceptAllFileFilterUsed(true);
		result = fileChooser.showOpenDialog(fileChooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Log: Selected file path " + selectedFile.getAbsolutePath());
			return selectedFile;
		} else {
			return null;
		}
	}

	private void resetCodells(String code) {
		String[] temp = code.split("\r\n");
		fileStrlls.clear();
		for (int i = 0; i < temp.length; i++) {
			fileStrlls.add(temp[i]);
		}
	}

	private void showInfo(String words, SimpleAttributeSet key) {
		try {
			doc.insertString(doc.getLength(), words, key);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private LinkedList<String> getLoadFileList(File inp) throws IOException {
		LinkedList<String> templls = new LinkedList<String>();
		BufferedReader bf = new BufferedReader(new FileReader(inp));
		while (bf.ready()) {
			templls.add(bf.readLine());
		}
		return templls;
	}
}
