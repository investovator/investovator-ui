package org.investovator.ui.dataplayback.util;

import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.investovator.controller.utils.events.GameCreationProgressChanged;
import org.investovator.controller.utils.events.GameEvent;
import org.investovator.controller.utils.events.GameEventListener;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class ProgressWindow extends Window implements GameEventListener {


    private ProgressBar progressBar;

    public ProgressWindow(String caption) {
        super(caption);

        progressBar = new ProgressBar(new Float(0.0));
        progressBar.setWidth("100px");
        progressBar.setEnabled(true);
        progressBar.setIndeterminate(false);
        this.setContent(progressBar);
    }

    @Override
    public void eventOccurred(GameEvent event) {

        if (event instanceof GameCreationProgressChanged){
            synchronized (UI.getCurrent()){
                progressBar.setValue(((GameCreationProgressChanged) event).getProgress());
            }
        }

    }
}
