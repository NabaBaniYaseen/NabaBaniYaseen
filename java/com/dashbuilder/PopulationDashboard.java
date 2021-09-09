package com.dashbuilder;

import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.displayer.DisplayerSettings;
import org.dashbuilder.dsl.serialization.DashboardExporter;
import org.dashbuilder.dsl.serialization.DashboardExporter.ExportType;
import static java.util.Arrays.asList;
import static org.dashbuilder.dataset.DataSetFactory.newDataSetBuilder;
import static org.dashbuilder.displayer.DisplayerSettingsFactory.newBarChartSettings;
import static org.dashbuilder.dsl.factory.component.ComponentFactory.displayer;
import static org.dashbuilder.dsl.factory.dashboard.DashboardFactory.dashboard;
import static org.dashbuilder.dsl.factory.page.PageFactory.page;
import static org.dashbuilder.dsl.factory.page.PageFactory.row;
public class PopulationDashboard {
	public static void main(String[] args) {
		var dataSet =newDataSetBuilder().column("Country", ColumnType.LABEL)
        .column("Population", ColumnType.NUMBER)
        .row("China", "1439323776")
        .row("India", "1380004385")
        .row("United States", "331002651")
        .row("Indonesia", "273523615")
        .row("Pakistan", "220892340")
        .row("Brazil", "212559417")
        .row("Nigeria", "206139589")
        .row("Bangladesh", "164689383")
        .row("Russia", "145934462")
        .row("Mexico", "128932753")
        .buildDataSet();
		 // a bar chart configuration that is used to visualize the data set
	    var popBarChart = newBarChartSettings().subType_Column()
	                                           .width(800)
	                                           .height(600)
	                                           .dataset(dataSet)
	                                           .column("Country")
	                                           .column("Population")
	                                           .buildSettings();
	    // a page with a HTML header and the displayer that uses the bar chart configuration                                               
	    var page = page("Countries Population",
	                    row("<h3> Countries Population</h3>"),
	                    row(displayer(popBarChart)));
	    // finally exports the dashboard to a local ZIP file
	    DashboardExporter.get().export(dashboard(asList(page)),
	                                   "population.zip",
	                                   ExportType.ZIP);
	}

	}
                                     
 
