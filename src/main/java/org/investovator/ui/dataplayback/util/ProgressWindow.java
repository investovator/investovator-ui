package org.investovator.ui.dataplayback.util;

import com.vaadin.ui.*;
import org.investovator.controller.utils.events.GameCreationProgressChanged;
import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.commons.events.GameEventListener;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ProgressWindow extends Window implements GameEventListener {


    private ProgressBar progressBar;

    public ProgressWindow(String caption) {
        super(caption);

        VerticalLayout content = new VerticalLayout();

        progressBar = new ProgressBar(new Float(0.0));
        progressBar.setWidth("150px");
        progressBar.setEnabled(true);
        progressBar.setIndeterminate(false);
        progressBar.setStyleName("v-progressbar");

        content.addComponent(progressBar);
        content.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);


        this.setContent(content);
        this.center();
        this.setResizable(false);
        this.setModal(true);
        this.setClosable(false);
    }

    @Override
    public void eventOccurred(GameEvent event) {

        if (event instanceof GameCreationProgressChanged){
            synchronized (UI.getCurrent()){
                progressBar.setValue(((GameCreationProgressChanged) event).getProgress());
                if(((GameCreationProgressChanged) event).getProgress() == 1) this.close();
            }
        }

    }
}
