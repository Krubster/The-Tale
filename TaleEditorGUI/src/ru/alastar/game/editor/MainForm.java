package ru.alastar.game.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ru.alastar.game.Action;
import ru.alastar.game.Page;

public class MainForm {

	protected Shell shlTaleEditor;
	private Text text;
	public static ArrayList<Page> pages = new ArrayList<Page>();
	static List pagesList;
	static List actionsList;
	static int selected = 0;
	static File selectedFile;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainForm window = new MainForm();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlTaleEditor.open();
		shlTaleEditor.layout();
		while (!shlTaleEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTaleEditor = new Shell();
		shlTaleEditor.setSize(658, 557);
		shlTaleEditor.setText("Tale Editor");
		text = new Text(shlTaleEditor, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text.setBounds(10, 10, 479, 504);

		Menu menu = new Menu(shlTaleEditor);
		menu.setLocation(new Point(0, 0));
		shlTaleEditor.setMenu(menu);

		MenuItem mntmNew = new MenuItem(menu, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
              selectedFile = new File("pagesData");
              try {
				selectedFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			}
		});
		mntmNew.setText("New...");

		MenuItem mntmOpen = new MenuItem(menu, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(shlTaleEditor,  SWT.OPEN  );
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null) return;
				selectedFile = new File(path);
				LoadPagesData(selectedFile);
			}
		});
		mntmOpen.setText("Open...");

		MenuItem mntmSave = new MenuItem(menu, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedFile != null)
				{
						try {

							FileOutputStream fos = new FileOutputStream(selectedFile);
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							oos.writeObject(pages);

							oos.flush();
							oos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					
				}
				else
		              CreateSaveDialog();
			}
		});
		mntmSave.setText("Save...");

		MenuItem mntmSaveAs = new MenuItem(menu, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
              CreateSaveDialog();
			}
		});
		mntmSaveAs.setText("Save As...");

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setText("Exit");

		Label lblActions = new Label(shlTaleEditor, SWT.NONE);
		lblActions.setBounds(495, 47, 55, 15);
		lblActions.setText("Actions:");

		actionsList = new List(shlTaleEditor, SWT.BORDER | SWT.V_SCROLL);
		actionsList.setBounds(495, 68, 137, 134);

		Button btnAddAction = new Button(shlTaleEditor, SWT.NONE);
		btnAddAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected < pages.size()) {
					if (pages.get(selected) != null) {
						new AddAction(shlTaleEditor, 0).open();
					}
				}
			}
		});
		btnAddAction.setBounds(495, 208, 137, 25);
		btnAddAction.setText("Add Action...");

		Button btnRemoveAction = new Button(shlTaleEditor, SWT.NONE);
		btnRemoveAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected < pages.size()) {
					Page p = pages.get(selected);
					if (p != null) {
						if (actionsList.getItemCount() != 0) 
						actionsList.remove(actionsList.getSelectionIndex());
						if (actionsList.getItemCount() != 0) {

							Action[] actions = new Action[p.actions.length - 1];
							for (int i = 0; i < actions.length; ++i) {
								actions[i] = new Action(actionsList.getItem(i)
										.split("<=>")[0], Integer
										.parseInt(actionsList.getItem(i).split(
												"<=>")[1].trim()));
							}
							pages.get(selected).actions = actions;
						} else
							pages.get(selected).actions = new Action[0];

					}
				}
			}
		});
		btnRemoveAction.setBounds(495, 239, 137, 25);
		btnRemoveAction.setText("Remove Action");

		Label lblPages = new Label(shlTaleEditor, SWT.NONE);
		lblPages.setBounds(495, 270, 55, 15);
		lblPages.setText("Pages:");

		pagesList = new List(shlTaleEditor, SWT.BORDER | SWT.V_SCROLL);
		pagesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selected = pagesList.getSelectionIndex();
				FocusPage(selected);
			}
		});
		pagesList.setBounds(495, 291, 137, 161);

		Button btnAddPage = new Button(shlTaleEditor, SWT.NONE);
		btnAddPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String s = text.getText();
				pages.add(new Page(s, new Action[0]));
				if (s.length() > 10)
					s = s.substring(0, 10);
				pagesList.add(s);
				text.setText("");
				actionsList.removeAll();
				RefreshPagesList();
			}
		});
		btnAddPage.setBounds(495, 458, 137, 25);
		btnAddPage.setText("Add Page...");

		Button btnRemovePage = new Button(shlTaleEditor, SWT.NONE);
		btnRemovePage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected < pages.size()) {
					if (pages.get(selected) != null) {
						pages.remove(selected);
						RefreshPagesList();
						RefreshActionsList(selected);
					}
				}
			}
		});
		btnRemovePage.setBounds(495, 489, 137, 25);
		btnRemovePage.setText("Remove Page");
		
		Button btnNewButton = new Button(shlTaleEditor, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selected < pages.size())
				{
					if(pages.get(selected) != null)
					{
						pages.get(selected).text = text.getText();
						RefreshPagesList();
					}
				}
			}
		});
		btnNewButton.setBounds(495, 10, 137, 25);
		btnNewButton.setText("Save Page");

	}

	protected void CreateSaveDialog() {
		JFileChooser chooser = new JFileChooser();
		int retrival = chooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {

				FileOutputStream fos = new FileOutputStream(chooser
						.getSelectedFile());
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(pages);

				oos.flush();
				oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
	}

	protected Action[] FormActionsFromList() {
		Action[] actions = new Action[actionsList.getItemCount()];
		for (int i = 0; i < actions.length; ++i) {
			actions[i] = new Action(actionsList.getItem(i).split("<=>")[0],
					Integer.parseInt(actionsList.getItem(i).split("<=>")[1]
							.trim()));
		}
		return actions;
	}

	private void FocusPage(int selectionIndex) {
		if(selectionIndex >= 0){
		text.setText(pages.get(selectionIndex).text);
		RefreshActionsList(selectionIndex);
		}
		else
		{
			text.setText("");
			actionsList.removeAll();
		}
	}

	private void RefreshActionsList(int index) {
		actionsList.removeAll();
		if (index < pages.size()) {
			if (pages.get(index) != null) {
				for (Action act : pages.get(index).actions) {
					actionsList.add(act.name + "<=>" + act.sendTo);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void LoadPagesData(File selectedFile) {
		FileInputStream f_in;
		try {
			f_in = new FileInputStream(selectedFile);

			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			Object obj = obj_in.readObject();

			if (obj instanceof ArrayList<?>) {
				this.pages = (ArrayList<Page>) obj;
			}
			obj_in.close();
			f_in.close();
			RefreshPagesList();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void RefreshPagesList() {
		pagesList.removeAll();
		int i = 0;
		for (Page page : pages) {
			pagesList.add(i + ")" + page.text);
			++i;
		}
	}

	public static void AddAction(String text2, String text3) {
		actionsList.add(text2 + "<=>" + text3);
		Action[] act = new Action[pages.get(selected).actions.length + 1];
		for (int i = 0; i < pages.get(selected).actions.length + 1; ++i) {
			act[i] = new Action(actionsList.getItem(i).split("<=>")[0],
					Integer.parseInt(actionsList.getItem(i).split("<=>")[1]
							.trim()));
		}
		pages.get(selected).actions = act;
	}

}
