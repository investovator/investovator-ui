package org.investovator.ui.agentgaming;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import net.sourceforge.jabm.report.Report;
import org.investovator.jasa.api.JASAFacade;
import org.investovator.jasa.api.MarketFacade;
import org.investovator.jasa.multiasset.simulation.HeadlessMultiAssetSimulationManager;
import org.springframework.core.env.Environment;

/**
 * @author Amila Surendra
 * @version $Revision
 */
public class DashboardPlayingView extends Panel {

    VerticalLayout content;

   MarketFacade simulationFacade = JASAFacade.getMarketFacade();
    Report[] reports;

    String mainXmlPath;

    boolean simulationRunning = false;


    public DashboardPlayingView() {
        content = new VerticalLayout();
        content.setSizeFull();

        Button test = new Button("Start");
        Button stop  = new Button("Stop");

        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //testing

                System.out.println(System.getProperty("jabm.config"));


                simulationFacade = JASAFacade.getMarketFacade();
                simulationFacade.startSimulation();
                simulationRunning=true;

            }
        });

        stop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                simulationFacade.terminateSimulation();
            }
        });

        content.addComponent(test);
        content.addComponent(stop);

        this.setSizeFull();

        this.setContent(content);




    }







}
