package ru.alastar.game.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;



public class AddAction extends Dialog {

	protected Object result;
	static Shell shlAddAction;
	static Group grpAddAction;
	private Text txtActionName;
	private Text txtSendsTo;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddAction(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAddAction.open();
		shlAddAction.layout();
		Display display = getParent().getDisplay();
		while (!shlAddAction.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAddAction = new Shell(getParent(), getStyle());
		shlAddAction.setSize(201, 271);
		shlAddAction.setText("Add Action...");
		grpAddAction = new Group(shlAddAction, SWT.NONE);
		grpAddAction.setBounds(0, 10, 195, 222);
		grpAddAction.setText("Add Action");
		
		Label lblName = new Label(grpAddAction, SWT.NONE);
		lblName.setBounds(20, 24, 55, 15);
		lblName.setText("Name:");
		
		txtActionName = new Text(grpAddAction, SWT.BORDER);
		txtActionName.setBounds(20, 42, 163, 21);
		
		Label lblSendsTo = new Label(grpAddAction, SWT.NONE);
		lblSendsTo.setBounds(20, 69, 55, 15);
		lblSendsTo.setText("Sends To:");
		
		txtSendsTo = new Text(grpAddAction, SWT.BORDER);
		txtSendsTo.addVerifyListener(new VerifyListener() {

	        @Override
	        public void verifyText(VerifyEvent e) {

	            Text text = (Text)e.getSource();

	            final String oldS = text.getText();
	            String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

	            boolean isInt = true;
	            try
	            {
	                Integer.parseInt(newS);
	            }
	            catch(NumberFormatException ex)
	            {
	                isInt = false;
	            }

	            System.out.println(newS);

	            if(!isInt)
	                e.doit = false;
	        }
	    });
		txtSendsTo.setBounds(20, 88, 163, 21);
		
		Button btnAdd = new Button(grpAddAction, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!txtActionName.getText().isEmpty() && !txtSendsTo.getText().isEmpty())
				MainForm.AddAction(txtActionName.getText(), txtSendsTo.getText());
				
				AddAction.shlAddAction.close();
			}
		});
		btnAdd.setBounds(20, 115, 163, 48);
		btnAdd.setText("Add...");
		
		Button btnBack = new Button(grpAddAction, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddAction.shlAddAction.close();
			}
		});
		btnBack.setBounds(20, 169, 163, 43);
		btnBack.setText("Back");
	}

}
