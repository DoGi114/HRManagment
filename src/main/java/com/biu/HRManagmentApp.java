package com.biu;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
/**
 *
 */
@Theme("tests-valo-metro")
@Title("HR managment app")
public class HRManagmentApp extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table workersTable = new Table();
	private Button add = new Button("Add worker");
	private Button remove = new Button("Remove worker");
	private TextField search = new TextField();
	private FormLayout formLayout = new FormLayout();
	private FieldGroup formFields = new FieldGroup();
	
    IndexedContainer contactContainer = createDefaultData();

    private static final String Name = "Name";
    private static final String Surname = "Surname";
    private static final String Department = "Department";
    private static final String Pension = "Pension";
    
    private static final String[] fieldsNames = new String[] {Name, Surname, Department, Pension};
    
    private class ContactFilter implements Filter {
        private String needle;

        public ContactFilter(String needle) {
                this.needle = needle.toLowerCase();
        }

        public boolean passesFilter(Object itemId, Item item) {
                String haystack = ("" + item.getItemProperty(Name).getValue()
                                + item.getItemProperty(Surname).getValue() + item.getItemProperty(Department).getValue() + item.getItemProperty(Pension).getValue() ).toLowerCase();
                return haystack.contains(needle);
        }

        public boolean appliesToProperty(Object id) {
                return true;
        }
}
	/*
    class intValidator implements Validator {

	    @Override
	    public void validate(Object value) throws InvalidValueException {
	    	Double tmp = null;
	    	tmp.parseDouble((String) value);
	        if (value instanceof Double) {
	            Notification.show("Value you want to submite as pension was correct");

	        } else {
	            Notification.show("Value you want to submite as pension has to be a number");
	        }
	    }

	    public boolean isInt(String s) {
	        try { 
	            Integer.parseInt(s); 
	        } catch(NumberFormatException e) { 
	            return false; 
	        }
	        // only got here if we didn't return false
	        return true;
	    }
	}
    */
	
    @Override
	protected void init(VaadinRequest request) {
		initLayout();
		initData();
		initForm();
		initListeners();
		initValidators();
		initSearch();
	}
	
	private void initLayout(){
		HorizontalSplitPanel container = new HorizontalSplitPanel();
		VerticalLayout leftPanel = new VerticalLayout();
		VerticalLayout rightPanel = new VerticalLayout();
		HorizontalLayout topLeftPanel = new HorizontalLayout();
		
		topLeftPanel.addComponent(search);
		topLeftPanel.addComponent(add);
		leftPanel.addComponent(topLeftPanel);
		leftPanel.addComponent(workersTable);
		leftPanel.setSizeFull();
		leftPanel.setExpandRatio(workersTable, 1);

		workersTable.setSizeFull();
		topLeftPanel.setWidth("100%");
		search.setWidth("100%");
		topLeftPanel.setExpandRatio(search, 1);
		rightPanel.addComponent(formLayout);
		container.addComponent(leftPanel);
		container.addComponent(rightPanel);
		rightPanel.setExpandRatio(formLayout, 1);
		formLayout.setVisible(false);
		setContent(container);
		
		
	}
	
	private void initForm(){
		for(String fields : fieldsNames ){
			TextField field = new TextField(fields);
			formLayout.addComponent(field);
            field.setWidth("100%");
            if(fields == "Name"){
            	//field.setRequired(true);
            	field.setInputPrompt("Please insert name");

            }
            if(fields == "Surname"){
            	//field.setRequired(true);
            	field.setInputPrompt("Please insert surname");

            }
            if(fields == "Department"){
            	//field.setRequired(true);
            	field.setInputPrompt("Please insert department");

            }
            if(fields == "Pension"){
            	
            	//field.setRequired(true);
            	//field.setInvalidAllowed(false);
            	field.setInputPrompt("Please insert number pension in number");
            	//field.addValidator(new intValidator());
            	//field.addValidator(new String);
            }

            formFields.bind(field, fields);
		}
		
		formLayout.addComponent(remove);
		
		formFields.setBuffered(false);
		
	}
	
	private void initSearch(){
		
		 search.setInputPrompt("Search contacts");

		 search.setTextChangeEventMode(TextChangeEventMode.LAZY);
	
	}
	
	
	private void initData(){

		workersTable.setContainerDataSource(contactContainer);
		workersTable.setVisibleColumns(new String[] { Name, Surname, Department, Pension });
		workersTable.setSelectable(true);
		workersTable.setImmediate(true);

		workersTable.addValueChangeListener(new Property.ValueChangeListener() {
                    public void valueChange(ValueChangeEvent event) {
                            Object contactId = workersTable.getValue();

                            formFields.setItemDataSource(workersTable
                                                    .getItem(contactId));

                            formLayout.setVisible(contactId != null);
                    }
            });
		
	}

	private void initListeners(){
		
		add.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 contactContainer.removeAllContainerFilters();
                 Object contactId = contactContainer.addItemAt(0);

                 workersTable.getContainerProperty(contactId, Name);
                 workersTable.getContainerProperty(contactId, Surname);
                 

                 workersTable.select(contactId);
                 Notification.show("Person added, now fill the empty spaces as required");

				
			}
		});
		
		remove.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                    Object contactId = workersTable.getValue();
                    workersTable.removeItem(contactId);
                    Notification.show("Person removed");

            }
    });
		
		search.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
                Notification.show("Enter name or surname to search");				
			}
			
		}
		);
		
		search.addTextChangeListener(new TextChangeListener() {
            public void textChange(final TextChangeEvent event) {

                    contactContainer.removeAllContainerFilters();
                    contactContainer.addContainerFilter(new ContactFilter(event
                                    .getText()));
             }
     });
	}

	private void initValidators(){
		
		
	}
	
	private static IndexedContainer createDefaultData() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldsNames) {
                ic.addContainerProperty(p, String.class, "");
        }

        String[] names = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
                        "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
                        "Lisa", "Marge" };
        String[] surnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
                        "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
                        "Barks", "Ross", "Schneider", "Tate" };
        String[] departments = {"Managment", "HR", "PR", "Storekeeper", "Shop-Assistant", "Boss", "Accountant", "Lawyer", "Secretary" };
        String[] pensions = {"2500", "1200", "10000", "4000", "3500", "2700"};
        for (int i = 0; i < 50; i++) {
                Object id = ic.addItem();
                ic.getContainerProperty(id, Name).setValue(
                                names[(int) (names.length * Math.random())]);
                ic.getContainerProperty(id, Surname).setValue(
                                surnames[(int) (surnames.length * Math.random())]);
                ic.getContainerProperty(id, Department).setValue(
                        departments[(int) (departments.length * Math.random())]);
                ic.getContainerProperty(id, Pension).setValue(
                        pensions[(int) (pensions.length * Math.random())]);
        }

        return ic;
}
}
