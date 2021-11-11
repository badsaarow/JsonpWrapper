package com.threecolors.jsonwrapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Resource which has only one representation.
 * 
 */
public class PojoResource extends ServerResource {

    @Get
    public String represent() {
        Form frm =  this.getRequest().getResourceRef().getQueryAsForm();
        String jsonStr = frm.getFirstValue("json");
        if ( jsonStr.isEmpty() ) {
            this.getResponse().setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
            return "json is empty";
        }
        
        String output = "";
        try {
            output = generatePojo(jsonStr);
            this.getResponse().setStatus(Status.SUCCESS_OK);
        } catch (Exception e) {
            this.getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            output = e.getMessage();
        }
        
        return output;
    }

    public static String generatePojo(String jsonStr) throws Exception{
        StringBuilder builder = new StringBuilder();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonStr);
            String tableName = node.get("dataTableNm").textValue();
            JsonNode nodeArray = node.path("rows");
            if ( nodeArray.isArray() ) {
                //@DatabaseTable(tableName = "VW_VITAL")
                //public class VwAdvancedDirective {
                builder.append("@DatabaseTable(tableName = \"" + tableName + "\")\n");
                builder.append("public class " + convertCamelCase(tableName) + " {\n");
                JsonNode nodeRow = nodeArray.get(0);
                Iterator<String> fieldNames = nodeRow.fieldNames();
                while (fieldNames.hasNext()) {
                    //@SerializedName("VISIT_NO")
                    //@DatabaseField(columnName = "VISIT_NO", canBeNull = true)
                    //private java.lang.String visitNo;
                    String fieldName = fieldNames.next();
                    builder.append("@SerializedName(\"" + fieldName + "\")\n");
                    builder.append("@DatabaseField(columnName = \"" + fieldName + "\", canBeNull = true)\n");
                    builder.append("private java.lang.String " + convertCamelCase(fieldName) + ";\n");
                    builder.append("\n");
                }
                //}
                builder.append("}");
            }
        } catch (ResourceException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        
        return builder.toString();
    }

    public static String convertCamelCase(String strdata) {
        StringBuilder strbufCamelCase = new StringBuilder();
        StringTokenizer st = new StringTokenizer(strdata, "_");
        if (null == strdata || strdata.length() == 0) {
            return "";
        }

        boolean isFirst = true;
        while (st.hasMoreTokens()) {
            String strWord = st.nextToken();
            if ( isFirst ) {
                strbufCamelCase.append(strWord.toLowerCase());
                isFirst = false;
            } else {
                strbufCamelCase.append(strWord.substring(0, 1).toUpperCase());
                if (strWord.length() > 1) {
                    strbufCamelCase.append(strWord.substring(1).toLowerCase());
                }
            }
        }

        return strbufCamelCase.toString();
    }
}
