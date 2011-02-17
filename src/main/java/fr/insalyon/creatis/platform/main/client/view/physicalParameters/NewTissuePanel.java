/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.platform.main.client.view.physicalParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import fr.insalyon.creatis.platform.main.client.bean.Tissue;
import fr.insalyon.creatis.platform.main.client.rpc.TissueService;
import fr.insalyon.creatis.platform.main.client.rpc.TissueServiceAsync;
import fr.insalyon.creatis.platform.main.client.view.common.FieldUtil;
import fr.insalyon.creatis.platform.main.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author glatard
 */
public class NewTissuePanel extends Panel {
    private TissuePanel leftPanel;
    private static NewTissuePanel instance = null;
    
    public static NewTissuePanel getInstance(TissuePanel t){
        if(instance == null)
                instance = new NewTissuePanel("New Tissue",t);
        return instance;
    }


    private NewTissuePanel(String title, TissuePanel leftPanel){
        super(title);
    this.leftPanel = leftPanel;
        this.setLayout(new VerticalLayout());
        this.setBorder(false);
        this.setId("tissue-formpanel");
        this.setHeight(200);
        this.setWidth(600);

        FieldSet tissueFields = new FieldSet("Tissue information");
        tissueFields.setId("tissue-fieldset");
        tissueFields.setHeight(100);
        tissueFields.setWidth(450);

        final TextField tissueNameField = FieldUtil.getTextField("tissue-name", 300, "Name", false);
        tissueNameField.setLayoutData(new RowLayout());
        tissueNameField.setDisabled(false);
        //tissueNameField.setHeight(50);
        tissueFields.add(tissueNameField);

        this.add(tissueFields);

        Panel tissueControl = new Panel();
        tissueControl.setHeight(25);
        tissueControl.setWidth(300);
        tissueControl.setBorder(false);

         //save and delete buttons
        Button save = new Button("Save",  new ButtonListenerAdapter(){
             @Override
            public void onClick(Button button, EventObject e) {

                 //save tissue
                String tissueName = tissueNameField.getValueAsString();

                if (tissueName.trim().equals("")) {
                    MessageBox.alert("Error", "You should provide a tissue name.");
                    return;
                }
                saveTissue(new Tissue(tissueName));
            }
        });
        tissueControl.addButton(save);
       
         this.add(tissueControl);

    }

       private void saveTissue(final Tissue t){
        TissueServiceAsync ts = TissueService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>(){

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error","Cannot save tissue "+t.getTissueName()+" ("+t.getTissueName()+")");
            }

            public void onSuccess(Void result) {
                MessageBox.alert("Tissue update", "Tissue list was updated");
                leftPanel.loadTissues();
            }
        };
            ts.addTissue(t, callback);
    }

    
}
