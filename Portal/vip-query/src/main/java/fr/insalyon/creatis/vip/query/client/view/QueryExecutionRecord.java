/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author nouha
 */
public class QueryExecutionRecord extends ListGridRecord {

    public QueryExecutionRecord(String ExecutionID, String name, String queryName, String version, String executer, String dateExecution, String status,String statuss, String bodyResult,String pathFileResult,String dateEndExecution) {
        setAttribute("statusIcon", status);
        setAttribute("queryExecutionID", ExecutionID);
        setAttribute("name", name);
        setAttribute("query", queryName);
        setAttribute("version", "v." + version);
        setAttribute("executer", executer);
        setAttribute("dateExecution", dateExecution);
        setAttribute("status", status);
        setAttribute("statusFormatter", statuss);
        setAttribute("bodyResult", bodyResult);
        setAttribute("pathFileResult", pathFileResult);
        setAttribute("dateEndExecution", dateEndExecution);


    }
}